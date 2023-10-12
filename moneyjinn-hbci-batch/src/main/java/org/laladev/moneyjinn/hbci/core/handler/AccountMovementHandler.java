//
// Copyright (c) 2015-2023 Oliver Lehmann <lehmann@ans-netz.de>
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.List;

import org.laladev.moneyjinn.hbci.batch.main.MoneyjinnConnectionHolder;
import org.laladev.moneyjinn.hbci.core.entity.AccountMovement;

public class AccountMovementHandler extends AbstractHandler {
	// @formatter:off
	private final static String STATEMENT =
			"   INSERT "
			+ "   INTO account_movements "
			+ "      ( creation_time "
			+ "      , my_iban "
			+ "      , my_bic "
			+ "      , my_accountnumber "
			+ "      , my_bankcode "
			+ "      , booking_date "
			+ "      , value_date "
			+ "      , invoice_timestamp "
			+ "      , other_iban "
			+ "      , other_bic "
			+ "      , other_accountnumber "
			+ "      , other_bankcode "
			+ "      , other_name "
			+ "      , charge_value "
			+ "      , charge_currency "
			+ "      , original_value "
			+ "      , original_currency "
			+ "      , movement_value "
			+ "      , movement_currency "
			+ "      , movement_reason "
			+ "      , movement_type_code "
			+ "      , movement_type_text "
			+ "      , customer_reference "
			+ "      , bank_reference "
			+ "      , cancellation "
			+ "      , additional_information "
			+ "      , additional_key "
			+ "      , prima_nota "
			+ "      , balance_date "
			+ "      , balance_value "
			+ "      , balance_currency "
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
			+ "      , ? "
			+ "      , ? "
			+ "      , ? "
			+ "      , ? "
			+ "      , ? "
			+ "      , ? "
			+ "      , ? "
			+ "      , ? "
			+ "      , ? "
			+ "      , ? "
			+ "      , ? "
			+ "      , ? "
			+ "      , ? "
			+ "      , ? "
			+ "      , ? "
			+ "      , ? "
			+ "      , ? "
			+ "      , ? "
			+ "      , ? "
			+ "      , ? "
			+ "      , ? "
			+ "      ) ";
	// @formatter:on

	private final List<AccountMovement> accountMovements;

	public AccountMovementHandler(final List<AccountMovement> accountMovements) {
		this.accountMovements = accountMovements;
	}

	@Override
	public void handle() {
		final Connection con = MoneyjinnConnectionHolder.getConnection();
		for (final AccountMovement accountMovement : this.accountMovements) {
			try (final PreparedStatement stmt = con.prepareStatement(STATEMENT, Statement.RETURN_GENERATED_KEYS)) {
				stmt.setTimestamp(1, Timestamp.valueOf(accountMovement.getCreationTime()));
				stmt.setString(2, accountMovement.getMyIban());
				stmt.setString(3, accountMovement.getMyBic());
				stmt.setLong(4, accountMovement.getMyAccountnumber());
				stmt.setInt(5, accountMovement.getMyBankcode());
				stmt.setDate(6, Date.valueOf(accountMovement.getBookingDate()));
				stmt.setDate(7, Date.valueOf(accountMovement.getValueDate()));
				stmt.setTimestamp(8, accountMovement.getInvoiceTimestamp() == null ? null
						: Timestamp.valueOf(accountMovement.getInvoiceTimestamp()));
				stmt.setString(9, accountMovement.getOtherIban());
				stmt.setString(10, accountMovement.getOtherBic());

				if (accountMovement.getOtherAccountnumber() == null)
					stmt.setNull(11, Types.BIGINT);
				else
					stmt.setLong(11, accountMovement.getOtherAccountnumber());

				if (accountMovement.getOtherBankcode() == null)
					stmt.setNull(12, Types.INTEGER);
				else
					stmt.setLong(12, accountMovement.getOtherBankcode());

				stmt.setString(13, accountMovement.getOtherName());

				if (accountMovement.getChargeValue() == null)
					stmt.setNull(14, Types.DECIMAL);
				else
					stmt.setBigDecimal(14, accountMovement.getChargeValue());

				stmt.setString(15, accountMovement.getChargeCurrency());

				if (accountMovement.getOriginalValue() == null)
					stmt.setNull(16, Types.DECIMAL);
				else
					stmt.setBigDecimal(16, accountMovement.getOriginalValue());

				stmt.setString(17, accountMovement.getOriginalCurrency());
				stmt.setBigDecimal(18, accountMovement.getMovementValue());
				stmt.setString(19, accountMovement.getMovementCurrency());
				stmt.setString(20, accountMovement.getMovementReason());
				stmt.setInt(21, accountMovement.getMovementTypeCode());
				stmt.setString(22, accountMovement.getMovementTypeText());
				stmt.setString(23, accountMovement.getCustomerReference());
				stmt.setString(24, accountMovement.getBankReference());
				stmt.setInt(25, Boolean.TRUE.equals(accountMovement.getCancellation()) ? 1 : 0);
				stmt.setString(26, accountMovement.getAdditionalInformation());

				if (accountMovement.getAdditionalKey() == null)
					stmt.setNull(27, Types.INTEGER);
				else
					stmt.setInt(27, accountMovement.getAdditionalKey());

				stmt.setString(28, accountMovement.getPrimaNota());
				stmt.setDate(29, Date.valueOf(accountMovement.getBalanceDate()));
				stmt.setBigDecimal(30, accountMovement.getBalanceValue());
				stmt.setString(31, accountMovement.getBalanceCurrency());

				final int rowCount = stmt.executeUpdate();
				if (rowCount == 1) {
					final ResultSet rs = stmt.getGeneratedKeys();
					if (rs.next()) {
						accountMovement.setId(rs.getInt(1));
					}
				}
				con.commit();

				this.notifyObservers(accountMovement);
			} catch (final SQLException e) {
				// ignore: Duplicate entry '........' for key 'account_movements.hbci_i_03'
				if (e.getErrorCode() != 1062)
					e.printStackTrace();
			}
		}
	}
}
