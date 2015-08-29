package org.laladev.moneyjinn.businesslogic.service.impl;

import javax.inject.Inject;
import javax.inject.Named;

import org.laladev.moneyjinn.businesslogic.dao.SettingDao;
import org.laladev.moneyjinn.businesslogic.dao.data.SettingData;
import org.laladev.moneyjinn.businesslogic.dao.data.mapper.SettingTypeConverter;
import org.laladev.moneyjinn.businesslogic.model.access.AccessID;
import org.laladev.moneyjinn.businesslogic.model.access.UserID;
import org.laladev.moneyjinn.businesslogic.model.setting.ClientDateFormatSetting;
import org.laladev.moneyjinn.businesslogic.model.setting.ClientDisplayedLanguageSetting;
import org.laladev.moneyjinn.businesslogic.model.setting.ClientMaxRowsSetting;
import org.laladev.moneyjinn.businesslogic.model.setting.ClientNumFreeMoneyflowsSetting;
import org.laladev.moneyjinn.businesslogic.model.setting.SettingType;
import org.laladev.moneyjinn.businesslogic.service.api.ISettingService;

@Named
public class SettingService extends AbstractService implements ISettingService {
	@Inject
	private SettingDao settingDao;

	@Override
	protected void addBeanMapper() {
	}

	@Override
	public ClientMaxRowsSetting getClientMaxRowsSetting(final AccessID accessId) {
		final SettingData settingData = this.settingDao.getSetting(accessId.getId(),
				SettingTypeConverter.getSettingNameByType(SettingType.CLIENT_MAX_ROWS));
		return new ClientMaxRowsSetting(Integer.valueOf(settingData.getValue()));
	}

	@Override
	public void setClientMaxRowsSetting(final AccessID accessId, final ClientMaxRowsSetting setting) {
		final SettingData settingData = new SettingData(accessId.getId(),
				SettingTypeConverter.getSettingNameByType(SettingType.CLIENT_MAX_ROWS),
				setting.getSetting().toString());
		this.settingDao.setSetting(settingData);
	}

	@Override
	public ClientDateFormatSetting getClientDateFormatSetting(final AccessID accessId) {
		final SettingData settingData = this.settingDao.getSetting(accessId.getId(),
				SettingTypeConverter.getSettingNameByType(SettingType.CLIENT_DATE_FORMAT));
		return new ClientDateFormatSetting(settingData.getValue());
	}

	@Override
	public void setClientDateFormatSetting(final AccessID accessId, final ClientDateFormatSetting setting) {
		final SettingData settingData = new SettingData(accessId.getId(),
				SettingTypeConverter.getSettingNameByType(SettingType.CLIENT_DATE_FORMAT), setting.getSetting());
		this.settingDao.setSetting(settingData);
	}

	@Override
	public ClientDisplayedLanguageSetting getClientDisplayedLanguageSetting(final AccessID accessId) {
		final SettingData settingData = this.settingDao.getSetting(accessId.getId(),
				SettingTypeConverter.getSettingNameByType(SettingType.CLIENT_DISPLAYED_LANGUAGE));
		return new ClientDisplayedLanguageSetting(Integer.valueOf(settingData.getValue()));
	}

	@Override
	public void setClientDisplayedLanguageSetting(final AccessID accessId,
			final ClientDisplayedLanguageSetting setting) {
		final SettingData settingData = new SettingData(accessId.getId(),
				SettingTypeConverter.getSettingNameByType(SettingType.CLIENT_DISPLAYED_LANGUAGE),
				setting.getSetting().toString());
		this.settingDao.setSetting(settingData);

	}

	@Override
	public ClientNumFreeMoneyflowsSetting getClientNumFreeMoneyflowsSetting(final AccessID accessId) {
		final SettingData settingData = this.settingDao.getSetting(accessId.getId(),
				SettingTypeConverter.getSettingNameByType(SettingType.CLIENT_NUM_FREE_MONEYFLOWS));
		return new ClientNumFreeMoneyflowsSetting(Integer.valueOf(settingData.getValue()));
	}

	@Override
	public void setClientNumFreeMoneyflowsSetting(final AccessID accessId,
			final ClientNumFreeMoneyflowsSetting setting) {
		final SettingData settingData = new SettingData(accessId.getId(),
				SettingTypeConverter.getSettingNameByType(SettingType.CLIENT_NUM_FREE_MONEYFLOWS),
				setting.getSetting().toString());
		this.settingDao.setSetting(settingData);

	}

	@Override
	public void initSettings(final UserID userId) {
		this.setClientDateFormatSetting(userId, this.getClientDateFormatSetting(new AccessID(0l)));
		this.setClientDisplayedLanguageSetting(userId, this.getClientDisplayedLanguageSetting(new AccessID(0l)));
		this.setClientMaxRowsSetting(userId, this.getClientMaxRowsSetting(new AccessID(0l)));
		this.setClientNumFreeMoneyflowsSetting(userId, this.getClientNumFreeMoneyflowsSetting(new AccessID(0l)));
	}

	@Override
	public void deleteSettings(final UserID userId) {
		this.settingDao.deleteSettings(userId.getId());
	}
}
