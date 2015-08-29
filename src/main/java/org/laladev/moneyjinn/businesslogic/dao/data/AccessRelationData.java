package org.laladev.moneyjinn.businesslogic.dao.data;

import java.sql.Date;

public class AccessRelationData {
	private Long id;
	private Long refId;
	private Date validFrom;
	private Date validTil;

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

	public final Date getValidFrom() {
		return this.validFrom;
	}

	public final void setValidFrom(final Date validFrom) {
		this.validFrom = validFrom;
	}

	public final Date getValidTil() {
		return this.validTil;
	}

	public final void setValidTil(final Date validTil) {
		this.validTil = validTil;
	}

}
