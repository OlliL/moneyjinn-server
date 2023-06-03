//
// Copyright (c) 2015-2023 Oliver Lehmann <lehmann@ans-netz.de>
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
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.laladev.moneyjinn.model.access.AccessID;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.setting.AbstractSetting;
import org.laladev.moneyjinn.model.setting.ClientCalcEtfSaleAskPrice;
import org.laladev.moneyjinn.model.setting.ClientCalcEtfSaleBidPrice;
import org.laladev.moneyjinn.model.setting.ClientCalcEtfSaleIsin;
import org.laladev.moneyjinn.model.setting.ClientCalcEtfSalePieces;
import org.laladev.moneyjinn.model.setting.ClientCalcEtfSaleTransactionCosts;
import org.laladev.moneyjinn.model.setting.ClientCompareDataSelectedCapitalsource;
import org.laladev.moneyjinn.model.setting.ClientCompareDataSelectedFormat;
import org.laladev.moneyjinn.model.setting.ClientCompareDataSelectedSourceIsFile;
import org.laladev.moneyjinn.model.setting.ClientReportingUnselectedPostingAccountIdsSetting;
import org.laladev.moneyjinn.model.setting.ClientTrendCapitalsourceIDsSetting;
import org.laladev.moneyjinn.service.api.ISettingService;
import org.laladev.moneyjinn.service.dao.SettingDao;
import org.laladev.moneyjinn.service.dao.data.SettingData;
import org.laladev.moneyjinn.service.dao.data.mapper.SettingNameConverter;
import org.springframework.util.Assert;

@Named
@RequiredArgsConstructor(onConstructor = @__(@Inject))
@Log
public class SettingService extends AbstractService implements ISettingService {

  private static final String SETTING_GET_SETTING_MUST_NOT_BE_NULL = "setting.getSetting() must not be null!";
  private static final String SETTING_MUST_NOT_BE_NULL = "setting must not be null!";
  private static final String ACCESS_ID_MUST_NOT_BE_NULL = "accessId must not be null!";
  private final SettingDao settingDao;
  private final ObjectMapper objectMapper;

  @Override
  @PostConstruct
  protected void addBeanMapper() {
    // no Mapper needed
  }

  @Override
  public void deleteSettings(final UserID userId) {
    Assert.notNull(userId, "UserId must not be null!");
    this.settingDao.deleteSettings(userId.getId());
  }

  private void setSetting(final AccessID accessId, final AbstractSetting<?> setting) {
    Assert.notNull(accessId, ACCESS_ID_MUST_NOT_BE_NULL);
    Assert.notNull(setting, SETTING_MUST_NOT_BE_NULL);
    Assert.notNull(setting.getSetting(), SETTING_GET_SETTING_MUST_NOT_BE_NULL);

    try {
      final String settingString = this.objectMapper.writeValueAsString(setting);

      final SettingData settingData = new SettingData(accessId.getId(),
          SettingNameConverter.getSettingNameByClassName(setting.getClass().getSimpleName()),
          settingString);
      this.settingDao.setSetting(settingData);
    } catch (final JsonProcessingException e) {
      log.log(Level.SEVERE, "Setting-JSON can not be created!", e);
    }
  }

  private <T> Optional<T> getSetting(final AccessID accessId, final Class<T> clazz) {
    Assert.notNull(accessId, ACCESS_ID_MUST_NOT_BE_NULL);
    final SettingData settingData = this.settingDao.getSetting(accessId.getId(),
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
  public void setClientReportingUnselectedPostingAccountIdsSetting(final AccessID accessId,
      final ClientReportingUnselectedPostingAccountIdsSetting setting) {
    this.setSetting(accessId, setting);
  }

  @Override
  public Optional<ClientReportingUnselectedPostingAccountIdsSetting> getClientReportingUnselectedPostingAccountIdsSetting(
      final AccessID accessId) {
    return this.getSetting(accessId, ClientReportingUnselectedPostingAccountIdsSetting.class);
  }

  @Override
  public void setClientTrendCapitalsourceIDsSetting(final AccessID accessId,
      final ClientTrendCapitalsourceIDsSetting setting) {
    this.setSetting(accessId, setting);
  }

  @Override
  public Optional<ClientTrendCapitalsourceIDsSetting> getClientTrendCapitalsourceIDsSetting(
      final AccessID accessId) {
    return this.getSetting(accessId, ClientTrendCapitalsourceIDsSetting.class);
  }

  @Override
  public void setClientCompareDataSelectedCapitalsource(final AccessID accessId,
      final ClientCompareDataSelectedCapitalsource setting) {
    this.setSetting(accessId, setting);
  }

  @Override
  public Optional<ClientCompareDataSelectedCapitalsource> getClientCompareDataSelectedCapitalsource(
      final AccessID accessId) {
    return this.getSetting(accessId, ClientCompareDataSelectedCapitalsource.class);
  }

  @Override
  public void setClientCompareDataSelectedFormat(final AccessID accessId,
      final ClientCompareDataSelectedFormat setting) {
    this.setSetting(accessId, setting);
  }

  @Override
  public Optional<ClientCompareDataSelectedFormat> getClientCompareDataSelectedFormat(
      final AccessID accessId) {
    return this.getSetting(accessId, ClientCompareDataSelectedFormat.class);
  }

  @Override
  public void setClientCompareDataSelectedSourceIsFile(final AccessID accessId,
      final ClientCompareDataSelectedSourceIsFile setting) {
    this.setSetting(accessId, setting);
  }

  @Override
  public Optional<ClientCompareDataSelectedSourceIsFile> getClientCompareDataSelectedSourceIsFile(
      final AccessID accessId) {
    return this.getSetting(accessId, ClientCompareDataSelectedSourceIsFile.class);
  }

  @Override
  public void setClientCalcEtfSaleIsin(final AccessID accessId,
      final ClientCalcEtfSaleIsin setting) {
    this.setSetting(accessId, setting);
  }

  @Override
  public Optional<ClientCalcEtfSaleIsin> getClientCalcEtfSaleIsin(final AccessID accessId) {
    return this.getSetting(accessId, ClientCalcEtfSaleIsin.class);
  }

  @Override
  public void setClientCalcEtfSaleAskPrice(final AccessID accessId,
      final ClientCalcEtfSaleAskPrice setting) {
    this.setSetting(accessId, setting);
  }

  @Override
  public Optional<ClientCalcEtfSaleAskPrice> getClientCalcEtfSaleAskPrice(final AccessID accessId) {
    return this.getSetting(accessId, ClientCalcEtfSaleAskPrice.class);
  }

  @Override
  public void setClientCalcEtfSaleBidPrice(final AccessID accessId,
      final ClientCalcEtfSaleBidPrice setting) {
    this.setSetting(accessId, setting);
  }

  @Override
  public Optional<ClientCalcEtfSaleBidPrice> getClientCalcEtfSaleBidPrice(final AccessID accessId) {
    return this.getSetting(accessId, ClientCalcEtfSaleBidPrice.class);
  }

  @Override
  public void setClientCalcEtfSalePieces(final AccessID accessId,
      final ClientCalcEtfSalePieces setting) {
    this.setSetting(accessId, setting);
  }

  @Override
  public Optional<ClientCalcEtfSalePieces> getClientCalcEtfSalePieces(final AccessID accessId) {
    return this.getSetting(accessId, ClientCalcEtfSalePieces.class);
  }

  @Override
  public void setClientCalcEtfSaleTransactionCosts(final AccessID accessId,
      final ClientCalcEtfSaleTransactionCosts setting) {
    this.setSetting(accessId, setting);
  }

  @Override
  public Optional<ClientCalcEtfSaleTransactionCosts> getClientCalcEtfSaleTransactionCosts(
      final AccessID accessId) {
    return this.getSetting(accessId, ClientCalcEtfSaleTransactionCosts.class);
  }
}
