//
// Copyright (c) 2014-2025 Oliver Lehmann <lehmann@ans-netz.de>
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
import java.util.*;
import java.util.regex.Pattern;

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
    private final DateFormat dateTimeFormatterGerman = new SimpleDateFormat("dd-MM-yyyy'T'HH:mm:ss");
    private final Pattern elvOlvPattern = Pattern
            .compile(".*(ELV|OLV)\\d{8} ([0-3]\\d\\.[0-1]\\d [0-2]\\d\\.[0-5]\\d).*");
    private final Pattern taNrPattern = Pattern.compile(".*([0-3]\\d\\.[0-1]\\d [0-2]\\d\\.[0-5]\\d) TA-NR. .*");
    private final Pattern germanDateTimePattern = Pattern.compile(
            ".*(\\d\\d-\\d\\d-\\d\\d\\d\\dT\\d\\d:\\d\\d:\\d\\d).*");

    public AccountMovement map(final UmsLine entry, final Konto myAccount) {
        final AccountMovement accountMovement = new AccountMovement();
        accountMovement.setCreationTime(LocalDateTime.now());
        accountMovement.setMyIban(myAccount.iban);
        accountMovement.setMyBic(myAccount.bic);
        accountMovement.setMyAccountnumber(Long.valueOf(myAccount.number));
        accountMovement.setMyBankcode(Integer.valueOf(myAccount.blz));

        final Instant instantBookingDate = Instant.ofEpochMilli(entry.bdate.getTime());
        final Instant instantValueDate = Instant.ofEpochMilli(entry.valuta.getTime());

        accountMovement
                .setBookingDate(LocalDateTime.ofInstant(instantBookingDate, ZoneId.systemDefault()).toLocalDate());
        accountMovement.setValueDate(LocalDateTime.ofInstant(instantValueDate, ZoneId.systemDefault()).toLocalDate());

        if (entry.other != null) {
            if (entry.other.iban != null && !entry.other.iban.isEmpty()) {
                if (Character.isDigit(entry.other.iban.toCharArray()[0])) {
                    this.setAccountnumber(entry.other.iban, entry.other.bic, accountMovement);
                } else {
                    this.setIbanBic(entry.other.iban, entry.other.bic, accountMovement);
                }

            } else if (entry.other.number != null && !entry.other.number.isEmpty()) {
                if (Character.isDigit(entry.other.number.toCharArray()[0])) {
                    this.setAccountnumber(entry.other.number, entry.other.blz, accountMovement);
                } else {
                    this.setIbanBic(entry.other.number, entry.other.blz, accountMovement);
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
            usage = usage == null ? usageline : usage + '\n' + usageline;
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

        accountMovement
                .setBalanceDate(LocalDateTime.ofInstant(instantBalanceDate, ZoneId.systemDefault()).toLocalDate());
        accountMovement.setBalanceValue(entry.saldo.value.getBigDecimalValue());
        accountMovement.setBalanceCurrency(entry.saldo.value.getCurr());

        final Timestamp invoiceDate = this.getInvoiceTimestamp(accountMovement);
        if (invoiceDate != null) {
            accountMovement.setInvoiceTimestamp(invoiceDate.toLocalDateTime());
        }

        return accountMovement;
    }

    private void setAccountnumber(final String number, String blz, final AccountMovement accountMovement) {
        // Ikano bank sends an empty blz as bankcode - I've no idea why?!
        if (blz.equals("")) {
            blz = "0";
        }
        accountMovement.setOtherAccountnumber(Long.valueOf(number));
        accountMovement.setOtherBankcode(Integer.valueOf(blz));
    }

    private void setIbanBic(final String iban, final String bic, final AccountMovement accountMovement) {
        accountMovement.setOtherIban(this.getIban(iban));
        final String bicTrimmed = bic == null ? "" : bic.trim();
        accountMovement.setOtherBic(bicTrimmed);
    }

    private String getIban(final String iban) {
        if (iban == null || iban.isBlank())
            return null;

        if (!iban.contains("/"))
            return iban;

        return iban.substring(0, iban.indexOf("/"));
    }

    /**
     * This method tries to compute the Invoice date of a {@link AccountMovement} by
     * analysing its usage text based on the used Business-AccountMovement-Code +
     * Text. <br>
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
                final String oneLine = movementReason.replaceAll("\r\n|\r|\n", "");

                if (movementTypeCode.equals(MOVEMENT_TYPE_DIRECT_DEBIT)
                        || movementTypeCode.equals(MOVEMENT_TYPE_SEPA_DIRECT_DEBIT_POS)) {

                    final var elvOlvMatcher = this.elvOlvPattern.matcher(oneLine);

                    if (elvOlvMatcher.matches()) {
                        // ELV12345678 10.06 01.03
                        invoiceDate = this.dateTimeWithoutYearSpaceFormatterDot.parse(elvOlvMatcher.group(2));
                        this.setYear(accountMovement.getBookingDate(), invoiceDate);
                    } else {

                        final Iterator<String> lineIterator = lines.iterator();

                        lineloop:
                        while (lineIterator.hasNext()) {
                            final String line = lineIterator.next();

                            if (line.startsWith("EL+")) {
                                // Usage text starts with "EL+ XXXXXXXX 06.07 09:46 KA"
                                invoiceDate = this.dateTimeWithoutYearSpaceFormatterColon.parse(line.substring(13, 24));
                                this.setYear(accountMovement.getBookingDate(), invoiceDate);
                                break;

                            } else if (line.startsWith("EC ")) {
                                // Usage text starts with "EC XXXXXXXX 090215165422IC1"
                                invoiceDate = this.dateTimeWithYearFormatter.parse(line.substring(12, 24));
                                break;
                            } else {
                                // 100601031885492151200031520 --> 10060103 --> 10.06. 01:03
                                if (line.length() == 27) {
                                    for (int i = 0; i < line.length(); i++) {
                                        if (!Character.isDigit(line.charAt(i))) {
                                            continue lineloop;
                                        }
                                    }
                                    invoiceDate = this.dateTimeWithoutYearFormatter.parse(line.substring(0, 8));
                                    this.setYear(accountMovement.getBookingDate(), invoiceDate);
                                    /*
                                     * the invoice date must be before or equal than the bookingdate and not more
                                     * than two weeks before the bookingdate
                                     */
                                    final Date bookingDate = Date.valueOf(accountMovement.getBookingDate());
                                    if (invoiceDate.before(bookingDate)
                                            && bookingDate.getTime() - invoiceDate.getTime() > 14 * 86400000) {
                                        invoiceDate = null;
                                    } else {
                                        break;
                                    }
                                }
                            }
                        }
                    }

                } else if (movementTypeCode.equals(MOVEMENT_TYPE_WITHDRAWAL)) {
                    final var taNrMatcher = this.taNrPattern.matcher(oneLine);

                    if (taNrMatcher.matches()) {
                        // 10.06 01.03 TA-NR. XXXXXX
                        invoiceDate = this.dateTimeWithoutYearSpaceFormatterDot.parse(taNrMatcher.group(1));
                    } else {
                        for (final String line : lines) {
                            if (line.length() >= 16 && line.substring(11, 15).equals("UHR ")) {
                                // 16.02/07.49UHR XXXXXXXXXX
                                invoiceDate = this.dateTimeWithoutYearSlashFormatter.parse(line.substring(0, 11));
                                break;
                            }
                        }
                    }
                    if (invoiceDate != null) {
                        this.setYear(accountMovement.getBookingDate(), invoiceDate);
                    }
                } else if (movementTypeCode.equals(MOVEMENT_TYPE_SEPA_CARDS_CLEARING)) {
                    final var germanDateTimeMatcher = this.germanDateTimePattern.matcher(oneLine);

                    if (germanDateTimeMatcher.matches()) {
                        // 10-06-2015T01:03:22
                        invoiceDate = this.dateTimeFormatterGerman.parse(germanDateTimeMatcher.group(1));
                    } else {
                        for (final String line : lines) {
                            if (line.matches(
                                    "^\\d\\d\\d\\d-\\d\\d-\\d\\dT[0-2]\\d:\\d\\d:\\d\\d$")) {
                                // 2015-09-22T17:16:41
                                invoiceDate = this.dateTimeFormatter.parse(line);
                                break;
                            }
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
        // in case we found an invoiceDate, the year information is missing. We are now
        // going to do
        // our best to restore it
        final Calendar calValuta = GregorianCalendar.from(bookingDate.atStartOfDay(ZoneId.systemDefault()));

        final Calendar calInvoice = Calendar.getInstance();
        calInvoice.setTime(invoiceDate);
        if (calInvoice.get(Calendar.MONTH) > calValuta.get(Calendar.MONTH)) {
            // Invoice was generated in the previous year (invoicemonth is higher than
            // bookingmonth)
            calInvoice.set(Calendar.YEAR, calValuta.get(Calendar.YEAR) - 1);
        } else {
            // Month is smaller or same so it is the same year as the valuta-year
            calInvoice.set(Calendar.YEAR, calValuta.get(Calendar.YEAR));
        }
        invoiceDate.setTime(calInvoice.getTimeInMillis());
    }

}
