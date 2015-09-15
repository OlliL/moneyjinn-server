package org.laladev.moneyjinn.server.controller.mapper;

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

import org.laladev.moneyjinn.api.IMapper;
import org.laladev.moneyjinn.businesslogic.model.PostingAccount;
import org.laladev.moneyjinn.businesslogic.model.PostingAccountID;
import org.laladev.moneyjinn.core.rest.model.transport.PostingAccountTransport;

public class PostingAccountTransportMapper implements IMapper<PostingAccount, PostingAccountTransport> {

	@Override
	public PostingAccount mapBToA(final PostingAccountTransport postingAccountTransport) {
		final PostingAccount postingAccount = new PostingAccount(new PostingAccountID(postingAccountTransport.getId()),
				postingAccountTransport.getName());
		return postingAccount;
	}

	@Override
	public PostingAccountTransport mapAToB(final PostingAccount postingAccount) {
		final PostingAccountTransport postingAccountTransport = new PostingAccountTransport();
		postingAccountTransport.setId(postingAccount.getId().getId());
		postingAccountTransport.setName(postingAccount.getName());

		return postingAccountTransport;
	}
}