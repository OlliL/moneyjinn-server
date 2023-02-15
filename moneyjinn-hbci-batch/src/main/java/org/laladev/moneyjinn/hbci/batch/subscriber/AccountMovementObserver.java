//
// Copyright (c) 2014-2023 Oliver Lehmann <lehmann@ans-netz.de>
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
// $Id: AccountMovementObserver.java,v 1.7 2015/09/11 18:43:05 olivleh1 Exp $
//
package org.laladev.moneyjinn.hbci.batch.subscriber;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.laladev.moneyjinn.hbci.backend.ApiException;
import org.laladev.moneyjinn.hbci.backend.api.ImportedMoneyflowControllerApi;
import org.laladev.moneyjinn.hbci.backend.model.CreateImportedMoneyflowRequest;
import org.laladev.moneyjinn.hbci.backend.model.ImportedMoneyflowTransport;
import org.laladev.moneyjinn.hbci.backend.model.ValidationResponse;
import org.laladev.moneyjinn.hbci.core.entity.AccountMovement;

public class AccountMovementObserver implements PropertyChangeListener {

  @Override
  public void propertyChange(final PropertyChangeEvent event) {
    if (event.getNewValue() instanceof AccountMovement) {
      this.notify((AccountMovement) event.getNewValue());
    }

  }

  private void notify(final AccountMovement transaction) {
    final ImportedMoneyflowTransport transport = new ImportedMoneyflowTransport();
    transport.setAccountNumberCapitalsource(transaction.getMyIban());
    transport.setBankCodeCapitalsource(transaction.getMyBic());
    transport.setExternalid(transaction.getId().toString());
    transport.setBookingdate(transaction.getValueDate());
    if (transaction.getInvoiceTimestamp() == null) {
      transport.setInvoicedate(transaction.getBookingDate());
    } else {
      transport.setInvoicedate(transaction.getInvoiceTimestamp().toLocalDate());
    }
    transport.setName(transaction.getOtherName());
    transport
        .setAccountNumber(transaction.getOtherAccountnumber() == null ? transaction.getOtherIban()
            : transaction.getOtherAccountnumber().toString());
    transport.setBankCode(transaction.getOtherBankcode() == null ? transaction.getOtherBic()
        : transaction.getOtherBankcode().toString());
    transport.setUsage(transaction.getMovementReason());
    transport.setAmount(transaction.getMovementValue());

    if (transport.getName() == null) {
      transport.setName(" ");
    }

    if (transport.getAccountNumber() == null) {
      transport.setAccountNumber(" ");
    }

    if (transport.getBankCode() == null) {
      transport.setBankCode(" ");
    }

    final CreateImportedMoneyflowRequest request = new CreateImportedMoneyflowRequest();
    request.setImportedMoneyflowTransport(transport);

    try {
      final ValidationResponse response = new ImportedMoneyflowControllerApi()
          .createImportedMoneyflow(request);
      if (response != null) {
        if (response.getMessage() != null) {
          throw new RuntimeException("error: (" + transport.getAccountNumberCapitalsource() + "/"
              + transport.getBankCodeCapitalsource() + ") " + response.getMessage());
        } else if (response.getResult().equals(Boolean.FALSE)) {
          throw new RuntimeException(
              "error: (" + transport.getAccountNumberCapitalsource() + "/" + transport.getBankCode()
                  + ") " + response.getValidationItemTransports().get(0).getError().toString());
        }

      }
    } catch (final ApiException e) {
      throw new RuntimeException(e);
    }

  }

}
