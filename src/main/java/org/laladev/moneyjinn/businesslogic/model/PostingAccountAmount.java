package org.laladev.moneyjinn.businesslogic.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PostingAccountAmount {
	private PostingAccount postingAccount;
	private LocalDate date;
	private BigDecimal amount;

	public final PostingAccount getPostingAccount() {
		return this.postingAccount;
	}

	public final void setPostingAccount(final PostingAccount postingAccount) {
		this.postingAccount = postingAccount;
	}

	public final LocalDate getDate() {
		return this.date;
	}

	public final void setDate(final LocalDate date) {
		this.date = date;
	}

	public final BigDecimal getAmount() {
		return this.amount;
	}

	public final void setAmount(final BigDecimal amount) {
		this.amount = amount;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.amount == null) ? 0 : this.amount.hashCode());
		result = prime * result + ((this.date == null) ? 0 : this.date.hashCode());
		result = prime * result + ((this.postingAccount == null) ? 0 : this.postingAccount.hashCode());
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
		final PostingAccountAmount other = (PostingAccountAmount) obj;
		if (this.amount == null) {
			if (other.amount != null) {
				return false;
			}
		} else if (!this.amount.equals(other.amount)) {
			return false;
		}
		if (this.date == null) {
			if (other.date != null) {
				return false;
			}
		} else if (!this.date.equals(other.date)) {
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
		builder.append("PostingAccountAmount [postingAccount=");
		builder.append(this.postingAccount);
		builder.append(", date=");
		builder.append(this.date);
		builder.append(", amount=");
		builder.append(this.amount);
		builder.append("]");
		return builder.toString();
	}

}
