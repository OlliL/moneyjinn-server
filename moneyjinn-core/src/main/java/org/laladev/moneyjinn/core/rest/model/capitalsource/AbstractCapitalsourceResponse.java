package org.laladev.moneyjinn.core.rest.model.capitalsource;

import org.laladev.moneyjinn.core.rest.model.AbstractResponse;
import org.laladev.moneyjinn.core.rest.model.transport.CapitalsourceTransport;

public abstract class AbstractCapitalsourceResponse extends AbstractResponse {
	private CapitalsourceTransport capitalsourceTransport;

	public final CapitalsourceTransport getCapitalsourceTransport() {
		return this.capitalsourceTransport;
	}

	public final void setCapitalsourceTransport(final CapitalsourceTransport capitalsourceTransport) {
		this.capitalsourceTransport = capitalsourceTransport;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((this.capitalsourceTransport == null) ? 0 : this.capitalsourceTransport.hashCode());
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
		final AbstractCapitalsourceResponse other = (AbstractCapitalsourceResponse) obj;
		if (this.capitalsourceTransport == null) {
			if (other.capitalsourceTransport != null) {
				return false;
			}
		} else if (!this.capitalsourceTransport.equals(other.capitalsourceTransport)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("AbstractCapitalsourceResponse [capitalsourceTransport=");
		builder.append(this.capitalsourceTransport);
		builder.append("]");
		return builder.toString();
	}

}
