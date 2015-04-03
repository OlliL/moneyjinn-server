package org.laladev.hbci.entity.mapper;

import java.util.Calendar;

import org.kapott.hbci.structures.Konto;
import org.kapott.hbci.structures.Saldo;
import org.laladev.hbci.entity.BalanceMonthly;

public class BalanceMonthlyMapper {
	public BalanceMonthly map(final Saldo saldo, final Calendar calendar, final Konto account) {
		final BalanceMonthly balanceMonthly = new BalanceMonthly();
		balanceMonthly.setMyIban(account.iban);
		balanceMonthly.setMyBic(account.bic);
		balanceMonthly.setMyAccountnumber(Long.valueOf(account.number));
		balanceMonthly.setMyBankcode(Integer.valueOf(account.blz));
		if (calendar != null) {
			balanceMonthly.setBalanceYear(calendar.get(Calendar.YEAR));
			balanceMonthly.setBalanceMonth(calendar.get(Calendar.MONTH));
		}
		if (saldo != null) {
			balanceMonthly.setBalanceCurrency(saldo.value.getCurr());
			balanceMonthly.setBalanceValue(saldo.value.getBigDecimalValue());
		}
		return balanceMonthly;
	}
}
