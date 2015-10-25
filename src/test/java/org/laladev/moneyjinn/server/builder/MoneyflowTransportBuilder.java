package org.laladev.moneyjinn.server.builder;

import java.math.BigDecimal;

import org.laladev.moneyjinn.core.rest.model.transport.MoneyflowTransport;

public class MoneyflowTransportBuilder extends MoneyflowTransport {

	public static final String MONEYFLOW1_COMMENT = "flow1";
	public static final String NEW_MONEYFLOW_COMMENT = "flownew";

	public static final Long MONEYFLOW1_ID = 1l;
	public static final Long NON_EXISTING_ID = 666l;
	public static final Long NEXT_ID = 19l;

	public MoneyflowTransportBuilder forMoneyflow1() {
		super.setId(MONEYFLOW1_ID);
		super.setUserid(UserTransportBuilder.USER1_ID);
		super.setPrivat(null);
		super.setBookingdate(DateUtil.getGMTDate("2009-01-01"));
		super.setInvoicedate(DateUtil.getGMTDate("2009-01-01"));
		super.setAmount(new BigDecimal("-1.10"));
		super.setCapitalsourcecomment(CapitalsourceTransportBuilder.CAPITALSOURCE1_COMMENT);
		super.setCapitalsourceid(CapitalsourceTransportBuilder.CAPITALSOURCE1_ID);
		super.setComment(MONEYFLOW1_COMMENT);
		super.setContractpartnerid(ContractpartnerTransportBuilder.CONTRACTPARTNER1_ID);
		super.setContractpartnername(ContractpartnerTransportBuilder.CONTRACTPARTNER1_NAME);
		super.setPostingaccountid(PostingAccountTransportBuilder.POSTING_ACCOUNT1_ID);
		super.setPostingaccountname(PostingAccountTransportBuilder.POSTING_ACCOUNT1_NAME);
		return this;
	}

	public MoneyflowTransportBuilder forNewMoneyflow() {
		super.setId(NON_EXISTING_ID);
		super.setUserid(UserTransportBuilder.USER1_ID);
		super.setPrivat(null);
		super.setBookingdate(DateUtil.getGMTDate("2008-01-01"));
		super.setInvoicedate(DateUtil.getGMTDate("2008-01-01"));
		super.setAmount(new BigDecimal("10.10"));
		super.setCapitalsourcecomment(CapitalsourceTransportBuilder.CAPITALSOURCE2_COMMENT);
		super.setCapitalsourceid(CapitalsourceTransportBuilder.CAPITALSOURCE2_ID);
		super.setComment(NEW_MONEYFLOW_COMMENT);
		super.setContractpartnerid(ContractpartnerTransportBuilder.CONTRACTPARTNER2_ID);
		super.setContractpartnername(ContractpartnerTransportBuilder.CONTRACTPARTNER2_NAME);
		super.setPostingaccountid(PostingAccountTransportBuilder.POSTING_ACCOUNT2_ID);
		super.setPostingaccountname(PostingAccountTransportBuilder.POSTING_ACCOUNT2_NAME);
		return this;
	}

	public MoneyflowTransport build() {
		final MoneyflowTransport transport = new MoneyflowTransport();

		transport.setId(super.getId());
		transport.setUserid(super.getUserid());
		transport.setPrivat(super.getPrivat());
		transport.setBookingdate(super.getBookingdate());
		transport.setInvoicedate(super.getInvoicedate());
		transport.setComment(super.getComment());
		transport.setAmount(super.getAmount());
		transport.setCapitalsourcecomment(super.getCapitalsourcecomment());
		transport.setCapitalsourceid(super.getCapitalsourceid());
		transport.setContractpartnerid(super.getContractpartnerid());
		transport.setContractpartnername(super.getContractpartnername());
		transport.setPostingaccountid(super.getPostingaccountid());
		transport.setPostingaccountname(super.getPostingaccountname());

		return transport;
	}
}
