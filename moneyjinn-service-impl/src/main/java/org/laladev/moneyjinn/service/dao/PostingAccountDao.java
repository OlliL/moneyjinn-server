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
import lombok.RequiredArgsConstructor;
import org.laladev.moneyjinn.service.dao.data.PostingAccountData;
import org.laladev.moneyjinn.service.dao.mapper.IPostingAccountDaoMapper;

@Named
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class PostingAccountDao {
  private final IPostingAccountDaoMapper mapper;

  public List<PostingAccountData> getAllPostingAccounts() {
    return this.mapper.getAllPostingAccounts();
  }

  public PostingAccountData getPostingAccountById(final Long id) {
    return this.mapper.getPostingAccountById(id);
  }

  public PostingAccountData getPostingAccountByName(final String name) {
    return this.mapper.getPostingAccountByName(name);
  }

  public Long createPostingAccount(final PostingAccountData postingAccountData) {
    this.mapper.createPostingAccount(postingAccountData);
    return postingAccountData.getId();
  }

  public void updatePostingAccount(final PostingAccountData postingAccountData) {
    this.mapper.updatePostingAccount(postingAccountData);
  }

  public void deletePostingAccount(final Long id) {
    this.mapper.deletePostingAccount(id);
  }
}
