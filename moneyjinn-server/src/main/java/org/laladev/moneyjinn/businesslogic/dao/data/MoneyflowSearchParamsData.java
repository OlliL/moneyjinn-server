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

package org.laladev.moneyjinn.businesslogic.dao.data;

import java.time.LocalDate;

public class MoneyflowSearchParamsData {
	private Long userId;
	private LocalDate startDate;
	private LocalDate endDate;
	private String searchString;
	private boolean featureEqual;
	private boolean featureRegexp;
	private boolean featureCaseSensitive;
	private boolean featureOnlyMinusAmounts;
	private Long contractpartnerId;
	private Long postingAccountId;
	private String groupBy1;
	private String groupBy2;

	public final Long getUserId() {
		return this.userId;
	}

	public final void setUserId(final Long userId) {
		this.userId = userId;
	}

	public final LocalDate getStartDate() {
		return this.startDate;
	}

	public final void setStartDate(final LocalDate startDate) {
		this.startDate = startDate;
	}

	public final LocalDate getEndDate() {
		return this.endDate;
	}

	public final void setEndDate(final LocalDate endDate) {
		this.endDate = endDate;
	}

	public final String getSearchString() {
		return this.searchString;
	}

	public final void setSearchString(final String searchString) {
		this.searchString = searchString;
	}

	public final boolean isFeatureEqual() {
		return this.featureEqual;
	}

	public final void setFeatureEqual(final boolean featureEqual) {
		this.featureEqual = featureEqual;
	}

	public final boolean isFeatureRegexp() {
		return this.featureRegexp;
	}

	public final void setFeatureRegexp(final boolean featureRegexp) {
		this.featureRegexp = featureRegexp;
	}

	public final boolean isFeatureCaseSensitive() {
		return this.featureCaseSensitive;
	}

	public final void setFeatureCaseSensitive(final boolean featureCaseSensitive) {
		this.featureCaseSensitive = featureCaseSensitive;
	}

	public final boolean isFeatureOnlyMinusAmounts() {
		return this.featureOnlyMinusAmounts;
	}

	public final void setFeatureOnlyMinusAmounts(final boolean featureOnlyMinusAmounts) {
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

	public final String getGroupBy1() {
		return this.groupBy1;
	}

	public final void setGroupBy1(final String groupBy1) {
		this.groupBy1 = groupBy1;
	}

	public final String getGroupBy2() {
		return this.groupBy2;
	}

	public final void setGroupBy2(final String groupBy2) {
		this.groupBy2 = groupBy2;
	}

}
