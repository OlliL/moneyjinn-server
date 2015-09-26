package org.laladev.moneyjinn.businesslogic.model;

public class PostingAccount extends AbstractEntity<PostingAccountID> {
	private static final long serialVersionUID = 1L;
	private String name;

	public PostingAccount() {
	}

	public PostingAccount(final PostingAccountID postingAccountID) {
		super.setId(postingAccountID);
	}

	public PostingAccount(final PostingAccountID postingAccountID, final String name) {
		super.setId(postingAccountID);
		this.name = name;
	}

	public final String getName() {
		return this.name;
	}

	public final void setName(final String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
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
		final PostingAccount other = (PostingAccount) obj;
		if (this.name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!this.name.equals(other.name)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("PostingAccount [name=");
		builder.append(this.name);
		builder.append(", getId()=");
		builder.append(this.getId());
		builder.append("]");
		return builder.toString();
	}

}
