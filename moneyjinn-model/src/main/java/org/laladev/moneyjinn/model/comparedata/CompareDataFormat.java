//
// Copyright (c) 2015-2025 Oliver Lehmann <lehmann@ans-netz.de>
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

package org.laladev.moneyjinn.model.comparedata;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.laladev.moneyjinn.model.AbstractEntity;

import java.io.Serial;
import java.util.List;
import java.util.regex.Pattern;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CompareDataFormat extends AbstractEntity<CompareDataFormatID> {
    @Serial
    private static final long serialVersionUID = 1L;
    private CompareDataFormatType type;
    private String name;
    private List<String> startTrigger;
    private List<String> endTrigger;
    private Character delimiter;
    private Integer positionDate;
    private Integer positionInvoicedate;
    private Integer positionPartner;
    private Integer positionAmount;
    private Integer positionComment;
    private String formatDate;
    private Character formatAmountDecimal;
    private Character formatAmountThousand;
    private Integer positionPartnerAlternative;
    private Integer partnerAlternativeIndicatorPosition;
    private Pattern partnerAlternativeIndicator;
    private Integer positionCreditDebitIndicator;
    private Pattern creditIndicator;
}