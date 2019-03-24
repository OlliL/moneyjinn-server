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

package org.laladev.moneyjinn.core.rest.model.comparedata.transport;

import org.laladev.moneyjinn.core.rest.model.transport.MoneyflowTransport;

public class CompareDataWrongCapitalsourceTransport {
	private MoneyflowTransport moneyflowTransport;
	private CompareDataDatasetTransport compareDataDatasetTransport;

	public final MoneyflowTransport getMoneyflowTransport() {
		return this.moneyflowTransport;
	}

	public final void setMoneyflowTransport(final MoneyflowTransport moneyflowTransport) {
		this.moneyflowTransport = moneyflowTransport;
	}

	public final CompareDataDatasetTransport getCompareDataDatasetTransport() {
		return this.compareDataDatasetTransport;
	}

	public final void setCompareDataDatasetTransport(final CompareDataDatasetTransport compareDataDatasetTransport) {
		this.compareDataDatasetTransport = compareDataDatasetTransport;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((this.compareDataDatasetTransport == null) ? 0 : this.compareDataDatasetTransport.hashCode());
		result = prime * result + ((this.moneyflowTransport == null) ? 0 : this.moneyflowTransport.hashCode());
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
		final CompareDataWrongCapitalsourceTransport other = (CompareDataWrongCapitalsourceTransport) obj;
		if (this.compareDataDatasetTransport == null) {
			if (other.compareDataDatasetTransport != null) {
				return false;
			}
		} else if (!this.compareDataDatasetTransport.equals(other.compareDataDatasetTransport)) {
			return false;
		}
		if (this.moneyflowTransport == null) {
			if (other.moneyflowTransport != null) {
				return false;
			}
		} else if (!this.moneyflowTransport.equals(other.moneyflowTransport)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("CompareDataWrongCapitalsourceTransport [moneyflowTransport=");
		builder.append(this.moneyflowTransport);
		builder.append(", compareDataDatasetTransport=");
		builder.append(this.compareDataDatasetTransport);
		builder.append("]");
		return builder.toString();
	}

}
