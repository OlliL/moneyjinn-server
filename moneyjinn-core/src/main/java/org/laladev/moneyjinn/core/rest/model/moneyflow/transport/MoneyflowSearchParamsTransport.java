//
// Copyright (c) 2015 Oliver Lehmann <lehmann@ans-netz.de>
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

package org.laladev.moneyjinn.core.rest.model.moneyflow.transport;

import java.sql.Date;

public class MoneyflowSearchParamsTransport {
	private Date startDate;
	private Date endDate;
	private String searchString;
	private Short featureEqual;
	private Short featureRegexp;
	private Short featureCaseSensitive;
	private Short featureOnlyMinusAmounts;
	private Long contractpartnerId;
	private Long postingAccountId;

	public final Date getStartDate() {
		return this.startDate;
	}

	public final void setStartDate(final Date startDate) {
		this.startDate = startDate;
	}

	public final Date getEndDate() {
		return this.endDate;
	}

	public final void setEndDate(final Date endDate) {
		this.endDate = endDate;
	}

	public final String getSearchString() {
		return this.searchString;
	}

	public final void setSearchString(final String searchString) {
		this.searchString = searchString;
	}

	public final Short getFeatureEqual() {
		return this.featureEqual;
	}

	public final void setFeatureEqual(final Short featureEqual) {
		this.featureEqual = featureEqual;
	}

	public final Short getFeatureRegexp() {
		return this.featureRegexp;
	}

	public final void setFeatureRegexp(final Short featureRegexp) {
		this.featureRegexp = featureRegexp;
	}

	public final Short getFeatureCaseSensitive() {
		return this.featureCaseSensitive;
	}

	public final void setFeatureCaseSensitive(final Short featureCaseSensitive) {
		this.featureCaseSensitive = featureCaseSensitive;
	}

	public final Short getFeatureOnlyMinusAmounts() {
		return this.featureOnlyMinusAmounts;
	}

	public final void setFeatureOnlyMinusAmounts(final Short featureOnlyMinusAmounts) {
		this.featureOnlyMinusAmounts = featureOnlyMinusAmounts;
	}

	public final Long getContractpartnerId() {
		return this.contractpartnerId;
	}

	public final void setContractpartnerId(final Long contractpartnerId) {
		this.contractpartnerId = contractpartnerId;
	}

	public final Long getPostingAccountId() {
		return this.postingAccountId;
	}

	public final void setPostingAccountId(final Long postingAccountId) {
		this.postingAccountId = postingAccountId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.contractpartnerId == null) ? 0 : this.contractpartnerId.hashCode());
		result = prime * result + ((this.endDate == null) ? 0 : this.endDate.hashCode());
		result = prime * result + ((this.featureCaseSensitive == null) ? 0 : this.featureCaseSensitive.hashCode());
		result = prime * result + ((this.featureEqual == null) ? 0 : this.featureEqual.hashCode());
		result = prime * result
				+ ((this.featureOnlyMinusAmounts == null) ? 0 : this.featureOnlyMinusAmounts.hashCode());
		result = prime * result + ((this.featureRegexp == null) ? 0 : this.featureRegexp.hashCode());
		result = prime * result + ((this.postingAccountId == null) ? 0 : this.postingAccountId.hashCode());
		result = prime * result + ((this.searchString == null) ? 0 : this.searchString.hashCode());
		result = prime * result + ((this.startDate == null) ? 0 : this.startDate.hashCode());
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
		final MoneyflowSearchParamsTransport other = (MoneyflowSearchParamsTransport) obj;
		if (this.contractpartnerId == null) {
			if (other.contractpartnerId != null) {
				return false;
			}
		} else if (!this.contractpartnerId.equals(other.contractpartnerId)) {
			return false;
		}
		if (this.endDate == null) {
			if (other.endDate != null) {
				return false;
			}
		} else if (!this.endDate.equals(other.endDate)) {
			return false;
		}
		if (this.featureCaseSensitive == null) {
			if (other.featureCaseSensitive != null) {
				return false;
			}
		} else if (!this.featureCaseSensitive.equals(other.featureCaseSensitive)) {
			return false;
		}
		if (this.featureEqual == null) {
			if (other.featureEqual != null) {
				return false;
			}
		} else if (!this.featureEqual.equals(other.featureEqual)) {
			return false;
		}
		if (this.featureOnlyMinusAmounts == null) {
			if (other.featureOnlyMinusAmounts != null) {
				return false;
			}
		} else if (!this.featureOnlyMinusAmounts.equals(other.featureOnlyMinusAmounts)) {
			return false;
		}
		if (this.featureRegexp == null) {
			if (other.featureRegexp != null) {
				return false;
			}
		} else if (!this.featureRegexp.equals(other.featureRegexp)) {
			return false;
		}
		if (this.postingAccountId == null) {
			if (other.postingAccountId != null) {
				return false;
			}
		} else if (!this.postingAccountId.equals(other.postingAccountId)) {
			return false;
		}
		if (this.searchString == null) {
			if (other.searchString != null) {
				return false;
			}
		} else if (!this.searchString.equals(other.searchString)) {
			return false;
		}
		if (this.startDate == null) {
			if (other.startDate != null) {
				return false;
			}
		} else if (!this.startDate.equals(other.startDate)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("MoneyflowSearchParamsTransport [startDate=");
		builder.append(this.startDate);
		builder.append(", endDate=");
		builder.append(this.endDate);
		builder.append(", searchString=");
		builder.append(this.searchString);
		builder.append(", featureEqual=");
		builder.append(this.featureEqual);
		builder.append(", featureRegexp=");
		builder.append(this.featureRegexp);
		builder.append(", featureCaseSensitive=");
		builder.append(this.featureCaseSensitive);
		builder.append(", featureOnlyMinusAmounts=");
		builder.append(this.featureOnlyMinusAmounts);
		builder.append(", contractpartnerId=");
		builder.append(this.contractpartnerId);
		builder.append(", postingAccountId=");
		builder.append(this.postingAccountId);
		builder.append("]");
		return builder.toString();
	}

}
