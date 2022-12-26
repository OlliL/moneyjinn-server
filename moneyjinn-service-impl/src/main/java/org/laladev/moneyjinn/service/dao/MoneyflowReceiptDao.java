//
// Copyright (c) 2017 Oliver Lehmann <lehmann@ans-netz.de>
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

package org.laladev.moneyjinn.service.dao;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.List;
import org.laladev.moneyjinn.service.dao.data.MoneyflowReceiptData;
import org.laladev.moneyjinn.service.dao.mapper.IMoneyflowReceiptDaoMapper;

@Named
public class MoneyflowReceiptDao {
  @Inject
  private IMoneyflowReceiptDaoMapper mapper;

  public MoneyflowReceiptData getMoneyflowReceipt(final Long moneyflowId) {
    return this.mapper.getMoneyflowReceipt(moneyflowId);
  }

  public List<Long> getMoneyflowIdsWithReceipt(final List<Long> moneyflowIds) {
    return this.mapper.getMoneyflowIdsWithReceipt(moneyflowIds);
  }

  public void deleteMoneyflowReceipt(final Long moneyflowId) {
    this.mapper.deleteMoneyflowReceipt(moneyflowId);
  }

  public void createMoneyflowReceipt(final MoneyflowReceiptData moneyflowReceiptData) {
    this.mapper.createMoneyflowReceipt(moneyflowReceiptData);
  }
}
