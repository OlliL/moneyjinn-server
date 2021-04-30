//
//Copyright (c) 2021 Oliver Lehmann <lehmann@ans-netz.de>
//All rights reserved.
//
//Redistribution and use in source and binary forms, with or without
//modification, are permitted provided that the following conditions
//are met:
//1. Redistributions of source code must retain the above copyright
//notice, this list of conditions and the following disclaimer
//2. Redistributions in binary form must reproduce the above copyright
//notice, this list of conditions and the following disclaimer in the
//documentation and/or other materials provided with the distribution.
//
//THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS'' AND
//ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
//IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
//ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE LIABLE
//FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
//DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
//OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
//HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
//LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
//OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
//SUCH DAMAGE.
//

package org.laladev.moneyjinn.core.rest.model.moneyflow;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.laladev.moneyjinn.core.rest.model.transport.MoneyflowSplitEntryTransport;
import org.laladev.moneyjinn.core.rest.model.transport.MoneyflowTransport;

@XmlRootElement(name = "searchMoneyflowsByAmountResponse")
public class SearchMoneyflowsByAmountResponse {
	@XmlElement(name = "moneyflowTransport")
	private List<MoneyflowTransport> moneyflowTransports;
	@XmlElement(name = "moneyflowSplitEntryTransport")
	private List<MoneyflowSplitEntryTransport> moneyflowSplitEntryTransports;

	public final List<MoneyflowTransport> getMoneyflowTransports() {
		return this.moneyflowTransports;
	}

	public final void setMoneyflowTransports(final List<MoneyflowTransport> moneyflowTransports) {
		this.moneyflowTransports = moneyflowTransports;
	}

	public final List<MoneyflowSplitEntryTransport> getMoneyflowSplitEntryTransports() {
		return this.moneyflowSplitEntryTransports;
	}

	public final void setMoneyflowSplitEntryTransports(
			final List<MoneyflowSplitEntryTransport> moneyflowSplitEntryTransports) {
		this.moneyflowSplitEntryTransports = moneyflowSplitEntryTransports;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((this.moneyflowSplitEntryTransports == null) ? 0 : this.moneyflowSplitEntryTransports.hashCode());
		result = prime * result + ((this.moneyflowTransports == null) ? 0 : this.moneyflowTransports.hashCode());
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
		final SearchMoneyflowsByAmountResponse other = (SearchMoneyflowsByAmountResponse) obj;
		if (this.moneyflowSplitEntryTransports == null) {
			if (other.moneyflowSplitEntryTransports != null) {
				return false;
			}
		} else if (!this.moneyflowSplitEntryTransports.equals(other.moneyflowSplitEntryTransports)) {
			return false;
		}
		if (this.moneyflowTransports == null) {
			if (other.moneyflowTransports != null) {
				return false;
			}
		} else if (!this.moneyflowTransports.equals(other.moneyflowTransports)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("SearchMoneyflowsByAmountResponse [moneyflowTransports=");
		builder.append(this.moneyflowTransports);
		builder.append(", moneyflowSplitEntryTransports=");
		builder.append(this.moneyflowSplitEntryTransports);
		builder.append("]");
		return builder.toString();
	}

}
