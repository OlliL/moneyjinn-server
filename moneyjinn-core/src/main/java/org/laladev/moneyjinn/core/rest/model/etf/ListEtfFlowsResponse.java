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
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.laladev.moneyjinn.core.rest.model.AbstractResponse;
import org.laladev.moneyjinn.core.rest.model.etf.transport.EtfFlowTransport;
import org.laladev.moneyjinn.core.rest.model.etf.transport.EtfTransport;

@XmlRootElement(name = "listEtfFlowsResponse")
public class ListEtfFlowsResponse extends AbstractResponse {
	@XmlElement(name = "etfTransport")
	private List<EtfTransport> etfTransports;
	@XmlElement(name = "etfFlowTransport")
	private List<EtfFlowTransport> etfFlowTransports;
	private String calcEtfSaleIsin;
	private BigDecimal calcEtfSalePieces;
	private BigDecimal calcEtfBidPrice;
	private BigDecimal calcEtfAskPrice;
	private BigDecimal calcEtfTransactionCosts;

	public final List<EtfTransport> getEtfTransports() {
		return this.etfTransports;
	}

	public final void setEtfTransports(final List<EtfTransport> etfTransports) {
		this.etfTransports = etfTransports;
	}

	public final List<EtfFlowTransport> getEtfFlowTransports() {
		return this.etfFlowTransports;
	}

	public final void setEtfFlowTransports(final List<EtfFlowTransport> etfFlowTransports) {
		this.etfFlowTransports = etfFlowTransports;
	}

	public final String getCalcEtfSaleIsin() {
		return this.calcEtfSaleIsin;
	}

	public final void setCalcEtfSaleIsin(final String calcEtfSaleIsin) {
		this.calcEtfSaleIsin = calcEtfSaleIsin;
	}

	public final BigDecimal getCalcEtfSalePieces() {
		return this.calcEtfSalePieces;
	}

	public final void setCalcEtfSalePieces(final BigDecimal calcEtfSalePieces) {
		this.calcEtfSalePieces = calcEtfSalePieces;
	}

	public final BigDecimal getCalcEtfBidPrice() {
		return this.calcEtfBidPrice;
	}

	public final void setCalcEtfBidPrice(final BigDecimal calcEtfBidPrice) {
		this.calcEtfBidPrice = calcEtfBidPrice;
	}

	public final BigDecimal getCalcEtfAskPrice() {
		return this.calcEtfAskPrice;
	}

	public final void setCalcEtfAskPrice(final BigDecimal calcEtfAskPrice) {
		this.calcEtfAskPrice = calcEtfAskPrice;
	}

	public final BigDecimal getCalcEtfTransactionCosts() {
		return this.calcEtfTransactionCosts;
	}

	public final void setCalcEtfTransactionCosts(final BigDecimal calcEtfTransactionCosts) {
		this.calcEtfTransactionCosts = calcEtfTransactionCosts;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((this.calcEtfAskPrice == null) ? 0 : this.calcEtfAskPrice.hashCode());
		result = prime * result + ((this.calcEtfBidPrice == null) ? 0 : this.calcEtfBidPrice.hashCode());
		result = prime * result + ((this.calcEtfSaleIsin == null) ? 0 : this.calcEtfSaleIsin.hashCode());
		result = prime * result + ((this.calcEtfSalePieces == null) ? 0 : this.calcEtfSalePieces.hashCode());
		result = prime * result
				+ ((this.calcEtfTransactionCosts == null) ? 0 : this.calcEtfTransactionCosts.hashCode());
		result = prime * result + ((this.etfFlowTransports == null) ? 0 : this.etfFlowTransports.hashCode());
		result = prime * result + ((this.etfTransports == null) ? 0 : this.etfTransports.hashCode());
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
		final ListEtfFlowsResponse other = (ListEtfFlowsResponse) obj;
		if (this.calcEtfAskPrice == null) {
			if (other.calcEtfAskPrice != null) {
				return false;
			}
		} else if (!this.calcEtfAskPrice.equals(other.calcEtfAskPrice)) {
			return false;
		}
		if (this.calcEtfBidPrice == null) {
			if (other.calcEtfBidPrice != null) {
				return false;
			}
		} else if (!this.calcEtfBidPrice.equals(other.calcEtfBidPrice)) {
			return false;
		}
		if (this.calcEtfSaleIsin == null) {
			if (other.calcEtfSaleIsin != null) {
				return false;
			}
		} else if (!this.calcEtfSaleIsin.equals(other.calcEtfSaleIsin)) {
			return false;
		}
		if (this.calcEtfSalePieces == null) {
			if (other.calcEtfSalePieces != null) {
				return false;
			}
		} else if (!this.calcEtfSalePieces.equals(other.calcEtfSalePieces)) {
			return false;
		}
		if (this.calcEtfTransactionCosts == null) {
			if (other.calcEtfTransactionCosts != null) {
				return false;
			}
		} else if (!this.calcEtfTransactionCosts.equals(other.calcEtfTransactionCosts)) {
			return false;
		}
		if (this.etfFlowTransports == null) {
			if (other.etfFlowTransports != null) {
				return false;
			}
		} else if (!this.etfFlowTransports.equals(other.etfFlowTransports)) {
			return false;
		}
		if (this.etfTransports == null) {
			if (other.etfTransports != null) {
				return false;
			}
		} else if (!this.etfTransports.equals(other.etfTransports)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("ListEtfFlowsResponse [etfTransports=");
		builder.append(this.etfTransports);
		builder.append(", etfFlowTransports=");
		builder.append(this.etfFlowTransports);
		builder.append(", calcEtfSaleIsin=");
		builder.append(this.calcEtfSaleIsin);
		builder.append(", calcEtfSalePieces=");
		builder.append(this.calcEtfSalePieces);
		builder.append(", calcEtfBidPrice=");
		builder.append(this.calcEtfBidPrice);
		builder.append(", calcEtfAskPrice=");
		builder.append(this.calcEtfAskPrice);
		builder.append(", calcEtfTransactionCosts=");
		builder.append(this.calcEtfTransactionCosts);
		builder.append("]");
		return builder.toString();
	}

}