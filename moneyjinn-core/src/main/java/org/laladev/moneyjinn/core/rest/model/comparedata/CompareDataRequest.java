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

package org.laladev.moneyjinn.core.rest.model.comparedata;

import java.sql.Date;

import org.laladev.moneyjinn.core.rest.model.AbstractRequest;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "compareDataRequest")
public class CompareDataRequest extends AbstractRequest {
	private Long formatId;
	@XmlElement(name = "capitalSourceId")
	private Long capitalsourceId;
	private Date startDate;
	private Date endDate;
	private String fileContents;
	private Short useImportedData;

	public final Long getFormatId() {
		return this.formatId;
	}

	public final void setFormatId(final Long formatId) {
		this.formatId = formatId;
	}

	public final Long getCapitalsourceId() {
		return this.capitalsourceId;
	}

	public final void setCapitalsourceId(final Long capitalsourceId) {
		this.capitalsourceId = capitalsourceId;
	}

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

	public final String getFileContents() {
		return this.fileContents;
	}

	public final void setFileContents(final String fileContents) {
		this.fileContents = fileContents;
	}

	public final Short getUseImportedData() {
		return this.useImportedData;
	}

	public final void setUseImportedData(final Short useImportedData) {
		this.useImportedData = useImportedData;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.capitalsourceId == null) ? 0 : this.capitalsourceId.hashCode());
		result = prime * result + ((this.endDate == null) ? 0 : this.endDate.hashCode());
		result = prime * result + ((this.fileContents == null) ? 0 : this.fileContents.hashCode());
		result = prime * result + ((this.formatId == null) ? 0 : this.formatId.hashCode());
		result = prime * result + ((this.startDate == null) ? 0 : this.startDate.hashCode());
		result = prime * result + ((this.useImportedData == null) ? 0 : this.useImportedData.hashCode());
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
		final CompareDataRequest other = (CompareDataRequest) obj;
		if (this.capitalsourceId == null) {
			if (other.capitalsourceId != null) {
				return false;
			}
		} else if (!this.capitalsourceId.equals(other.capitalsourceId)) {
			return false;
		}
		if (this.endDate == null) {
			if (other.endDate != null) {
				return false;
			}
		} else if (!this.endDate.equals(other.endDate)) {
			return false;
		}
		if (this.fileContents == null) {
			if (other.fileContents != null) {
				return false;
			}
		} else if (!this.fileContents.equals(other.fileContents)) {
			return false;
		}
		if (this.formatId == null) {
			if (other.formatId != null) {
				return false;
			}
		} else if (!this.formatId.equals(other.formatId)) {
			return false;
		}
		if (this.startDate == null) {
			if (other.startDate != null) {
				return false;
			}
		} else if (!this.startDate.equals(other.startDate)) {
			return false;
		}
		if (this.useImportedData == null) {
			if (other.useImportedData != null) {
				return false;
			}
		} else if (!this.useImportedData.equals(other.useImportedData)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("CompareDataRequest [formatId=");
		builder.append(this.formatId);
		builder.append(", capitalsourceId=");
		builder.append(this.capitalsourceId);
		builder.append(", startDate=");
		builder.append(this.startDate);
		builder.append(", endDate=");
		builder.append(this.endDate);
		builder.append(", fileContents=");
		builder.append(this.fileContents);
		builder.append(", useImportedData=");
		builder.append(this.useImportedData);
		builder.append("]");
		return builder.toString();
	}

}
