package org.laladev.moneyjinn.core.rest.model.contractpartner;

import java.util.List;

import org.laladev.moneyjinn.core.rest.model.AbstractResponse;
import org.laladev.moneyjinn.core.rest.model.transport.ContractpartnerTransport;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("showContractpartnerListResponse")
public class ShowContractpartnerListResponse extends AbstractResponse {
	private List<Character> initials;
	@JsonProperty("contractpartnerTransport")
	private List<ContractpartnerTransport> contractpartnerTransports;
	private boolean currentlyValid;

	public final List<Character> getInitials() {
		return this.initials;
	}

	public final void setInitials(final List<Character> initials) {
		this.initials = initials;
	}

	public final List<ContractpartnerTransport> getContractpartnerTransports() {
		return this.contractpartnerTransports;
	}

	public final void setContractpartnerTransports(final List<ContractpartnerTransport> contractpartnerTransports) {
		this.contractpartnerTransports = contractpartnerTransports;
	}

	public final boolean isCurrentlyValid() {
		return this.currentlyValid;
	}

	public final void setCurrentlyValid(final boolean currentlyValid) {
		this.currentlyValid = currentlyValid;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((this.contractpartnerTransports == null) ? 0 : this.contractpartnerTransports.hashCode());
		result = prime * result + (this.currentlyValid ? 1231 : 1237);
		result = prime * result + ((this.initials == null) ? 0 : this.initials.hashCode());
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
		final ShowContractpartnerListResponse other = (ShowContractpartnerListResponse) obj;
		if (this.contractpartnerTransports == null) {
			if (other.contractpartnerTransports != null) {
				return false;
			}
		} else if (!this.contractpartnerTransports.equals(other.contractpartnerTransports)) {
			return false;
		}
		if (this.currentlyValid != other.currentlyValid) {
			return false;
		}
		if (this.initials == null) {
			if (other.initials != null) {
				return false;
			}
		} else if (!this.initials.equals(other.initials)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("ShowContractpartnerListResponse [initials=");
		builder.append(this.initials);
		builder.append(", contractpartnerTransports=");
		builder.append(this.contractpartnerTransports);
		builder.append(", currentlyValid=");
		builder.append(this.currentlyValid);
		builder.append("]");
		return builder.toString();
	}

}
