
package org.laladev.moneyjinn.model.setting;

import org.laladev.moneyjinn.model.capitalsource.Capitalsource;

/**
 * <p>
 * This Setting describes if when listing all {@link Capitalsource}s, only the currently valid ones
 * should be displayed by default or all.
 * </p>
 *
 * @author olivleh1
 *
 */
public class ClientCurrentlyValidCapitalsourcesSetting extends AbstractSetting<Boolean> {
  public ClientCurrentlyValidCapitalsourcesSetting(final Boolean setting) {
    super.setSetting(setting);
  }

  @Override
  public SettingType getType() {
    return SettingType.CLIENT_CURRENTLY_VALID_CAPITALSOURCES;
  }
}
