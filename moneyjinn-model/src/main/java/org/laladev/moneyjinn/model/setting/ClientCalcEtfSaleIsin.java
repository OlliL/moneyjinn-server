
package org.laladev.moneyjinn.model.setting;

import lombok.NoArgsConstructor;

/**
 * <p>
 * This Setting holds the last selected ISIN for calculating etf sales.
 * </p>
 *
 * @author Oliver Lehmann
 *
 */
@NoArgsConstructor
public class ClientCalcEtfSaleIsin extends AbstractSetting<String> {
	public ClientCalcEtfSaleIsin(final String setting) {
		super.setSetting(setting);
	}
}
