package org.laladev.moneyjinn.businesslogic.model;

public class PostingAccount extends AbstractEntity<PostingAccountID> {
	private static final long serialVersionUID = 1L;
	private String name;

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
