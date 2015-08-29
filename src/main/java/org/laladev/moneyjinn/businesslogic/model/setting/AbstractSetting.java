package org.laladev.moneyjinn.businesslogic.model.setting;

public abstract class AbstractSetting<T> {

	private T setting;

	public abstract SettingType getType();

	public final T getSetting() {
		return setting;
	}

	public final void setSetting(final T setting) {
		this.setting = setting;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("AbstractSetting [setting=").append(setting).append(", getType()=").append(getType())
				.append("]");
		return builder.toString();
	}

}
