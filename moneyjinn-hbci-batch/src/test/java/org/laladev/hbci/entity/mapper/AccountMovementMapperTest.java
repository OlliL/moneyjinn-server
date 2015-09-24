package org.laladev.hbci.entity.mapper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.kapott.hbci.GV_Result.GVRKUms.UmsLine;
import org.kapott.hbci.structures.Konto;
import org.laladev.hbci.entity.AccountMovement;

public class AccountMovementMapperTest {
	private final AccountMovementMapper accountMovementMapper = new AccountMovementMapper();
	private final Calendar expectedCalendar = Calendar.getInstance();
	private final Calendar currentCalendar = Calendar.getInstance();

	private void testInvoiceDate(final List<String> usage, final String gvcode) {
		final UmsLine entry = ObjectBuilder.getUmsLine(this.currentCalendar);

		entry.usage = usage;
		entry.gvcode = gvcode;

		final Konto account = ObjectBuilder.getKonto();

		final AccountMovement accountMovement = this.accountMovementMapper.map(entry, account);
		Assert.assertNotNull(accountMovement);
		Assert.assertNotNull(accountMovement.getInvoiceTimestamp());
		final Calendar invoiceCalendar = Calendar.getInstance();
		invoiceCalendar.setTime(accountMovement.getInvoiceTimestamp());
		Assert.assertEquals(this.expectedCalendar.get(Calendar.YEAR), invoiceCalendar.get(Calendar.YEAR));
		Assert.assertEquals(this.expectedCalendar.get(Calendar.MONTH), invoiceCalendar.get(Calendar.MONTH));
		Assert.assertEquals(this.expectedCalendar.get(Calendar.DAY_OF_MONTH), invoiceCalendar.get(Calendar.DAY_OF_MONTH));
		Assert.assertEquals(this.expectedCalendar.get(Calendar.HOUR_OF_DAY), invoiceCalendar.get(Calendar.HOUR_OF_DAY));
		Assert.assertEquals(this.expectedCalendar.get(Calendar.MINUTE), invoiceCalendar.get(Calendar.MINUTE));
		Assert.assertEquals(this.expectedCalendar.get(Calendar.SECOND), invoiceCalendar.get(Calendar.SECOND));
		Assert.assertEquals(this.expectedCalendar.get(Calendar.MILLISECOND), invoiceCalendar.get(Calendar.MILLISECOND));

	}

	@Before
	public void setCalendar() {
		this.expectedCalendar.set(Calendar.MONTH, Calendar.JUNE);
		this.expectedCalendar.set(Calendar.DAY_OF_MONTH, 10);
		this.expectedCalendar.set(Calendar.HOUR_OF_DAY, 1);
		this.expectedCalendar.set(Calendar.MINUTE, 3);
		this.expectedCalendar.set(Calendar.SECOND, 0);
		this.expectedCalendar.set(Calendar.MILLISECOND, 0);

		this.currentCalendar.set(Calendar.MONTH, Calendar.JULY);
		this.currentCalendar.set(Calendar.DAY_OF_MONTH, 1);
	}

	@Test
	public void test_InvoiceTimestamp5ELV() {
		final List<String> usage = new ArrayList<String>();
		final String usageLine = "ELV12345678 10.06 01.03";
		usage.add("123456789012345678901234567");
		usage.add(usageLine);

		this.testInvoiceDate(usage, "5");
	}

	@Test
	public void test_InvoiceTimestamp5OLV() {
		final List<String> usage = new ArrayList<String>();
		final String usageLine = "OLV12345678 10.06 01.03";
		usage.add("123456789012345678901234567");
		usage.add(usageLine);

		this.testInvoiceDate(usage, "5");
	}

	@Test
	public void test_InvoiceTimestamp5OLVFirstLine() {
		final List<String> usage = new ArrayList<String>();
		final String usageLine = "OLV12345678 10.06 01.03";
		usage.add(usageLine);

		this.testInvoiceDate(usage, "5");
	}

	@Test
	public void test_InvoiceTimestamp5EC() {
		this.expectedCalendar.set(Calendar.SECOND, 22);

		final List<String> usage = new ArrayList<String>();
		final String usageLine = "EC XXXXXXXX 100615010322IC1";
		usage.add(usageLine);

		this.testInvoiceDate(usage, "5");
	}

	@Test
	public void test_InvoiceTimestamp5Lastschrift() {
		final List<String> usage = new ArrayList<String>();
		final String usageLine = "100601031885492151200031520";
		usage.add(usageLine);

		this.testInvoiceDate(usage, "5");
	}

	@Test
	public void test_InvoiceTimestamp83TaNr() {
		final List<String> usage = new ArrayList<String>();
		final String usageLine = "10.06 01.03 TA-NR. XXXXXX";
		usage.add(usageLine);

		this.testInvoiceDate(usage, "83");
	}

	@Test
	public void test_InvoiceTimestamp83Uhr() {
		final List<String> usage = new ArrayList<String>();
		final String usageLine = "10.06/01.03UHR XXXXXXXXXX";
		usage.add(usageLine);

		this.testInvoiceDate(usage, "83");
	}

	@Test
	public void test_InvoiceTimestampPreviousYear() {
		this.expectedCalendar.set(Calendar.YEAR, this.currentCalendar.get(Calendar.YEAR) - 1);
		this.expectedCalendar.set(Calendar.MONTH, Calendar.DECEMBER);
		final List<String> usage = new ArrayList<String>();
		final String usageLine = "10.12/01.03UHR XXXXXXXXXX";
		usage.add(usageLine);

		this.testInvoiceDate(usage, "83");
	}

	@Test
	public void test_InvoiceTimestamp106() {
		this.expectedCalendar.set(Calendar.SECOND, 22);

		final List<String> usage = new ArrayList<String>();
		final String usageLine = "2015-06-10T01:03:22";
		usage.add(usageLine);

		this.testInvoiceDate(usage, "106");
	}

	@Test
	@Ignore
	public void test_InvoiceTimestamp96Kontouebertrag() {
		final List<String> usage = new ArrayList<String>();
		final String usageLine = "DATUM 10.06.2015,01.03 UHR, 1.TAN 123456";
		usage.add(usageLine);

		this.testInvoiceDate(usage, "96");
	}

}
