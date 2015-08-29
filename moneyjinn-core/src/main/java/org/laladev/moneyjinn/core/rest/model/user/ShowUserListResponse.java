package org.laladev.moneyjinn.core.rest.model.user;

import java.util.List;

import org.laladev.moneyjinn.core.rest.model.transport.AccessRelationTransport;
import org.laladev.moneyjinn.core.rest.model.transport.GroupTransport;
import org.laladev.moneyjinn.core.rest.model.transport.UserTransport;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("showUserListResponse")
public class ShowUserListResponse {
	private List<Character> initials;
	@JsonProperty("userTransport")
	private List<UserTransport> userTransports;
	@JsonProperty("groupTransport")
	private List<GroupTransport> groupTransports;
	@JsonProperty("accessRelationTransport")
	private List<AccessRelationTransport> accessRelationTransports;

	public final List<Character> getInitials() {
		return initials;
	}

	public final void setInitials(final List<Character> initials) {
		this.initials = initials;
	}

	public final List<UserTransport> getUserTransports() {
		return userTransports;
	}

	public final void setUserTransports(final List<UserTransport> userTransports) {
		this.userTransports = userTransports;
	}

	public final List<GroupTransport> getGroupTransports() {
		return groupTransports;
	}

	public final void setGroupTransports(final List<GroupTransport> groupTransports) {
		this.groupTransports = groupTransports;
	}

	public final List<AccessRelationTransport> getAccessRelationTransports() {
		return accessRelationTransports;
	}

	public final void setAccessRelationTransports(final List<AccessRelationTransport> accessRelationTransports) {
		this.accessRelationTransports = accessRelationTransports;
	}

}
