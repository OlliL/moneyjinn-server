//Copyright (c) 2015 Oliver Lehmann <oliver@laladev.org>
//All rights reserved.
//
//Redistribution and use in source and binary forms, with or without
//modification, are permitted provided that the following conditions
//are met:
//1. Redistributions of source code must retain the above copyright
//notice, this list of conditions and the following disclaimer
//2. Redistributions in binary form must reproduce the above copyright
//notice, this list of conditions and the following disclaimer in the
//documentation and/or other materials provided with the distribution.
//
//THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS'' AND
//ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
//IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
//ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE LIABLE
//FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
//DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
//OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
//HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
//LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
//OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
//SUCH DAMAGE.

package org.laladev.moneyjinn.model.monthlysettlement;

import java.math.BigDecimal;
import java.time.Month;

import org.laladev.moneyjinn.model.AbstractEntity;
import org.laladev.moneyjinn.model.AbstractEntityID;
import org.laladev.moneyjinn.model.access.Group;
import org.laladev.moneyjinn.model.access.User;
import org.laladev.moneyjinn.model.capitalsource.Capitalsource;

public abstract class AbstractMonthlySettlement<I extends AbstractEntityID<?>> extends AbstractEntity<I> {
	private static final long serialVersionUID = 1L;
	private User user;
	private Group group;
	private Short year;
	private Month month;
	private Capitalsource capitalsource;
	private BigDecimal amount;

	public final User getUser() {
		return this.user;
	}

	public final void setUser(final User user) {
		this.user = user;
	}

	public final Group getGroup() {
		return this.group;
	}

	public final void setGroup(final Group group) {
		this.group = group;
	}

	public final Short getYear() {
		return this.year;
	}

	public final void setYear(final Short year) {
		this.year = year;
	}

	public final Month getMonth() {
		return this.month;
	}

	public final void setMonth(final Month month) {
		this.month = month;
	}

	public final Capitalsource getCapitalsource() {
		return this.capitalsource;
	}

	public final void setCapitalsource(final Capitalsource capitalsource) {
		this.capitalsource = capitalsource;
	}

	public final BigDecimal getAmount() {
		return this.amount;
	}

	public final void setAmount(final BigDecimal amount) {
		this.amount = amount;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((this.amount == null) ? 0 : this.amount.hashCode());
		result = prime * result + ((this.capitalsource == null) ? 0 : this.capitalsource.hashCode());
		result = prime * result + ((this.group == null) ? 0 : this.group.hashCode());
		result = prime * result + ((this.month == null) ? 0 : this.month.hashCode());
		result = prime * result + ((this.user == null) ? 0 : this.user.hashCode());
		result = prime * result + ((this.year == null) ? 0 : this.year.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		@SuppressWarnings("rawtypes")
		final AbstractMonthlySettlement other = (AbstractMonthlySettlement) obj;
		if (this.amount == null) {
			if (other.amount != null) {
				return false;
			}
		} else if (!this.amount.equals(other.amount)) {
			return false;
		}
		if (this.capitalsource == null) {
			if (other.capitalsource != null) {
				return false;
			}
		} else if (!this.capitalsource.equals(other.capitalsource)) {
			return false;
		}
		if (this.group == null) {
			if (other.group != null) {
				return false;
			}
		} else if (!this.group.equals(other.group)) {
			return false;
		}
		if (this.month != other.month) {
			return false;
		}
		if (this.user == null) {
			if (other.user != null) {
				return false;
			}
		} else if (!this.user.equals(other.user)) {
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

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("MonthlySettlement [user=");
		builder.append(this.user);
		builder.append(", group=");
		builder.append(this.group);
		builder.append(", year=");
		builder.append(this.year);
		builder.append(", month=");
		builder.append(this.month);
		builder.append(", capitalsource=");
		builder.append(this.capitalsource);
		builder.append(", amount=");
		builder.append(this.amount);
		builder.append(", getId()=");
		builder.append(this.getId());
		builder.append("]");
		return builder.toString();
	}

}