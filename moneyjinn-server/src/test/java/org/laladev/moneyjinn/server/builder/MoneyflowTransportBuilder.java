package org.laladev.moneyjinn.server.builder;

import java.math.BigDecimal;

import org.laladev.moneyjinn.core.rest.model.transport.MoneyflowTransport;

public class MoneyflowTransportBuilder extends MoneyflowTransport {

	public static final String MONEYFLOW1_COMMENT = "flow1";
	public static final String MONEYFLOW_GENERATED_COMMENT = "generated";
	public static final String NEW_MONEYFLOW_COMMENT = "flownew";

	public static final Long MONEYFLOW1_ID = 1l;
	public static final Long MONEYFLOW2_ID = 2l;
	public static final Long MONEYFLOW13_ID = 13l;
	public static final Long MONEYFLOW14_ID = 14l;
	public static final Long MONEYFLOW15_ID = 15l;
	public static final Long MONEYFLOW16_ID = 16l;
	public static final Long MONEYFLOW18_ID = 18l;
	public static final Long MONEYFLOW19_ID = 19l;
	public static final Long NON_EXISTING_ID = 666l;
	public static final Long NEXT_ID = 20l;

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

	public MoneyflowTransportBuilder forMoneyflow2() {
		super.setId(MONEYFLOW2_ID);
		super.setUserid(UserTransportBuilder.USER1_ID);
		super.setBookingdate(DateUtil.getGMTDate("2008-12-01"));
		super.setInvoicedate(DateUtil.getGMTDate("2008-12-01"));
		super.setAmount(new BigDecimal("10.10"));
		super.setCapitalsourcecomment(CapitalsourceTransportBuilder.CAPITALSOURCE1_COMMENT);
		super.setCapitalsourceid(CapitalsourceTransportBuilder.CAPITALSOURCE1_ID);
		super.setComment(MONEYFLOW_GENERATED_COMMENT);
		super.setContractpartnerid(ContractpartnerTransportBuilder.CONTRACTPARTNER1_ID);
		super.setContractpartnername(ContractpartnerTransportBuilder.CONTRACTPARTNER1_NAME);
		super.setPostingaccountid(PostingAccountTransportBuilder.POSTING_ACCOUNT1_ID);
		super.setPostingaccountname(PostingAccountTransportBuilder.POSTING_ACCOUNT1_NAME);
		return this;
	}

	public MoneyflowTransportBuilder forMoneyflow13() {
		super.setId(MONEYFLOW13_ID);
		super.setUserid(UserTransportBuilder.USER1_ID);
		super.setPrivat((short) 1);
		super.setBookingdate(DateUtil.getGMTDate("2009-12-01"));
		super.setInvoicedate(DateUtil.getGMTDate("2009-12-01"));
		super.setAmount(new BigDecimal("10.00"));
		super.setCapitalsourcecomment(CapitalsourceTransportBuilder.CAPITALSOURCE2_COMMENT);
		super.setCapitalsourceid(CapitalsourceTransportBuilder.CAPITALSOURCE2_ID);
		super.setComment(MONEYFLOW_GENERATED_COMMENT);
		super.setContractpartnerid(ContractpartnerTransportBuilder.CONTRACTPARTNER1_ID);
		super.setContractpartnername(ContractpartnerTransportBuilder.CONTRACTPARTNER1_NAME);
		super.setPostingaccountid(PostingAccountTransportBuilder.POSTING_ACCOUNT2_ID);
		super.setPostingaccountname(PostingAccountTransportBuilder.POSTING_ACCOUNT2_NAME);
		return this;
	}

	public MoneyflowTransportBuilder forMoneyflow14() {
		super.setId(MONEYFLOW14_ID);
		super.setUserid(UserTransportBuilder.USER1_ID);
		super.setBookingdate(DateUtil.getGMTDate("2010-01-01"));
		super.setInvoicedate(DateUtil.getGMTDate("2010-01-01"));
		super.setAmount(new BigDecimal("-10.00"));
		super.setCapitalsourcecomment(CapitalsourceTransportBuilder.CAPITALSOURCE2_COMMENT);
		super.setCapitalsourceid(CapitalsourceTransportBuilder.CAPITALSOURCE2_ID);
		super.setComment(MONEYFLOW_GENERATED_COMMENT);
		super.setContractpartnerid(ContractpartnerTransportBuilder.CONTRACTPARTNER1_ID);
		super.setContractpartnername(ContractpartnerTransportBuilder.CONTRACTPARTNER1_NAME);
		super.setPostingaccountid(PostingAccountTransportBuilder.POSTING_ACCOUNT2_ID);
		super.setPostingaccountname(PostingAccountTransportBuilder.POSTING_ACCOUNT2_NAME);
		return this;
	}

	public MoneyflowTransportBuilder forMoneyflow15() {
		super.setId(MONEYFLOW15_ID);
		super.setUserid(UserTransportBuilder.USER1_ID);
		super.setPrivat((short) 1);
		super.setBookingdate(DateUtil.getGMTDate("2010-02-01"));
		super.setInvoicedate(DateUtil.getGMTDate("2010-02-01"));
		super.setAmount(new BigDecimal("10.00"));
		super.setCapitalsourcecomment(CapitalsourceTransportBuilder.CAPITALSOURCE2_COMMENT);
		super.setCapitalsourceid(CapitalsourceTransportBuilder.CAPITALSOURCE2_ID);
		super.setComment(MONEYFLOW_GENERATED_COMMENT);
		super.setContractpartnerid(ContractpartnerTransportBuilder.CONTRACTPARTNER1_ID);
		super.setContractpartnername(ContractpartnerTransportBuilder.CONTRACTPARTNER1_NAME);
		super.setPostingaccountid(PostingAccountTransportBuilder.POSTING_ACCOUNT2_ID);
		super.setPostingaccountname(PostingAccountTransportBuilder.POSTING_ACCOUNT2_NAME);
		return this;
	}

	public MoneyflowTransportBuilder forMoneyflow16() {
		super.setId(MONEYFLOW16_ID);
		super.setUserid(UserTransportBuilder.USER1_ID);
		super.setPrivat((short) 1);
		super.setBookingdate(DateUtil.getGMTDate("2010-03-01"));
		super.setInvoicedate(DateUtil.getGMTDate("2010-03-01"));
		super.setAmount(new BigDecimal("-10.00"));
		super.setCapitalsourcecomment(CapitalsourceTransportBuilder.CAPITALSOURCE2_COMMENT);
		super.setCapitalsourceid(CapitalsourceTransportBuilder.CAPITALSOURCE2_ID);
		super.setComment(MONEYFLOW_GENERATED_COMMENT);
		super.setContractpartnerid(ContractpartnerTransportBuilder.CONTRACTPARTNER1_ID);
		super.setContractpartnername(ContractpartnerTransportBuilder.CONTRACTPARTNER1_NAME);
		super.setPostingaccountid(PostingAccountTransportBuilder.POSTING_ACCOUNT2_ID);
		super.setPostingaccountname(PostingAccountTransportBuilder.POSTING_ACCOUNT2_NAME);
		return this;
	}

	public MoneyflowTransportBuilder forMoneyflow18() {
		super.setId(MONEYFLOW18_ID);
		super.setUserid(UserTransportBuilder.USER1_ID);
		super.setPrivat((short) 1);
		super.setBookingdate(DateUtil.getGMTDate("2010-05-03"));
		super.setInvoicedate(DateUtil.getGMTDate("2010-05-03"));
		super.setAmount(new BigDecimal("-5.00"));
		super.setCapitalsourcecomment(CapitalsourceTransportBuilder.CAPITALSOURCE2_COMMENT);
		super.setCapitalsourceid(CapitalsourceTransportBuilder.CAPITALSOURCE2_ID);
		super.setComment(MONEYFLOW_GENERATED_COMMENT);
		super.setContractpartnerid(ContractpartnerTransportBuilder.CONTRACTPARTNER1_ID);
		super.setContractpartnername(ContractpartnerTransportBuilder.CONTRACTPARTNER1_NAME);
		super.setPostingaccountid(PostingAccountTransportBuilder.POSTING_ACCOUNT2_ID);
		super.setPostingaccountname(PostingAccountTransportBuilder.POSTING_ACCOUNT2_NAME);
		return this;
	}

	public MoneyflowTransportBuilder forMoneyflow19() {
		super.setId(MONEYFLOW19_ID);
		super.setUserid(UserTransportBuilder.USER1_ID);
		super.setBookingdate(DateUtil.getGMTDate("2010-05-01"));
		super.setInvoicedate(DateUtil.getGMTDate("2010-05-01"));
		super.setAmount(new BigDecimal("-5.00"));
		super.setCapitalsourcecomment(CapitalsourceTransportBuilder.CAPITALSOURCE2_COMMENT);
		super.setCapitalsourceid(CapitalsourceTransportBuilder.CAPITALSOURCE2_ID);
		super.setComment(MONEYFLOW_GENERATED_COMMENT);
		super.setContractpartnerid(ContractpartnerTransportBuilder.CONTRACTPARTNER2_ID);
		super.setContractpartnername(ContractpartnerTransportBuilder.CONTRACTPARTNER2_NAME);
		super.setPostingaccountid(PostingAccountTransportBuilder.POSTING_ACCOUNT2_ID);
		super.setPostingaccountname(PostingAccountTransportBuilder.POSTING_ACCOUNT2_NAME);
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