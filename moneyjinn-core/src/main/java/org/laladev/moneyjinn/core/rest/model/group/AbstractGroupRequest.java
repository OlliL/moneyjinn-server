package org.laladev.moneyjinn.core.rest.model.group;

import org.laladev.moneyjinn.core.rest.model.AbstractRequest;
import org.laladev.moneyjinn.core.rest.model.transport.GroupTransport;

public abstract class AbstractGroupRequest extends AbstractRequest {
	private GroupTransport groupTransport;

	public final GroupTransport getGroupTransport() {
		return this.groupTransport;
	}

	public final void setGroupTransport(final GroupTransport groupTransport) {
		this.groupTransport = groupTransport;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.groupTransport == null) ? 0 : this.groupTransport.hashCode());
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
		final AbstractGroupRequest other = (AbstractGroupRequest) obj;
		if (this.groupTransport == null) {
			if (other.groupTransport != null) {
				return false;
			}
		} else if (!this.groupTransport.equals(other.groupTransport)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("AbstractGroupRequest [groupTransport=");
		builder.append(this.groupTransport);
		builder.append("]");
		return builder.toString();
	}

}
