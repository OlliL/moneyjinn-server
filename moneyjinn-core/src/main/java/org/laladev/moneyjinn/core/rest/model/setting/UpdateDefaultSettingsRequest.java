package org.laladev.moneyjinn.core.rest.model.setting;

import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("updateDefaultSettingsRequest")
public class UpdateDefaultSettingsRequest extends AbstractUpdateSettingsRequest {

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("UpdateDefaultSettingsRequest [getLanguage()=");
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
