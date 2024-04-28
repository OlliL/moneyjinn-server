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
import java.util.stream.Collectors;

import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.model.etf.Etf;
import org.laladev.moneyjinn.model.etf.EtfFlow;
import org.laladev.moneyjinn.model.etf.EtfFlowComparator;
import org.laladev.moneyjinn.model.etf.EtfFlowID;
import org.laladev.moneyjinn.model.etf.EtfFlowWithTaxInfo;
import org.laladev.moneyjinn.model.etf.EtfIsin;
import org.laladev.moneyjinn.model.etf.EtfPreliminaryLumpSum;
import org.laladev.moneyjinn.model.etf.EtfPreliminaryLumpSumID;
import org.laladev.moneyjinn.model.etf.EtfPreliminaryLumpSumIDValues;
import org.laladev.moneyjinn.model.etf.EtfValue;
import org.laladev.moneyjinn.model.exception.BusinessException;
import org.laladev.moneyjinn.model.validation.ValidationResult;
import org.laladev.moneyjinn.model.validation.ValidationResultItem;
import org.laladev.moneyjinn.service.api.IEtfService;
import org.laladev.moneyjinn.service.dao.EtfDao;
import org.laladev.moneyjinn.service.dao.data.EtfData;
import org.laladev.moneyjinn.service.dao.data.EtfFlowData;
import org.laladev.moneyjinn.service.dao.data.EtfPreliminaryLumpSumData;
import org.laladev.moneyjinn.service.dao.data.EtfValueData;
import org.laladev.moneyjinn.service.dao.data.mapper.EtfDataMapper;
import org.laladev.moneyjinn.service.dao.data.mapper.EtfFlowDataMapper;
import org.laladev.moneyjinn.service.dao.data.mapper.EtfPreliminaryLumpSumDataMapper;
import org.laladev.moneyjinn.service.dao.data.mapper.EtfValueDataMapper;
import org.springframework.util.Assert;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;

@Named
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class EtfService extends AbstractService implements IEtfService {
	private final EtfDao etfDao;
	private final EtfFlowDataMapper etfFlowDataMapper;
	private final EtfValueDataMapper etfValueDataMapper;
	private final EtfDataMapper etfDataMapper;
	private final EtfPreliminaryLumpSumDataMapper etfPreliminaryLumpSumDataMapper;

	@Override
	@PostConstruct
	protected void addBeanMapper() {
		super.registerBeanMapper(this.etfFlowDataMapper);
		super.registerBeanMapper(this.etfValueDataMapper);
		super.registerBeanMapper(this.etfDataMapper);
		super.registerBeanMapper(this.etfPreliminaryLumpSumDataMapper);
	}

	@Override
	public List<Etf> getAllEtf() {
		final List<EtfData> etfData = this.etfDao.getAllEtf();
		return super.mapList(etfData, Etf.class);
	}

	@Override
	public List<EtfFlow> getAllEtfFlowsUntil(final EtfIsin isin, final LocalDateTime timeUntil) {
		final List<EtfFlowData> etfFlowData = this.etfDao.getAllFlowsUntil(isin.getId(), timeUntil);
		return super.mapList(etfFlowData, EtfFlow.class);
	}

	@Override
	public EtfValue getEtfValueEndOfMonth(final EtfIsin isin, final Year year, final Month month) {
		final EtfValueData etfValueData = this.etfDao.getEtfValueForMonth(isin.getId(), year, month);
		return super.map(etfValueData, EtfValue.class);
	}

	@Override
	public ValidationResult validateEtfFlow(final EtfFlow etfFlow) {
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

		if (etfFlow.getIsin() == null || etfFlow.getIsin().getId().isBlank()) {
			addResult.accept(ErrorCode.NO_ETF_SPECIFIED);
		} else {
			final EtfData etfData = this.etfDao.getEtfById(etfFlow.getIsin().getId());
			if (etfData == null) {
				addResult.accept(ErrorCode.NO_ETF_SPECIFIED);
			}
		}

		return validationResult;
	}

	@Override
	public ValidationResult validateEtfPreliminaryLumpSum(final EtfPreliminaryLumpSum etfPreliminaryLumpSum) {
		final ValidationResult validationResult = new ValidationResult();
		final Consumer<ErrorCode> addResult = (final ErrorCode errorCode) -> validationResult.addValidationResultItem(
				new ValidationResultItem(null, errorCode));

		if (etfPreliminaryLumpSum.getId() == null || etfPreliminaryLumpSum.getId().getId() == null) {
			addResult.accept(ErrorCode.NO_ETF_SPECIFIED);
			addResult.accept(ErrorCode.YEAR_NOT_SET);

		} else {
			final EtfPreliminaryLumpSumIDValues idValues = etfPreliminaryLumpSum.getId().getId();
			if (idValues.getEtfIsin() == null || idValues.getEtfIsin().getId().isBlank()) {
				addResult.accept(ErrorCode.NO_ETF_SPECIFIED);
			} else {
				final EtfData etfData = this.etfDao
						.getEtfById(etfPreliminaryLumpSum.getId().getId().getEtfIsin().getId());
				if (etfData == null) {
					addResult.accept(ErrorCode.NO_ETF_SPECIFIED);
				}
			}

			if (idValues.getYear() == null) {
				addResult.accept(ErrorCode.YEAR_NOT_SET);
			}
		}

		return validationResult;
	}

	@Override
	public EtfFlow getEtfFlowById(final EtfFlowID etfFlowId) {
		Assert.notNull(etfFlowId, "etfFlowId must not be null!");
		final EtfFlowData etfFlowData = this.etfDao.getEtfFowById(etfFlowId.getId());
		return super.map(etfFlowData, EtfFlow.class);
	}

	@Override
	public EtfFlowID createEtfFlow(final EtfFlow etfFlow) {
		final ValidationResult validationResult = this.validateEtfFlow(etfFlow);
		if (!validationResult.isValid() && !validationResult.getValidationResultItems().isEmpty()) {
			final ValidationResultItem validationResultItem = validationResult.getValidationResultItems().get(0);
			throw new BusinessException("EtfFlow creation failed!", validationResultItem.getError());
		}
		final EtfFlowData etfFlowData = super.map(etfFlow, EtfFlowData.class);
		final Long etfFlowId = this.etfDao.createEtfFlow(etfFlowData);
		return new EtfFlowID(etfFlowId);
	}

	@Override
	public void updateEtfFlow(final EtfFlow etfFlow) {
		final ValidationResult validationResult = this.validateEtfFlow(etfFlow);
		if (!validationResult.isValid() && !validationResult.getValidationResultItems().isEmpty()) {
			final ValidationResultItem validationResultItem = validationResult.getValidationResultItems().get(0);
			throw new BusinessException("EtfFlow update failed!", validationResultItem.getError());
		}
		final EtfFlowData etfFlowData = super.map(etfFlow, EtfFlowData.class);
		this.etfDao.updateEtfFlow(etfFlowData);
	}

	@Override
	public void deleteEtfFlow(final EtfFlowID etfFlowId) {
		Assert.notNull(etfFlowId, "etfFlowId must not be null!");
		this.etfDao.deleteEtfFlow(etfFlowId.getId());
	}

	@Override
	public List<EtfFlowWithTaxInfo> calculateEffectiveEtfFlows(final List<EtfFlow> allEtfFlows) {
		final LocalDateTime now = LocalDate.now().atTime(LocalTime.MAX);
		final Map<EtfIsin, List<EtfFlow>> etfFlowsByIsinMap = allEtfFlows.stream()
				.collect(Collectors.groupingBy(EtfFlow::getIsin));

		final List<EtfFlowWithTaxInfo> relevantEtfFlows = new ArrayList<>();
		for (final var etfFlowsByIsinMapEntry : etfFlowsByIsinMap.entrySet()) {
			final var etfFlows = etfFlowsByIsinMapEntry.getValue();
			final var etfPreliminaryLumpSums = this.getAllEtfPreliminaryLumpSums(etfFlowsByIsinMapEntry.getKey());
			final List<EtfFlow> etfBuyFlows = this.calculateEffectiveEtfFlowsUntil(etfFlows, now);

			// initialize accumulated preliminary lump sum to 0 for each effective flow
			final List<EtfFlowWithTaxInfo> etfFlowWithTaxInfos = etfBuyFlows.stream().map(ef -> {
				final EtfFlowWithTaxInfo etfFlowWithTaxInfo = new EtfFlowWithTaxInfo(ef);
				etfFlowWithTaxInfo.setAccumulatedPreliminaryLumpSum(BigDecimal.ZERO);
				return etfFlowWithTaxInfo;
			}).toList();

			// delete all preliminary lump sums older than the earliest effective flow
			final Year yearOfEarliestEtfBuyFlow = Year.from(etfFlowWithTaxInfos.get(0).getTime());
			etfPreliminaryLumpSums.removeIf(epls -> epls.getId().getId().getYear().isBefore(yearOfEarliestEtfBuyFlow));

			for (final var etfPreliminaryLumpSum : etfPreliminaryLumpSums) {
				for (final Month month : Month.values()) {
					final LocalDateTime startOfMonth = etfPreliminaryLumpSum.getId().getId().getYear().atMonth(month)
							.atDay(1).atStartOfDay();
					final LocalDateTime endOfMonth = etfPreliminaryLumpSum.getId().getId().getYear().atMonth(month)
							.atEndOfMonth().atTime(LocalTime.MAX);
					final List<EtfFlow> relevantTaxFlows = this.calculateEffectiveEtfFlowsUntil(allEtfFlows,
							endOfMonth);

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
						final BigDecimal pieceTax = this.getPieceTax(amount, relevantTaxFlows);

						etfFlowWithTaxInfos.stream().filter(efwti -> !efwti.getTime().isAfter(endOfMonth))
								.forEach(efwti -> efwti.setAccumulatedPreliminaryLumpSum(
										efwti.getAccumulatedPreliminaryLumpSum()
												.add(efwti.getAmount().multiply(pieceTax))));
					} else {
						final List<EtfFlow> relevantTaxFlowsForThisMonth = relevantTaxFlows.stream()
								.filter(rtf -> !startOfMonth.isAfter(rtf.getTime())).toList();
						if (!relevantTaxFlowsForThisMonth.isEmpty()) {
							final BigDecimal pieceTax = this.getPieceTax(amount, relevantTaxFlowsForThisMonth);

							etfFlowWithTaxInfos.stream().filter(
									efwti -> !efwti.getTime().isAfter(endOfMonth)
											&& !startOfMonth.isAfter(efwti.getTime()))
									.forEach(efwti -> efwti
											.setAccumulatedPreliminaryLumpSum(efwti.getAccumulatedPreliminaryLumpSum()
													.add(efwti.getAmount().multiply(pieceTax))));
						}
					}
				}
			}

			relevantEtfFlows.addAll(etfFlowWithTaxInfos);
		}

		return relevantEtfFlows;
	}

	private BigDecimal getPieceTax(final BigDecimal amount, final List<EtfFlow> relevantTaxFlows) {
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

	private List<EtfPreliminaryLumpSum> getAllEtfPreliminaryLumpSums(final EtfIsin isin) {
		final List<EtfPreliminaryLumpSumData> datas = this.etfDao.getAllPreliminaryLumpSum(isin.getId());
		return super.mapList(datas, EtfPreliminaryLumpSum.class);
	}

	@Override
	public EtfPreliminaryLumpSum getEtfPreliminaryLumpSum(final EtfPreliminaryLumpSumID id) {
		final var idValues = id.getId();
		final EtfPreliminaryLumpSumData data = this.etfDao.getPreliminaryLumpSum(idValues.getEtfIsin().getId(),
				idValues.getYear());
		return super.map(data, EtfPreliminaryLumpSum.class);

	}

	@Override
	public List<Year> getAllEtfPreliminaryLumpSumYears(final EtfIsin isin) {
		final List<Integer> datas = this.etfDao.getAllPreliminaryLumpSumYears(isin.getId());
		return datas.stream().map(Year::of).toList();
	}

	@Override
	public void createEtfPreliminaryLumpSum(final EtfPreliminaryLumpSum etfPreliminaryLumpSum) {
		final ValidationResult validationResult = this.validateEtfPreliminaryLumpSum(etfPreliminaryLumpSum);
		if (!validationResult.isValid() && !validationResult.getValidationResultItems().isEmpty()) {
			final ValidationResultItem validationResultItem = validationResult.getValidationResultItems().get(0);
			throw new BusinessException("EtfPreliminaryLumpSum creation failed!", validationResultItem.getError());
		}
		final EtfPreliminaryLumpSumData data = super.map(etfPreliminaryLumpSum, EtfPreliminaryLumpSumData.class);
		this.etfDao.createPreliminaryLumpSum(data);
	}

	@Override
	public void updateEtfPreliminaryLumpSum(final EtfPreliminaryLumpSum etfPreliminaryLumpSum) {
		final ValidationResult validationResult = this.validateEtfPreliminaryLumpSum(etfPreliminaryLumpSum);
		if (!validationResult.isValid() && !validationResult.getValidationResultItems().isEmpty()) {
			final ValidationResultItem validationResultItem = validationResult.getValidationResultItems().get(0);
			throw new BusinessException("EtfPreliminaryLumpSum creation failed!", validationResultItem.getError());
		}
		final EtfPreliminaryLumpSumData data = super.map(etfPreliminaryLumpSum, EtfPreliminaryLumpSumData.class);
		this.etfDao.updatePreliminaryLumpSum(data);
	}

	@Override
	public void deleteEtfPreliminaryLumpSum(final EtfPreliminaryLumpSumID id) {
		Assert.notNull(id, "ID must not be null!");
		final var idValues = id.getId();
		Assert.notNull(idValues, "ID must not be null!");
		Assert.notNull(idValues.getEtfIsin(), "ISIN must not be null!");
		Assert.notNull(idValues.getEtfIsin().getId(), "ISIN must not be null!");
		Assert.notNull(idValues.getYear(), "year must not be null!");

		this.etfDao.deletePreliminaryLumpSum(idValues.getEtfIsin().getId(), idValues.getYear());
	}
}
