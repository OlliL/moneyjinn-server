//
// Copyright (c) 2015-2024 Oliver Lehmann <lehmann@ans-netz.de>
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

import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;

import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.setting.AbstractSetting;
import org.laladev.moneyjinn.model.setting.ClientCalcEtfSalePieces;
import org.laladev.moneyjinn.model.setting.ClientCompareDataSelectedCapitalsource;
import org.laladev.moneyjinn.model.setting.ClientCompareDataSelectedFormat;
import org.laladev.moneyjinn.model.setting.ClientCompareDataSelectedSourceIsFile;
import org.laladev.moneyjinn.model.setting.ClientListEtfDepotDefaultEtfId;
import org.laladev.moneyjinn.model.setting.ClientReportingUnselectedPostingAccountIdsSetting;
import org.laladev.moneyjinn.model.setting.ClientTrendCapitalsourceIDsSetting;
import org.laladev.moneyjinn.service.api.ISettingService;
import org.laladev.moneyjinn.service.dao.SettingDao;
import org.laladev.moneyjinn.service.dao.data.SettingData;
import org.laladev.moneyjinn.service.dao.data.mapper.SettingNameConverter;
import org.springframework.util.Assert;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Named
@RequiredArgsConstructor(onConstructor = @__(@Inject))
@Log
public class SettingService extends AbstractService implements ISettingService {

	private static final String SETTING_GET_SETTING_MUST_NOT_BE_NULL = "setting.getSetting() must not be null!";
	private static final String SETTING_MUST_NOT_BE_NULL = "setting must not be null!";
	private static final String ACCESS_ID_MUST_NOT_BE_NULL = "userId must not be null!";
	private final SettingDao settingDao;
	private final ObjectMapper objectMapper;

	@Override
	@PostConstruct
	protected void addBeanMapper() {
		// no Mapper needed
	}

	@Override
	public void deleteSettings(final UserID userId) {
		Assert.notNull(userId, ACCESS_ID_MUST_NOT_BE_NULL);
		this.settingDao.deleteSettings(userId.getId());
	}

	@Override
	public void deleteSetting(final UserID userId, final AbstractSetting<?> setting) {
		Assert.notNull(userId, ACCESS_ID_MUST_NOT_BE_NULL);
		Assert.notNull(setting, SETTING_MUST_NOT_BE_NULL);
		this.settingDao.deleteSetting(userId.getId(),
				SettingNameConverter.getSettingNameByClassName(setting.getClass().getSimpleName()));
	}

	private void setSetting(final UserID userId, final AbstractSetting<?> setting) {
		Assert.notNull(userId, ACCESS_ID_MUST_NOT_BE_NULL);
		Assert.notNull(setting, SETTING_MUST_NOT_BE_NULL);
		Assert.notNull(setting.getSetting(), SETTING_GET_SETTING_MUST_NOT_BE_NULL);

		try {
			final String settingString = this.objectMapper.writeValueAsString(setting);

			final SettingData settingData = new SettingData(userId.getId(),
					SettingNameConverter.getSettingNameByClassName(setting.getClass().getSimpleName()), settingString);
			this.settingDao.setSetting(settingData);
		} catch (final JsonProcessingException e) {
			log.log(Level.SEVERE, "Setting-JSON can not be created!", e);
		}
	}

	private <T> Optional<T> getSetting(final UserID userId, final Class<T> clazz) {
		Assert.notNull(userId, ACCESS_ID_MUST_NOT_BE_NULL);
		final SettingData settingData = this.settingDao.getSetting(userId.getId(),
				SettingNameConverter.getSettingNameByClassName(clazz.getSimpleName()));
		if (settingData != null) {
			try {
				final T setting = this.objectMapper.readValue(settingData.getValue(), clazz);
				return Optional.of(setting);
			} catch (final IOException e) {
				log.log(Level.SEVERE, "Setting-JSON can not be mapped!", e);
			}
		}
		return Optional.empty();
	}

	@Override
	public void setClientReportingUnselectedPostingAccountIdsSetting(final UserID userId,
			final ClientReportingUnselectedPostingAccountIdsSetting setting) {
		this.setSetting(userId, setting);
	}

	@Override
	public Optional<ClientReportingUnselectedPostingAccountIdsSetting> getClientReportingUnselectedPostingAccountIdsSetting(
			final UserID userId) {
		return this.getSetting(userId, ClientReportingUnselectedPostingAccountIdsSetting.class);
	}

	@Override
	public void setClientTrendCapitalsourceIDsSetting(final UserID userId,
			final ClientTrendCapitalsourceIDsSetting setting) {
		this.setSetting(userId, setting);
	}

	@Override
	public Optional<ClientTrendCapitalsourceIDsSetting> getClientTrendCapitalsourceIDsSetting(final UserID userId) {
		return this.getSetting(userId, ClientTrendCapitalsourceIDsSetting.class);
	}

	@Override
	public void setClientCompareDataSelectedCapitalsource(final UserID userId,
			final ClientCompareDataSelectedCapitalsource setting) {
		this.setSetting(userId, setting);
	}

	@Override
	public Optional<ClientCompareDataSelectedCapitalsource> getClientCompareDataSelectedCapitalsource(
			final UserID userId) {
		return this.getSetting(userId, ClientCompareDataSelectedCapitalsource.class);
	}

	@Override
	public void setClientCompareDataSelectedFormat(final UserID userId, final ClientCompareDataSelectedFormat setting) {
		this.setSetting(userId, setting);
	}

	@Override
	public Optional<ClientCompareDataSelectedFormat> getClientCompareDataSelectedFormat(final UserID userId) {
		return this.getSetting(userId, ClientCompareDataSelectedFormat.class);
	}

	@Override
	public void setClientCompareDataSelectedSourceIsFile(final UserID userId,
			final ClientCompareDataSelectedSourceIsFile setting) {
		this.setSetting(userId, setting);
	}

	@Override
	public Optional<ClientCompareDataSelectedSourceIsFile> getClientCompareDataSelectedSourceIsFile(
			final UserID userId) {
		return this.getSetting(userId, ClientCompareDataSelectedSourceIsFile.class);
	}

	@Override
	public void setClientListEtfDepotDefaultEtfId(final UserID userId, final ClientListEtfDepotDefaultEtfId setting) {
		this.setSetting(userId, setting);
	}

	@Override
	public Optional<ClientListEtfDepotDefaultEtfId> getClientListEtfDepotDefaultEtfId(final UserID userId) {
		return this.getSetting(userId, ClientListEtfDepotDefaultEtfId.class);
	}

	@Override
	public void setClientCalcEtfSalePieces(final UserID userId, final ClientCalcEtfSalePieces setting) {
		this.setSetting(userId, setting);
	}

	@Override
	public Optional<ClientCalcEtfSalePieces> getClientCalcEtfSalePieces(final UserID userId) {
		return this.getSetting(userId, ClientCalcEtfSalePieces.class);
	}
}
