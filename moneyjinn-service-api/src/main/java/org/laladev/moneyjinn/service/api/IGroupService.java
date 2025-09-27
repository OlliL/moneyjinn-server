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

import org.laladev.moneyjinn.model.IHasGroup;
import org.laladev.moneyjinn.model.access.Group;
import org.laladev.moneyjinn.model.access.GroupID;
import org.laladev.moneyjinn.model.exception.BusinessException;
import org.laladev.moneyjinn.model.validation.ValidationResult;

import java.util.List;

/**
 * <p>
 * GroupService is the Core Service handling everything around an {@link Group}.
 * </p>
 *
 * <p>
 * GroupService is the Domain Service handling operations around an
 * {@link Group} like getting, creating, updating, deleting. Before a
 * {@link Group} is created or updated, the {@link Group} is validated for
 * correctness.
 * </p>
 * <p>
 * The main datasource is the Table <code>access</code>.
 * </p>
 *
 * @author Oliver Lehmann
 * @since 0.0.1
 */
public interface IGroupService {
    /**
     * Checks the validity of the given {@link Group}.
     *
     * @param group the {@link Group}
     * @return {@link ValidationResult}
     */
    ValidationResult validateGroup(Group group);

    /**
     * Returns the {@link Group} for the given {@link GroupID}.
     *
     * @param groupId the {@link GroupID}
     * @return Group
     */
    Group getGroupById(GroupID groupId);

    /**
     * This Service returns all existing {@link Group}s.
     *
     * @return list of all {@link Group}s
     */
    List<Group> getAllGroups();

    /**
     * This Service returns the {@link Group} for the specified name.
     *
     * @param name the Group-Name
     * @return {@link Group}
     */
    Group getGroupByName(String name);

    /**
     * This service creates a {@link Group}. Before the {@link Group} is created it
     * is validated for correctness.
     *
     * @param group the {@link Group} to be created
     * @throws BusinessException If the validation of the given {@link Group}
     *                           failed.
     */
    GroupID createGroup(Group group);

    /**
     * This service changes a {@link Group}. Before the {@link Group} is changed,
     * the new values are validated for correctness.
     *
     * @param group the new {@link Group} attributes
     * @throws BusinessException If the validation of the given {@link Group}
     *                           failed.
     */
    void updateGroup(Group group);

    /**
     * This service deletes a {@link Group} from the system.
     *
     * @param groupId The {@link GroupID} of the to-be-deleted {@link Group}
     * @throws BusinessException If the deletion fails an error is throws. It is
     *                           always assumed that it fails because of a Foreign
     *                           Key Constraint Violation on the DB level
     */
    void deleteGroup(GroupID groupId);

    <T extends IHasGroup> void enrichEntity(T entity);
}
