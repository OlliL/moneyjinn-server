package org.laladev.moneyjinn.hbci.core.handler;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.hbci.core.entity.AccountMovement;
import org.laladev.moneyjinn.hbci.core.entity.BalanceDaily;
import org.laladev.moneyjinn.hbci.core.entity.BalanceMonthly;

public class BalanceMonthlyTest {

	private BalanceMonthlyHandler balanceMonthlyHandler;

	@BeforeEach
	public void setupHandler() {
		this.balanceMonthlyHandler = new BalanceMonthlyHandler(null, null, null);
	}

	private AccountMovement getBaseAccountMovement(final LocalDate date, final BigDecimal amount) {
		final AccountMovement am = new AccountMovement();
		am.setMyIban("DE12345");
		am.setMyBic("123XX");
		am.setMyAccountnumber(1234L);
		am.setMyBankcode(123);
		am.setBalanceCurrency("EUR");

		am.setBalanceDate(date);
		am.setBalanceValue(amount);

		return am;
	}

	private BalanceDaily getBaseBalanceDaily(final LocalDateTime dateTime, final BigDecimal amount) {
		final BalanceDaily bd = new BalanceDaily();
		bd.setMyIban("DE12345");
		bd.setMyBic("123XX");
		bd.setMyAccountnumber(1234L);
		bd.setMyBankcode(123);
		bd.setBalanceCurrency("EUR");

		bd.setLastTransactionDate(dateTime);
		bd.setBalanceAvailableValue(amount);

		return bd;
	}

	private BalanceMonthly getBaseBalanceMonthly() {
		final BalanceMonthly bm = new BalanceMonthly();
		bm.setMyIban("DE12345");
		bm.setMyBic("123XX");
		bm.setMyAccountnumber(1234L);
		bm.setMyBankcode(123);
		bm.setBalanceCurrency("EUR");

		return bm;
	}

	List<BalanceMonthly> getExpectedBalanceMonthly(YearMonth yearMonth, final BigDecimal... amounts) {
		final List<BalanceMonthly> bms = new ArrayList<>();
		for (final BigDecimal amount : amounts) {
			final BalanceMonthly bm = this.getBaseBalanceMonthly();
			bm.setBalanceYear(yearMonth.getYear());
			bm.setBalanceMonth(yearMonth.getMonthValue());
			bm.setBalanceValue(amount);

			yearMonth = yearMonth.plusMonths(1);

			bms.add(bm);
		}

		return bms;
	}

	@Test
	public void twoMonthWithAGap_prolongTheFirstMonth() {
		final LocalDate date = LocalDate.now();
		final List<AccountMovement> ams = new ArrayList<>();

		final AccountMovement am1 = this.getBaseAccountMovement(date.minusMonths(5), BigDecimal.TEN);
		final AccountMovement am2 = this.getBaseAccountMovement(date.minusMonths(2), BigDecimal.ONE);

		ams.add(am1);
		ams.add(am2);

		final List<BalanceMonthly> bms = this.getExpectedBalanceMonthly(YearMonth.from(date.minusMonths(5)),
				am1.getBalanceValue(), am1.getBalanceValue(), am1.getBalanceValue(), am2.getBalanceValue(),
				am2.getBalanceValue());

		final List<BalanceMonthly> balanceMonthlyList = this.balanceMonthlyHandler.getBalanceMonthlyList(ams, null);

		Assertions.assertNotNull(balanceMonthlyList);
		Assertions.assertEquals(5, balanceMonthlyList.size());
		Assertions.assertEquals(bms, balanceMonthlyList);

	}

	@Test
	public void twoMonthWithAGapOneInFuture_prolongTheFirstMonthUntilThisMonth() {
		final LocalDate date = LocalDate.now();
		final List<AccountMovement> ams = new ArrayList<>();

		final AccountMovement am1 = this.getBaseAccountMovement(date.minusMonths(5), BigDecimal.TEN);
		date.plusMonths(10);
		final AccountMovement am2 = this.getBaseAccountMovement(date.plusMonths(5), BigDecimal.ONE);

		ams.add(am1);
		ams.add(am2);

		final List<BalanceMonthly> bms = this.getExpectedBalanceMonthly(YearMonth.from(date.minusMonths(5)),
				am1.getBalanceValue(), am1.getBalanceValue(), am1.getBalanceValue(), am1.getBalanceValue(),
				am1.getBalanceValue());

		final List<BalanceMonthly> balanceMonthlyList = this.balanceMonthlyHandler.getBalanceMonthlyList(ams, null);

		Assertions.assertNotNull(balanceMonthlyList);
		Assertions.assertEquals(5, balanceMonthlyList.size());
		Assertions.assertEquals(bms, balanceMonthlyList);

	}

	@Test
	public void balanceDailyLastTransactionSomeMonthAgo_BalanceMonthliesCreated() {
		final LocalDateTime threeMonthAgo = LocalDateTime.now().minusMonths(3L);
		final BalanceDaily balanceDaily = this.getBaseBalanceDaily(threeMonthAgo, BigDecimal.TEN);

		final List<BalanceMonthly> bms = this.getExpectedBalanceMonthly(YearMonth.from(threeMonthAgo),
				balanceDaily.getBalanceAvailableValue(), balanceDaily.getBalanceAvailableValue(),
				balanceDaily.getBalanceAvailableValue());

		final List<BalanceMonthly> balanceMonthlyList = this.balanceMonthlyHandler.getBalanceMonthlyList(null,
				balanceDaily);

		Assertions.assertNotNull(balanceMonthlyList);
		Assertions.assertEquals(3, balanceMonthlyList.size());
		Assertions.assertEquals(bms, balanceMonthlyList);

	}

	@Test
	public void balanceDailyLastTransactionToday_NoBalanceMonthliesCreated() {
		final LocalDateTime now = LocalDateTime.now();
		final BalanceDaily balanceDaily = this.getBaseBalanceDaily(now, BigDecimal.TEN);

		final List<BalanceMonthly> balanceMonthlyList = this.balanceMonthlyHandler.getBalanceMonthlyList(null,
				balanceDaily);

		Assertions.assertNotNull(balanceMonthlyList);
		Assertions.assertTrue(balanceMonthlyList.isEmpty());

	}

	@Test
	public void balanceDailyLastTransactionSomewhereThisMonth_NoBalanceMonthliesCreated() {
		final LocalDateTime now = YearMonth.now().atDay(1).atStartOfDay();
		final BalanceDaily balanceDaily = this.getBaseBalanceDaily(now, BigDecimal.TEN);

		final List<BalanceMonthly> balanceMonthlyList = this.balanceMonthlyHandler.getBalanceMonthlyList(null,
				balanceDaily);

		Assertions.assertNotNull(balanceMonthlyList);
		Assertions.assertTrue(balanceMonthlyList.isEmpty());

	}

}
