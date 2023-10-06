
package org.laladev.moneyjinn.model.setting;

import java.math.BigDecimal;

import lombok.NoArgsConstructor;

/**
 * <p>
 * This Setting holds the last selected Ask Price for calculating etf sales.
 * </p>
 *
 * @author Oliver Lehmann
 *
 */
@NoArgsConstructor
public class ClientCalcEtfSaleAskPrice extends AbstractSetting<BigDecimal> {
	public ClientCalcEtfSaleAskPrice(final BigDecimal setting) {
		super.setSetting(setting);
	}
}
