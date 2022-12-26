
package org.laladev.moneyjinn.model.setting;

/**
 * <p>
 * This Setting holds the last selected ISIN for calculating etf sales.
 * </p>
 *
 * @author olivleh1
 *
 */
public class ClientCalcEtfSaleIsin extends AbstractSetting<String> {
  public ClientCalcEtfSaleIsin(final String setting) {
    super.setSetting(setting);
  }

  @Override
  public SettingType getType() {
    return SettingType.CLIENT_CALC_ETF_SALE_ISIN;
  }
}
