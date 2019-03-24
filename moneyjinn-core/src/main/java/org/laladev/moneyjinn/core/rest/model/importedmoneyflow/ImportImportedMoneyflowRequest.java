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
// $Id: CreateImportedMoneyflowRequest.java,v 1.1 2015/02/08 00:26:04 olivleh1 Exp $
//
package org.laladev.moneyjinn.core.rest.model.importedmoneyflow;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.laladev.moneyjinn.core.rest.model.AbstractRequest;
import org.laladev.moneyjinn.core.rest.model.transport.ImportedMoneyflowTransport;
import org.laladev.moneyjinn.core.rest.model.transport.MoneyflowSplitEntryTransport;

@XmlRootElement(name = "importImportedMoneyflowRequest")
public class ImportImportedMoneyflowRequest extends AbstractRequest {
	private ImportedMoneyflowTransport importedMoneyflowTransport;
	@XmlElement(name = "insertMoneyflowSplitEntryTransport")
	public List<MoneyflowSplitEntryTransport> insertMoneyflowSplitEntryTransports;

	public final ImportedMoneyflowTransport getImportedMoneyflowTransport() {
		return this.importedMoneyflowTransport;
	}

	public final void setImportedMoneyflowTransport(final ImportedMoneyflowTransport importedMoneyflowTransport) {
		this.importedMoneyflowTransport = importedMoneyflowTransport;
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
				+ ((this.importedMoneyflowTransport == null) ? 0 : this.importedMoneyflowTransport.hashCode());
		result = prime * result + ((this.insertMoneyflowSplitEntryTransports == null) ? 0
				: this.insertMoneyflowSplitEntryTransports.hashCode());
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
		final ImportImportedMoneyflowRequest other = (ImportImportedMoneyflowRequest) obj;
		if (this.importedMoneyflowTransport == null) {
			if (other.importedMoneyflowTransport != null) {
				return false;
			}
		} else if (!this.importedMoneyflowTransport.equals(other.importedMoneyflowTransport)) {
			return false;
		}
		if (this.insertMoneyflowSplitEntryTransports == null) {
			if (other.insertMoneyflowSplitEntryTransports != null) {
				return false;
			}
		} else if (!this.insertMoneyflowSplitEntryTransports.equals(other.insertMoneyflowSplitEntryTransports)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("ImportImportedMoneyflowRequest [importedMoneyflowTransport=");
		builder.append(this.importedMoneyflowTransport);
		builder.append(", insertMoneyflowSplitEntryTransports=");
		builder.append(this.insertMoneyflowSplitEntryTransports);
		builder.append("]");
		return builder.toString();
	}

}
