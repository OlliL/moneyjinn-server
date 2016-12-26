//
// Copyright (c) 2015 Oliver Lehmann <oliver@laladev.org>
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions
// are met:
// 1. Redistributions of source code must retain the above copyright
// notice, this list of conditions and the following disclaimer
// 2. Redistributions in binary form must reproduce the above copyright
// notice, this list of conditions and the following disclaimer in the
// documentation and/or other materials provided with the distribution.
//
// THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS'' AND
// ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE LIABLE
// FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
// DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
// OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
// HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
// LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
// OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE.
//

package org.laladev.moneyjinn.core.rest.model.moneyflow;

import java.util.List;

import org.laladev.moneyjinn.core.rest.model.ValidationResponse;
import org.laladev.moneyjinn.core.rest.model.transport.CapitalsourceTransport;
import org.laladev.moneyjinn.core.rest.model.transport.ContractpartnerTransport;
import org.laladev.moneyjinn.core.rest.model.transport.MoneyflowSplitEntryTransport;
import org.laladev.moneyjinn.core.rest.model.transport.PostingAccountTransport;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class AbstractEditMoneyflowResponse extends ValidationResponse {
	@JsonProperty("capitalsourceTransport")
	private List<CapitalsourceTransport> capitalsourceTransports;
	@JsonProperty("contractpartnerTransport")
	private List<ContractpartnerTransport> contractpartnerTransports;
	@JsonProperty("postingAccountTransport")
	private List<PostingAccountTransport> postingAccountTransports;
	@JsonProperty("moneyflowSplitEntryTransport")
	private List<MoneyflowSplitEntryTransport> moneyflowSplitEntryTransports;


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

	
	public final List<MoneyflowSplitEntryTransport> getMoneyflowSplitEntryTransports() {
		return moneyflowSplitEntryTransports;
	}

	public final void setMoneyflowSplitEntryTransports(List<MoneyflowSplitEntryTransport> moneyflowSplitEntryTransports) {
		this.moneyflowSplitEntryTransports = moneyflowSplitEntryTransports;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((capitalsourceTransports == null) ? 0 : capitalsourceTransports.hashCode());
		result = prime * result + ((contractpartnerTransports == null) ? 0 : contractpartnerTransports.hashCode());
		result = prime * result
				+ ((moneyflowSplitEntryTransports == null) ? 0 : moneyflowSplitEntryTransports.hashCode());
		result = prime * result + ((postingAccountTransports == null) ? 0 : postingAccountTransports.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractEditMoneyflowResponse other = (AbstractEditMoneyflowResponse) obj;
		if (capitalsourceTransports == null) {
			if (other.capitalsourceTransports != null)
				return false;
		} else if (!capitalsourceTransports.equals(other.capitalsourceTransports))
			return false;
		if (contractpartnerTransports == null) {
			if (other.contractpartnerTransports != null)
				return false;
		} else if (!contractpartnerTransports.equals(other.contractpartnerTransports))
			return false;
		if (moneyflowSplitEntryTransports == null) {
			if (other.moneyflowSplitEntryTransports != null)
				return false;
		} else if (!moneyflowSplitEntryTransports.equals(other.moneyflowSplitEntryTransports))
			return false;
		if (postingAccountTransports == null) {
			if (other.postingAccountTransports != null)
				return false;
		} else if (!postingAccountTransports.equals(other.postingAccountTransports))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AbstractEditMoneyflowResponse [capitalsourceTransports=");
		builder.append(capitalsourceTransports);
		builder.append(", contractpartnerTransports=");
		builder.append(contractpartnerTransports);
		builder.append(", postingAccountTransports=");
		builder.append(postingAccountTransports);
		builder.append(", moneyflowSplitEntryTransports=");
		builder.append(moneyflowSplitEntryTransports);
		builder.append("]");
		return builder.toString();
	}

}
