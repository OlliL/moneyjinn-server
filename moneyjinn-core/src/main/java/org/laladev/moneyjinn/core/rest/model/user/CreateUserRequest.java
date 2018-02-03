package org.laladev.moneyjinn.core.rest.model.user;

import org.laladev.moneyjinn.core.rest.model.AbstractRequest;
import org.laladev.moneyjinn.core.rest.model.transport.UserTransport;
import org.laladev.moneyjinn.core.rest.model.user.transport.AccessRelationTransport;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "createUserRequest")
public class CreateUserRequest extends AbstractRequest {
	private UserTransport userTransport;
	private AccessRelationTransport accessRelationTransport;

	public final UserTransport getUserTransport() {
		return this.userTransport;
	}

	public final void setUserTransport(final UserTransport userTransport) {
		this.userTransport = userTransport;
	}

	public AccessRelationTransport getAccessRelationTransport() {
		return this.accessRelationTransport;
	}

	public void setAccessRelationTransport(final AccessRelationTransport accessRelationTransport) {
		this.accessRelationTransport = accessRelationTransport;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((this.accessRelationTransport == null) ? 0 : this.accessRelationTransport.hashCode());
		result = prime * result + ((this.userTransport == null) ? 0 : this.userTransport.hashCode());
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
		final CreateUserRequest other = (CreateUserRequest) obj;
		if (this.accessRelationTransport == null) {
			if (other.accessRelationTransport != null) {
				return false;
			}
		} else if (!this.accessRelationTransport.equals(other.accessRelationTransport)) {
			return false;
		}
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
