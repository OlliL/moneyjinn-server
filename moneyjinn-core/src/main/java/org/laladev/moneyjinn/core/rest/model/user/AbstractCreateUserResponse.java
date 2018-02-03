package org.laladev.moneyjinn.core.rest.model.user;

import java.util.List;

import org.laladev.moneyjinn.core.rest.model.ValidationResponse;
import org.laladev.moneyjinn.core.rest.model.transport.GroupTransport;

import javax.xml.bind.annotation.XmlElement;

public abstract class AbstractCreateUserResponse extends ValidationResponse {
	@XmlElement(name = "groupTransport")
	private List<GroupTransport> groupTransports;

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
		final AbstractCreateUserResponse other = (AbstractCreateUserResponse) obj;
		if (this.groupTransports == null) {
			if (other.groupTransports != null) {
				return false;
			}
		} else if (!this.groupTransports.equals(other.groupTransports)) {
			return false;
		}
		return true;
	}

}
