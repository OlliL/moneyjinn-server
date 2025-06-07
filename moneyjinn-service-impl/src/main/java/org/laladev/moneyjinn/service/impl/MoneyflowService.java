//
// Copyright (c) 2015-2025 Oliver Lehmann <lehmann@ans-netz.de>
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions
// are met:
// 1. Redistributions of source code must retain the above copyright
// notice, this list of conditions and the following disclaimer
// 2. Redistributions in binary form must reproduce the above copyright
// notice, this list of conditions and the following disclaimer in the
// documentation and/or other materials provided with the distribution.
//
// THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS'' AND
// ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE LIABLE
// FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
// DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
// OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
// HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
// LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
// OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE.
//

package org.laladev.moneyjinn.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.time.temporal.TemporalAdjusters;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.model.Contractpartner;
import org.laladev.moneyjinn.model.PostingAccount;
import org.laladev.moneyjinn.model.PostingAccountAmount;
import org.laladev.moneyjinn.model.PostingAccountID;
import org.laladev.moneyjinn.model.access.AccessRelation;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.capitalsource.Capitalsource;
import org.laladev.moneyjinn.model.capitalsource.CapitalsourceID;
import org.laladev.moneyjinn.model.capitalsource.CapitalsourceType;
import org.laladev.moneyjinn.model.exception.BusinessException;
import org.laladev.moneyjinn.model.moneyflow.Moneyflow;
import org.laladev.moneyjinn.model.moneyflow.MoneyflowID;
import org.laladev.moneyjinn.model.moneyflow.search.MoneyflowSearchParams;
import org.laladev.moneyjinn.model.validation.ValidationResult;
import org.laladev.moneyjinn.model.validation.ValidationResultItem;
import org.laladev.moneyjinn.service.CacheNames;
import org.laladev.moneyjinn.service.api.IAccessRelationService;
import org.laladev.moneyjinn.service.api.ICapitalsourceService;
import org.laladev.moneyjinn.service.api.IContractpartnerService;
import org.laladev.moneyjinn.service.api.IGroupService;
import org.laladev.moneyjinn.service.api.IMoneyflowService;
import org.laladev.moneyjinn.service.api.IPostingAccountService;
import org.laladev.moneyjinn.service.api.IUserService;
import org.laladev.moneyjinn.service.dao.MoneyflowDao;
import org.laladev.moneyjinn.service.dao.data.MoneyflowData;
import org.laladev.moneyjinn.service.dao.data.MoneyflowSearchParamsData;
import org.laladev.moneyjinn.service.dao.data.PostingAccountAmountData;
import org.laladev.moneyjinn.service.dao.data.mapper.MoneyflowDataMapper;
import org.laladev.moneyjinn.service.dao.data.mapper.MoneyflowSearchParamsDataMapper;
import org.laladev.moneyjinn.service.dao.data.mapper.PostingAccountAmountDataMapper;
import org.springframework.cache.interceptor.SimpleKey;
import org.springframework.util.Assert;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;

@Named
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class MoneyflowService extends AbstractService implements IMoneyflowService {
	private static final String DATE_TIL_MUST_NOT_BE_NULL = "dateTil must not be null!";
	private static final String DATE_FROM_MUST_NOT_BE_NULL = "dateFrom must not be null!";
	private static final String USER_ID_MUST_NOT_BE_NULL = "UserId must not be null!";
	private final IUserService userService;
	private final IGroupService groupService;
	private final ICapitalsourceService capitalsourceService;
	private final IContractpartnerService contractpartnerService;
	private final IAccessRelationService accessRelationService;
	private final IPostingAccountService postingAccountService;
	private final MoneyflowDao moneyflowDao;
	private final MoneyflowDataMapper moneyflowDataMapper;
	private final PostingAccountAmountDataMapper postingAccountAmountDataMapper;
	private final MoneyflowSearchParamsDataMapper moneyflowSearchParamsDataMapper;

	private Moneyflow mapMoneyflowData(final MoneyflowData moneyflowData) {
		if (moneyflowData != null) {
			final Moneyflow moneyflow = this.moneyflowDataMapper.mapBToA(moneyflowData);

			this.userService.enrichEntity(moneyflow);
			this.groupService.enrichEntity(moneyflow);
			this.postingAccountService.enrichEntity(moneyflow);
			this.capitalsourceService.enrichEntity(moneyflow);
			this.contractpartnerService.enrichEntity(moneyflow);

			return moneyflow;
		}
		return null;
	}

	private List<Moneyflow> mapMoneyflowDataList(final List<MoneyflowData> moneyflowDataList) {
		return moneyflowDataList.stream().map(this::mapMoneyflowData).toList();
	}

	private List<PostingAccountAmount> mapPostingAccountAmountDataList(
			final List<PostingAccountAmountData> postingAccountAmountDatas) {
		final List<PostingAccountAmount> postingAccountAmounts = this.postingAccountAmountDataMapper
				.mapBToA(postingAccountAmountDatas);
		for (final PostingAccountAmount postingAccountAmount : postingAccountAmounts) {
			PostingAccount postingAccount = postingAccountAmount.getPostingAccount();
			postingAccount = this.postingAccountService.getPostingAccountById(postingAccount.getId());
			postingAccountAmount.setPostingAccount(postingAccount);
		}
		return postingAccountAmounts;
	}

	private void prepareMoneyflow(final Moneyflow moneyflow) {
		if (moneyflow.getInvoiceDate() == null && moneyflow.getBookingDate() != null) {
			moneyflow.setInvoiceDate(moneyflow.getBookingDate());
		}
	}

	@Override
	public ValidationResult validateMoneyflow(final Moneyflow moneyflow) {
		Assert.notNull(moneyflow, "Moneyflow must not be null!");
		Assert.notNull(moneyflow.getUser(), "Moneyflow.user must not be null!");
		Assert.notNull(moneyflow.getUser().getId(), "Moneyflow.user.id must not be null!");
		Assert.notNull(moneyflow.getGroup(), "Moneyflow.group must not be null!");
		Assert.notNull(moneyflow.getGroup().getId(), "Moneyflow.group.id must not be null!");

		this.prepareMoneyflow(moneyflow);

		final UserID userId = moneyflow.getUser().getId();
		final LocalDate bookingDate = moneyflow.getBookingDate();
		final ValidationResult validationResult = new ValidationResult();
		final Consumer<ErrorCode> addResult = (final ErrorCode errorCode) -> validationResult.addValidationResultItem(
				new ValidationResultItem(moneyflow.getId(), errorCode));

		if (bookingDate == null) {
			addResult.accept(ErrorCode.BOOKINGDATE_IN_WRONG_FORMAT);
		} else {
			final AccessRelation accessRelation = this.accessRelationService
					.getCurrentAccessRelationById(moneyflow.getUser().getId());
			// if this check is removed, make sure the group is evaluated for the
			// bookingdate, not for today otherwise it will be created with the wrong
			// group
			if (!accessRelation.dateIsInValidPeriod(bookingDate)) {
				addResult.accept(ErrorCode.BOOKINGDATE_OUTSIDE_GROUP_ASSIGNMENT);
			}
		}

		if (moneyflow.getCapitalsource() == null) {
			addResult.accept(ErrorCode.CAPITALSOURCE_IS_NOT_SET);
		} else {
			final Capitalsource capitalsource = this.capitalsourceService.getCapitalsourceById(userId,
					moneyflow.getGroup().getId(), moneyflow.getCapitalsource().getId());
			if (capitalsource == null) {
				addResult.accept(ErrorCode.CAPITALSOURCE_DOES_NOT_EXIST);
			} else if (!capitalsource.groupUseAllowed(userId)) {
				addResult.accept(ErrorCode.CAPITALSOURCE_DOES_NOT_EXIST);
			} else if (!capitalsource.dateIsInValidPeriod(bookingDate)) {
				addResult.accept(ErrorCode.CAPITALSOURCE_USE_OUT_OF_VALIDITY);
			} else if (capitalsource.getType() == CapitalsourceType.CREDIT) {
				addResult.accept(ErrorCode.CAPITALSOURCE_INVALID);
			}
		}

		if (moneyflow.getContractpartner() == null) {
			addResult.accept(ErrorCode.CONTRACTPARTNER_IS_NOT_SET);
		} else {
			final Contractpartner contractpartner = this.contractpartnerService.getContractpartnerById(userId,
					moneyflow.getContractpartner().getId());
			if (contractpartner == null) {
				addResult.accept(ErrorCode.CONTRACTPARTNER_DOES_NOT_EXIST);
			} else if (!contractpartner.dateIsInValidPeriod(bookingDate)) {
				addResult.accept(ErrorCode.CONTRACTPARTNER_NO_LONGER_VALID);
			}
		}

		if (moneyflow.getComment() == null || moneyflow.getComment().isBlank()) {
			addResult.accept(ErrorCode.COMMENT_IS_NOT_SET);
		}

		final BigDecimal amount = moneyflow.getAmount();
		if (amount == null || amount.compareTo(BigDecimal.ZERO) == 0) {
			addResult.accept(ErrorCode.AMOUNT_IS_ZERO);
		} else if (amount.precision() - amount.scale() > 6) {
			addResult.accept(ErrorCode.AMOUNT_TO_BIG);
		}

		if (moneyflow.getPostingAccount() == null) {
			addResult.accept(ErrorCode.POSTING_ACCOUNT_NOT_SPECIFIED);
		} else {
			final PostingAccount postingAccount = this.postingAccountService
					.getPostingAccountById(moneyflow.getPostingAccount().getId());
			if (postingAccount == null) {
				addResult.accept(ErrorCode.POSTING_ACCOUNT_NOT_SPECIFIED);
			}
		}

		return validationResult;
	}

	@Override
	public Moneyflow getMoneyflowById(final UserID userId, final MoneyflowID moneyflowId) {
		Assert.notNull(userId, USER_ID_MUST_NOT_BE_NULL);
		Assert.notNull(moneyflowId, "moneyflowId must not be null!");

		final Supplier<Moneyflow> supplier = () -> this.mapMoneyflowData(
				this.moneyflowDao.getMoneyflowById(userId.getId(), moneyflowId.getId()));

		return super.getFromCacheOrExecute(CacheNames.MONEYFLOW_BY_ID, new SimpleKey(userId, moneyflowId), supplier,
				Moneyflow.class);
	}

	@Override
	public MoneyflowID createMoneyflow(final Moneyflow moneyflow) {
		final ValidationResult validationResult = this.validateMoneyflow(moneyflow);
		if (!validationResult.isValid() && !validationResult.getValidationResultItems().isEmpty()) {
			final ValidationResultItem validationResultItem = validationResult.getValidationResultItems().getFirst();
			throw new BusinessException("Moneyflow creation failed!", validationResultItem.getError());
		}
		final MoneyflowData moneyflowData = this.moneyflowDataMapper.mapAToB(moneyflow);
		final Long moneyflowId = this.moneyflowDao.createMoneyflow(moneyflowData);
		this.evictMoneyflowCache(moneyflow.getUser().getId(), new MoneyflowID(moneyflowId));
		return new MoneyflowID(moneyflowId);
	}

	@Override
	public void updateMoneyflow(final Moneyflow moneyflow) {
		Assert.notNull(moneyflow, "moneyflow must not be null!");
		final ValidationResult validationResult = this.validateMoneyflow(moneyflow);
		if (!validationResult.isValid() && !validationResult.getValidationResultItems().isEmpty()) {
			final ValidationResultItem validationResultItem = validationResult.getValidationResultItems().getFirst();
			throw new BusinessException("Moneyflow update failed!", validationResultItem.getError());
		}
		final MoneyflowData moneyflowData = this.moneyflowDataMapper.mapAToB(moneyflow);
		this.moneyflowDao.updateMoneyflow(moneyflowData);
		this.evictMoneyflowCache(moneyflow.getUser().getId(), moneyflow.getId());
	}

	@Override
	public void deleteMoneyflow(final UserID userId, final MoneyflowID moneyflowId) {
		Assert.notNull(userId, USER_ID_MUST_NOT_BE_NULL);
		Assert.notNull(moneyflowId, "moneyflowId must not be null!");
		this.moneyflowDao.deleteMoneyflow(userId.getId(), moneyflowId.getId());
		this.evictMoneyflowCache(userId, moneyflowId);
	}

	private void evictMoneyflowCache(final UserID userId, final MoneyflowID moneyflowId) {
		if (moneyflowId != null) {
			this.accessRelationService.getAllUserWithSameGroup(userId).forEach(evictingUserId -> {
				super.evictFromCache(CacheNames.MONEYFLOW_BY_ID, new SimpleKey(evictingUserId, moneyflowId));
				super.evictFromCache(CacheNames.MONEYFLOW_YEARS, evictingUserId);
				super.clearCache(super.getCombinedCacheName(CacheNames.MONEYFLOW_MONTH, evictingUserId.getId()));
			});
		}
	}

	@Override
	public BigDecimal getSumAmountByDateRangeForCapitalsourceId(final UserID userId, final LocalDate dateFrom,
			final LocalDate dateTil, final CapitalsourceID capitalsourceId) {
		return this.getSumAmountByDateRangeForCapitalsourceIds(userId, dateFrom, dateTil,
				Collections.singletonList(capitalsourceId));
	}

	@Override
	public List<Integer> getAllYears(final UserID userId) {
		Assert.notNull(userId, USER_ID_MUST_NOT_BE_NULL);

		final Supplier<List<Integer>> supplier = () -> this.moneyflowDao.getAllYears(userId.getId());

		return super.getListFromCacheOrExecute(CacheNames.MONEYFLOW_YEARS, userId, supplier);

	}

	@Override
	public List<Month> getAllMonth(final UserID userId, final Integer year) {
		Assert.notNull(userId, USER_ID_MUST_NOT_BE_NULL);
		Assert.notNull(year, "year must not be null!");

		final Supplier<List<Month>> supplier = () -> {
			final LocalDate beginOfYear = LocalDate.of(year, Month.JANUARY, 1);
			final LocalDate endOfYear = LocalDate.of(year, Month.DECEMBER, 31);
			final List<Integer> allMonths = this.moneyflowDao.getAllMonth(userId.getId(), beginOfYear, endOfYear);
			return allMonths.stream().map(m -> Month.of(m.intValue())).toList();
		};

		return super.getListFromCacheOrExecute(super.getCombinedCacheName(CacheNames.MONEYFLOW_MONTH, userId.getId()),
				year, supplier);

	}

	@Override
	public List<Moneyflow> getAllMoneyflowsByDateRangeIncludingPrivate(final UserID userId, final LocalDate dateFrom,
			final LocalDate dateTil) {
		Assert.notNull(userId, USER_ID_MUST_NOT_BE_NULL);
		Assert.notNull(dateFrom, DATE_FROM_MUST_NOT_BE_NULL);
		Assert.notNull(dateTil, DATE_TIL_MUST_NOT_BE_NULL);
		final List<MoneyflowData> moneyflowDatas = this.moneyflowDao
				.getAllMoneyflowsByDateRangeIncludingPrivate(userId.getId(), dateFrom, dateTil);
		return this.mapMoneyflowDataList(moneyflowDatas);
	}

	@Override
	public boolean monthHasMoneyflows(final UserID userId, final Integer year, final Month month) {
		Assert.notNull(userId, USER_ID_MUST_NOT_BE_NULL);
		Assert.notNull(year, "year must not be null!");
		Assert.notNull(month, "month must not be null!");
		final LocalDate beginOfMonth = LocalDate.of(year, month, 1);
		final LocalDate endOfMonth = beginOfMonth.with(TemporalAdjusters.lastDayOfMonth());
		return this.moneyflowDao.monthHasMoneyflows(userId.getId(), beginOfMonth, endOfMonth);
	}

	@Override
	public BigDecimal getSumAmountByDateRangeForCapitalsourceIds(final UserID userId, final LocalDate dateFrom,
			final LocalDate dateTil, final List<CapitalsourceID> capitalsourceIds) {
		Assert.notNull(userId, USER_ID_MUST_NOT_BE_NULL);
		Assert.notNull(dateFrom, DATE_FROM_MUST_NOT_BE_NULL);
		Assert.notNull(dateTil, DATE_TIL_MUST_NOT_BE_NULL);
		Assert.notNull(capitalsourceIds, "capitalsourceIds must not be null!");
		final List<Long> capitalsourceIdLongs = capitalsourceIds.stream().map(CapitalsourceID::getId).toList();
		return this.moneyflowDao.getSumAmountByDateRangeForCapitalsourceIds(userId.getId(), dateFrom, dateTil,
				capitalsourceIdLongs);
	}

	@Override
	public LocalDate getMinMoneyflowDate(final UserID userId) {
		Assert.notNull(userId, USER_ID_MUST_NOT_BE_NULL);
		return this.moneyflowDao.getMinMoneyflowDate(userId.getId());
	}

	@Override
	public LocalDate getMaxMoneyflowDate(final UserID userId) {
		Assert.notNull(userId, USER_ID_MUST_NOT_BE_NULL);
		return this.moneyflowDao.getMaxMoneyflowDate(userId.getId());
	}

	@Override
	public LocalDate getPreviousMoneyflowDate(final UserID userId, final LocalDate date) {
		Assert.notNull(userId, USER_ID_MUST_NOT_BE_NULL);
		Assert.notNull(date, "Date must not be null!");
		return this.moneyflowDao.getPreviousMoneyflowDate(userId.getId(), date);
	}

	@Override
	public LocalDate getNextMoneyflowDate(final UserID userId, final LocalDate date) {
		Assert.notNull(userId, USER_ID_MUST_NOT_BE_NULL);
		Assert.notNull(date, "Date must not be null!");
		return this.moneyflowDao.getNextMoneyflowDate(userId.getId(), date);
	}

	@Override
	public List<PostingAccountAmount> getAllMoneyflowsByDateRangeGroupedByYearMonthPostingAccount(final UserID userId,
			final List<PostingAccountID> postingAccountIds, final LocalDate dateFrom, final LocalDate dateTil) {
		Assert.notNull(userId, USER_ID_MUST_NOT_BE_NULL);
		Assert.notNull(dateFrom, "DateFrom must not be null!");
		Assert.notNull(dateTil, "DateTil must not be null!");
		Assert.notEmpty(postingAccountIds, "PsostingAccountIds must not be null!");
		final List<Long> postingAccountIdLongs = postingAccountIds.stream().map(PostingAccountID::getId).toList();
		final List<PostingAccountAmountData> postingAccountAmountDatas = this.moneyflowDao
				.getAllMoneyflowsByDateRangeGroupedByYearMonthPostingAccount(userId.getId(), postingAccountIdLongs,
						dateFrom, dateTil);
		return this.mapPostingAccountAmountDataList(postingAccountAmountDatas);
	}

	@Override
	public List<PostingAccountAmount> getAllMoneyflowsByDateRangeGroupedByYearPostingAccount(final UserID userId,
			final List<PostingAccountID> postingAccountIds, final LocalDate dateFrom, final LocalDate dateTil) {
		Assert.notNull(userId, USER_ID_MUST_NOT_BE_NULL);
		Assert.notNull(dateFrom, "DateFrom must not be null!");
		Assert.notNull(dateTil, "DateTil must not be null!");
		Assert.notEmpty(postingAccountIds, "PsostingAccountIds must not be null!");
		final List<Long> postingAccountIdLongs = postingAccountIds.stream().map(PostingAccountID::getId).toList();
		final List<PostingAccountAmountData> postingAccountAmountDatas = this.moneyflowDao
				.getAllMoneyflowsByDateRangeGroupedByYearPostingAccount(userId.getId(), postingAccountIdLongs, dateFrom,
						dateTil);
		return this.mapPostingAccountAmountDataList(postingAccountAmountDatas);
	}

	@Override
	public List<Moneyflow> searchMoneyflowsByAbsoluteAmountDate(final UserID userId, final BigDecimal amount,
			final LocalDate dateFrom, final LocalDate dateTil) {
		Assert.notNull(userId, USER_ID_MUST_NOT_BE_NULL);
		Assert.notNull(dateFrom, "fromDate must not be null!");
		Assert.notNull(amount, "amount must not be null!");
		Assert.notNull(dateTil, "toDate must not be null!");
		final List<MoneyflowData> moneyflowDatas = this.moneyflowDao
				.searchMoneyflowsByAbsoluteAmountDate(userId.getId(), dateFrom, dateTil, amount);
		return this.mapMoneyflowDataList(moneyflowDatas);
	}

	@Override
	public List<Moneyflow> searchMoneyflowsByAmountDate(final UserID userId, final LocalDate bookingDate,
			final BigDecimal amount, final Period searchPeriod) {
		Assert.notNull(userId, USER_ID_MUST_NOT_BE_NULL);
		Assert.notNull(bookingDate, "bookingDate must not be null!");
		Assert.notNull(amount, "amount must not be null!");
		Assert.notNull(searchPeriod, "searchPeriod must not be null!");
		final LocalDate beginOfMonth = bookingDate.with(TemporalAdjusters.firstDayOfMonth());
		final LocalDate endOfMonth = bookingDate.with(TemporalAdjusters.lastDayOfMonth());
		LocalDate dateFrom = bookingDate.minus(searchPeriod);
		if (dateFrom.isBefore(beginOfMonth)) {
			dateFrom = beginOfMonth;
		}
		LocalDate dateTil = bookingDate.plus(searchPeriod);
		if (dateTil.isAfter(endOfMonth)) {
			dateTil = endOfMonth;
		}
		final List<MoneyflowData> moneyflowDatas = this.moneyflowDao.searchMoneyflowsByAmountDate(userId.getId(),
				dateFrom, dateTil, amount);
		return this.mapMoneyflowDataList(moneyflowDatas);
	}

	@Override
	public List<Moneyflow> searchMoneyflows(final UserID userId, final MoneyflowSearchParams moneyflowSearchParams) {
		Assert.notNull(userId, USER_ID_MUST_NOT_BE_NULL);
		Assert.notNull(moneyflowSearchParams, "moneyflowSearchParams must not be null!");
		if (moneyflowSearchParams.getStartDate() == null) {
			moneyflowSearchParams.setStartDate(LocalDate.of(0, Month.JANUARY, 1));
		}
		if (moneyflowSearchParams.getEndDate() == null) {
			moneyflowSearchParams.setEndDate(LocalDate.of(9999, Month.DECEMBER, 31));
		}
		final MoneyflowSearchParamsData moneyflowSearchParamsData = this.moneyflowSearchParamsDataMapper
				.mapAToB(moneyflowSearchParams);
		final List<MoneyflowData> moneyflowDatas = this.moneyflowDao.searchMoneyflows(userId.getId(),
				moneyflowSearchParamsData);
		return this.mapMoneyflowDataList(moneyflowDatas);
	}

	@Override
	public List<Moneyflow> getAllMoneyflowsByDateRangeCapitalsourceId(final UserID userId, final LocalDate dateFrom,
			final LocalDate dateTil, final CapitalsourceID capitalsourceId) {
		Assert.notNull(userId, USER_ID_MUST_NOT_BE_NULL);
		Assert.notNull(dateFrom, DATE_FROM_MUST_NOT_BE_NULL);
		Assert.notNull(dateTil, DATE_TIL_MUST_NOT_BE_NULL);
		Assert.notNull(capitalsourceId, "capitalsourceId must not be null!");
		final List<MoneyflowData> moneyflowDatas = this.moneyflowDao
				.getAllMoneyflowsByDateRangeCapitalsourceId(userId.getId(), dateFrom, dateTil, capitalsourceId.getId());
		return this.mapMoneyflowDataList(moneyflowDatas);
	}
}
