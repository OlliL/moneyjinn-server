//
//Copyright (c) 2015 Oliver Lehmann <lehmann@ans-netz.de>
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
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.laladev.moneyjinn.hbci.core.entity.AccountMovement;

public class AccountMovementHandler extends AbstractHandler {
	private final EntityManager entityManager;
	private final List<AccountMovement> accountMovements;

	public AccountMovementHandler(final EntityManager entityManager, final List<AccountMovement> accountMovements) {
		this.entityManager = entityManager;
		this.accountMovements = accountMovements;
	}

	@Override
	public void handle() {

		final CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		final CriteriaQuery<AccountMovement> query = builder.createQuery(AccountMovement.class);
		final Root<AccountMovement> root = query.from(AccountMovement.class);

		final List<Predicate> predicates = new ArrayList<>();

		for (final AccountMovement accountMovement : this.accountMovements) {
			predicates.clear();

			predicates.add(builder.equal(root.get("myIban"), accountMovement.getMyIban()));
			predicates.add(builder.equal(root.get("myBic"), accountMovement.getMyBic()));
			predicates.add(builder.equal(root.get("myAccountnumber"), accountMovement.getMyAccountnumber()));
			predicates.add(builder.equal(root.get("myBankcode"), accountMovement.getMyBankcode()));
			predicates.add(builder.equal(root.get("bookingDate"), accountMovement.getBookingDate()));
			predicates.add(builder.equal(root.get("valueDate"), accountMovement.getValueDate()));
			predicates.add(builder.equal(root.get("movementValue"), accountMovement.getMovementValue()));
			predicates.add(builder.equal(root.get("movementCurrency"), accountMovement.getMovementCurrency()));
			predicates.add(builder.equal(root.get("movementTypeCode"), accountMovement.getMovementTypeCode()));
			predicates.add(builder.equal(root.get("customerReference"), accountMovement.getCustomerReference()));
			predicates.add(builder.equal(root.get("cancellation"), accountMovement.getCancellation()));
			predicates.add(builder.equal(root.get("balanceDate"), accountMovement.getBalanceDate()));
			predicates.add(builder.equal(root.get("balanceValue"), accountMovement.getBalanceValue()));
			predicates.add(builder.equal(root.get("balanceCurrency"), accountMovement.getBalanceCurrency()));

			query.select(root).where(predicates.toArray(new Predicate[] {}));

			final List<AccountMovement> results = this.entityManager.createQuery(query).getResultList();
			if (results.isEmpty()) {
				this.entityManager.persist(accountMovement);
				this.setChanged();
				this.notifyObservers(accountMovement);
			}
		}
	}
}
