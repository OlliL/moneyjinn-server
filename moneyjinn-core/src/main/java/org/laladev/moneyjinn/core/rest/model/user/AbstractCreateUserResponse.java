package org.laladev.moneyjinn.core.rest.model.user;

import java.util.List;

import org.laladev.moneyjinn.core.rest.model.ValidationResponse;
import org.laladev.moneyjinn.core.rest.model.transport.GroupTransport;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class AbstractCreateUserResponse extends ValidationResponse {
	@JsonProperty("groupTransport")
	private List<GroupTransport> groupTransports;

	public final List<GroupTransport> getGroupTransports() {
		return groupTransports;
	}

	public final void setGroupTransports(final List<GroupTransport> groupTransports) {
		this.groupTransports = groupTransports;
	}

}
