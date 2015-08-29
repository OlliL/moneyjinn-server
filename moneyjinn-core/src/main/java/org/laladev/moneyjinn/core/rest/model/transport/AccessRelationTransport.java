package org.laladev.moneyjinn.core.rest.model.transport;

import java.sql.Date;

public class AccessRelationTransport {
	private Long id;
	private Long refId;
	private Date validfrom;
	private Date validtil;

	public final Long getId() {
		return this.id;
	}

	public final void setId(final Long id) {
		this.id = id;
	}

	public final Long getRefId() {
		return this.refId;
	}

	public final void setRefId(final Long refId) {
		this.refId = refId;
	}

	public final Date getValidfrom() {
		return this.validfrom;
	}

	public final void setValidfrom(final Date validfrom) {
		this.validfrom = validfrom;
	}

	public final Date getValidtil() {
		return this.validtil;
	}

	public final void setValidtil(final Date validtil) {
		this.validtil = validtil;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
		result = prime * result + ((this.refId == null) ? 0 : this.refId.hashCode());
		result = prime * result + ((this.validfrom == null) ? 0 : this.validfrom.hashCode());
		result = prime * result + ((this.validtil == null) ? 0 : this.validtil.hashCode());
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
		final AccessRelationTransport other = (AccessRelationTransport) obj;
		if (this.id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!this.id.equals(other.id)) {
			return false;
		}
		if (this.refId == null) {
			if (other.refId != null) {
				return false;
			}
		} else if (!this.refId.equals(other.refId)) {
			return false;
		}
		if (this.validfrom == null) {
			if (other.validfrom != null) {
				return false;
			}
		} else if (!this.validfrom.equals(other.validfrom)) {
			return false;
		}
		if (this.validtil == null) {
			if (other.validtil != null) {
				return false;
			}
		} else if (!this.validtil.equals(other.validtil)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("AccessRelationTransport [id=");
		builder.append(this.id);
		builder.append(", refId=");
		builder.append(this.refId);
		builder.append(", validfrom=");
		builder.append(this.validfrom);
		builder.append(", validtil=");
		builder.append(this.validtil);
		builder.append("]");
		return builder.toString();
	}

}
