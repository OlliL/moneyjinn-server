
package org.laladev.moneyjinn.model.setting;

import java.math.BigDecimal;

import lombok.NoArgsConstructor;

/**
 * <p>
 * This Setting holds the last selected Pieces for calculating etf sales.
 * </p>
 *
 * @author Oliver Lehmann
 *
 */
@NoArgsConstructor
public class ClientCalcEtfSalePieces extends AbstractSetting<BigDecimal> {
	public ClientCalcEtfSalePieces(final BigDecimal setting) {
		super.setSetting(setting);
	}
}
