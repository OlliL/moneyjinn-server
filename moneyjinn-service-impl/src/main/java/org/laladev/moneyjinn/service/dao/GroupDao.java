//
// Copyright (c) 2015 Oliver Lehmann <lehmann@ans-netz.de>
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
import org.laladev.moneyjinn.service.dao.data.GroupData;
import org.laladev.moneyjinn.service.dao.mapper.IGroupDaoMapper;

@Named
public class GroupDao {
  @Inject
  private IGroupDaoMapper mapper;

  public List<GroupData> getAllGroups() {
    return this.mapper.getAllGroups();
  }

  public GroupData getGroupById(final Long id) {
    return this.mapper.getGroupById(id);
  }

  public GroupData getGroupByName(final String name) {
    return this.mapper.getGroupByName(name);
  }

  public Long createGroup(final GroupData groupData) {
    this.mapper.createGroup(groupData);
    return groupData.getId();
  }

  public void updateGroup(final GroupData groupData) {
    this.mapper.updateGroup(groupData);
  }

  public void deleteGroup(final Long id) {
    this.mapper.deleteGroup(id);
  }
}
