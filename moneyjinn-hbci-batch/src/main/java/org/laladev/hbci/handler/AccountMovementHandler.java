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
