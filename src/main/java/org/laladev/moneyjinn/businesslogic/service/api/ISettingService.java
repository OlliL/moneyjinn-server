//
// Copyright (c) 2015 Oliver Lehmann <oliver@laladev.org>
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

package org.laladev.moneyjinn.businesslogic.service.api;

import org.laladev.moneyjinn.businesslogic.model.access.AccessID;
import org.laladev.moneyjinn.businesslogic.model.access.UserID;
import org.laladev.moneyjinn.businesslogic.model.setting.AbstractSetting;
import org.laladev.moneyjinn.businesslogic.model.setting.ClientCompareDataSelectedCapitalsource;
import org.laladev.moneyjinn.businesslogic.model.setting.ClientCompareDataSelectedFormat;
import org.laladev.moneyjinn.businesslogic.model.setting.ClientCurrentlyValidCapitalsourcesSetting;
import org.laladev.moneyjinn.businesslogic.model.setting.ClientCurrentlyValidContractpartnerSetting;
import org.laladev.moneyjinn.businesslogic.model.setting.ClientDateFormatSetting;
import org.laladev.moneyjinn.businesslogic.model.setting.ClientDisplayedLanguageSetting;
import org.laladev.moneyjinn.businesslogic.model.setting.ClientMaxRowsSetting;
import org.laladev.moneyjinn.businesslogic.model.setting.ClientNumFreeMoneyflowsSetting;
import org.laladev.moneyjinn.businesslogic.model.setting.ClientReportingUnselectedPostingAccountIdsSetting;
import org.laladev.moneyjinn.businesslogic.model.setting.ClientTrendCapitalsourceIDsSetting;

/**
 * <p>
 * SettingService is the Core Service handling everything around a {@link AbstractSetting}.
 * </p>
 *
 * <p>
 * SettingService is the Core Service handling operations around an {@link AbstractSetting} like
 * getting, creating, updating, deleting.
 * </p>
 * <p>
 * The main datasource is the Table <code>settings</code>.
 * </p>
 *
 * @author Oliver Lehmann
 * @since 0.0.1
 *
 */
public interface ISettingService {

	/**
	 * This Service returns the {@link ClientReportingUnselectedPostingAccountIdsSetting} for the
	 * given {@link UserID}
	 *
	 * @param userID
	 *            or groupID {@link AccessID}
	 * @return {@link ClientReportingUnselectedPostingAccountIdsSetting}
	 */
	public ClientReportingUnselectedPostingAccountIdsSetting getClientReportingUnselectedPostingAccountIdsSetting(
			AccessID accessId);

	/**
	 * This Service sets the {@link ClientReportingUnselectedPostingAccountIdsSetting} for the given
	 * {@link UserID}
	 *
	 * @param userID
	 *            or groupID {@link AccessID}
	 * @param setting
	 *            the {@link ClientReportingUnselectedPostingAccountIdsSetting}
	 */
	public void setClientReportingUnselectedPostingAccountIdsSetting(AccessID accessId,
			ClientReportingUnselectedPostingAccountIdsSetting setting);

	/**
	 * This Service returns the {@link ClientTrendCapitalsourceIDsSetting} for the given
	 * {@link UserID}
	 *
	 * @param userID
	 *            or groupID {@link AccessID}
	 * @return {@link ClientTrendCapitalsourceIDsSetting}
	 */
	public ClientTrendCapitalsourceIDsSetting getClientTrendCapitalsourceIDsSetting(final AccessID accessId);

	/**
	 * This Service sets the {@link ClientTrendCapitalsourceIDsSetting} for the given {@link UserID}
	 *
	 * @param userID
	 *            or groupID {@link AccessID}
	 * @param setting
	 *            the {@link ClientTrendCapitalsourceIDsSetting}
	 */
	public void setClientTrendCapitalsourceIDsSetting(final AccessID accessId,
			final ClientTrendCapitalsourceIDsSetting setting);

	/**
	 * This Service returns the {@link ClientMaxRowsSetting} for the given {@link UserID}
	 *
	 * @param userID
	 *            or groupID {@link AccessID}
	 * @return {@link ClientMaxRowsSetting}
	 */
	public ClientMaxRowsSetting getClientMaxRowsSetting(final AccessID accessId);

	/**
	 * This Service sets the {@link ClientMaxRowsSetting} for the given {@link UserID}
	 *
	 * @param userID
	 *            or groupID {@link AccessID}
	 * @param setting
	 *            the {@link ClientMaxRowsSetting}
	 */
	public void setClientMaxRowsSetting(final AccessID accessId, final ClientMaxRowsSetting setting);

	/**
	 * This Service returns the {@link ClientDateFormatSetting} for the given {@link UserID}
	 *
	 * @param userID
	 *            or groupID {@link AccessID}
	 * @return {@link ClientDateFormatSetting}
	 */
	public ClientDateFormatSetting getClientDateFormatSetting(final AccessID accessId);

	/**
	 * This Service sets the {@link ClientDateFormatSetting} for the given {@link UserID}
	 *
	 * @param userID
	 *            or groupID {@link AccessID}
	 * @param setting
	 *            the {@link ClientDateFormatSetting}
	 */
	public void setClientDateFormatSetting(final AccessID accessId, final ClientDateFormatSetting setting);

	/**
	 * This Service returns the {@link ClientDisplayedLanguageSetting} for the given {@link UserID}
	 *
	 * @param userID
	 *            or groupID {@link AccessID}
	 * @return {@link ClientDisplayedLanguageSetting}
	 */
	public ClientDisplayedLanguageSetting getClientDisplayedLanguageSetting(final AccessID accessId);

	/**
	 * This Service sets the {@link ClientDisplayedLanguageSetting} for the given {@link UserID}
	 *
	 * @param userID
	 *            or groupID {@link AccessID}
	 * @param setting
	 *            the {@link ClientDisplayedLanguageSetting}
	 */
	public void setClientDisplayedLanguageSetting(final AccessID accessId,
			final ClientDisplayedLanguageSetting setting);

	/**
	 * Initializes the settings for a new {@link User} by copying the default settings.
	 *
	 * @param newUserID
	 */
	public void initSettings(final UserID userId);

	/**
	 * Deletes all settings for the given {@link UserID}
	 *
	 * @param userId
	 */
	public void deleteSettings(final UserID userId);

	/**
	 * This Service returns the {@link ClientNumFreeMoneyflowsSetting} for the given {@link UserID}
	 *
	 * @param userID
	 *            or groupID {@link AccessID}
	 * @return {@link ClientNumFreeMoneyflowsSetting}
	 */
	public ClientNumFreeMoneyflowsSetting getClientNumFreeMoneyflowsSetting(final AccessID accessId);

	/**
	 * This Service sets the {@link ClientNumFreeMoneyflowsSetting} for the given {@link UserID}
	 *
	 * @param userID
	 *            or groupID {@link AccessID}
	 * @param setting
	 *            the {@link ClientNumFreeMoneyflowsSetting}
	 */
	public void setClientNumFreeMoneyflowsSetting(final AccessID accessId,
			final ClientNumFreeMoneyflowsSetting setting);

	/**
	 * This Service returns the {@link ClientCurrentlyValidCapitalsourcesSetting} for the given
	 * {@link UserID}
	 *
	 * @param userID
	 *            or groupID {@link AccessID}
	 * @return {@link ClientCurrentlyValidCapitalsourcesSetting}
	 */
	public ClientCurrentlyValidCapitalsourcesSetting getClientCurrentlyValidCapitalsourcesSetting(AccessID accessId);

	/**
	 * This Service sets the {@link ClientCurrentlyValidCapitalsourcesSetting} for the given
	 * {@link UserID}
	 *
	 * @param userID
	 *            or groupID {@link AccessID}
	 * @param setting
	 *            the {@link ClientCurrentlyValidCapitalsourcesSetting}
	 */
	public void setClientCurrentlyValidCapitalsourcesSetting(AccessID accessId,
			ClientCurrentlyValidCapitalsourcesSetting setting);

	/**
	 * This Service returns the {@link ClientCurrentlyValidContractpartnerSetting} for the given
	 * {@link UserID}
	 *
	 * @param userID
	 *            or groupID {@link AccessID}
	 * @return {@link ClientCurrentlyValidContractpartnerSetting}
	 */
	public ClientCurrentlyValidContractpartnerSetting getClientCurrentlyValidContractpartnerSetting(AccessID accessId);

	/**
	 * This Service sets the {@link ClientCurrentlyValidContractpartnerSetting} for the given
	 * {@link UserID}
	 *
	 * @param userID
	 *            or groupID {@link AccessID}
	 * @param setting
	 *            the {@link ClientCurrentlyValidContractpartnerSetting}
	 */
	public void setClientCurrentlyValidContractpartnerSetting(AccessID accessId,
			ClientCurrentlyValidContractpartnerSetting setting);

	/**
	 * This Service sets the {@link ClientCompareDataSelectedCapitalsource} for the given
	 * {@link UserID}
	 *
	 * @param userID
	 *            or groupID {@link AccessID}
	 * @param setting
	 *            the {@link ClientCompareDataSelectedCapitalsource}
	 */
	public void setClientCompareDataSelectedCapitalsource(AccessID accessId,
			ClientCompareDataSelectedCapitalsource setting);

	/**
	 * This Service returns the {@link ClientCompareDataSelectedCapitalsource} for the given
	 * {@link UserID}
	 *
	 * @param userID
	 *            or groupID {@link AccessID}
	 * @return {@link ClientCompareDataSelectedCapitalsource}
	 */
	public ClientCompareDataSelectedCapitalsource getClientCompareDataSelectedCapitalsource(AccessID accessId);

	/**
	 * This Service sets the {@link ClientCompareDataSelectedFormat} for the given {@link UserID}
	 *
	 * @param userID
	 *            or groupID {@link AccessID}
	 * @param setting
	 *            the {@link ClientCompareDataSelectedFormat}
	 */
	public void setClientCompareDataSelectedFormat(AccessID accessId, ClientCompareDataSelectedFormat setting);

	/**
	 * This Service returns the {@link ClientCompareDataSelectedFormat} for the given {@link UserID}
	 *
	 * @param userID
	 *            or groupID {@link AccessID}
	 * @return {@link ClientCompareDataSelectedFormat}
	 */
	public ClientCompareDataSelectedFormat getClientCompareDataSelectedFormat(AccessID accessId);

}