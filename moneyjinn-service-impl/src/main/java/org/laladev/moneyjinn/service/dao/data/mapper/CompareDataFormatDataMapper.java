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

import jakarta.inject.Named;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import org.laladev.moneyjinn.core.mapper.IMapper;
import org.laladev.moneyjinn.model.comparedata.CompareDataFormat;
import org.laladev.moneyjinn.model.comparedata.CompareDataFormatID;
import org.laladev.moneyjinn.model.comparedata.CompareDataFormatType;
import org.laladev.moneyjinn.service.dao.data.CompareDataFormatData;

@Named
public class CompareDataFormatDataMapper
    implements IMapper<CompareDataFormat, CompareDataFormatData> {
  @Override
  public CompareDataFormat mapBToA(final CompareDataFormatData compareDataFormatData) {
    final CompareDataFormat compareDataFormat = new CompareDataFormat();
    compareDataFormat.setId(new CompareDataFormatID(compareDataFormatData.getFormatId()));
    compareDataFormat.setName(compareDataFormatData.getName());
    if ("camt".equals(compareDataFormatData.getStartline())) {
      compareDataFormat.setType(CompareDataFormatType.XML);
    } else {
      compareDataFormat.setType(CompareDataFormatType.CVS);
      final List<String> startTrigger = new ArrayList<>(3);
      startTrigger.add(compareDataFormatData.getStartTrigger0());
      startTrigger.add(compareDataFormatData.getStartTrigger1());
      startTrigger.add(compareDataFormatData.getStartTrigger2());
      compareDataFormat.setStartTrigger(startTrigger);
      final String delimiter = compareDataFormatData.getDelimiter();
      if (delimiter != null) {
        compareDataFormat.setDelimiter(delimiter.charAt(0));
      }
      compareDataFormat.setPositionDate(compareDataFormatData.getPosDate());
      compareDataFormat.setPositionPartner(compareDataFormatData.getPosPartner());
      compareDataFormat.setPositionAmount(compareDataFormatData.getPosAmount());
      compareDataFormat.setPositionComment(compareDataFormatData.getPosComment());
      final String dateFormat = compareDataFormatData.getFmtDate().replace("DD", "dd")
          .replace("YYYY", "yyyy");
      compareDataFormat.setFormatDate(DateTimeFormatter.ofPattern(dateFormat));
      final String fmtAmountDecimal = compareDataFormatData.getFmtAmountDecimal();
      if (fmtAmountDecimal != null) {
        compareDataFormat.setFormatAmountDecimal(fmtAmountDecimal.charAt(0));
      }
      final String fmtAmountThousand = compareDataFormatData.getFmtAmountThousand();
      if (fmtAmountThousand != null) {
        compareDataFormat.setFormatAmountThousand(fmtAmountThousand.charAt(0));
      }
      final String posPartnerAltKeyword = compareDataFormatData.getPosPartnerAltKeyword();
      if (posPartnerAltKeyword != null) {
        compareDataFormat.setPositionPartnerAlternative(compareDataFormatData.getPosPartnerAlt());
        compareDataFormat
            .setPartnerAlternativeIndicatorPosition(compareDataFormatData.getPosPartnerAltPosKey());
        final Pattern keyword = this.getPattern(posPartnerAltKeyword);
        compareDataFormat.setPartnerAlternativeIndicator(keyword);
      }
      final String creditIndicator = compareDataFormatData.getCreditIndicator();
      if (creditIndicator != null) {
        compareDataFormat
            .setPositionCreditDebitIndicator(compareDataFormatData.getPosCreditDebitIndicator());
        final Pattern keyword = this.getPattern(creditIndicator);
        compareDataFormat.setCreditIndicator(keyword);
      }
    }
    return compareDataFormat;
  }

  private Pattern getPattern(final String pattern) {
    return Pattern.compile(pattern.replaceFirst("/$", "").replaceFirst("^/", ""));
  }

  @Override
  public CompareDataFormatData mapAToB(final CompareDataFormat compareDataFormat) {
    throw new UnsupportedOperationException("Mapping not supported!");
  }
}
