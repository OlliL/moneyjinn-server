package org.laladev.moneyjinn.server.builder;

import java.math.BigDecimal;

import org.laladev.moneyjinn.core.rest.model.transport.ImportedMoneyflowTransport;

public class ImportedMoneyflowTransportBuilder extends ImportedMoneyflowTransport {

	public static final String IMPORTED_MONEYFLOW1_EXTERNAL_ID = "8765421A";

	public static final String NEW_IMPORTED_MONEYFLOW_COMMENT = "flownew";

	public static final Long IMPORTED_MONEYFLOW1_ID = 1l;
	public static final Long IMPORTED_MONEYFLOW2_ID = 2l;
	public static final Long NON_EXISTING_ID = 666l;
	public static final Long NEXT_ID = 3l;

	public ImportedMoneyflowTransportBuilder withPrivat(final Short privat) {
		super.setPrivat(privat);
		return this;
	}

	public ImportedMoneyflowTransportBuilder forImportedMoneyflow1() {
		super.setId(IMPORTED_MONEYFLOW1_ID);
		super.setExternalid(IMPORTED_MONEYFLOW1_EXTERNAL_ID);
		super.setCapitalsourceid(CapitalsourceTransportBuilder.CAPITALSOURCE1_ID);
		super.setCapitalsourcecomment(CapitalsourceTransportBuilder.CAPITALSOURCE1_COMMENT);
		super.setBookingdate(DateUtil.getGMTDate("2010-01-02"));
		super.setInvoicedate(DateUtil.getGMTDate("2010-01-01"));
		super.setName("Paul");
		super.setAccountNumber(ContractpartnerAccountTransportBuilder.CONTRACTPARTNER_ACCOUNT1_ACCOUNT_NUMBER);
		super.setBankCode(ContractpartnerAccountTransportBuilder.CONTRACTPARTNER_ACCOUNT1_BANK_CODE);
		super.setContractpartnerid(ContractpartnerAccountTransportBuilder.CONTRACTPARTNER_ACCOUNT1_ID);
		super.setContractpartnername(ContractpartnerTransportBuilder.CONTRACTPARTNER1_NAME);
		super.setUsage("secret");
		super.setAmount(new BigDecimal("10.10"));
		return this;
	}

	public ImportedMoneyflowTransportBuilder forImportedMoneyflow1ToImport() {
		this.forImportedMoneyflow1();
		super.setComment("test");
		super.setPostingaccountid(PostingAccountTransportBuilder.POSTING_ACCOUNT1_ID);
		return this;
	}

	public ImportedMoneyflowTransportBuilder forImportedMoneyflow2() {
		super.setId(IMPORTED_MONEYFLOW2_ID);
		super.setExternalid("8765421B");
		super.setCapitalsourceid(CapitalsourceTransportBuilder.CAPITALSOURCE1_ID);
		super.setCapitalsourcecomment(CapitalsourceTransportBuilder.CAPITALSOURCE1_COMMENT);
		super.setBookingdate(DateUtil.getGMTDate("2010-01-02"));
		super.setInvoicedate(DateUtil.getGMTDate("2010-01-01"));
		super.setName("Jane");
		super.setAccountNumber("888888888888");
		super.setBankCode("999999");
		super.setUsage("code");
		super.setAmount(new BigDecimal("-20.20"));
		return this;
	}

	public ImportedMoneyflowTransportBuilder forImportedMoneyflow2ToImport() {
		this.forImportedMoneyflow2();
		super.setComment("test");
		super.setPostingaccountid(PostingAccountTransportBuilder.POSTING_ACCOUNT1_ID);
		super.setContractpartnerid(ContractpartnerAccountTransportBuilder.CONTRACTPARTNER_ACCOUNT1_ID);
		super.setContractpartnername(ContractpartnerTransportBuilder.CONTRACTPARTNER1_NAME);
		return this;
	}

	public ImportedMoneyflowTransportBuilder forNewImportedMoneyflow() {
		super.setId(NON_EXISTING_ID);
		super.setExternalid("ABC123");
		super.setBankCodeCapitalsource(CapitalsourceTransportBuilder.CAPITALSOURCE1_BANKCODE);
		super.setAccountNumberCapitalsource(CapitalsourceTransportBuilder.CAPITALSOURCE1_ACCOUNTNUMBER);
		super.setBookingdate(DateUtil.getGMTDate("2008-01-02"));
		super.setInvoicedate(DateUtil.getGMTDate("2008-01-01"));
		super.setName("Klaus");
		super.setAccountNumber("1234");
		super.setBankCode("ABCD");
		super.setComment(NEW_IMPORTED_MONEYFLOW_COMMENT);
		super.setAmount(new BigDecimal("10.10"));
		return this;
	}

	public ImportedMoneyflowTransport build() {
		final ImportedMoneyflowTransport transport = new ImportedMoneyflowTransport();

		transport.setId(super.getId());
		transport.setExternalid(super.getExternalid());
		transport.setAccountNumberCapitalsource(super.getAccountNumberCapitalsource());
		transport.setBankCodeCapitalsource(super.getBankCodeCapitalsource());
		transport.setName(super.getName());
		transport.setAccountNumber(super.getAccountNumber());
		transport.setBankCode(super.getBankCode());
		transport.setUsage(super.getUsage());

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
