package org.laladev.moneyjinn.server.builder;

import org.laladev.moneyjinn.core.rest.model.transport.ContractpartnerTransport;

public class ContractpartnerTransportBuilder extends ContractpartnerTransport {

	public static final String CONTRACTPARTNER1_NAME = "Partner1";
	public static final String CONTRACTPARTNER2_NAME = "Qartner2";
	public static final String CONTRACTPARTNER3_NAME = "Qartner3";
	public static final String CONTRACTPARTNER4_NAME = "Sartner4";
	public static final String CONTRACTPARTNER5_NAME = "AdminPartner";
	public static final String NEWCONTRACTPARTNER_NAME = "PartnerNew";

	public static final Long CONTRACTPARTNER1_ID = 1l;
	public static final Long CONTRACTPARTNER2_ID = 2l;
	public static final Long CONTRACTPARTNER3_ID = 3l;
	public static final Long CONTRACTPARTNER4_ID = 4l;
	public static final Long CONTRACTPARTNER5_ID = 5l;
	public static final Long NON_EXISTING_ID = 666l;
	public static final Long NEXT_ID = 6l;

	public ContractpartnerTransportBuilder forContractpartner1() {
		super.setId(CONTRACTPARTNER1_ID);
		super.setUserid(UserTransportBuilder.USER1_ID);
		super.setName(CONTRACTPARTNER1_NAME);
		super.setValidFrom(DateUtil.getGmtDate("2000-01-01"));
		super.setValidTil(DateUtil.getGmtDate("2999-12-31"));
		super.setStreet("Street1");
		super.setPostcode(12345);
		super.setTown("Town1");
		super.setCountry("Country1");
		super.setMoneyflowComment("Default Comment 1");
		super.setPostingAccountId(PostingAccountTransportBuilder.POSTING_ACCOUNT1_ID);
		super.setPostingAccountName(PostingAccountTransportBuilder.POSTING_ACCOUNT1_NAME);

		return this;
	}

	public ContractpartnerTransportBuilder forContractpartner2() {
		super.setId(CONTRACTPARTNER2_ID);
		super.setUserid(UserTransportBuilder.USER1_ID);
		super.setName(CONTRACTPARTNER2_NAME);
		super.setValidFrom(DateUtil.getGmtDate("2000-01-01"));
		super.setValidTil(DateUtil.getGmtDate("2999-12-31"));
		super.setStreet("Street2");
		super.setPostcode(12345);
		super.setTown("Town2");
		super.setCountry("Country2");
		super.setMoneyflowComment(null);
		super.setPostingAccountId(null);
		super.setPostingAccountName(null);

		return this;
	}

	public ContractpartnerTransportBuilder forContractpartner3() {
		super.setId(CONTRACTPARTNER3_ID);
		super.setUserid(UserTransportBuilder.USER3_ID);
		super.setName(CONTRACTPARTNER3_NAME);
		super.setValidFrom(DateUtil.getGmtDate("2000-01-01"));
		super.setValidTil(DateUtil.getGmtDate("2010-12-31"));
		super.setStreet("Street3");
		super.setPostcode(12345);
		super.setTown("Town3");
		super.setCountry("Country3");
		super.setMoneyflowComment("Default Comment 3");
		super.setPostingAccountId(PostingAccountTransportBuilder.POSTING_ACCOUNT2_ID);
		super.setPostingAccountName(PostingAccountTransportBuilder.POSTING_ACCOUNT2_NAME);

		return this;
	}

	public ContractpartnerTransportBuilder forContractpartner4() {
		super.setId(CONTRACTPARTNER4_ID);
		super.setUserid(UserTransportBuilder.USER3_ID);
		super.setName(CONTRACTPARTNER4_NAME);
		super.setValidFrom(DateUtil.getGmtDate("2000-01-02"));
		super.setValidTil(DateUtil.getGmtDate("2010-12-31"));
		super.setStreet("Street4");
		super.setPostcode(12345);
		super.setTown("Town4");
		super.setCountry("Country4");
		super.setMoneyflowComment(null);
		super.setPostingAccountId(null);
		super.setPostingAccountName(null);

		return this;
	}

	public ContractpartnerTransportBuilder forContractpartner5() {
		super.setId(CONTRACTPARTNER5_ID);
		super.setUserid(UserTransportBuilder.ADMIN_ID);
		super.setName(CONTRACTPARTNER5_NAME);
		super.setValidFrom(DateUtil.getGmtDate("2000-01-01"));
		super.setValidTil(DateUtil.getGmtDate("2999-12-31"));
		super.setStreet("Street");
		super.setPostcode(12345);
		super.setTown("Town");
		super.setCountry("Country");
		super.setMoneyflowComment(null);
		super.setPostingAccountId(null);
		super.setPostingAccountName(null);

		return this;
	}

	public ContractpartnerTransportBuilder forNewContractpartner() {
		super.setId(NON_EXISTING_ID);
		super.setUserid(UserTransportBuilder.USER2_ID);
		super.setName(NEWCONTRACTPARTNER_NAME);
		super.setValidFrom(DateUtil.getGmtDate("1980-01-01"));
		super.setValidTil(DateUtil.getGmtDate("2999-12-31"));
		super.setStreet("StreetNew");
		super.setPostcode(12345);
		super.setTown("TownNew");
		super.setCountry("CountryNew");
		super.setMoneyflowComment("Default Comment New");
		super.setPostingAccountId(PostingAccountTransportBuilder.POSTING_ACCOUNT1_ID);
		super.setPostingAccountName(PostingAccountTransportBuilder.POSTING_ACCOUNT1_NAME);

		return this;
	}

	public ContractpartnerTransport build() {
		final ContractpartnerTransport transport = new ContractpartnerTransport();

		transport.setId(super.getId());
		transport.setUserid(super.getUserid());
		transport.setName(super.getName());
		transport.setValidFrom(super.getValidFrom());
		transport.setValidTil(super.getValidTil());
		transport.setStreet(super.getStreet());
		transport.setPostcode(super.getPostcode());
		transport.setTown(super.getTown());
		transport.setCountry(super.getCountry());
		transport.setMoneyflowComment(super.getMoneyflowComment());
		transport.setPostingAccountId(super.getPostingAccountId());
		transport.setPostingAccountName(super.getPostingAccountName());

		return transport;
	}
}
