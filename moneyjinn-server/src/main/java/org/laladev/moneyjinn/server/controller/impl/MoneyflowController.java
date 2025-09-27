//Copyright (c) 2015-2025 Oliver Lehmann <lehmann@ans-netz.de>
//All rights reserved.
//
//Redistribution and use in source and binary forms, with or without
//modification, are permitted provided that the following conditions
//are met:
//1. Redistributions of source code must retain the above copyright
//notice, this list of conditions and the following disclaimer
//2. Redistributions in binary form must reproduce the above copyright
//notice, this list of conditions and the following disclaimer in the
//documentation and/or other materials provided with the distribution.
//
//THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS'' AND
//ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
//IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
//ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE LIABLE
//FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
//DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
//OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
//HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
//LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
//OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
//SUCH DAMAGE.

package org.laladev.moneyjinn.server.controller.impl;

import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.model.Contractpartner;
import org.laladev.moneyjinn.model.PreDefMoneyflow;
import org.laladev.moneyjinn.model.PreDefMoneyflowID;
import org.laladev.moneyjinn.model.access.AccessRelation;
import org.laladev.moneyjinn.model.access.Group;
import org.laladev.moneyjinn.model.access.User;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.capitalsource.Capitalsource;
import org.laladev.moneyjinn.model.exception.BusinessException;
import org.laladev.moneyjinn.model.moneyflow.Moneyflow;
import org.laladev.moneyjinn.model.moneyflow.MoneyflowID;
import org.laladev.moneyjinn.model.moneyflow.MoneyflowSplitEntry;
import org.laladev.moneyjinn.model.moneyflow.MoneyflowSplitEntryID;
import org.laladev.moneyjinn.model.moneyflow.search.MoneyflowSearchParams;
import org.laladev.moneyjinn.model.validation.ValidationResult;
import org.laladev.moneyjinn.model.validation.ValidationResultItem;
import org.laladev.moneyjinn.server.controller.api.MoneyflowControllerApi;
import org.laladev.moneyjinn.server.controller.mapper.MoneyflowSearchParamsTransportMapper;
import org.laladev.moneyjinn.server.controller.mapper.MoneyflowSplitEntryTransportMapper;
import org.laladev.moneyjinn.server.controller.mapper.MoneyflowTransportMapper;
import org.laladev.moneyjinn.server.model.*;
import org.laladev.moneyjinn.service.api.*;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

@RestController
@Transactional(propagation = Propagation.REQUIRES_NEW)
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class MoneyflowController extends AbstractController implements MoneyflowControllerApi {
    private static final DateTimeFormatter SEARCH_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    private final IUserService userService;
    private final IAccessRelationService accessRelationService;
    private final IPreDefMoneyflowService preDefMoneyflowService;
    private final ICapitalsourceService capitalsourceService;
    private final IContractpartnerService contractpartnerService;
    private final IMoneyflowService moneyflowService;
    private final IMoneyflowSplitEntryService moneyflowSplitEntryService;
    private final IMoneyflowReceiptService moneyflowReceiptService;
    private final MoneyflowSearchParamsTransportMapper moneyflowSearchParamsTransportMapper;
    private final MoneyflowSplitEntryTransportMapper moneyflowSplitEntryTransportMapper;
    private final MoneyflowTransportMapper moneyflowTransportMapper;

    /**
     * Checks if capitalsource and contractparter are valid on bookingdate -
     * otherwise the validity is modified. Also fills comment and postingaccount if
     * it is empty and MoneyflowSplitEntries where provided with data from the first
     * MoneyflowSplitEntry.
     *
     * @param moneyflow             Moneyflow
     * @param moneyflowSplitEntries MoneyflowSplitEntries
     */
    private void prepareForValidityCheck(final Moneyflow moneyflow,
                                         final List<MoneyflowSplitEntry> moneyflowSplitEntries) {
        final LocalDate bookingDate = moneyflow.getBookingDate();
        final UserID userId = moneyflow.getUser().getId();
        final Group group = moneyflow.getGroup();
        if (bookingDate != null) {
            final AccessRelation accessRelation = this.accessRelationService
                    .getCurrentAccessRelationById(moneyflow.getUser().getId());
            // Only modify Capitalsources or Contractpartner if the Bookingdate is within
            // the
            // current group assignment validity period
            if (!bookingDate.isBefore(accessRelation.getValidFrom())
                    && !bookingDate.isAfter(accessRelation.getValidTil())) {
                // Check if used Capitalsource is valid at bookingDate - if not, change its
                // validity so it fits.
                if (moneyflow.getCapitalsource() != null) {
                    final Capitalsource capitalsource = this.capitalsourceService.getCapitalsourceById(userId,
                            group.getId(), moneyflow.getCapitalsource().getId());
                    if (capitalsource != null && capitalsource.groupUseAllowed(userId)) {
                        final boolean bookingDateIsBeforeValidity = bookingDate
                                .isBefore(capitalsource.getValidFrom());
                        final boolean bookingDateIsAfterValidity = bookingDate.isAfter(capitalsource.getValidTil());
                        if (bookingDateIsBeforeValidity) {
                            capitalsource.setValidFrom(bookingDate);
                        }
                        if (bookingDateIsAfterValidity) {
                            capitalsource.setValidTil(bookingDate);
                        }
                        if (bookingDateIsAfterValidity || bookingDateIsBeforeValidity) {
                            this.capitalsourceService.updateCapitalsource(capitalsource);
                        }
                    }
                }
                // Check if used Contractpartner is valid at bookingDate - if not, change its
                // validity so it fits.
                if (moneyflow.getContractpartner() != null) {
                    final Contractpartner contractpartner = this.contractpartnerService.getContractpartnerById(userId,
                            moneyflow.getContractpartner().getId());
                    if (contractpartner != null) {
                        final boolean bookingDateIsBeforeValidity = bookingDate
                                .isBefore(contractpartner.getValidFrom());
                        final boolean bookingDateIsAfterValidity = bookingDate.isAfter(contractpartner.getValidTil());
                        if (bookingDateIsBeforeValidity) {
                            contractpartner.setValidFrom(bookingDate);
                        }
                        if (bookingDateIsAfterValidity) {
                            contractpartner.setValidTil(bookingDate);
                        }
                        if (bookingDateIsAfterValidity || bookingDateIsBeforeValidity) {
                            this.contractpartnerService.updateContractpartner(contractpartner);
                        }
                    }
                }
            }
        }
        // use the comment and postingaccount of the 1st split booking for the main
        // booking if nothing is specified
        if (!moneyflowSplitEntries.isEmpty()) {
            final MoneyflowSplitEntry moneyflowSplitEntry = moneyflowSplitEntries.getFirst();
            if (moneyflow.getComment() == null || moneyflow.getComment().trim().isEmpty()) {
                moneyflow.setComment(moneyflowSplitEntry.getComment());
            }
            if (moneyflow.getPostingAccount() == null) {
                moneyflow.setPostingAccount(moneyflowSplitEntry.getPostingAccount());
            }
        }
    }

    private ValidationResult checkIfAmountIsEqual(final Moneyflow moneyflow,
                                                  final List<MoneyflowSplitEntry> moneyflowSplitEntries) {
        final ValidationResult validationResult = new ValidationResult();
        if (!moneyflowSplitEntries.isEmpty()) {
            final BigDecimal sumOfSplitEntriesAmount = moneyflowSplitEntries.stream()
                    .map(MoneyflowSplitEntry::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            if (sumOfSplitEntriesAmount.compareTo(moneyflow.getAmount()) != 0) {
                validationResult.addValidationResultItem(new ValidationResultItem(moneyflow.getId(),
                        ErrorCode.SPLIT_ENTRIES_AMOUNT_IS_NOT_EQUALS_MONEYFLOW_AMOUNT));
            }
        }
        return validationResult;
    }

    @Override
    public ResponseEntity<ShowEditMoneyflowResponse> showEditMoneyflow(final Long id) {
        final UserID userId = super.getUserId();
        final ShowEditMoneyflowResponse response = new ShowEditMoneyflowResponse();
        final Moneyflow moneyflow = this.moneyflowService.getMoneyflowById(userId, new MoneyflowID(id));
        if (moneyflow != null && moneyflow.getUser().getId().equals(userId)) {
            response.setMoneyflowTransport(this.moneyflowTransportMapper.mapAToB(moneyflow));
            final List<MoneyflowSplitEntry> moneyflowSplitEntries = this.moneyflowSplitEntryService
                    .getMoneyflowSplitEntries(userId, moneyflow.getId());
            if (!moneyflowSplitEntries.isEmpty()) {
                response.setMoneyflowSplitEntryTransports(
                        this.moneyflowSplitEntryTransportMapper.mapAToB(moneyflowSplitEntries));
            }
            final List<MoneyflowID> moneyflowIdsWithReceipts = this.moneyflowReceiptService
                    .getMoneyflowIdsWithReceipt(userId, Collections.singletonList(moneyflow.getId()));
            response.setHasReceipt(moneyflowIdsWithReceipts.size() == 1);
        }
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<SearchMoneyflowsResponse> searchMoneyflows(
            @RequestBody final SearchMoneyflowsRequest request) {
        final UserID userId = super.getUserId();
        final SearchMoneyflowsResponse response = new SearchMoneyflowsResponse();
        final MoneyflowSearchParams moneyflowSearchParams = this.moneyflowSearchParamsTransportMapper
                .mapBToA(request.getMoneyflowSearchParamsTransport());

        if (moneyflowSearchParams == null || (moneyflowSearchParams.getContractpartnerId() == null
                && moneyflowSearchParams.getPostingAccountId() == null
                && moneyflowSearchParams.getSearchString() == null)) {
            final ValidationResult validationResult = new ValidationResult();
            validationResult
                    .addValidationResultItem(new ValidationResultItem(null, ErrorCode.NO_SEARCH_CRITERIA_ENTERED));

            this.throwValidationExceptionIfInvalid(validationResult);
        }

        final List<Moneyflow> moneyflows = this.moneyflowService.searchMoneyflows(userId, moneyflowSearchParams);
        if (moneyflows != null && !moneyflows.isEmpty()) {
            final List<MoneyflowTransport> moneyflowTransports = moneyflows.stream().filter(mf -> mf.isVisible(userId))
                    .map(this.moneyflowTransportMapper::mapAToB).toList();
            response.setMoneyflowTransports(moneyflowTransports);
        }

        return ResponseEntity.ok(response);
    }

    /**
     * Creates a new moneyflow together with split entries if they where given.
     *
     * @param request The request which contains the moneyflow
     * @return ValidationResponse
     */

    @Override
    public ResponseEntity<Void> createMoneyflows(@RequestBody final CreateMoneyflowRequest request) {
        final UserID userId = super.getUserId();
        final Moneyflow moneyflow = this.moneyflowTransportMapper.mapBToA(request.getMoneyflowTransport());
        final List<MoneyflowSplitEntry> moneyflowSplitEntries = this.moneyflowSplitEntryTransportMapper
                .mapBToA(request.getInsertMoneyflowSplitEntryTransports());
        final Long preDefMoneyflowIdLong = request.getUsedPreDefMoneyflowId();
        PreDefMoneyflowID preDefMoneyflowId = preDefMoneyflowIdLong != null
                ? new PreDefMoneyflowID(preDefMoneyflowIdLong)
                : null;

        final boolean saveAsPreDefMoneyflow = Integer.valueOf(1).equals(request.getSaveAsPreDefMoneyflow());

        final User user = this.userService.getUserById(userId);
        final Group group = this.accessRelationService.getCurrentGroup(userId);
        moneyflow.setUser(user);
        moneyflow.setGroup(group);
        this.prepareForValidityCheck(moneyflow, moneyflowSplitEntries);

        final ValidationResult validationResult = this.moneyflowService.validateMoneyflow(moneyflow);
        if (validationResult.isValid() && !moneyflowSplitEntries.isEmpty()) {
            moneyflowSplitEntries.forEach(mse -> validationResult
                    .mergeValidationResult(this.moneyflowSplitEntryService.validateMoneyflowSplitEntry(mse)));
            if (validationResult.isValid()) {
                validationResult.mergeValidationResult(this.checkIfAmountIsEqual(moneyflow, moneyflowSplitEntries));
            }
        }

        this.throwValidationExceptionIfInvalid(validationResult);

        final MoneyflowID moneyflowId = this.moneyflowService.createMoneyflow(moneyflow);
        if (!moneyflowSplitEntries.isEmpty()) {
            moneyflowSplitEntries.forEach(mse -> mse.setMoneyflowId(moneyflowId));
            this.moneyflowSplitEntryService.createMoneyflowSplitEntries(userId, moneyflowSplitEntries);
        }

        if (saveAsPreDefMoneyflow) {
            final PreDefMoneyflow preDefMoneyflow = new PreDefMoneyflow();
            preDefMoneyflow.setAmount(moneyflow.getAmount());
            preDefMoneyflow.setCapitalsource(moneyflow.getCapitalsource());
            preDefMoneyflow.setComment(moneyflow.getComment());
            preDefMoneyflow.setContractpartner(moneyflow.getContractpartner());
            preDefMoneyflow.setPostingAccount(moneyflow.getPostingAccount());
            preDefMoneyflow.setUser(user);
            if (preDefMoneyflowId != null) {
                final PreDefMoneyflow preDefMoneyflowOrig = this.preDefMoneyflowService.getPreDefMoneyflowById(userId,
                        preDefMoneyflowId);
                if (preDefMoneyflowOrig != null) {
                    preDefMoneyflow.setId(preDefMoneyflowId);
                    preDefMoneyflow.setOnceAMonth(preDefMoneyflowOrig.isOnceAMonth());
                    this.preDefMoneyflowService.updatePreDefMoneyflow(preDefMoneyflow);
                }
            } else {
                preDefMoneyflow.setOnceAMonth(false);
                preDefMoneyflowId = this.preDefMoneyflowService.createPreDefMoneyflow(preDefMoneyflow);
            }
        }
        if (preDefMoneyflowId != null) {
            this.preDefMoneyflowService.setLastUsedDate(userId, preDefMoneyflowId);
        }
        return ResponseEntity.noContent().build();
    }

    /**
     * Updates the given Moneyflow.
     *
     * @param request The Request object which contains the Moneyflow.
     * @return Validation Response
     */

    @Override
    public ResponseEntity<UpdateMoneyflowResponse> updateMoneyflowV2(
            @RequestBody final UpdateMoneyflowRequest request) {
        final UpdateMoneyflowResponse response = new UpdateMoneyflowResponse();
        final UserID userId = super.getUserId();
        final User user = this.userService.getUserById(userId);
        final Group group = this.accessRelationService.getCurrentGroup(userId);
        final List<MoneyflowSplitEntry> updateMoneyflowSplitEntries = this.moneyflowSplitEntryTransportMapper
                .mapBToA(request.getUpdateMoneyflowSplitEntryTransports());
        final List<MoneyflowSplitEntry> insertMoneyflowSplitEntries = this.moneyflowSplitEntryTransportMapper.mapBToA(
                request.getInsertMoneyflowSplitEntryTransports());
        List<MoneyflowSplitEntryID> deleteMoneyflowSplitEntryIds = null;
        if (request.getDeleteMoneyflowSplitEntryIds() != null) {
            deleteMoneyflowSplitEntryIds = request.getDeleteMoneyflowSplitEntryIds().stream()
                    .map(MoneyflowSplitEntryID::new).toList();
        }
        final Moneyflow moneyflow = this.moneyflowTransportMapper.mapBToA(request.getMoneyflowTransport());
        moneyflow.setUser(user);
        moneyflow.setGroup(group);
        // build a List of all MoneyflowSplitEntries which will be there after this
        // update
        final List<MoneyflowSplitEntry> moneyflowSplitEntries = this.moneyflowSplitEntryService
                .getMoneyflowSplitEntries(userId, moneyflow.getId());
        final ListIterator<MoneyflowSplitEntry> entryIterator = moneyflowSplitEntries.listIterator();
        while (entryIterator.hasNext()) {
            final MoneyflowSplitEntry entry = entryIterator.next();
            if (deleteMoneyflowSplitEntryIds != null && deleteMoneyflowSplitEntryIds.contains(entry.getId())) {
                entryIterator.remove();
            } else {
                updateMoneyflowSplitEntries.stream()
                        .filter(mse -> mse.getId().equals(entry.getId()))
                        .findAny()
                        .ifPresent(entryIterator::set);
            }
        }
        moneyflowSplitEntries.addAll(insertMoneyflowSplitEntries);
        this.prepareForValidityCheck(moneyflow, moneyflowSplitEntries);
        final MoneyflowID moneyflowId = moneyflow.getId();
        final Moneyflow moneyflowById = this.moneyflowService.getMoneyflowById(userId, moneyflowId);
        // only the creator of a moneyflow may edit it!
        if (moneyflowById == null || !moneyflowById.getUser().equals(user)) {
            throw new BusinessException("Moneyflow not found!", ErrorCode.MONEYFLOW_DOES_NOT_EXISTS);
        }

        final ValidationResult validationResult = this.moneyflowService.validateMoneyflow(moneyflow);
        if (validationResult.isValid()) {
            updateMoneyflowSplitEntries.forEach(mse -> {
                mse.setMoneyflowId(moneyflowId);
                validationResult
                        .mergeValidationResult(this.moneyflowSplitEntryService.validateMoneyflowSplitEntry(mse));
            });
            insertMoneyflowSplitEntries.forEach(mse -> {
                mse.setMoneyflowId(moneyflowId);
                validationResult
                        .mergeValidationResult(this.moneyflowSplitEntryService.validateMoneyflowSplitEntry(mse));
            });

            if (validationResult.isValid()) {
                validationResult.mergeValidationResult(this.checkIfAmountIsEqual(moneyflow, moneyflowSplitEntries));
            }
        }

        this.throwValidationExceptionIfInvalid(validationResult);

        if (deleteMoneyflowSplitEntryIds != null) {
            deleteMoneyflowSplitEntryIds.forEach(
                    mseId -> this.moneyflowSplitEntryService.deleteMoneyflowSplitEntry(userId, moneyflowId, mseId));
        }
        updateMoneyflowSplitEntries
                .forEach(mse -> this.moneyflowSplitEntryService.updateMoneyflowSplitEntry(userId, mse));
        if (!insertMoneyflowSplitEntries.isEmpty()) {
            this.moneyflowSplitEntryService.createMoneyflowSplitEntries(userId, insertMoneyflowSplitEntries);
        }
        this.moneyflowService.updateMoneyflow(moneyflow);

        final Moneyflow moneyflowNew = this.moneyflowService.getMoneyflowById(userId, moneyflow.getId());
        if (moneyflowNew != null && moneyflowNew.getUser().getId().equals(userId)) {
            response.setMoneyflowTransport(this.moneyflowTransportMapper.mapAToB(moneyflowNew));
            final List<MoneyflowSplitEntry> moneyflowSplitEntriesNew = this.moneyflowSplitEntryService
                    .getMoneyflowSplitEntries(userId, moneyflowNew.getId());
            if (!moneyflowSplitEntriesNew.isEmpty()) {
                response.setMoneyflowSplitEntryTransports(
                        this.moneyflowSplitEntryTransportMapper.mapAToB(moneyflowSplitEntriesNew));
            }
            final List<MoneyflowID> moneyflowIdsWithReceipts = this.moneyflowReceiptService
                    .getMoneyflowIdsWithReceipt(userId, Collections.singletonList(moneyflowNew.getId()));
            response.setHasReceipt(moneyflowIdsWithReceipts.size() == 1);
        }

        return ResponseEntity.ok(response);
    }

    /**
     * Deletes the specified Moneyflow.
     *
     * @param id The ID of the Moneyflow to delete
     */

    @Override
    public ResponseEntity<Void> deleteMoneyflowById(final Long id) {
        final UserID userId = super.getUserId();
        final MoneyflowID moneyflowId = new MoneyflowID(id);

        this.moneyflowSplitEntryService.deleteMoneyflowSplitEntries(userId, moneyflowId);
        this.moneyflowReceiptService.deleteMoneyflowReceipt(userId, moneyflowId);
        this.moneyflowService.deleteMoneyflow(userId, moneyflowId);

        return ResponseEntity.noContent().build();
    }

    /**
     * Searches for Moneyflows given by an absolut amount and a date range.
     *
     * @param amount      ABS amount
     * @param dateFromStr date to start searching (format: YYYYMMDD)
     * @param dateTilStr  date to end searching (format: YYYYMMDD)
     * @return matching Moneyflows
     */

    @Override
    public ResponseEntity<SearchMoneyflowsByAmountResponse> searchMoneyflowsByAmount(
            final BigDecimal amount,
            final String dateFromStr,
            final String dateTilStr) {
        final UserID userId = super.getUserId();
        final SearchMoneyflowsByAmountResponse response = new SearchMoneyflowsByAmountResponse();
        final LocalDate dateFrom = LocalDate.parse(dateFromStr, SEARCH_DATE_FORMATTER);
        final LocalDate dateTil = LocalDate.parse(dateTilStr, SEARCH_DATE_FORMATTER);
        final List<Moneyflow> moneyflows = this.moneyflowService.searchMoneyflowsByAbsoluteAmountDate(userId, amount,
                dateFrom, dateTil);
        if (moneyflows != null && !moneyflows.isEmpty()) {
            final List<Moneyflow> relevantMoneyflows = moneyflows.stream().filter(mf -> mf.isVisible(userId)).toList();
            if (!relevantMoneyflows.isEmpty()) {
                final List<MoneyflowID> relevantMoneyflowIds = relevantMoneyflows.stream().map(Moneyflow::getId)
                        .toList();
                final Map<MoneyflowID, List<MoneyflowSplitEntry>> moneyflowSplitEntries = this.moneyflowSplitEntryService
                        .getMoneyflowSplitEntries(userId, relevantMoneyflowIds);
                final List<MoneyflowTransport> moneyflowTransports = this.moneyflowTransportMapper
                        .mapAToB(relevantMoneyflows);
                response.setMoneyflowTransports(moneyflowTransports);
                if (!moneyflowSplitEntries.isEmpty()) {
                    final List<MoneyflowSplitEntry> moneyflowSplitEntryList = moneyflowSplitEntries.values().stream()
                            .flatMap(List::stream).toList();
                    response.setMoneyflowSplitEntryTransports(
                            this.moneyflowSplitEntryTransportMapper.mapAToB(moneyflowSplitEntryList));
                }
                return ResponseEntity.ok(response);
            }
        }
        return ResponseEntity.noContent().build();
    }
}
