//
//Copyright (c) 2015 Oliver Lehmann <oliver@laladev.org>
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
//

package org.laladev.moneyjinn.core.rest.model.report;

import java.util.List;

import org.laladev.moneyjinn.core.rest.model.AbstractResponse;
import org.laladev.moneyjinn.core.rest.model.report.transport.TrendsCalculatedTransport;
import org.laladev.moneyjinn.core.rest.model.report.transport.TrendsSettledTransport;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "showTrendsGraphResponse")
public class ShowTrendsGraphResponse extends AbstractResponse {
	@XmlElement(name = "trendsSettledTransport")
	private List<TrendsSettledTransport> trendsSettledTransports;
	@XmlElement(name = "trendsCalculatedTransport")
	private List<TrendsCalculatedTransport> trendsCalculatedTransports;

	public final List<TrendsSettledTransport> getTrendsSettledTransports() {
		return this.trendsSettledTransports;
	}

	public final void setTrendsSettledTransports(final List<TrendsSettledTransport> trendsSettledTransports) {
		this.trendsSettledTransports = trendsSettledTransports;
	}

	public final List<TrendsCalculatedTransport> getTrendsCalculatedTransports() {
		return this.trendsCalculatedTransports;
	}

	public final void setTrendsCalculatedTransports(final List<TrendsCalculatedTransport> trendsCalculatedTransports) {
		this.trendsCalculatedTransports = trendsCalculatedTransports;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((this.trendsCalculatedTransports == null) ? 0 : this.trendsCalculatedTransports.hashCode());
		result = prime * result
				+ ((this.trendsSettledTransports == null) ? 0 : this.trendsSettledTransports.hashCode());
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
		final ShowTrendsGraphResponse other = (ShowTrendsGraphResponse) obj;
		if (this.trendsCalculatedTransports == null) {
			if (other.trendsCalculatedTransports != null) {
				return false;
			}
		} else if (!this.trendsCalculatedTransports.equals(other.trendsCalculatedTransports)) {
			return false;
		}
		if (this.trendsSettledTransports == null) {
			if (other.trendsSettledTransports != null) {
				return false;
			}
		} else if (!this.trendsSettledTransports.equals(other.trendsSettledTransports)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("ShowTrendsGraphResponse [trendsSettledTransports=");
		builder.append(this.trendsSettledTransports);
		builder.append(", trendsCalculatedTransports=");
		builder.append(this.trendsCalculatedTransports);
		builder.append("]");
		return builder.toString();
	}

}
