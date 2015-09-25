package org.laladev.moneyjinn.server.builder;

import org.laladev.moneyjinn.core.rest.model.transport.ContractpartnerAccountTransport;

public class ContractpartnerAccountTransportBuilder extends ContractpartnerAccountTransport {

	public static final Long CONTRACTPARTNER_ACCOUNT1_ID = 1l;
	public static final Long CONTRACTPARTNER_ACCOUNT2_ID = 2l;
	public static final Long NON_EXISTING_ID = 666l;
	public static final Long NEXT_ID = 3l;

	public ContractpartnerAccountTransportBuilder forContractpartnerAccount1() {
		super.setId(CONTRACTPARTNER_ACCOUNT1_ID);
		super.setBankCode("ABC123");
		super.setAccountNumber("DE1234567890");

		return this;
	}

	public ContractpartnerAccountTransportBuilder forContractpartnerAccount2() {
		super.setId(CONTRACTPARTNER_ACCOUNT1_ID);
		super.setBankCode("ABC456");
		super.setAccountNumber("DE0987654321");

		return this;
	}

	public ContractpartnerAccountTransportBuilder forNewContractpartner() {
		super.setId(NON_EXISTING_ID);
		super.setBankCode("ABC789");
		super.setAccountNumber("DE1111111111");

		return this;
	}

	public ContractpartnerAccountTransport build() {
		final ContractpartnerAccountTransport transport = new ContractpartnerAccountTransport();

		transport.setId(super.getId());
		transport.setBankCode(super.getBankCode());
		transport.setAccountNumber(super.getAccountNumber());

		return transport;
	}
}
