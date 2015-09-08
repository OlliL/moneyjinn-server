package org.laladev.moneyjinn.core.rest.model.contractpartner;

import org.laladev.moneyjinn.core.rest.model.AbstractResponse;
import org.laladev.moneyjinn.core.rest.model.transport.ContractpartnerTransport;

public abstract class AbstractContractpartnerResponse extends AbstractResponse {
	private ContractpartnerTransport contractpartnerTransport;

	public final ContractpartnerTransport getContractpartnerTransport() {
		return this.contractpartnerTransport;
	}

	public final void setContractpartnerTransport(final ContractpartnerTransport contractpartnerTransport) {
		this.contractpartnerTransport = contractpartnerTransport;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((this.contractpartnerTransport == null) ? 0 : this.contractpartnerTransport.hashCode());
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
		final AbstractContractpartnerResponse other = (AbstractContractpartnerResponse) obj;
		if (this.contractpartnerTransport == null) {
			if (other.contractpartnerTransport != null) {
				return false;
			}
		} else if (!this.contractpartnerTransport.equals(other.contractpartnerTransport)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("AbstractContractpartnerResponse [contractpartnerTransport=");
		builder.append(this.contractpartnerTransport);
		builder.append("]");
		return builder.toString();
	}

}
