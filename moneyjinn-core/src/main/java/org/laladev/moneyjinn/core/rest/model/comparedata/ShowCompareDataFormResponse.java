package org.laladev.moneyjinn.core.rest.model.comparedata;

import java.util.List;

import org.laladev.moneyjinn.core.rest.model.AbstractResponse;
import org.laladev.moneyjinn.core.rest.model.transport.CapitalsourceTransport;
import org.laladev.moneyjinn.core.rest.model.transport.CompareDataFormatTransport;

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

	public final List<CompareDataFormatTransport> getCompareDataFormatTransports() {
		return this.compareDataFormatTransports;
	}

	public final void setCompareDataFormatTransports(
			final List<CompareDataFormatTransport> compareDataFormatTransports) {
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((this.capitalsourceTransports == null) ? 0 : this.capitalsourceTransports.hashCode());
		result = prime * result
				+ ((this.compareDataFormatTransports == null) ? 0 : this.compareDataFormatTransports.hashCode());
		result = prime * result
				+ ((this.selectedCapitalsourceId == null) ? 0 : this.selectedCapitalsourceId.hashCode());
		result = prime * result + ((this.selectedDataFormat == null) ? 0 : this.selectedDataFormat.hashCode());
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
		final ShowCompareDataFormResponse other = (ShowCompareDataFormResponse) obj;
		if (this.capitalsourceTransports == null) {
			if (other.capitalsourceTransports != null) {
				return false;
			}
		} else if (!this.capitalsourceTransports.equals(other.capitalsourceTransports)) {
			return false;
		}
		if (this.compareDataFormatTransports == null) {
			if (other.compareDataFormatTransports != null) {
				return false;
			}
		} else if (!this.compareDataFormatTransports.equals(other.compareDataFormatTransports)) {
			return false;
		}
		if (this.selectedCapitalsourceId == null) {
			if (other.selectedCapitalsourceId != null) {
				return false;
			}
		} else if (!this.selectedCapitalsourceId.equals(other.selectedCapitalsourceId)) {
			return false;
		}
		if (this.selectedDataFormat == null) {
			if (other.selectedDataFormat != null) {
				return false;
			}
		} else if (!this.selectedDataFormat.equals(other.selectedDataFormat)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("ShowCompareDataFormResponse [compareDataFormatTransports=");
		builder.append(this.compareDataFormatTransports);
		builder.append(", capitalsourceTransports=");
		builder.append(this.capitalsourceTransports);
		builder.append(", selectedCapitalsourceId=");
		builder.append(this.selectedCapitalsourceId);
		builder.append(", selectedDataFormat=");
		builder.append(this.selectedDataFormat);
		builder.append("]");
		return builder.toString();
	}

}
