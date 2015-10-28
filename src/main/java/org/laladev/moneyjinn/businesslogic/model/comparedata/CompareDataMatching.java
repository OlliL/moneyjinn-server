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

package org.laladev.moneyjinn.businesslogic.model.comparedata;

import org.laladev.moneyjinn.businesslogic.model.moneyflow.Moneyflow;

public class CompareDataMatching {
	private Moneyflow moneyflow;
	private CompareDataDataset compareDataDataset;

	public final Moneyflow getMoneyflow() {
		return this.moneyflow;
	}

	public final void setMoneyflow(final Moneyflow moneyflow) {
		this.moneyflow = moneyflow;
	}

	public final CompareDataDataset getCompareDataDataset() {
		return this.compareDataDataset;
	}

	public final void setCompareDataDataset(final CompareDataDataset compareDataDataset) {
		this.compareDataDataset = compareDataDataset;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.compareDataDataset == null) ? 0 : this.compareDataDataset.hashCode());
		result = prime * result + ((this.moneyflow == null) ? 0 : this.moneyflow.hashCode());
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
		final CompareDataMatching other = (CompareDataMatching) obj;
		if (this.compareDataDataset == null) {
			if (other.compareDataDataset != null) {
				return false;
			}
		} else if (!this.compareDataDataset.equals(other.compareDataDataset)) {
			return false;
		}
		if (this.moneyflow == null) {
			if (other.moneyflow != null) {
				return false;
			}
		} else if (!this.moneyflow.equals(other.moneyflow)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("CompareDataMatching [moneyflow=");
		builder.append(this.moneyflow);
		builder.append(", compareDataDataset=");
		builder.append(this.compareDataDataset);
		builder.append("]");
		return builder.toString();
	}

}
