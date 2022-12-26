
package org.laladev.moneyjinn.model.setting;

/**
 * <p>
 * This Setting describes how a date should be displayed in the client (Date Format).
 * </p>
 *
 * @author olivleh1
 *
 */
public class ClientDateFormatSetting extends AbstractSetting<String> {
  public ClientDateFormatSetting(final String setting) {
    super.setSetting(setting);
  }

  @Override
  public SettingType getType() {
    return SettingType.CLIENT_DATE_FORMAT;
  }
}
