
package org.laladev.moneyjinn.model.setting;

import java.math.BigDecimal;

import lombok.NoArgsConstructor;

/**
 * <p>
 * This Setting holds the last selected absolute transaction cost for
 * calculating etf sales.
 * </p>
 *
 * @author Oliver Lehmann
 *
 */
@NoArgsConstructor
public class ClientCalcEtfSaleTransactionCostsAbsolute extends AbstractSetting<BigDecimal> {
	public ClientCalcEtfSaleTransactionCostsAbsolute(final BigDecimal setting) {
		super.setSetting(setting);
	}
}
