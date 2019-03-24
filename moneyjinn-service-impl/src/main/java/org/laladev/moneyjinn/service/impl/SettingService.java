//
// Copyright (c) 2015 Oliver Lehmann <lehmann@ans-netz.de>
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

package org.laladev.moneyjinn.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Arrays;

import javax.inject.Inject;
import javax.inject.Named;

import org.laladev.moneyjinn.model.PostingAccountID;
import org.laladev.moneyjinn.model.access.AccessID;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.capitalsource.CapitalsourceID;
import org.laladev.moneyjinn.model.comparedata.CompareDataFormatID;
import org.laladev.moneyjinn.model.setting.ClientCompareDataSelectedCapitalsource;
import org.laladev.moneyjinn.model.setting.ClientCompareDataSelectedFormat;
import org.laladev.moneyjinn.model.setting.ClientCompareDataSelectedSourceIsFile;
import org.laladev.moneyjinn.model.setting.ClientCurrentlyValidCapitalsourcesSetting;
import org.laladev.moneyjinn.model.setting.ClientCurrentlyValidContractpartnerSetting;
import org.laladev.moneyjinn.model.setting.ClientDateFormatSetting;
import org.laladev.moneyjinn.model.setting.ClientDisplayedLanguageSetting;
import org.laladev.moneyjinn.model.setting.ClientMaxRowsSetting;
import org.laladev.moneyjinn.model.setting.ClientReportingUnselectedPostingAccountIdsSetting;
import org.laladev.moneyjinn.model.setting.ClientTrendCapitalsourceIDsSetting;
import org.laladev.moneyjinn.model.setting.SettingType;
import org.laladev.moneyjinn.service.api.ISettingService;
import org.laladev.moneyjinn.service.dao.SettingDao;
import org.laladev.moneyjinn.service.dao.data.SettingData;
import org.laladev.moneyjinn.service.dao.data.mapper.SettingTypeConverter;
import org.springframework.util.Assert;

//Copyright (c) 2015 Oliver Lehmann <lehmann@ans-netz.de>
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

@Named
public class SettingService extends AbstractService implements ISettingService {
	private static final AccessID ROOT_ACCESS_ID = new AccessID(0L);
	@Inject
	private SettingDao settingDao;
	@Inject
	private ObjectMapper objectMapper;

	@Override
	protected void addBeanMapper() {
		// no Mapper needed
	}

	@Override
	public ClientReportingUnselectedPostingAccountIdsSetting getClientReportingUnselectedPostingAccountIdsSetting(
			final AccessID accessId) {
		Assert.notNull(accessId, "accessId must not be null!");
		final SettingData settingData = this.settingDao.getSetting(accessId.getId(),
				SettingTypeConverter.getSettingNameByType(SettingType.CLIENT_REPORTING_UNSELECTED_POSTINGACCOUNTIDS));
		if (settingData != null) {
			try {
				final PostingAccountID[] postingAccountIds = this.objectMapper.readValue(settingData.getValue(),
						PostingAccountID[].class);
				return new ClientReportingUnselectedPostingAccountIdsSetting(Arrays.asList(postingAccountIds));
			} catch (final IOException e) {
			}
		}
		return null;
	}

	@Override
	public void setClientReportingUnselectedPostingAccountIdsSetting(final AccessID accessId,
			final ClientReportingUnselectedPostingAccountIdsSetting setting) {
		Assert.notNull(accessId, "accessId must not be null!");
		Assert.notNull(setting, "setting must not be null!");
		Assert.notNull(setting.getSetting(), "setting.getSetting() must not be null!");

		try {
			final String settingString = this.objectMapper
					.writeValueAsString(setting.getSetting().toArray(new PostingAccountID[0]));
			final SettingData settingData = new SettingData(accessId.getId(), SettingTypeConverter
					.getSettingNameByType(SettingType.CLIENT_REPORTING_UNSELECTED_POSTINGACCOUNTIDS), settingString);
			this.settingDao.setSetting(settingData);
		} catch (final JsonProcessingException e) {
		}
	}

	@Override
	public ClientTrendCapitalsourceIDsSetting getClientTrendCapitalsourceIDsSetting(final AccessID accessId) {
		Assert.notNull(accessId, "accessId must not be null!");
		final SettingData settingData = this.settingDao.getSetting(accessId.getId(),
				SettingTypeConverter.getSettingNameByType(SettingType.CLIENT_TREND_CAPITALSOURCEIDS));
		if (settingData != null) {
			try {
				final CapitalsourceID[] capitalsourceIds = this.objectMapper.readValue(settingData.getValue(),
						CapitalsourceID[].class);
				return new ClientTrendCapitalsourceIDsSetting(Arrays.asList(capitalsourceIds));
			} catch (final IOException e) {
			}
		}
		return null;
	}

	@Override
	public void setClientTrendCapitalsourceIDsSetting(final AccessID accessId,
			final ClientTrendCapitalsourceIDsSetting setting) {
		Assert.notNull(accessId, "accessId must not be null!");
		Assert.notNull(setting, "setting must not be null!");
		Assert.notNull(setting.getSetting(), "setting.getSetting() must not be null!");

		try {
			final String settingString = this.objectMapper
					.writeValueAsString(setting.getSetting().toArray(new CapitalsourceID[0]));
			final SettingData settingData = new SettingData(accessId.getId(),
					SettingTypeConverter.getSettingNameByType(SettingType.CLIENT_TREND_CAPITALSOURCEIDS),
					settingString);
			this.settingDao.setSetting(settingData);
		} catch (final JsonProcessingException e) {
		}
	}

	@Override
	public ClientMaxRowsSetting getClientMaxRowsSetting(final AccessID accessId) {
		Assert.notNull(accessId, "accessId must not be null!");
		final SettingData settingData = this.settingDao.getSetting(accessId.getId(),
				SettingTypeConverter.getSettingNameByType(SettingType.CLIENT_MAX_ROWS));
		return new ClientMaxRowsSetting(Integer.valueOf(settingData.getValue()));
	}

	@Override
	public void setClientMaxRowsSetting(final AccessID accessId, final ClientMaxRowsSetting setting) {
		Assert.notNull(accessId, "accessId must not be null!");
		Assert.notNull(setting, "setting must not be null!");
		Assert.notNull(setting.getSetting(), "setting.getSetting() must not be null!");
		final SettingData settingData = new SettingData(accessId.getId(),
				SettingTypeConverter.getSettingNameByType(SettingType.CLIENT_MAX_ROWS),
				setting.getSetting().toString());
		this.settingDao.setSetting(settingData);
	}

	@Override
	public ClientCurrentlyValidCapitalsourcesSetting getClientCurrentlyValidCapitalsourcesSetting(
			final AccessID accessId) {
		Assert.notNull(accessId, "accessId must not be null!");
		final SettingData settingData = this.settingDao.getSetting(accessId.getId(),
				SettingTypeConverter.getSettingNameByType(SettingType.CLIENT_CURRENTLY_VALID_CAPITALSOURCES));
		Boolean setting;
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
		Assert.notNull(accessId, "accessId must not be null!");
		Assert.notNull(setting, "setting must not be null!");
		Assert.notNull(setting.getSetting(), "setting.getSetting() must not be null!");
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
		Assert.notNull(accessId, "accessId must not be null!");
		final SettingData settingData = this.settingDao.getSetting(accessId.getId(),
				SettingTypeConverter.getSettingNameByType(SettingType.CLIENT_CURRENTLY_VALID_CONTRACTPARTNER));
		Boolean setting;
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
		Assert.notNull(accessId, "accessId must not be null!");
		Assert.notNull(setting, "setting must not be null!");
		Assert.notNull(setting.getSetting(), "setting.getSetting() must not be null!");
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
		Assert.notNull(accessId, "accessId must not be null!");
		final SettingData settingData = this.settingDao.getSetting(accessId.getId(),
				SettingTypeConverter.getSettingNameByType(SettingType.CLIENT_DISPLAYED_LANGUAGE));
		return new ClientDisplayedLanguageSetting(Integer.valueOf(settingData.getValue()));
	}

	@Override
	public void setClientDisplayedLanguageSetting(final AccessID accessId,
			final ClientDisplayedLanguageSetting setting) {
		Assert.notNull(accessId, "accessId must not be null!");
		Assert.notNull(setting, "setting must not be null!");
		Assert.notNull(setting.getSetting(), "setting.getSetting() must not be null!");
		final SettingData settingData = new SettingData(accessId.getId(),
				SettingTypeConverter.getSettingNameByType(SettingType.CLIENT_DISPLAYED_LANGUAGE),
				setting.getSetting().toString());
		this.settingDao.setSetting(settingData);

	}

	@Override
	public ClientDateFormatSetting getClientDateFormatSetting(final AccessID accessId) {
		Assert.notNull(accessId, "accessId must not be null!");
		final SettingData settingData = this.settingDao.getSetting(accessId.getId(),
				SettingTypeConverter.getSettingNameByType(SettingType.CLIENT_DATE_FORMAT));
		return new ClientDateFormatSetting(settingData.getValue());
	}

	@Override
	public void setClientDateFormatSetting(final AccessID accessId, final ClientDateFormatSetting setting) {
		Assert.notNull(accessId, "accessId must not be null!");
		Assert.notNull(setting, "setting must not be null!");
		Assert.notNull(setting.getSetting(), "setting.getSetting() must not be null!");
		final SettingData settingData = new SettingData(accessId.getId(),
				SettingTypeConverter.getSettingNameByType(SettingType.CLIENT_DATE_FORMAT), setting.getSetting());
		this.settingDao.setSetting(settingData);
	}

	@Override
	public void setClientCompareDataSelectedCapitalsource(final AccessID accessId,
			final ClientCompareDataSelectedCapitalsource setting) {
		Assert.notNull(accessId, "accessId must not be null!");
		Assert.notNull(setting, "setting must not be null!");
		Assert.notNull(setting.getSetting(), "setting.getSetting() must not be null!");
		final SettingData settingData = new SettingData(accessId.getId(),
				SettingTypeConverter.getSettingNameByType(SettingType.CLIENT_COMPARE_DATA_SELECTED_CAPITALSOURCE),
				setting.getSetting().getId().toString());
		this.settingDao.setSetting(settingData);
	}

	@Override
	public ClientCompareDataSelectedCapitalsource getClientCompareDataSelectedCapitalsource(final AccessID accessId) {
		Assert.notNull(accessId, "accessId must not be null!");
		final SettingData settingData = this.settingDao.getSetting(accessId.getId(),
				SettingTypeConverter.getSettingNameByType(SettingType.CLIENT_COMPARE_DATA_SELECTED_CAPITALSOURCE));
		if (settingData != null && settingData.getValue() != null) {
			return new ClientCompareDataSelectedCapitalsource(new CapitalsourceID(new Long(settingData.getValue())));
		}
		return null;
	}

	@Override
	public void setClientCompareDataSelectedFormat(final AccessID accessId,
			final ClientCompareDataSelectedFormat setting) {
		Assert.notNull(accessId, "accessId must not be null!");
		Assert.notNull(setting, "setting must not be null!");
		Assert.notNull(setting.getSetting(), "setting.getSetting() must not be null!");
		final SettingData settingData = new SettingData(accessId.getId(),
				SettingTypeConverter.getSettingNameByType(SettingType.CLIENT_COMPARE_DATA_SELECTED_FORMAT),
				setting.getSetting().getId().toString());
		this.settingDao.setSetting(settingData);

	}

	@Override
	public ClientCompareDataSelectedFormat getClientCompareDataSelectedFormat(final AccessID accessId) {
		Assert.notNull(accessId, "accessId must not be null!");
		final SettingData settingData = this.settingDao.getSetting(accessId.getId(),
				SettingTypeConverter.getSettingNameByType(SettingType.CLIENT_COMPARE_DATA_SELECTED_FORMAT));
		if (settingData != null && settingData.getValue() != null) {
			return new ClientCompareDataSelectedFormat(new CompareDataFormatID(new Long(settingData.getValue())));
		}
		return null;
	}

	@Override
	public void setClientCompareDataSelectedSourceIsFile(final AccessID accessId,
			final ClientCompareDataSelectedSourceIsFile setting) {
		Assert.notNull(accessId, "accessId must not be null!");
		Assert.notNull(setting, "setting must not be null!");
		Assert.notNull(setting.getSetting(), "setting.getSetting() must not be null!");
		final SettingData settingData = new SettingData(accessId.getId(),
				SettingTypeConverter.getSettingNameByType(SettingType.CLIENT_COMPARE_DATA_SELECTED_SOURCE_IS_FILE),
				Boolean.TRUE.equals(setting.getSetting()) ? "1" : "0");
		this.settingDao.setSetting(settingData);

	}

	@Override
	public ClientCompareDataSelectedSourceIsFile getClientCompareDataSelectedSourceIsFile(final AccessID accessId) {
		Assert.notNull(accessId, "accessId must not be null!");
		final SettingData settingData = this.settingDao.getSetting(accessId.getId(),
				SettingTypeConverter.getSettingNameByType(SettingType.CLIENT_COMPARE_DATA_SELECTED_SOURCE_IS_FILE));
		if (settingData != null && settingData.getValue() != null) {
			Boolean result = Boolean.FALSE;
			if (settingData.getValue().compareTo("1") == 0) {
				result = Boolean.TRUE;
			}
			return new ClientCompareDataSelectedSourceIsFile(result);
		}
		return null;
	}

	@Override
	public void initSettings(final UserID userId) {
		Assert.notNull(userId, "UserId must not be null!");
		this.setClientDateFormatSetting(userId, this.getClientDateFormatSetting(ROOT_ACCESS_ID));
		this.setClientDisplayedLanguageSetting(userId, this.getClientDisplayedLanguageSetting(ROOT_ACCESS_ID));
		this.setClientMaxRowsSetting(userId, this.getClientMaxRowsSetting(ROOT_ACCESS_ID));
	}

	@Override
	public void deleteSettings(final UserID userId) {
		Assert.notNull(userId, "UserId must not be null!");
		this.settingDao.deleteSettings(userId.getId());
	}
}
