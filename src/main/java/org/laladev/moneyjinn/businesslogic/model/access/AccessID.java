package org.laladev.moneyjinn.businesslogic.model.access;

import org.laladev.moneyjinn.businesslogic.model.AbstractEntityID;

public class AccessID extends AbstractEntityID<Long> implements Cloneable {

	public AccessID(final Long id) {
		super(id);
	}

	@Override
	public AccessID clone() throws CloneNotSupportedException {
		return (AccessID) super.clone();
	}
}
