//
//Copyright (c) 2015-2021 Oliver Lehmann <lehmann@ans-netz.de>
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
//

package org.laladev.moneyjinn.core.rest.model.report;

import java.sql.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.laladev.moneyjinn.core.rest.model.AbstractResponse;
import org.laladev.moneyjinn.core.rest.model.transport.PostingAccountTransport;

@XmlRootElement(name = "showReportingFormResponse")
public class ShowReportingFormResponse extends AbstractResponse {
	@XmlElement(name = "postingAccountTransport")
	private List<PostingAccountTransport> postingAccountTransports;
	@XmlElement(name = "postingAccountIdsNo")
	private List<Long> postingAccountIds;
	private Date minDate;
	private Date maxDate;

	public final Date getMinDate() {
		return this.minDate;
	}

	public final void setMinDate(final Date minDate) {
		this.minDate = minDate;
	}

	public final Date getMaxDate() {
		return this.maxDate;
	}

	public final void setMaxDate(final Date maxDate) {
		this.maxDate = maxDate;
	}

	public final List<PostingAccountTransport> getPostingAccountTransports() {
		return this.postingAccountTransports;
	}

	public final void setPostingAccountTransports(final List<PostingAccountTransport> postingAccountTransports) {
		this.postingAccountTransports = postingAccountTransports;
	}

	public final List<Long> getPostingAccountIds() {
		return this.postingAccountIds;
	}

	public final void setPostingAccountIds(final List<Long> postingAccountIds) {
		this.postingAccountIds = postingAccountIds;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((this.maxDate == null) ? 0 : this.maxDate.hashCode());
		result = prime * result + ((this.minDate == null) ? 0 : this.minDate.hashCode());
		result = prime * result + ((this.postingAccountIds == null) ? 0 : this.postingAccountIds.hashCode());
		result = prime * result
				+ ((this.postingAccountTransports == null) ? 0 : this.postingAccountTransports.hashCode());
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
		final ShowReportingFormResponse other = (ShowReportingFormResponse) obj;
		if (this.maxDate == null) {
			if (other.maxDate != null) {
				return false;
			}
		} else if (!this.maxDate.equals(other.maxDate)) {
			return false;
		}
		if (this.minDate == null) {
			if (other.minDate != null) {
				return false;
			}
		} else if (!this.minDate.equals(other.minDate)) {
			return false;
		}
		if (this.postingAccountIds == null) {
			if (other.postingAccountIds != null) {
				return false;
			}
		} else if (!this.postingAccountIds.equals(other.postingAccountIds)) {
			return false;
		}
		if (this.postingAccountTransports == null) {
			if (other.postingAccountTransports != null) {
				return false;
			}
		} else if (!this.postingAccountTransports.equals(other.postingAccountTransports)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("ShowReportingFormResponse [postingAccountTransports=");
		builder.append(this.postingAccountTransports);
		builder.append(", postingAccountIds=");
		builder.append(this.postingAccountIds);
		builder.append(", minDate=");
		builder.append(this.minDate);
		builder.append(", maxDate=");
		builder.append(this.maxDate);
		builder.append("]");
		return builder.toString();
	}

}
