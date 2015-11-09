//
// Copyright (c) 2015 Oliver Lehmann <oliver@laladev.org>
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions
// are met:
// 1. Redistributions of source code must retain the above copyright
// notice, this list of conditions and the following disclaimer
// 2. Redistributions in binary form must reproduce the above copyright
// notice, this list of conditions and the following disclaimer in the
// documentation and/or other materials provided with the distribution.
//
// THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS'' AND
// ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE LIABLE
// FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
// DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
// OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
// HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
// LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
// OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE.
//

package org.laladev.moneyjinn.core.rest.model.report.transport;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class ReportTurnoverCapitalsourceTransport {
	private Short capitalsourceType;
	private Short capitalsourceState;
	private String capitalsourceComment;
	private BigDecimal amountBeginOfMonthFixed;
	private BigDecimal amountEndOfMonthFixed;
	private BigDecimal amountEndOfMonthCalculated;
	private BigDecimal amountCurrent;
	private Timestamp amountCurrentState;

	public final Short getCapitalsourceType() {
		return this.capitalsourceType;
	}

	public final void setCapitalsourceType(final Short capitalsourceType) {
		this.capitalsourceType = capitalsourceType;
	}

	public final Short getCapitalsourceState() {
		return this.capitalsourceState;
	}

	public final void setCapitalsourceState(final Short capitalsourceState) {
		this.capitalsourceState = capitalsourceState;
	}

	public final String getCapitalsourceComment() {
		return this.capitalsourceComment;
	}

	public final void setCapitalsourceComment(final String capitalsourceComment) {
		this.capitalsourceComment = capitalsourceComment;
	}

	public final BigDecimal getAmountBeginOfMonthFixed() {
		return this.amountBeginOfMonthFixed;
	}

	public final void setAmountBeginOfMonthFixed(final BigDecimal amountBeginOfMonthFixed) {
		this.amountBeginOfMonthFixed = amountBeginOfMonthFixed;
	}

	public final BigDecimal getAmountEndOfMonthFixed() {
		return this.amountEndOfMonthFixed;
	}

	public final void setAmountEndOfMonthFixed(final BigDecimal amountEndOfMonthFixed) {
		this.amountEndOfMonthFixed = amountEndOfMonthFixed;
	}

	public final BigDecimal getAmountEndOfMonthCalculated() {
		return this.amountEndOfMonthCalculated;
	}

	public final void setAmountEndOfMonthCalculated(final BigDecimal amountEndOfMonthCalculated) {
		this.amountEndOfMonthCalculated = amountEndOfMonthCalculated;
	}

	public final BigDecimal getAmountCurrent() {
		return this.amountCurrent;
	}

	public final void setAmountCurrent(final BigDecimal amountCurrent) {
		this.amountCurrent = amountCurrent;
	}

	public final Timestamp getAmountCurrentState() {
		return this.amountCurrentState;
	}

	public final void setAmountCurrentState(final Timestamp amountCurrentState) {
		this.amountCurrentState = amountCurrentState;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((this.amountBeginOfMonthFixed == null) ? 0 : this.amountBeginOfMonthFixed.hashCode());
		result = prime * result + ((this.amountCurrent == null) ? 0 : this.amountCurrent.hashCode());
		result = prime * result + ((this.amountCurrentState == null) ? 0 : this.amountCurrentState.hashCode());
		result = prime * result
				+ ((this.amountEndOfMonthCalculated == null) ? 0 : this.amountEndOfMonthCalculated.hashCode());
		result = prime * result + ((this.amountEndOfMonthFixed == null) ? 0 : this.amountEndOfMonthFixed.hashCode());
		result = prime * result + ((this.capitalsourceComment == null) ? 0 : this.capitalsourceComment.hashCode());
		result = prime * result + ((this.capitalsourceState == null) ? 0 : this.capitalsourceState.hashCode());
		result = prime * result + ((this.capitalsourceType == null) ? 0 : this.capitalsourceType.hashCode());
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
		final ReportTurnoverCapitalsourceTransport other = (ReportTurnoverCapitalsourceTransport) obj;
		if (this.amountBeginOfMonthFixed == null) {
			if (other.amountBeginOfMonthFixed != null) {
				return false;
			}
		} else if (!this.amountBeginOfMonthFixed.equals(other.amountBeginOfMonthFixed)) {
			return false;
		}
		if (this.amountCurrent == null) {
			if (other.amountCurrent != null) {
				return false;
			}
		} else if (!this.amountCurrent.equals(other.amountCurrent)) {
			return false;
		}
		if (this.amountCurrentState == null) {
			if (other.amountCurrentState != null) {
				return false;
			}
		} else if (!this.amountCurrentState.equals(other.amountCurrentState)) {
			return false;
		}
		if (this.amountEndOfMonthCalculated == null) {
			if (other.amountEndOfMonthCalculated != null) {
				return false;
			}
		} else if (!this.amountEndOfMonthCalculated.equals(other.amountEndOfMonthCalculated)) {
			return false;
		}
		if (this.amountEndOfMonthFixed == null) {
			if (other.amountEndOfMonthFixed != null) {
				return false;
			}
		} else if (!this.amountEndOfMonthFixed.equals(other.amountEndOfMonthFixed)) {
			return false;
		}
		if (this.capitalsourceComment == null) {
			if (other.capitalsourceComment != null) {
				return false;
			}
		} else if (!this.capitalsourceComment.equals(other.capitalsourceComment)) {
			return false;
		}
		if (this.capitalsourceState == null) {
			if (other.capitalsourceState != null) {
				return false;
			}
		} else if (!this.capitalsourceState.equals(other.capitalsourceState)) {
			return false;
		}
		if (this.capitalsourceType == null) {
			if (other.capitalsourceType != null) {
				return false;
			}
		} else if (!this.capitalsourceType.equals(other.capitalsourceType)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("ReportTurnoverCapitalsourceTransport [capitalsourceType=");
		builder.append(this.capitalsourceType);
		builder.append(", capitalsourceState=");
		builder.append(this.capitalsourceState);
		builder.append(", capitalsourceComment=");
		builder.append(this.capitalsourceComment);
		builder.append(", amountBeginOfMonthFixed=");
		builder.append(this.amountBeginOfMonthFixed);
		builder.append(", amountEndOfMonthFixed=");
		builder.append(this.amountEndOfMonthFixed);
		builder.append(", amountEndOfMonthCalculated=");
		builder.append(this.amountEndOfMonthCalculated);
		builder.append(", amountCurrent=");
		builder.append(this.amountCurrent);
		builder.append(", amountCurrentState=");
		builder.append(this.amountCurrentState);
		builder.append("]");
		return builder.toString();
	}

}
