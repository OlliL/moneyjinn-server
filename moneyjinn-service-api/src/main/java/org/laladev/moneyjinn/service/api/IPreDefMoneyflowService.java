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

package org.laladev.moneyjinn.service.api;

import java.util.List;
import org.laladev.moneyjinn.model.PreDefMoneyflow;
import org.laladev.moneyjinn.model.PreDefMoneyflowID;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.exception.BusinessException;
import org.laladev.moneyjinn.model.validation.ValidationResult;

/**
 * <p>
 * PreDefMoneyflowService is the Service handling everything around an {@link PreDefMoneyflow}.
 * </p>
 *
 * <p>
 * PreDefMoneyflowService is the Service handling operations around an {@link PreDefMoneyflow} like
 * getting, creating, updating, deleting. Before a {@link PreDefMoneyflow} is created or updated,
 * the {@link PreDefMoneyflow} is validated for correctness.
 * </p>
 * <p>
 * The main datasource is the Table <code>predefmoneyflows</code>.
 * </p>
 *
 * @author Oliver Lehmann
 * @since 0.0.1
 *
 */
public interface IPreDefMoneyflowService {
  /**
   * Checks the validity of the given {@link PreDefMoneyflow}.
   *
   * @param preDefMoneyflow
   *                          the {@link PreDefMoneyflow}
   * @return {@link ValidationResult}
   */
  ValidationResult validatePreDefMoneyflow(PreDefMoneyflow preDefMoneyflow);

  /**
   * Returns the {@link PreDefMoneyflow} for the given {@link PreDefMoneyflowID}.
   *
   * @param userId
   *                            {@link UserID}
   * @param preDefMoneyflowId
   *                            the {@link PreDefMoneyflowID}
   * @return
   */
  PreDefMoneyflow getPreDefMoneyflowById(UserID userId, PreDefMoneyflowID preDefMoneyflowId);

  /**
   * This Service returns all existing {@link PreDefMoneyflow}s.
   *
   * @param userId
   *                 {@link UserID}
   * @return list of all {@link PreDefMoneyflow}s
   */
  List<PreDefMoneyflow> getAllPreDefMoneyflows(UserID userId);

  /**
   * This service creates a {@link PreDefMoneyflow}. Before the {@link PreDefMoneyflow} is created
   * it is validated for correctness.
   *
   * @param preDefMoneyflow
   *                          the {@link PreDefMoneyflow} to be created
   * @throws BusinessException
   *                             If the validation of the given {@link PreDefMoneyflow} failed.
   */
  PreDefMoneyflowID createPreDefMoneyflow(PreDefMoneyflow preDefMoneyflow);

  /**
   * This service changes a {@link PreDefMoneyflow}. Before the {@link PreDefMoneyflow} is changed,
   * the new values are validated for correctness.
   *
   * @param preDefMoneyflow
   *                          the new {@link PreDefMoneyflow} attributes
   * @throws BusinessException
   *                             If the validation of the given {@link PreDefMoneyflow} failed.
   */
  void updatePreDefMoneyflow(PreDefMoneyflow preDefMoneyflow);

  /**
   * This service deletes a {@link PreDefMoneyflow} from the system.
   *
   * @param userId
   *                            {@link UserID}
   * @param preDefMoneyflowId
   *                            The {@link PreDefMoneyflowID} of the to-be-deleted
   *                            {@link PreDefMoneyflow}
   */
  void deletePreDefMoneyflow(UserID userId, PreDefMoneyflowID preDefMoneyflowId);

  /**
   * Updates the "lastUsedDate" information for a {@link PreDefMoneyflow}. It is meant to be called
   * whenever a moneyflow gets created based on a {@link PreDefMoneyflow}.
   *
   * @param userId
   *                            {@link UserID}
   * @param preDefMoneyflowId
   */
  void setLastUsedDate(UserID userId, PreDefMoneyflowID preDefMoneyflowId);
}
