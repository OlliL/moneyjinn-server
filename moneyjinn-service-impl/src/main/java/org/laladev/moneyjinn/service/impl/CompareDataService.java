//
// Copyright (c) 2015-2017 Oliver Lehmann <lehmann@ans-netz.de>
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

import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.codec.language.DoubleMetaphone;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.model.ContractpartnerAccount;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.capitalsource.CapitalsourceID;
import org.laladev.moneyjinn.model.comparedata.CompareDataDataset;
import org.laladev.moneyjinn.model.comparedata.CompareDataFormat;
import org.laladev.moneyjinn.model.comparedata.CompareDataFormatID;
import org.laladev.moneyjinn.model.comparedata.CompareDataFormatType;
import org.laladev.moneyjinn.model.comparedata.CompareDataMatching;
import org.laladev.moneyjinn.model.comparedata.CompareDataNotInDatabase;
import org.laladev.moneyjinn.model.comparedata.CompareDataNotInFile;
import org.laladev.moneyjinn.model.comparedata.CompareDataResult;
import org.laladev.moneyjinn.model.comparedata.CompareDataWrongCapitalsource;
import org.laladev.moneyjinn.model.exception.BusinessException;
import org.laladev.moneyjinn.model.moneyflow.ImportedMoneyflow;
import org.laladev.moneyjinn.model.moneyflow.Moneyflow;
import org.laladev.moneyjinn.sepa.camt.mapper.BankToCustomerAccountReportMapper;
import org.laladev.moneyjinn.sepa.camt.model.BankToCustomerAccountReport;
import org.laladev.moneyjinn.sepa.camt.model.Entry;
import org.laladev.moneyjinn.service.api.ICompareDataService;
import org.laladev.moneyjinn.service.api.IContractpartnerAccountService;
import org.laladev.moneyjinn.service.api.IImportedMoneyflowService;
import org.laladev.moneyjinn.service.api.IMoneyflowService;
import org.laladev.moneyjinn.service.dao.CompareDataFormatDao;
import org.laladev.moneyjinn.service.dao.data.CompareDataFormatData;
import org.laladev.moneyjinn.service.dao.data.mapper.CompareDataFormatDataMapper;
import org.xml.sax.InputSource;

@Named
public class CompareDataService extends AbstractService implements ICompareDataService {
	private static final String WRONG_FILE_FORMAT_TEXT = "The specified file is not parseable! Maybe you've selected the wrong format or file?";
	@Inject
	private CompareDataFormatDao compareDataFormatDao;
	@Inject
	private IMoneyflowService moneyflowService;
	@Inject
	private IImportedMoneyflowService importedMoneyflowService;
	@Inject
	private IContractpartnerAccountService contractpartnerAccountService;

	private final DoubleMetaphone doubleMetaphone = new DoubleMetaphone();

	@Override
	protected void addBeanMapper() {
		super.registerBeanMapper(new CompareDataFormatDataMapper());
	}

	@Override
	public CompareDataFormat getCompareDataFormatById(final CompareDataFormatID compareDataFormatId) {
		final CompareDataFormatData compareDataFormatData = this.compareDataFormatDao
				.getCompareDataFormatById(compareDataFormatId.getId());
		return super.map(compareDataFormatData, CompareDataFormat.class);
	}

	@Override
	public List<CompareDataFormat> getAllCompareDataFormats() {
		final List<CompareDataFormatData> compareDataFormatDatas = this.compareDataFormatDao.getAllCompareDataFormats();
		return super.mapList(compareDataFormatDatas, CompareDataFormat.class);

	}

	@Override
	public CompareDataResult compareDataFile(final UserID userId, final CompareDataFormatID compareDataFormatId,
			final CapitalsourceID capitalsourceId, final LocalDate startDate, final LocalDate endDate,
			final String fileContents) {

		final CompareDataFormat compareDataFormat = this.getCompareDataFormatById(compareDataFormatId);

		List<CompareDataDataset> compareDataDatasets = null;

		if (compareDataFormat.getType() == CompareDataFormatType.CVS) {
			compareDataDatasets = this.mapCvsFileToCompareData(fileContents, compareDataFormat);
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
			final LocalDate startDate, final LocalDate endDate) {

		List<CompareDataDataset> compareDataDatasets = null;

		final List<ImportedMoneyflow> importedMoneyflows = this.importedMoneyflowService
				.getAllImportedMoneyflowsByCapitalsourceIds(userId, Collections.singletonList(capitalsourceId),
						startDate, endDate);

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
			final LocalDate startDate, final LocalDate endDate, final List<CompareDataDataset> compareDataDatasets) {
		final CompareDataResult result = new CompareDataResult();
		final Period searchFrame = Period.ofDays(5);

		// gather all recorded moneyflows in the given period of time to work on for comparision
		final List<Moneyflow> allMoneyflows = this.moneyflowService.getAllMoneyflowsByDateRangeIncludingPrivate(userId,
				startDate, endDate);

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
						matchingMoneyflow = moneyflows.get(0);
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
						if (!matchingMoneyflow.isPrivat() || matchingMoneyflow.getUser().getId().equals(userId)) {
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
			if (!moneyflow.isPrivat() || moneyflow.getUser().getId().equals(userId)) {
				result.addCompareDataNotInFile(new CompareDataNotInFile(moneyflow));
			}
		}

		return result;
	}

	private int rateMoneyflowToDataset(final UserID userId, final Moneyflow moneyflow,
			final CompareDataDataset compareDataDataset, final CapitalsourceID capitalsourceId) {
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
		if (compareDataDataset.getPartnerBankAccount() != null) {
			final List<ContractpartnerAccount> contractpartnerAccounts = this.contractpartnerAccountService
					.getAllContractpartnerByAccounts(userId,
							Collections.singletonList(compareDataDataset.getPartnerBankAccount()));

			if (contractpartnerAccounts != null && !contractpartnerAccounts.isEmpty()) {
				if (contractpartnerAccounts.get(0).getContractpartner().getId()
						.equals(moneyflow.getContractpartner().getId())) {
					rating += 50;
				}
			}
		}

		// does our input-file contain contractpartner information?
		final String partner = compareDataDataset.getPartner();
		if (partner != null && !partner.isEmpty()) {
			final String monPartner = moneyflow.getContractpartner().getName();

			final String splitPattern = "[\\., -]";

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
		ZonedDateTime zonedDateTime;
		if (source instanceof GregorianCalendar) {
			zonedDateTime = ((GregorianCalendar) source).toZonedDateTime();
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
		final InputSource xml = new InputSource(characterStream);

		try {
			final BankToCustomerAccountReport bankToCustomerAccountReport = xmlMapper.mapXml(xml);

			for (final Entry entry : bankToCustomerAccountReport.getReport().getEntries()) {
				final CompareDataDataset data = new CompareDataDataset();
				data.setBookingDate(this.calendarToLocalDate(entry.getBookingDate()));
				switch (entry.getCreditDebitIndicator()) {
				case Credit:
					data.setAmount(entry.getAmount().getValue());
					data.setPartner(
							entry.getEntryDetails().getTransactionDetails().getRelatedParties().getDebtor().getName());
					break;
				default:
					data.setAmount(entry.getAmount().getValue().negate());
					data.setPartner(entry.getEntryDetails().getTransactionDetails().getRelatedParties().getCreditor()
							.getName());
					break;
				}

				data.setComment(String.join(" ",
						entry.getEntryDetails().getTransactionDetails().getRemittanceInformation().getUnstructured()));
				compareDataDatasets.add(data);
			}
		} catch (final Exception e) {
			throw new BusinessException(WRONG_FILE_FORMAT_TEXT, ErrorCode.WRONG_FILE_FORMAT);
		} finally {
			characterStream.close();
		}
		return compareDataDatasets;
	}

	private List<CompareDataDataset> mapCvsFileToCompareData(final String fileContents,
			final CompareDataFormat compareDataFormat) {

		final List<CompareDataDataset> compareDataDatasets = new ArrayList<>();

		final CSVParser parser = new CSVParserBuilder().withSeparator(compareDataFormat.getDelimiter()).build();
		final CSVReader cvsReader = new CSVReaderBuilder(new StringReader(fileContents)).withCSVParser(parser).build();

		try {
			final List<String[]> cvsLines = cvsReader.readAll();

			boolean match = false;

			final DateTimeFormatter dateFormat = compareDataFormat.getFormatDate();
			final List<String> startTrigger = compareDataFormat.getStartTrigger();
			final Pattern partnerAlternativeIndicator = compareDataFormat.getPartnerAlternativeIndicator();
			final Pattern creditIndicator = compareDataFormat.getCreditIndicator();

			for (final String[] cmpDataRaw : cvsLines) {
				if (cmpDataRaw.length >= 3) {
					final List<String> startLine = Arrays.asList(cmpDataRaw[0], cmpDataRaw[1], cmpDataRaw[2]);
					if (startLine.equals(startTrigger)) {
						match = true;
						continue;
					}
				}

				if (match && cmpDataRaw.length <= compareDataFormat.getPositionAmount()) {
					// main section in CSV is done skip the rest
					break;
				}

				if (match) {
					final CompareDataDataset data = new CompareDataDataset();
					/*
					 * Date
					 */
					final String dateString = cmpDataRaw[compareDataFormat.getPositionDate() - 1];
					final LocalDate date = LocalDate.parse(dateString, dateFormat);
					data.setBookingDate(date);

					/*
					 * Amount
					 */
					String amountStr = cmpDataRaw[compareDataFormat.getPositionAmount() - 1];
					if (compareDataFormat.getFormatAmountThousand() != null) {
						amountStr = amountStr.replace(compareDataFormat.getFormatAmountThousand().toString(), "");
					}
					amountStr = amountStr.replace(compareDataFormat.getFormatAmountDecimal().toString(), ".");
					amountStr = amountStr.replaceAll("[^\\-\\.0-9]*", "");
					BigDecimal amount = new BigDecimal(amountStr);

					if (compareDataFormat.getPositionCreditDebitIndicator() != null) {
						final String keyword = cmpDataRaw[compareDataFormat.getPositionCreditDebitIndicator() - 1];
						final Matcher creditIndicatorMatcher = creditIndicator.matcher(keyword);
						if (creditIndicatorMatcher.find()) {
							amount = amount.negate();
						}

					}

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
					final Short posComment = compareDataFormat.getPositionComment();
					if (posComment != null) {
						final String comment = cmpDataRaw[posComment - 1];
						data.setComment(comment.trim());
					}
					compareDataDatasets.add(data);
				}
			}
		} catch (final IOException | CsvException e) {
			throw new BusinessException(WRONG_FILE_FORMAT_TEXT, ErrorCode.WRONG_FILE_FORMAT);
		} finally {
			try {
				cvsReader.close();
			} catch (final IOException e) {
				throw new BusinessException(WRONG_FILE_FORMAT_TEXT, ErrorCode.WRONG_FILE_FORMAT);
			}
		}

		return compareDataDatasets;
	}

}
