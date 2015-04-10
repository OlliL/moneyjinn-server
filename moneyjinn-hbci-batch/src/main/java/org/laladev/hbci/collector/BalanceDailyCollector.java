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
