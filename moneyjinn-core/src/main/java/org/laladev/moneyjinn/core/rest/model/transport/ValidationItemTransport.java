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

package org.laladev.moneyjinn.core.rest.model.transport;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "validationItemTransport")
public class ValidationItemTransport {
	private Object key;
	private Integer error;
	private List<String> variableArray;

	public final Object getKey() {
		return this.key;
	}

	public final void setKey(final Object key) {
		this.key = key;
	}

	public final Integer getError() {
		return this.error;
	}

	public final void setError(final Integer error) {
		this.error = error;
	}

	public final List<String> getVariableArray() {
		return this.variableArray;
	}

	public final void setVariableArray(final List<String> variableArray) {
		this.variableArray = variableArray;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.error == null) ? 0 : this.error.hashCode());
		result = prime * result + ((this.key == null) ? 0 : this.key.hashCode());
		result = prime * result + ((this.variableArray == null) ? 0 : this.variableArray.hashCode());
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
		final ValidationItemTransport other = (ValidationItemTransport) obj;
		if (this.error == null) {
			if (other.error != null) {
				return false;
			}
		} else if (!this.error.equals(other.error)) {
			return false;
		}
		if (this.key == null) {
			if (other.key != null) {
				return false;
			}
		} else if (!this.key.equals(other.key)) {
			return false;
		}
		if (this.variableArray == null) {
			if (other.variableArray != null) {
				return false;
			}
		} else if (!this.variableArray.equals(other.variableArray)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("ValidationItemTransport [key=");
		builder.append(this.key);
		builder.append(", error=");
		builder.append(this.error);
		builder.append(", variableArray=");
		builder.append(this.variableArray);
		builder.append("]");
		return builder.toString();
	}

}
