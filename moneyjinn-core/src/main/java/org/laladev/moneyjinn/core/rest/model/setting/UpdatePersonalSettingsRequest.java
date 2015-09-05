package org.laladev.moneyjinn.core.rest.model.setting;

import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("updatePersonalSettingsRequest")
public class UpdatePersonalSettingsRequest extends AbstractUpdateSettingsRequest {
	private String password;

	public final String getPassword() {
		return this.password;
	}

	public final void setPassword(final String password) {
		this.password = password;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((this.password == null) ? 0 : this.password.hashCode());
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
		final UpdatePersonalSettingsRequest other = (UpdatePersonalSettingsRequest) obj;
		if (this.password == null) {
			if (other.password != null) {
				return false;
			}
		} else if (!this.password.equals(other.password)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("UpdatePersonalSettingsRequest [password=");
		builder.append(this.password);
		builder.append(", getLanguage()=");
		builder.append(this.getLanguage());
		builder.append(", getDateFormat()=");
		builder.append(this.getDateFormat());
		builder.append(", getMaxRows()=");
		builder.append(this.getMaxRows());
		builder.append(", getNumFreeMoneyflows()=");
		builder.append(this.getNumFreeMoneyflows());
		builder.append("]");
		return builder.toString();
	}

}
