package org.laladev.moneyjinn.core.rest.model.capitalsource;

import java.util.List;
import java.util.Set;

import org.laladev.moneyjinn.core.rest.model.AbstractResponse;
import org.laladev.moneyjinn.core.rest.model.transport.CapitalsourceTransport;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "showCapitalsourceListResponse")
public class ShowCapitalsourceListResponse extends AbstractResponse {
	private Set<Character> initials;
	@XmlElement(name = "capitalsourceTransport")
	private List<CapitalsourceTransport> capitalsourceTransports;
	private boolean currentlyValid;

	public final Set<Character> getInitials() {
		return this.initials;
	}

	public final void setInitials(final Set<Character> initials) {
		this.initials = initials;
	}

	public final List<CapitalsourceTransport> getCapitalsourceTransports() {
		return this.capitalsourceTransports;
	}

	public final void setCapitalsourceTransports(final List<CapitalsourceTransport> capitalsourceTransports) {
		this.capitalsourceTransports = capitalsourceTransports;
	}

	public final boolean isCurrentlyValid() {
		return this.currentlyValid;
	}

	public final void setCurrentlyValid(final boolean currentlyValid) {
		this.currentlyValid = currentlyValid;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((this.capitalsourceTransports == null) ? 0 : this.capitalsourceTransports.hashCode());
		result = prime * result + (this.currentlyValid ? 1231 : 1237);
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
		final ShowCapitalsourceListResponse other = (ShowCapitalsourceListResponse) obj;
		if (this.capitalsourceTransports == null) {
			if (other.capitalsourceTransports != null) {
				return false;
			}
		} else if (!this.capitalsourceTransports.equals(other.capitalsourceTransports)) {
			return false;
		}
		if (this.currentlyValid != other.currentlyValid) {
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
		builder.append("ShowCapitalsourceListResponse [initials=");
		builder.append(this.initials);
		builder.append(", capitalsourceTransports=");
		builder.append(this.capitalsourceTransports);
		builder.append(", currentlyValid=");
		builder.append(this.currentlyValid);
		builder.append("]");
		return builder.toString();
	}

}
