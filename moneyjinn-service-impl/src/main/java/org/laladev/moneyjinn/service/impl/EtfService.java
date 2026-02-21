//
// Copyright (c) 2021-2025 Oliver Lehmann <lehmann@ans-netz.de>
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

import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.model.access.GroupID;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.etf.*;
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
import org.laladev.moneyjinn.service.event.EtfChangedEvent;
import org.laladev.moneyjinn.service.event.EventType;
import org.springframework.cache.interceptor.SimpleKey;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.Year;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.stream.Collectors;

import static java.lang.Math.abs;
import static org.springframework.util.Assert.notNull;

@Named
@RequiredArgsConstructor(onConstructor = @__(@Inject))
@Log
public class EtfService extends AbstractService implements IEtfService {
    private static final String STILL_REFERENCED =
            "You may not delete an ETF while it is referenced by a flows or preliminary lump sums!";

    private final EtfDao etfDao;
    private final EtfFlowDataMapper etfFlowDataMapper;
    private final EtfValueDataMapper etfValueDataMapper;
    private final EtfDataMapper etfDataMapper;
    private final EtfPreliminaryLumpSumDataMapper etfPreliminaryLumpSumDataMapper;

    private final IUserService userService;
    private final IGroupService groupService;
    private final IAccessRelationService accessRelationService;

    //
    // Etf
    //

    @Override
    public ValidationResult validateEtf(@NonNull final Etf etf) {
        notNull(etf.getUser(), "etf.user must not be null!");
        notNull(etf.getUser().getId(), "etf.user.id must not be null!");
        notNull(etf.getGroup(), "etf.group must not be null!");
        notNull(etf.getGroup().getId(), "etf.group.id must not be null!");

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
        if (etf.getChartUrl() != null && etf.getChartUrl().isBlank()) {
            etf.setChartUrl(null);
        }

        return validationResult;
    }

    private Etf mapEtfData(final EtfData etfData) {
        if (etfData != null) {
            final Etf etf = this.etfDataMapper.mapBToA(etfData);

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
    public List<Etf> getAllEtf(@NonNull final UserID userId) {
        final List<EtfData> etfData = this.etfDao.getAllEtf(userId.getId());
        return this.mapEtfDataList(etfData);
    }

    private void evictEtfCache(final UserID userId, final EtfID etfId) {
        if (etfId != null) {
            this.accessRelationService.getAllUserWithSameGroup(userId).forEach(
                    evictingUserId -> super.evictFromCache(CacheNames.ETF_BY_ID, new SimpleKey(evictingUserId, etfId))
            );
        }
    }

    @Override
    public Etf getEtfById(@NonNull final UserID userId, @NonNull final EtfID etfId) {
        final Supplier<Etf> supplier = () -> this.mapEtfData(
                this.etfDao.getEtfById(userId.getId(), etfId.getId()));

        return super.getFromCacheOrExecute(CacheNames.ETF_BY_ID, new SimpleKey(userId, etfId), supplier, Etf.class);
    }

    @Override
    public void updateEtf(@NonNull final Etf etf) {
        final ValidationResult validationResult = this.validateEtf(etf);
        if (!validationResult.isValid() && !validationResult.getValidationResultItems().isEmpty()) {
            final ValidationResultItem validationResultItem = validationResult.getValidationResultItems().getFirst();
            throw new BusinessException("Etf update failed!", validationResultItem.getError());
        }
        final EtfData etfData = this.etfDataMapper.mapAToB(etf);
        this.etfDao.updateEtf(etfData);
        this.evictEtfCache(etf.getUser().getId(), etf.getId());

        final var event = new EtfChangedEvent(this, EventType.UPDATE, etf);
        super.publishEvent(event);
    }

    @Override
    public EtfID createEtf(@NonNull final Etf etf) {
        etf.setId(null);
        final ValidationResult validationResult = this.validateEtf(etf);
        if (!validationResult.isValid() && !validationResult.getValidationResultItems().isEmpty()) {
            final ValidationResultItem validationResultItem = validationResult.getValidationResultItems().getFirst();
            throw new BusinessException("Etf creation failed!", validationResultItem.getError());
        }
        final EtfData etfData = this.etfDataMapper.mapAToB(etf);
        final Long etfIdLong = this.etfDao.createEtf(etfData);
        final EtfID etfId = new EtfID(etfIdLong);
        etf.setId(etfId);

        final var event = new EtfChangedEvent(this, EventType.CREATE, etf);
        super.publishEvent(event);

        return etfId;
    }

    @Override
    public void deleteEtf(@NonNull final UserID userId, @NonNull final GroupID groupId, @NonNull final EtfID etfId) {
        final Etf etf = this.getEtfById(userId, etfId);
        if (etf != null) {
            try {
                this.etfDao.deleteEtf(groupId.getId(), etfId.getId());
                this.evictEtfCache(userId, etfId);

                final var event = new EtfChangedEvent(this, EventType.DELETE, etf);
                super.publishEvent(event);

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
    public List<EtfFlow> getAllEtfFlowsUntil(@NonNull final UserID userId, @NonNull final EtfID etfId,
                                             final LocalDateTime timeUntil) {
        if (this.getEtfById(userId, etfId) != null) {
            final List<EtfFlowData> etfFlowData = this.etfDao.getAllFlowsUntil(etfId.getId(), timeUntil);
            return this.etfFlowDataMapper.mapBToA(etfFlowData);
        }
        return Collections.emptyList();
    }

    @Override
    public ValidationResult validateEtfFlow(@NonNull final UserID userId, @NonNull final EtfFlow etfFlow) {
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
    public EtfFlow getEtfFlowById(@NonNull final UserID userId, @NonNull final EtfFlowID etfFlowId) {
        final EtfFlowData etfFlowData = this.etfDao.getEtfFowById(etfFlowId.getId());
        if (etfFlowData != null) {
            final EtfFlow etfFlow = this.etfFlowDataMapper.mapBToA(etfFlowData);
            if (this.getEtfById(userId, etfFlow.getEtfId()) != null) {
                return etfFlow;
            }
        }
        return null;
    }

    @Override
    public EtfFlowID createEtfFlow(@NonNull final UserID userId, @NonNull final EtfFlow etfFlow) {
        final ValidationResult validationResult = this.validateEtfFlow(userId, etfFlow);
        if (!validationResult.isValid() && !validationResult.getValidationResultItems().isEmpty()) {
            final ValidationResultItem validationResultItem = validationResult.getValidationResultItems().getFirst();
            throw new BusinessException("EtfFlow creation failed!", validationResultItem.getError());
        }
        final EtfFlowData etfFlowData = this.etfFlowDataMapper.mapAToB(etfFlow);
        final Long etfFlowId = this.etfDao.createEtfFlow(etfFlowData);
        return new EtfFlowID(etfFlowId);
    }

    @Override
    public void updateEtfFlow(@NonNull final UserID userId, @NonNull final EtfFlow etfFlow) {
        final ValidationResult validationResult = this.validateEtfFlow(userId, etfFlow);
        if (!validationResult.isValid() && !validationResult.getValidationResultItems().isEmpty()) {
            final ValidationResultItem validationResultItem = validationResult.getValidationResultItems().getFirst();
            throw new BusinessException("EtfFlow update failed!", validationResultItem.getError());
        }
        final EtfFlowData etfFlowData = this.etfFlowDataMapper.mapAToB(etfFlow);
        this.etfDao.updateEtfFlow(etfFlowData);
    }

    @Override
    public void deleteEtfFlow(@NonNull final UserID userId, @NonNull final EtfFlowID etfFlowId) {
        final EtfFlow etfFlow = this.getEtfFlowById(userId, etfFlowId);
        if (etfFlow != null) {
            this.etfDao.deleteEtfFlow(etfFlowId.getId());
        }
    }

    @Override
    public List<EtfFlowWithTaxInfo> calculateEffectiveEtfFlows(final UserID userId, final List<EtfFlow> allEtfFlows,
                                                               final LocalDateTime untilDate) {
        final Map<EtfID, List<EtfFlow>> etfFlowsByEtfIdMap = allEtfFlows.stream()
                .collect(Collectors.groupingBy(EtfFlow::getEtfId));

        final List<EtfFlowWithTaxInfo> relevantEtfFlows = new ArrayList<>();
        for (final var etfFlowsByEtfIdMapEntry : etfFlowsByEtfIdMap.entrySet()) {
            final var etfFlows = etfFlowsByEtfIdMapEntry.getValue();
            final var etfPreliminaryLumpSums = this.getAllEtfPreliminaryLumpSums(userId,
                    etfFlowsByEtfIdMapEntry.getKey());
            final List<EtfFlow> etfBuyFlows = this.calculateEffectiveEtfFlowsUntil(etfFlows, untilDate);

            // initialize accumulated preliminary lump sum to 0 for each effective flow
            final List<EtfFlowWithTaxInfo> etfFlowWithTaxInfos = etfBuyFlows.stream().map(ef -> {
                final EtfFlowWithTaxInfo etfFlowWithTaxInfo = new EtfFlowWithTaxInfo(ef);
                etfFlowWithTaxInfo.setAccumulatedPreliminaryLumpSum(BigDecimal.ZERO);
                return etfFlowWithTaxInfo;
            }).toList();

            // delete all preliminary lump sums older than the earliest effective flow
            final Year yearOfEarliestEtfBuyFlow = Year.from(etfFlowWithTaxInfos.getFirst().getTime());
            etfPreliminaryLumpSums.removeIf(epls -> epls.getYear().isBefore(yearOfEarliestEtfBuyFlow));

            // delete all preliminary lump sums newer than the latest effective flow
            final Year yearOfLatestEtfBuyFlow = Year.from(etfFlowWithTaxInfos.getLast().getTime());
            etfPreliminaryLumpSums.removeIf(epls -> epls.getYear().isAfter(yearOfLatestEtfBuyFlow));

            for (final var etfPreliminaryLumpSum : etfPreliminaryLumpSums) {
                final BigDecimal basePieceTax = switch (etfPreliminaryLumpSum.getType()) {
                    case AMOUNT_PER_MONTH -> BigDecimal.ZERO;
                    case AMOUNT_PER_PIECE -> etfPreliminaryLumpSum.getAmountPerPiece();
                    case AMOUNT_PER_YEAR -> this.getPieceTaxWithAmountPerYear(etfPreliminaryLumpSum, allEtfFlows);
                };

                LocalDateTime beginOfPeriod = LocalDateTime.MIN;
                for (final Month month : Month.values()) {
                    final BigDecimal pieceTax = switch (etfPreliminaryLumpSum.getType()) {
                        case AMOUNT_PER_MONTH ->
                                this.getPieceTaxWithAmountPerMonth(month, etfPreliminaryLumpSum, allEtfFlows);
                        case AMOUNT_PER_PIECE, AMOUNT_PER_YEAR ->
                                basePieceTax.multiply(BigDecimal.valueOf(abs(month.ordinal() - 12) / (double) 12));
                    };

                    final LocalDateTime endOfMonth = etfPreliminaryLumpSum.getYear().atMonth(month).atEndOfMonth()
                            .atTime(LocalTime.MAX);
                    final var finalBeginOfPeriod = beginOfPeriod;

                    etfFlowWithTaxInfos.stream()
                            .filter(efwti -> !efwti.getTime().isAfter(endOfMonth))
                            .filter(efwti -> !finalBeginOfPeriod.isAfter(efwti.getTime()))
                            .forEach(efwti -> addTaxToFlow(etfPreliminaryLumpSum, efwti, pieceTax));
                    beginOfPeriod = endOfMonth.plusDays(1).with(LocalTime.MIN);
                }
            }

            relevantEtfFlows.addAll(etfFlowWithTaxInfos);
        }

        return relevantEtfFlows;
    }

    private static void addTaxToFlow(final EtfPreliminaryLumpSum etfPreliminaryLumpSum, final EtfFlowWithTaxInfo efwti,
                                     final BigDecimal pieceTax) {
        final var tax = efwti.getAmount().multiply(pieceTax).setScale(8, RoundingMode.HALF_UP);
        efwti.getPreliminaryLumpSumPerYear().put(etfPreliminaryLumpSum.getYear(), tax);
        efwti.setAccumulatedPreliminaryLumpSum(efwti
                .getAccumulatedPreliminaryLumpSum()
                .add(tax));
    }

    private BigDecimal getPieceTaxWithAmountPerMonth(final Month month,
                                                     final EtfPreliminaryLumpSum etfPreliminaryLumpSum,
                                                     final List<EtfFlow> allEtfFlows) {
        final LocalDateTime startOfMonth = etfPreliminaryLumpSum.getYear().atMonth(month).atDay(1).atStartOfDay();
        final LocalDateTime endOfMonth = etfPreliminaryLumpSum.getYear().atMonth(month).atEndOfMonth()
                .atTime(LocalTime.MAX);
        final LocalDateTime endOfYear = etfPreliminaryLumpSum.getYear().atMonth(12).atEndOfMonth()
                .atTime(LocalTime.MAX);

        // Consider the whole year because of sales happened later this year
        final List<EtfFlow> relevantTaxFlows = this.calculateEffectiveEtfFlowsUntil(allEtfFlows, endOfYear);
        // after the sales consideration remove all flows after this month because the sum paid is only for stuff until this month
        relevantTaxFlows.removeIf(tf -> tf.getTime().isAfter(endOfMonth));

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

        if (BigDecimal.ZERO.compareTo(amount) == 0) {
            return BigDecimal.ZERO;
        }

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

    private BigDecimal getPieceTaxWithAmountPerYear(final EtfPreliminaryLumpSum etfPreliminaryLumpSum,
                                                    final List<EtfFlow> allEtfFlows) {
        final LocalDateTime endOfYear = etfPreliminaryLumpSum.getYear().atMonth(12).atEndOfMonth()
                .atTime(LocalTime.MAX);

        // Consider the whole year because of sales happened later this year
        final List<EtfFlow> relevantTaxFlows = this.calculateEffectiveEtfFlowsUntil(allEtfFlows, endOfYear);

        BigDecimal weightedSum = BigDecimal.ZERO;
        var flowFrom = LocalDateTime.MIN;

        // Do a weighted distribution of the total preliminary lump sum across the shares held over the course of the year.
        // The weighting factor is always the number of months in which the pieces were in the depot.
        // For January all the shares held until the end of January are relevant. For all following month the shares
        // which where purchased this month.
        for (final var month : Month.values()) {
            final var endOfMonth = etfPreliminaryLumpSum.getYear().atMonth(month).atEndOfMonth()
                    .atTime(LocalTime.MAX);
            final var finalFlowFrom = flowFrom;
            final var relevantAmount = relevantTaxFlows.stream()
                    .filter(tf -> !tf.getTime().isBefore(finalFlowFrom))
                    .filter(tf -> !tf.getTime().isAfter(endOfMonth))
                    .map(EtfFlow::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            weightedSum = weightedSum.add(relevantAmount.multiply(BigDecimal.valueOf(12L - month.ordinal())));
            flowFrom = endOfMonth;
        }

        return etfPreliminaryLumpSum.getAmountDecember().multiply(BigDecimal.valueOf(12L))
                .divide(weightedSum, 10, RoundingMode.HALF_UP);

    }

    private List<EtfFlow> calculateEffectiveEtfFlowsUntil(final List<EtfFlow> etfFlows, final LocalDateTime until) {
        etfFlows.sort(new EtfFlowComparator());
        final List<EtfFlow> etfSalesFlows = etfFlows.stream()
                .filter(ef -> ef.getAmount().compareTo(BigDecimal.ZERO) < 0).filter(ef -> ef.getTime().isBefore(until))
                .toList();
        @SuppressWarnings("java:S6204") // list gets modified
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
    public ValidationResult validateEtfPreliminaryLumpSum(@NonNull final UserID userId,
                                                          @NonNull final EtfPreliminaryLumpSum mep) {
        final ValidationResult validationResult = new ValidationResult();
        final Consumer<ErrorCode> addResult = (final ErrorCode errorCode) -> validationResult.addValidationResultItem(
                new ValidationResultItem(null, errorCode));

        if (mep.getEtfId() == null || mep.getEtfId().getId() == null) {
            addResult.accept(ErrorCode.NO_ETF_SPECIFIED);
        } else {
            final Etf etf = this.getEtfById(userId, mep.getEtfId());
            if (etf == null) {
                addResult.accept(ErrorCode.NO_ETF_SPECIFIED);
            }
        }

        if (mep.getYear() == null) {
            addResult.accept(ErrorCode.YEAR_NOT_SET);
        }

        if (mep.getType() == EtfPreliminaryLumpSumType.AMOUNT_PER_MONTH && mep.getAmountPerPiece() != null) {
            addResult.accept(ErrorCode.ETF_PRELIMINARY_LUMP_SUM_PIECE_PRICE_MUST_BE_NULL);
        } else if (mep.getType() == EtfPreliminaryLumpSumType.AMOUNT_PER_PIECE && (mep.getAmountJanuary() != null
                || mep.getAmountFebruary() != null || mep.getAmountMarch() != null || mep.getAmountApril() != null
                || mep.getAmountMay() != null || mep.getAmountJune() != null || mep.getAmountJuly() != null
                || mep.getAmountAugust() != null || mep.getAmountSeptember() != null || mep.getAmountOctober() != null
                || mep.getAmountNovember() != null || mep.getAmountDecember() != null)) {
            addResult.accept(ErrorCode.ETF_PRELIMINARY_LUMP_SUM_MONTHLY_PRICES_MUST_BE_NULL);

        }

        return validationResult;
    }

    private List<EtfPreliminaryLumpSum> getAllEtfPreliminaryLumpSums(@NonNull final UserID userId,
                                                                     @NonNull final EtfID etfId) {
        final Etf etf = this.getEtfById(userId, etfId);
        if (etf != null) {
            final List<EtfPreliminaryLumpSumData> datas = this.etfDao.getAllPreliminaryLumpSum(etfId.getId());
            return this.etfPreliminaryLumpSumDataMapper.mapBToA(datas);
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public EtfPreliminaryLumpSum getEtfPreliminaryLumpSum(@NonNull final UserID userId,
                                                          @NonNull final EtfPreliminaryLumpSumID id) {
        final EtfPreliminaryLumpSumData data = this.etfDao.getPreliminaryLumpSum(id.getId());
        if (data != null) {
            final var etfPreliminaryLumpSum = this.etfPreliminaryLumpSumDataMapper.mapBToA(data);

            final Etf etf = this.getEtfById(userId, etfPreliminaryLumpSum.getEtfId());
            if (etf != null) {
                return etfPreliminaryLumpSum;
            }
        }
        return null;

    }

    @Override
    public List<EtfPreliminaryLumpSum> getAllEtfPreliminaryLumpSum(@NonNull final UserID userId,
                                                                   @NonNull final EtfID etfId) {
        final Etf etf = this.getEtfById(userId, etfId);
        if (etf != null) {
            final List<EtfPreliminaryLumpSumData> datas = this.etfDao.getAllEtfPreliminaryLumpSum(etfId.getId());
            return this.etfPreliminaryLumpSumDataMapper.mapBToA(datas);
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public void createEtfPreliminaryLumpSum(@NonNull final UserID userId,
                                            @NonNull final EtfPreliminaryLumpSum etfPreliminaryLumpSum) {
        final ValidationResult validationResult = this.validateEtfPreliminaryLumpSum(userId, etfPreliminaryLumpSum);
        if (!validationResult.isValid() && !validationResult.getValidationResultItems().isEmpty()) {
            final ValidationResultItem validationResultItem = validationResult.getValidationResultItems().getFirst();
            throw new BusinessException("EtfPreliminaryLumpSum creation failed!", validationResultItem.getError());
        }
        final var idLong = this.etfDao.getPreliminaryLumpSumId(etfPreliminaryLumpSum.getEtfId().getId(),
                etfPreliminaryLumpSum.getYear().getValue());
        if (idLong != null) {
            throw new BusinessException("EtfPreliminaryLumpSum already exists!",
                    ErrorCode.ETF_PRELIMINARY_LUMP_SUM_ALREADY_EXISTS);
        }
        final EtfPreliminaryLumpSumData data = this.etfPreliminaryLumpSumDataMapper.mapAToB(etfPreliminaryLumpSum);
        this.etfDao.createPreliminaryLumpSum(data);
    }

    @Override
    public void updateEtfPreliminaryLumpSum(@NonNull final UserID userId,
                                            @NonNull final EtfPreliminaryLumpSum etfPreliminaryLumpSum) {
        final ValidationResult validationResult = this.validateEtfPreliminaryLumpSum(userId, etfPreliminaryLumpSum);
        if (!validationResult.isValid() && !validationResult.getValidationResultItems().isEmpty()) {
            final ValidationResultItem validationResultItem = validationResult.getValidationResultItems().getFirst();
            throw new BusinessException("EtfPreliminaryLumpSum creation failed!", validationResultItem.getError());
        }
        final var etfPreliminaryLumpSumExists = this.getEtfPreliminaryLumpSum(userId, etfPreliminaryLumpSum.getId());
        if (etfPreliminaryLumpSumExists == null) {
            throw new BusinessException("EtfPreliminaryLumpSum does not exist!",
                    ErrorCode.ETF_PRELIMINARY_LUMP_SUM_DOES_NOT_EXIST);
        }
        final var idLong = this.etfDao.getPreliminaryLumpSumId(etfPreliminaryLumpSum.getEtfId().getId(),
                etfPreliminaryLumpSum.getYear().getValue());
        if (idLong != null && !idLong.equals(etfPreliminaryLumpSum.getId().getId())) {
            throw new BusinessException("EtfPreliminaryLumpSum already exists!",
                    ErrorCode.ETF_PRELIMINARY_LUMP_SUM_ALREADY_EXISTS);
        }
        final EtfPreliminaryLumpSumData data = this.etfPreliminaryLumpSumDataMapper.mapAToB(etfPreliminaryLumpSum);
        this.etfDao.updatePreliminaryLumpSum(data);
    }

    @Override
    public void deleteEtfPreliminaryLumpSum(@NonNull final UserID userId, @NonNull final EtfPreliminaryLumpSumID id) {
        if (this.getEtfPreliminaryLumpSum(userId, id) != null) {
            this.etfDao.deletePreliminaryLumpSum(id.getId());
        }
    }

    //
    // ETF value
    //

    @Override
    public EtfValue getEtfValueEndOfMonth(final EtfIsin etfIsin, final Year year, final Month month) {
        final EtfValueData etfValueData = this.etfDao.getEtfValueForMonth(etfIsin.getId(), year, month);
        return this.etfValueDataMapper.mapBToA(etfValueData);
    }

    @Override
    public EtfValue getLatestEtfValue(final EtfIsin etfIsin) {
        final EtfValueData etfValueData = this.etfDao.getLatestEtfValue(etfIsin.getId());
        return this.etfValueDataMapper.mapBToA(etfValueData);
    }
}
