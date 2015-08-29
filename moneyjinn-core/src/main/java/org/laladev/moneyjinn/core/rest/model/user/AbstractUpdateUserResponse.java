package org.laladev.moneyjinn.core.rest.model.user;

import java.util.List;

import org.laladev.moneyjinn.core.rest.model.transport.AccessRelationTransport;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class AbstractUpdateUserResponse extends AbstractCreateUserResponse {
	@JsonProperty("accessRelationTransport")
	private List<AccessRelationTransport> accessRelationTransports;

	public final List<AccessRelationTransport> getAccessRelationTransports() {
		return accessRelationTransports;
	}

	public final void setAccessRelationTransports(final List<AccessRelationTransport> accessRelationTransports) {
		this.accessRelationTransports = accessRelationTransports;
	}

}
