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

package org.laladev.moneyjinn.service.dao.data;

public class UserData {
  private Long id;
  private String name;
  private String password;
  private boolean permLogin;
  private boolean permAdmin;
  private boolean attChangePassword;

  public final Long getId() {
    return this.id;
  }

  public final void setId(final Long id) {
    this.id = id;
  }

  public final String getName() {
    return this.name;
  }

  public final void setName(final String name) {
    this.name = name;
  }

  public final String getPassword() {
    return this.password;
  }

  public final void setPassword(final String password) {
    this.password = password;
  }

  public final boolean isPermLogin() {
    return this.permLogin;
  }

  public final void setPermLogin(final boolean permLogin) {
    this.permLogin = permLogin;
  }

  public final boolean isPermAdmin() {
    return this.permAdmin;
  }

  public final void setPermAdmin(final boolean permAdmin) {
    this.permAdmin = permAdmin;
  }

  public final boolean isAttChangePassword() {
    return this.attChangePassword;
  }

  public final void setAttChangePassword(final boolean attChangePassword) {
    this.attChangePassword = attChangePassword;
  }
}
