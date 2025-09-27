//Copyright (c) 2023-2025 Oliver Lehmann <lehmann@ans-netz.de>
//All rights reserved.
//
//Redistribution and use in source and binary forms, with or without
//modification, are permitted provided that the following conditions
//are met:
//1. Redistributions of source code must retain the above copyright
//notice, this list of conditions and the following disclaimer
//2. Redistributions in binary form must reproduce the above copyright
//notice, this list of conditions and the following disclaimer in the
//documentation and/or other materials provided with the distribution.
//
//THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS'' AND
//ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
//IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
//ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE LIABLE
//FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
//DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
//OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
//HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
//LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
//OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
//SUCH DAMAGE.

package org.laladev.moneyjinn.service.dao.data.mapper;

import org.laladev.moneyjinn.model.setting.*;

import java.util.Map;

public class SettingNameConverter {
    private static final Map<String, String> lookupMap;

    static {

        lookupMap = Map.of(
                ClientTrendCapitalsourceIDsSetting.class.getSimpleName(), "client_trend_capitalsource_ids",
                ClientTrendEtfIDsSetting.class.getSimpleName(), "client_trend_etf_ids",
                ClientTrendActiveCapitalsourcesSetting.class.getSimpleName(), "client_trend_active_capitalsources",
                ClientTrendActiveEtfsSetting.class.getSimpleName(), "client_trend_active_etfs",
                ClientReportingUnselectedPostingAccountIdsSetting.class.getSimpleName(),
                "client_reporting_unselected_posting_account_ids",
                ClientCompareDataSelectedCapitalsource.class.getSimpleName(),
                "client_compare_data_selected_capitalsource",
                ClientCompareDataSelectedFormat.class.getSimpleName(), "client_compare_data_selected_format",
                ClientCompareDataSelectedSourceIsFile.class.getSimpleName(),
                "client_compare_data_selected_source_is_file",
                ClientListEtfDepotDefaultEtfId.class.getSimpleName(), "client_list_etf_depot_default_etfid",
                ClientCalcEtfSalePieces.class.getSimpleName(), "client_calc_etf_sale_pieces");
    }

    private SettingNameConverter() {
    }

    public static String getSettingNameByClassName(final String settingClassName) {
        return lookupMap.get(settingClassName);
    }

}
