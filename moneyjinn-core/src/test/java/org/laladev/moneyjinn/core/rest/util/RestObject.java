package org.laladev.moneyjinn.core.rest.util;

import java.sql.Date;
import java.sql.Timestamp;

public class RestObject {

	private String attribute1;
	private Long attribute2;
	private Date attribute3;
	private Timestamp attribute4;

	public RestObject() {
		super();
	}

	public final String getAttribute1() {
		return this.attribute1;
	}

	public final void setAttribute1(final String attribute1) {
		this.attribute1 = attribute1;
	}

	public final Long getAttribute2() {
		return this.attribute2;
	}

	public final void setAttribute2(final Long attribute2) {
		this.attribute2 = attribute2;
	}

	public final Date getAttribute3() {
		return this.attribute3;
	}

	public final void setAttribute3(final Date attribute3) {
		this.attribute3 = attribute3;
	}

	public final Timestamp getAttribute4() {
		return this.attribute4;
	}

	public final void setAttribute4(final Timestamp attribute4) {
		this.attribute4 = attribute4;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.attribute1 == null) ? 0 : this.attribute1.hashCode());
		result = prime * result + ((this.attribute2 == null) ? 0 : this.attribute2.hashCode());
		result = prime * result + ((this.attribute3 == null) ? 0 : this.attribute3.hashCode());
		result = prime * result + ((this.attribute4 == null) ? 0 : this.attribute4.hashCode());
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
		final RestObject other = (RestObject) obj;
		if (this.attribute1 == null) {
			if (other.attribute1 != null) {
				return false;
			}
		} else if (!this.attribute1.equals(other.attribute1)) {
			return false;
		}
		if (this.attribute2 == null) {
			if (other.attribute2 != null) {
				return false;
			}
		} else if (!this.attribute2.equals(other.attribute2)) {
			return false;
		}
		if (this.attribute3 == null) {
			if (other.attribute3 != null) {
				return false;
			}
		} else if (!this.attribute3.equals(other.attribute3)) {
			return false;
		}
		if (this.attribute4 == null) {
			if (other.attribute4 != null) {
				return false;
			}
		} else if (!this.attribute4.equals(other.attribute4)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("RestObject [attribute1=");
		builder.append(this.attribute1);
		builder.append(", attribute2=");
		builder.append(this.attribute2);
		builder.append(", attribute3=");
		builder.append(this.attribute3);
		builder.append(", attribute4=");
		builder.append(this.attribute4);
		builder.append("]");
		return builder.toString();
	}

}
