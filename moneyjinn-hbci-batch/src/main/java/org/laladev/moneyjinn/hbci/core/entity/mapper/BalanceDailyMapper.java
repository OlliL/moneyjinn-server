package org.laladev.moneyjinn.hbci.core.entity.mapper;

import org.kapott.hbci.structures.Konto;
import org.kapott.hbci.structures.Saldo;
import org.kapott.hbci.structures.Value;
import org.laladev.moneyjinn.hbci.core.entity.BalanceDaily;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

public class BalanceDailyMapper {
	public BalanceDaily map(final Saldo balance, Value lineOfCredit,  final Konto account) {
		final BalanceDaily balanceDaily = new BalanceDaily();
		balanceDaily.setMyIban(account.iban);
		balanceDaily.setMyBic(account.bic);
		balanceDaily.setMyAccountnumber(Long.valueOf(account.number));
		balanceDaily.setMyBankcode(Integer.valueOf(account.blz));

		balanceDaily.setBalanceCurrency(account.curr);
		balanceDaily.setBalanceAvailableValue(balance.value.getBigDecimalValue());

		if (lineOfCredit != null) {
			balanceDaily.setLineOfCreditValue(lineOfCredit.getBigDecimalValue());
		} else {
			balanceDaily.setLineOfCreditValue(BigDecimal.ZERO);
		}
		balanceDaily.setBalanceDate(new Date(System.currentTimeMillis()));
		balanceDaily.setLastTransactionDate(new Timestamp(balance.timestamp.getTime()));
		return balanceDaily;
	}

	public BalanceDaily mergeSaldoResult(BalanceDaily oldSaldo, BalanceDaily newSaldo) {

		oldSaldo.setBalanceAvailableValue(newSaldo.getBalanceAvailableValue());
		oldSaldo.setLineOfCreditValue(newSaldo.getLineOfCreditValue());
		oldSaldo.setBalanceDate(newSaldo.getBalanceDate());
		oldSaldo.setLastTransactionDate(newSaldo.getLastTransactionDate());
		oldSaldo.setLastBalanceUpdate(new Timestamp(System.currentTimeMillis()));

		return oldSaldo;
	}


}
