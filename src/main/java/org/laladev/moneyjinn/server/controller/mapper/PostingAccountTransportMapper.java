package org.laladev.moneyjinn.server.controller.mapper;

import org.laladev.moneyjinn.businesslogic.model.PostingAccount;
import org.laladev.moneyjinn.businesslogic.model.PostingAccountID;
import org.laladev.moneyjinn.core.mapper.IMapper;
import org.laladev.moneyjinn.core.rest.model.transport.PostingAccountTransport;

public class PostingAccountTransportMapper implements IMapper<PostingAccount, PostingAccountTransport> {

	@Override
	public PostingAccount mapBToA(final PostingAccountTransport postingAccountTransport) {
		return new PostingAccount(new PostingAccountID(postingAccountTransport.getId()),
				postingAccountTransport.getName());
	}

	@Override
	public PostingAccountTransport mapAToB(final PostingAccount postingAccount) {
		final PostingAccountTransport postingAccountTransport = new PostingAccountTransport();
		postingAccountTransport.setId(postingAccount.getId().getId());
		postingAccountTransport.setName(postingAccount.getName());

		return postingAccountTransport;
	}
}
