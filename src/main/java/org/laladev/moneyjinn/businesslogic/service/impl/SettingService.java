package org.laladev.moneyjinn.businesslogic.service.impl;

//Copyright (c) 2015 Oliver Lehmann <oliver@laladev.org>
//All rights reserved.
//
//Redistribution and use in source and binary forms, with or without
//modification, are permitted provided that the following conditions
//are met:
//1. Redistributions of source code must retain the above copyright
//notice, this list of conditions and the following disclaimer
//2. Redistributions in binary form must reproduce the above copyright
//notice, this list of conditions and the following disclaimer in the
//documentation and/or other materials provided with the distribution.
//
//THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS'' AND
//ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
//IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
//ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE LIABLE
//FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
//DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
//OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
//HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
//LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
//OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
//SUCH DAMAGE.

import javax.inject.Inject;
import javax.inject.Named;

import org.laladev.moneyjinn.businesslogic.dao.SettingDao;
import org.laladev.moneyjinn.businesslogic.dao.data.SettingData;
import org.laladev.moneyjinn.businesslogic.dao.data.mapper.SettingTypeConverter;
import org.laladev.moneyjinn.businesslogic.model.access.AccessID;
import org.laladev.moneyjinn.businesslogic.model.access.UserID;
import org.laladev.moneyjinn.businesslogic.model.setting.ClientCurrentlyValidCapitalsourcesSetting;
import org.laladev.moneyjinn.businesslogic.model.setting.ClientCurrentlyValidContractpartnerSetting;
import org.laladev.moneyjinn.businesslogic.model.setting.ClientDateFormatSetting;
import org.laladev.moneyjinn.businesslogic.model.setting.ClientDisplayedLanguageSetting;
import org.laladev.moneyjinn.businesslogic.model.setting.ClientMaxRowsSetting;
import org.laladev.moneyjinn.businesslogic.model.setting.ClientNumFreeMoneyflowsSetting;
import org.laladev.moneyjinn.businesslogic.model.setting.SettingType;
import org.laladev.moneyjinn.businesslogic.service.api.ISettingService;
import org.springframework.util.Assert;

@Named
public class SettingService extends AbstractService implements ISettingService {
	private static final AccessID ROOT_ACCESS_ID = new AccessID(0L);
	@Inject
	private SettingDao settingDao;

	@Override
	protected void addBeanMapper() {
	}

	@Override
	public ClientMaxRowsSetting getClientMaxRowsSetting(final AccessID accessId) {
		Assert.notNull(accessId);
		final SettingData settingData = this.settingDao.getSetting(accessId.getId(),
				SettingTypeConverter.getSettingNameByType(SettingType.CLIENT_MAX_ROWS));
		return new ClientMaxRowsSetting(Integer.valueOf(settingData.getValue()));
	}

	@Override
	public void setClientMaxRowsSetting(final AccessID accessId, final ClientMaxRowsSetting setting) {
		Assert.notNull(accessId);
		Assert.notNull(setting);
		Assert.notNull(setting.getSetting());
		final SettingData settingData = new SettingData(accessId.getId(),
				SettingTypeConverter.getSettingNameByType(SettingType.CLIENT_MAX_ROWS),
				setting.getSetting().toString());
		this.settingDao.setSetting(settingData);
	}

	@Override
	public ClientCurrentlyValidCapitalsourcesSetting getClientCurrentlyValidCapitalsourcesSetting(
			final AccessID accessId) {
		Assert.notNull(accessId);
		final SettingData settingData = this.settingDao.getSetting(accessId.getId(),
				SettingTypeConverter.getSettingNameByType(SettingType.CLIENT_CURRENTLY_VALID_CAPITALSOURCES));
		Boolean setting = null;
		if (settingData != null && "1".equals(settingData.getValue())) {
			setting = Boolean.TRUE;
		} else {
			setting = Boolean.FALSE;
		}
		return new ClientCurrentlyValidCapitalsourcesSetting(setting);
	}

	@Override
	public void setClientCurrentlyValidCapitalsourcesSetting(final AccessID accessId,
			final ClientCurrentlyValidCapitalsourcesSetting setting) {
		Assert.notNull(accessId);
		Assert.notNull(setting);
		Assert.notNull(setting.getSetting());
		String settingValue = "0";
		if (Boolean.TRUE.equals(setting.getSetting())) {
			settingValue = "1";
		}
		final SettingData settingData = new SettingData(accessId.getId(),
				SettingTypeConverter.getSettingNameByType(SettingType.CLIENT_CURRENTLY_VALID_CAPITALSOURCES),
				settingValue);
		this.settingDao.setSetting(settingData);
	}

	@Override
	public ClientCurrentlyValidContractpartnerSetting getClientCurrentlyValidContractpartnerSetting(
			final AccessID accessId) {
		Assert.notNull(accessId);
		final SettingData settingData = this.settingDao.getSetting(accessId.getId(),
				SettingTypeConverter.getSettingNameByType(SettingType.CLIENT_CURRENTLY_VALID_CONTRACTPARTNER));
		Boolean setting = null;
		if (settingData != null && "1".equals(settingData.getValue())) {
			setting = Boolean.TRUE;
		} else {
			setting = Boolean.FALSE;
		}
		return new ClientCurrentlyValidContractpartnerSetting(setting);
	}

	@Override
	public void setClientCurrentlyValidContractpartnerSetting(final AccessID accessId,
			final ClientCurrentlyValidContractpartnerSetting setting) {
		Assert.notNull(accessId);
		Assert.notNull(setting);
		Assert.notNull(setting.getSetting());
		String settingValue = "0";
		if (Boolean.TRUE.equals(setting.getSetting())) {
			settingValue = "1";
		}
		final SettingData settingData = new SettingData(accessId.getId(),
				SettingTypeConverter.getSettingNameByType(SettingType.CLIENT_CURRENTLY_VALID_CONTRACTPARTNER),
				settingValue);
		this.settingDao.setSetting(settingData);
	}

	@Override
	public ClientDisplayedLanguageSetting getClientDisplayedLanguageSetting(final AccessID accessId) {
		Assert.notNull(accessId);
		final SettingData settingData = this.settingDao.getSetting(accessId.getId(),
				SettingTypeConverter.getSettingNameByType(SettingType.CLIENT_DISPLAYED_LANGUAGE));
		return new ClientDisplayedLanguageSetting(Integer.valueOf(settingData.getValue()));
	}

	@Override
	public void setClientDisplayedLanguageSetting(final AccessID accessId,
			final ClientDisplayedLanguageSetting setting) {
		Assert.notNull(accessId);
		Assert.notNull(setting);
		Assert.notNull(setting.getSetting());
		final SettingData settingData = new SettingData(accessId.getId(),
				SettingTypeConverter.getSettingNameByType(SettingType.CLIENT_DISPLAYED_LANGUAGE),
				setting.getSetting().toString());
		this.settingDao.setSetting(settingData);

	}

	@Override
	public ClientNumFreeMoneyflowsSetting getClientNumFreeMoneyflowsSetting(final AccessID accessId) {
		Assert.notNull(accessId);
		final SettingData settingData = this.settingDao.getSetting(accessId.getId(),
				SettingTypeConverter.getSettingNameByType(SettingType.CLIENT_NUM_FREE_MONEYFLOWS));
		return new ClientNumFreeMoneyflowsSetting(Integer.valueOf(settingData.getValue()));
	}

	@Override
	public void setClientNumFreeMoneyflowsSetting(final AccessID accessId,
			final ClientNumFreeMoneyflowsSetting setting) {
		Assert.notNull(accessId);
		Assert.notNull(setting);
		Assert.notNull(setting.getSetting());
		final SettingData settingData = new SettingData(accessId.getId(),
				SettingTypeConverter.getSettingNameByType(SettingType.CLIENT_NUM_FREE_MONEYFLOWS),
				setting.getSetting().toString());
		this.settingDao.setSetting(settingData);

	}

	@Override
	public ClientDateFormatSetting getClientDateFormatSetting(final AccessID accessId) {
		Assert.notNull(accessId);
		final SettingData settingData = this.settingDao.getSetting(accessId.getId(),
				SettingTypeConverter.getSettingNameByType(SettingType.CLIENT_DATE_FORMAT));
		return new ClientDateFormatSetting(settingData.getValue());
	}

	@Override
	public void setClientDateFormatSetting(final AccessID accessId, final ClientDateFormatSetting setting) {
		Assert.notNull(accessId);
		Assert.notNull(setting);
		Assert.notNull(setting.getSetting());
		final SettingData settingData = new SettingData(accessId.getId(),
				SettingTypeConverter.getSettingNameByType(SettingType.CLIENT_DATE_FORMAT), setting.getSetting());
		this.settingDao.setSetting(settingData);
	}

	@Override
	public void initSettings(final UserID userId) {
		Assert.notNull(userId);
		this.setClientDateFormatSetting(userId, this.getClientDateFormatSetting(ROOT_ACCESS_ID));
		this.setClientDisplayedLanguageSetting(userId, this.getClientDisplayedLanguageSetting(ROOT_ACCESS_ID));
		this.setClientMaxRowsSetting(userId, this.getClientMaxRowsSetting(ROOT_ACCESS_ID));
		this.setClientNumFreeMoneyflowsSetting(userId, this.getClientNumFreeMoneyflowsSetting(ROOT_ACCESS_ID));
	}

	@Override
	public void deleteSettings(final UserID userId) {
		Assert.notNull(userId);
		this.settingDao.deleteSettings(userId.getId());
	}
}
