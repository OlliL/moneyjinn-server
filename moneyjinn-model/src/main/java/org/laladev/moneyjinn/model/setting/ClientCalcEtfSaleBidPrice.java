
package org.laladev.moneyjinn.model.setting;

import java.math.BigDecimal;
import lombok.NoArgsConstructor;

/**
 * <p>
 * This Setting holds the last selected Bid Price for calculating etf sales.
 * </p>
 *
 * @author olivleh1
 *
 */
@NoArgsConstructor
public class ClientCalcEtfSaleBidPrice extends AbstractSetting<BigDecimal> {
  public ClientCalcEtfSaleBidPrice(final BigDecimal setting) {
    super.setSetting(setting);
  }
}
