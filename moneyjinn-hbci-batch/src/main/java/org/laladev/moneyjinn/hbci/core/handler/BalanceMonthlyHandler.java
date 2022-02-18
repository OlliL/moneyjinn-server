//
//Copyright (c) 2015-2017 Oliver Lehmann <lehmann@ans-netz.de>
//All rights reserved.
//
//Redistribution and use in source and binary forms, with or without
//modification, are permitted provided that the following conditions
//are met:
//1. Redistributions of source code must retain the above copyright
//notice, this list of conditions and the following disclaimer
//2. Redistributions in binary form must reproduce the above copyright
//notice, this list of conditions and the following disclaimer in the
//documentation and/or other materials provided with the distribution.
//
//THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS'' AND
//ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
//IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
//ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE LIABLE
//FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
//DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
//OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
//HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
//LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
//OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
//SUCH DAMAGE.
//
package org.laladev.moneyjinn.hbci.core.handler;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.laladev.moneyjinn.hbci.core.entity.AccountMovement;
import org.laladev.moneyjinn.hbci.core.entity.BalanceDaily;
import org.laladev.moneyjinn.hbci.core.entity.BalanceMonthly;
import org.laladev.moneyjinn.hbci.core.entity.mapper.BalanceMonthlyMapper;

/**
 * This Handler determines the monthly balance of all previous month. This is done by collecting all
 * movements of an account and determine the balance for each month by crawling through them. If no
 * movements could be retrived at all, it is assumed that at least the previous month had the same
 * balance as the current balance is.
 *
 * @author Oliver Lehmann
 *
 */
public class BalanceMonthlyHandler extends AbstractHandler {
	private final EntityManager entityManager;
	private final BalanceDaily balanceDaily;
	private final List<AccountMovement> accountMovements;
	private final BalanceMonthlyMapper mapper;

	public BalanceMonthlyHandler(final EntityManager entityManager, final BalanceDaily balanceDaily,
			final List<AccountMovement> accountMovements) {
		this.entityManager = entityManager;
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
			final ArrayList<AccountMovement> movementsUntilToday = accountMovements.stream()
					.filter(am -> am.getBalanceDate().isBefore(LocalDate.now()))
					.collect(Collectors.toCollection(ArrayList::new));

			// Now generate BalanceMonthly entries for each AccountMovement
			final Iterator<AccountMovement> movementsIterator = movementsUntilToday.iterator();
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
				 * If the last processed movement is not from the current month, we can assume that
				 * for the month of the last movement, the end of month balance can be written.
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
			 * some banks always send the current day as balance date. some banks send the date of
			 * the last booking. If thats the case, and we fetch the data often enough we can have
			 * luck here.
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
		final BalanceMonthly oldBalance = this.searchEntityInDB(balanceMonthly);
		if (oldBalance != null) {
			if (oldBalance.getBalanceValue().compareTo(balanceMonthly.getBalanceValue()) != 0) {
				final BalanceMonthly mergeBalanceMonthly = this.mapper.mergeBalanceMonthly(oldBalance, balanceMonthly);
				this.entityManager.merge(mergeBalanceMonthly);
				this.setChanged();
				this.notifyObservers(mergeBalanceMonthly);
			}
		} else {
			this.entityManager.persist(balanceMonthly);
			this.setChanged();
			this.notifyObservers(balanceMonthly);
		}
	}

	private BalanceMonthly searchEntityInDB(final BalanceMonthly balanceMonthly) {
		final CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		final CriteriaQuery<BalanceMonthly> query = builder.createQuery(BalanceMonthly.class);
		final Root<BalanceMonthly> root = query.from(BalanceMonthly.class);

		final List<Predicate> predicates = new ArrayList<>();
		predicates.add(builder.equal(root.get("myIban"), balanceMonthly.getMyIban()));
		predicates.add(builder.equal(root.get("myBic"), balanceMonthly.getMyBic()));
		predicates.add(builder.equal(root.get("myAccountnumber"), balanceMonthly.getMyAccountnumber()));
		predicates.add(builder.equal(root.get("myBankcode"), balanceMonthly.getMyBankcode()));
		predicates.add(builder.equal(root.get("balanceYear"), balanceMonthly.getBalanceYear()));
		predicates.add(builder.equal(root.get("balanceMonth"), balanceMonthly.getBalanceMonth()));

		query.select(root).where(predicates.toArray(new Predicate[] {}));

		final List<BalanceMonthly> results = this.entityManager.createQuery(query).getResultList();
		if (results.isEmpty()) {
			return null;
		}
		return results.iterator().next();
	}
}
