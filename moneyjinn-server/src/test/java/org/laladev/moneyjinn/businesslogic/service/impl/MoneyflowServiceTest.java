package org.laladev.moneyjinn.businesslogic.service.impl;

import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.AbstractTest;
import org.laladev.moneyjinn.model.access.Group;
import org.laladev.moneyjinn.model.access.GroupID;
import org.laladev.moneyjinn.model.access.User;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.capitalsource.Capitalsource;
import org.laladev.moneyjinn.model.capitalsource.CapitalsourceID;
import org.laladev.moneyjinn.model.exception.BusinessException;
import org.laladev.moneyjinn.model.moneyflow.Moneyflow;
import org.laladev.moneyjinn.model.moneyflow.MoneyflowID;
import org.laladev.moneyjinn.server.builder.CapitalsourceTransportBuilder;
import org.laladev.moneyjinn.server.builder.MoneyflowTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.service.api.IMoneyflowService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.util.List;

class MoneyflowServiceTest extends AbstractTest {
    private static final UserID USER_ID_1 = new UserID(UserTransportBuilder.USER1_ID);
    @Inject
    private IMoneyflowService moneyflowService;

    @Test
    void monthHasMoneyflows_true() {
        final boolean result = this.moneyflowService.monthHasMoneyflows(USER_ID_1, 2010, Month.JANUARY);
        Assertions.assertTrue(result);
    }

    @Test
    void monthHasMoneyflows_false() {
        final boolean result = this.moneyflowService.monthHasMoneyflows(USER_ID_1, 2010, Month.DECEMBER);
        Assertions.assertFalse(result);
    }

    @Test
    void getPreviousMoneyflowDate_notExisting() {
        final LocalDate date = this.moneyflowService.getPreviousMoneyflowDate(USER_ID_1,
                LocalDate.of(2008, Month.DECEMBER, 1));
        Assertions.assertNull(date);
    }

    @Test
    void getNextMoneyflowDate_notExisting() {
        final LocalDate date = this.moneyflowService.getNextMoneyflowDate(USER_ID_1, LocalDate.of(2010, Month.MAY, 31));
        Assertions.assertNull(date);
    }

    @Test
    void searchMoneyflowsByAmountDate_matchFound() {
        final List<Moneyflow> moneyflows = this.moneyflowService.searchMoneyflowsByAmountDate(USER_ID_1,
                LocalDate.of(2010, Month.MAY, 3), new BigDecimal("-5.00"), Period.ofDays(5));
        Assertions.assertNotNull(moneyflows);
        Assertions.assertEquals(2, moneyflows.size());
    }

    @Test
    void searchMoneyflowsByAmountDate_noMatchFound() {
        final List<Moneyflow> moneyflows = this.moneyflowService.searchMoneyflowsByAmountDate(USER_ID_1,
                LocalDate.of(2010, Month.MAY, 20), new BigDecimal("-5.00"), Period.ofDays(5));
        Assertions.assertNotNull(moneyflows);
        Assertions.assertEquals(0, moneyflows.size());
    }

    @Test
    void searchMoneyflowsByAmountDate_matchFoundOnlyInMonthOfBookingDateNotInTheWhole200DayPeriod() {
        final List<Moneyflow> moneyflows = this.moneyflowService.searchMoneyflowsByAmountDate(USER_ID_1,
                LocalDate.of(2010, Month.JANUARY, 3), new BigDecimal("-10.00"), Period.ofDays(200));
        Assertions.assertNotNull(moneyflows);
        Assertions.assertEquals(1, moneyflows.size());
    }

    @Test
    void getAllMoneyflowsByDateRangeCapitalsourceId_Found() {
        final List<Moneyflow> moneyflows = this.moneyflowService.getAllMoneyflowsByDateRangeCapitalsourceId(USER_ID_1,
                LocalDate.of(2010, Month.JANUARY, 1), LocalDate.of(2010, Month.JANUARY, 30),
                new CapitalsourceID(CapitalsourceTransportBuilder.CAPITALSOURCE2_ID));
        Assertions.assertNotNull(moneyflows);
        Assertions.assertEquals(1, moneyflows.size());
    }

    @Test
    void getAllMoneyflowsByDateRangeCapitalsourceId_NothingFound() {
        final List<Moneyflow> moneyflows = this.moneyflowService.getAllMoneyflowsByDateRangeCapitalsourceId(USER_ID_1,
                LocalDate.of(2011, Month.JANUARY, 1), LocalDate.of(2011, Month.JANUARY, 30),
                new CapitalsourceID(CapitalsourceTransportBuilder.CAPITALSOURCE2_ID));
        Assertions.assertNotNull(moneyflows);
        Assertions.assertEquals(0, moneyflows.size());
    }

    @Test
    void getAllMonth_noMonthFound() {
        final List<Month> month = this.moneyflowService.getAllMonth(USER_ID_1, 2011);
        Assertions.assertNotNull(month);
        Assertions.assertEquals(0, month.size());
    }

    @Test
    void test_validateNullUser_raisesException() {
        final Moneyflow moneyflow = new Moneyflow();
        moneyflow.setGroup(new Group(new GroupID(1L)));
        Assertions.assertThrows(IllegalArgumentException.class, () -> this.moneyflowService.validateMoneyflow(moneyflow));
    }

    @Test
    void test_validateNullGroup_raisesException() {
        final Moneyflow moneyflow = new Moneyflow();
        moneyflow.setUser(new User(new UserID(1L)));
        Assertions.assertThrows(IllegalArgumentException.class, () -> this.moneyflowService.validateMoneyflow(moneyflow));
    }

    @Test
    void test_createWithInvalidEntity_raisesException() {
        final Moneyflow moneyflow = new Moneyflow();
        moneyflow.setUser(new User(new UserID(1L)));
        moneyflow.setGroup(new Group(new GroupID(1L)));
        Assertions.assertThrows(BusinessException.class, () -> this.moneyflowService.createMoneyflow(moneyflow));
    }

    @Test
    void test_updateWithInvalidEntity_raisesException() {
        final Moneyflow moneyflow = new Moneyflow();
        moneyflow.setUser(new User(new UserID(1L)));
        moneyflow.setGroup(new Group(new GroupID(1L)));
        Assertions.assertThrows(BusinessException.class, () -> this.moneyflowService.updateMoneyflow(moneyflow));
    }

    @Test
    void test_userAeditsMoneyflow_userBsameGroupSeesCachedChange() {
        final UserID user1Id = new UserID(UserTransportBuilder.USER1_ID);
        final UserID user2Id = new UserID(UserTransportBuilder.USER2_ID);
        // this caches
        this.moneyflowService.getMoneyflowById(user2Id, new MoneyflowID(MoneyflowTransportBuilder.MONEYFLOW1_ID));
        Moneyflow moneyflow = this.moneyflowService.getMoneyflowById(user1Id,
                new MoneyflowID(MoneyflowTransportBuilder.MONEYFLOW1_ID));
        final String comment = String.valueOf(System.currentTimeMillis());
        moneyflow.getUser().setId(user1Id);
        moneyflow.setComment(comment);
        // this should also modify the cache of user 1!
        this.moneyflowService.updateMoneyflow(moneyflow);
        // this should now retrieve the changed cache entry!
        moneyflow = this.moneyflowService.getMoneyflowById(user2Id,
                new MoneyflowID(MoneyflowTransportBuilder.MONEYFLOW1_ID));
        Assertions.assertEquals(comment, moneyflow.getComment());
    }

    @Test
    void test_userAaddsAMoneyflow_userBsameGroupSeessItTooBecauseCacheWasReset() {
        final UserID user1Id = new UserID(UserTransportBuilder.USER1_ID);
        final UserID user2Id = new UserID(UserTransportBuilder.USER2_ID);
        // this caches
        final List<Integer> allYears1 = this.moneyflowService.getAllYears(user1Id);
        final Moneyflow moneyflow = this.moneyflowService.getMoneyflowById(user2Id,
                new MoneyflowID(MoneyflowTransportBuilder.MONEYFLOW1_ID));
        moneyflow.getUser().setId(user2Id);
        moneyflow.setBookingDate(LocalDate.now());
        moneyflow.setCapitalsource(
                new Capitalsource(new CapitalsourceID(CapitalsourceTransportBuilder.CAPITALSOURCE2_ID)));
        // this should also modify the cache of user 1!
        this.moneyflowService.createMoneyflow(moneyflow);
        final List<Integer> allYears2 = this.moneyflowService.getAllYears(user1Id);
        // Cache of user1 should have been invalidated and the added Moneyflow should be
        // now in the List of all years.
        Assertions.assertNotEquals(allYears1.size(), allYears2.size());
    }
}
