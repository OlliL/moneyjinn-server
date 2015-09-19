package org.laladev.moneyjinn.businesslogic.dao.data;

public class ContractpartnerAccountData {
	private Long contractpartnerAccountId;
	private Long mcpContractpartnerId;
	private String accountNumber;
	private String bankCode;

	public final Long getContractpartnerAccountId() {
		return this.contractpartnerAccountId;
	}

	public final void setContractpartnerAccountId(final Long contractpartnerAccountId) {
		this.contractpartnerAccountId = contractpartnerAccountId;
	}

	public final Long getMcpContractpartnerId() {
		return this.mcpContractpartnerId;
	}

	public final void setMcpContractpartnerId(final Long mcpContractpartnerId) {
		this.mcpContractpartnerId = mcpContractpartnerId;
	}

	public final String getAccountNumber() {
		return this.accountNumber;
	}

	public final void setAccountNumber(final String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public final String getBankCode() {
		return this.bankCode;
	}

	public final void setBankCode(final String bankCode) {
		this.bankCode = bankCode;
	}

}
