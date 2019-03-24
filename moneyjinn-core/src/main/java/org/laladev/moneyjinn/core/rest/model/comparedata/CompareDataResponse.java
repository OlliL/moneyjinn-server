//
// Copyright (c) 2015 Oliver Lehmann <lehmann@ans-netz.de>
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

package org.laladev.moneyjinn.core.rest.model.comparedata;

import java.util.ArrayList;
import java.util.List;

import org.laladev.moneyjinn.core.rest.model.AbstractResponse;
import org.laladev.moneyjinn.core.rest.model.comparedata.transport.CompareDataMatchingTransport;
import org.laladev.moneyjinn.core.rest.model.comparedata.transport.CompareDataNotInDatabaseTransport;
import org.laladev.moneyjinn.core.rest.model.comparedata.transport.CompareDataNotInFileTransport;
import org.laladev.moneyjinn.core.rest.model.comparedata.transport.CompareDataWrongCapitalsourceTransport;
import org.laladev.moneyjinn.core.rest.model.transport.CapitalsourceTransport;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "compareDataResponse")
public class CompareDataResponse extends AbstractResponse {
	@XmlElement(name = "compareDataMatchingTransport")
	private final List<CompareDataMatchingTransport> compareDataMatchingTransports = new ArrayList<CompareDataMatchingTransport>();
	@XmlElement(name = "compareDataWrongCapitalsourceTransport")
	private final List<CompareDataWrongCapitalsourceTransport> compareDataWrongCapitalsourceTransports = new ArrayList<CompareDataWrongCapitalsourceTransport>();
	@XmlElement(name = "compareDataNotInFileTransport")
	private final List<CompareDataNotInFileTransport> compareDataNotInFileTransports = new ArrayList<CompareDataNotInFileTransport>();
	@XmlElement(name = "compareDataNotInDatabaseTransport")
	private final List<CompareDataNotInDatabaseTransport> compareDataNotInDatabaseTransports = new ArrayList<CompareDataNotInDatabaseTransport>();
	private CapitalsourceTransport capitalsourceTransport;

	public final List<CompareDataMatchingTransport> getCompareDataMatchingTransports() {
		return this.compareDataMatchingTransports;
	}

	public final void addCompareDataMatchingTransport(final CompareDataMatchingTransport compareDataMatchingTransport) {
		this.compareDataMatchingTransports.add(compareDataMatchingTransport);
	}

	public final List<CompareDataWrongCapitalsourceTransport> getCompareDataWrongCapitalsourceTransports() {
		return this.compareDataWrongCapitalsourceTransports;
	}

	public final void addCompareDataWrongCapitalsourceTransport(
			final CompareDataWrongCapitalsourceTransport compareDataWrongCapitalsourceTransport) {
		this.compareDataWrongCapitalsourceTransports.add(compareDataWrongCapitalsourceTransport);
	}

	public final List<CompareDataNotInFileTransport> getCompareDataNotInFileTransports() {
		return this.compareDataNotInFileTransports;
	}

	public final void addCompareDataNotInFileTransport(
			final CompareDataNotInFileTransport compareDataNotInFileTransport) {
		this.compareDataNotInFileTransports.add(compareDataNotInFileTransport);
	}

	public final List<CompareDataNotInDatabaseTransport> getCompareDataNotInDatabaseTransports() {
		return this.compareDataNotInDatabaseTransports;
	}

	public final void addCompareDataNotInDatabaseTransport(
			final CompareDataNotInDatabaseTransport compareDataNotInDatabaseTransport) {
		this.compareDataNotInDatabaseTransports.add(compareDataNotInDatabaseTransport);
	}

	public final CapitalsourceTransport getCapitalsourceTransport() {
		return this.capitalsourceTransport;
	}

	public final void setCapitalsourceTransport(final CapitalsourceTransport capitalsourceTransport) {
		this.capitalsourceTransport = capitalsourceTransport;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((this.capitalsourceTransport == null) ? 0 : this.capitalsourceTransport.hashCode());
		result = prime * result
				+ ((this.compareDataMatchingTransports == null) ? 0 : this.compareDataMatchingTransports.hashCode());
		result = prime * result + ((this.compareDataNotInDatabaseTransports == null) ? 0
				: this.compareDataNotInDatabaseTransports.hashCode());
		result = prime * result
				+ ((this.compareDataNotInFileTransports == null) ? 0 : this.compareDataNotInFileTransports.hashCode());
		result = prime * result + ((this.compareDataWrongCapitalsourceTransports == null) ? 0
				: this.compareDataWrongCapitalsourceTransports.hashCode());
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
		final CompareDataResponse other = (CompareDataResponse) obj;
		if (this.capitalsourceTransport == null) {
			if (other.capitalsourceTransport != null) {
				return false;
			}
		} else if (!this.capitalsourceTransport.equals(other.capitalsourceTransport)) {
			return false;
		}
		if (this.compareDataMatchingTransports == null) {
			if (other.compareDataMatchingTransports != null) {
				return false;
			}
		} else if (!this.compareDataMatchingTransports.equals(other.compareDataMatchingTransports)) {
			return false;
		}
		if (this.compareDataNotInDatabaseTransports == null) {
			if (other.compareDataNotInDatabaseTransports != null) {
				return false;
			}
		} else if (!this.compareDataNotInDatabaseTransports.equals(other.compareDataNotInDatabaseTransports)) {
			return false;
		}
		if (this.compareDataNotInFileTransports == null) {
			if (other.compareDataNotInFileTransports != null) {
				return false;
			}
		} else if (!this.compareDataNotInFileTransports.equals(other.compareDataNotInFileTransports)) {
			return false;
		}
		if (this.compareDataWrongCapitalsourceTransports == null) {
			if (other.compareDataWrongCapitalsourceTransports != null) {
				return false;
			}
		} else if (!this.compareDataWrongCapitalsourceTransports
				.equals(other.compareDataWrongCapitalsourceTransports)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("CompareDataResponse [compareDataMatchingTransports=");
		builder.append(this.compareDataMatchingTransports);
		builder.append(", compareDataWrongCapitalsourceTransports=");
		builder.append(this.compareDataWrongCapitalsourceTransports);
		builder.append(", compareDataNotInFileTransports=");
		builder.append(this.compareDataNotInFileTransports);
		builder.append(", compareDataNotInDatabaseTransports=");
		builder.append(this.compareDataNotInDatabaseTransports);
		builder.append(", capitalsourceTransport=");
		builder.append(this.capitalsourceTransport);
		builder.append("]");
		return builder.toString();
	}

}
