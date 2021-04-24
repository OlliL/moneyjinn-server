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

package org.laladev.moneyjinn.core.rest.model.etf.transport;

public class EtfTransport {
	private String isin;
	private String name;
	private String wkn;
	private String ticker;
	private String chartUrl;

	public final String getIsin() {
		return this.isin;
	}

	public final void setIsin(final String isin) {
		this.isin = isin;
	}

	public final String getName() {
		return this.name;
	}

	public final void setName(final String name) {
		this.name = name;
	}

	public final String getWkn() {
		return this.wkn;
	}

	public final void setWkn(final String wkn) {
		this.wkn = wkn;
	}

	public final String getTicker() {
		return this.ticker;
	}

	public final void setTicker(final String ticker) {
		this.ticker = ticker;
	}

	public final String getChartUrl() {
		return this.chartUrl;
	}

	public final void setChartUrl(final String chartUrl) {
		this.chartUrl = chartUrl;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.chartUrl == null) ? 0 : this.chartUrl.hashCode());
		result = prime * result + ((this.isin == null) ? 0 : this.isin.hashCode());
		result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
		result = prime * result + ((this.ticker == null) ? 0 : this.ticker.hashCode());
		result = prime * result + ((this.wkn == null) ? 0 : this.wkn.hashCode());
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
		final EtfTransport other = (EtfTransport) obj;
		if (this.chartUrl == null) {
			if (other.chartUrl != null) {
				return false;
			}
		} else if (!this.chartUrl.equals(other.chartUrl)) {
			return false;
		}
		if (this.isin == null) {
			if (other.isin != null) {
				return false;
			}
		} else if (!this.isin.equals(other.isin)) {
			return false;
		}
		if (this.name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!this.name.equals(other.name)) {
			return false;
		}
		if (this.ticker == null) {
			if (other.ticker != null) {
				return false;
			}
		} else if (!this.ticker.equals(other.ticker)) {
			return false;
		}
		if (this.wkn == null) {
			if (other.wkn != null) {
				return false;
			}
		} else if (!this.wkn.equals(other.wkn)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("EtfTransport [isin=");
		builder.append(this.isin);
		builder.append(", name=");
		builder.append(this.name);
		builder.append(", wkn=");
		builder.append(this.wkn);
		builder.append(", ticker=");
		builder.append(this.ticker);
		builder.append(", chartUrl=");
		builder.append(this.chartUrl);
		builder.append("]");
		return builder.toString();
	}

}
