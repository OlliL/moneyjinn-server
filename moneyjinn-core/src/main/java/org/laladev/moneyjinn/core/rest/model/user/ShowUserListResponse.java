package org.laladev.moneyjinn.core.rest.model.user;

import java.util.List;

import org.laladev.moneyjinn.core.rest.model.AbstractResponse;
import org.laladev.moneyjinn.core.rest.model.transport.AccessRelationTransport;
import org.laladev.moneyjinn.core.rest.model.transport.GroupTransport;
import org.laladev.moneyjinn.core.rest.model.transport.UserTransport;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("showUserListResponse")
public class ShowUserListResponse extends AbstractResponse {
	private List<Character> initials;
	@JsonProperty("userTransport")
	private List<UserTransport> userTransports;
	@JsonProperty("groupTransport")
	private List<GroupTransport> groupTransports;
	@JsonProperty("accessRelationTransport")
	private List<AccessRelationTransport> accessRelationTransports;

	public final List<Character> getInitials() {
		return this.initials;
	}

	public final void setInitials(final List<Character> initials) {
		this.initials = initials;
	}

	public final List<UserTransport> getUserTransports() {
		return this.userTransports;
	}

	public final void setUserTransports(final List<UserTransport> userTransports) {
		this.userTransports = userTransports;
	}

	public final List<GroupTransport> getGroupTransports() {
		return this.groupTransports;
	}

	public final void setGroupTransports(final List<GroupTransport> groupTransports) {
		this.groupTransports = groupTransports;
	}

	public final List<AccessRelationTransport> getAccessRelationTransports() {
		return this.accessRelationTransports;
	}

	public final void setAccessRelationTransports(final List<AccessRelationTransport> accessRelationTransports) {
		this.accessRelationTransports = accessRelationTransports;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((this.accessRelationTransports == null) ? 0 : this.accessRelationTransports.hashCode());
		result = prime * result + ((this.groupTransports == null) ? 0 : this.groupTransports.hashCode());
		result = prime * result + ((this.initials == null) ? 0 : this.initials.hashCode());
		result = prime * result + ((this.userTransports == null) ? 0 : this.userTransports.hashCode());
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
		final ShowUserListResponse other = (ShowUserListResponse) obj;
		if (this.accessRelationTransports == null) {
			if (other.accessRelationTransports != null) {
				return false;
			}
		} else if (!this.accessRelationTransports.equals(other.accessRelationTransports)) {
			return false;
		}
		if (this.groupTransports == null) {
			if (other.groupTransports != null) {
				return false;
			}
		} else if (!this.groupTransports.equals(other.groupTransports)) {
			return false;
		}
		if (this.initials == null) {
			if (other.initials != null) {
				return false;
			}
		} else if (!this.initials.equals(other.initials)) {
			return false;
		}
		if (this.userTransports == null) {
			if (other.userTransports != null) {
				return false;
			}
		} else if (!this.userTransports.equals(other.userTransports)) {
			return false;
		}
		return true;
	}

}
