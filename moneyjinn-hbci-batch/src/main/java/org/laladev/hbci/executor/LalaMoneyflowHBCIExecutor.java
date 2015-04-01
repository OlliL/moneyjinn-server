//
// Copyright (c) 2014-2015 Oliver Lehmann <oliver@laladev.org>
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
// $Id: LalaMoneyflowHBCIExecutor.java,v 1.13 2015/03/31 19:52:17 olivleh1 Exp $
//
package org.laladev.hbci.executor;

import java.util.Calendar;
import java.util.Observable;

import org.hibernate.Criteria;
import org.hibernate.StatelessSession;
import org.hibernate.criterion.Property;
import org.hibernate.exception.ConstraintViolationException;
import org.kapott.hbci.GV.HBCIJob;
import org.kapott.hbci.GV_Result.GVRKUms;
import org.kapott.hbci.GV_Result.GVRKUms.UmsLine;
import org.kapott.hbci.GV_Result.GVRSaldoReq;
import org.kapott.hbci.manager.HBCIHandler;
import org.kapott.hbci.passport.HBCIPassport;
import org.kapott.hbci.status.HBCIExecStatus;
import org.kapott.hbci.structures.Konto;
import org.laladev.hbci.entity.MonthlyBalance;
import org.laladev.hbci.entity.Transaction;
import org.laladev.hbci.entity.mapper.TransactionMapper;

public class LalaMoneyflowHBCIExecutor extends Observable {
	private final StatelessSession session;
	private final TransactionMapper transactionMapper;

	public LalaMoneyflowHBCIExecutor(final StatelessSession session) {
		this.session = session;
		this.transactionMapper = new TransactionMapper();
	}

	public void execute(final HBCIPassport hbciPassport) {
		HBCIHandler hbciHandle = null;
		try {
			hbciHandle = new HBCIHandler(null, hbciPassport);
			analyzeReportOfTransactions(hbciPassport, hbciHandle);
			analyzeMonthlyBalance(hbciPassport, hbciHandle);
		} finally {
			if (hbciHandle != null) {
				hbciHandle.close();
			} else if (hbciPassport != null) {
				hbciPassport.close();
			}
		}

	}

	/**
	 * Computes the final balance for the previous month (if not already done) of the given account
	 * and stores it + notifies all Observers
	 *
	 * @param hbciPassport
	 * @param hbciHandle
	 */
	private void analyzeMonthlyBalance(final HBCIPassport hbciPassport, final HBCIHandler hbciHandle) {
		final Konto konto = hbciPassport.getAccounts()[0];

		final Calendar currentCal = Calendar.getInstance();
		final Calendar previousMonth = Calendar.getInstance();
		previousMonth.add(Calendar.MONTH, -1);

		final Criteria cr = session.createCriteria(MonthlyBalance.class);
		cr.add(Property.forName("iban").eq(konto.iban));
		cr.add(Property.forName("bic").eq(konto.bic));
		cr.add(Property.forName("year").eq(previousMonth.get(Calendar.MONTH) + 1));
		cr.add(Property.forName("month").eq(previousMonth.get(Calendar.YEAR)));
		if (cr.list().size() == 0) {
			// Balance for previous month not in DB
			HBCIJob hbciJob = hbciHandle.newJob("SaldoReq");
			hbciJob.setParam("my", konto);
			hbciJob.addToQueue();

			HBCIExecStatus ret = hbciHandle.execute();
			final GVRSaldoReq resultSaldoReq = (GVRSaldoReq) hbciJob.getJobResult();
			if (resultSaldoReq.isOK()) {
				final GVRSaldoReq.Info currentBalance = resultSaldoReq.getEntries()[0];

				hbciJob = hbciHandle.newJob("KUmsAll");
				final Calendar calendar = Calendar.getInstance();
				calendar.add(Calendar.DAY_OF_MONTH, currentCal.get(Calendar.DAY_OF_MONTH) * -1);
				hbciJob.setParam("startdate", calendar.getTime());
				hbciJob.setParam("my", konto);
				hbciJob.addToQueue();

				ret = hbciHandle.execute();
				final GVRKUms resultKUms = (GVRKUms) hbciJob.getJobResult();
				if (resultKUms.isOK()) {
					// now we have the current balance and all movements since the previous month
					// end - now we can try to determine the balance of the previous month
				}
			} else {
				System.out.println("Job-Error");
				System.out.println(resultSaldoReq.getJobStatus().getErrorString());
				System.out.println("Global Error");
				System.out.println(ret.getErrorString());
			}
		}
	}

	private void analyzeReportOfTransactions(final HBCIPassport hbciPassport, final HBCIHandler hbciHandle) {
		final Konto konto = hbciPassport.getAccounts()[0];
		final HBCIJob hbciJob = hbciHandle.newJob("KUmsAll");
		hbciJob.setParam("my", konto);
		hbciJob.addToQueue();

		final HBCIExecStatus ret = hbciHandle.execute();
		final GVRKUms result = (GVRKUms) hbciJob.getJobResult();

		if (result.isOK()) {
			for (final UmsLine entry : result.getFlatData()) {
				storeUmsLine(entry, konto);
			}
		} else {
			System.out.println("Job-Error");
			System.out.println(result.getJobStatus().getErrorString());
			System.out.println("Global Error");
			System.out.println(ret.getErrorString());
		}
	}

	private void storeUmsLine(final UmsLine entry, final Konto konto) {
		final Transaction transaction = transactionMapper.map(entry);
		transaction.setIban(konto.iban);
		transaction.setBic(konto.bic);

		// System.out.println(transaction);

		try {
			session.insert(transaction);
			setChanged();
			notifyObservers(transaction);

		} catch (final ConstraintViolationException e) {
		}
	}
}
