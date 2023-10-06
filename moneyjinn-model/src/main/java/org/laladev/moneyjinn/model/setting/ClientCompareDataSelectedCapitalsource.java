
package org.laladev.moneyjinn.model.setting;

import org.laladev.moneyjinn.model.capitalsource.CapitalsourceID;

import lombok.NoArgsConstructor;

/**
 * <p>
 * This Setting describes the default selected capitalsource when comparing
 * external data files.
 * </p>
 *
 * @author Oliver Lehmann
 *
 */
@NoArgsConstructor
public class ClientCompareDataSelectedCapitalsource extends AbstractSetting<CapitalsourceID> {
	public ClientCompareDataSelectedCapitalsource(final CapitalsourceID setting) {
		super.setSetting(setting);
	}
}
