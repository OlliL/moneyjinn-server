//
// Copyright (c) 2015-2025 Oliver Lehmann <lehmann@ans-netz.de>
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

import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.setting.*;
import org.laladev.moneyjinn.service.api.ISettingService;
import org.laladev.moneyjinn.service.dao.SettingDao;
import org.laladev.moneyjinn.service.dao.data.SettingData;
import org.laladev.moneyjinn.service.dao.data.mapper.SettingNameConverter;
import tools.jackson.databind.ObjectMapper;

import java.util.Optional;

import static org.springframework.util.Assert.notNull;

@Named
@RequiredArgsConstructor(onConstructor = @__(@Inject))
@Log
public class SettingService extends AbstractService implements ISettingService {

    private static final String SETTING_GET_SETTING_MUST_NOT_BE_NULL = "setting.getSetting() must not be null!";
    private final SettingDao settingDao;
    private final ObjectMapper objectMapper;

    @Override
    public void deleteSettings(@NonNull final UserID userId) {
        this.settingDao.deleteSettings(userId.getId());
    }

    @Override
    public void deleteSetting(@NonNull final UserID userId, @NonNull final AbstractSetting<?> setting) {
        this.settingDao.deleteSetting(userId.getId(),
                SettingNameConverter.getSettingNameByClassName(setting.getClass().getSimpleName()));
    }

    private void setSetting(@NonNull final UserID userId, @NonNull final AbstractSetting<?> setting) {
        notNull(setting.getSetting(), SETTING_GET_SETTING_MUST_NOT_BE_NULL);

        final String settingString = this.objectMapper.writeValueAsString(setting);

        final SettingData settingData = new SettingData(userId.getId(),
                SettingNameConverter.getSettingNameByClassName(setting.getClass().getSimpleName()), settingString);
        this.settingDao.setSetting(settingData);
    }

    private <T> Optional<T> getSetting(@NonNull final UserID userId, final Class<T> clazz) {
        final SettingData settingData = this.settingDao.getSetting(userId.getId(),
                SettingNameConverter.getSettingNameByClassName(clazz.getSimpleName()));

        return settingData == null
                ? Optional.empty()
                : Optional.of(this.objectMapper.readValue(settingData.getValue(), clazz));
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
    public void setClientTrendEtfIDsSetting(final UserID userId,
                                            final ClientTrendEtfIDsSetting setting) {
        this.setSetting(userId, setting);
    }

    @Override
    public Optional<ClientTrendEtfIDsSetting> getClientTrendEtfIDsSetting(final UserID userId) {
        return this.getSetting(userId, ClientTrendEtfIDsSetting.class);
    }

    @Override
    public Optional<ClientTrendActiveEtfsSetting> getClientTrendActiveEtfsSetting(final UserID userId) {
        return this.getSetting(userId, ClientTrendActiveEtfsSetting.class);
    }

    @Override
    public void setClientTrendActiveEtfsSetting(final UserID userId, final ClientTrendActiveEtfsSetting setting) {
        this.setSetting(userId, setting);

    }

    @Override
    public Optional<ClientTrendActiveCapitalsourcesSetting> getClientTrendActiveCapitalsourcesSetting(
            final UserID userId) {
        return this.getSetting(userId, ClientTrendActiveCapitalsourcesSetting.class);
    }

    @Override
    public void setClientTrendActiveCapitalsourcesSetting(final UserID userId,
                                                          final ClientTrendActiveCapitalsourcesSetting setting) {
        this.setSetting(userId, setting);

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
