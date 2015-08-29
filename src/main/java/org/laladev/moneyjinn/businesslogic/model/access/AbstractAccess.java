package org.laladev.moneyjinn.businesslogic.model.access;

import java.io.Serializable;

import org.laladev.moneyjinn.businesslogic.model.AbstractEntity;

public class AbstractAccess<ID extends AccessID> extends AbstractEntity<ID> implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;

	public AbstractAccess() {
	}

	public AbstractAccess(final ID id, final String name) {
		super.setId(id);
		this.name = name;
	}

	public final String getName() {
		return this.name;
	}

	public final void setName(final String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("AbstractAccess [name=").append(this.name).append(", getId()=").append(this.getId()).append("]");
		return builder.toString();
	}

}
