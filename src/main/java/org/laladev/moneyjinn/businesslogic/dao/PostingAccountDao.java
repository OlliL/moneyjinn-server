package org.laladev.moneyjinn.businesslogic.dao;

//Copyright (c) 2015 Oliver Lehmann <oliver@laladev.org>
//All rights reserved.
//
//Redistribution and use in source and binary forms, with or without
//modification, are permitted provided that the following conditions
//are met:
//1. Redistributions of source code must retain the above copyright
//notice, this list of conditions and the following disclaimer
//2. Redistributions in binary form must reproduce the above copyright
//notice, this list of conditions and the following disclaimer in the
//documentation and/or other materials provided with the distribution.
//
//THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS'' AND
//ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
//IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
//ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE LIABLE
//FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
//DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
//OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
//HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
//LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
//OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
//SUCH DAMAGE.

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.laladev.moneyjinn.businesslogic.dao.data.PostingAccountData;
import org.laladev.moneyjinn.businesslogic.dao.mapper.IPostingAccountDaoMapper;

@Named
public class PostingAccountDao {

	@Inject
	IPostingAccountDaoMapper mapper;

	public List<PostingAccountData> getAllPostingAccounts() {
		return this.mapper.getAllPostingAccounts();
	}

	public PostingAccountData getPostingAccountById(final Long id) {
		return this.mapper.getPostingAccountById(id);
	}

	public Integer countAllPostingAccounts() {
		return this.mapper.countAllPostingAccounts();
	}

	public List<Character> getAllPostingAccountInitials() {
		return this.mapper.getAllPostingAccountInitials();
	}

	public List<PostingAccountData> getAllPostingAccountsByInitial(final Character initial) {
		return this.mapper.getAllPostingAccountsByInitial(initial);
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
