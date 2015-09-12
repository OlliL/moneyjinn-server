package org.laladev.moneyjinn.core.rest.model.moneyflow;

import java.util.List;

import org.laladev.moneyjinn.core.rest.model.ValidationResponse;
import org.laladev.moneyjinn.core.rest.model.transport.CapitalsourceTransport;
import org.laladev.moneyjinn.core.rest.model.transport.ContractpartnerTransport;
import org.laladev.moneyjinn.core.rest.model.transport.PostingAccountTransport;
import org.laladev.moneyjinn.core.rest.model.transport.PreDefMoneyflowTransport;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class AbstractAddMoneyflowResponse extends ValidationResponse {
	@JsonProperty("preDefMoneyflowTransport")
	private List<PreDefMoneyflowTransport> preDefMoneyflowTransports;
	@JsonProperty("capitalsourceTransport")
	private List<CapitalsourceTransport> capitalsourceTransports;
	@JsonProperty("contractpartnerTransport")
	private List<ContractpartnerTransport> contractpartnerTransports;
	@JsonProperty("postingAccountTransport")
	private List<PostingAccountTransport> postingAccountTransports;
	private Integer settingNumberOfFreeMoneyflows;

	public final List<PreDefMoneyflowTransport> getPreDefMoneyflowTransports() {
		return this.preDefMoneyflowTransports;
	}

	public final void setPreDefMoneyflowTransports(final List<PreDefMoneyflowTransport> preDefMoneyflowTransports) {
		this.preDefMoneyflowTransports = preDefMoneyflowTransports;
	}

	public final List<CapitalsourceTransport> getCapitalsourceTransports() {
		return this.capitalsourceTransports;
	}

	public final void setCapitalsourceTransports(final List<CapitalsourceTransport> capitalsourceTransports) {
		this.capitalsourceTransports = capitalsourceTransports;
	}

	public final List<ContractpartnerTransport> getContractpartnerTransports() {
		return this.contractpartnerTransports;
	}

	public final void setContractpartnerTransports(final List<ContractpartnerTransport> contractpartnerTransports) {
		this.contractpartnerTransports = contractpartnerTransports;
	}

	public final List<PostingAccountTransport> getPostingAccountTransports() {
		return this.postingAccountTransports;
	}

	public final void setPostingAccountTransports(final List<PostingAccountTransport> postingAccountTransports) {
		this.postingAccountTransports = postingAccountTransports;
	}

	public final Integer getSettingNumberOfFreeMoneyflows() {
		return this.settingNumberOfFreeMoneyflows;
	}

	public final void setSettingNumberOfFreeMoneyflows(final Integer settingNumberOfFreeMoneyflows) {
		this.settingNumberOfFreeMoneyflows = settingNumberOfFreeMoneyflows;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((this.capitalsourceTransports == null) ? 0 : this.capitalsourceTransports.hashCode());
		result = prime * result
				+ ((this.contractpartnerTransports == null) ? 0 : this.contractpartnerTransports.hashCode());
		result = prime * result
				+ ((this.postingAccountTransports == null) ? 0 : this.postingAccountTransports.hashCode());
		result = prime * result
				+ ((this.preDefMoneyflowTransports == null) ? 0 : this.preDefMoneyflowTransports.hashCode());
		result = prime * result
				+ ((this.settingNumberOfFreeMoneyflows == null) ? 0 : this.settingNumberOfFreeMoneyflows.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		final AbstractAddMoneyflowResponse other = (AbstractAddMoneyflowResponse) obj;
		if (this.capitalsourceTransports == null) {
			if (other.capitalsourceTransports != null) {
				return false;
			}
		} else if (!this.capitalsourceTransports.equals(other.capitalsourceTransports)) {
			return false;
		}
		if (this.contractpartnerTransports == null) {
			if (other.contractpartnerTransports != null) {
				return false;
			}
		} else if (!this.contractpartnerTransports.equals(other.contractpartnerTransports)) {
			return false;
		}
		if (this.postingAccountTransports == null) {
			if (other.postingAccountTransports != null) {
				return false;
			}
		} else if (!this.postingAccountTransports.equals(other.postingAccountTransports)) {
			return false;
		}
		if (this.preDefMoneyflowTransports == null) {
			if (other.preDefMoneyflowTransports != null) {
				return false;
			}
		} else if (!this.preDefMoneyflowTransports.equals(other.preDefMoneyflowTransports)) {
			return false;
		}
		if (this.settingNumberOfFreeMoneyflows == null) {
			if (other.settingNumberOfFreeMoneyflows != null) {
				return false;
			}
		} else if (!this.settingNumberOfFreeMoneyflows.equals(other.settingNumberOfFreeMoneyflows)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("AbstractAddMoneyflowResponse [preDefMoneyflowTransports=");
		builder.append(this.preDefMoneyflowTransports);
		builder.append(", capitalsourceTransports=");
		builder.append(this.capitalsourceTransports);
		builder.append(", contractpartnerTransports=");
		builder.append(this.contractpartnerTransports);
		builder.append(", postingAccountTransports=");
		builder.append(this.postingAccountTransports);
		builder.append(", settingNumberOfFreeMoneyflows=");
		builder.append(this.settingNumberOfFreeMoneyflows);
		builder.append("]");
		return builder.toString();
	}

}
