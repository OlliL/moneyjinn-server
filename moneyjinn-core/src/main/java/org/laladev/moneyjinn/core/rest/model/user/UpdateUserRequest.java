package org.laladev.moneyjinn.core.rest.model.user;

import org.laladev.moneyjinn.core.rest.model.transport.AccessRelationTransport;
import org.laladev.moneyjinn.core.rest.model.transport.UserTransport;

import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("updateUserRequest")
public class UpdateUserRequest {
	private UserTransport userTransport;
	private AccessRelationTransport accessRelationTransport;

	public final UserTransport getUserTransport() {
		return userTransport;
	}

	public final void setUserTransport(final UserTransport userTransport) {
		this.userTransport = userTransport;
	}

	public AccessRelationTransport getAccessRelationTransport() {
		return accessRelationTransport;
	}

	public void setAccessRelationTransport(final AccessRelationTransport accessRelationTransport) {
		this.accessRelationTransport = accessRelationTransport;
	}

}
