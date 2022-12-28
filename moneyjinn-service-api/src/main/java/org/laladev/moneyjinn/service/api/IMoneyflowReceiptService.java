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

package org.laladev.moneyjinn.service.api;

import java.util.List;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.moneyflow.MoneyflowID;
import org.laladev.moneyjinn.model.moneyflow.MoneyflowReceipt;

/**
 * <p>
 * MoneyflowReceiptService is the Service handling everything around an {@link MoneyflowReceipt}.
 * </p>
 *
 * <p>
 * MoneyflowReceiptService is the Service handling operations around an {@link MoneyflowReceipt}
 * like getting, creating, updating, deleting. Before a {@link MoneyflowReceipt} is created or
 * updated, the {@link MoneyflowReceipt} is validated for correctness.
 * </p>
 * <p>
 * The main datasource is the Table <code>moneyflowreceipts</code>.
 * </p>
 *
 * @author Oliver Lehmann
 * @since 0.0.1
 *
 */
public interface IMoneyflowReceiptService {
  /**
   * This service retrieve a {@link MoneyflowReceipt} for a given {@link MoneyflowID} from the
   * system.
   *
   * @param userId
   *                      {@link UserID}
   * @param moneyflowId
   *                      The {@link MoneyflowID} of the {@link MoneyflowReceipt}
   */
  MoneyflowReceipt getMoneyflowReceipt(UserID userId, MoneyflowID moneyflowId);

  /**
   * Returns a list of {@link MoneyflowID}s which have a Receipt in the DB.
   *
   * @param userId
   *                 {@link UserID}
   * @return
   */
  List<MoneyflowID> getMoneyflowIdsWithReceipt(UserID userId, List<MoneyflowID> moneyflowIds);

  /**
   * Deletes the {@link MoneyflowReceipt}.
   *
   * @param userId
   *                      {@link UserID}
   * @param moneyflowId
   *                      {@link MoneyflowReceipt}
   * @return
   */
  void deleteMoneyflowReceipt(UserID userId, MoneyflowID moneyflowId);

  void createMoneyflowReceipt(UserID userId, MoneyflowReceipt moneyflowReceipt);
}
