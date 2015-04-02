package org.laladev.hbci.handler;

import org.hibernate.StatelessSession;
import org.hibernate.exception.ConstraintViolationException;
import org.kapott.hbci.GV.HBCIJob;
import org.kapott.hbci.GV_Result.GVRKUms;
import org.kapott.hbci.GV_Result.GVRKUms.UmsLine;
import org.kapott.hbci.manager.HBCIHandler;
import org.kapott.hbci.status.HBCIExecStatus;
import org.kapott.hbci.structures.Konto;
import org.laladev.hbci.entity.AccountMovement;
import org.laladev.hbci.entity.mapper.AccountMovementMapper;

public class AccountMovementHandler extends AbstractHandler {
	private final StatelessSession session;
	private final AccountMovementMapper accountMovementMapper;

	public AccountMovementHandler(final StatelessSession session) {
		this.session = session;
		this.accountMovementMapper = new AccountMovementMapper();
	}

	@Override
	public void handle(final HBCIHandler hbciHandler, final Konto account) {
		final HBCIJob hbciJob = hbciHandler.newJob("KUmsAll");
		hbciJob.setParam("my", account);
		hbciJob.addToQueue();

		final HBCIExecStatus ret = hbciHandler.execute();
		final GVRKUms result = (GVRKUms) hbciJob.getJobResult();

		if (result.isOK()) {
			for (final UmsLine entry : result.getFlatData()) {
				storeUmsLine(entry, account);
			}
		} else {
			System.out.println("Job-Error");
			System.out.println(result.getJobStatus().getErrorString());
			System.out.println("Global Error");
			System.out.println(ret.getErrorString());
		}
	}

	private void storeUmsLine(final UmsLine entry, final Konto account) {
		final AccountMovement accountMovement = accountMovementMapper.map(entry, account);

		try {
			session.insert(accountMovement);
			setChanged();
			notifyObservers(accountMovement);

		} catch (final ConstraintViolationException e) {
		}
	}
}
