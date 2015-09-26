package org.laladev.moneyjinn.businesslogic.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.laladev.moneyjinn.businesslogic.dao.MoneyflowDao;
import org.laladev.moneyjinn.businesslogic.dao.data.MoneyflowData;
import org.laladev.moneyjinn.businesslogic.dao.data.mapper.MoneyflowDataMapper;
import org.laladev.moneyjinn.businesslogic.model.Contractpartner;
import org.laladev.moneyjinn.businesslogic.model.ContractpartnerID;
import org.laladev.moneyjinn.businesslogic.model.Moneyflow;
import org.laladev.moneyjinn.businesslogic.model.MoneyflowID;
import org.laladev.moneyjinn.businesslogic.model.PostingAccount;
import org.laladev.moneyjinn.businesslogic.model.PostingAccountID;
import org.laladev.moneyjinn.businesslogic.model.access.AccessRelation;
import org.laladev.moneyjinn.businesslogic.model.access.Group;
import org.laladev.moneyjinn.businesslogic.model.access.GroupID;
import org.laladev.moneyjinn.businesslogic.model.access.User;
import org.laladev.moneyjinn.businesslogic.model.access.UserID;
import org.laladev.moneyjinn.businesslogic.model.capitalsource.Capitalsource;
import org.laladev.moneyjinn.businesslogic.model.capitalsource.CapitalsourceID;
import org.laladev.moneyjinn.businesslogic.model.exception.BusinessException;
import org.laladev.moneyjinn.businesslogic.model.validation.ValidationResult;
import org.laladev.moneyjinn.businesslogic.model.validation.ValidationResultItem;
import org.laladev.moneyjinn.businesslogic.service.CacheNames;
import org.laladev.moneyjinn.businesslogic.service.api.IAccessRelationService;
import org.laladev.moneyjinn.businesslogic.service.api.ICapitalsourceService;
import org.laladev.moneyjinn.businesslogic.service.api.IContractpartnerService;
import org.laladev.moneyjinn.businesslogic.service.api.IMoneyflowService;
import org.laladev.moneyjinn.businesslogic.service.api.IPostingAccountService;
import org.laladev.moneyjinn.businesslogic.service.api.IUserService;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.SimpleKey;
import org.springframework.util.Assert;

@Named
@EnableCaching
public class MoneyflowService extends AbstractService implements IMoneyflowService {

	@Inject
	private IUserService userService;
	@Inject
	private ICapitalsourceService capitalsourceService;
	@Inject
	private IContractpartnerService contractpartnerService;
	@Inject
	private IAccessRelationService accessRelationService;
	@Inject
	private IPostingAccountService postingAccountService;

	@Inject
	private MoneyflowDao moneyflowDao;

	@Override
	protected void addBeanMapper() {
		super.registerBeanMapper(new MoneyflowDataMapper());

	}

	private final Moneyflow mapMoneyflowData(final MoneyflowData moneyflowData) {
		if (moneyflowData != null) {
			final Moneyflow moneyflow = super.map(moneyflowData, Moneyflow.class);
			final UserID userId = moneyflow.getUser().getId();
			final User user = this.userService.getUserById(userId);
			final Group accessor = this.accessRelationService.getAccessor(userId, moneyflow.getBookingDate());
			final GroupID groupId = accessor.getId();
			moneyflow.setUser(user);
			moneyflow.setGroup(accessor);

			PostingAccount postingAccount = moneyflow.getPostingAccount();
			if (postingAccount != null) {
				final PostingAccountID postingAccountId = postingAccount.getId();
				postingAccount = this.postingAccountService.getPostingAccountById(postingAccountId);
				moneyflow.setPostingAccount(postingAccount);
			}

			Capitalsource capitalsource = moneyflow.getCapitalsource();
			if (capitalsource != null) {
				final CapitalsourceID capitalsourceId = capitalsource.getId();
				capitalsource = this.capitalsourceService.getCapitalsourceById(userId, groupId, capitalsourceId);
				moneyflow.setCapitalsource(capitalsource);
			}

			Contractpartner contractpartner = moneyflow.getContractpartner();
			if (contractpartner != null) {
				final ContractpartnerID contractpartnerId = contractpartner.getId();
				contractpartner = this.contractpartnerService.getContractpartnerById(userId, contractpartnerId);
				moneyflow.setContractpartner(contractpartner);
			}

			return moneyflow;
		}
		return null;
	}

	private void prepareMoneyflow(final Moneyflow moneyflow) {
		if (moneyflow.getInvoiceDate() == null && moneyflow.getBookingDate() != null) {
			moneyflow.setInvoiceDate(moneyflow.getBookingDate());
		}
	}

	@Override
	public ValidationResult validateMoneyflow(final Moneyflow moneyflow) {
		Assert.notNull(moneyflow);
		Assert.notNull(moneyflow.getUser());
		Assert.notNull(moneyflow.getUser().getId());
		Assert.notNull(moneyflow.getGroup());
		Assert.notNull(moneyflow.getGroup().getId());

		this.prepareMoneyflow(moneyflow);

		final ValidationResult validationResult = new ValidationResult();

		final LocalDate today = LocalDate.now();
		final UserID userId = moneyflow.getUser().getId();
		final GroupID groupId = moneyflow.getGroup().getId();

		if (moneyflow.getBookingDate() == null) {
			validationResult.addValidationResultItem(
					new ValidationResultItem(moneyflow.getId(), ErrorCode.BOOKINGDATE_IN_WRONG_FORMAT));
		} else {
			final LocalDate bookingDate = moneyflow.getBookingDate();
			final AccessRelation accessRelation = this.accessRelationService
					.getAccessRelationById(moneyflow.getUser().getId(), today);
			// if this check is removed, make sure the accessor is evaluated for the bookingdate,
			// not for today otherwise it will be created with the wrong accessor
			if (bookingDate.isBefore(accessRelation.getValidFrom())
					|| bookingDate.isAfter(accessRelation.getValidTil())) {
				validationResult.addValidationResultItem(
						new ValidationResultItem(moneyflow.getId(), ErrorCode.BOOKINGDATE_OUTSIDE_GROUP_ASSIGNMENT));
			}
		}

		if (moneyflow.getCapitalsource() == null) {
			validationResult.addValidationResultItem(
					new ValidationResultItem(moneyflow.getId(), ErrorCode.CAPITALSOURCE_IS_NOT_SET));
		} else {
			final Capitalsource capitalsource = this.capitalsourceService.getCapitalsourceById(userId, groupId,
					moneyflow.getCapitalsource().getId());
			if (capitalsource == null || (!capitalsource.getUser().getId().equals(moneyflow.getUser().getId())
					&& !capitalsource.isGroupUse())) {
				validationResult.addValidationResultItem(
						new ValidationResultItem(moneyflow.getId(), ErrorCode.CAPITALSOURCE_DOES_NOT_EXIST));
			} else if (today.isBefore(capitalsource.getValidFrom()) || today.isAfter(capitalsource.getValidTil())) {
				validationResult.addValidationResultItem(
						new ValidationResultItem(moneyflow.getId(), ErrorCode.CAPITALSOURCE_USE_OUT_OF_VALIDITY));
			}
		}

		if (moneyflow.getContractpartner() == null) {
			validationResult.addValidationResultItem(
					new ValidationResultItem(moneyflow.getId(), ErrorCode.CONTRACTPARTNER_IS_NOT_SET));
		} else {
			final Contractpartner contractpartner = this.contractpartnerService.getContractpartnerById(userId,
					moneyflow.getContractpartner().getId());
			if (contractpartner == null) {
				validationResult.addValidationResultItem(
						new ValidationResultItem(moneyflow.getId(), ErrorCode.CONTRACTPARTNER_DOES_NOT_EXIST));
			} else if (today.isBefore(contractpartner.getValidFrom()) || today.isAfter(contractpartner.getValidTil())) {
				validationResult.addValidationResultItem(
						new ValidationResultItem(moneyflow.getId(), ErrorCode.CONTRACTPARTNER_NO_LONGER_VALID));
			}
		}

		if (moneyflow.getComment() == null || moneyflow.getComment().trim().isEmpty()) {
			validationResult
					.addValidationResultItem(new ValidationResultItem(moneyflow.getId(), ErrorCode.COMMENT_IS_NOT_SET));
		}

		if (moneyflow.getAmount() == null || moneyflow.getAmount().compareTo(BigDecimal.ZERO) == 0) {
			validationResult
					.addValidationResultItem(new ValidationResultItem(moneyflow.getId(), ErrorCode.AMOUNT_IS_ZERO));
		}

		if (moneyflow.getPostingAccount() == null) {
			validationResult.addValidationResultItem(
					new ValidationResultItem(moneyflow.getId(), ErrorCode.POSTING_ACCOUNT_NOT_SPECIFIED));
		} else {
			final PostingAccount postingAccount = this.postingAccountService
					.getPostingAccountById(moneyflow.getPostingAccount().getId());
			if (postingAccount == null) {
				validationResult.addValidationResultItem(
						new ValidationResultItem(moneyflow.getId(), ErrorCode.POSTING_ACCOUNT_NOT_SPECIFIED));
			}

		}

		return validationResult;
	}

	@Override
	@Cacheable(value = CacheNames.MONEYFLOW_BY_ID)
	public Moneyflow getMoneyflowById(final UserID userId, final MoneyflowID moneyflowId) {
		Assert.notNull(userId);
		Assert.notNull(moneyflowId);
		final MoneyflowData moneyflowData = this.moneyflowDao.getMoneyflowById(userId.getId(), moneyflowId.getId());
		return this.mapMoneyflowData(moneyflowData);
	}

	@Override
	public void createMoneyflows(final List<Moneyflow> moneyflows) {
		final ValidationResult validationResult = new ValidationResult();
		moneyflows.stream().forEach(mf -> validationResult.mergeValidationResult(this.validateMoneyflow(mf)));

		if (!validationResult.isValid() && !validationResult.getValidationResultItems().isEmpty()) {
			final ValidationResultItem validationResultItem = validationResult.getValidationResultItems().get(0);
			throw new BusinessException("Moneyflow creation failed!", validationResultItem.getError());
		}

		for (final Moneyflow moneyflow : moneyflows) {
			final MoneyflowData moneyflowData = super.map(moneyflow, MoneyflowData.class);
			final Long moneyflowId = this.moneyflowDao.createMoneyflow(moneyflowData);
			this.evictMoneyflowCache(moneyflow.getUser().getId(), new MoneyflowID(moneyflowId));
		}
	}

	@Override
	public void updateMoneyflow(final Moneyflow moneyflow) {
		Assert.notNull(moneyflow);
		final ValidationResult validationResult = this.validateMoneyflow(moneyflow);

		if (!validationResult.isValid() && !validationResult.getValidationResultItems().isEmpty()) {
			final ValidationResultItem validationResultItem = validationResult.getValidationResultItems().get(0);
			throw new BusinessException("Moneyflow update failed!", validationResultItem.getError());
		}

		final MoneyflowData moneyflowData = super.map(moneyflow, MoneyflowData.class);
		this.moneyflowDao.updateMoneyflow(moneyflowData);
		this.evictMoneyflowCache(moneyflow.getUser().getId(), moneyflow.getId());
	}

	@Override
	public void deleteMoneyflow(final UserID userId, final MoneyflowID moneyflowId) {
		Assert.notNull(userId);
		Assert.notNull(moneyflowId);
		this.moneyflowDao.deleteMoneyflow(userId.getId(), moneyflowId.getId());
		this.evictMoneyflowCache(userId, moneyflowId);
	}

	private void evictMoneyflowCache(final UserID userId, final MoneyflowID moneyflowId) {
		if (moneyflowId != null) {
			final Cache moneyflowIdCache = super.getCache(CacheNames.MONEYFLOW_BY_ID);
			if (moneyflowIdCache != null) {
				moneyflowIdCache.evict(new SimpleKey(userId, moneyflowId));
			}
		}
	}

}
