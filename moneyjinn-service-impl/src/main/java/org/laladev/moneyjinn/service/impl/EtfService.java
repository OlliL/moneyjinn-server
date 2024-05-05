//
// Copyright (c) 2021-2024 Oliver Lehmann <lehmann@ans-netz.de>
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
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.stream.Collectors;

import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.model.access.GroupID;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.etf.Etf;
import org.laladev.moneyjinn.model.etf.EtfFlow;
import org.laladev.moneyjinn.model.etf.EtfFlowComparator;
import org.laladev.moneyjinn.model.etf.EtfFlowID;
import org.laladev.moneyjinn.model.etf.EtfFlowWithTaxInfo;
import org.laladev.moneyjinn.model.etf.EtfID;
import org.laladev.moneyjinn.model.etf.EtfIsin;
import org.laladev.moneyjinn.model.etf.EtfPreliminaryLumpSum;
import org.laladev.moneyjinn.model.etf.EtfPreliminaryLumpSumID;
import org.laladev.moneyjinn.model.etf.EtfValue;
import org.laladev.moneyjinn.model.exception.BusinessException;
import org.laladev.moneyjinn.model.validation.ValidationResult;
import org.laladev.moneyjinn.model.validation.ValidationResultItem;
import org.laladev.moneyjinn.service.CacheNames;
import org.laladev.moneyjinn.service.api.IAccessRelationService;
import org.laladev.moneyjinn.service.api.IEtfService;
import org.laladev.moneyjinn.service.api.IGroupService;
import org.laladev.moneyjinn.service.api.IUserService;
import org.laladev.moneyjinn.service.dao.EtfDao;
import org.laladev.moneyjinn.service.dao.data.EtfData;
import org.laladev.moneyjinn.service.dao.data.EtfFlowData;
import org.laladev.moneyjinn.service.dao.data.EtfPreliminaryLumpSumData;
import org.laladev.moneyjinn.service.dao.data.EtfValueData;
import org.laladev.moneyjinn.service.dao.data.mapper.EtfDataMapper;
import org.laladev.moneyjinn.service.dao.data.mapper.EtfFlowDataMapper;
import org.laladev.moneyjinn.service.dao.data.mapper.EtfPreliminaryLumpSumDataMapper;
import org.laladev.moneyjinn.service.dao.data.mapper.EtfValueDataMapper;
import org.springframework.cache.interceptor.SimpleKey;
import org.springframework.util.Assert;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Named
@RequiredArgsConstructor(onConstructor = @__(@Inject))
@Log
public class EtfService extends AbstractService implements IEtfService {
	private static final String STILL_REFERENCED = "You may not delete an ETF while it is referenced by a flows or preliminary lump sums!";
	private static final String USER_ID_MUST_NOT_BE_NULL = "UserId must not be null!";
	private static final String ETF_MUST_NOT_BE_NULL = "ETF must not be null!";

	private final EtfDao etfDao;
	private final EtfFlowDataMapper etfFlowDataMapper;
	private final EtfValueDataMapper etfValueDataMapper;
	private final EtfDataMapper etfDataMapper;
	private final EtfPreliminaryLumpSumDataMapper etfPreliminaryLumpSumDataMapper;

	private final IUserService userService;
	private final IGroupService groupService;
	private final IAccessRelationService accessRelationService;

	@Override
	@PostConstruct
	protected void addBeanMapper() {
		super.registerBeanMapper(this.etfFlowDataMapper);
		super.registerBeanMapper(this.etfValueDataMapper);
		super.registerBeanMapper(this.etfDataMapper);
		super.registerBeanMapper(this.etfPreliminaryLumpSumDataMapper);
	}

	//
	// Etf
	//

	@Override
	public ValidationResult validateEtf(final Etf etf) {
		Assert.notNull(etf, ETF_MUST_NOT_BE_NULL);
		Assert.notNull(etf.getUser(), "etf.user must not be null!");
		Assert.notNull(etf.getUser().getId(), "etf.user.id must not be null!");
		Assert.notNull(etf.getGroup(), "etf.group must not be null!");
		Assert.notNull(etf.getGroup().getId(), "etf.group.id must not be null!");

		final ValidationResult validationResult = new ValidationResult();
		final Consumer<ErrorCode> addResult = (final ErrorCode errorCode) -> validationResult.addValidationResultItem(
				new ValidationResultItem(etf.getId(), errorCode));

		if (etf.getIsin() == null || etf.getIsin().getId().isBlank()) {
			addResult.accept(ErrorCode.ISIN_MUST_NOT_BE_EMPTY);
		}
		if (etf.getWkn() == null || etf.getWkn().isBlank()) {
			addResult.accept(ErrorCode.WKN_MUST_NOT_BE_EMPTY);
		}
		if (etf.getTicker() == null || etf.getTicker().isBlank()) {
			addResult.accept(ErrorCode.TICKER_MUST_NOT_BE_EMPTY);
		}
		if (etf.getName() == null || etf.getName().isBlank()) {
			addResult.accept(ErrorCode.NAME_MUST_NOT_BE_EMPTY);
		}
		if (etf.getChartUrl() != null && etf.getChartUrl().isBlank())
			etf.setChartUrl(null);

		return validationResult;
	}

	private Etf mapEtfData(final EtfData etfData) {
		if (etfData != null) {
			final Etf etf = super.map(etfData, Etf.class);

			this.userService.enrichEntity(etf);
			this.groupService.enrichEntity(etf);

			return etf;
		}
		return null;
	}

	private List<Etf> mapEtfDataList(final List<EtfData> etfDataList) {
		return etfDataList.stream().map(this::mapEtfData).toList();
	}

	@Override
	public List<Etf> getAllEtf(final UserID userId) {
		Assert.notNull(userId, USER_ID_MUST_NOT_BE_NULL);

		final List<EtfData> etfData = this.etfDao.getAllEtf(userId.getId());
		return this.mapEtfDataList(etfData);
	}

	private void evictEtfCache(final UserID userId, final EtfID etfId) {
		if (etfId != null) {
			this.accessRelationService.getAllUserWithSameGroup(userId).forEach(evictingUserId -> {
				super.evictFromCache(CacheNames.ETF_BY_ID, new SimpleKey(evictingUserId, etfId));
			});
		}
	}

	@Override
	public Etf getEtfById(final UserID userId, final EtfID etfId) {
		Assert.notNull(userId, USER_ID_MUST_NOT_BE_NULL);
		Assert.notNull(etfId, "etfId must not be null!");

		final Supplier<Etf> supplier = () -> this.mapEtfData(
				this.etfDao.getEtfById(userId.getId(), etfId.getId()));

		return super.getFromCacheOrExecute(CacheNames.ETF_BY_ID, new SimpleKey(userId, etfId), supplier, Etf.class);
	}

	@Override
	public void updateEtf(final Etf etf) {
		Assert.notNull(etf, ETF_MUST_NOT_BE_NULL);
		final ValidationResult validationResult = this.validateEtf(etf);
		if (!validationResult.isValid() && !validationResult.getValidationResultItems().isEmpty()) {
			final ValidationResultItem validationResultItem = validationResult.getValidationResultItems().get(0);
			throw new BusinessException("Etf update failed!", validationResultItem.getError());
		}
		final EtfData etfData = super.map(etf, EtfData.class);
		this.etfDao.updateEtf(etfData);
		this.evictEtfCache(etf.getUser().getId(), etf.getId());
	}

	@Override
	public EtfID createEtf(final Etf etf) {
		Assert.notNull(etf, ETF_MUST_NOT_BE_NULL);
		etf.setId(null);
		final ValidationResult validationResult = this.validateEtf(etf);
		if (!validationResult.isValid() && !validationResult.getValidationResultItems().isEmpty()) {
			final ValidationResultItem validationResultItem = validationResult.getValidationResultItems().get(0);
			throw new BusinessException("Etf creation failed!", validationResultItem.getError());
		}
		final EtfData etfData = super.map(etf, EtfData.class);
		final Long etfIdLong = this.etfDao.createEtf(etfData);
		final EtfID etfId = new EtfID(etfIdLong);
		etf.setId(etfId);
		return etfId;
	}

	@Override
	public void deleteEtf(final UserID userId, final GroupID groupId, final EtfID etfId) {
		Assert.notNull(userId, USER_ID_MUST_NOT_BE_NULL);
		Assert.notNull(groupId, "GroupId must not be null!");
		Assert.notNull(etfId, "EtfId must not be null!");

		final Etf etf = this.getEtfById(userId, etfId);
		if (etf != null) {
			try {
				this.etfDao.deleteEtf(groupId.getId(), etfId.getId());
				this.evictEtfCache(userId, etfId);
			} catch (final Exception e) {
				log.log(Level.INFO, STILL_REFERENCED, e);
				throw new BusinessException(STILL_REFERENCED, ErrorCode.ETF_STILL_REFERENCED);
			}
		}
	}

	//
	// EtfFlow
	//

	@Override
	public List<EtfFlow> getAllEtfFlowsUntil(final UserID userId, final EtfID etfId, final LocalDateTime timeUntil) {
		if (this.getEtfById(userId, etfId) != null) {
			final List<EtfFlowData> etfFlowData = this.etfDao.getAllFlowsUntil(etfId.getId(), timeUntil);
			return super.mapList(etfFlowData, EtfFlow.class);
		}
		return Collections.emptyList();
	}

	@Override
	public ValidationResult validateEtfFlow(final UserID userId, final EtfFlow etfFlow) {
		Assert.notNull(userId, USER_ID_MUST_NOT_BE_NULL);
		Assert.notNull(etfFlow, "etfFlow must not be null!");

		final ValidationResult validationResult = new ValidationResult();
		final Consumer<ErrorCode> addResult = (final ErrorCode errorCode) -> validationResult.addValidationResultItem(
				new ValidationResultItem(etfFlow.getId(), errorCode));

		if (etfFlow.getTime() == null) {
			addResult.accept(ErrorCode.BOOKINGDATE_IN_WRONG_FORMAT);
		}

		if (etfFlow.getAmount() == null || etfFlow.getAmount().compareTo(BigDecimal.ZERO) == 0) {
			addResult.accept(ErrorCode.PIECES_NOT_SET);
		}

		if (etfFlow.getPrice() == null || etfFlow.getPrice().compareTo(BigDecimal.ZERO) == 0) {
			addResult.accept(ErrorCode.PRICE_NOT_SET);
		}

		if (etfFlow.getEtfId() == null || etfFlow.getEtfId().getId() == null) {
			addResult.accept(ErrorCode.NO_ETF_SPECIFIED);
		} else {
			final Etf etf = this.getEtfById(userId, etfFlow.getEtfId());
			if (etf == null) {
				addResult.accept(ErrorCode.NO_ETF_SPECIFIED);
			}
		}

		return validationResult;
	}

	@Override
	public EtfFlow getEtfFlowById(final UserID userId, final EtfFlowID etfFlowId) {
		Assert.notNull(userId, USER_ID_MUST_NOT_BE_NULL);
		Assert.notNull(etfFlowId, "etfFlowId must not be null!");
		final EtfFlowData etfFlowData = this.etfDao.getEtfFowById(etfFlowId.getId());
		if (etfFlowData != null) {
			final EtfFlow etfFlow = super.map(etfFlowData, EtfFlow.class);
			if (this.getEtfById(userId, etfFlow.getEtfId()) != null) {
				return etfFlow;
			}
		}
		return null;
	}

	@Override
	public EtfFlowID createEtfFlow(final UserID userId, final EtfFlow etfFlow) {
		Assert.notNull(userId, USER_ID_MUST_NOT_BE_NULL);
		final ValidationResult validationResult = this.validateEtfFlow(userId, etfFlow);
		if (!validationResult.isValid() && !validationResult.getValidationResultItems().isEmpty()) {
			final ValidationResultItem validationResultItem = validationResult.getValidationResultItems().get(0);
			throw new BusinessException("EtfFlow creation failed!", validationResultItem.getError());
		}
		final EtfFlowData etfFlowData = super.map(etfFlow, EtfFlowData.class);
		final Long etfFlowId = this.etfDao.createEtfFlow(etfFlowData);
		return new EtfFlowID(etfFlowId);
	}

	@Override
	public void updateEtfFlow(final UserID userId, final EtfFlow etfFlow) {
		Assert.notNull(userId, USER_ID_MUST_NOT_BE_NULL);
		final ValidationResult validationResult = this.validateEtfFlow(userId, etfFlow);
		if (!validationResult.isValid() && !validationResult.getValidationResultItems().isEmpty()) {
			final ValidationResultItem validationResultItem = validationResult.getValidationResultItems().get(0);
			throw new BusinessException("EtfFlow update failed!", validationResultItem.getError());
		}
		final EtfFlowData etfFlowData = super.map(etfFlow, EtfFlowData.class);
		this.etfDao.updateEtfFlow(etfFlowData);
	}

	@Override
	public void deleteEtfFlow(final UserID userId, final EtfFlowID etfFlowId) {
		Assert.notNull(userId, USER_ID_MUST_NOT_BE_NULL);
		Assert.notNull(etfFlowId, "etfFlowId must not be null!");

		final EtfFlow etfFlow = this.getEtfFlowById(userId, etfFlowId);
		if (etfFlow != null) {
			this.etfDao.deleteEtfFlow(etfFlowId.getId());
		}
	}

	@Override
	public List<EtfFlowWithTaxInfo> calculateEffectiveEtfFlows(final UserID userId, final List<EtfFlow> allEtfFlows) {
		final LocalDateTime now = LocalDate.now().atTime(LocalTime.MAX);
		final Map<EtfID, List<EtfFlow>> etfFlowsByEtfIdMap = allEtfFlows.stream()
				.collect(Collectors.groupingBy(EtfFlow::getEtfId));

		final List<EtfFlowWithTaxInfo> relevantEtfFlows = new ArrayList<>();
		for (final var etfFlowsByEtfIdMapEntry : etfFlowsByEtfIdMap.entrySet()) {
			final var etfFlows = etfFlowsByEtfIdMapEntry.getValue();
			final var etfPreliminaryLumpSums = this.getAllEtfPreliminaryLumpSums(userId,
					etfFlowsByEtfIdMapEntry.getKey());
			final List<EtfFlow> etfBuyFlows = this.calculateEffectiveEtfFlowsUntil(etfFlows, now);

			// initialize accumulated preliminary lump sum to 0 for each effective flow
			final List<EtfFlowWithTaxInfo> etfFlowWithTaxInfos = etfBuyFlows.stream().map(ef -> {
				final EtfFlowWithTaxInfo etfFlowWithTaxInfo = new EtfFlowWithTaxInfo(ef);
				etfFlowWithTaxInfo.setAccumulatedPreliminaryLumpSum(BigDecimal.ZERO);
				return etfFlowWithTaxInfo;
			}).toList();

			// delete all preliminary lump sums older than the earliest effective flow
			final Year yearOfEarliestEtfBuyFlow = Year.from(etfFlowWithTaxInfos.get(0).getTime());
			etfPreliminaryLumpSums.removeIf(epls -> epls.getYear().isBefore(yearOfEarliestEtfBuyFlow));

			for (final var etfPreliminaryLumpSum : etfPreliminaryLumpSums) {
				for (final Month month : Month.values()) {
					final LocalDateTime startOfMonth = etfPreliminaryLumpSum.getYear().atMonth(month).atDay(1)
							.atStartOfDay();
					final LocalDateTime endOfMonth = etfPreliminaryLumpSum.getYear().atMonth(month).atEndOfMonth()
							.atTime(LocalTime.MAX);
					final BigDecimal pieceTax = this.getPieceTax(month, etfPreliminaryLumpSum, allEtfFlows);

					if (month.equals(Month.JANUARY)) {
						etfFlowWithTaxInfos.stream().filter(efwti -> !efwti.getTime().isAfter(endOfMonth))
								.forEach(efwti -> efwti.setAccumulatedPreliminaryLumpSum(
										efwti.getAccumulatedPreliminaryLumpSum()
												.add(efwti.getAmount().multiply(pieceTax))));
					} else {
						etfFlowWithTaxInfos.stream().filter(
								efwti -> !efwti.getTime().isAfter(endOfMonth) && !startOfMonth.isAfter(efwti.getTime()))
								.forEach(efwti -> efwti.setAccumulatedPreliminaryLumpSum(efwti
										.getAccumulatedPreliminaryLumpSum().add(efwti.getAmount().multiply(pieceTax))));
					}
				}
			}

			relevantEtfFlows.addAll(etfFlowWithTaxInfos);
		}

		return relevantEtfFlows;
	}

	private BigDecimal getPieceTax(final Month month, final EtfPreliminaryLumpSum etfPreliminaryLumpSum,
			final List<EtfFlow> allEtfFlows) {
		final LocalDateTime startOfMonth = etfPreliminaryLumpSum.getYear().atMonth(month).atDay(1).atStartOfDay();
		final LocalDateTime endOfMonth = etfPreliminaryLumpSum.getYear().atMonth(month).atEndOfMonth()
				.atTime(LocalTime.MAX);
		final List<EtfFlow> relevantTaxFlows = this.calculateEffectiveEtfFlowsUntil(allEtfFlows, endOfMonth);

		final BigDecimal amount = switch (month) {
		case JANUARY -> etfPreliminaryLumpSum.getAmountJanuary();
		case FEBRUARY -> etfPreliminaryLumpSum.getAmountFebruary();
		case MARCH -> etfPreliminaryLumpSum.getAmountMarch();
		case APRIL -> etfPreliminaryLumpSum.getAmountApril();
		case MAY -> etfPreliminaryLumpSum.getAmountMay();
		case JUNE -> etfPreliminaryLumpSum.getAmountJune();
		case JULY -> etfPreliminaryLumpSum.getAmountJuly();
		case AUGUST -> etfPreliminaryLumpSum.getAmountAugust();
		case SEPTEMBER -> etfPreliminaryLumpSum.getAmountSeptember();
		case OCTOBER -> etfPreliminaryLumpSum.getAmountOctober();
		case NOVEMBER -> etfPreliminaryLumpSum.getAmountNovember();
		case DECEMBER -> etfPreliminaryLumpSum.getAmountDecember();
		};

		if (month.equals(Month.JANUARY)) {
			return this.calculatePieceTax(amount, relevantTaxFlows);
		} else {
			final List<EtfFlow> relevantTaxFlowsForThisMonth = relevantTaxFlows.stream()
					.filter(rtf -> !startOfMonth.isAfter(rtf.getTime())).toList();
			if (!relevantTaxFlowsForThisMonth.isEmpty()) {
				return this.calculatePieceTax(amount, relevantTaxFlowsForThisMonth);
			}
		}

		return BigDecimal.ZERO;

	}

	private BigDecimal calculatePieceTax(final BigDecimal amount, final List<EtfFlow> relevantTaxFlows) {
		final BigDecimal amountSum = relevantTaxFlows.stream().map(EtfFlow::getAmount).reduce(BigDecimal.ZERO,
				BigDecimal::add);
		return amount.divide(amountSum, 10, RoundingMode.HALF_UP);
	}

	private List<EtfFlow> calculateEffectiveEtfFlowsUntil(final List<EtfFlow> etfFlows, final LocalDateTime until) {
		Collections.sort(etfFlows, new EtfFlowComparator());
		final List<EtfFlow> etfSalesFlows = etfFlows.stream()
				.filter(ef -> ef.getAmount().compareTo(BigDecimal.ZERO) < 0).filter(ef -> ef.getTime().isBefore(until))
				.toList();
		final List<EtfFlow> etfBuyFlows = etfFlows.stream().filter(ef -> ef.getAmount().compareTo(BigDecimal.ZERO) > -1)
				.filter(ef -> ef.getTime().isBefore(until)).collect(Collectors.toList());
		for (final EtfFlow etfSalesFlow : etfSalesFlows) {
			BigDecimal salesAmount = etfSalesFlow.getAmount().negate();
			final ListIterator<EtfFlow> etfBuyFlowsIterator = etfBuyFlows.listIterator();
			while (etfBuyFlowsIterator.hasNext() && salesAmount.compareTo(BigDecimal.ZERO) > 0) {
				final EtfFlow etfBuyFlow = etfBuyFlowsIterator.next();
				if (salesAmount.compareTo(etfBuyFlow.getAmount()) >= 0) {
					etfBuyFlowsIterator.remove();
					salesAmount = salesAmount.subtract(etfBuyFlow.getAmount());
				} else {
					final BigDecimal newAmount = etfBuyFlow.getAmount().subtract(salesAmount);
					// Don't modify the elements in the parameterlist - create a new object instead.
					final EtfFlow firstAndReducedEtfFlow = new EtfFlow(etfBuyFlow);
					firstAndReducedEtfFlow.setAmount(newAmount);
					etfBuyFlowsIterator.set(firstAndReducedEtfFlow);
					salesAmount = BigDecimal.ZERO;
				}
			}
		}
		return etfBuyFlows;
	}

	//
	// Etf Preliminary Lump Sum
	//

	@Override
	public ValidationResult validateEtfPreliminaryLumpSum(final UserID userId,
			final EtfPreliminaryLumpSum etfPreliminaryLumpSum) {
		Assert.notNull(userId, USER_ID_MUST_NOT_BE_NULL);
		Assert.notNull(etfPreliminaryLumpSum, "etfPreliminaryLumpSum must not be null!");

		final ValidationResult validationResult = new ValidationResult();
		final Consumer<ErrorCode> addResult = (final ErrorCode errorCode) -> validationResult.addValidationResultItem(
				new ValidationResultItem(null, errorCode));

		if (etfPreliminaryLumpSum.getId() == null || etfPreliminaryLumpSum.getId().getId() == null) {
			addResult.accept(ErrorCode.NO_ETF_SPECIFIED);
			addResult.accept(ErrorCode.YEAR_NOT_SET);

		} else {
			if (etfPreliminaryLumpSum.getEtfId() == null || etfPreliminaryLumpSum.getEtfId().getId() == null) {
				addResult.accept(ErrorCode.NO_ETF_SPECIFIED);
			} else {
				final Etf etf = this.getEtfById(userId, etfPreliminaryLumpSum.getEtfId());
				if (etf == null) {
					addResult.accept(ErrorCode.NO_ETF_SPECIFIED);
				}
			}

			if (etfPreliminaryLumpSum.getYear() == null) {
				addResult.accept(ErrorCode.YEAR_NOT_SET);
			}
		}

		return validationResult;
	}

	private List<EtfPreliminaryLumpSum> getAllEtfPreliminaryLumpSums(final UserID userId, final EtfID etfId) {
		Assert.notNull(userId, USER_ID_MUST_NOT_BE_NULL);
		Assert.notNull(etfId, "etfId must not be null!");

		final Etf etf = this.getEtfById(userId, etfId);
		if (etf != null) {
			final List<EtfPreliminaryLumpSumData> datas = this.etfDao.getAllPreliminaryLumpSum(etfId.getId());
			return super.mapList(datas, EtfPreliminaryLumpSum.class);
		} else {
			return null;
		}
	}

	@Override
	public EtfPreliminaryLumpSum getEtfPreliminaryLumpSum(final UserID userId, final EtfPreliminaryLumpSumID id) {
		Assert.notNull(userId, USER_ID_MUST_NOT_BE_NULL);
		Assert.notNull(id, "id must not be null!");

		final EtfPreliminaryLumpSumData data = this.etfDao.getPreliminaryLumpSum(id.getId());
		if (data != null) {
			final var etfPreliminaryLumpSum = super.map(data, EtfPreliminaryLumpSum.class);

			final Etf etf = this.getEtfById(userId, etfPreliminaryLumpSum.getEtfId());
			if (etf != null) {
				return etfPreliminaryLumpSum;
			}
		}
		return null;

	}

	@Override
	public List<Year> getAllEtfPreliminaryLumpSumYears(final UserID userId, final EtfID etfId) {
		Assert.notNull(userId, USER_ID_MUST_NOT_BE_NULL);
		Assert.notNull(etfId, "etfId must not be null!");
		final Etf etf = this.getEtfById(userId, etfId);
		if (etf != null) {
			final List<Integer> datas = this.etfDao.getAllPreliminaryLumpSumYears(etfId.getId());
			return datas.stream().map(Year::of).toList();
		} else {
			return Collections.emptyList();
		}
	}

	@Override
	public void createEtfPreliminaryLumpSum(final UserID userId, final EtfPreliminaryLumpSum etfPreliminaryLumpSum) {
		Assert.notNull(userId, USER_ID_MUST_NOT_BE_NULL);
		Assert.notNull(etfPreliminaryLumpSum, "etfPreliminaryLumpSum must not be null!");
		final ValidationResult validationResult = this.validateEtfPreliminaryLumpSum(userId, etfPreliminaryLumpSum);
		if (!validationResult.isValid() && !validationResult.getValidationResultItems().isEmpty()) {
			final ValidationResultItem validationResultItem = validationResult.getValidationResultItems().get(0);
			throw new BusinessException("EtfPreliminaryLumpSum creation failed!", validationResultItem.getError());
		}
		final var etfPreliminaryLumpSumExists = this.getEtfPreliminaryLumpSum(userId, etfPreliminaryLumpSum.getId());
		if (etfPreliminaryLumpSumExists != null) {
			throw new BusinessException("EtfPreliminaryLumpSum already exists!",
					ErrorCode.ETF_PRELIMINARY_LUMP_SUM_ALREADY_EXISTS);
		}
		final EtfPreliminaryLumpSumData data = super.map(etfPreliminaryLumpSum, EtfPreliminaryLumpSumData.class);
		this.etfDao.createPreliminaryLumpSum(data);
	}

	@Override
	public void updateEtfPreliminaryLumpSum(final UserID userId, final EtfPreliminaryLumpSum etfPreliminaryLumpSum) {
		Assert.notNull(userId, USER_ID_MUST_NOT_BE_NULL);
		Assert.notNull(etfPreliminaryLumpSum, "etfPreliminaryLumpSum must not be null!");
		final ValidationResult validationResult = this.validateEtfPreliminaryLumpSum(userId, etfPreliminaryLumpSum);
		if (!validationResult.isValid() && !validationResult.getValidationResultItems().isEmpty()) {
			final ValidationResultItem validationResultItem = validationResult.getValidationResultItems().get(0);
			throw new BusinessException("EtfPreliminaryLumpSum creation failed!", validationResultItem.getError());
		}
		final var etfPreliminaryLumpSumExists = this.getEtfPreliminaryLumpSum(userId, etfPreliminaryLumpSum.getId());
		if (etfPreliminaryLumpSumExists == null) {
			throw new BusinessException("EtfPreliminaryLumpSum does not exist!",
					ErrorCode.ETF_PRELIMINARY_LUMP_SUM_DOES_NOT_EXIST);
		}
		final EtfPreliminaryLumpSumData data = super.map(etfPreliminaryLumpSum, EtfPreliminaryLumpSumData.class);
		this.etfDao.updatePreliminaryLumpSum(data);
	}

	@Override
	public void deleteEtfPreliminaryLumpSum(final UserID userId, final EtfPreliminaryLumpSumID id) {
		Assert.notNull(userId, USER_ID_MUST_NOT_BE_NULL);
		Assert.notNull(id, "ID must not be null!");
		Assert.notNull(id.getId(), "ID must not be null!");

		if (this.getEtfPreliminaryLumpSum(userId, id) != null) {
			this.etfDao.deletePreliminaryLumpSum(id.getId());
		}
	}

	//
	// ETF value
	//

	@Override
	public EtfValue getEtfValueEndOfMonth(final EtfIsin etfIsin, final Year year, final Month month) {
		final EtfValueData etfValueData = this.etfDao.getEtfValueForMonth(etfIsin, year, month);
		return super.map(etfValueData, EtfValue.class);
	}
}
