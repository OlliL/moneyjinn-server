//
//Copyright (c) 2015 Dennis Garske <d.garske@gmx.de>
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

import org.hibernate.Query;
import org.hibernate.StatelessSession;
import org.hibernate.exception.ConstraintViolationException;
import org.laladev.moneyjinn.hbci.core.entity.BalanceDaily;
import org.laladev.moneyjinn.hbci.core.entity.mapper.BalanceDailyMapper;

/**
 * @author Dennis Garske
 */
public class BalanceDailyHandler extends AbstractHandler {
	private final StatelessSession session;
	private final BalanceDaily balanceDaily;

	public BalanceDailyHandler(final StatelessSession session, final BalanceDaily balanceDaily) {
		this.session = session;
		this.balanceDaily = balanceDaily;
	}

	@Override
	public void handle() {
		final BalanceDailyMapper balanceDailyMapper = new BalanceDailyMapper();
		if (this.balanceDaily != null) {
			try {
				session.insert(balanceDaily);
			} catch (final ConstraintViolationException e) {
				// here is already a balance value for today in the database, update it
				final BalanceDaily oldBalance = getBalanceFromDB(balanceDaily);
				if (oldBalance != null) {
					session.update(balanceDailyMapper.mergeSaldoResult(oldBalance, balanceDaily));
				} else {
					throw e;
				}
			}
			setChanged();
			notifyObservers(balanceDaily);
		}
	}

	private BalanceDaily getBalanceFromDB(final BalanceDaily currentBalance) {
		final Query query = session.getNamedQuery("findDailyBalance").setString("myIban", currentBalance.getMyIban())
				.setString("myBic", currentBalance.getMyBic())
				.setLong("myAccountnumber", currentBalance.getMyAccountnumber())
				.setLong("myBankcode", currentBalance.getMyBankcode())
				.setDate("balanceDate", currentBalance.getBalanceDate());

		return (BalanceDaily) query.uniqueResult();
	}
}
