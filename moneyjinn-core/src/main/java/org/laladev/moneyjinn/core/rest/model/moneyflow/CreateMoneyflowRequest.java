//
// Copyright (c) 2015 Oliver Lehmann <lehmann@ans-netz.de>
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

import org.laladev.moneyjinn.core.rest.model.AbstractRequest;
import org.laladev.moneyjinn.core.rest.model.transport.MoneyflowSplitEntryTransport;
import org.laladev.moneyjinn.core.rest.model.transport.MoneyflowTransport;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "createMoneyflowRequest")
public class CreateMoneyflowRequest extends AbstractRequest {
	private MoneyflowTransport moneyflowTransport;
	@XmlElement(name = "insertMoneyflowSplitEntryTransport")
	public List<MoneyflowSplitEntryTransport> insertMoneyflowSplitEntryTransports;
	private Long usedPreDefMoneyflowId;
	private Short saveAsPreDefMoneyflow;

	public final MoneyflowTransport getMoneyflowTransport() {
		return this.moneyflowTransport;
	}

	public final void setMoneyflowTransport(final MoneyflowTransport moneyflowTransport) {
		this.moneyflowTransport = moneyflowTransport;
	}

	public final List<MoneyflowSplitEntryTransport> getInsertMoneyflowSplitEntryTransports() {
		return this.insertMoneyflowSplitEntryTransports;
	}

	public final void setInsertMoneyflowSplitEntryTransports(
			final List<MoneyflowSplitEntryTransport> insertMoneyflowSplitEntryTransports) {
		this.insertMoneyflowSplitEntryTransports = insertMoneyflowSplitEntryTransports;
	}

	public final Long getUsedPreDefMoneyflowId() {
		return this.usedPreDefMoneyflowId;
	}

	public final void setUsedPreDefMoneyflowId(final Long usedPreDefMoneyflowId) {
		this.usedPreDefMoneyflowId = usedPreDefMoneyflowId;
	}

	public final Short getSaveAsPreDefMoneyflow() {
		return this.saveAsPreDefMoneyflow;
	}

	public final void setSaveAsPreDefMoneyflow(final Short saveAsPreDefMoneyflow) {
		this.saveAsPreDefMoneyflow = saveAsPreDefMoneyflow;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.insertMoneyflowSplitEntryTransports == null) ? 0
				: this.insertMoneyflowSplitEntryTransports.hashCode());
		result = prime * result + ((this.moneyflowTransport == null) ? 0 : this.moneyflowTransport.hashCode());
		result = prime * result + ((this.saveAsPreDefMoneyflow == null) ? 0 : this.saveAsPreDefMoneyflow.hashCode());
		result = prime * result + ((this.usedPreDefMoneyflowId == null) ? 0 : this.usedPreDefMoneyflowId.hashCode());
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
		final CreateMoneyflowRequest other = (CreateMoneyflowRequest) obj;
		if (this.insertMoneyflowSplitEntryTransports == null) {
			if (other.insertMoneyflowSplitEntryTransports != null) {
				return false;
			}
		} else if (!this.insertMoneyflowSplitEntryTransports.equals(other.insertMoneyflowSplitEntryTransports)) {
			return false;
		}
		if (this.moneyflowTransport == null) {
			if (other.moneyflowTransport != null) {
				return false;
			}
		} else if (!this.moneyflowTransport.equals(other.moneyflowTransport)) {
			return false;
		}
		if (this.saveAsPreDefMoneyflow == null) {
			if (other.saveAsPreDefMoneyflow != null) {
				return false;
			}
		} else if (!this.saveAsPreDefMoneyflow.equals(other.saveAsPreDefMoneyflow)) {
			return false;
		}
		if (this.usedPreDefMoneyflowId == null) {
			if (other.usedPreDefMoneyflowId != null) {
				return false;
			}
		} else if (!this.usedPreDefMoneyflowId.equals(other.usedPreDefMoneyflowId)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("CreateMoneyflowRequest [moneyflowTransport=");
		builder.append(this.moneyflowTransport);
		builder.append(", insertMoneyflowSplitEntryTransports=");
		builder.append(this.insertMoneyflowSplitEntryTransports);
		builder.append(", usedPreDefMoneyflowId=");
		builder.append(this.usedPreDefMoneyflowId);
		builder.append(", saveAsPreDefMoneyflow=");
		builder.append(this.saveAsPreDefMoneyflow);
		builder.append("]");
		return builder.toString();
	}

}
