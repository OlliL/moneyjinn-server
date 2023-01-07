//
// Copyright (c) 2017-2023 Oliver Lehmann <lehmann@ans-netz.de>
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

package org.laladev.moneyjinn.service.dao.data.mapper;

import org.laladev.moneyjinn.core.mapper.IMapper;
import org.laladev.moneyjinn.model.moneyflow.MoneyflowID;
import org.laladev.moneyjinn.model.moneyflow.MoneyflowReceipt;
import org.laladev.moneyjinn.model.moneyflow.MoneyflowReceiptID;
import org.laladev.moneyjinn.service.dao.data.MoneyflowReceiptData;

public class MoneyflowReceiptDataMapper implements IMapper<MoneyflowReceipt, MoneyflowReceiptData> {
  @Override
  public MoneyflowReceipt mapBToA(final MoneyflowReceiptData moneyflowReceiptData) {
    final MoneyflowReceipt moneyflowReceipt = new MoneyflowReceipt();
    moneyflowReceipt.setId(new MoneyflowReceiptID(moneyflowReceiptData.getId()));
    moneyflowReceipt.setMoneyflowId(new MoneyflowID(moneyflowReceiptData.getMmfMoneyflowId()));
    moneyflowReceipt.setReceipt(moneyflowReceiptData.getReceipt());
    moneyflowReceipt.setMoneyflowReceiptType(
        MoneyflowReceiptTypeMapper.map(moneyflowReceiptData.getReceiptType()));
    return moneyflowReceipt;
  }

  @Override
  public MoneyflowReceiptData mapAToB(final MoneyflowReceipt moneyflowReceipt) {
    final MoneyflowReceiptData moneyflowReceiptData = new MoneyflowReceiptData();
    // might be null for new MoneyflowReceipts
    if (moneyflowReceipt.getId() != null) {
      moneyflowReceiptData.setId(moneyflowReceipt.getId().getId());
    }
    moneyflowReceiptData.setMmfMoneyflowId(moneyflowReceipt.getMoneyflowId().getId());
    moneyflowReceiptData.setReceipt(moneyflowReceipt.getReceipt());
    moneyflowReceiptData
        .setReceiptType(MoneyflowReceiptTypeMapper.map(moneyflowReceipt.getMoneyflowReceiptType()));
    return moneyflowReceiptData;
  }
}
