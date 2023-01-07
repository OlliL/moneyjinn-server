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
import org.laladev.moneyjinn.model.access.Group;
import org.laladev.moneyjinn.model.access.GroupID;
import org.laladev.moneyjinn.model.access.User;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.moneyflow.ImportedMoneyflowReceipt;
import org.laladev.moneyjinn.model.moneyflow.ImportedMoneyflowReceiptID;
import org.laladev.moneyjinn.service.dao.data.ImportedMoneyflowReceiptData;

public class ImportedMoneyflowReceiptDataMapper
    implements IMapper<ImportedMoneyflowReceipt, ImportedMoneyflowReceiptData> {
  @Override
  public ImportedMoneyflowReceipt mapBToA(
      final ImportedMoneyflowReceiptData importedMoneyflowReceiptData) {
    final ImportedMoneyflowReceipt importedMoneyflowReceipt = new ImportedMoneyflowReceipt();
    importedMoneyflowReceipt
        .setId(new ImportedMoneyflowReceiptID(importedMoneyflowReceiptData.getId()));
    importedMoneyflowReceipt
        .setUser(new User(new UserID(importedMoneyflowReceiptData.getMacIdCreator())));
    importedMoneyflowReceipt
        .setAccess(new Group(new GroupID(importedMoneyflowReceiptData.getMacIdAccessor())));
    importedMoneyflowReceipt.setReceipt(importedMoneyflowReceiptData.getReceipt());
    importedMoneyflowReceipt.setFilename(importedMoneyflowReceiptData.getFilename());
    importedMoneyflowReceipt.setMediaType(importedMoneyflowReceiptData.getMediaType());
    return importedMoneyflowReceipt;
  }

  @Override
  public ImportedMoneyflowReceiptData mapAToB(
      final ImportedMoneyflowReceipt importedMoneyflowReceipt) {
    final ImportedMoneyflowReceiptData importedMoneyflowReceiptData = new ImportedMoneyflowReceiptData();
    // might be null for new ImportedMoneyflowReceipts
    if (importedMoneyflowReceipt.getId() != null) {
      importedMoneyflowReceiptData.setId(importedMoneyflowReceipt.getId().getId());
    }
    if (importedMoneyflowReceipt.getUser() != null) {
      importedMoneyflowReceiptData
          .setMacIdCreator(importedMoneyflowReceipt.getUser().getId().getId());
    }
    if (importedMoneyflowReceipt.getAccess() != null) {
      importedMoneyflowReceiptData
          .setMacIdAccessor(importedMoneyflowReceipt.getAccess().getId().getId());
    }
    importedMoneyflowReceiptData.setReceipt(importedMoneyflowReceipt.getReceipt());
    importedMoneyflowReceiptData.setFilename(importedMoneyflowReceipt.getFilename());
    importedMoneyflowReceiptData.setMediaType(importedMoneyflowReceipt.getMediaType());
    return importedMoneyflowReceiptData;
  }
}
