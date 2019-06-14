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

package org.laladev.moneyjinn.service.dao.mapper;

import java.util.LinkedHashSet;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.laladev.moneyjinn.service.dao.data.UserData;

public interface IUserDaoMapper {
	public List<UserData> getAllUsers();

	public UserData getUserById(Long id);

	public Integer countAllUsers();

	public LinkedHashSet<Character> getAllUserInitials();

	public List<UserData> getAllUsersByInitial(String initial);

	public UserData getUserByName(String name);

	public void createUser(UserData userData);

	public void updateUser(UserData userData);

	public void deleteUser(Long userId);

	public void setPassword(@Param("userId") Long userId, @Param("password") String password);

	public void resetPassword(@Param("userId") Long userId, @Param("password") String password);

}
