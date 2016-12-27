package org.laladev.moneyjinn.server.builder;

import java.math.BigDecimal;

import org.laladev.moneyjinn.core.rest.model.transport.MoneyflowSplitEntryTransport;

public class MoneyflowSplitEntryTransportBuilder extends MoneyflowSplitEntryTransport {

	public static final Long MONEYFLOW1_ID = 1l;
	public static final Long MONEYFLOW_SPLIT_ENTRY1_ID = 1l;
	public static final Long MONEYFLOW_SPLIT_ENTRY2_ID = 2l;
	public static final Long NEXT_ID = 3l;

	public MoneyflowSplitEntryTransportBuilder forMoneyflowSplitEntry1() {
		super.setId(MONEYFLOW_SPLIT_ENTRY1_ID);
		super.setMoneyflowid(MONEYFLOW1_ID);
		super.setAmount(new BigDecimal("-1.00"));
		super.setComment("split1");
		super.setPostingaccountid(PostingAccountTransportBuilder.POSTING_ACCOUNT1_ID);
		super.setPostingaccountname(PostingAccountTransportBuilder.POSTING_ACCOUNT1_NAME);
		return this;
	}

	public MoneyflowSplitEntryTransportBuilder forMoneyflowSplitEntry2() {
		super.setId(MONEYFLOW_SPLIT_ENTRY2_ID);
		super.setMoneyflowid(MONEYFLOW1_ID);
		super.setAmount(new BigDecimal("-0.10"));
		super.setComment("split2");
		super.setPostingaccountid(PostingAccountTransportBuilder.POSTING_ACCOUNT2_ID);
		super.setPostingaccountname(PostingAccountTransportBuilder.POSTING_ACCOUNT2_NAME);
		return this;
	}

	public MoneyflowSplitEntryTransport build() {
		final MoneyflowSplitEntryTransport transport = new MoneyflowSplitEntryTransport();

		transport.setId(super.getId());
		transport.setMoneyflowid(super.getMoneyflowid());
		transport.setComment(super.getComment());
		transport.setAmount(super.getAmount());
		transport.setPostingaccountid(super.getPostingaccountid());
		transport.setPostingaccountname(super.getPostingaccountname());

		return transport;
	}
}
