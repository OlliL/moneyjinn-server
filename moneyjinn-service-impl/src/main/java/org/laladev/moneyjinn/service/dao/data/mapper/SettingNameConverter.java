package org.laladev.moneyjinn.service.dao.data.mapper;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.laladev.moneyjinn.model.setting.ClientCalcEtfSaleAskPrice;
import org.laladev.moneyjinn.model.setting.ClientCalcEtfSaleBidPrice;
import org.laladev.moneyjinn.model.setting.ClientCalcEtfSaleIsin;
import org.laladev.moneyjinn.model.setting.ClientCalcEtfSalePieces;
import org.laladev.moneyjinn.model.setting.ClientCalcEtfSaleTransactionCosts;
import org.laladev.moneyjinn.model.setting.ClientCompareDataSelectedCapitalsource;
import org.laladev.moneyjinn.model.setting.ClientCompareDataSelectedFormat;
import org.laladev.moneyjinn.model.setting.ClientCompareDataSelectedSourceIsFile;
import org.laladev.moneyjinn.model.setting.ClientReportingUnselectedPostingAccountIdsSetting;
import org.laladev.moneyjinn.model.setting.ClientTrendCapitalsourceIDsSetting;

public class SettingNameConverter {
  private static Map<String, String> lookupMap;

  static {
    final Map<String, String> tempMap = new HashMap<>();
    tempMap.put(ClientTrendCapitalsourceIDsSetting.class.getSimpleName(),
        "client_trend_capitalsource_ids");
    tempMap.put(ClientReportingUnselectedPostingAccountIdsSetting.class.getSimpleName(),
        "client_reporting_unselected_posting_account_ids");
    tempMap.put(ClientCompareDataSelectedCapitalsource.class.getSimpleName(),
        "client_compare_data_selected_capitalsource");
    tempMap.put(ClientCompareDataSelectedFormat.class.getSimpleName(),
        "client_compare_data_selected_format");
    tempMap.put(ClientCompareDataSelectedSourceIsFile.class.getSimpleName(),
        "client_compare_data_selected_source_is_file");
    tempMap.put(ClientCalcEtfSaleAskPrice.class.getSimpleName(), "client_calc_etf_sale_ask_price");
    tempMap.put(ClientCalcEtfSaleBidPrice.class.getSimpleName(), "client_calc_etf_sale_bid_price");
    tempMap.put(ClientCalcEtfSaleIsin.class.getSimpleName(), "client_calc_etf_sale_isin");
    tempMap.put(ClientCalcEtfSalePieces.class.getSimpleName(), "client_calc_etf_sale_pieces");
    tempMap.put(ClientCalcEtfSaleTransactionCosts.class.getSimpleName(),
        "client_calc_etf_sale_transaction_costs");

    lookupMap = Collections.unmodifiableMap(tempMap);
  }

  private SettingNameConverter() {
  }

  public static String getSettingNameByClassName(final String settingClassName) {
    return lookupMap.get(settingClassName);
  }

}
