package org.laladev.moneyjinn.model.setting;

import lombok.NoArgsConstructor;
import org.laladev.moneyjinn.model.capitalsource.CapitalsourceID;

/**
 * <p>
 * This Setting describes the default selected capitalsource when comparing
 * external data files.
 * </p>
 *
 * @author Oliver Lehmann
 */
@NoArgsConstructor
public class ClientCompareDataSelectedCapitalsource extends AbstractSetting<CapitalsourceID> {
    public ClientCompareDataSelectedCapitalsource(final CapitalsourceID setting) {
        super.setSetting(setting);
    }
}
