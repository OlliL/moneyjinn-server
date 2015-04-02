//
// Copyright (c) 2014-2015 Oliver Lehmann <oliver@laladev.org>
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
// $Id: TransactionMapper.java,v 1.4 2015/03/31 18:55:58 olivleh1 Exp $
//
package org.laladev.hbci.entity.mapper;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.kapott.hbci.GV_Result.GVRKUms.UmsLine;
import org.kapott.hbci.structures.Konto;
import org.laladev.hbci.entity.AccountMovement;

public class AccountMovementMapper {
	private final DateFormat dateTimeWithYearFormatter = new SimpleDateFormat("ddMMyyHHmmss");
	private final DateFormat dateTimeWithoutYearSpaceFormatter = new SimpleDateFormat("dd.MM HH.mm");
	private final DateFormat dateTimeWithoutYearFormatter = new SimpleDateFormat("ddMMHHmm");
	private final DateFormat dateTimeWithoutYearSlashFormatter = new SimpleDateFormat("dd.MM/HH.mm");

	public AccountMovement map(final UmsLine entry, final Konto myAccount) {
		final AccountMovement accountMovement = new AccountMovement();
		accountMovement.setMyIban(myAccount.iban);
		accountMovement.setMyBic(myAccount.bic);
		accountMovement.setMyAccountnumber(Long.valueOf(myAccount.number));
		accountMovement.setMyBankcode(Integer.valueOf(myAccount.blz));

		accountMovement.setBookingDate(new Date(entry.bdate.getTime()));
		accountMovement.setValueDate(new Date(entry.valuta.getTime()));

		if (entry.other != null) {
			if (entry.other.isSEPAAccount()) {
				if (Character.isDigit(entry.other.iban.toCharArray()[0])) {
					accountMovement.setOtherAccountnumber(Long.valueOf(entry.other.iban));
					accountMovement.setOtherBankcode(Integer.valueOf(entry.other.bic));
				} else {
					accountMovement.setOtherIban(entry.other.iban);
					accountMovement.setOtherBic(entry.other.bic);
				}
			} else {
				if (Character.isDigit(entry.other.number.toCharArray()[0])) {
					accountMovement.setOtherAccountnumber(Long.valueOf(entry.other.number));
					accountMovement.setOtherBankcode(Integer.valueOf(entry.other.blz));
				} else {
					accountMovement.setOtherIban(entry.other.number);
					accountMovement.setOtherBic(entry.other.blz);
				}
			}
			if (entry.other.name2 != null) {
				accountMovement.setOtherName(entry.other.name + entry.other.name2);
			} else {
				accountMovement.setOtherName(entry.other.name);
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
		accountMovement.setMovementReason(usage);
		accountMovement.setMovementTypeCode(Short.valueOf(entry.gvcode));
		accountMovement.setMovementTypeText(entry.text);

		accountMovement.setCustomerReference(entry.customerref);
		accountMovement.setBankReference(entry.instref);

		accountMovement.setCancellation(entry.isStorno);
		accountMovement.setAdditionalInformation(entry.additional);
		if (entry.addkey != null) {
			accountMovement.setAdditionalKey(Short.valueOf(entry.addkey));
		}

		accountMovement.setPrimaNota(entry.primanota);
		accountMovement.setBalanceDate(new Date(entry.saldo.timestamp.getTime()));
		accountMovement.setBalanceValue(entry.saldo.value.getBigDecimalValue());
		accountMovement.setBalanceCurrency(entry.saldo.value.getCurr());

		final java.util.Date invoiceDate = getInvoiceDate(accountMovement);
		if (invoiceDate != null) {
			accountMovement.setInvoiceDate(new Date(invoiceDate.getTime()));
		}

		return accountMovement;
	}

	/**
	 * This method tries to compute the Invoice date of a {@link Transaction} by analysing its usage
	 * text based on the used Business-Transaction-Code + Text. <br>
	 *
	 * @param {@link
	 * 			Transaction} transaction
	 * @return {@link Date} invoiceDate (or null if not determinable)
	 */
	private java.util.Date getInvoiceDate(final AccountMovement accountMovement) {
		java.util.Date invoiceDate = null;
		try {
			if (accountMovement.getMovementReason() != null && accountMovement.getMovementTypeText() != null
					&& accountMovement.getMovementTypeCode() != null) {
				final String movementReason = accountMovement.getMovementReason();
				final Short movementTypeCode = accountMovement.getMovementTypeCode();
				final String movementTypeText = accountMovement.getMovementTypeText();
				if (movementTypeCode.equals(5)) {

					// check GvText as it could be also "KREDKARTUMS"
					if ("LASTSCHRIFT".equals(movementTypeText)) {
						if (movementReason.startsWith("ELV")) {
							// Usage text starts with ELVXXXXXXXX 15.12 16.29 ME1
							invoiceDate = this.dateTimeWithoutYearSpaceFormatter
									.parse(movementReason.substring(12, 23));
						} else {
							// Usage text starts with 01121802XXXXXXXXXXXXXXXXXXX
							invoiceDate = this.dateTimeWithoutYearFormatter.parse(movementReason.substring(0, 8));
						}

					} else if ("KARTENVERFÜG".equals(movementTypeText)) {
						if (movementReason.startsWith("EC ")) {
							// Usage text starts with EC XXXXXXXX 090215165422IC1
							invoiceDate = this.dateTimeWithYearFormatter.parse(movementReason.substring(12, 24));
						}

					}
				} else if (movementTypeCode.equals(83)) {
					final String[] lines = movementReason.split("\r\n|\r|\n");
					for (final String line : lines) {
						if ("AUSZAHLUNG".equals(movementTypeText)) {
							if (line.contains("TA-NR")) {
								// 10.02 16.56 TA-NR. XXXXXX
								invoiceDate = this.dateTimeWithoutYearSpaceFormatter.parse(line.substring(0, 11));
								break;
							}
						} else if ("KARTENVERFÜG".equals(movementTypeText)) {
							if (line.length() >= 16 && line.substring(11, 15).equals("UHR ")) {
								// 16.02/07.49UHR XXXXXXXXXX
								invoiceDate = this.dateTimeWithoutYearSlashFormatter.parse(line.substring(0, 11));
								break;
							}
						}
					}
					if (invoiceDate != null) {
						setYear(accountMovement.getBookingDate(), invoiceDate);
					}
				}
			}
		} catch (final Exception e) {
			System.out.println(accountMovement);
			e.printStackTrace();
		}

		return invoiceDate;
	}

	private void setYear(final Date bookingDate, final java.util.Date invoiceDate) {
		// in case we found an invoiceDate, the year information is missing. We are now going to do
		// our best to restore it
		final Calendar calValuta = Calendar.getInstance();
		calValuta.setTime(bookingDate);
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
