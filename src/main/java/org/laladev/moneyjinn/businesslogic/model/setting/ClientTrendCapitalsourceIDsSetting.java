package org.laladev.moneyjinn.businesslogic.model.setting;

import java.util.List;

import org.laladev.moneyjinn.businesslogic.model.capitalsource.CapitalsourceID;

/**
 * returns a List of {@link CapitalsourceID}s the user had chosen the last time when using the
 * <code>Trends</code> functionality.
 *
 * @deprecated Client Settings should be stored on client side!
 * @author olivleh1
 *
 */
@Deprecated
public class ClientTrendCapitalsourceIDsSetting extends AbstractSetting<List<CapitalsourceID>> {

	@Override
	public SettingType getType() {
		return SettingType.CLIENT_TREND_CAPITALSOURCEIDS;
	}

}
