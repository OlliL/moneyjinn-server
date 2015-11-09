//
// Copyright (c) 2015 Oliver Lehmann <oliver@laladev.org>
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

package org.laladev.moneyjinn.businesslogic.service.impl;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;

import org.laladev.moneyjinn.businesslogic.dao.MoneyflowDao;
import org.laladev.moneyjinn.businesslogic.dao.data.MoneyflowData;
import org.laladev.moneyjinn.businesslogic.dao.data.MoneyflowSearchParamsData;
import org.laladev.moneyjinn.businesslogic.dao.data.MoneyflowSearchResultData;
import org.laladev.moneyjinn.businesslogic.dao.data.PostingAccountAmountData;
import org.laladev.moneyjinn.businesslogic.dao.data.mapper.MoneyflowDataMapper;
import org.laladev.moneyjinn.businesslogic.dao.data.mapper.MoneyflowSearchParamsDataMapper;
import org.laladev.moneyjinn.businesslogic.dao.data.mapper.MoneyflowSearchResultDataMapper;
import org.laladev.moneyjinn.businesslogic.dao.data.mapper.PostingAccountAmountDataMapper;
import org.laladev.moneyjinn.businesslogic.model.Contractpartner;
import org.laladev.moneyjinn.businesslogic.model.ContractpartnerID;
import org.laladev.moneyjinn.businesslogic.model.PostingAccount;
import org.laladev.moneyjinn.businesslogic.model.PostingAccountAmount;
import org.laladev.moneyjinn.businesslogic.model.PostingAccountID;
import org.laladev.moneyjinn.businesslogic.model.access.AccessRelation;
import org.laladev.moneyjinn.businesslogic.model.access.Group;
import org.laladev.moneyjinn.businesslogic.model.access.GroupID;
import org.laladev.moneyjinn.businesslogic.model.access.User;
import org.laladev.moneyjinn.businesslogic.model.access.UserID;
import org.laladev.moneyjinn.businesslogic.model.capitalsource.Capitalsource;
import org.laladev.moneyjinn.businesslogic.model.capitalsource.CapitalsourceID;
import org.laladev.moneyjinn.businesslogic.model.exception.BusinessException;
import org.laladev.moneyjinn.businesslogic.model.moneyflow.Moneyflow;
import org.laladev.moneyjinn.businesslogic.model.moneyflow.MoneyflowID;
import org.laladev.moneyjinn.businesslogic.model.moneyflow.search.MoneyflowSearchParams;
import org.laladev.moneyjinn.businesslogic.model.moneyflow.search.MoneyflowSearchResult;
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
		super.registerBeanMapper(new PostingAccountAmountDataMapper());
		super.registerBeanMapper(new MoneyflowSearchParamsDataMapper());
		super.registerBeanMapper(new MoneyflowSearchResultDataMapper());
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

	private final List<Moneyflow> mapMoneyflowDataList(final List<MoneyflowData> moneyflowDataList) {
		return moneyflowDataList.stream().map(element -> this.mapMoneyflowData(element))
				.collect(Collectors.toCollection(ArrayList::new));
	}

	private List<PostingAccountAmount> mapPostingAccountAmountDataList(
			final List<PostingAccountAmountData> postingAccountAmountDatas) {
		final List<PostingAccountAmount> postingAccountAmounts = super.mapList(postingAccountAmountDatas,
				PostingAccountAmount.class);
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
		final LocalDate bookingDate = moneyflow.getBookingDate();

		if (bookingDate == null) {
			validationResult.addValidationResultItem(
					new ValidationResultItem(moneyflow.getId(), ErrorCode.BOOKINGDATE_IN_WRONG_FORMAT));
		} else {
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
			} else if (bookingDate != null && (bookingDate.isBefore(capitalsource.getValidFrom())
					|| bookingDate.isAfter(capitalsource.getValidTil()))) {
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
			} else if (bookingDate != null && (bookingDate.isBefore(contractpartner.getValidFrom())
					|| bookingDate.isAfter(contractpartner.getValidTil()))) {
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
		Assert.notNull(moneyflows);

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
			final Cache monthsCache = super.getCache(CacheNames.MONEYFLOW_MONTH, userId.getId().toString());
			final Cache yearsCache = super.getCache(CacheNames.MONEYFLOW_YEARS);
			if (moneyflowIdCache != null) {
				moneyflowIdCache.evict(new SimpleKey(userId, moneyflowId));
			}
			if (monthsCache != null) {
				monthsCache.clear();
			}
			if (yearsCache != null) {
				yearsCache.evict(userId);
			}
		}
	}

	@Override
	public BigDecimal getSumAmountByDateRangeForCapitalsourceId(final UserID userId, final LocalDate dateFrom,
			final LocalDate dateTil, final CapitalsourceID capitalsourceId) {
		return this.getSumAmountByDateRangeForCapitalsourceIds(userId, dateFrom, dateTil,
				Arrays.asList(capitalsourceId));
	}

	@Override
	@Cacheable(value = CacheNames.MONEYFLOW_YEARS)
	public List<Short> getAllYears(final UserID userId) {
		Assert.notNull(userId);

		return this.moneyflowDao.getAllYears(userId.getId());
	}

	@Override
	public List<Month> getAllMonth(final UserID userId, final Short year) {
		Assert.notNull(userId);
		Assert.notNull(year);

		final Cache cache = super.getCache(CacheNames.MONEYFLOW_MONTH, userId.getId().toString());
		@SuppressWarnings("unchecked")
		List<Month> months = cache.get(year, List.class);

		if (months != null) {
			return months;
		}

		final LocalDate beginOfYear = LocalDate.of(year, Month.JANUARY, 1);
		final LocalDate endOfYear = LocalDate.of(year, Month.DECEMBER, 31);

		final List<Short> allMonths = this.moneyflowDao.getAllMonth(userId.getId(), Date.valueOf(beginOfYear),
				Date.valueOf(endOfYear));

		if (allMonths == null || allMonths.isEmpty()) {
			months = new ArrayList<>();
		} else {
			months = allMonths.stream().map(m -> Month.of(m.intValue()))
					.collect(Collectors.toCollection(ArrayList::new));
		}

		cache.put(year, months);

		return months;
	}

	@Override
	public List<Moneyflow> getAllMoneyflowsByDateRange(final UserID userId, final LocalDate dateFrom,
			final LocalDate dateTil) {
		Assert.notNull(userId);
		Assert.notNull(dateFrom);
		Assert.notNull(dateTil);

		final List<MoneyflowData> moneyflowDatas = this.moneyflowDao.getAllMoneyflowsByDateRange(userId.getId(),
				Date.valueOf(dateFrom), Date.valueOf(dateTil));

		return this.mapMoneyflowDataList(moneyflowDatas);
	}

	@Override
	public boolean monthHasMoneyflows(final UserID userId, final Short year, final Month month) {
		Assert.notNull(userId);
		Assert.notNull(year);
		Assert.notNull(month);

		final LocalDate beginOfMonth = LocalDate.of(year, month, 1);
		final LocalDate endOfMonth = beginOfMonth.with(TemporalAdjusters.lastDayOfMonth());

		return this.moneyflowDao.monthHasMoneyflows(userId.getId(), Date.valueOf(beginOfMonth),
				Date.valueOf(endOfMonth));
	}

	@Override
	public BigDecimal getSumAmountByDateRangeForCapitalsourceIds(final UserID userId, final LocalDate dateFrom,
			final LocalDate dateTil, final List<CapitalsourceID> capitalsourceIds) {
		Assert.notNull(userId);
		Assert.notNull(dateFrom);
		Assert.notNull(dateTil);
		Assert.notNull(capitalsourceIds);

		final List<Long> capitalsourceIdLongs = capitalsourceIds.stream().map(CapitalsourceID::getId)
				.collect(Collectors.toCollection(ArrayList::new));

		return this.moneyflowDao.getSumAmountByDateRangeForCapitalsourceIds(userId.getId(), Date.valueOf(dateFrom),
				Date.valueOf(dateTil), capitalsourceIdLongs);
	}

	@Override
	public LocalDate getMaxMoneyflowDate(final UserID userId) {
		final Date date = this.moneyflowDao.getMaxMoneyflowDate(userId.getId());
		if (date != null) {
			return date.toLocalDate();
		}
		return null;
	}

	@Override
	public LocalDate getPreviousMoneyflowDate(final UserID userId, final LocalDate date) {
		Assert.notNull(userId);
		Assert.notNull(date);

		final Date sqlDate = this.moneyflowDao.getPreviousMoneyflowDate(userId.getId(), Date.valueOf(date));
		if (sqlDate != null) {
			return sqlDate.toLocalDate();
		}
		return null;
	}

	@Override
	public LocalDate getNextMoneyflowDate(final UserID userId, final LocalDate date) {
		Assert.notNull(userId);
		Assert.notNull(date);

		final Date sqlDate = this.moneyflowDao.getNextMoneyflowDate(userId.getId(), Date.valueOf(date));
		if (sqlDate != null) {
			return sqlDate.toLocalDate();
		}
		return null;
	}

	@Override
	public List<PostingAccountAmount> getAllMoneyflowsByDateRangeGroupedByYearMonthPostingAccount(final UserID userId,
			final List<PostingAccountID> postingAccountIds, final LocalDate dateFrom, final LocalDate dateTil) {
		Assert.notNull(userId);
		Assert.notNull(dateFrom);
		Assert.notNull(dateTil);
		Assert.notEmpty(postingAccountIds);

		final List<Long> postingAccountIdLongs = postingAccountIds.stream().map(PostingAccountID::getId)
				.collect(Collectors.toCollection(ArrayList::new));

		final List<PostingAccountAmountData> postingAccountAmountDatas = this.moneyflowDao
				.getAllMoneyflowsByDateRangeGroupedByYearMonthPostingAccount(userId.getId(), postingAccountIdLongs,
						Date.valueOf(dateFrom), Date.valueOf(dateTil));
		return this.mapPostingAccountAmountDataList(postingAccountAmountDatas);

	}

	@Override
	public List<PostingAccountAmount> getAllMoneyflowsByDateRangeGroupedByYearPostingAccount(final UserID userId,
			final List<PostingAccountID> postingAccountIds, final LocalDate dateFrom, final LocalDate dateTil) {
		Assert.notNull(userId);
		Assert.notNull(dateFrom);
		Assert.notNull(dateTil);
		Assert.notEmpty(postingAccountIds);

		final List<Long> postingAccountIdLongs = postingAccountIds.stream().map(PostingAccountID::getId)
				.collect(Collectors.toCollection(ArrayList::new));

		final List<PostingAccountAmountData> postingAccountAmountDatas = this.moneyflowDao
				.getAllMoneyflowsByDateRangeGroupedByYearPostingAccount(userId.getId(), postingAccountIdLongs,
						Date.valueOf(dateFrom), Date.valueOf(dateTil));
		return this.mapPostingAccountAmountDataList(postingAccountAmountDatas);
	}

	@Override
	public List<Moneyflow> searchMoneyflowsByAmountDate(final UserID userId, final LocalDate bookingDate,
			final BigDecimal amount, final Period searchPeriod) {
		Assert.notNull(userId);
		Assert.notNull(bookingDate);
		Assert.notNull(amount);
		Assert.notNull(searchPeriod);

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
				Date.valueOf(dateFrom), Date.valueOf(dateTil), amount);

		return this.mapMoneyflowDataList(moneyflowDatas);
	}

	@Override
	public List<MoneyflowSearchResult> searchMoneyflows(final UserID userId,
			final MoneyflowSearchParams moneyflowSearchParams) {
		Assert.notNull(userId);
		Assert.notNull(moneyflowSearchParams);

		if (moneyflowSearchParams.getStartDate() == null) {
			moneyflowSearchParams.setStartDate(LocalDate.of(0, Month.JANUARY, 1));
		}
		if (moneyflowSearchParams.getEndDate() == null) {
			moneyflowSearchParams.setEndDate(LocalDate.of(9999, Month.DECEMBER, 31));
		}

		final MoneyflowSearchParamsData moneyflowSearchParamsData = super.map(moneyflowSearchParams,
				MoneyflowSearchParamsData.class);

		final List<MoneyflowSearchResultData> moneyflowSearchResultDatas = this.moneyflowDao
				.searchMoneyflows(userId.getId(), moneyflowSearchParamsData);
		final List<MoneyflowSearchResult> moneyflowSearchResults = super.mapList(moneyflowSearchResultDatas,
				MoneyflowSearchResult.class);

		for (final MoneyflowSearchResult moneyflowSearchResult : moneyflowSearchResults) {
			if (moneyflowSearchResult.getContractpartner() != null) {
				final ContractpartnerID contractpartnerId = moneyflowSearchResult.getContractpartner().getId();
				final Contractpartner contractpartner = this.contractpartnerService.getContractpartnerById(userId,
						contractpartnerId);
				moneyflowSearchResult.setContractpartner(contractpartner);
			}
		}

		return moneyflowSearchResults;
	}

	@Override
	public List<Moneyflow> getAllMoneyflowsByDateRangeCapitalsourceId(final UserID userId, final LocalDate dateFrom,
			final LocalDate dateTil, final CapitalsourceID capitalsourceId) {
		Assert.notNull(userId);
		Assert.notNull(dateFrom);
		Assert.notNull(dateTil);
		Assert.notNull(capitalsourceId);

		final List<MoneyflowData> moneyflowDatas = this.moneyflowDao.getAllMoneyflowsByDateRangeCapitalsourceId(
				userId.getId(), Date.valueOf(dateFrom), Date.valueOf(dateTil), capitalsourceId.getId());

		return this.mapMoneyflowDataList(moneyflowDatas);
	}

}
