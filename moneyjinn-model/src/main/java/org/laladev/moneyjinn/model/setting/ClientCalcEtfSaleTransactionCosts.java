package org.laladev.moneyjinn.model.setting;

import java.math.BigDecimal;

/**
 * <p>
 * This Setting holds the last selected Ask Price for calculating etf sales
 * </p>
 *
 * @author olivleh1
 *
 */
public class ClientCalcEtfSaleTransactionCosts extends AbstractSetting<BigDecimal> {

	public ClientCalcEtfSaleTransactionCosts(final BigDecimal setting) {
		super.setSetting(setting);
	}

	@Override
	public SettingType getType() {
		return SettingType.CLIENT_CALC_ETF_SALE_TRANSACTION_COSTS;
	}

}
