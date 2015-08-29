package org.laladev.moneyjinn.businesslogic.dao;

import javax.inject.Inject;
import javax.inject.Named;

import org.laladev.moneyjinn.businesslogic.dao.data.SettingData;
import org.laladev.moneyjinn.businesslogic.dao.mapper.ISettingDaoMapper;

/**
 * The DAO handling Setting information stored in the Table <code>settings</code>
 *
 * @author olivleh1
 *
 */
@Named
public class SettingDao {

	@Inject
	ISettingDaoMapper mapper;

	public SettingData getSetting(final Long accessId, final String name) {
		return this.mapper.getSetting(accessId, name);
	}

	public void setSetting(final SettingData settingData) {
		this.mapper.setSetting(settingData);
	}

	public void deleteSettings(final Long id) {
		this.mapper.deleteSettings(id);
	}

}
