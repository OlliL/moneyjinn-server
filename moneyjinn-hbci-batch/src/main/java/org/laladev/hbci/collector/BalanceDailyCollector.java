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
package org.laladev.hbci.collector;

import org.kapott.hbci.GV.HBCIJob;
import org.kapott.hbci.GV_Result.GVRSaldoReq;
import org.kapott.hbci.GV_Result.GVRSaldoReq.Info;
import org.kapott.hbci.manager.HBCIHandler;
import org.kapott.hbci.status.HBCIExecStatus;
import org.kapott.hbci.structures.Konto;
import org.laladev.hbci.entity.BalanceDaily;
import org.laladev.hbci.entity.mapper.BalanceDailyMapper;

public class BalanceDailyCollector {
	public BalanceDaily collect(final HBCIHandler hbciHandler, final Konto account) {
		final BalanceDailyMapper balanceDailyMapper = new BalanceDailyMapper();
		BalanceDaily balanceDaily = null;
		final HBCIJob hbciJob = hbciHandler.newJob("SaldoReq");
		hbciJob.setParam("my", account);
		hbciJob.addToQueue();

		final HBCIExecStatus ret = hbciHandler.execute();
		final GVRSaldoReq saldoResult = (GVRSaldoReq) hbciJob.getJobResult();

		if (saldoResult.isOK()) {

			final Info balanceInfo = saldoResult.getEntries()[0];
			if (balanceInfo.ready != null && balanceInfo.ready.value != null) {
				final BalanceDaily balanceDailyTmp = balanceDailyMapper.map(balanceInfo.ready, balanceInfo.kredit,
						account);
				if (balanceDailyTmp != null && balanceDailyTmp.getMyIban() != null
						&& balanceDailyTmp.getMyBic() != null) {
					balanceDaily = balanceDailyTmp;
				}
			}

		} else {
			System.out.println("Job-Error");
			System.out.println(saldoResult.getJobStatus().getErrorString());
			System.out.println("Global Error");
			System.out.println(ret.getErrorString());
		}
		return balanceDaily;
	}

}
