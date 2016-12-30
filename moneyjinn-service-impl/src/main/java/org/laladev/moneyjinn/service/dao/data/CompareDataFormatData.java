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

package org.laladev.moneyjinn.service.dao.data;

public class CompareDataFormatData {
	private Long formatId;
	private String name;
	private String startTrigger0;
	private String startTrigger1;
	private String startTrigger2;
	private String startline;
	private String delimiter;
	private Short posDate;
	private Short posPartner;
	private Short posAmount;
	private Short posComment;
	private String fmtDate;
	private String fmtAmountDecimal;
	private String fmtAmountThousand;
	private Short posPartnerAlt;
	private Short posPartnerAltPosKey;
	private String posPartnerAltKeyword;
	private Short posCreditDebitIndicator;
	private String creditIndicator;

	public final Long getFormatId() {
		return this.formatId;
	}

	public final void setFormatId(final Long formatId) {
		this.formatId = formatId;
	}

	public final String getName() {
		return this.name;
	}

	public final void setName(final String name) {
		this.name = name;
	}

	public final String getStartTrigger0() {
		return this.startTrigger0;
	}

	public final void setStartTrigger0(final String startTrigger0) {
		this.startTrigger0 = startTrigger0;
	}

	public final String getStartTrigger1() {
		return this.startTrigger1;
	}

	public final void setStartTrigger1(final String startTrigger1) {
		this.startTrigger1 = startTrigger1;
	}

	public final String getStartTrigger2() {
		return this.startTrigger2;
	}

	public final void setStartTrigger2(final String startTrigger2) {
		this.startTrigger2 = startTrigger2;
	}

	public final String getStartline() {
		return this.startline;
	}

	public final void setStartline(final String startline) {
		this.startline = startline;
	}

	public final String getDelimiter() {
		return this.delimiter;
	}

	public final void setDelimiter(final String delimiter) {
		this.delimiter = delimiter;
	}

	public final Short getPosDate() {
		return this.posDate;
	}

	public final void setPosDate(final Short posDate) {
		this.posDate = posDate;
	}

	public final Short getPosPartner() {
		return this.posPartner;
	}

	public final void setPosPartner(final Short posPartner) {
		this.posPartner = posPartner;
	}

	public final Short getPosAmount() {
		return this.posAmount;
	}

	public final void setPosAmount(final Short posAmount) {
		this.posAmount = posAmount;
	}

	public final Short getPosComment() {
		return this.posComment;
	}

	public final void setPosComment(final Short posComment) {
		this.posComment = posComment;
	}

	public final String getFmtDate() {
		return this.fmtDate;
	}

	public final void setFmtDate(final String fmtDate) {
		this.fmtDate = fmtDate;
	}

	public final String getFmtAmountDecimal() {
		return this.fmtAmountDecimal;
	}

	public final void setFmtAmountDecimal(final String fmtAmountDecimal) {
		this.fmtAmountDecimal = fmtAmountDecimal;
	}

	public final String getFmtAmountThousand() {
		return this.fmtAmountThousand;
	}

	public final void setFmtAmountThousand(final String fmtAmountThousand) {
		this.fmtAmountThousand = fmtAmountThousand;
	}

	public final Short getPosPartnerAlt() {
		return this.posPartnerAlt;
	}

	public final void setPosPartnerAlt(final Short posPartnerAlt) {
		this.posPartnerAlt = posPartnerAlt;
	}

	public final Short getPosPartnerAltPosKey() {
		return this.posPartnerAltPosKey;
	}

	public final void setPosPartnerAltPosKey(final Short posPartnerAltPosKey) {
		this.posPartnerAltPosKey = posPartnerAltPosKey;
	}

	public final String getPosPartnerAltKeyword() {
		return this.posPartnerAltKeyword;
	}

	public final void setPosPartnerAltKeyword(final String posPartnerAltKeyword) {
		this.posPartnerAltKeyword = posPartnerAltKeyword;
	}

	public final Short getPosCreditDebitIndicator() {
		return this.posCreditDebitIndicator;
	}

	public final void setPosCreditDebitIndicator(final Short posCreditDebitIndicator) {
		this.posCreditDebitIndicator = posCreditDebitIndicator;
	}

	public final String getCreditIndicator() {
		return this.creditIndicator;
	}

	public final void setCreditIndicator(final String creditIndicator) {
		this.creditIndicator = creditIndicator;
	}

}
