
package org.laladev.moneyjinn.model.setting;

import org.laladev.moneyjinn.model.comparedata.CompareDataFormatID;

/**
 * <p>
 * This Setting describes the default selected data format when comparing external data files.
 * </p>
 *
 * @author olivleh1
 *
 */
public class ClientCompareDataSelectedFormat extends AbstractSetting<CompareDataFormatID> {
  public ClientCompareDataSelectedFormat(final CompareDataFormatID setting) {
    super.setSetting(setting);
  }

  @Override
  public SettingType getType() {
    return SettingType.CLIENT_COMPARE_DATA_SELECTED_FORMAT;
  }
}
