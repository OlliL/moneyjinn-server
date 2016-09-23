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

package org.laladev.moneyjinn.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.laladev.moneyjinn.model.capitalsource.Capitalsource;

public class ImportedBalance {
	private Capitalsource capitalsource;
	private LocalDateTime date;
	private BigDecimal balance;

	public final Capitalsource getCapitalsource() {
		return this.capitalsource;
	}

	public final void setCapitalsource(final Capitalsource capitalsource) {
		this.capitalsource = capitalsource;
	}

	public final LocalDateTime getDate() {
		return this.date;
	}

	public final void setDate(final LocalDateTime date) {
		this.date = date;
	}

	public final BigDecimal getBalance() {
		return this.balance;
	}

	public final void setBalance(final BigDecimal balance) {
		this.balance = balance;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.balance == null) ? 0 : this.balance.hashCode());
		result = prime * result + ((this.capitalsource == null) ? 0 : this.capitalsource.hashCode());
		result = prime * result + ((this.date == null) ? 0 : this.date.hashCode());
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
		final ImportedBalance other = (ImportedBalance) obj;
		if (this.balance == null) {
			if (other.balance != null) {
				return false;
			}
		} else if (!this.balance.equals(other.balance)) {
			return false;
		}
		if (this.capitalsource == null) {
			if (other.capitalsource != null) {
				return false;
			}
		} else if (!this.capitalsource.equals(other.capitalsource)) {
			return false;
		}
		if (this.date == null) {
			if (other.date != null) {
				return false;
			}
		} else if (!this.date.equals(other.date)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("ImportedBalance [capitalsource=");
		builder.append(this.capitalsource);
		builder.append(", date=");
		builder.append(this.date);
		builder.append(", balance=");
		builder.append(this.balance);
		builder.append("]");
		return builder.toString();
	}

}
