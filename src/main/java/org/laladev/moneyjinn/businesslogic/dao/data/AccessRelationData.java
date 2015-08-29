package org.laladev.moneyjinn.businesslogic.dao.data;

import java.sql.Date;

public class AccessRelationData {
	private Long id;
	private Long refId;
	private Date validFrom;
	private Date validTil;

	public AccessRelationData(final Long id, final Long refId, final Date validFrom, final Date validTil) {
		super();
		this.id = id;
		this.refId = refId;
		this.validFrom = validFrom;
		this.validTil = validTil;
	}

	public final Long getId() {
		return id;
	}

	public final void setId(final Long id) {
		this.id = id;
	}

	public final Long getRefId() {
		return refId;
	}

	public final void setRefId(final Long refId) {
		this.refId = refId;
	}

	public final Date getValidFrom() {
		return validFrom;
	}

	public final void setValidFrom(final Date validFrom) {
		this.validFrom = validFrom;
	}

	public final Date getValidTil() {
		return validTil;
	}

	public final void setValidTil(final Date validTil) {
		this.validTil = validTil;
	}

}
