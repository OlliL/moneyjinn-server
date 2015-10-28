package org.laladev.moneyjinn.core.rest.model.transport;

public class CompareDataFormatTransport {
	private Long formatId;
	private String name;

	public final Long getFormatId() {
		return this.formatId;
	}

	public final void setFormatId(final Long formatId) {
		this.formatId = formatId;
	}

	public final String getName() {
		return this.name;
	}

	public final void setName(final String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.formatId == null) ? 0 : this.formatId.hashCode());
		result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
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
		final CompareDataFormatTransport other = (CompareDataFormatTransport) obj;
		if (this.formatId == null) {
			if (other.formatId != null) {
				return false;
			}
		} else if (!this.formatId.equals(other.formatId)) {
			return false;
		}
		if (this.name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!this.name.equals(other.name)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("CompareDataFormatTransport [formatId=");
		builder.append(this.formatId);
		builder.append(", name=");
		builder.append(this.name);
		builder.append("]");
		return builder.toString();
	}

}
