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

package org.laladev.moneyjinn.businesslogic.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.laladev.moneyjinn.businesslogic.model.access.User;
import org.laladev.moneyjinn.businesslogic.model.capitalsource.Capitalsource;

public class PreDefMoneyflow extends AbstractEntity<PreDefMoneyflowID> {
	private static final long serialVersionUID = 1L;
	private User user;
	private BigDecimal amount;
	private Capitalsource capitalsource;
	private Contractpartner contractpartner;
	private String comment;
	private LocalDate creationDate;
	private boolean onceAMonth;
	private LocalDate lastUsedDate;
	private PostingAccount postingAccount;

	public final User getUser() {
		return this.user;
	}

	public final void setUser(final User user) {
		this.user = user;
	}

	public final BigDecimal getAmount() {
		return this.amount;
	}

	public final void setAmount(final BigDecimal amount) {
		this.amount = amount;
	}

	public final Capitalsource getCapitalsource() {
		return this.capitalsource;
	}

	public final void setCapitalsource(final Capitalsource capitalsource) {
		this.capitalsource = capitalsource;
	}

	public final Contractpartner getContractpartner() {
		return this.contractpartner;
	}

	public final void setContractpartner(final Contractpartner contractpartner) {
		this.contractpartner = contractpartner;
	}

	public final String getComment() {
		return this.comment;
	}

	public final void setComment(final String comment) {
		this.comment = comment;
	}

	public final boolean isOnceAMonth() {
		return this.onceAMonth;
	}

	public final void setOnceAMonth(final boolean onceAMonth) {
		this.onceAMonth = onceAMonth;
	}

	public final PostingAccount getPostingAccount() {
		return this.postingAccount;
	}

	public final void setPostingAccount(final PostingAccount postingAccount) {
		this.postingAccount = postingAccount;
	}

	public final LocalDate getCreationDate() {
		return this.creationDate;
	}

	public final void setCreationDate(final LocalDate creationDate) {
		this.creationDate = creationDate;
	}

	public final LocalDate getLastUsedDate() {
		return this.lastUsedDate;
	}

	public final void setLastUsedDate(final LocalDate lastUsedDate) {
		this.lastUsedDate = lastUsedDate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((this.amount == null) ? 0 : this.amount.hashCode());
		result = prime * result + ((this.capitalsource == null) ? 0 : this.capitalsource.hashCode());
		result = prime * result + ((this.comment == null) ? 0 : this.comment.hashCode());
		result = prime * result + ((this.contractpartner == null) ? 0 : this.contractpartner.hashCode());
		result = prime * result + ((this.creationDate == null) ? 0 : this.creationDate.hashCode());
		result = prime * result + ((this.lastUsedDate == null) ? 0 : this.lastUsedDate.hashCode());
		result = prime * result + (this.onceAMonth ? 1231 : 1237);
		result = prime * result + ((this.postingAccount == null) ? 0 : this.postingAccount.hashCode());
		result = prime * result + ((this.user == null) ? 0 : this.user.hashCode());
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
		final PreDefMoneyflow other = (PreDefMoneyflow) obj;
		if (this.amount == null) {
			if (other.amount != null) {
				return false;
			}
		} else if (!this.amount.equals(other.amount)) {
			return false;
		}
		if (this.capitalsource == null) {
			if (other.capitalsource != null) {
				return false;
			}
		} else if (!this.capitalsource.equals(other.capitalsource)) {
			return false;
		}
		if (this.comment == null) {
			if (other.comment != null) {
				return false;
			}
		} else if (!this.comment.equals(other.comment)) {
			return false;
		}
		if (this.contractpartner == null) {
			if (other.contractpartner != null) {
				return false;
			}
		} else if (!this.contractpartner.equals(other.contractpartner)) {
			return false;
		}
		if (this.creationDate == null) {
			if (other.creationDate != null) {
				return false;
			}
		} else if (!this.creationDate.equals(other.creationDate)) {
			return false;
		}
		if (this.lastUsedDate == null) {
			if (other.lastUsedDate != null) {
				return false;
			}
		} else if (!this.lastUsedDate.equals(other.lastUsedDate)) {
			return false;
		}
		if (this.onceAMonth != other.onceAMonth) {
			return false;
		}
		if (this.postingAccount == null) {
			if (other.postingAccount != null) {
				return false;
			}
		} else if (!this.postingAccount.equals(other.postingAccount)) {
			return false;
		}
		if (this.user == null) {
			if (other.user != null) {
				return false;
			}
		} else if (!this.user.equals(other.user)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("PreDefMoneyflow [user=");
		builder.append(this.user);
		builder.append(", amount=");
		builder.append(this.amount);
		builder.append(", capitalsource=");
		builder.append(this.capitalsource);
		builder.append(", contractpartner=");
		builder.append(this.contractpartner);
		builder.append(", comment=");
		builder.append(this.comment);
		builder.append(", creationDate=");
		builder.append(this.creationDate);
		builder.append(", onceAMonth=");
		builder.append(this.onceAMonth);
		builder.append(", lastUsedDate=");
		builder.append(this.lastUsedDate);
		builder.append(", postingAccount=");
		builder.append(this.postingAccount);
		builder.append(", getId()=");
		builder.append(this.getId());
		builder.append("]");
		return builder.toString();
	}

}
