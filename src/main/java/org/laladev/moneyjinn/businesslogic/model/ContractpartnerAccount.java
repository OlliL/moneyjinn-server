package org.laladev.moneyjinn.businesslogic.model;

public class ContractpartnerAccount extends AbstractEntity<ContractpartnerAccountID> {
	private static final long serialVersionUID = 1L;
	private Contractpartner contractpartner;
	private BankAccount bankAccount;

	public ContractpartnerAccount() {
	}

	public ContractpartnerAccount(final ContractpartnerAccountID contractpartnerAccountId,
			final Contractpartner contractpartner, final BankAccount bankAccount) {
		super();
		super.setId(contractpartnerAccountId);
		this.contractpartner = contractpartner;
		this.bankAccount = bankAccount;
	}

	public final Contractpartner getContractpartner() {
		return this.contractpartner;
	}

	public final void setContractpartner(final Contractpartner contractpartner) {
		this.contractpartner = contractpartner;
	}

	public final BankAccount getBankAccount() {
		return this.bankAccount;
	}

	public final void setBankAccount(final BankAccount bankAccount) {
		this.bankAccount = bankAccount;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((this.bankAccount == null) ? 0 : this.bankAccount.hashCode());
		result = prime * result + ((this.contractpartner == null) ? 0 : this.contractpartner.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		final ContractpartnerAccount other = (ContractpartnerAccount) obj;
		if (this.bankAccount == null) {
			if (other.bankAccount != null) {
				return false;
			}
		} else if (!this.bankAccount.equals(other.bankAccount)) {
			return false;
		}
		if (this.contractpartner == null) {
			if (other.contractpartner != null) {
				return false;
			}
		} else if (!this.contractpartner.equals(other.contractpartner)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("ContractpartnerAccount [contractpartner=");
		builder.append(this.contractpartner);
		builder.append(", bankAccount=");
		builder.append(this.bankAccount);
		builder.append("]");
		return builder.toString();
	}

}
