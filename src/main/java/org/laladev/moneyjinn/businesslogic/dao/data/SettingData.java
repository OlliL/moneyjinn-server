package org.laladev.moneyjinn.businesslogic.dao.data;

public class SettingData {
	private Long accessId;
	private String name;
	private String value;

	public SettingData() {

	}

	public SettingData(final Long accessId, final String name, final String value) {
		super();
		this.accessId = accessId;
		this.name = name;
		this.value = value;
	}

	public final Long getAccessId() {
		return accessId;
	}

	public final void setAccessId(final Long accessId) {
		this.accessId = accessId;
	}

	public final String getName() {
		return name;
	}

	public final void setName(final String name) {
		this.name = name;
	}

	public final String getValue() {
		return value;
	}

	public final void setValue(final String value) {
		this.value = value;
	}

}
