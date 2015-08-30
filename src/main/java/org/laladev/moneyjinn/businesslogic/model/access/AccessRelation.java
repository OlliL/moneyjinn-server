package org.laladev.moneyjinn.businesslogic.model.access;

import java.time.LocalDate;

import org.laladev.moneyjinn.businesslogic.model.AbstractEntity;

public class AccessRelation extends AbstractEntity<AccessID> implements Cloneable {
	private static final long serialVersionUID = 1L;
	private AccessRelation parentAccessRelation;
	private LocalDate validFrom;
	private LocalDate validTil;

	public AccessRelation(final AccessID id) {
		super.setId(id);
	}

	public AccessRelation(final AccessID id, final AccessRelation parentAccessRelation, final LocalDate validFrom,
			final LocalDate validTil) {
		super.setId(id);
		this.parentAccessRelation = parentAccessRelation;
		this.validFrom = validFrom;
		this.validTil = validTil;
	}

	public AccessRelation(final AccessID id, final AccessRelation parentAccessRelation) {
		super.setId(id);
		this.parentAccessRelation = parentAccessRelation;
	}

	public AccessRelation() {
	}

	public final AccessRelation getParentAccessRelation() {
		return this.parentAccessRelation;
	}

	public final void setParentAccessRelation(final AccessRelation parentAccessRelation) {
		this.parentAccessRelation = parentAccessRelation;
	}

	public final LocalDate getValidFrom() {
		return this.validFrom;
	}

	public final void setValidFrom(final LocalDate validFrom) {
		this.validFrom = validFrom;
	}

	public final LocalDate getValidTil() {
		return this.validTil;
	}

	public final void setValidTil(final LocalDate validTil) {
		this.validTil = validTil;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("AccessRelation [refId=").append(this.parentAccessRelation).append(", validFrom=")
				.append(this.validFrom).append(", validTil=").append(this.validTil).append(", getId()=")
				.append(this.getId()).append("]");
		return builder.toString();
	}

	@Override
	public AccessRelation clone() throws CloneNotSupportedException {
		final AccessRelation accessRelation = (AccessRelation) super.clone();
		accessRelation.setId(this.getId().clone());
		if (this.getParentAccessRelation() != null) {
			accessRelation.setParentAccessRelation(this.getParentAccessRelation().clone());
		}
		return accessRelation;
	}
}
