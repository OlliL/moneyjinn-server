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

import java.util.List;

import org.hibernate.StatelessSession;
import org.hibernate.exception.ConstraintViolationException;
import org.laladev.hbci.entity.AccountMovement;

public class AccountMovementHandler extends AbstractHandler {
	private final StatelessSession session;
	private final List<AccountMovement> accountMovements;

	public AccountMovementHandler(final StatelessSession session, final List<AccountMovement> accountMovements) {
		this.session = session;
		this.accountMovements = accountMovements;
	}

	@Override
	public void handle() {
		for (final AccountMovement accountMovement : this.accountMovements) {
			try {
				session.insert(accountMovement);
				setChanged();
				notifyObservers(accountMovement);

			} catch (final ConstraintViolationException e) {
			}

		}
	}
}
