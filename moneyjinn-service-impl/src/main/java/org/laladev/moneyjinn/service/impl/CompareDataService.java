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

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.language.DoubleMetaphone;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.model.ContractpartnerAccount;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.capitalsource.CapitalsourceID;
import org.laladev.moneyjinn.model.comparedata.*;
import org.laladev.moneyjinn.model.exception.BusinessException;
import org.laladev.moneyjinn.model.moneyflow.ImportedMoneyflow;
import org.laladev.moneyjinn.model.moneyflow.Moneyflow;
import org.laladev.moneyjinn.sepa.camt.mapper.BankToCustomerAccountReportMapper;
import org.laladev.moneyjinn.sepa.camt.model.BankToCustomerAccountReport;
import org.laladev.moneyjinn.sepa.camt.model.CreditDebitCode;
import org.laladev.moneyjinn.sepa.camt.model.Entry;
import org.laladev.moneyjinn.service.api.ICompareDataService;
import org.laladev.moneyjinn.service.api.IContractpartnerAccountService;
import org.laladev.moneyjinn.service.api.IContractpartnerMatchingService;
import org.laladev.moneyjinn.service.api.IMoneyflowService;
import org.laladev.moneyjinn.service.dao.CompareDataFormatDao;
import org.laladev.moneyjinn.service.dao.data.CompareDataFormatData;
import org.laladev.moneyjinn.service.dao.data.mapper.CompareDataFormatDataMapper;
import org.xml.sax.InputSource;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Named
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class CompareDataService extends AbstractService implements ICompareDataService {
    private static final String WRONG_FILE_FORMAT_TEXT =
            "The specified file is not parseable! Maybe you've selected the wrong format or file?";
    private final CompareDataFormatDao compareDataFormatDao;
    private final IMoneyflowService moneyflowService;
    private final IContractpartnerAccountService contractpartnerAccountService;
    private final IContractpartnerMatchingService contractpartnerMatchingService;
    private final CompareDataFormatDataMapper compareDataFormatDataMapper;

    private final DoubleMetaphone doubleMetaphone = new DoubleMetaphone();

    private static BigDecimal getAmount(final CompareDataFormat compareDataFormat, final String[] cmpDataRaw,
                                        final Pattern creditIndicator) {
        String amountStr = cmpDataRaw[compareDataFormat.getPositionAmount() - 1];
        if (compareDataFormat.getFormatAmountThousand() != null) {
            amountStr = amountStr.replace(compareDataFormat.getFormatAmountThousand().toString(), "");
        }
        amountStr = amountStr.replace(compareDataFormat.getFormatAmountDecimal().toString(), ".");
        amountStr = amountStr.replaceAll("[^-.0-9]*", "");
        BigDecimal amount = new BigDecimal(amountStr);
        if (compareDataFormat.getPositionCreditDebitIndicator() != null) {
            final String keyword = cmpDataRaw[compareDataFormat.getPositionCreditDebitIndicator() - 1];
            final Matcher creditIndicatorMatcher = creditIndicator.matcher(keyword);
            if (creditIndicatorMatcher.find()) {
                amount = amount.negate();
            }
        }
        return amount;
    }

    @Override
    public CompareDataFormat getCompareDataFormatById(final CompareDataFormatID compareDataFormatId) {
        final CompareDataFormatData compareDataFormatData = this.compareDataFormatDao
                .getCompareDataFormatById(compareDataFormatId.getId());
        return this.compareDataFormatDataMapper.mapBToA(compareDataFormatData);
    }

    @Override
    public List<CompareDataFormat> getAllCompareDataFormats() {
        final List<CompareDataFormatData> compareDataFormatDatas = this.compareDataFormatDao.getAllCompareDataFormats();
        return this.compareDataFormatDataMapper.mapBToA(compareDataFormatDatas);
    }

    @Override
    public CompareDataResult compareDataFile(final UserID userId, final CompareDataFormatID compareDataFormatId,
                                             final CapitalsourceID capitalsourceId, final LocalDate startDate,
                                             final LocalDate endDate,
                                             final String fileContents) {
        final CompareDataFormat compareDataFormat = this.getCompareDataFormatById(compareDataFormatId);
        List<CompareDataDataset> compareDataDatasets = null;
        if (compareDataFormat.getType() == CompareDataFormatType.CVS) {
            compareDataDatasets = this.mapCvsFileToCompareData(userId, fileContents, compareDataFormat);
        } else if (compareDataFormat.getType() == CompareDataFormatType.XML) {
            compareDataDatasets = this.mapCamtFileToCompareData(fileContents);
        }
        if (compareDataDatasets == null || compareDataDatasets.isEmpty()) {
            throw new BusinessException(WRONG_FILE_FORMAT_TEXT, ErrorCode.WRONG_FILE_FORMAT);
        }
        // remove File-Data outside the given period of time
        compareDataDatasets
                .removeIf(cdd -> cdd.getBookingDate().isAfter(endDate) || cdd.getBookingDate().isBefore(startDate));
        return this.doComparision(userId, capitalsourceId, startDate, endDate, compareDataDatasets);
    }

    @Override
    public CompareDataResult compareDataImport(final UserID userId, final CapitalsourceID capitalsourceId,
                                               final LocalDate startDate, final LocalDate endDate,
                                               final List<ImportedMoneyflow> importedMoneyflows) {
        List<CompareDataDataset> compareDataDatasets = null;
        if (importedMoneyflows != null) {
            compareDataDatasets = this.mapImportedMoneyflows(importedMoneyflows);
        }
        return this.doComparision(userId, capitalsourceId, startDate, endDate, compareDataDatasets);
    }

    private List<CompareDataDataset> mapImportedMoneyflows(final List<ImportedMoneyflow> importedMoneyflows) {
        final List<CompareDataDataset> compareDataDatasets = new ArrayList<>();
        for (final ImportedMoneyflow importedMoneyflow : importedMoneyflows) {
            final CompareDataDataset compareDataDataset = new CompareDataDataset();
            compareDataDataset.setAmount(importedMoneyflow.getAmount());
            compareDataDataset.setBookingDate(importedMoneyflow.getBookingDate());
            compareDataDataset.setComment(importedMoneyflow.getUsage());
            compareDataDataset.setInvoiceDate(importedMoneyflow.getInvoiceDate());
            compareDataDataset.setPartner(importedMoneyflow.getName());
            compareDataDataset.setPartnerBankAccount(importedMoneyflow.getBankAccount());
            compareDataDatasets.add(compareDataDataset);
        }
        return compareDataDatasets;
    }

    private CompareDataResult doComparision(final UserID userId, final CapitalsourceID capitalsourceId,
                                            final LocalDate startDate, final LocalDate endDate,
                                            final List<CompareDataDataset> compareDataDatasets) {
        final CompareDataResult result = new CompareDataResult();
        final Period searchFrame = Period.ofDays(5);
        // gather all recorded moneyflows in the given period of time to work on for
        // comparision
        final List<Moneyflow> allMoneyflows = new ArrayList<>(
                this.moneyflowService.getAllMoneyflowsByDateRangeIncludingPrivate(userId, startDate, endDate));
        if (compareDataDatasets != null) {
            for (final CompareDataDataset compareDataDataset : compareDataDatasets) {
                final LocalDate bookingDate = compareDataDataset.getBookingDate();
                final LocalDate bookingDateLeft = bookingDate.minus(searchFrame);
                final LocalDate bookingDateRight = bookingDate.plus(searchFrame);
                final List<Moneyflow> moneyflows = new ArrayList<>();
                // find all recorded moneyflows which could possibly match the file dataset
                for (final Moneyflow moneyflow : allMoneyflows) {
                    if (moneyflow.getAmount().compareTo(compareDataDataset.getAmount()) == 0
                            && moneyflow.getBookingDate().isAfter(bookingDateLeft)
                            && moneyflow.getBookingDate().isBefore(bookingDateRight)) {
                        moneyflows.add(moneyflow);
                    }
                }
                if (moneyflows.isEmpty()) {
                    // no matching moneyflows found
                    result.addCompareDataNotInDatabase(new CompareDataNotInDatabase(compareDataDataset));
                } else {
                    Moneyflow matchingMoneyflow = null;
                    if (moneyflows.size() == 1) {
                        // one match found
                        matchingMoneyflow = moneyflows.getFirst();
                    } else {
                        // more than one match found - try to find the best one by rating all
                        int currentRating = -1;
                        for (final Moneyflow moneyflow : moneyflows) {
                            final int rating = this.rateMoneyflowToDataset(userId, moneyflow, compareDataDataset,
                                    capitalsourceId);
                            if (rating > currentRating) {
                                matchingMoneyflow = moneyflow;
                                currentRating = rating;
                            }
                        }
                    }
                    if (matchingMoneyflow != null) {
                        if (matchingMoneyflow.isVisible(userId)) {
                            if (matchingMoneyflow.getCapitalsource().getId().equals(capitalsourceId)) {
                                result.addCompareDataMatching(
                                        new CompareDataMatching(matchingMoneyflow, compareDataDataset));
                            } else {
                                result.addCompareDataWrongCapitalsource(
                                        new CompareDataWrongCapitalsource(matchingMoneyflow, compareDataDataset));
                            }
                        }
                        allMoneyflows.remove(matchingMoneyflow);
                    }
                }
            }
        }
        allMoneyflows.removeIf(mf -> !mf.getCapitalsource().getId().equals(capitalsourceId));
        for (final Moneyflow moneyflow : allMoneyflows) {
            if (moneyflow.isVisible(userId)) {
                result.addCompareDataNotInFile(new CompareDataNotInFile(moneyflow));
            }
        }
        return result;
    }

    private int rateMoneyflowToDataset(final UserID userId, final Moneyflow moneyflow,
                                       final CompareDataDataset compareDataDataset,
                                       final CapitalsourceID capitalsourceId) {
        int rating = 0;
        if (moneyflow.getBookingDate().equals(compareDataDataset.getBookingDate())) {
            rating += 10;
        }
        if (moneyflow.getInvoiceDate().equals(compareDataDataset.getInvoiceDate())) {
            rating += 10;
        }
        if (moneyflow.getCapitalsource().getId().equals(capitalsourceId)) {
            rating += 10;
        }
        // does our source contain bank account information?
        rating = this.rateBankAccount(userId, moneyflow, compareDataDataset, rating);
        // does our input-file contain contractpartner information?
        rating = this.rateContractpartner(moneyflow, compareDataDataset, rating);
        return rating;
    }

    private int rateBankAccount(final UserID userId, final Moneyflow moneyflow,
                                final CompareDataDataset compareDataDataset, int rating) {
        if (compareDataDataset.getPartnerBankAccount() != null) {
            final List<ContractpartnerAccount> contractpartnerAccounts = this.contractpartnerAccountService
                    .getAllContractpartnerByAccounts(userId, List.of(compareDataDataset.getPartnerBankAccount()));
            if (contractpartnerAccounts != null && !contractpartnerAccounts.isEmpty() &&
                    contractpartnerAccounts.getFirst().getContractpartner().getId().equals(
                            moneyflow.getContractpartner().getId())) {
                rating += 50;
            }
        }
        return rating;
    }

    private int rateContractpartner(final Moneyflow moneyflow, final CompareDataDataset compareDataDataset,
                                    int rating) {
        final String partner = compareDataDataset.getPartner();
        if (partner != null && !partner.isEmpty()) {
            final String monPartner = moneyflow.getContractpartner().getName();
            final String splitPattern = "[., -]";
            int matchingWords = 0;
            int words = 0;
            for (final String cmpWord : partner.split(splitPattern)) {
                words++;
                for (final String monWord : monPartner.split(splitPattern)) {
                    if (monWord.equals(cmpWord)) {
                        rating += 10;
                        matchingWords++;
                    } else if (this.doubleMetaphone.encode(monWord).equals(this.doubleMetaphone.encode(cmpWord))) {
                        rating += 8;
                        matchingWords++;
                    }
                }
            }
            if (matchingWords == words && matchingWords != 0) {
                rating += 5;
            }
        }
        return rating;
    }

    private LocalDate calendarToLocalDate(final Calendar source) {
        final ZonedDateTime zonedDateTime;
        if (source instanceof final GregorianCalendar calendar) {
            zonedDateTime = calendar.toZonedDateTime();
        } else {
            zonedDateTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(source.getTimeInMillis()),
                    source.getTimeZone().toZoneId());
        }
        return zonedDateTime.toLocalDate();
    }

    private List<CompareDataDataset> mapCamtFileToCompareData(final String fileContents) {
        final List<CompareDataDataset> compareDataDatasets = new ArrayList<>();
        final BankToCustomerAccountReportMapper xmlMapper = new BankToCustomerAccountReportMapper();
        final StringReader characterStream = new StringReader(fileContents);
        try (characterStream) {
            final InputSource xml = new InputSource(characterStream);
            final BankToCustomerAccountReport bankToCustomerAccountReport = xmlMapper.mapXml(xml);
            for (final Entry entry : bankToCustomerAccountReport.getReport().getEntries()) {
                final CompareDataDataset data = new CompareDataDataset();
                data.setBookingDate(this.calendarToLocalDate(entry.getBookingDate()));
                if (entry.getCreditDebitIndicator() == CreditDebitCode.Credit) {
                    data.setAmount(entry.getAmount().getValue());
                    data.setPartner(
                            entry.getEntryDetails().getTransactionDetails().getRelatedParties().getDebtor().getName());
                } else {
                    data.setAmount(entry.getAmount().getValue().negate());
                    data.setPartner(entry.getEntryDetails().getTransactionDetails().getRelatedParties().getCreditor()
                            .getName());
                }
                data.setComment(String.join(" ",
                        entry.getEntryDetails().getTransactionDetails().getRemittanceInformation().getUnstructured()));
                compareDataDatasets.add(data);
            }
        } catch (final Exception e) {
            throw new BusinessException(WRONG_FILE_FORMAT_TEXT, ErrorCode.WRONG_FILE_FORMAT);
        }
        return compareDataDatasets;
    }

    private List<CompareDataDataset> mapCvsFileToCompareData(final UserID userId, final String fileContents,
                                                             final CompareDataFormat compareDataFormat) {
        final List<CompareDataDataset> compareDataDatasets = new ArrayList<>();
        final CSVParser parser = new CSVParserBuilder().withSeparator(compareDataFormat.getDelimiter()).build();

        try (final CSVReader cvsReader = new CSVReaderBuilder(new StringReader(fileContents)).withCSVParser(parser)
                .build()) {

            final List<String[]> cvsLines = cvsReader.readAll();
            boolean match = false;
            final List<String> startTrigger = compareDataFormat.getStartTrigger();


            for (final String[] cmpDataRaw : cvsLines) {
                if (cmpDataRaw.length >= 3) {
                    final List<String> startLine = List.of(cmpDataRaw[0], cmpDataRaw[1], cmpDataRaw[2]);
                    if (startLine.equals(startTrigger)) {
                        match = true;
                        continue;
                    }
                }
                if (match && compareDataFormat.getEndTrigger() != null) {
                    final List<String> endLine = List.of(cmpDataRaw[0]);
                    if (endLine.equals(compareDataFormat.getEndTrigger())) {
                        break;
                    }
                } else if (match && cmpDataRaw.length <= compareDataFormat.getPositionAmount()) {
                    // main section in CSV is done skip the rest
                    break;
                }
                if (match) {
                    compareDataDatasets.add(this.mapCsvFileMainPart(userId, compareDataFormat, cmpDataRaw));
                }
            }
        } catch (final IOException | CsvException e) {
            throw new BusinessException(WRONG_FILE_FORMAT_TEXT, ErrorCode.WRONG_FILE_FORMAT);
        }
        return compareDataDatasets;
    }

    private CompareDataDataset mapCsvFileMainPart(final UserID userId, final CompareDataFormat compareDataFormat,
                                                  final String[] cmpDataRaw) {
        final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern(compareDataFormat.getFormatDate());
        final Pattern partnerAlternativeIndicator = compareDataFormat.getPartnerAlternativeIndicator();
        final Pattern creditIndicator = compareDataFormat.getCreditIndicator();
        final CompareDataDataset data = new CompareDataDataset();
        /*
         * Date
         */
        final String dateString = cmpDataRaw[compareDataFormat.getPositionDate() - 1];
        final LocalDate date = LocalDate.parse(dateString, dateFormat);
        data.setBookingDate(date);
        /*
         * Invoice Date
         */
        if (compareDataFormat.getPositionInvoicedate() != null) {
            final String invoicedateString = cmpDataRaw[compareDataFormat.getPositionInvoicedate() - 1];
            final LocalDate invoicedate = LocalDate.parse(invoicedateString, dateFormat);
            data.setInvoiceDate(invoicedate);
        }
        /*
         * Amount
         */
        final BigDecimal amount = getAmount(compareDataFormat, cmpDataRaw, creditIndicator);
        data.setAmount(amount);
        /*
         * Partner
         */
        String partner = null;
        if (compareDataFormat.getPositionPartner() != null) {
            partner = cmpDataRaw[compareDataFormat.getPositionPartner() - 1];
        }
        // alternative Partner overrules Partner if existant
        if (compareDataFormat.getPositionPartnerAlternative() != null) {
            final String keyword = cmpDataRaw[compareDataFormat.getPartnerAlternativeIndicatorPosition()
                    - 1];
            final Matcher altPartnerMatcher = partnerAlternativeIndicator.matcher(keyword);
            if (altPartnerMatcher.find()) {
                partner = cmpDataRaw[compareDataFormat.getPositionPartnerAlternative() - 1];
            }
        }
        data.setPartner(partner);
        /*
         * Comment
         */
        final Integer posComment = compareDataFormat.getPositionComment();
        if (posComment != null) {
            final String comment = cmpDataRaw[posComment - 1];
            data.setComment(comment.trim());
            final var contractpartnerMatching = this.contractpartnerMatchingService
                    .getContractpartnerMatchingBySearchString(userId, data.getComment());
            if (contractpartnerMatching != null) {
                data.setContractpartner(contractpartnerMatching.getContractpartner());
            }
        }

        return data;
    }
}
