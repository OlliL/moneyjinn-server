
package org.laladev.moneyjinn.model.setting;

import lombok.NoArgsConstructor;

/**
 * <p>
 * This Setting holds the last selected EtfId for showing the ETF Depot view.
 * </p>
 *
 * @author Oliver Lehmann
 *
 */
@NoArgsConstructor
public class ClientListEtfDepotDefaultEtfId extends AbstractSetting<String> {
	public ClientListEtfDepotDefaultEtfId(final String setting) {
		super.setSetting(setting);
	}
}
