package org.laladev.moneyjinn.core.rest.model.predefmoneyflow;

import java.util.List;

import org.laladev.moneyjinn.core.rest.model.ValidationResponse;
import org.laladev.moneyjinn.core.rest.model.transport.CapitalsourceTransport;
import org.laladev.moneyjinn.core.rest.model.transport.ContractpartnerTransport;
import org.laladev.moneyjinn.core.rest.model.transport.PostingAccountTransport;

import javax.xml.bind.annotation.XmlElement;

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

public abstract class AbstractCreatePreDefMoneyflowResponse extends ValidationResponse {
	@XmlElement(name = "capitalsourceTransport")
	private List<CapitalsourceTransport> capitalsourceTransports;
	@XmlElement(name = "contractpartnerTransport")
	private List<ContractpartnerTransport> contractpartnerTransports;
	@XmlElement(name = "postingAccountTransport")
	private List<PostingAccountTransport> postingAccountTransports;

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((this.capitalsourceTransports == null) ? 0 : this.capitalsourceTransports.hashCode());
		result = prime * result
				+ ((this.contractpartnerTransports == null) ? 0 : this.contractpartnerTransports.hashCode());
		result = prime * result
				+ ((this.postingAccountTransports == null) ? 0 : this.postingAccountTransports.hashCode());
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
		final AbstractCreatePreDefMoneyflowResponse other = (AbstractCreatePreDefMoneyflowResponse) obj;
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
		return true;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("AbstractCreatePreDefMoneyflowResponse [capitalsourceTransports=");
		builder.append(this.capitalsourceTransports);
		builder.append(", contractpartnerTransports=");
		builder.append(this.contractpartnerTransports);
		builder.append(", postingAccountTransports=");
		builder.append(this.postingAccountTransports);
		builder.append(", getResult()=");
		builder.append(this.getResult());
		builder.append(", getValidationItemTransports()=");
		builder.append(this.getValidationItemTransports());
		builder.append(", getErrorResponse()=");
		builder.append(this.getErrorResponse());
		builder.append("]");
		return builder.toString();
	}

}
