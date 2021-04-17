//
// Copyright (c) 2021 Oliver Lehmann <lehmann@ans-netz.de>
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
package org.laladev.moneyjinn.core.rest.model.etf;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlRootElement;

import org.laladev.moneyjinn.core.rest.model.AbstractRequest;

@XmlRootElement(name = "calcEtfSaleRequest")
public class CalcEtfSaleRequest extends AbstractRequest {
	private String isin;
	private BigDecimal pieces;
	private BigDecimal bidPrice;
	private BigDecimal askPrice;
	private BigDecimal TransactionCosts;

	public final String getIsin() {
		return this.isin;
	}

	public final void setIsin(final String isin) {
		this.isin = isin;
	}

	public final BigDecimal getPieces() {
		return this.pieces;
	}

	public final void setPieces(final BigDecimal pieces) {
		this.pieces = pieces;
	}

	public final BigDecimal getBidPrice() {
		return this.bidPrice;
	}

	public final void setBidPrice(final BigDecimal bidPrice) {
		this.bidPrice = bidPrice;
	}

	public final BigDecimal getAskPrice() {
		return this.askPrice;
	}

	public final void setAskPrice(final BigDecimal askPrice) {
		this.askPrice = askPrice;
	}

	public final BigDecimal getTransactionCosts() {
		return this.TransactionCosts;
	}

	public final void setTransactionCosts(final BigDecimal transactionCosts) {
		this.TransactionCosts = transactionCosts;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.TransactionCosts == null) ? 0 : this.TransactionCosts.hashCode());
		result = prime * result + ((this.askPrice == null) ? 0 : this.askPrice.hashCode());
		result = prime * result + ((this.bidPrice == null) ? 0 : this.bidPrice.hashCode());
		result = prime * result + ((this.isin == null) ? 0 : this.isin.hashCode());
		result = prime * result + ((this.pieces == null) ? 0 : this.pieces.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		final CalcEtfSaleRequest other = (CalcEtfSaleRequest) obj;
		if (this.TransactionCosts == null) {
			if (other.TransactionCosts != null) {
				return false;
			}
		} else if (!this.TransactionCosts.equals(other.TransactionCosts)) {
			return false;
		}
		if (this.askPrice == null) {
			if (other.askPrice != null) {
				return false;
			}
		} else if (!this.askPrice.equals(other.askPrice)) {
			return false;
		}
		if (this.bidPrice == null) {
			if (other.bidPrice != null) {
				return false;
			}
		} else if (!this.bidPrice.equals(other.bidPrice)) {
			return false;
		}
		if (this.isin == null) {
			if (other.isin != null) {
				return false;
			}
		} else if (!this.isin.equals(other.isin)) {
			return false;
		}
		if (this.pieces == null) {
			if (other.pieces != null) {
				return false;
			}
		} else if (!this.pieces.equals(other.pieces)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("CalcEtfSaleRequest [isin=");
		builder.append(this.isin);
		builder.append(", pieces=");
		builder.append(this.pieces);
		builder.append(", bidPrice=");
		builder.append(this.bidPrice);
		builder.append(", askPrice=");
		builder.append(this.askPrice);
		builder.append(", TransactionCosts=");
		builder.append(this.TransactionCosts);
		builder.append("]");
		return builder.toString();
	}

}
