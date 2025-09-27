//
// Copyright (c) 2021-2025 Oliver Lehmann <lehmann@ans-netz.de>
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

package org.laladev.moneyjinn.service.api;

import org.laladev.moneyjinn.model.access.GroupID;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.moneyflow.ImportedMoneyflowReceipt;
import org.laladev.moneyjinn.model.moneyflow.ImportedMoneyflowReceiptID;
import org.laladev.moneyjinn.model.validation.ValidationResult;

import java.util.List;

/**
 * <p>
 * ImportedMoneyflowReceiptService is the Service handling everything around an
 * {@link ImportedMoneyflowReceipt}.
 * </p>
 *
 * <p>
 * ImportedMoneyflowReceiptService is the Service handling operations around an
 * {@link ImportedMoneyflowReceipt} like getting, creating, updating, deleting.
 * </p>
 * <p>
 * The main datasource is the Table <code>impmoneyflowreceipts</code>.
 * </p>
 *
 * @author Oliver Lehmann
 * @since 0.0.1
 */
public interface IImportedMoneyflowReceiptService {
    List<ImportedMoneyflowReceipt> getAllImportedMoneyflowReceipts(UserID userId, GroupID groupId);

    ImportedMoneyflowReceipt getImportedMoneyflowReceiptById(UserID userId, GroupID groupId,
                                                             ImportedMoneyflowReceiptID importedMoneyflowReceiptId);

    ImportedMoneyflowReceiptID createImportedMoneyflowReceipt(ImportedMoneyflowReceipt importedMoneyflowReceipt);

    void deleteImportedMoneyflowReceipt(UserID userId, GroupID groupId,
                                        ImportedMoneyflowReceiptID importedMoneyflowReceiptId);

    ValidationResult validateImportedMoneyflowReceipt(ImportedMoneyflowReceipt importedMoneyflowReceipt);
}
