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

import org.laladev.moneyjinn.core.rest.model.ValidationResponse;

@XmlRootElement(name = "calcEtfSaleResponse")
public class CalcEtfSaleResponse extends ValidationResponse {
	private String isin;
	private BigDecimal originalBuyPrice;
	private BigDecimal sellPrice;
	private BigDecimal newBuyPrice;
	private BigDecimal profit;
	private BigDecimal chargeable;
	private BigDecimal transactionCosts;
	private BigDecimal rebuyLosses;
	private BigDecimal overallCosts;
	private BigDecimal pieces;

	public final String getIsin() {
		return this.isin;
	}

	public final void setIsin(final String isin) {
		this.isin = isin;
	}

	public final BigDecimal getOriginalBuyPrice() {
		return this.originalBuyPrice;
	}

	public final void setOriginalBuyPrice(final BigDecimal originalBuyPrice) {
		this.originalBuyPrice = originalBuyPrice;
	}

	public final BigDecimal getSellPrice() {
		return this.sellPrice;
	}

	public final void setSellPrice(final BigDecimal sellPrice) {
		this.sellPrice = sellPrice;
	}

	public final BigDecimal getNewBuyPrice() {
		return this.newBuyPrice;
	}

	public final void setNewBuyPrice(final BigDecimal newBuyPrice) {
		this.newBuyPrice = newBuyPrice;
	}

	public final BigDecimal getProfit() {
		return this.profit;
	}

	public final void setProfit(final BigDecimal profit) {
		this.profit = profit;
	}

	public final BigDecimal getChargeable() {
		return this.chargeable;
	}

	public final void setChargeable(final BigDecimal chargeable) {
		this.chargeable = chargeable;
	}

	public final BigDecimal getTransactionCosts() {
		return this.transactionCosts;
	}

	public final void setTransactionCosts(final BigDecimal transactionCosts) {
		this.transactionCosts = transactionCosts;
	}

	public final BigDecimal getRebuyLosses() {
		return this.rebuyLosses;
	}

	public final void setRebuyLosses(final BigDecimal rebuyLosses) {
		this.rebuyLosses = rebuyLosses;
	}

	public final BigDecimal getOverallCosts() {
		return this.overallCosts;
	}

	public final void setOverallCosts(final BigDecimal overallCosts) {
		this.overallCosts = overallCosts;
	}

	public final BigDecimal getPieces() {
		return this.pieces;
	}

	public final void setPieces(final BigDecimal pieces) {
		this.pieces = pieces;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((this.chargeable == null) ? 0 : this.chargeable.hashCode());
		result = prime * result + ((this.isin == null) ? 0 : this.isin.hashCode());
		result = prime * result + ((this.newBuyPrice == null) ? 0 : this.newBuyPrice.hashCode());
		result = prime * result + ((this.originalBuyPrice == null) ? 0 : this.originalBuyPrice.hashCode());
		result = prime * result + ((this.overallCosts == null) ? 0 : this.overallCosts.hashCode());
		result = prime * result + ((this.pieces == null) ? 0 : this.pieces.hashCode());
		result = prime * result + ((this.profit == null) ? 0 : this.profit.hashCode());
		result = prime * result + ((this.rebuyLosses == null) ? 0 : this.rebuyLosses.hashCode());
		result = prime * result + ((this.sellPrice == null) ? 0 : this.sellPrice.hashCode());
		result = prime * result + ((this.transactionCosts == null) ? 0 : this.transactionCosts.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		final CalcEtfSaleResponse other = (CalcEtfSaleResponse) obj;
		if (this.chargeable == null) {
			if (other.chargeable != null) {
				return false;
			}
		} else if (!this.chargeable.equals(other.chargeable)) {
			return false;
		}
		if (this.isin == null) {
			if (other.isin != null) {
				return false;
			}
		} else if (!this.isin.equals(other.isin)) {
			return false;
		}
		if (this.newBuyPrice == null) {
			if (other.newBuyPrice != null) {
				return false;
			}
		} else if (!this.newBuyPrice.equals(other.newBuyPrice)) {
			return false;
		}
		if (this.originalBuyPrice == null) {
			if (other.originalBuyPrice != null) {
				return false;
			}
		} else if (!this.originalBuyPrice.equals(other.originalBuyPrice)) {
			return false;
		}
		if (this.overallCosts == null) {
			if (other.overallCosts != null) {
				return false;
			}
		} else if (!this.overallCosts.equals(other.overallCosts)) {
			return false;
		}
		if (this.pieces == null) {
			if (other.pieces != null) {
				return false;
			}
		} else if (!this.pieces.equals(other.pieces)) {
			return false;
		}
		if (this.profit == null) {
			if (other.profit != null) {
				return false;
			}
		} else if (!this.profit.equals(other.profit)) {
			return false;
		}
		if (this.rebuyLosses == null) {
			if (other.rebuyLosses != null) {
				return false;
			}
		} else if (!this.rebuyLosses.equals(other.rebuyLosses)) {
			return false;
		}
		if (this.sellPrice == null) {
			if (other.sellPrice != null) {
				return false;
			}
		} else if (!this.sellPrice.equals(other.sellPrice)) {
			return false;
		}
		if (this.transactionCosts == null) {
			if (other.transactionCosts != null) {
				return false;
			}
		} else if (!this.transactionCosts.equals(other.transactionCosts)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("CalcEtfSaleResponse [isin=");
		builder.append(this.isin);
		builder.append(", originalBuyPrice=");
		builder.append(this.originalBuyPrice);
		builder.append(", sellPrice=");
		builder.append(this.sellPrice);
		builder.append(", newBuyPrice=");
		builder.append(this.newBuyPrice);
		builder.append(", profit=");
		builder.append(this.profit);
		builder.append(", chargeable=");
		builder.append(this.chargeable);
		builder.append(", transactionCosts=");
		builder.append(this.transactionCosts);
		builder.append(", rebuyLosses=");
		builder.append(this.rebuyLosses);
		builder.append(", overallCosts=");
		builder.append(this.overallCosts);
		builder.append(", pieces=");
		builder.append(this.pieces);
		builder.append("]");
		return builder.toString();
	}

}
