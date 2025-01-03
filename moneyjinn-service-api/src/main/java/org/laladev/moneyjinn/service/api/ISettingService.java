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

package org.laladev.moneyjinn.service.api;

import java.util.Optional;

import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.setting.AbstractSetting;
import org.laladev.moneyjinn.model.setting.ClientCalcEtfSalePieces;
import org.laladev.moneyjinn.model.setting.ClientCompareDataSelectedCapitalsource;
import org.laladev.moneyjinn.model.setting.ClientCompareDataSelectedFormat;
import org.laladev.moneyjinn.model.setting.ClientCompareDataSelectedSourceIsFile;
import org.laladev.moneyjinn.model.setting.ClientListEtfDepotDefaultEtfId;
import org.laladev.moneyjinn.model.setting.ClientReportingUnselectedPostingAccountIdsSetting;
import org.laladev.moneyjinn.model.setting.ClientTrendActiveCapitalsourcesSetting;
import org.laladev.moneyjinn.model.setting.ClientTrendActiveEtfsSetting;
import org.laladev.moneyjinn.model.setting.ClientTrendCapitalsourceIDsSetting;
import org.laladev.moneyjinn.model.setting.ClientTrendEtfIDsSetting;

/**
 * <p>
 * SettingService is the Service handling everything around a
 * {@link AbstractSetting}.
 * </p>
 *
 * <p>
 * SettingService is the Service handling operations around an
 * {@link AbstractSetting} like getting, creating, updating, deleting.
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
	 * Deletes all settings for the given {@link UserID}.
	 *
	 * @param userId
	 */
	void deleteSettings(final UserID userId);

	/**
	 * Delete a specific setting for the given {@link UserID}.
	 *
	 * @param The User ID.
	 * @param The setting.
	 */
	void deleteSetting(final UserID userId, AbstractSetting<?> setting);

	/**
	 * This Service returns the
	 * {@link ClientReportingUnselectedPostingAccountIdsSetting} for the given
	 * {@link UserID}.
	 *
	 * @param userId {@link UserID}
	 * @return {@link ClientReportingUnselectedPostingAccountIdsSetting}
	 */
	Optional<ClientReportingUnselectedPostingAccountIdsSetting> getClientReportingUnselectedPostingAccountIdsSetting(
			UserID userId);

	/**
	 * This Service sets the
	 * {@link ClientReportingUnselectedPostingAccountIdsSetting} for the given
	 * {@link UserID}.
	 *
	 * @param userId  {@link UserID}
	 * @param setting the {@link ClientReportingUnselectedPostingAccountIdsSetting}
	 */
	void setClientReportingUnselectedPostingAccountIdsSetting(UserID userId,
			ClientReportingUnselectedPostingAccountIdsSetting setting);

	/**
	 * This Service returns the {@link ClientTrendCapitalsourceIDsSetting} for the
	 * given {@link UserID}.
	 *
	 * @param userId {@link UserID}
	 * @return {@link ClientTrendCapitalsourceIDsSetting}
	 */
	Optional<ClientTrendCapitalsourceIDsSetting> getClientTrendCapitalsourceIDsSetting(final UserID userId);

	/**
	 * This Service sets the {@link ClientTrendCapitalsourceIDsSetting} for the
	 * given {@link UserID}.
	 *
	 * @param userId  {@link UserID}
	 * @param setting the {@link ClientTrendCapitalsourceIDsSetting}
	 */
	void setClientTrendCapitalsourceIDsSetting(final UserID userId, final ClientTrendCapitalsourceIDsSetting setting);

	/**
	 * This Service returns the {@link ClientTrendEtfIDsSetting} for the given
	 * {@link UserID}.
	 *
	 * @param userId {@link UserID}
	 * @return {@link ClientTrendEtfIDsSetting}
	 */
	Optional<ClientTrendEtfIDsSetting> getClientTrendEtfIDsSetting(final UserID userId);

	/**
	 * This Service sets the {@link ClientTrendEtfIDsSetting} for the given
	 * {@link UserID}.
	 *
	 * @param userId  {@link UserID}
	 * @param setting the {@link ClientTrendEtfIDsSetting}
	 */
	void setClientTrendEtfIDsSetting(final UserID userId, final ClientTrendEtfIDsSetting setting);

	/**
	 * This Service returns the {@link ClientTrendActiveEtfsSetting} for the given
	 * {@link UserID}.
	 *
	 * @param userId {@link UserID}
	 * @return {@link ClientTrendActiveEtfsSetting}
	 */
	Optional<ClientTrendActiveEtfsSetting> getClientTrendActiveEtfsSetting(final UserID userId);

	/**
	 * This Service sets the {@link ClientTrendActiveEtfsSetting} for the given
	 * {@link UserID}.
	 *
	 * @param userId  {@link UserID}
	 * @param setting the {@link ClientTrendActiveEtfsSetting}
	 */
	void setClientTrendActiveEtfsSetting(final UserID userId, final ClientTrendActiveEtfsSetting setting);

	/**
	 * This Service returns the {@link ClientTrendActiveCapitalsourcesSetting} for
	 * the given {@link UserID}.
	 *
	 * @param userId {@link UserID}
	 * @return {@link ClientTrendActiveCapitalsourcesSetting}
	 */
	Optional<ClientTrendActiveCapitalsourcesSetting> getClientTrendActiveCapitalsourcesSetting(final UserID userId);

	/**
	 * This Service sets the {@link ClientTrendActiveCapitalsourcesSetting} for the
	 * given {@link UserID}.
	 *
	 * @param userId  {@link UserID}
	 * @param setting the {@link ClientTrendActiveCapitalsourcesSetting}
	 */
	void setClientTrendActiveCapitalsourcesSetting(final UserID userId,
			final ClientTrendActiveCapitalsourcesSetting setting);

	/**
	 * This Service sets the {@link ClientCompareDataSelectedCapitalsource} for the
	 * given {@link UserID}.
	 *
	 * @param userId  {@link UserID}
	 * @param setting the {@link ClientCompareDataSelectedCapitalsource}
	 */
	void setClientCompareDataSelectedCapitalsource(UserID userId, ClientCompareDataSelectedCapitalsource setting);

	/**
	 * This Service returns the {@link ClientCompareDataSelectedCapitalsource} for
	 * the given {@link UserID}.
	 *
	 * @param userId {@link UserID}
	 * @return {@link ClientCompareDataSelectedCapitalsource}
	 */
	Optional<ClientCompareDataSelectedCapitalsource> getClientCompareDataSelectedCapitalsource(UserID userId);

	/**
	 * This Service sets the {@link ClientCompareDataSelectedFormat} for the given
	 * {@link UserID}.
	 *
	 * @param userId  {@link UserID}
	 * @param setting the {@link ClientCompareDataSelectedFormat}
	 */
	void setClientCompareDataSelectedFormat(UserID userId, ClientCompareDataSelectedFormat setting);

	/**
	 * This Service returns the {@link ClientCompareDataSelectedFormat} for the
	 * given {@link UserID}.
	 *
	 * @param userId {@link UserID}
	 * @return {@link ClientCompareDataSelectedFormat}
	 */
	Optional<ClientCompareDataSelectedFormat> getClientCompareDataSelectedFormat(UserID userId);

	/**
	 * This Service sets the {@link ClientCompareDataSelectedSourceIsFile} for the
	 * given {@link UserID}.
	 *
	 * @param userId  {@link UserID}
	 * @param setting the {@link ClientCompareDataSelectedSourceIsFile}
	 */
	void setClientCompareDataSelectedSourceIsFile(UserID userId, ClientCompareDataSelectedSourceIsFile setting);

	/**
	 * This Service returns the {@link ClientCompareDataSelectedSourceIsFile} for
	 * the given {@link UserID}.
	 *
	 * @param userId {@link UserID}
	 * @return {@link ClientCompareDataSelectedSourceIsFile}
	 */
	Optional<ClientCompareDataSelectedSourceIsFile> getClientCompareDataSelectedSourceIsFile(UserID userId);

	void setClientListEtfDepotDefaultEtfId(UserID userId, ClientListEtfDepotDefaultEtfId setting);

	Optional<ClientListEtfDepotDefaultEtfId> getClientListEtfDepotDefaultEtfId(UserID userId);

	void setClientCalcEtfSalePieces(UserID userId, ClientCalcEtfSalePieces setting);

	Optional<ClientCalcEtfSalePieces> getClientCalcEtfSalePieces(UserID userId);

}