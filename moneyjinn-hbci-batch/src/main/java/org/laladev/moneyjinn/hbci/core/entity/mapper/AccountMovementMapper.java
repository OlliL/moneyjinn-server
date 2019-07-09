//
// Copyright (c) 2014-2017 Oliver Lehmann <lehmann@ans-netz.de>
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
package org.laladev.moneyjinn.hbci.core.entity.mapper;

import org.kapott.hbci.GV_Result.GVRKUms.UmsLine;
import org.kapott.hbci.structures.Konto;
import org.laladev.moneyjinn.hbci.core.entity.AccountMovement;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

// TODO Migrate to Java 8 java.time.*
public class AccountMovementMapper {
	private static final short MOVEMENT_TYPE_DIRECT_DEBIT = (short) 5;
	private static final short MOVEMENT_TYPE_WITHDRAWAL = (short) 83;
	private static final short MOVEMENT_TYPE_SEPA_CARDS_CLEARING = (short) 106;
	private static final short MOVEMENT_TYPE_SEPA_DIRECT_DEBIT_POS = (short) 107;
	private final DateFormat dateTimeWithYearFormatter = new SimpleDateFormat("ddMMyyHHmmss");
	private final DateFormat dateTimeWithoutYearFormatter = new SimpleDateFormat("ddMMHHmm");
	private final DateFormat dateTimeWithoutYearSpaceFormatterDot = new SimpleDateFormat("dd.MM HH.mm");
	private final DateFormat dateTimeWithoutYearSpaceFormatterColon = new SimpleDateFormat("dd.MM HH:mm");
	private final DateFormat dateTimeWithoutYearSlashFormatter = new SimpleDateFormat("dd.MM/HH.mm");
	private final DateFormat dateTimeFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

	public AccountMovement map(final UmsLine entry, final Konto myAccount) {
		final AccountMovement accountMovement = new AccountMovement();
		accountMovement.setCreationTime(LocalDateTime.now());
		accountMovement.setMyIban(myAccount.iban);
		accountMovement.setMyBic(myAccount.bic);
		accountMovement.setMyAccountnumber(Long.valueOf(myAccount.number));
		accountMovement.setMyBankcode(Integer.valueOf(myAccount.blz));

		final Instant instantBookingDate = Instant.ofEpochMilli(entry.bdate.getTime());
		final Instant instantValueDate = Instant.ofEpochMilli(entry.valuta.getTime());

		accountMovement.setBookingDate(LocalDateTime.ofInstant(instantBookingDate, ZoneId.systemDefault()).toLocalDate());
		accountMovement.setValueDate(LocalDateTime.ofInstant(instantValueDate, ZoneId.systemDefault()).toLocalDate());

		if (entry.other != null) {
			if (entry.other.isSEPAAccount() && entry.other.iban != null && !entry.other.iban.isEmpty()) {
				if (Character.isDigit(entry.other.iban.toCharArray()[0])) {
					// Ikano bank sends an empty blz as bankcode - I've no idea why?!
					if (entry.other.bic.equals("")) {
						entry.other.bic = "0";
					}
					accountMovement.setOtherAccountnumber(Long.valueOf(entry.other.iban));
					accountMovement.setOtherBankcode(Integer.valueOf(entry.other.bic));
				} else {
					accountMovement.setOtherIban(entry.other.iban);
					accountMovement.setOtherBic(entry.other.bic);
				}

			} else if (entry.other.number != null && !entry.other.number.isEmpty()) {
				if (Character.isDigit(entry.other.number.toCharArray()[0])) {
					// Ikano bank sends an empty blz as bankcode - I've no idea why?!
					if (entry.other.blz.equals("")) {
						entry.other.blz = "0";
					}
					accountMovement.setOtherAccountnumber(Long.valueOf(entry.other.number));
					accountMovement.setOtherBankcode(Integer.valueOf(entry.other.blz));
				} else {
					accountMovement.setOtherIban(entry.other.number);
					accountMovement.setOtherBic(entry.other.blz);
				}
			}
			if (entry.other.name2 != null) {
				accountMovement.setOtherName(entry.other.name.trim() + entry.other.name2.trim());
			} else {
				accountMovement.setOtherName(entry.other.name.trim());
			}
		}

		if (entry.charge_value != null) {
			accountMovement.setChargeValue(entry.charge_value.getBigDecimalValue());
			accountMovement.setChargeCurrency(entry.charge_value.getCurr());
		}

		if (entry.orig_value != null) {
			accountMovement.setOriginalValue(entry.orig_value.getBigDecimalValue());
			accountMovement.setOriginalCurrency(entry.orig_value.getCurr());
		}

		accountMovement.setMovementValue(entry.value.getBigDecimalValue());
		accountMovement.setMovementCurrency(entry.value.getCurr());
		String usage = null;
		for (final String usageline : entry.usage) {
			usage = usage == null ? new String(usageline) : usage + '\n' + usageline;
		}

		if (usage != null) {
			usage = usage.trim();
		}
		accountMovement.setMovementReason(usage);
		accountMovement.setMovementTypeCode(Short.valueOf(entry.gvcode));
		if (entry.text != null) {
			accountMovement.setMovementTypeText(entry.text.trim());
		}

		accountMovement.setCustomerReference(entry.customerref);
		if (entry.instref != null && entry.instref.length() > 0) {
			accountMovement.setBankReference(entry.instref);
		}

		accountMovement.setCancellation(entry.isStorno);
		accountMovement.setAdditionalInformation(entry.additional);
		if (entry.addkey != null) {
			accountMovement.setAdditionalKey(Short.valueOf(entry.addkey));
		}

		accountMovement.setPrimaNota(entry.primanota);

		final Instant instantBalanceDate = Instant.ofEpochMilli(entry.saldo.timestamp.getTime());

		accountMovement.setBalanceDate(LocalDateTime.ofInstant(instantBalanceDate, ZoneId.systemDefault()).toLocalDate());
		accountMovement.setBalanceValue(entry.saldo.value.getBigDecimalValue());
		accountMovement.setBalanceCurrency(entry.saldo.value.getCurr());

		final Timestamp invoiceDate = this.getInvoiceTimestamp(accountMovement);
		if (invoiceDate != null) {
			accountMovement.setInvoiceTimestamp(invoiceDate.toLocalDateTime());
		}

		return accountMovement;
	}

	/**
	 * This method tries to compute the Invoice date of a {@link AccountMovement} by analysing its
	 * usage text based on the used Business-AccountMovement-Code + Text. <br>
	 *
	 * @param {@link AccountMovement} accountMovement
	 * @return {@link Date} invoiceDate (or null if not determinable)
	 */
	private Timestamp getInvoiceTimestamp(final AccountMovement accountMovement) {
		java.util.Date invoiceDate = null;
		try {
			if (accountMovement.getMovementReason() != null && accountMovement.getMovementReason().length() > 0
					&& accountMovement.getMovementTypeCode() != null) {
				final String movementReason = accountMovement.getMovementReason();
				final Short movementTypeCode = accountMovement.getMovementTypeCode();
				final List<String> lines = Arrays.asList(movementReason.split("\r\n|\r|\n"));

				if (movementTypeCode.equals(MOVEMENT_TYPE_DIRECT_DEBIT) || movementTypeCode.equals(MOVEMENT_TYPE_SEPA_DIRECT_DEBIT_POS)) {

					final Iterator<String> lineIterator = lines.iterator();

					lineloop:
					while (lineIterator.hasNext()) {
						String line = lineIterator.next();

						if ((line.startsWith(" ELV") || line.startsWith(" OLV")) && line.length() == 8 && lineIterator.hasNext()) {
							// Usage text starts with " OLVXXXX" and continues on the next line with
							// "XXXX 09.12 17.05 ME0"
							line = line.substring(1) + lineIterator.next();
						} else if (line.startsWith("ELV") || line.startsWith("OLV")) {
							// Usage text starts with "ELVXXXXXXXX 15.12 16.29 ME1"
							invoiceDate = this.dateTimeWithoutYearSpaceFormatterDot.parse(line.substring(12, 23));
							this.setYear(accountMovement.getBookingDate(), invoiceDate);
							break;

						} else if (line.startsWith("EL+")) {
							// Usage text starts with "EL+ XXXXXXXX 06.07 09:46 KA"
							invoiceDate = this.dateTimeWithoutYearSpaceFormatterColon.parse(line.substring(13, 24));
							this.setYear(accountMovement.getBookingDate(), invoiceDate);
							break;

						} else if (line.startsWith("EC ")) {
							// Usage text starts with "EC XXXXXXXX 090215165422IC1"
							invoiceDate = this.dateTimeWithYearFormatter.parse(line.substring(12, 24));
							break;
						} else {
							if (line.length() == 27) {
								for (int i = 0; i < line.length(); i++) {
									if (!Character.isDigit(line.charAt(i))) {
										continue lineloop;
									}
								}
								invoiceDate = this.dateTimeWithoutYearFormatter.parse(line.substring(0, 8));
								this.setYear(accountMovement.getBookingDate(), invoiceDate);
								/*
								 * the invoice date must be before or equal than the bookingdate and
								 * not more than two weeks before the bookingdate
								 */
								final Date bookingDate = Date.valueOf(accountMovement.getBookingDate());
								if (invoiceDate.before(bookingDate) && bookingDate.getTime() - invoiceDate.getTime() > 14 * 86400000) {
									invoiceDate = null;
								} else {
									break;
								}
							}
						}
					}

				} else if (movementTypeCode.equals(MOVEMENT_TYPE_WITHDRAWAL)) {
					for (final String line : lines) {
						if (line.contains("TA-NR.")) {
							// 10.02 16.56 TA-NR. XXXXXX
							invoiceDate = this.dateTimeWithoutYearSpaceFormatterDot.parse(line.substring(0, 11));
							break;
						} else if (line.length() >= 16 && line.substring(11, 15).equals("UHR ")) {
							// 16.02/07.49UHR XXXXXXXXXX
							invoiceDate = this.dateTimeWithoutYearSlashFormatter.parse(line.substring(0, 11));
							break;
						}
					}
					if (invoiceDate != null) {
						this.setYear(accountMovement.getBookingDate(), invoiceDate);
					}
				} else if (movementTypeCode.equals(MOVEMENT_TYPE_SEPA_CARDS_CLEARING)) {
					for (final String line : lines) {
						if (line.matches("^[0-9][0-9][0-9][0-9]-[0-9][0-9]-[0-9][0-9]T[0-9][0-9]:[0-9][0-9]:[0-9][0-9]$")) {
							// 2015-09-22T17:16:41
							invoiceDate = this.dateTimeFormatter.parse(line);
							break;
						}
					}
				}
			}
		} catch (final Exception e) {
			System.out.println(accountMovement);
			e.printStackTrace();
		}

		if (invoiceDate == null) {
			return null;
		}
		return new Timestamp(invoiceDate.getTime());
	}

	private void setYear(final LocalDate bookingDate, final java.util.Date invoiceDate) {
		// in case we found an invoiceDate, the year information is missing. We are now going to do
		// our best to restore it
		final Calendar calValuta = GregorianCalendar.from(bookingDate.atStartOfDay(ZoneId.systemDefault()));

		final Calendar calInvoice = Calendar.getInstance();
		calInvoice.setTime(invoiceDate);
		if (calInvoice.get(Calendar.MONTH) > calValuta.get(Calendar.MONTH)) {
			// Invoice was generated in the previous year (invoicemonth is higher than bookingmonth)
			calInvoice.set(Calendar.YEAR, calValuta.get(Calendar.YEAR) - 1);
		} else {
			// Month is smaller or same so it is the same year as the valuta-year
			calInvoice.set(Calendar.YEAR, calValuta.get(Calendar.YEAR));
		}
		invoiceDate.setTime(calInvoice.getTimeInMillis());
	}

}
