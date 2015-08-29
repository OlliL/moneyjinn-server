package org.laladev.moneyjinn.businesslogic.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public abstract class AbstractEntity<ID extends AbstractEntityID<?>> implements Serializable {
	private ID id;

	public final ID getId() {
		return this.id;
	}

	public final void setId(final ID id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
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
		final AbstractEntity<?> other = (AbstractEntity<?>) obj;
		if (this.id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!this.id.equals(other.id)) {
			return false;
		}
		return true;
	}

}
