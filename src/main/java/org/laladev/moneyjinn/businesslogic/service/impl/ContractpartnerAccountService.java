package org.laladev.moneyjinn.businesslogic.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;

import org.laladev.moneyjinn.businesslogic.dao.ContractpartnerAccountDao;
import org.laladev.moneyjinn.businesslogic.dao.data.BankAccountData;
import org.laladev.moneyjinn.businesslogic.dao.data.ContractpartnerAccountData;
import org.laladev.moneyjinn.businesslogic.dao.data.mapper.BankAccountDataMapper;
import org.laladev.moneyjinn.businesslogic.dao.data.mapper.ContractpartnerAccountDataMapper;
import org.laladev.moneyjinn.businesslogic.model.BankAccount;
import org.laladev.moneyjinn.businesslogic.model.Contractpartner;
import org.laladev.moneyjinn.businesslogic.model.ContractpartnerAccount;
import org.laladev.moneyjinn.businesslogic.model.ContractpartnerAccountID;
import org.laladev.moneyjinn.businesslogic.model.ContractpartnerID;
import org.laladev.moneyjinn.businesslogic.model.access.UserID;
import org.laladev.moneyjinn.businesslogic.model.exception.BusinessException;
import org.laladev.moneyjinn.businesslogic.model.validation.ValidationResult;
import org.laladev.moneyjinn.businesslogic.model.validation.ValidationResultItem;
import org.laladev.moneyjinn.businesslogic.service.CacheNames;
import org.laladev.moneyjinn.businesslogic.service.api.IContractpartnerAccountService;
import org.laladev.moneyjinn.businesslogic.service.api.IContractpartnerService;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.interceptor.SimpleKey;
import org.springframework.util.Assert;

@Named
public class ContractpartnerAccountService extends AbstractService implements IContractpartnerAccountService {

	@Inject
	private IContractpartnerService contractpartnerService;

	@Inject
	private ContractpartnerAccountDao contractpartnerAccountDao;

	@Override
	protected void addBeanMapper() {
		super.registerBeanMapper(new ContractpartnerAccountDataMapper());
		super.registerBeanMapper(new BankAccountDataMapper());
	}

	@Override
	public ValidationResult validateContractpartnerAccount(final UserID userId,
			final ContractpartnerAccount contractpartnerAccount) {
		final ValidationResult validationResult = new ValidationResult();

		if (contractpartnerAccount.getBankAccount() == null) {
			validationResult.addValidationResultItem(new ValidationResultItem(contractpartnerAccount.getId(),
					ErrorCode.BANK_CODE_CONTAINS_ILLEGAL_CHARS_OR_IS_EMPTY));
			validationResult.addValidationResultItem(new ValidationResultItem(contractpartnerAccount.getId(),
					ErrorCode.ACCOUNT_NUMBER_CONTAINS_ILLEGAL_CHARS_OR_IS_EMPTY));
		} else {
			for (final ErrorCode errorCode : contractpartnerAccount.getBankAccount().checkValidity()) {
				validationResult
						.addValidationResultItem(new ValidationResultItem(contractpartnerAccount.getId(), errorCode));
			}

			final ContractpartnerAccount contractpartnerAccountChecked = this
					.getContractpartnerAccountByBankAccount(userId, contractpartnerAccount.getBankAccount());
			if (contractpartnerAccountChecked != null && (contractpartnerAccount.getId() == null
					|| !contractpartnerAccountChecked.getId().equals(contractpartnerAccount.getId()))) {
				validationResult.addValidationResultItem(new ValidationResultItem(contractpartnerAccount.getId(),
						ErrorCode.ACCOUNT_ALREADY_ASSIGNED_TO_OTHER_PARTNER,
						Arrays.asList(contractpartnerAccountChecked.getContractpartner().getName())));
			}
		}

		if (contractpartnerAccount.getContractpartner() == null) {
			validationResult.addValidationResultItem(
					new ValidationResultItem(contractpartnerAccount.getId(), ErrorCode.CONTRACTPARTNER_IS_NOT_SET));
		} else {
			final Contractpartner contractpartner = this.contractpartnerService.getContractpartnerById(userId,
					contractpartnerAccount.getContractpartner().getId());
			if (contractpartner == null) {
				validationResult.addValidationResultItem(new ValidationResultItem(contractpartnerAccount.getId(),
						ErrorCode.CONTRACTPARTNER_DOES_NOT_EXIST));
			}
		}

		return validationResult;
	}

	private final ContractpartnerAccount mapContractpartnerAccountData(final UserID userId,
			final ContractpartnerAccountData contractpartnerAccountData) {
		if (contractpartnerAccountData != null) {
			final ContractpartnerAccount contractpartnerAccount = super.map(contractpartnerAccountData,
					ContractpartnerAccount.class);
			final Contractpartner contractpartner = this.contractpartnerService.getContractpartnerById(userId,
					contractpartnerAccount.getContractpartner().getId());
			// this secures the Account - a user which has no access to the partner may not modify
			// its accounts
			if (contractpartner != null) {
				contractpartnerAccount.setContractpartner(contractpartner);
				return contractpartnerAccount;
			}
		}
		return null;

	}

	private final List<ContractpartnerAccount> mapContractpartnerAccountDataList(final UserID userId,
			final List<ContractpartnerAccountData> contractpartnerAccountDataList) {
		return contractpartnerAccountDataList.stream()
				.map(element -> this.mapContractpartnerAccountData(userId, element))
				.collect(Collectors.toCollection(ArrayList::new));
	}

	private ContractpartnerAccount getContractpartnerAccountByBankAccount(final UserID userId,
			final BankAccount bankAccount) {
		Assert.notNull(userId);
		Assert.notNull(bankAccount);
		final ContractpartnerAccountData contractpartnerAccountData = this.contractpartnerAccountDao
				.getContractpartnerAccountByBankAccount(userId.getId(), bankAccount.getBankCode(),
						bankAccount.getAccountNumber());
		return this.mapContractpartnerAccountData(userId, contractpartnerAccountData);
	}

	@Override
	@Cacheable(CacheNames.CONTRACTPARTNER_ACCOUNT_BY_ID)
	public ContractpartnerAccount getContractpartnerAccountById(final UserID userId,
			final ContractpartnerAccountID contractpartnerAccountId) {
		Assert.notNull(userId);
		Assert.notNull(contractpartnerAccountId);
		final ContractpartnerAccountData contractpartnerAccountData = this.contractpartnerAccountDao
				.getContractpartnerAccountById(userId.getId(), contractpartnerAccountId.getId());
		return this.mapContractpartnerAccountData(userId, contractpartnerAccountData);
	}

	@Override
	@Cacheable(CacheNames.CONTRACTPARTNER_ACCOUNTS_BY_PARTNER)
	public List<ContractpartnerAccount> getContractpartnerAccounts(final UserID userId,
			final ContractpartnerID contractpartnerId) {
		Assert.notNull(userId);
		Assert.notNull(contractpartnerId);
		final List<ContractpartnerAccountData> contractpartnerAccountData = this.contractpartnerAccountDao
				.getContractpartnerAccounts(userId.getId(), contractpartnerId.getId());
		return this.mapContractpartnerAccountDataList(userId, contractpartnerAccountData);
	}

	@Override
	public void createContractpartnerAccount(final UserID userId, final ContractpartnerAccount contractpartnerAccount) {
		Assert.notNull(userId);
		Assert.notNull(contractpartnerAccount);
		contractpartnerAccount.setId(null);
		final ValidationResult validationResult = this.validateContractpartnerAccount(userId, contractpartnerAccount);

		if (!validationResult.isValid() && !validationResult.getValidationResultItems().isEmpty()) {
			final ValidationResultItem validationResultItem = validationResult.getValidationResultItems().get(0);
			throw new BusinessException("ContractpartnerAccount creation failed!", validationResultItem.getError());
		}

		final ContractpartnerAccountData contractpartnerAccountData = super.map(contractpartnerAccount,
				ContractpartnerAccountData.class);
		final Long contractpartnerAccountId = this.contractpartnerAccountDao
				.createContractpartnerAccount(contractpartnerAccountData);
		this.evictContractpartnerAccountCache(userId, new ContractpartnerAccountID(contractpartnerAccountId),
				contractpartnerAccount.getContractpartner().getId());

	}

	@Override
	public void updateContractpartnerAccount(final UserID userId, final ContractpartnerAccount contractpartnerAccount) {
		Assert.notNull(userId);
		Assert.notNull(contractpartnerAccount);
		final ValidationResult validationResult = this.validateContractpartnerAccount(userId, contractpartnerAccount);

		if (!validationResult.isValid() && !validationResult.getValidationResultItems().isEmpty()) {
			final ValidationResultItem validationResultItem = validationResult.getValidationResultItems().get(0);
			throw new BusinessException("ContractpartnerAccount update failed!", validationResultItem.getError());
		}

		final ContractpartnerAccount contractpartnerAccountOld = this.getContractpartnerAccountById(userId,
				contractpartnerAccount.getId());
		if (contractpartnerAccountOld != null) {
			final ContractpartnerAccountData contractpartnerAccountData = super.map(contractpartnerAccount,
					ContractpartnerAccountData.class);
			this.contractpartnerAccountDao.updateContractpartnerAccount(contractpartnerAccountData);

			this.evictContractpartnerAccountCache(userId, contractpartnerAccount.getId(),
					contractpartnerAccount.getContractpartner().getId());
			if (!contractpartnerAccountOld.getContractpartner().getId()
					.equals(contractpartnerAccount.getContractpartner().getId())) {
				this.evictContractpartnerAccountCache(userId, contractpartnerAccount.getId(),
						contractpartnerAccountOld.getContractpartner().getId());

			}
		}

	}

	@Override
	public void deleteContractpartnerAccountById(final UserID userId,
			final ContractpartnerAccountID contractpartnerAccountId) {
		Assert.notNull(userId);
		Assert.notNull(contractpartnerAccountId);
		final ContractpartnerAccount contractpartnerAccount = this.getContractpartnerAccountById(userId,
				contractpartnerAccountId);
		if (contractpartnerAccount != null) {
			this.contractpartnerAccountDao.deleteContractpartnerAccount(userId.getId(),
					contractpartnerAccountId.getId());
			this.evictContractpartnerAccountCache(userId, contractpartnerAccountId,
					contractpartnerAccount.getContractpartner().getId());
		}
	}

	@Override
	public void deleteContractpartnerAccounts(final UserID userId, final ContractpartnerID contractpartnerId) {
		Assert.notNull(userId);
		Assert.notNull(contractpartnerId);
		final List<ContractpartnerAccount> contractpartnerAccounts = this.getContractpartnerAccounts(userId,
				contractpartnerId);
		if (contractpartnerAccounts != null && !contractpartnerAccounts.isEmpty()) {
			this.contractpartnerAccountDao.deleteContractpartnerAccounts(userId.getId(), contractpartnerId.getId());
			contractpartnerAccounts.stream().forEach(
					ca -> this.evictContractpartnerAccountCache(userId, ca.getId(), ca.getContractpartner().getId()));
		}

	}

	@Override
	public List<ContractpartnerAccount> getAllContractpartnerByAccounts(final UserID userId,
			final List<BankAccount> bankAccounts) {
		Assert.notNull(userId);
		Assert.notNull(bankAccounts);
		final List<BankAccountData> bankAccountDatas = super.mapList(bankAccounts, BankAccountData.class);
		final List<ContractpartnerAccountData> contractpartnerAccountData = this.contractpartnerAccountDao
				.getAllContractpartnerByAccounts(userId.getId(), bankAccountDatas);
		return this.mapContractpartnerAccountDataList(userId, contractpartnerAccountData);
	}

	private void evictContractpartnerAccountCache(final UserID userId,
			final ContractpartnerAccountID contractpartnerAccountID, final ContractpartnerID contractpartnerId) {
		if (contractpartnerAccountID != null) {
			final Cache contractpartnerAccountsByPartnerCache = super.getCache(
					CacheNames.CONTRACTPARTNER_ACCOUNTS_BY_PARTNER);
			final Cache contractpartnerAccountByIdCache = super.getCache(CacheNames.CONTRACTPARTNER_ACCOUNT_BY_ID);
			if (contractpartnerAccountsByPartnerCache != null) {
				contractpartnerAccountsByPartnerCache.evict(new SimpleKey(userId, contractpartnerId));
			}
			if (contractpartnerAccountByIdCache != null) {
				contractpartnerAccountByIdCache.evict(new SimpleKey(userId, contractpartnerAccountID));
			}
		}

	}

}
