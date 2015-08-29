package org.laladev.moneyjinn.businesslogic.service.api;

import org.laladev.moneyjinn.businesslogic.model.access.AccessID;
import org.laladev.moneyjinn.businesslogic.model.access.UserID;
import org.laladev.moneyjinn.businesslogic.model.setting.AbstractSetting;
import org.laladev.moneyjinn.businesslogic.model.setting.ClientDateFormatSetting;
import org.laladev.moneyjinn.businesslogic.model.setting.ClientDisplayedLanguageSetting;
import org.laladev.moneyjinn.businesslogic.model.setting.ClientMaxRowsSetting;
import org.laladev.moneyjinn.businesslogic.model.setting.ClientNumFreeMoneyflowsSetting;

/**
 * <p>
 * SettingService is the Domain Service handling everything around a {@link AbstractSetting}.
 * </p>
 *
 * <p>
 * SettingService is the Domain Service handling operations around an {@link AbstractSetting} like
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
	 * This Service returns the {@link ClientMaxRowsSetting} for the given {@link UserID}
	 *
	 * @param userID
	 *            or groupID {@link AccessID}
	 * @return {@link ClientMaxRowsSetting}
	 * @deprecated Client Settings should be stored on client side!
	 */
	@Deprecated
	public ClientMaxRowsSetting getClientMaxRowsSetting(final AccessID accessId);

	/**
	 * This Service sets the {@link ClientMaxRowsSetting} for the given {@link UserID}
	 *
	 * @param userID
	 *            or groupID {@link AccessID}
	 * @param setting
	 *            the {@link ClientMaxRowsSetting}
	 * @deprecated Client Settings should be stored on client side!
	 */
	@Deprecated
	public void setClientMaxRowsSetting(final AccessID accessId, final ClientMaxRowsSetting setting);

	/**
	 * This Service returns the {@link ClientDateFormatSetting} for the given {@link UserID}
	 *
	 * @param userID
	 *            or groupID {@link AccessID}
	 * @return {@link ClientDateFormatSetting}
	 * @deprecated Client Settings should be stored on client side!
	 */
	@Deprecated
	public ClientDateFormatSetting getClientDateFormatSetting(final AccessID accessId);

	/**
	 * This Service sets the {@link ClientDateFormatSetting} for the given {@link UserID}
	 *
	 * @param userID
	 *            or groupID {@link AccessID}
	 * @param setting
	 *            the {@link ClientDateFormatSetting}
	 * @deprecated Client Settings should be stored on client side!
	 */
	@Deprecated
	public void setClientDateFormatSetting(final AccessID accessId, final ClientDateFormatSetting setting);

	/**
	 * This Service returns the {@link ClientDisplayedLanguageSetting} for the given {@link UserID}
	 *
	 * @param userID
	 *            or groupID {@link AccessID}
	 * @return {@link ClientDisplayedLanguageSetting}
	 * @deprecated Client Settings should be stored on client side!
	 */
	@Deprecated
	public ClientDisplayedLanguageSetting getClientDisplayedLanguageSetting(final AccessID accessId);

	/**
	 * This Service sets the {@link ClientDisplayedLanguageSetting} for the given {@link UserID}
	 *
	 * @param userID
	 *            or groupID {@link AccessID}
	 * @param setting
	 *            the {@link ClientDisplayedLanguageSetting}
	 * @deprecated Client Settings should be stored on client side!
	 */
	@Deprecated
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
	 * @deprecated Client Settings should be stored on client side!
	 */
	@Deprecated
	public ClientNumFreeMoneyflowsSetting getClientNumFreeMoneyflowsSetting(final AccessID accessId);

	/**
	 * This Service sets the {@link ClientNumFreeMoneyflowsSetting} for the given {@link UserID}
	 *
	 * @param userID
	 *            or groupID {@link AccessID}
	 * @param setting
	 *            the {@link ClientNumFreeMoneyflowsSetting}
	 * @deprecated Client Settings should be stored on client side!
	 */
	@Deprecated
	public void setClientNumFreeMoneyflowsSetting(final AccessID accessId,
			final ClientNumFreeMoneyflowsSetting setting);

}