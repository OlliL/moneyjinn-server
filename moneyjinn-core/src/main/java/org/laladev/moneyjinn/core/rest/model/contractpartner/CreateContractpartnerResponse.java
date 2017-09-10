package org.laladev.moneyjinn.core.rest.model.contractpartner;

import org.laladev.moneyjinn.core.rest.model.ValidationResponse;

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

import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("createContractpartnerResponse")
public class CreateContractpartnerResponse extends ValidationResponse {
	private Long contractpartnerId;

	public final Long getContractpartnerId() {
		return this.contractpartnerId;
	}

	public final void setContractpartnerId(final Long contractpartnerId) {
		this.contractpartnerId = contractpartnerId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((this.contractpartnerId == null) ? 0 : this.contractpartnerId.hashCode());
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
		final CreateContractpartnerResponse other = (CreateContractpartnerResponse) obj;
		if (this.contractpartnerId == null) {
			if (other.contractpartnerId != null) {
				return false;
			}
		} else if (!this.contractpartnerId.equals(other.contractpartnerId)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("CreateContractpartnerResponse [contractpartnerId=");
		builder.append(this.contractpartnerId);
		builder.append("]");
		return builder.toString();
	}

}
