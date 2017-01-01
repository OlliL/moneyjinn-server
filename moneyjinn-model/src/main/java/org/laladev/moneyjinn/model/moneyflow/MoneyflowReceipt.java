//
// Copyright (c) 2017 Oliver Lehmann <oliver@laladev.org>
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

package org.laladev.moneyjinn.model.moneyflow;

import java.util.Arrays;

import org.laladev.moneyjinn.model.AbstractEntity;

public class MoneyflowReceipt extends AbstractEntity<MoneyflowReceiptID> {
	private static final long serialVersionUID = 1L;
	private MoneyflowID moneyflowId;
	private byte[] receipt;

	public final MoneyflowID getMoneyflowId() {
		return this.moneyflowId;
	}

	public final void setMoneyflowId(final MoneyflowID moneyflowId) {
		this.moneyflowId = moneyflowId;
	}

	public final byte[] getReceipt() {
		return this.receipt;
	}

	public final void setReceipt(final byte[] receipt) {
		this.receipt = receipt;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((this.moneyflowId == null) ? 0 : this.moneyflowId.hashCode());
		result = prime * result + Arrays.hashCode(this.receipt);
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
		final MoneyflowReceipt other = (MoneyflowReceipt) obj;
		if (this.moneyflowId == null) {
			if (other.moneyflowId != null) {
				return false;
			}
		} else if (!this.moneyflowId.equals(other.moneyflowId)) {
			return false;
		}
		if (!Arrays.equals(this.receipt, other.receipt)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("MoneyflowReceipt [moneyflowId=");
		builder.append(this.moneyflowId);
		builder.append(", receipt=");
		builder.append(Arrays.toString(this.receipt));
		builder.append(", getId()=");
		builder.append(this.getId());
		builder.append("]");
		return builder.toString();
	}

}
