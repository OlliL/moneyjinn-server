package org.laladev.moneyjinn.core.rest.model.user;

import org.laladev.moneyjinn.core.rest.model.transport.UserTransport;

public abstract class AbstractShowUserResponse extends AbstractUpdateUserResponse {
	private UserTransport userTransport;

	public final UserTransport getUserTransport() {
		return userTransport;
	}

	public final void setUserTransport(final UserTransport userTransport) {
		this.userTransport = userTransport;
	}

}
