package org.laladev.moneyjinn.core.rest.model.transport;

import java.math.BigDecimal;

public class MonthlySettlementTransport {
	private Integer id;
	private Integer userid;
	private Integer year;
	private Integer month;
	private BigDecimal amount;
	private Integer capitalsourceid;
	private String capitalsourcecomment;
	private Boolean capitalsourcegroupuse;

	public final Integer getId() {
		return id;
	}

	public final void setId(final Integer id) {
		this.id = id;
	}

	public final Integer getUserid() {
		return userid;
	}

	public final void setUserid(final Integer userid) {
		this.userid = userid;
	}

	public final Integer getYear() {
		return year;
	}

	public final void setYear(final Integer year) {
		this.year = year;
	}

	public final Integer getMonth() {
		return month;
	}

	public final void setMonth(final Integer month) {
		this.month = month;
	}

	public final BigDecimal getAmount() {
		return amount;
	}

	public final void setAmount(final BigDecimal amount) {
		this.amount = amount;
	}

	public final Integer getCapitalsourceid() {
		return capitalsourceid;
	}

	public final void setCapitalsourceid(final Integer capitalsourceid) {
		this.capitalsourceid = capitalsourceid;
	}

	public final String getCapitalsourcecomment() {
		return capitalsourcecomment;
	}

	public final void setCapitalsourcecomment(final String capitalsourcecomment) {
		this.capitalsourcecomment = capitalsourcecomment;
	}

	public final Boolean getCapitalsourcegroupuse() {
		return capitalsourcegroupuse;
	}

	public final void setCapitalsourcegroupuse(final Boolean capitalsourcegroupuse) {
		this.capitalsourcegroupuse = capitalsourcegroupuse;
	}

}
