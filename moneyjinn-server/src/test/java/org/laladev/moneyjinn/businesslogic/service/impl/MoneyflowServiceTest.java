package org.laladev.moneyjinn.businesslogic.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.util.List;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Test;
import org.laladev.moneyjinn.AbstractTest;
import org.laladev.moneyjinn.model.access.Group;
import org.laladev.moneyjinn.model.access.GroupID;
import org.laladev.moneyjinn.model.access.User;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.capitalsource.CapitalsourceID;
import org.laladev.moneyjinn.model.exception.BusinessException;
import org.laladev.moneyjinn.model.moneyflow.Moneyflow;
import org.laladev.moneyjinn.model.moneyflow.MoneyflowID;
import org.laladev.moneyjinn.server.builder.CapitalsourceTransportBuilder;
import org.laladev.moneyjinn.server.builder.MoneyflowTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.service.api.IMoneyflowService;

public class MoneyflowServiceTest extends AbstractTest {
	@Inject
	private IMoneyflowService moneyflowService;

	private static final UserID USER_ID_1 = new UserID(UserTransportBuilder.USER1_ID);

	@Test
	public void monthHasMoneyflows_true() {
		final boolean result = this.moneyflowService.monthHasMoneyflows(USER_ID_1, (short) 2010, Month.JANUARY);
		Assert.assertTrue(result);
	}

	@Test
	public void monthHasMoneyflows_false() {
		final boolean result = this.moneyflowService.monthHasMoneyflows(USER_ID_1, (short) 2010, Month.DECEMBER);
		Assert.assertFalse(result);
	}

	@Test
	public void getPreviousMoneyflowDate_notExisting() {
		final LocalDate date = this.moneyflowService.getPreviousMoneyflowDate(USER_ID_1,
				LocalDate.of(2008, Month.DECEMBER, 1));
		Assert.assertNull(date);
	}

	@Test
	public void getNextMoneyflowDate_notExisting() {
		final LocalDate date = this.moneyflowService.getNextMoneyflowDate(USER_ID_1, LocalDate.of(2010, Month.MAY, 31));
		Assert.assertNull(date);
	}

	@Test
	public void searchMoneyflowsByAmountDate_matchFound() {
		final List<Moneyflow> moneyflows = this.moneyflowService.searchMoneyflowsByAmountDate(USER_ID_1,
				LocalDate.of(2010, Month.MAY, 3), new BigDecimal("-5.00"), Period.ofDays(5));
		Assert.assertNotNull(moneyflows);
		Assert.assertEquals(2, moneyflows.size());
	}

	@Test
	public void searchMoneyflowsByAmountDate_noMatchFound() {
		final List<Moneyflow> moneyflows = this.moneyflowService.searchMoneyflowsByAmountDate(USER_ID_1,
				LocalDate.of(2010, Month.MAY, 20), new BigDecimal("-5.00"), Period.ofDays(5));
		Assert.assertNotNull(moneyflows);
		Assert.assertEquals(0, moneyflows.size());
	}

	@Test
	public void searchMoneyflowsByAmountDate_matchFoundOnlyInMonthOfBookingDateNotInTheWhole200DayPeriod() {
		final List<Moneyflow> moneyflows = this.moneyflowService.searchMoneyflowsByAmountDate(USER_ID_1,
				LocalDate.of(2010, Month.JANUARY, 3), new BigDecimal("-10.00"), Period.ofDays(200));
		Assert.assertNotNull(moneyflows);
		Assert.assertEquals(1, moneyflows.size());
	}

	@Test
	public void getAllMoneyflowsByDateRangeCapitalsourceId_Found() {
		final List<Moneyflow> moneyflows = this.moneyflowService.getAllMoneyflowsByDateRangeCapitalsourceId(USER_ID_1,
				LocalDate.of(2010, Month.JANUARY, 1), LocalDate.of(2010, Month.JANUARY, 30),
				new CapitalsourceID(CapitalsourceTransportBuilder.CAPITALSOURCE2_ID));
		Assert.assertNotNull(moneyflows);
		Assert.assertEquals(1, moneyflows.size());
	}

	@Test
	public void getAllMoneyflowsByDateRangeCapitalsourceId_NothingFound() {
		final List<Moneyflow> moneyflows = this.moneyflowService.getAllMoneyflowsByDateRangeCapitalsourceId(USER_ID_1,
				LocalDate.of(2011, Month.JANUARY, 1), LocalDate.of(2011, Month.JANUARY, 30),
				new CapitalsourceID(CapitalsourceTransportBuilder.CAPITALSOURCE2_ID));
		Assert.assertNotNull(moneyflows);
		Assert.assertEquals(0, moneyflows.size());
	}

	@Test
	public void getAllMonth_noMonthFound() {
		final List<Month> month = this.moneyflowService.getAllMonth(USER_ID_1, (short) 2011);
		Assert.assertNotNull(month);
		Assert.assertEquals(0, month.size());
	}

	@Test(expected = IllegalArgumentException.class)
	public void test_validateNullUser_raisesException() {
		final Moneyflow moneyflow = new Moneyflow();
		moneyflow.setGroup(new Group(new GroupID(1l)));

		this.moneyflowService.validateMoneyflow(moneyflow);
	}

	@Test(expected = IllegalArgumentException.class)
	public void test_validateNullGroup_raisesException() {
		final Moneyflow moneyflow = new Moneyflow();
		moneyflow.setUser(new User(new UserID(1l)));

		this.moneyflowService.validateMoneyflow(moneyflow);
	}

	@Test(expected = BusinessException.class)
	public void test_createWithInvalidEntity_raisesException() {
		final Moneyflow moneyflow = new Moneyflow();
		moneyflow.setUser(new User(new UserID(1l)));
		moneyflow.setGroup(new Group(new GroupID(1l)));

		this.moneyflowService.createMoneyflow(moneyflow);
	}

	@Test(expected = BusinessException.class)
	public void test_updateWithInvalidEntity_raisesException() {
		final Moneyflow moneyflow = new Moneyflow();
		moneyflow.setUser(new User(new UserID(1l)));
		moneyflow.setGroup(new Group(new GroupID(1l)));

		this.moneyflowService.updateMoneyflow(moneyflow);
	}

	@Test
	public void test_userAeditsMoneyflow_userBsameGroupSeesCachedChange() {
		final UserID user1ID = new UserID(UserTransportBuilder.USER1_ID);
		final UserID user2ID = new UserID(UserTransportBuilder.USER2_ID);

		// this caches
		Moneyflow moneyflow = this.moneyflowService.getMoneyflowById(user2ID,
				new MoneyflowID(MoneyflowTransportBuilder.MONEYFLOW1_ID));

		moneyflow = this.moneyflowService.getMoneyflowById(user1ID,
				new MoneyflowID(MoneyflowTransportBuilder.MONEYFLOW1_ID));

		final String comment = String.valueOf(System.currentTimeMillis());

		moneyflow.getUser().setId(user1ID);
		moneyflow.setComment(comment);

		// this should also modify the cache of user 1!
		this.moneyflowService.updateMoneyflow(moneyflow);

		// this should now retrieve the changed cache entry!
		moneyflow = this.moneyflowService.getMoneyflowById(user2ID,
				new MoneyflowID(MoneyflowTransportBuilder.MONEYFLOW1_ID));

		Assert.assertEquals(comment, moneyflow.getComment());
	}

	@Test
	public void test_userAaddsAMoneyflow_userBsameGroupSeessItTooBecauseCacheWasReset() {
		final UserID user1ID = new UserID(UserTransportBuilder.USER1_ID);
		final UserID user2ID = new UserID(UserTransportBuilder.USER2_ID);

		// this caches
		final List<Short> allYears1 = this.moneyflowService.getAllYears(user1ID);

		final Moneyflow moneyflow = this.moneyflowService.getMoneyflowById(user2ID,
				new MoneyflowID(MoneyflowTransportBuilder.MONEYFLOW1_ID));

		moneyflow.getUser().setId(user2ID);
		moneyflow.setBookingDate(LocalDate.now());

		// this should also modify the cache of user 1!
		this.moneyflowService.createMoneyflow(moneyflow);

		final List<Short> allYears2 = this.moneyflowService.getAllYears(user1ID);

		// Cache of user1 should have been invalidated and the added Moneyflow should be now
		// in the List of all years.
		Assert.assertNotEquals(allYears1.size(), allYears2.size());
	}

}
