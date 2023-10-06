
package org.laladev.moneyjinn.model.setting;

import lombok.NoArgsConstructor;

/**
 * <p>
 * This Setting describes if the selected source the default is file or import.
 * </p>
 *
 * @author Oliver Lehmann
 *
 */
@NoArgsConstructor
public class ClientCompareDataSelectedSourceIsFile extends AbstractSetting<Boolean> {
	public ClientCompareDataSelectedSourceIsFile(final Boolean setting) {
		super.setSetting(setting);
	}
}
