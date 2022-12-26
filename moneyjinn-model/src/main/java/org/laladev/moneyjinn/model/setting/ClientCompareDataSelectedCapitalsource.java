
package org.laladev.moneyjinn.model.setting;

import org.laladev.moneyjinn.model.capitalsource.CapitalsourceID;

/**
 * <p>
 * This Setting describes the default selected capitalsource when comparing external data files.
 * </p>
 *
 * @author olivleh1
 *
 */
public class ClientCompareDataSelectedCapitalsource extends AbstractSetting<CapitalsourceID> {
  public ClientCompareDataSelectedCapitalsource(final CapitalsourceID setting) {
    super.setSetting(setting);
  }

  @Override
  public SettingType getType() {
    return SettingType.CLIENT_COMPARE_DATA_SELECTED_CAPITALSOURCE;
  }
}
