package org.laladev.moneyjinn.core.rest.model.user;

import org.laladev.moneyjinn.core.rest.model.transport.UserTransport;

public abstract class AbstractShowUserResponse extends AbstractUpdateUserResponse {
	private UserTransport userTransport;

	public final UserTransport getUserTransport() {
		return this.userTransport;
	}

	public final void setUserTransport(final UserTransport userTransport) {
		this.userTransport = userTransport;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((this.userTransport == null) ? 0 : this.userTransport.hashCode());
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
		final AbstractShowUserResponse other = (AbstractShowUserResponse) obj;
		if (this.userTransport == null) {
			if (other.userTransport != null) {
				return false;
			}
		} else if (!this.userTransport.equals(other.userTransport)) {
			return false;
		}
		return true;
	}

}
