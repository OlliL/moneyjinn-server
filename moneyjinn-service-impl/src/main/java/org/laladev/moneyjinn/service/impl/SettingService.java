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
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.laladev.moneyjinn.model.PostingAccountID;
import org.laladev.moneyjinn.model.access.AccessID;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.capitalsource.CapitalsourceID;
import org.laladev.moneyjinn.model.comparedata.CompareDataFormatID;
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
import org.laladev.moneyjinn.model.setting.SettingType;
import org.laladev.moneyjinn.service.api.ISettingService;
import org.laladev.moneyjinn.service.dao.SettingDao;
import org.laladev.moneyjinn.service.dao.data.SettingData;
import org.laladev.moneyjinn.service.dao.data.mapper.SettingTypeConverter;
import org.springframework.util.Assert;

// TODO - copy and paste hell, use Optional!
@Named
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class SettingService extends AbstractService implements ISettingService {
  private static final String SETTING_GET_SETTING_MUST_NOT_BE_NULL = "setting.getSetting() must not be null!";
  private static final String SETTING_MUST_NOT_BE_NULL = "setting must not be null!";
  private static final String ACCESS_ID_MUST_NOT_BE_NULL = "accessId must not be null!";
  private final SettingDao settingDao;
  private final ObjectMapper objectMapper;
  private final SettingTypeConverter settingTypeConverter;

  @Override
  @PostConstruct
  protected void addBeanMapper() {
    // no Mapper needed
  }

  @Override
  public ClientReportingUnselectedPostingAccountIdsSetting getClientReportingUnselectedPostingAccountIdsSetting(
      final AccessID accessId) {
    Assert.notNull(accessId, ACCESS_ID_MUST_NOT_BE_NULL);
    final SettingData settingData = this.settingDao.getSetting(accessId.getId(),
        this.settingTypeConverter
            .getSettingNameByType(SettingType.CLIENT_REPORTING_UNSELECTED_POSTINGACCOUNTIDS));
    if (settingData != null) {
      try {
        final PostingAccountID[] postingAccountIds = this.objectMapper
            .readValue(settingData.getValue(), PostingAccountID[].class);
        return new ClientReportingUnselectedPostingAccountIdsSetting(
            Arrays.asList(postingAccountIds));
      } catch (final IOException e) {
      }
    }
    return null;
  }

  @Override
  public void setClientReportingUnselectedPostingAccountIdsSetting(final AccessID accessId,
      final ClientReportingUnselectedPostingAccountIdsSetting setting) {
    Assert.notNull(accessId, ACCESS_ID_MUST_NOT_BE_NULL);
    Assert.notNull(setting, SETTING_MUST_NOT_BE_NULL);
    Assert.notNull(setting.getSetting(), SETTING_GET_SETTING_MUST_NOT_BE_NULL);
    try {
      final String settingString = this.objectMapper
          .writeValueAsString(setting.getSetting().toArray(new PostingAccountID[0]));
      final SettingData settingData = new SettingData(accessId.getId(), this.settingTypeConverter
          .getSettingNameByType(SettingType.CLIENT_REPORTING_UNSELECTED_POSTINGACCOUNTIDS),
          settingString);
      this.settingDao.setSetting(settingData);
    } catch (final JsonProcessingException e) {
    }
  }

  @Override
  public ClientTrendCapitalsourceIDsSetting getClientTrendCapitalsourceIDsSetting(
      final AccessID accessId) {
    Assert.notNull(accessId, ACCESS_ID_MUST_NOT_BE_NULL);
    final SettingData settingData = this.settingDao.getSetting(accessId.getId(),
        this.settingTypeConverter.getSettingNameByType(SettingType.CLIENT_TREND_CAPITALSOURCEIDS));
    if (settingData != null) {
      try {
        final CapitalsourceID[] capitalsourceIds = this.objectMapper
            .readValue(settingData.getValue(), CapitalsourceID[].class);
        return new ClientTrendCapitalsourceIDsSetting(Arrays.asList(capitalsourceIds));
      } catch (final IOException e) {
      }
    }
    return null;
  }

  @Override
  public void setClientTrendCapitalsourceIDsSetting(final AccessID accessId,
      final ClientTrendCapitalsourceIDsSetting setting) {
    Assert.notNull(accessId, ACCESS_ID_MUST_NOT_BE_NULL);
    Assert.notNull(setting, SETTING_MUST_NOT_BE_NULL);
    Assert.notNull(setting.getSetting(), SETTING_GET_SETTING_MUST_NOT_BE_NULL);
    try {
      final String settingString = this.objectMapper
          .writeValueAsString(setting.getSetting().toArray(new CapitalsourceID[0]));
      final SettingData settingData = new SettingData(accessId.getId(),
          this.settingTypeConverter.getSettingNameByType(SettingType.CLIENT_TREND_CAPITALSOURCEIDS),
          settingString);
      this.settingDao.setSetting(settingData);
    } catch (final JsonProcessingException e) {
    }
  }

  @Override
  public void setClientCompareDataSelectedCapitalsource(final AccessID accessId,
      final ClientCompareDataSelectedCapitalsource setting) {
    Assert.notNull(accessId, ACCESS_ID_MUST_NOT_BE_NULL);
    Assert.notNull(setting, SETTING_MUST_NOT_BE_NULL);
    Assert.notNull(setting.getSetting(), SETTING_GET_SETTING_MUST_NOT_BE_NULL);
    final SettingData settingData = new SettingData(accessId.getId(),
        this.settingTypeConverter
            .getSettingNameByType(SettingType.CLIENT_COMPARE_DATA_SELECTED_CAPITALSOURCE),
        setting.getSetting().getId().toString());
    this.settingDao.setSetting(settingData);
  }

  @Override
  public ClientCompareDataSelectedCapitalsource getClientCompareDataSelectedCapitalsource(
      final AccessID accessId) {
    Assert.notNull(accessId, ACCESS_ID_MUST_NOT_BE_NULL);
    final SettingData settingData = this.settingDao.getSetting(accessId.getId(),
        this.settingTypeConverter
            .getSettingNameByType(SettingType.CLIENT_COMPARE_DATA_SELECTED_CAPITALSOURCE));
    if (settingData != null && settingData.getValue() != null) {
      return new ClientCompareDataSelectedCapitalsource(
          new CapitalsourceID(Long.valueOf(settingData.getValue())));
    }
    return null;
  }

  @Override
  public void setClientCompareDataSelectedFormat(final AccessID accessId,
      final ClientCompareDataSelectedFormat setting) {
    Assert.notNull(accessId, ACCESS_ID_MUST_NOT_BE_NULL);
    Assert.notNull(setting, SETTING_MUST_NOT_BE_NULL);
    Assert.notNull(setting.getSetting(), SETTING_GET_SETTING_MUST_NOT_BE_NULL);
    final SettingData settingData = new SettingData(accessId.getId(),
        this.settingTypeConverter
            .getSettingNameByType(SettingType.CLIENT_COMPARE_DATA_SELECTED_FORMAT),
        setting.getSetting().getId().toString());
    this.settingDao.setSetting(settingData);
  }

  @Override
  public ClientCompareDataSelectedFormat getClientCompareDataSelectedFormat(
      final AccessID accessId) {
    Assert.notNull(accessId, ACCESS_ID_MUST_NOT_BE_NULL);
    final SettingData settingData = this.settingDao.getSetting(accessId.getId(),
        this.settingTypeConverter
            .getSettingNameByType(SettingType.CLIENT_COMPARE_DATA_SELECTED_FORMAT));
    if (settingData != null && settingData.getValue() != null) {
      return new ClientCompareDataSelectedFormat(
          new CompareDataFormatID(Long.valueOf(settingData.getValue())));
    }
    return null;
  }

  @Override
  public void setClientCompareDataSelectedSourceIsFile(final AccessID accessId,
      final ClientCompareDataSelectedSourceIsFile setting) {
    Assert.notNull(accessId, ACCESS_ID_MUST_NOT_BE_NULL);
    Assert.notNull(setting, SETTING_MUST_NOT_BE_NULL);
    Assert.notNull(setting.getSetting(), SETTING_GET_SETTING_MUST_NOT_BE_NULL);
    final SettingData settingData = new SettingData(accessId.getId(),
        this.settingTypeConverter
            .getSettingNameByType(SettingType.CLIENT_COMPARE_DATA_SELECTED_SOURCE_IS_FILE),
        Boolean.TRUE.equals(setting.getSetting()) ? "1" : "0");
    this.settingDao.setSetting(settingData);
  }

  @Override
  public ClientCompareDataSelectedSourceIsFile getClientCompareDataSelectedSourceIsFile(
      final AccessID accessId) {
    Assert.notNull(accessId, ACCESS_ID_MUST_NOT_BE_NULL);
    final SettingData settingData = this.settingDao.getSetting(accessId.getId(),
        this.settingTypeConverter
            .getSettingNameByType(SettingType.CLIENT_COMPARE_DATA_SELECTED_SOURCE_IS_FILE));
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
  public void deleteSettings(final UserID userId) {
    Assert.notNull(userId, "UserId must not be null!");
    this.settingDao.deleteSettings(userId.getId());
  }

  @Override
  public void setClientCalcEtfSaleIsin(final AccessID accessId,
      final ClientCalcEtfSaleIsin setting) {
    Assert.notNull(accessId, ACCESS_ID_MUST_NOT_BE_NULL);
    Assert.notNull(setting, SETTING_MUST_NOT_BE_NULL);
    Assert.notNull(setting.getSetting(), SETTING_GET_SETTING_MUST_NOT_BE_NULL);
    final SettingData settingData = new SettingData(accessId.getId(),
        this.settingTypeConverter.getSettingNameByType(SettingType.CLIENT_CALC_ETF_SALE_ISIN),
        setting.getSetting());
    this.settingDao.setSetting(settingData);
  }

  @Override
  public Optional<ClientCalcEtfSaleIsin> getClientCalcEtfSaleIsin(final AccessID accessId) {
    Assert.notNull(accessId, ACCESS_ID_MUST_NOT_BE_NULL);
    final SettingData settingData = this.settingDao.getSetting(accessId.getId(),
        this.settingTypeConverter.getSettingNameByType(SettingType.CLIENT_CALC_ETF_SALE_ISIN));
    if (settingData != null && settingData.getValue() != null) {
      return Optional.of(new ClientCalcEtfSaleIsin(settingData.getValue()));
    }
    return Optional.empty();
  }

  @Override
  public void setClientCalcEtfSaleAskPrice(final AccessID accessId,
      final ClientCalcEtfSaleAskPrice setting) {
    Assert.notNull(accessId, ACCESS_ID_MUST_NOT_BE_NULL);
    Assert.notNull(setting, SETTING_MUST_NOT_BE_NULL);
    Assert.notNull(setting.getSetting(), SETTING_GET_SETTING_MUST_NOT_BE_NULL);
    final SettingData settingData = new SettingData(accessId.getId(),
        this.settingTypeConverter.getSettingNameByType(SettingType.CLIENT_CALC_ETF_SALE_ASK_PRICE),
        setting.getSetting().toString());
    this.settingDao.setSetting(settingData);
  }

  @Override
  public Optional<ClientCalcEtfSaleAskPrice> getClientCalcEtfSaleAskPrice(final AccessID accessId) {
    Assert.notNull(accessId, ACCESS_ID_MUST_NOT_BE_NULL);
    final SettingData settingData = this.settingDao.getSetting(accessId.getId(),
        this.settingTypeConverter.getSettingNameByType(SettingType.CLIENT_CALC_ETF_SALE_ASK_PRICE));
    if (settingData != null && settingData.getValue() != null) {
      return Optional.of(new ClientCalcEtfSaleAskPrice(new BigDecimal(settingData.getValue())));
    }
    return Optional.empty();
  }

  @Override
  public void setClientCalcEtfSaleBidPrice(final AccessID accessId,
      final ClientCalcEtfSaleBidPrice setting) {
    Assert.notNull(accessId, ACCESS_ID_MUST_NOT_BE_NULL);
    Assert.notNull(setting, SETTING_MUST_NOT_BE_NULL);
    Assert.notNull(setting.getSetting(), SETTING_GET_SETTING_MUST_NOT_BE_NULL);
    final SettingData settingData = new SettingData(accessId.getId(),
        this.settingTypeConverter.getSettingNameByType(SettingType.CLIENT_CALC_ETF_SALE_BID_PRICE),
        setting.getSetting().toString());
    this.settingDao.setSetting(settingData);
  }

  @Override
  public Optional<ClientCalcEtfSaleBidPrice> getClientCalcEtfSaleBidPrice(final AccessID accessId) {
    Assert.notNull(accessId, ACCESS_ID_MUST_NOT_BE_NULL);
    final SettingData settingData = this.settingDao.getSetting(accessId.getId(),
        this.settingTypeConverter.getSettingNameByType(SettingType.CLIENT_CALC_ETF_SALE_BID_PRICE));
    if (settingData != null && settingData.getValue() != null) {
      return Optional.of(new ClientCalcEtfSaleBidPrice(new BigDecimal(settingData.getValue())));
    }
    return Optional.empty();
  }

  @Override
  public void setClientCalcEtfSalePieces(final AccessID accessId,
      final ClientCalcEtfSalePieces setting) {
    Assert.notNull(accessId, ACCESS_ID_MUST_NOT_BE_NULL);
    Assert.notNull(setting, SETTING_MUST_NOT_BE_NULL);
    Assert.notNull(setting.getSetting(), SETTING_GET_SETTING_MUST_NOT_BE_NULL);
    final SettingData settingData = new SettingData(accessId.getId(),
        this.settingTypeConverter.getSettingNameByType(SettingType.CLIENT_CALC_ETF_SALE_PIECES),
        setting.getSetting().toString());
    this.settingDao.setSetting(settingData);
  }

  @Override
  public Optional<ClientCalcEtfSalePieces> getClientCalcEtfSalePieces(final AccessID accessId) {
    Assert.notNull(accessId, ACCESS_ID_MUST_NOT_BE_NULL);
    final SettingData settingData = this.settingDao.getSetting(accessId.getId(),
        this.settingTypeConverter.getSettingNameByType(SettingType.CLIENT_CALC_ETF_SALE_PIECES));
    if (settingData != null && settingData.getValue() != null) {
      return Optional.of(new ClientCalcEtfSalePieces(new BigDecimal(settingData.getValue())));
    }
    return Optional.empty();
  }

  @Override
  public void setClientCalcEtfSaleTransactionCosts(final AccessID accessId,
      final ClientCalcEtfSaleTransactionCosts setting) {
    Assert.notNull(accessId, ACCESS_ID_MUST_NOT_BE_NULL);
    Assert.notNull(setting, SETTING_MUST_NOT_BE_NULL);
    Assert.notNull(setting.getSetting(), SETTING_GET_SETTING_MUST_NOT_BE_NULL);
    final SettingData settingData = new SettingData(accessId.getId(), this.settingTypeConverter
        .getSettingNameByType(SettingType.CLIENT_CALC_ETF_SALE_TRANSACTION_COSTS),
        setting.getSetting().toString());
    this.settingDao.setSetting(settingData);
  }

  @Override
  public Optional<ClientCalcEtfSaleTransactionCosts> getClientCalcEtfSaleTransactionCosts(
      final AccessID accessId) {
    Assert.notNull(accessId, ACCESS_ID_MUST_NOT_BE_NULL);
    final SettingData settingData = this.settingDao.getSetting(accessId.getId(),
        this.settingTypeConverter
            .getSettingNameByType(SettingType.CLIENT_CALC_ETF_SALE_TRANSACTION_COSTS));
    if (settingData != null && settingData.getValue() != null) {
      return Optional
          .of(new ClientCalcEtfSaleTransactionCosts(new BigDecimal(settingData.getValue())));
    }
    return Optional.empty();
  }
}
