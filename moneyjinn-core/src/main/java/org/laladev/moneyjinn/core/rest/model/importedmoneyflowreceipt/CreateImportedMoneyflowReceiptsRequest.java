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

package org.laladev.moneyjinn.core.rest.model.importedmoneyflowreceipt;

import java.util.List;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import org.laladev.moneyjinn.core.rest.model.AbstractRequest;
import org.laladev.moneyjinn.core.rest.model.importedmoneyflowreceipt.transport.ImportedMoneyflowReceiptTransport;

@XmlRootElement(name = "createImportedMoneyflowReceiptsRequest")
public class CreateImportedMoneyflowReceiptsRequest extends AbstractRequest {
	@XmlElement(name = "importedMoneyflowReceiptTransport")
	private List<ImportedMoneyflowReceiptTransport> importedMoneyflowReceiptTransports;

	public final List<ImportedMoneyflowReceiptTransport> getImportedMoneyflowReceiptTransports() {
		return this.importedMoneyflowReceiptTransports;
	}

	public final void setImportedMoneyflowReceiptTransports(
			final List<ImportedMoneyflowReceiptTransport> importedMoneyflowReceiptTransports) {
		this.importedMoneyflowReceiptTransports = importedMoneyflowReceiptTransports;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.importedMoneyflowReceiptTransports == null) ? 0
				: this.importedMoneyflowReceiptTransports.hashCode());
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
		final CreateImportedMoneyflowReceiptsRequest other = (CreateImportedMoneyflowReceiptsRequest) obj;
		if (this.importedMoneyflowReceiptTransports == null) {
			if (other.importedMoneyflowReceiptTransports != null) {
				return false;
			}
		} else if (!this.importedMoneyflowReceiptTransports.equals(other.importedMoneyflowReceiptTransports)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("CreateImportedMoneyflowReceiptsRequest [importedMoneyflowReceiptTransports=");
		builder.append(this.importedMoneyflowReceiptTransports);
		builder.append("]");
		return builder.toString();
	}

}
