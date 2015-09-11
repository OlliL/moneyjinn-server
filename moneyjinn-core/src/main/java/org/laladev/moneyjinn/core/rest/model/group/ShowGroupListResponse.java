package org.laladev.moneyjinn.core.rest.model.group;

import java.util.List;
import java.util.Set;

import org.laladev.moneyjinn.core.rest.model.AbstractResponse;
import org.laladev.moneyjinn.core.rest.model.transport.GroupTransport;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("showGroupListResponse")
public class ShowGroupListResponse extends AbstractResponse {
	private Set<Character> initials;
	@JsonProperty("groupTransport")
	private List<GroupTransport> groupTransports;

	public final Set<Character> getInitials() {
		return this.initials;
	}

	public final void setInitials(final Set<Character> initials) {
		this.initials = initials;
	}

	public final List<GroupTransport> getGroupTransports() {
		return this.groupTransports;
	}

	public final void setGroupTransports(final List<GroupTransport> groupTransports) {
		this.groupTransports = groupTransports;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((this.groupTransports == null) ? 0 : this.groupTransports.hashCode());
		result = prime * result + ((this.initials == null) ? 0 : this.initials.hashCode());
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
		final ShowGroupListResponse other = (ShowGroupListResponse) obj;
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
		return true;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("ShowGroupListResponse [initials=");
		builder.append(this.initials);
		builder.append(", groupTransports=");
		builder.append(this.groupTransports);
		builder.append("]");
		return builder.toString();
	}
}
