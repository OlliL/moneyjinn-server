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

import com.fasterxml.jackson.annotation.JsonProperty;
import org.laladev.moneyjinn.core.rest.model.transport.MoneyflowSplitEntryTransport;
import org.laladev.moneyjinn.core.rest.model.transport.MoneyflowTransport;

import com.fasterxml.jackson.annotation.JsonRootName;

import java.util.List;

@JsonRootName("showEditMoneyflowResponse")
public class ShowEditMoneyflowResponse extends AbstractEditMoneyflowResponse {
	@JsonProperty("moneyflowSplitEntryTransport")
	private List<MoneyflowSplitEntryTransport> moneyflowSplitEntryTransports;	private MoneyflowTransport moneyflowTransport;

	public final List<MoneyflowSplitEntryTransport> getMoneyflowSplitEntryTransports() {
		return moneyflowSplitEntryTransports;
	}

	public final void setMoneyflowSplitEntryTransports(List<MoneyflowSplitEntryTransport> moneyflowSplitEntryTransports) {
		this.moneyflowSplitEntryTransports = moneyflowSplitEntryTransports;
	}

	public final MoneyflowTransport getMoneyflowTransport() {
		return this.moneyflowTransport;
	}

	public final void setMoneyflowTransport(final MoneyflowTransport moneyflowTransport) {
		this.moneyflowTransport = moneyflowTransport;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((moneyflowSplitEntryTransports == null) ? 0 : moneyflowSplitEntryTransports.hashCode());
		result = prime * result + ((this.moneyflowTransport == null) ? 0 : this.moneyflowTransport.hashCode());
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
		final ShowEditMoneyflowResponse other = (ShowEditMoneyflowResponse) obj;
		if (moneyflowSplitEntryTransports == null) {
			if (other.moneyflowSplitEntryTransports != null)
				return false;
		} else if (!moneyflowSplitEntryTransports.equals(other.moneyflowSplitEntryTransports))
			return false;
		if (this.moneyflowTransport == null) {
			if (other.moneyflowTransport != null) {
				return false;
			}
		} else if (!this.moneyflowTransport.equals(other.moneyflowTransport)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ShowEditMoneyflowResponse [");
		builder.append("moneyflowSplitEntryTransports=");
		builder.append(moneyflowSplitEntryTransports);
		builder.append(", moneyflowTransport=");
		builder.append(moneyflowTransport);
		builder.append(", getCapitalsourceTransports()=");
		builder.append(getCapitalsourceTransports());
		builder.append(", getContractpartnerTransports()=");
		builder.append(getContractpartnerTransports());
		builder.append(", getPostingAccountTransports()=");
		builder.append(getPostingAccountTransports());
		builder.append("]");
		return builder.toString();
	}

}
