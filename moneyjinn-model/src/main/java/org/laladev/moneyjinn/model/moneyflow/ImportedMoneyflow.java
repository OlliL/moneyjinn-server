//
// Copyright (c) 2015-2023 Oliver Lehmann <lehmann@ans-netz.de>
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions
// are met:
// 1. Redistributions of source code must retain the above copyright
// notice, this list of conditions and the following disclaimer
// 2. Redistributions in binary form must reproduce the above copyright
// notice, this list of conditions and the following disclaimer in the
// documentation and/or other materials provided with the distribution.
//
// THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS'' AND
// ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE LIABLE
// FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
// DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
// OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
// HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
// LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
// OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE.
//

package org.laladev.moneyjinn.model.moneyflow;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.laladev.moneyjinn.model.BankAccount;
import org.laladev.moneyjinn.model.IHasBankAccount;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ImportedMoneyflow extends AbstractMoneyflow<ImportedMoneyflowID>
    implements IHasBankAccount {
  private static final long serialVersionUID = 1L;
  private String externalId;
  private String name;
  private BankAccount bankAccount;
  private String usage;
  private ImportedMoneyflowStatus status;

  public Moneyflow getMoneyflow() {
    final Moneyflow moneyflow = new Moneyflow();
    moneyflow.setUser(super.getUser());
    moneyflow.setGroup(super.getGroup());
    moneyflow.setBookingDate(super.getBookingDate());
    moneyflow.setInvoiceDate(super.getInvoiceDate());
    moneyflow.setAmount(super.getAmount());
    moneyflow.setCapitalsource(super.getCapitalsource());
    moneyflow.setContractpartner(super.getContractpartner());
    moneyflow.setComment(super.getComment());
    moneyflow.setPrivat(super.isPrivat());
    moneyflow.setPostingAccount(super.getPostingAccount());
    return moneyflow;
  }

}
