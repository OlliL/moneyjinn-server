//
// Copyright (c) 2015-2024 Oliver Lehmann <lehmann@ans-netz.de>
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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.laladev.moneyjinn.hbci.batch.main.MoneyjinnConnectionHolder;
import org.laladev.moneyjinn.hbci.core.entity.AccountMovement;
import org.laladev.moneyjinn.hbci.core.entity.BalanceDaily;
import org.laladev.moneyjinn.hbci.core.entity.BalanceMonthly;
import org.laladev.moneyjinn.hbci.core.entity.mapper.BalanceMonthlyMapper;

/**
 * This Handler determines the monthly balance of all previous month. This is
 * done by collecting all movements of an account and determine the balance for
 * each month by crawling through them. If no movements could be retrived at
 * all, it is assumed that at least the previous month had the same balance as
 * the current balance is.
 *
 * @author Oliver Lehmann
 *
 */
public class BalanceMonthlyHandler extends AbstractHandler {
	// @formatter:off
	private final static String INSERT_STATEMENT =
			"   INSERT "
			+ "   INTO balance_monthly "
			+ "      ( my_iban "
			+ "      , my_bic "
			+ "      , my_accountnumber "
			+ "      , my_bankcode "
			+ "      , balance_year "
			+ "      , balance_month "
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
			+ "      ) "
			+ " ON DUPLICATE KEY UPDATE "
                       // if the balance_value is the same, trigger a Column-cannot-be-NULL error and execute
			           // no update at all - to prevent notifying any Observers
			+ "        balance_value  = IF(VALUES(balance_value) = balance_value, NULL, VALUES(balance_value)) ";

	
	private final static String SELECT_STATEMENT =
			"   SELECT id "
			+ "   FROM balance_monthly "
			+ "  WHERE my_iban          = ?"
			+ "    AND my_bic           = ?"
			+ "    AND my_accountnumber = ? "
			+ "    AND my_bankcode      = ?"
			+ "    AND balance_year     = ?"
			+ "    AND balance_month    = ?"
			+ "  LIMIT 1";	
	// @formatter:on

	private final BalanceDaily balanceDaily;
	private final List<AccountMovement> accountMovements;
	private final BalanceMonthlyMapper mapper;

	public BalanceMonthlyHandler(final BalanceDaily balanceDaily, final List<AccountMovement> accountMovements) {
		this.accountMovements = accountMovements;
		this.balanceDaily = balanceDaily;
		this.mapper = new BalanceMonthlyMapper();

	}

	@Override
	public void handle() {
		final List<BalanceMonthly> balanceMonthlies = this.getBalanceMonthlyList(this.accountMovements,
				this.balanceDaily);
		balanceMonthlies.stream().forEach(this::insertBalanceMonthly);
	}

	List<BalanceMonthly> getBalanceMonthlyList(final List<AccountMovement> accountMovements,
			final BalanceDaily balanceDaily) {
		final List<BalanceMonthly> balanceMonthlies = new ArrayList<>();

		if (accountMovements != null && accountMovements.size() > 0) {

			// First step - remove all AccountMovements which are in the future
			final List<AccountMovement> movementsUntilToday = accountMovements.stream()
					.filter(am -> am.getBalanceDate().isBefore(LocalDate.now())).toList();

			// Now remove all AccountMovements which have a balanceDate and bookingDate off
			// by a month.
			// Example: bookingDate=2022-10-27, valueDate=2023-2024-03-31, balanceDate=2022-10-27
			// (wrong year on balanceDate!)
			final List<AccountMovement> movementsWithoutInvalid = movementsUntilToday.stream().filter(
					am -> java.time.temporal.ChronoUnit.DAYS.between(am.getBalanceDate(), am.getValueDate()) < 30)
					.toList();

			// Now generate BalanceMonthly entries for each AccountMovement
			final Iterator<AccountMovement> movementsIterator = movementsWithoutInvalid.iterator();

			if (movementsIterator.hasNext()) {
				AccountMovement accountMovementPrev = movementsIterator.next();
				YearMonth yearMonthPrev = YearMonth.from(accountMovementPrev.getBalanceDate());

				while (movementsIterator.hasNext()) {
					final AccountMovement accountMovement = movementsIterator.next();
					final YearMonth yearMonth = YearMonth.from(accountMovement.getBalanceDate());

					/*
					 * The month of the current movement is different from the month of the previous
					 * movement so we can be sure that the previous movement was the last one for
					 * that month and we can save the "end of month" balance. It's also possible,
					 * that the current movement is not in the next month of the previous movement
					 * but farer away. In this case, we can assume that the "end of month" balance
					 * of the previous month is the same as for all the following month until we
					 * reach the month of the current movement. Example: the previous movement was
					 * from 2015-01-21. The current movement is from 2015-03-03 -> we save the
					 * "end of month" balance for January and February with the same value
					 */
					while (yearMonth.isAfter(yearMonthPrev)) {
						final BalanceMonthly bm = this.mapper.map(accountMovementPrev, yearMonthPrev,
								accountMovementPrev.getBalanceCurrency(), accountMovementPrev.getBalanceValue());
						balanceMonthlies.add(bm);

						yearMonthPrev = yearMonthPrev.plusMonths(1L);
					}

					accountMovementPrev = accountMovement;
					yearMonthPrev = yearMonth;
				}

				/*
				 * If the last processed movement is not from the current month, we can assume
				 * that for the month of the last movement, the end of month balance can be
				 * written.
				 */
				final YearMonth yearMonth = YearMonth.now();
				while (yearMonth.isAfter(yearMonthPrev)) {
					final BalanceMonthly bm = this.mapper.map(accountMovementPrev, yearMonthPrev,
							accountMovementPrev.getBalanceCurrency(), accountMovementPrev.getBalanceValue());
					balanceMonthlies.add(bm);

					yearMonthPrev = yearMonthPrev.plusMonths(1L);
				}
			}
		} else if (balanceDaily != null) {
			final LocalDateTime yesterdayEndOfDay = LocalDate.now().atStartOfDay().minusNanos(1L);

			/*
			 * some banks always send the current day as balance date. some banks send the
			 * date of the last booking. If thats the case, and we fetch the data often
			 * enough we can have luck here.
			 */
			if (balanceDaily.getLastTransactionDate().isBefore(yesterdayEndOfDay)) {
				final YearMonth yearMonth = YearMonth.now();
				YearMonth yearMonthBalance = YearMonth.from(balanceDaily.getLastTransactionDate());

				while (yearMonth.isAfter(yearMonthBalance)) {
					final BalanceMonthly bm = this.mapper.map(balanceDaily, yearMonthBalance,
							balanceDaily.getBalanceCurrency(), balanceDaily.getBalanceAvailableValue());
					balanceMonthlies.add(bm);

					yearMonthBalance = yearMonthBalance.plusMonths(1L);
				}
			}
		}

		return balanceMonthlies;

	}

	private void insertBalanceMonthly(final BalanceMonthly balanceMonthly) {
		final Connection con = MoneyjinnConnectionHolder.getConnection();
		try (final PreparedStatement stmt = con.prepareStatement(INSERT_STATEMENT, Statement.RETURN_GENERATED_KEYS)) {
			stmt.setString(1, balanceMonthly.getMyIban());
			stmt.setString(2, balanceMonthly.getMyBic());
			stmt.setLong(3, balanceMonthly.getMyAccountnumber());
			stmt.setInt(4, balanceMonthly.getMyBankcode());
			stmt.setInt(5, balanceMonthly.getBalanceYear());
			stmt.setInt(6, balanceMonthly.getBalanceMonth());
			stmt.setBigDecimal(7, balanceMonthly.getBalanceValue());
			stmt.setString(8, balanceMonthly.getBalanceCurrency());

			final int rowCount = stmt.executeUpdate();
			if (rowCount == 1) {
				final ResultSet rs = stmt.getGeneratedKeys();
				if (rs.next()) {
					balanceMonthly.setId(rs.getInt(1));
				}

				this.notifyObservers(balanceMonthly);
			} else if (rowCount == 2) {
				try (final PreparedStatement stmt2 = con.prepareStatement(SELECT_STATEMENT)) {
					stmt2.setString(1, balanceMonthly.getMyIban());
					stmt2.setString(2, balanceMonthly.getMyBic());
					stmt2.setLong(3, balanceMonthly.getMyAccountnumber());
					stmt2.setInt(4, balanceMonthly.getMyBankcode());
					stmt2.setInt(5, balanceMonthly.getBalanceYear());
					stmt2.setInt(6, balanceMonthly.getBalanceMonth());

					final ResultSet rs = stmt2.executeQuery();
					if (rs.next()) {
						balanceMonthly.setId(rs.getInt(1));

						this.notifyObservers(balanceMonthly);
					}
				}
			}
			con.commit();

		} catch (final SQLException e) {
			// ignore: Column 'balance_value' cannot be null
			if (e.getErrorCode() != 1048)
				e.printStackTrace();
		}
	}
}
