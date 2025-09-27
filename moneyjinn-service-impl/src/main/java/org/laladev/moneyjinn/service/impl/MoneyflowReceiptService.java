//
// Copyright (c) 2016-2025 Oliver Lehmann <lehmann@ans-netz.de>
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

package org.laladev.moneyjinn.service.impl;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.moneyflow.MoneyflowID;
import org.laladev.moneyjinn.model.moneyflow.MoneyflowReceipt;
import org.laladev.moneyjinn.service.api.IMoneyflowReceiptService;
import org.laladev.moneyjinn.service.dao.MoneyflowReceiptDao;
import org.laladev.moneyjinn.service.dao.data.MoneyflowReceiptData;
import org.laladev.moneyjinn.service.dao.data.mapper.MoneyflowReceiptDataMapper;

import java.util.Collections;
import java.util.List;

@Named
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class MoneyflowReceiptService extends AbstractService implements IMoneyflowReceiptService {
    private final MoneyflowReceiptDao moneyflowReceiptDao;
    private final MoneyflowReceiptDataMapper moneyflowReceiptDataMapper;

    private MoneyflowReceipt mapMoneyflowReceiptData(final MoneyflowReceiptData moneyflowReceiptData) {
        return this.moneyflowReceiptDataMapper.mapBToA(moneyflowReceiptData);
    }

    @Override
    public MoneyflowReceipt getMoneyflowReceipt(@NonNull final UserID userId, @NonNull final MoneyflowID moneyflowId) {
        final MoneyflowReceiptData moneyflowReceiptData = this.moneyflowReceiptDao
                .getMoneyflowReceipt(moneyflowId.getId());
        return this.mapMoneyflowReceiptData(moneyflowReceiptData);
    }

    @Override
    public List<MoneyflowID> getMoneyflowIdsWithReceipt(@NonNull final UserID userId,
                                                        @NonNull final List<MoneyflowID> moneyflowIds) {
        final List<Long> moneyflowIdLongs = moneyflowIds.stream().map(MoneyflowID::getId).toList();
        final List<Long> moneyflowIdsWithReceipt = this.moneyflowReceiptDao
                .getMoneyflowIdsWithReceipt(moneyflowIdLongs);
        if (moneyflowIdsWithReceipt != null) {
            return moneyflowIdsWithReceipt.stream().map(MoneyflowID::new).toList();
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public void deleteMoneyflowReceipt(@NonNull final UserID userId, @NonNull final MoneyflowID moneyflowId) {
        this.moneyflowReceiptDao.deleteMoneyflowReceipt(moneyflowId.getId());
    }

    @Override
    public void createMoneyflowReceipt(@NonNull final UserID userId, @NonNull final MoneyflowReceipt moneyflowReceipt) {
        final MoneyflowReceiptData moneyflowReceiptData = this.moneyflowReceiptDataMapper.mapAToB(moneyflowReceipt);
        this.moneyflowReceiptDao.createMoneyflowReceipt(moneyflowReceiptData);
    }
}
