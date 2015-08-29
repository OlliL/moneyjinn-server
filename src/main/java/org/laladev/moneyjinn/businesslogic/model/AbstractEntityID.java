package org.laladev.moneyjinn.businesslogic.model;

public abstract class AbstractEntityID<ID> {
	private ID id;

	protected AbstractEntityID() {
	}

	protected AbstractEntityID(final ID id) {
		this.id = id;
	}

	public final ID getId() {
		return id;
	}

	public final void setId(final ID id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		if (getClass() != obj.getClass()) {
			return false;
		}
		final AbstractEntityID<?> other = (AbstractEntityID<?>) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("[id=").append(id).append("]");
		return builder.toString();
	}

}
