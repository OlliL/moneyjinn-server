package org.laladev.hbci.entity.mapper;

import java.math.BigDecimal;
import java.util.Calendar;

import org.laladev.hbci.entity.AbstractAccountEntitiy;
import org.laladev.hbci.entity.BalanceMonthly;

public class BalanceMonthlyMapper {
	public BalanceMonthly map(final AbstractAccountEntitiy accountEntitiy, final Calendar calendar,
			final String balanceCurrency, final BigDecimal balanceValue) {
		final BalanceMonthly balanceMonthly = new BalanceMonthly();
		balanceMonthly.setMyIban(accountEntitiy.getMyIban());
		balanceMonthly.setMyBic(accountEntitiy.getMyBic());
		balanceMonthly.setMyAccountnumber(accountEntitiy.getMyAccountnumber());
		balanceMonthly.setMyBankcode(accountEntitiy.getMyBankcode());
		if (calendar != null) {
			balanceMonthly.setBalanceYear(calendar.get(Calendar.YEAR));
			balanceMonthly.setBalanceMonth(calendar.get(Calendar.MONTH));
		}
		balanceMonthly.setBalanceCurrency(balanceCurrency);
		balanceMonthly.setBalanceValue(balanceValue);
		return balanceMonthly;
	}
}
