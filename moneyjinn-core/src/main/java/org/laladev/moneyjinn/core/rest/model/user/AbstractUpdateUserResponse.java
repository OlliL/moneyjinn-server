package org.laladev.moneyjinn.core.rest.model.user;

import java.util.List;

import org.laladev.moneyjinn.core.rest.model.user.transport.AccessRelationTransport;

import javax.xml.bind.annotation.XmlElement;

public abstract class AbstractUpdateUserResponse extends AbstractCreateUserResponse {
	@XmlElement(name = "accessRelationTransport")
	private List<AccessRelationTransport> accessRelationTransports;

	public final List<AccessRelationTransport> getAccessRelationTransports() {
		return this.accessRelationTransports;
	}

	public final void setAccessRelationTransports(final List<AccessRelationTransport> accessRelationTransports) {
		this.accessRelationTransports = accessRelationTransports;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((this.accessRelationTransports == null) ? 0 : this.accessRelationTransports.hashCode());
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
		final AbstractUpdateUserResponse other = (AbstractUpdateUserResponse) obj;
		if (this.accessRelationTransports == null) {
			if (other.accessRelationTransports != null) {
				return false;
			}
		} else if (!this.accessRelationTransports.equals(other.accessRelationTransports)) {
			return false;
		}
		return true;
	}

}
