package org.laladev.moneyjinn.model.setting;

import lombok.NoArgsConstructor;
import org.laladev.moneyjinn.model.etf.EtfID;

/**
 * <p>
 * This Setting holds the last selected EtfId for showing the ETF Depot view.
 * </p>
 *
 * @author Oliver Lehmann
 */
@NoArgsConstructor
public class ClientListEtfDepotDefaultEtfId extends AbstractSetting<EtfID> {
    public ClientListEtfDepotDefaultEtfId(final EtfID setting) {
        super.setSetting(setting);
    }
}
