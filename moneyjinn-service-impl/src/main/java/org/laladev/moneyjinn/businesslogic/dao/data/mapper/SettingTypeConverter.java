//
// Copyright (c) 2015 Oliver Lehmann <oliver@laladev.org>
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

package org.laladev.moneyjinn.businesslogic.dao.data.mapper;

import org.laladev.moneyjinn.model.setting.SettingType;

public class SettingTypeConverter {

	private SettingTypeConverter() {

	}

	public static String getSettingNameByType(final SettingType type) {
		switch (type) {
		case CLIENT_MAX_ROWS:
			return "max_rows";
		case CLIENT_TREND_CAPITALSOURCEIDS:
			return "trend_capitalsourceid";
		case CLIENT_DATE_FORMAT:
			return "date_format";
		case CLIENT_DISPLAYED_LANGUAGE:
			return "displayed_language";
		case CLIENT_NUM_FREE_MONEYFLOWS:
			return "num_free_moneyflows";
		case CLIENT_CURRENTLY_VALID_CAPITALSOURCES:
			return "currently_valid_capitalsources";
		case CLIENT_CURRENTLY_VALID_CONTRACTPARTNER:
			return "currently_valid_contractpartner";
		case CLIENT_REPORTING_UNSELECTED_POSTINGACCOUNTIDS:
			return "reporting_postingaccountids";
		case CLIENT_COMPARE_DATA_SELECTED_CAPITALSOURCE:
			return "compare_capitalsource";
		case CLIENT_COMPARE_DATA_SELECTED_FORMAT:
			return "compare_format";
		default:
			throw new UnsupportedOperationException("SettingType " + type + " unsupported!");
		}
	}
}