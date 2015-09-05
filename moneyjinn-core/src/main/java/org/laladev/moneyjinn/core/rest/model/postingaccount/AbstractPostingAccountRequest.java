package org.laladev.moneyjinn.core.rest.model.postingaccount;

import org.laladev.moneyjinn.core.rest.model.AbstractRequest;
import org.laladev.moneyjinn.core.rest.model.transport.PostingAccountTransport;

public abstract class AbstractPostingAccountRequest extends AbstractRequest {
	private PostingAccountTransport postingAccountTransport;

	public final PostingAccountTransport getPostingAccountTransport() {
		return this.postingAccountTransport;
	}

	public final void setPostingAccountTransport(final PostingAccountTransport postingAccountTransport) {
		this.postingAccountTransport = postingAccountTransport;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((this.postingAccountTransport == null) ? 0 : this.postingAccountTransport.hashCode());
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
		final AbstractPostingAccountRequest other = (AbstractPostingAccountRequest) obj;
		if (this.postingAccountTransport == null) {
			if (other.postingAccountTransport != null) {
				return false;
			}
		} else if (!this.postingAccountTransport.equals(other.postingAccountTransport)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("AbstractPostingAccountRequest [postingAccountTransport=");
		builder.append(this.postingAccountTransport);
		builder.append("]");
		return builder.toString();
	}

}
