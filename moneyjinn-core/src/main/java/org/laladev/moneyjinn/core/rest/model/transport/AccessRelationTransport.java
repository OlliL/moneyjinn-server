package org.laladev.moneyjinn.core.rest.model.transport;

import java.sql.Date;

public class AccessRelationTransport {
	private Long id;
	private Long refId;
	private Date validfrom;
	private Date validtil;

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

	public final Date getValidfrom() {
		return validfrom;
	}

	public final void setValidfrom(final Date validfrom) {
		this.validfrom = validfrom;
	}

	public final Date getValidtil() {
		return validtil;
	}

	public final void setValidtil(final Date validtil) {
		this.validtil = validtil;
	}

}
