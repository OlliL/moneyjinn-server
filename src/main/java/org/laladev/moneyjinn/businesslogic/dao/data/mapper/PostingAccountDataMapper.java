package org.laladev.moneyjinn.businesslogic.dao.data.mapper;

import org.laladev.moneyjinn.businesslogic.dao.data.PostingAccountData;
import org.laladev.moneyjinn.businesslogic.model.PostingAccount;
import org.laladev.moneyjinn.businesslogic.model.PostingAccountID;
import org.laladev.moneyjinn.core.mapper.IMapper;

public class PostingAccountDataMapper implements IMapper<PostingAccount, PostingAccountData> {

	@Override
	public PostingAccount mapBToA(final PostingAccountData postingAccountData) {
		return new PostingAccount(new PostingAccountID(postingAccountData.getId()), postingAccountData.getName());
	}

	@Override
	public PostingAccountData mapAToB(final PostingAccount postingAccount) {
		final PostingAccountData postingAccountData = new PostingAccountData();
		// might be null for new PostingAccounts
		if (postingAccount.getId() != null) {
			postingAccountData.setId(postingAccount.getId().getId());
		}
		postingAccountData.setName(postingAccount.getName());
		return postingAccountData;
	}
}
