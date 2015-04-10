package org.laladev.hbci.collector;

import java.util.ArrayList;
import java.util.List;

import org.kapott.hbci.GV.HBCIJob;
import org.kapott.hbci.GV_Result.GVRKUms;
import org.kapott.hbci.GV_Result.GVRKUms.UmsLine;
import org.kapott.hbci.manager.HBCIHandler;
import org.kapott.hbci.status.HBCIExecStatus;
import org.kapott.hbci.structures.Konto;
import org.laladev.hbci.entity.AccountMovement;
import org.laladev.hbci.entity.mapper.AccountMovementMapper;

public class AccountMovementCollector {
	public List<AccountMovement> collect(final HBCIHandler hbciHandler, final Konto account) {
		final AccountMovementMapper accountMovementMapper = new AccountMovementMapper();
		final List<AccountMovement> accountMovements = new ArrayList<AccountMovement>();

		final HBCIJob hbciJob = hbciHandler.newJob("KUmsAll");
		hbciJob.setParam("my", account);
		hbciJob.addToQueue();

		final HBCIExecStatus ret = hbciHandler.execute();
		final GVRKUms result = (GVRKUms) hbciJob.getJobResult();

		if (result.isOK()) {

			for (final UmsLine entry : result.getFlatData()) {
				accountMovements.add(accountMovementMapper.map(entry, account));
			}
		} else {
			System.out.println("Job-Error");
			System.out.println(result.getJobStatus().getErrorString());
			System.out.println("Global Error");
			System.out.println(ret.getErrorString());
		}
		return accountMovements;
	}
}
