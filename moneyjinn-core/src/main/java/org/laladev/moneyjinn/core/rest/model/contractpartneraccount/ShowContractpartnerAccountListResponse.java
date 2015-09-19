package org.laladev.moneyjinn.core.rest.model.contractpartneraccount;

//
//Copyright (c) 2015 Oliver Lehmann <oliver@laladev.org>
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

import java.util.List;

import org.laladev.moneyjinn.core.rest.model.AbstractResponse;
import org.laladev.moneyjinn.core.rest.model.transport.ContractpartnerAccountTransport;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("showContractpartnerAccountListResponse")
public class ShowContractpartnerAccountListResponse extends AbstractResponse {
	@JsonProperty("contractpartnerAccountTransport")
	private List<ContractpartnerAccountTransport> contractpartnerAccountTransports;
	private String contractpartnerName;

	public final List<ContractpartnerAccountTransport> getContractpartnerAccountTransports() {
		return this.contractpartnerAccountTransports;
	}

	public final void setContractpartnerAccountTransports(
			final List<ContractpartnerAccountTransport> contractpartnerAccountTransports) {
		this.contractpartnerAccountTransports = contractpartnerAccountTransports;
	}

	public final String getContractpartnerName() {
		return this.contractpartnerName;
	}

	public final void setContractpartnerName(final String contractpartnerName) {
		this.contractpartnerName = contractpartnerName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((this.contractpartnerAccountTransports == null) ? 0
				: this.contractpartnerAccountTransports.hashCode());
		result = prime * result + ((this.contractpartnerName == null) ? 0 : this.contractpartnerName.hashCode());
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
		final ShowContractpartnerAccountListResponse other = (ShowContractpartnerAccountListResponse) obj;
		if (this.contractpartnerAccountTransports == null) {
			if (other.contractpartnerAccountTransports != null) {
				return false;
			}
		} else if (!this.contractpartnerAccountTransports.equals(other.contractpartnerAccountTransports)) {
			return false;
		}
		if (this.contractpartnerName == null) {
			if (other.contractpartnerName != null) {
				return false;
			}
		} else if (!this.contractpartnerName.equals(other.contractpartnerName)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("ShowContractpartnerAccountListResponse [contractpartnerAccountTransports=");
		builder.append(this.contractpartnerAccountTransports);
		builder.append(", contractpartnerName=");
		builder.append(this.contractpartnerName);
		builder.append("]");
		return builder.toString();
	}

}