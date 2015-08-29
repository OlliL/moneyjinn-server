package org.laladev.moneyjinn.businesslogic.dao.data;

import java.sql.Date;

public class AccessFlattenedData {
	private Long id;
	private Date validFrom;
	private Date validTil;
	private Long idLevel1;
	private Long idLevel2;
	private Long idLevel3;
	private Long idLevel4;
	private Long idLevel5;

	public final Long getId() {
		return this.id;
	}

	public final void setId(final Long id) {
		this.id = id;
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

	public final Long getIdLevel1() {
		return this.idLevel1;
	}

	public final void setIdLevel1(final Long idLevel1) {
		this.idLevel1 = idLevel1;
	}

	public final Long getIdLevel2() {
		return this.idLevel2;
	}

	public final void setIdLevel2(final Long idLevel2) {
		this.idLevel2 = idLevel2;
	}

	public final Long getIdLevel3() {
		return this.idLevel3;
	}

	public final void setIdLevel3(final Long idLevel3) {
		this.idLevel3 = idLevel3;
	}

	public final Long getIdLevel4() {
		return this.idLevel4;
	}

	public final void setIdLevel4(final Long idLevel4) {
		this.idLevel4 = idLevel4;
	}

	public final Long getIdLevel5() {
		return this.idLevel5;
	}

	public final void setIdLevel5(final Long idLevel5) {
		this.idLevel5 = idLevel5;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("AccessFlattenedData [id=").append(this.id).append(", validFrom=").append(this.validFrom)
				.append(", validTil=").append(this.validTil).append(", idLevel1=").append(this.idLevel1)
				.append(", idLevel2=").append(this.idLevel2).append(", idLevel3=").append(this.idLevel3)
				.append(", idLevel4=").append(this.idLevel4).append(", idLevel5=").append(this.idLevel5).append("]");
		return builder.toString();
	}

}
