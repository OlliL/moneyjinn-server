
package org.laladev.moneyjinn.model.setting;

import java.math.BigDecimal;

import lombok.NoArgsConstructor;

/**
 * <p>
 * This Setting holds the last selected relative transtaction cost for
 * calculating etf sales.
 * </p>
 *
 * @author Oliver Lehmann
 *
 */
@NoArgsConstructor
public class ClientCalcEtfSaleTransactionCostsRelative extends AbstractSetting<BigDecimal> {
	public ClientCalcEtfSaleTransactionCostsRelative(final BigDecimal setting) {
		super.setSetting(setting);
	}
}
