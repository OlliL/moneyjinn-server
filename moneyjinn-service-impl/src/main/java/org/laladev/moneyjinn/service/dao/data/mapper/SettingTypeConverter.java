//
// Copyright (c) 2015-2023 Oliver Lehmann <lehmann@ans-netz.de>
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions
// are met:
// 1. Redistributions of source code must retain the above copyright
// notice, this list of conditions and the following disclaimer
// 2. Redistributions in binary form must reproduce the above copyright
// notice, this list of conditions and the following disclaimer in the
// documentation and/or other materials provided with the distribution.
//
// THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS'' AND
// ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE LIABLE
// FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
// DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
// OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
// HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
// LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
// OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE.
//

package org.laladev.moneyjinn.service.dao.data.mapper;

import org.laladev.moneyjinn.model.setting.SettingType;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.ValueMapping;

@Mapper(componentModel = ComponentModel.JAKARTA, unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface SettingTypeConverter {

  @ValueMapping(target = "trend_capitalsourceid", source = "CLIENT_TREND_CAPITALSOURCEIDS")
  @ValueMapping(target = "reporting_postingaccountids", source = "CLIENT_REPORTING_UNSELECTED_POSTINGACCOUNTIDS")
  @ValueMapping(target = "compare_capitalsource", source = "CLIENT_COMPARE_DATA_SELECTED_CAPITALSOURCE")
  @ValueMapping(target = "compare_format", source = "CLIENT_COMPARE_DATA_SELECTED_FORMAT")
  @ValueMapping(target = "compare_source_is_file", source = "CLIENT_COMPARE_DATA_SELECTED_SOURCE_IS_FILE")
  @ValueMapping(target = "client_calc_etf_sale_ask_price", source = "CLIENT_CALC_ETF_SALE_ASK_PRICE")
  @ValueMapping(target = "client_calc_etf_sale_bid_price", source = "CLIENT_CALC_ETF_SALE_BID_PRICE")
  @ValueMapping(target = "client_calc_etf_sale_isin", source = "CLIENT_CALC_ETF_SALE_ISIN")
  @ValueMapping(target = "client_calc_etf_sale_pieces", source = "CLIENT_CALC_ETF_SALE_PIECES")
  @ValueMapping(target = "client_calc_etf_sale_transaction_costs", source = "CLIENT_CALC_ETF_SALE_TRANSACTION_COSTS")
  @ValueMapping(target = MappingConstants.THROW_EXCEPTION, source = MappingConstants.ANY_UNMAPPED)
  String getSettingNameByType(SettingType type);
}