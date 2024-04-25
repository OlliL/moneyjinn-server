//
// Copyright (c) 2015-2024 Dennis Garske <d.garske@gmx.de>
// Copyright (c) 2017-2024 Oliver Lehmann <lehmann@ans-netz.de>
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

package org.laladev.moneyjinn.hbci.core.handler;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.laladev.moneyjinn.hbci.batch.main.MoneyjinnConnectionHolder;
import org.laladev.moneyjinn.hbci.core.entity.BalanceDaily;

public class BalanceDailyHandler extends AbstractHandler {
	// @formatter:off
	private static final String STATEMENT =
			"   INSERT "
			+ "   INTO balance_daily "
			+ "      ( my_iban "
			+ "      , my_bic "
			+ "      , my_accountnumber "
			+ "      , my_bankcode "
			+ "      , balance_date "
			+ "      , last_transaction_date "
			+ "      , balance_available_value "
			+ "      , line_of_credit_value "
			+ "      , balance_currency "
			+ "      , last_balance_update "
			+ "      ) "
			+ " VALUES "
			+ "      ( ? "
			+ "      , ? "
			+ "      , ? "
			+ "      , ? "
			+ "      , ? "
			+ "      , ? "
			+ "      , ? "
			+ "      , ? "
			+ "      , ? "
			+ "      , ? "
			+ "      ) "
			+ " ON DUPLICATE KEY UPDATE "
			+ "        balance_date            = VALUES (balance_date)"
			+ "      , last_transaction_date   = VALUES (last_transaction_date) "
			+ "      , balance_available_value = VALUES (balance_available_value) "
			+ "      , line_of_credit_value    = VALUES (line_of_credit_value) "
			+ "      , last_balance_update     = VALUES (last_balance_update) " ;
	// @formatter:on

	private final BalanceDaily balanceDaily;

	public BalanceDailyHandler(final BalanceDaily balanceDaily) {
		this.balanceDaily = balanceDaily;
	}

	@Override
	public void handle() {
		if (this.balanceDaily == null)
			return;

		final Connection con = MoneyjinnConnectionHolder.getConnection();
		try (final PreparedStatement stmt = con.prepareStatement(STATEMENT)) {
			stmt.setString(1, this.balanceDaily.getMyIban());
			stmt.setString(2, this.balanceDaily.getMyBic());
			stmt.setLong(3, this.balanceDaily.getMyAccountnumber());
			stmt.setInt(4, this.balanceDaily.getMyBankcode());
			stmt.setDate(5, Date.valueOf(this.balanceDaily.getBalanceDate()));
			stmt.setTimestamp(6, Timestamp.valueOf(this.balanceDaily.getLastTransactionDate()));
			stmt.setBigDecimal(7, this.balanceDaily.getBalanceAvailableValue());
			stmt.setBigDecimal(8, this.balanceDaily.getLineOfCreditValue());
			stmt.setString(9, this.balanceDaily.getBalanceCurrency());
			stmt.setTimestamp(10, Timestamp.valueOf(this.balanceDaily.getLastBalanceUpdate()));

			stmt.executeUpdate();
			con.commit();

			this.notifyObservers(this.balanceDaily);
		} catch (final SQLException e) {
			e.printStackTrace();
		}
	}
}
