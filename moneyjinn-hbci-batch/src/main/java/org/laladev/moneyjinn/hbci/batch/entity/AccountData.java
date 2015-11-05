package org.laladev.moneyjinn.hbci.batch.entity;

import java.util.List;

import org.laladev.moneyjinn.hbci.core.entity.AccountMovement;
import org.laladev.moneyjinn.hbci.core.entity.BalanceDaily;

public class AccountData {
	private List<AccountMovement> accountMovements;
	private BalanceDaily balanceDaily;

	public final List<AccountMovement> getAccountMovements() {
		return this.accountMovements;
	}

	public final void setAccountMovements(final List<AccountMovement> accountMovements) {
		this.accountMovements = accountMovements;
	}

	public final BalanceDaily getBalanceDaily() {
		return this.balanceDaily;
	}

	public final void setBalanceDaily(final BalanceDaily balanceDaily) {
		this.balanceDaily = balanceDaily;
	}

}
