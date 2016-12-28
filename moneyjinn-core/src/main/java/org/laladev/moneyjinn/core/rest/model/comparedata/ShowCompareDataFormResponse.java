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

import java.util.List;

import org.laladev.moneyjinn.core.rest.model.AbstractResponse;
import org.laladev.moneyjinn.core.rest.model.comparedata.transport.CompareDataFormatTransport;
import org.laladev.moneyjinn.core.rest.model.transport.CapitalsourceTransport;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("showCompareDataFormResponse")
public class ShowCompareDataFormResponse extends AbstractResponse {
	@JsonProperty("compareDataFormatTransport")
	private List<CompareDataFormatTransport> compareDataFormatTransports;
	@JsonProperty("capitalsourceTransport")
	private List<CapitalsourceTransport> capitalsourceTransports;
	private Long selectedCapitalsourceId;
	private Long selectedDataFormat;
	private Short selectedSourceIsFile;

	public final List<CompareDataFormatTransport> getCompareDataFormatTransports() {
		return this.compareDataFormatTransports;
	}

	public final void setCompareDataFormatTransports(final List<CompareDataFormatTransport> compareDataFormatTransports) {
		this.compareDataFormatTransports = compareDataFormatTransports;
	}

	public final List<CapitalsourceTransport> getCapitalsourceTransports() {
		return this.capitalsourceTransports;
	}

	public final void setCapitalsourceTransports(final List<CapitalsourceTransport> capitalsourceTransports) {
		this.capitalsourceTransports = capitalsourceTransports;
	}

	public final Long getSelectedCapitalsourceId() {
		return this.selectedCapitalsourceId;
	}

	public final void setSelectedCapitalsourceId(final Long selectedCapitalsourceId) {
		this.selectedCapitalsourceId = selectedCapitalsourceId;
	}

	public final Long getSelectedDataFormat() {
		return this.selectedDataFormat;
	}

	public final void setSelectedDataFormat(final Long selectedDataFormat) {
		this.selectedDataFormat = selectedDataFormat;
	}

	public Short getSelectedSourceIsFile() {
		return selectedSourceIsFile;
	}

	public void setSelectedSourceIsFile(Short selectedSourceIsFile) {
		this.selectedSourceIsFile = selectedSourceIsFile;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((capitalsourceTransports == null) ? 0 : capitalsourceTransports.hashCode());
		result = prime * result + ((compareDataFormatTransports == null) ? 0 : compareDataFormatTransports.hashCode());
		result = prime * result + ((selectedCapitalsourceId == null) ? 0 : selectedCapitalsourceId.hashCode());
		result = prime * result + ((selectedDataFormat == null) ? 0 : selectedDataFormat.hashCode());
		result = prime * result + ((selectedSourceIsFile == null) ? 0 : selectedSourceIsFile.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ShowCompareDataFormResponse other = (ShowCompareDataFormResponse) obj;
		if (capitalsourceTransports == null) {
			if (other.capitalsourceTransports != null) {
				return false;
			}
		} else if (!capitalsourceTransports.equals(other.capitalsourceTransports)) {
			return false;
		}
		if (compareDataFormatTransports == null) {
			if (other.compareDataFormatTransports != null) {
				return false;
			}
		} else if (!compareDataFormatTransports.equals(other.compareDataFormatTransports)) {
			return false;
		}
		if (selectedCapitalsourceId == null) {
			if (other.selectedCapitalsourceId != null) {
				return false;
			}
		} else if (!selectedCapitalsourceId.equals(other.selectedCapitalsourceId)) {
			return false;
		}
		if (selectedDataFormat == null) {
			if (other.selectedDataFormat != null) {
				return false;
			}
		} else if (!selectedDataFormat.equals(other.selectedDataFormat)) {
			return false;
		}
		if (selectedSourceIsFile == null) {
			if (other.selectedSourceIsFile != null) {
				return false;
			}
		} else if (!selectedSourceIsFile.equals(other.selectedSourceIsFile)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ShowCompareDataFormResponse [compareDataFormatTransports=");
		builder.append(compareDataFormatTransports);
		builder.append(", capitalsourceTransports=");
		builder.append(capitalsourceTransports);
		builder.append(", selectedCapitalsourceId=");
		builder.append(selectedCapitalsourceId);
		builder.append(", selectedDataFormat=");
		builder.append(selectedDataFormat);
		builder.append(", selectedSourceIsFile=");
		builder.append(selectedSourceIsFile);
		builder.append("]");
		return builder.toString();
	}

}
