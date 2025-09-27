package org.laladev.moneyjinn.model.setting;

import lombok.NoArgsConstructor;
import org.laladev.moneyjinn.model.comparedata.CompareDataFormatID;

/**
 * <p>
 * This Setting describes the default selected data format when comparing
 * external data files.
 * </p>
 *
 * @author Oliver Lehmann
 */
@NoArgsConstructor
public class ClientCompareDataSelectedFormat extends AbstractSetting<CompareDataFormatID> {
    public ClientCompareDataSelectedFormat(final CompareDataFormatID setting) {
        super.setSetting(setting);
    }
}
