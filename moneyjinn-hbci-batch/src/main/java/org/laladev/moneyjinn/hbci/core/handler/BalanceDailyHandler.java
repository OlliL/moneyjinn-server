//
//Copyright (c) 2015 Dennis Garske <d.garske@gmx.de>
//Copyright (c) 2017 Oliver Lehmann <lehmann@ans-netz.de>
//
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
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.laladev.moneyjinn.hbci.core.entity.BalanceDaily;
import org.laladev.moneyjinn.hbci.core.entity.mapper.BalanceDailyMapper;

public class BalanceDailyHandler extends AbstractHandler {
  private final EntityManager entityManager;
  private final BalanceDaily balanceDaily;

  public BalanceDailyHandler(final EntityManager entityManager, final BalanceDaily balanceDaily) {
    this.entityManager = entityManager;
    this.balanceDaily = balanceDaily;
  }

  @Override
  public void handle() {
    final BalanceDailyMapper balanceDailyMapper = new BalanceDailyMapper();
    if (this.balanceDaily != null) {
      final BalanceDaily oldBalance = this.searchEntityInDB(this.balanceDaily);
      if (oldBalance != null) {
        this.entityManager
            .merge(balanceDailyMapper.mergeBalanceDaily(oldBalance, this.balanceDaily));
      } else {
        this.entityManager.persist(this.balanceDaily);
      }
      this.notifyObservers(this.balanceDaily);
    }
  }

  private BalanceDaily searchEntityInDB(final BalanceDaily balanceDaily) {
    final CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
    final CriteriaQuery<BalanceDaily> query = builder.createQuery(BalanceDaily.class);
    final Root<BalanceDaily> root = query.from(BalanceDaily.class);

    final List<Predicate> predicates = new ArrayList<>();
    predicates.add(builder.equal(root.get("myIban"), balanceDaily.getMyIban()));
    predicates.add(builder.equal(root.get("myBic"), balanceDaily.getMyBic()));
    predicates.add(builder.equal(root.get("myAccountnumber"), balanceDaily.getMyAccountnumber()));
    predicates.add(builder.equal(root.get("myBankcode"), balanceDaily.getMyBankcode()));
    predicates.add(builder.equal(root.get("balanceDate"), balanceDaily.getBalanceDate()));

    query.select(root).where(predicates.toArray(new Predicate[] {}));

    final List<BalanceDaily> results = this.entityManager.createQuery(query).getResultList();
    if (results.isEmpty()) {
      return null;
    }
    return results.iterator().next();
  }
}
