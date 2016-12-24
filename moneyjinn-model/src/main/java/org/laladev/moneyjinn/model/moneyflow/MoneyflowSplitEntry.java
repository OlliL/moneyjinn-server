//
// Copyright (c) 2016 Oliver Lehmann <oliver@laladev.org>
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

import java.math.BigDecimal;

import org.laladev.moneyjinn.model.AbstractEntity;
import org.laladev.moneyjinn.model.PostingAccount;

public class MoneyflowSplitEntry extends AbstractEntity<MoneyflowSplitEntryID> {
	private static final long serialVersionUID = 1L;
	private MoneyflowID moneyflowId;
	private BigDecimal amount;
	private String comment;
	private PostingAccount postingAccount;

	public final MoneyflowID getMoneyflowId() {
		return this.moneyflowId;
	}

	public final void setMoneyflowId(final MoneyflowID moneyflowId) {
		this.moneyflowId = moneyflowId;
	}

	public final BigDecimal getAmount() {
		return this.amount;
	}

	public final void setAmount(final BigDecimal amount) {
		this.amount = amount;
	}

	public final String getComment() {
		return this.comment;
	}

	public final void setComment(final String comment) {
		this.comment = comment;
	}

	public final PostingAccount getPostingAccount() {
		return this.postingAccount;
	}

	public final void setPostingAccount(final PostingAccount postingAccount) {
		this.postingAccount = postingAccount;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((this.amount == null) ? 0 : this.amount.hashCode());
		result = prime * result + ((this.comment == null) ? 0 : this.comment.hashCode());
		result = prime * result + ((this.moneyflowId == null) ? 0 : this.moneyflowId.hashCode());
		result = prime * result + ((this.postingAccount == null) ? 0 : this.postingAccount.hashCode());
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
		final MoneyflowSplitEntry other = (MoneyflowSplitEntry) obj;
		if (this.amount == null) {
			if (other.amount != null) {
				return false;
			}
		} else if (!this.amount.equals(other.amount)) {
			return false;
		}
		if (this.comment == null) {
			if (other.comment != null) {
				return false;
			}
		} else if (!this.comment.equals(other.comment)) {
			return false;
		}
		if (this.moneyflowId == null) {
			if (other.moneyflowId != null) {
				return false;
			}
		} else if (!this.moneyflowId.equals(other.moneyflowId)) {
			return false;
		}
		if (this.postingAccount == null) {
			if (other.postingAccount != null) {
				return false;
			}
		} else if (!this.postingAccount.equals(other.postingAccount)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("MoneyflowSplitEntry [moneyflowId=");
		builder.append(this.moneyflowId);
		builder.append(", amount=");
		builder.append(this.amount);
		builder.append(", comment=");
		builder.append(this.comment);
		builder.append(", postingAccount=");
		builder.append(this.postingAccount);
		builder.append(", getId()=");
		builder.append(this.getId());
		builder.append("]");
		return builder.toString();
	}

}
