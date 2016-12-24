package org.laladev.moneyjinn.core.rest.model.transport;

import java.math.BigDecimal;

public class MoneyflowSplitEntryTransport {
	private Long id;
	private Long moneyflowid;
	private BigDecimal amount;
	private String comment;
	private Long postingaccountid;
	private String postingaccountname;

	public final Long getId() {
		return this.id;
	}

	public final void setId(final Long id) {
		this.id = id;
	}

	public final Long getMoneyflowid() {
		return this.moneyflowid;
	}

	public final void setMoneyflowid(final Long moneyflowid) {
		this.moneyflowid = moneyflowid;
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

	public final Long getPostingaccountid() {
		return this.postingaccountid;
	}

	public final void setPostingaccountid(final Long postingaccountid) {
		this.postingaccountid = postingaccountid;
	}

	public final String getPostingaccountname() {
		return this.postingaccountname;
	}

	public final void setPostingaccountname(final String postingaccountname) {
		this.postingaccountname = postingaccountname;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.amount == null) ? 0 : this.amount.hashCode());
		result = prime * result + ((this.comment == null) ? 0 : this.comment.hashCode());
		result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
		result = prime * result + ((this.moneyflowid == null) ? 0 : this.moneyflowid.hashCode());
		result = prime * result + ((this.postingaccountid == null) ? 0 : this.postingaccountid.hashCode());
		result = prime * result + ((this.postingaccountname == null) ? 0 : this.postingaccountname.hashCode());
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
		final MoneyflowSplitEntryTransport other = (MoneyflowSplitEntryTransport) obj;
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
		if (this.id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!this.id.equals(other.id)) {
			return false;
		}
		if (this.moneyflowid == null) {
			if (other.moneyflowid != null) {
				return false;
			}
		} else if (!this.moneyflowid.equals(other.moneyflowid)) {
			return false;
		}
		if (this.postingaccountid == null) {
			if (other.postingaccountid != null) {
				return false;
			}
		} else if (!this.postingaccountid.equals(other.postingaccountid)) {
			return false;
		}
		if (this.postingaccountname == null) {
			if (other.postingaccountname != null) {
				return false;
			}
		} else if (!this.postingaccountname.equals(other.postingaccountname)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("MoneyflowSplitEntryTransport [id=");
		builder.append(this.id);
		builder.append(", moneyflowid=");
		builder.append(this.moneyflowid);
		builder.append(", amount=");
		builder.append(this.amount);
		builder.append(", comment=");
		builder.append(this.comment);
		builder.append(", postingaccountid=");
		builder.append(this.postingaccountid);
		builder.append(", postingaccountname=");
		builder.append(this.postingaccountname);
		builder.append("]");
		return builder.toString();
	}

}
