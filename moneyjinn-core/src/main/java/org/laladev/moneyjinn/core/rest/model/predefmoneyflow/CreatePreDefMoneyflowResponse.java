package org.laladev.moneyjinn.core.rest.model.predefmoneyflow;

import java.util.Objects;

import javax.xml.bind.annotation.XmlRootElement;

import org.laladev.moneyjinn.core.rest.model.ValidationResponse;

//
//Copyright (c) 2015 Oliver Lehmann <lehmann@ans-netz.de>
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

@XmlRootElement(name = "createPreDefMoneyflowResponse")
public class CreatePreDefMoneyflowResponse extends ValidationResponse {
	private Long preDefMoneyflowId;

	public Long getPreDefMoneyflowId() {
		return this.preDefMoneyflowId;
	}

	public void setPreDefMoneyflowId(final Long preDefMoneyflowId) {
		this.preDefMoneyflowId = preDefMoneyflowId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(this.preDefMoneyflowId);
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
		final CreatePreDefMoneyflowResponse other = (CreatePreDefMoneyflowResponse) obj;
		return Objects.equals(this.preDefMoneyflowId, other.preDefMoneyflowId);
	}

	@Override
	public String toString() {
		return "CreatePreDefMoneyflowResponse [preDefMoneyflowId=" + this.preDefMoneyflowId + "]";
	}

}
