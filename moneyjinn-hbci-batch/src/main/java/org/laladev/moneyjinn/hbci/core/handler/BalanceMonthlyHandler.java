//
//Copyright (c) 2015-2016 Oliver Lehmann <oliver@laladev.org>
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
		if (this.balanceDaily != null) {
			final BalanceMonthlyMapper balanceMonthlyMapper = new BalanceMonthlyMapper();
			BalanceMonthly balanceMonthly = null;

			if (this.accountMovements != null && this.accountMovements.size() > 0) {
				final Calendar calendar = Calendar.getInstance();
				for (final AccountMovement accountMovement : this.accountMovements) {
					calendar.setTime(accountMovement.getBalanceDate());

					if (balanceMonthly != null) {
						if (balanceMonthly.getBalanceYear().compareTo(calendar.get(Calendar.YEAR)) < 0
								|| (balanceMonthly.getBalanceYear().equals(calendar.get(Calendar.YEAR))
										&& balanceMonthly.getBalanceMonth()
												.compareTo(calendar.get(Calendar.MONTH)) < 0)) {
							/*
							 * The month of the current movement is different from the month of the
							 * previous movement so we can be sure that the previous movement was
							 * the last one for that month and we can save the "end of month"
							 * balance
							 */
							this.insertBalanceMonthly(balanceMonthly);
							/*
							 * it's possible, that the current movement is not in the next month of
							 * the previous movement but farer away. In this case, we can assume
							 * that the "end of month" balance of the previous month is the same as
							 * for all the following month until we reach the month of the current
							 * movement. Example: the previous movement was from 2015-01-21. The
							 * current movement is from 2015-03-03 -> we save the "end of month"
							 * balance for January (already done above) and February with the same
							 * value
							 */
							this.prolongBalance(balanceMonthly, calendar);
						}
					}
					balanceMonthly = balanceMonthlyMapper.map(accountMovement, calendar,
							accountMovement.getBalanceCurrency(), accountMovement.getBalanceValue());
				}

				if (balanceMonthly != null) {
					final Calendar currentCalendar = Calendar.getInstance();
					/*
					 * If the last processed movement is not from the current month, we can assume
					 * that for the month of the last movement, the end of month balance can be
					 * written.
					 */
					if (balanceMonthly.getBalanceYear().compareTo(currentCalendar.get(Calendar.YEAR)) < 0
							|| (balanceMonthly.getBalanceYear().equals(currentCalendar.get(Calendar.YEAR))
									&& balanceMonthly.getBalanceMonth()
											.compareTo(currentCalendar.get(Calendar.MONTH)) < 0)) {
						this.insertBalanceMonthly(balanceMonthly);
						/*
						 * now also write the balance of the last movement for all following month
						 * until the current month is reached
						 */
						this.prolongBalance(balanceMonthly, currentCalendar);
					}
				}
			} else {
				final Calendar previousCalendar = Calendar.getInstance();
				previousCalendar.add(Calendar.DAY_OF_MONTH, previousCalendar.get(Calendar.DAY_OF_MONTH) * -1);
				previousCalendar.set(Calendar.HOUR_OF_DAY, 23);
				previousCalendar.set(Calendar.MINUTE, 59);
				previousCalendar.set(Calendar.SECOND, 59);
				previousCalendar.set(Calendar.MILLISECOND, 999);

				final Calendar readyCalendar = Calendar.getInstance();
				readyCalendar.setTime(this.balanceDaily.getLastTransactionDate());

				/*
				 * some banks always send the current day as balance date. some banks send the date
				 * of the last booking. If thats the case, and we fetch the data often enough we can
				 * have luck here.
				 */
				if (readyCalendar.compareTo(previousCalendar) < 0) {
					// last date of booking
					balanceMonthly = balanceMonthlyMapper.map(this.balanceDaily, readyCalendar,
							this.balanceDaily.getBalanceCurrency(), this.balanceDaily.getBalanceAvailableValue());
					this.insertBalanceMonthly(balanceMonthly);
					final Calendar currentCalendar = Calendar.getInstance();
					this.prolongBalance(balanceMonthly, currentCalendar);
				}
			}
		}
	}

	private void prolongBalance(final BalanceMonthly balanceMonthly, final Calendar calendar) {
		while (!balanceMonthly.getBalanceYear().equals(calendar.get(Calendar.YEAR))
				|| !balanceMonthly.getBalanceMonth().equals(calendar.get(Calendar.MONTH))) {
			balanceMonthly.setBalanceMonth(balanceMonthly.getBalanceMonth() + 1);

			// did the year change?
			if (balanceMonthly.getBalanceMonth() > Calendar.DECEMBER) {
				balanceMonthly.setBalanceYear(balanceMonthly.getBalanceYear() + 1);
				balanceMonthly.setBalanceMonth(Calendar.JANUARY);
			}

			// we don't save the balance if we reached the same month as our current
			// movement is.
			if (balanceMonthly.getBalanceYear().compareTo(calendar.get(Calendar.YEAR)) < 0
					|| (balanceMonthly.getBalanceYear().equals(calendar.get(Calendar.YEAR))
							&& balanceMonthly.getBalanceMonth().compareTo(calendar.get(Calendar.MONTH)) < 0)) {
				this.insertBalanceMonthly(balanceMonthly);
			} else {
				break;
			}
		}
	}

	private void insertBalanceMonthly(final BalanceMonthly balanceMonthly) {
		try {
			balanceMonthly.setBalanceMonth(balanceMonthly.getBalanceMonth() + 1);

			final BalanceMonthly oldBalance = this.searchEntityInDB(balanceMonthly);
			if (oldBalance != null) {
				if (oldBalance.getBalanceValue().compareTo(balanceMonthly.getBalanceValue()) != 0) {
					this.entityManager.merge(this.mapper.mergeBalanceMonthly(oldBalance, balanceMonthly));
					this.setChanged();
					this.notifyObservers(balanceMonthly);
				}
			} else {
				this.entityManager.persist(balanceMonthly);
				this.setChanged();
				this.notifyObservers(balanceMonthly);
			}

		} finally {
			balanceMonthly.setBalanceMonth(balanceMonthly.getBalanceMonth() - 1);
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
		System.out.println(balanceMonthly);
		System.out.println(query.getParameters());
		final List<BalanceMonthly> results = this.entityManager.createQuery(query).getResultList();
		if (results.isEmpty()) {
			return null;
		}
		return results.iterator().next();
	}
}
