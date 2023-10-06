
package org.laladev.moneyjinn.model.setting;

import org.laladev.moneyjinn.model.comparedata.CompareDataFormatID;

import lombok.NoArgsConstructor;

/**
 * <p>
 * This Setting describes the default selected data format when comparing
 * external data files.
 * </p>
 *
 * @author Oliver Lehmann
 *
 */
@NoArgsConstructor
public class ClientCompareDataSelectedFormat extends AbstractSetting<CompareDataFormatID> {
	public ClientCompareDataSelectedFormat(final CompareDataFormatID setting) {
		super.setSetting(setting);
	}
}
