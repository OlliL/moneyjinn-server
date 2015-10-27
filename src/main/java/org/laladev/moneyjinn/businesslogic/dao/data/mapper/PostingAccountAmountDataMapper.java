package org.laladev.moneyjinn.businesslogic.dao.data.mapper;

import org.laladev.moneyjinn.businesslogic.dao.data.PostingAccountAmountData;
import org.laladev.moneyjinn.businesslogic.model.PostingAccount;
import org.laladev.moneyjinn.businesslogic.model.PostingAccountAmount;
import org.laladev.moneyjinn.businesslogic.model.PostingAccountID;
import org.laladev.moneyjinn.core.mapper.IMapper;

public class PostingAccountAmountDataMapper implements IMapper<PostingAccountAmount, PostingAccountAmountData> {

	@Override
	public PostingAccountAmount mapBToA(final PostingAccountAmountData postingAccountAmountData) {
		final PostingAccountAmount postingAccountAmount = new PostingAccountAmount();
		postingAccountAmount.setAmount(postingAccountAmountData.getAmount());
		postingAccountAmount.setDate(postingAccountAmountData.getDate().toLocalDate());
		postingAccountAmount.setPostingAccount(
				new PostingAccount(new PostingAccountID(postingAccountAmountData.getMpaPostingAccountId())));
		return postingAccountAmount;
	}

	@Override
	public PostingAccountAmountData mapAToB(final PostingAccountAmount postingAccountAmount) {
		throw new UnsupportedOperationException("Mapping not supported!");
	}
}
