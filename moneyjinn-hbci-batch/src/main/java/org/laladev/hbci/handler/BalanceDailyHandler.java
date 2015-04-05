//
//Copyright (c) 2015 Oliver Lehmann <oliver@laladev.org>
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
package org.laladev.hbci.handler;

import org.hibernate.Query;
import org.hibernate.StatelessSession;
import org.hibernate.exception.ConstraintViolationException;
import org.kapott.hbci.GV.HBCIJob;
import org.kapott.hbci.GV_Result.GVRSaldoReq;
import org.kapott.hbci.GV_Result.GVRSaldoReq.Info;
import org.kapott.hbci.manager.HBCIHandler;
import org.kapott.hbci.status.HBCIExecStatus;
import org.kapott.hbci.structures.Konto;
import org.laladev.hbci.entity.BalanceDaily;
import org.laladev.hbci.entity.BalanceMonthly;
import org.laladev.hbci.entity.mapper.BalanceDailyMapper;

import java.util.List;

/**
 * This Handler determines the monthly balance of all previous month. This is done by collecting all
 * movements of an account and determine the balance for each month by crawling through them. If no
 * movements could be retrived at all, it is assumed that at least the previous month had the same
 * balance as the current balance is.
 *
 * @author Oliver Lehmann
 */
public class BalanceDailyHandler extends AbstractHandler {
    private final StatelessSession session;
    private final BalanceDailyMapper balanceDailyMapper;

    public BalanceDailyHandler(final StatelessSession session) {
        this.session = session;
        this.balanceDailyMapper = new BalanceDailyMapper();
    }

    @Override
    public void handle(final HBCIHandler hbciHandler, final Konto account) {
        HBCIJob hbciJob = hbciHandler.newJob("SaldoReq");
        hbciJob.setParam("my", account);
        hbciJob.addToQueue();

        HBCIExecStatus ret = hbciHandler.execute();
        final GVRSaldoReq saldoResult = (GVRSaldoReq) hbciJob.getJobResult();

        if (saldoResult.isOK()) {

            final Info balanceInfo = saldoResult.getEntries()[0];
            if (balanceInfo.ready != null && balanceInfo.ready.value != null) {
                final BalanceDaily balanceDaily = balanceDailyMapper.map(balanceInfo.ready, balanceInfo.kredit, account);
                insertBalanceDaily(balanceDaily);
            }

        } else {
            System.out.println("Job-Error");
            System.out.println(saldoResult.getJobStatus().getErrorString());
            System.out.println("Global Error");
            System.out.println(ret.getErrorString());
        }
    }


    private void insertBalanceDaily(BalanceDaily currentBalance) {
        try {
            session.insert(currentBalance);
        } catch (final ConstraintViolationException e) {
            //here is already a balance value for today in the database, update it
            BalanceDaily oldBalance = getBalanceFromDB(currentBalance);
            currentBalance = balanceDailyMapper.mergeSaldoResult(oldBalance, currentBalance);
            session.update(currentBalance);
        }
        setChanged();
        notifyObservers(currentBalance);
    }

    private BalanceDaily getBalanceFromDB(BalanceDaily currentBalance) {
        Query query = session.getNamedQuery("findDailyBalance")
                .setString("myIban", currentBalance.getMyIban())
                .setString("myBic", currentBalance.getMyBic())
                .setLong("myAccountnumber", currentBalance.getMyAccountnumber())
                .setLong("myBankcode", currentBalance.getMyBankcode())
                .setDate("balanceDate", currentBalance.getBalanceDate());

        return (BalanceDaily) query.uniqueResult();
    }
}
