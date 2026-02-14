//
// Copyright (c) 2015-2025 Oliver Lehmann <lehmann@ans-netz.de>
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

import org.laladev.moneyjinn.model.ContractpartnerMatching;
import org.laladev.moneyjinn.model.access.UserID;

/**
 * <p>
 * ContractpartnerMatchingService is the Core Service handling everything around
 * an {@link ContractpartnerMatching}.
 * </p>
 *
 * <p>
 * ContractpartnerMatchingService is the Domain Service handling operations
 * around an {@link ContractpartnerMatching} like getting, creating, updating,
 * deleting. Before a {@link ContractpartnerMatching} is created or updated, the
 * {@link ContractpartnerMatching} is validated for correctness.
 * </p>
 * <p>
 * The main datasource is the Table <code>contractpartnermatchings</code>.
 * </p>
 *
 * @author Oliver Lehmann
 * @since 0.0.1
 */
public interface IContractpartnerMatchingService {

    /**
     * This method a {@link ContractpartnerMatching} for the given searchstring.
     *
     * @param userId       {@link UserID}
     * @param searchString The String to be used for matching.
     * @return List of all found {@link ContractpartnerMatching}
     */
    ContractpartnerMatching getContractpartnerBySearchString(UserID userId, String searchString);
}
