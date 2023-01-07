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

package org.laladev.moneyjinn.service.dao;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.laladev.moneyjinn.service.dao.data.UserData;
import org.laladev.moneyjinn.service.dao.mapper.IUserDaoMapper;

@Named
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class UserDao {
  private final IUserDaoMapper mapper;

  public UserData getUserById(final Long id) {
    return this.mapper.getUserById(id);
  }

  public List<UserData> getAllUsers() {
    return this.mapper.getAllUsers();
  }

  public UserData getUserByName(final String name) {
    return this.mapper.getUserByName(name);
  }

  public Long createUser(final UserData userData) {
    this.mapper.createUser(userData);
    return userData.getId();
  }

  public void updateUser(final UserData userData) {
    this.mapper.updateUser(userData);
  }

  public void setPassword(final Long userId, final String password) {
    this.mapper.setPassword(userId, password);
  }

  public void resetPassword(final Long userId, final String password) {
    this.mapper.resetPassword(userId, password);
  }

  public void deleteUser(final Long userId) {
    this.mapper.deleteUser(userId);
  }
}
