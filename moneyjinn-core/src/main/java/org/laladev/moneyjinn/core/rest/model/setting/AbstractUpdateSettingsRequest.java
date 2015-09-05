package org.laladev.moneyjinn.core.rest.model.setting;

import org.laladev.moneyjinn.core.rest.model.AbstractRequest;

public abstract class AbstractUpdateSettingsRequest extends AbstractRequest {
	private Integer language;
	private String dateFormat;
	private Integer maxRows;
	private Integer numFreeMoneyflows;

	public final Integer getLanguage() {
		return this.language;
	}

	public final void setLanguage(final Integer language) {
		this.language = language;
	}

	public final String getDateFormat() {
		return this.dateFormat;
	}

	public final void setDateFormat(final String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public final Integer getMaxRows() {
		return this.maxRows;
	}

	public final void setMaxRows(final Integer maxRows) {
		this.maxRows = maxRows;
	}

	public final Integer getNumFreeMoneyflows() {
		return this.numFreeMoneyflows;
	}

	public final void setNumFreeMoneyflows(final Integer numFreeMoneyflows) {
		this.numFreeMoneyflows = numFreeMoneyflows;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((this.dateFormat == null) ? 0 : this.dateFormat.hashCode());
		result = prime * result + ((this.language == null) ? 0 : this.language.hashCode());
		result = prime * result + ((this.maxRows == null) ? 0 : this.maxRows.hashCode());
		result = prime * result + ((this.numFreeMoneyflows == null) ? 0 : this.numFreeMoneyflows.hashCode());
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
		final AbstractUpdateSettingsRequest other = (AbstractUpdateSettingsRequest) obj;
		if (this.dateFormat == null) {
			if (other.dateFormat != null) {
				return false;
			}
		} else if (!this.dateFormat.equals(other.dateFormat)) {
			return false;
		}
		if (this.language == null) {
			if (other.language != null) {
				return false;
			}
		} else if (!this.language.equals(other.language)) {
			return false;
		}
		if (this.maxRows == null) {
			if (other.maxRows != null) {
				return false;
			}
		} else if (!this.maxRows.equals(other.maxRows)) {
			return false;
		}
		if (this.numFreeMoneyflows == null) {
			if (other.numFreeMoneyflows != null) {
				return false;
			}
		} else if (!this.numFreeMoneyflows.equals(other.numFreeMoneyflows)) {
			return false;
		}
		return true;
	}

}
