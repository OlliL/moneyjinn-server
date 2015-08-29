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
		return this.id;
	}

	public final void setId(final Integer id) {
		this.id = id;
	}

	public final Integer getUserid() {
		return this.userid;
	}

	public final void setUserid(final Integer userid) {
		this.userid = userid;
	}

	public final Integer getYear() {
		return this.year;
	}

	public final void setYear(final Integer year) {
		this.year = year;
	}

	public final Integer getMonth() {
		return this.month;
	}

	public final void setMonth(final Integer month) {
		this.month = month;
	}

	public final BigDecimal getAmount() {
		return this.amount;
	}

	public final void setAmount(final BigDecimal amount) {
		this.amount = amount;
	}

	public final Integer getCapitalsourceid() {
		return this.capitalsourceid;
	}

	public final void setCapitalsourceid(final Integer capitalsourceid) {
		this.capitalsourceid = capitalsourceid;
	}

	public final String getCapitalsourcecomment() {
		return this.capitalsourcecomment;
	}

	public final void setCapitalsourcecomment(final String capitalsourcecomment) {
		this.capitalsourcecomment = capitalsourcecomment;
	}

	public final Boolean getCapitalsourcegroupuse() {
		return this.capitalsourcegroupuse;
	}

	public final void setCapitalsourcegroupuse(final Boolean capitalsourcegroupuse) {
		this.capitalsourcegroupuse = capitalsourcegroupuse;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.amount == null) ? 0 : this.amount.hashCode());
		result = prime * result + ((this.capitalsourcecomment == null) ? 0 : this.capitalsourcecomment.hashCode());
		result = prime * result + ((this.capitalsourcegroupuse == null) ? 0 : this.capitalsourcegroupuse.hashCode());
		result = prime * result + ((this.capitalsourceid == null) ? 0 : this.capitalsourceid.hashCode());
		result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
		result = prime * result + ((this.month == null) ? 0 : this.month.hashCode());
		result = prime * result + ((this.userid == null) ? 0 : this.userid.hashCode());
		result = prime * result + ((this.year == null) ? 0 : this.year.hashCode());
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
		final MonthlySettlementTransport other = (MonthlySettlementTransport) obj;
		if (this.amount == null) {
			if (other.amount != null) {
				return false;
			}
		} else if (!this.amount.equals(other.amount)) {
			return false;
		}
		if (this.capitalsourcecomment == null) {
			if (other.capitalsourcecomment != null) {
				return false;
			}
		} else if (!this.capitalsourcecomment.equals(other.capitalsourcecomment)) {
			return false;
		}
		if (this.capitalsourcegroupuse == null) {
			if (other.capitalsourcegroupuse != null) {
				return false;
			}
		} else if (!this.capitalsourcegroupuse.equals(other.capitalsourcegroupuse)) {
			return false;
		}
		if (this.capitalsourceid == null) {
			if (other.capitalsourceid != null) {
				return false;
			}
		} else if (!this.capitalsourceid.equals(other.capitalsourceid)) {
			return false;
		}
		if (this.id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!this.id.equals(other.id)) {
			return false;
		}
		if (this.month == null) {
			if (other.month != null) {
				return false;
			}
		} else if (!this.month.equals(other.month)) {
			return false;
		}
		if (this.userid == null) {
			if (other.userid != null) {
				return false;
			}
		} else if (!this.userid.equals(other.userid)) {
			return false;
		}
		if (this.year == null) {
			if (other.year != null) {
				return false;
			}
		} else if (!this.year.equals(other.year)) {
			return false;
		}
		return true;
	}

}
