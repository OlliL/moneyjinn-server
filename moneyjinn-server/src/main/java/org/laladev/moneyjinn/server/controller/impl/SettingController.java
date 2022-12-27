//Copyright (c) 2015-2018 Oliver Lehmann <lehmann@ans-netz.de>
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

package org.laladev.moneyjinn.server.controller.impl;

import jakarta.inject.Inject;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.core.rest.model.setting.AbstractShowSettingsResponse;
import org.laladev.moneyjinn.core.rest.model.setting.AbstractUpdateSettingsRequest;
import org.laladev.moneyjinn.core.rest.model.setting.ShowDefaultSettingsResponse;
import org.laladev.moneyjinn.core.rest.model.setting.ShowPersonalSettingsResponse;
import org.laladev.moneyjinn.core.rest.model.setting.UpdateDefaultSettingsRequest;
import org.laladev.moneyjinn.core.rest.model.setting.UpdatePersonalSettingsRequest;
import org.laladev.moneyjinn.model.access.AccessID;
import org.laladev.moneyjinn.model.access.User;
import org.laladev.moneyjinn.model.access.UserAttribute;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.exception.BusinessException;
import org.laladev.moneyjinn.model.setting.ClientDateFormatSetting;
import org.laladev.moneyjinn.model.setting.ClientDisplayedLanguageSetting;
import org.laladev.moneyjinn.model.setting.ClientMaxRowsSetting;
import org.laladev.moneyjinn.service.api.ISettingService;
import org.laladev.moneyjinn.service.api.IUserService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Transactional(propagation = Propagation.REQUIRES_NEW)
@RequestMapping("/moneyflow/server/setting/")
public class SettingController extends AbstractController {
  private static final AccessID ROOT_ACCESS_ID = new AccessID(0L);
  @Inject
  private IUserService userService;
  @Inject
  private ISettingService settingService;

  @Override
  protected void addBeanMapper() {
    // No Mapping needed.
  }

  @RequestMapping(value = "showDefaultSettings", method = { RequestMethod.GET })
  public ShowDefaultSettingsResponse showDefaultSettings() {
    final ShowDefaultSettingsResponse response = new ShowDefaultSettingsResponse();
    this.getStandardSettings(ROOT_ACCESS_ID, response);
    return response;
  }

  @RequestMapping(value = "updateDefaultSettings", method = { RequestMethod.PUT })
  public void updateDefaultSettings(@RequestBody final UpdateDefaultSettingsRequest request) {
    this.updateStandardSettings(request, ROOT_ACCESS_ID);
  }

  @RequestMapping(value = "showPersonalSettings", method = { RequestMethod.GET })
  public ShowPersonalSettingsResponse showPersonalSettings() {
    final UserID userId = super.getUserId();
    final ShowPersonalSettingsResponse response = new ShowPersonalSettingsResponse();
    this.getStandardSettings(userId, response);
    return response;
  }

  @RequestMapping(value = "updatePersonalSettings", method = { RequestMethod.PUT })
  public void updatePersonalSettings(@RequestBody final UpdatePersonalSettingsRequest request) {
    final UserID userId = super.getUserId();
    final User user = this.userService.getUserById(userId);
    final String password = request.getPassword();
    this.updateStandardSettings(request, userId);
    if (password != null && !password.trim().isEmpty()) {
      this.userService.setPassword(userId, password);
    } else if (user.getAttributes().contains(UserAttribute.IS_NEW)) {
      throw new BusinessException("You have to change your password!",
          ErrorCode.PASSWORD_MUST_BE_CHANGED);
    }
  }

  private void getStandardSettings(final AccessID accessId,
      final AbstractShowSettingsResponse response) {
    final ClientDisplayedLanguageSetting clientDisplayedLanguageSetting = this.settingService
        .getClientDisplayedLanguageSetting(accessId);
    final ClientDateFormatSetting clientDateFormatSetting = this.settingService
        .getClientDateFormatSetting(accessId);
    final ClientMaxRowsSetting clientMaxRowsSetting = this.settingService
        .getClientMaxRowsSetting(accessId);
    response.setLanguage(clientDisplayedLanguageSetting.getSetting());
    response.setDateFormat(clientDateFormatSetting.getSetting());
    response.setMaxRows(clientMaxRowsSetting.getSetting());
  }

  private void updateStandardSettings(final AbstractUpdateSettingsRequest request,
      final AccessID accessId) {
    if (request.getLanguage() != null) {
      final ClientDisplayedLanguageSetting clientDisplayedLanguageSetting = new ClientDisplayedLanguageSetting(
          request.getLanguage());
      this.settingService.setClientDisplayedLanguageSetting(accessId,
          clientDisplayedLanguageSetting);
    }
    if (request.getDateFormat() != null) {
      final ClientDateFormatSetting clientDateFormatSetting = new ClientDateFormatSetting(
          request.getDateFormat());
      this.settingService.setClientDateFormatSetting(accessId, clientDateFormatSetting);
    }
    if (request.getMaxRows() != null) {
      final ClientMaxRowsSetting clientMaxRowsSetting = new ClientMaxRowsSetting(
          request.getMaxRows());
      this.settingService.setClientMaxRowsSetting(accessId, clientMaxRowsSetting);
    }
  }
}
