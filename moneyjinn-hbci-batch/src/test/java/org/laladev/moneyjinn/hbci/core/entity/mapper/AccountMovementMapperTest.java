package org.laladev.moneyjinn.hbci.core.entity.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.kapott.hbci.GV_Result.GVRKUms.UmsLine;
import org.kapott.hbci.structures.Konto;
import org.laladev.moneyjinn.hbci.core.entity.AccountMovement;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Stream;

//TODO Migrate to Java 8 java.time.*
class AccountMovementMapperTest {
    private final AccountMovementMapper accountMovementMapper = new AccountMovementMapper();
    private final Calendar expectedCalendar = Calendar.getInstance();
    private final Calendar currentCalendar = Calendar.getInstance();

    static Stream<Arguments> test_InvoiceTimestampELVOLV() {
        return Stream.of(
                Arguments.of(List.of("123456789012345678901234567", "ELV12345678 10.06 01.03"), "5"),
                Arguments.of(List.of("123456789012345678901234567", "ELV12345678 10.06 01.03 ME2"), "107"),
                Arguments.of(List.of("123456789012345678901234567", "OLV12345678 10.06 01.03"), "5"),
                Arguments.of(List.of("EREF+9999999999999999999999", "9999999999999", "MREF+G999999999999999999999",
                        "CRED+DE9999999999999999", "SVWZ+9999999999999999999999", "99999 ELV99999999 10.06 01.",
                        "03 ME4"), "5"),
                Arguments.of(List.of("OLV12345678 10.06 01.03"), "5"),
                Arguments.of(List.of("10.06 01.03 TA-NR. XXXXXX"), "83"),
                Arguments.of(List.of("100601031885492151200031520"), "5"),
                Arguments.of(List.of("10.06/01.03UHR XXXXXXXXXX"), "83"),
                Arguments.of(List.of("2025-06-10T01:03:00"), "106"),
                Arguments.of(List.of("BLAH BLAH 10-06-2025T01:03:00 BLAH"), "106"),
                Arguments.of(List.of("BLAH", "BLAH BLAH 10-06-20", "25T01:03:00 BLAH", "BLAH"), "106"),
                Arguments.of(List.of("EC XXXXXXXX 100625010300IC1"), "5")
        );
    }

    private void testInvoiceDate(final List<String> usage, final String gvcode) {
        final UmsLine entry = ObjectBuilder.getUmsLine(this.currentCalendar);

        entry.usage = usage;
        entry.gvcode = gvcode;

        final Konto account = ObjectBuilder.getKonto();

        final AccountMovement accountMovement = this.accountMovementMapper.map(entry, account);
        Assertions.assertNotNull(accountMovement);
        Assertions.assertNotNull(accountMovement.getInvoiceTimestamp());
        final GregorianCalendar invoiceCalendar = GregorianCalendar
                .from(ZonedDateTime.of(accountMovement.getInvoiceTimestamp(), ZoneId.systemDefault()));
        Assertions.assertEquals(this.expectedCalendar.get(Calendar.YEAR), invoiceCalendar.get(Calendar.YEAR));
        Assertions.assertEquals(this.expectedCalendar.get(Calendar.MONTH), invoiceCalendar.get(Calendar.MONTH));
        Assertions.assertEquals(this.expectedCalendar.get(Calendar.DAY_OF_MONTH),
                invoiceCalendar.get(Calendar.DAY_OF_MONTH));
        Assertions.assertEquals(this.expectedCalendar.get(Calendar.HOUR_OF_DAY),
                invoiceCalendar.get(Calendar.HOUR_OF_DAY));
        Assertions.assertEquals(this.expectedCalendar.get(Calendar.MINUTE), invoiceCalendar.get(Calendar.MINUTE));
        Assertions.assertEquals(this.expectedCalendar.get(Calendar.SECOND), invoiceCalendar.get(Calendar.SECOND));
        Assertions.assertEquals(this.expectedCalendar.get(Calendar.MILLISECOND),
                invoiceCalendar.get(Calendar.MILLISECOND));

    }

    @BeforeEach
    void setCalendar() {
        this.expectedCalendar.set(Calendar.MONTH, Calendar.JUNE);
        this.expectedCalendar.set(Calendar.DAY_OF_MONTH, 10);
        this.expectedCalendar.set(Calendar.HOUR_OF_DAY, 1);
        this.expectedCalendar.set(Calendar.MINUTE, 3);
        this.expectedCalendar.set(Calendar.SECOND, 0);
        this.expectedCalendar.set(Calendar.MILLISECOND, 0);

        this.currentCalendar.set(Calendar.MONTH, Calendar.JUNE);
        this.currentCalendar.set(Calendar.DAY_OF_MONTH, 20);
    }

    @ParameterizedTest
    @MethodSource("test_InvoiceTimestampELVOLV")
    void test_InvoiceTimestamps(final List<String> usage, final String gvCode) {
        this.testInvoiceDate(usage, gvCode);
    }

    @Test
    void test_ArrayIndexOutOfBoundException_notRaised() {
        final List<String> usage = new ArrayList<>();
        final String usageLine = "100601031885492151200031520";

        usage.add("EREF+YYIJ222224K446NA PP.53");
        usage.add("89.PP PAYPAL ");
        usage.add("MREF+5NHJ224NJJL8N   ");
        usage.add("");
        usage.add("CRED+LU96ZZZ000000000000000");
        usage.add("0058         ");
        usage.add("SVWZ+PP.5389.PP . superscho");
        usage.add("en01, Ihr Einkauf bei super");
        usage.add("schoen01, Artikel-38093416");
        usage.add("421");
        usage.add(usageLine);

        this.testInvoiceDate(usage, "5");
    }

    @Test
    void test_OLV_is_not_at_beginning() {
        final List<String> usage = new ArrayList<>();

        usage.add("Referenz 091217053064422201");
        usage.add("20003152");
        usage.add("085543540");
        usage.add("Mandat 85543540442216120917");
        usage.add("05");
        usage.add("Einreicher-ID DE53ZZZ000001");
        usage.add("32681");
        usage.add("091217053064422201200031520");
        usage.add(" OLV8554");
        usage.add("3540 10.06 01.03 ME0");

        this.testInvoiceDate(usage, "107");
    }

    @Test
    void test_ELplus() {
        final List<String> usage = new ArrayList<>();

        usage.add("Referenz 636756724689973223");
        usage.add("88953224");
        usage.add("2344B5662");
        usage.add("Mandat 56756756743232689005");
        usage.add("43");
        usage.add("Einreicher-ID DE41224232572");
        usage.add("AAAA4");
        usage.add("EL+ 61257899 10.06 01:03 KA");
        usage.add("AAA04 ZU");
        usage.add("GJHZTSASF ASDGGGHGFA");

        this.testInvoiceDate(usage, "107");
    }

    @Test
    void test_correctDateUsed() {
        final List<String> usage = new ArrayList<>();
        usage.add("304-2383932-4629952  0983583325674196");
        usage.add("Amazon .MktplceEU-DE");
        usage.add("0983583325674196"); // This line has to be ignored
        usage.add("100601031885492151200031520");

        this.testInvoiceDate(usage, "5");
    }

    @Test
    void test_InvoiceTimestamp83TaNrInMiddle() {
        final List<String> usage = new ArrayList<>();
        usage.add("SVWZ+KBS 99999999 AAA0000/1");
        usage.add("2.12 10.06 01.03 TA-NR. 999");
        usage.add("999 99999 AAAAAAAAAA AA AAA");
        usage.add("AAAAAAAAn AAA AA AAAAAAAA A");
        usage.add("AAA (DEBITKARTE) MIT PIN");

        this.testInvoiceDate(usage, "83");
    }

    @Test
    void test_InvoiceTimestampPreviousYear() {
        this.expectedCalendar.set(Calendar.YEAR, this.currentCalendar.get(Calendar.YEAR) - 1);
        this.expectedCalendar.set(Calendar.MONTH, Calendar.DECEMBER);
        final List<String> usage = new ArrayList<>();
        final String usageLine = "10.12/01.03UHR XXXXXXXXXX";
        usage.add(usageLine);

        this.testInvoiceDate(usage, "83");
    }
}
