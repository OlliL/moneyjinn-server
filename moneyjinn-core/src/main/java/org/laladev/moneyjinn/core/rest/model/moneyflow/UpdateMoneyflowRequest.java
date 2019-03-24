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

@XmlRootElement(name = "updateMoneyflowRequest")
public class UpdateMoneyflowRequest extends AbstractRequest {
	private MoneyflowTransport moneyflowTransport;

	public List<Long> deleteMoneyflowSplitEntryIds;
	@XmlElement(name = "updateMoneyflowSplitEntryTransport")
	public List<MoneyflowSplitEntryTransport> updateMoneyflowSplitEntryTransports;
	@XmlElement(name = "insertMoneyflowSplitEntryTransport")
	public List<MoneyflowSplitEntryTransport> insertMoneyflowSplitEntryTransports;

	public final MoneyflowTransport getMoneyflowTransport() {
		return this.moneyflowTransport;
	}

	public final void setMoneyflowTransport(final MoneyflowTransport moneyflowTransport) {
		this.moneyflowTransport = moneyflowTransport;
	}

	public final List<Long> getDeleteMoneyflowSplitEntryIds() {
		return this.deleteMoneyflowSplitEntryIds;
	}

	public final void setDeleteMoneyflowSplitEntryIds(final List<Long> deleteMoneyflowSplitEntryIds) {
		this.deleteMoneyflowSplitEntryIds = deleteMoneyflowSplitEntryIds;
	}

	public final List<MoneyflowSplitEntryTransport> getUpdateMoneyflowSplitEntryTransports() {
		return this.updateMoneyflowSplitEntryTransports;
	}

	public final void setUpdateMoneyflowSplitEntryTransports(
			final List<MoneyflowSplitEntryTransport> updateMoneyflowSplitEntryTransports) {
		this.updateMoneyflowSplitEntryTransports = updateMoneyflowSplitEntryTransports;
	}

	public final List<MoneyflowSplitEntryTransport> getInsertMoneyflowSplitEntryTransports() {
		return this.insertMoneyflowSplitEntryTransports;
	}

	public final void setInsertMoneyflowSplitEntryTransports(
			final List<MoneyflowSplitEntryTransport> insertMoneyflowSplitEntryTransports) {
		this.insertMoneyflowSplitEntryTransports = insertMoneyflowSplitEntryTransports;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((this.deleteMoneyflowSplitEntryIds == null) ? 0 : this.deleteMoneyflowSplitEntryIds.hashCode());
		result = prime * result + ((this.insertMoneyflowSplitEntryTransports == null) ? 0
				: this.insertMoneyflowSplitEntryTransports.hashCode());
		result = prime * result + ((this.moneyflowTransport == null) ? 0 : this.moneyflowTransport.hashCode());
		result = prime * result + ((this.updateMoneyflowSplitEntryTransports == null) ? 0
				: this.updateMoneyflowSplitEntryTransports.hashCode());
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
		final UpdateMoneyflowRequest other = (UpdateMoneyflowRequest) obj;
		if (this.deleteMoneyflowSplitEntryIds == null) {
			if (other.deleteMoneyflowSplitEntryIds != null) {
				return false;
			}
		} else if (!this.deleteMoneyflowSplitEntryIds.equals(other.deleteMoneyflowSplitEntryIds)) {
			return false;
		}
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
		if (this.updateMoneyflowSplitEntryTransports == null) {
			if (other.updateMoneyflowSplitEntryTransports != null) {
				return false;
			}
		} else if (!this.updateMoneyflowSplitEntryTransports.equals(other.updateMoneyflowSplitEntryTransports)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("UpdateMoneyflowRequest [moneyflowTransport=");
		builder.append(this.moneyflowTransport);
		builder.append(", deleteMoneyflowSplitEntryIds=");
		builder.append(this.deleteMoneyflowSplitEntryIds);
		builder.append(", updateMoneyflowSplitEntryTransports=");
		builder.append(this.updateMoneyflowSplitEntryTransports);
		builder.append(", insertMoneyflowSplitEntryTransports=");
		builder.append(this.insertMoneyflowSplitEntryTransports);
		builder.append("]");
		return builder.toString();
	}

}
