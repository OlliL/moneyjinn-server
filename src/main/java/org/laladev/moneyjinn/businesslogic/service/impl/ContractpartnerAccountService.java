package org.laladev.moneyjinn.businesslogic.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;

import org.laladev.moneyjinn.businesslogic.dao.ContractpartnerAccountDao;
import org.laladev.moneyjinn.businesslogic.dao.data.ContractpartnerAccountData;
import org.laladev.moneyjinn.businesslogic.dao.data.mapper.ContractpartnerAccountDataMapper;
import org.laladev.moneyjinn.businesslogic.model.BankAccount;
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
	IContractpartnerService contractpartnerService;

	@Inject
	ContractpartnerAccountDao contractpartnerAccountDao;

	@Override
	protected void addBeanMapper() {
		super.registerBeanMapper(new ContractpartnerAccountDataMapper());
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
			if (contractpartnerAccountChecked != null && !contractpartnerAccountChecked.getContractpartner().getId()
					.equals(contractpartnerAccount.getContractpartner().getId())) {
				validationResult.addValidationResultItem(new ValidationResultItem(contractpartnerAccount.getId(),
						ErrorCode.ACCOUNT_ALREADY_ASSIGNED_TO_OTHER_PARTNER,
						Arrays.asList(contractpartnerAccountChecked.getContractpartner().getName())));
			}
		}

		return validationResult;
	}

	private final ContractpartnerAccount mapContractpartnerAccountData(final UserID userId,
			final ContractpartnerAccountData contractpartnerAccountData) {
		if (contractpartnerAccountData != null) {
			final ContractpartnerAccount contractpartnerAccount = super.map(contractpartnerAccountData,
					ContractpartnerAccount.class);
			contractpartnerAccount.setContractpartner(this.contractpartnerService.getContractpartnerById(userId,
					contractpartnerAccount.getContractpartner().getId()));
			return contractpartnerAccount;
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
		this.evictContractpartnerAccountCache(userId, new ContractpartnerAccountID(contractpartnerAccountId));

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

		final ContractpartnerAccountData contractpartnerAccountData = super.map(contractpartnerAccount,
				ContractpartnerAccountData.class);
		this.contractpartnerAccountDao.updateContractpartnerAccount(contractpartnerAccountData);
		this.evictContractpartnerAccountCache(userId, contractpartnerAccount.getId());

	}

	@Override
	public void deleteContractpartnerAccountById(final UserID userId,
			final ContractpartnerAccountID contractpartnerAccountId) {
		Assert.notNull(userId);
		Assert.notNull(contractpartnerAccountId);
		this.contractpartnerAccountDao.deleteContractpartnerAccount(userId.getId(), contractpartnerAccountId.getId());
		this.evictContractpartnerAccountCache(userId, contractpartnerAccountId);
	}

	@Override
	public void deleteContractpartnerAccounts(final UserID userId, final ContractpartnerID contractpartnerId) {
		Assert.notNull(userId);
		Assert.notNull(contractpartnerId);
		final List<ContractpartnerAccount> contractpartnerAccounts = this.getContractpartnerAccounts(userId,
				contractpartnerId);
		this.contractpartnerAccountDao.deleteContractpartnerAccounts(userId.getId(), contractpartnerId.getId());
		contractpartnerAccounts.stream().forEach(ca -> this.evictContractpartnerAccountCache(userId, ca.getId()));

	}

	private void evictContractpartnerAccountCache(final UserID userId,
			final ContractpartnerAccountID contractpartnerAccountID) {
		if (contractpartnerAccountID != null) {
			final Cache contractpartnerAccountsByPartnerCache = super.getCache(
					CacheNames.CONTRACTPARTNER_ACCOUNTS_BY_PARTNER);
			final Cache contractpartnerAccountByIdCache = super.getCache(CacheNames.CONTRACTPARTNER_ACCOUNT_BY_ID);
			if (contractpartnerAccountsByPartnerCache != null) {
				contractpartnerAccountsByPartnerCache.evict(userId);
			}
			if (contractpartnerAccountByIdCache != null) {
				contractpartnerAccountByIdCache.evict(new SimpleKey(userId, contractpartnerAccountID));
			}
		}

	}

}
