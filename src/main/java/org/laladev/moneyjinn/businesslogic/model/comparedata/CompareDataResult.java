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

public class CompareDataResult {
	private CompareDataMatching compareDataMatching;
	private CompareDataWrongCapitalsource compareDataWrongCapitalsource;
	private CompareDataNotInFile compareDataNotInFile;
	private CompareDataNotInDatabase compareDataNotInDatabase;

	public final CompareDataMatching getCompareDataMatching() {
		return this.compareDataMatching;
	}

	public final void setCompareDataMatching(final CompareDataMatching compareDataMatching) {
		this.compareDataMatching = compareDataMatching;
	}

	public final CompareDataWrongCapitalsource getCompareDataWrongCapitalsource() {
		return this.compareDataWrongCapitalsource;
	}

	public final void setCompareDataWrongCapitalsource(
			final CompareDataWrongCapitalsource compareDataWrongCapitalsource) {
		this.compareDataWrongCapitalsource = compareDataWrongCapitalsource;
	}

	public final CompareDataNotInFile getCompareDataNotInFile() {
		return this.compareDataNotInFile;
	}

	public final void setCompareDataNotInFile(final CompareDataNotInFile compareDataNotInFile) {
		this.compareDataNotInFile = compareDataNotInFile;
	}

	public final CompareDataNotInDatabase getCompareDataNotInDatabase() {
		return this.compareDataNotInDatabase;
	}

	public final void setCompareDataNotInDatabase(final CompareDataNotInDatabase compareDataNotInDatabase) {
		this.compareDataNotInDatabase = compareDataNotInDatabase;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.compareDataMatching == null) ? 0 : this.compareDataMatching.hashCode());
		result = prime * result
				+ ((this.compareDataNotInDatabase == null) ? 0 : this.compareDataNotInDatabase.hashCode());
		result = prime * result + ((this.compareDataNotInFile == null) ? 0 : this.compareDataNotInFile.hashCode());
		result = prime * result
				+ ((this.compareDataWrongCapitalsource == null) ? 0 : this.compareDataWrongCapitalsource.hashCode());
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
		final CompareDataResult other = (CompareDataResult) obj;
		if (this.compareDataMatching == null) {
			if (other.compareDataMatching != null) {
				return false;
			}
		} else if (!this.compareDataMatching.equals(other.compareDataMatching)) {
			return false;
		}
		if (this.compareDataNotInDatabase == null) {
			if (other.compareDataNotInDatabase != null) {
				return false;
			}
		} else if (!this.compareDataNotInDatabase.equals(other.compareDataNotInDatabase)) {
			return false;
		}
		if (this.compareDataNotInFile == null) {
			if (other.compareDataNotInFile != null) {
				return false;
			}
		} else if (!this.compareDataNotInFile.equals(other.compareDataNotInFile)) {
			return false;
		}
		if (this.compareDataWrongCapitalsource == null) {
			if (other.compareDataWrongCapitalsource != null) {
				return false;
			}
		} else if (!this.compareDataWrongCapitalsource.equals(other.compareDataWrongCapitalsource)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("CompareDataResult [compareDataMatching=");
		builder.append(this.compareDataMatching);
		builder.append(", compareDataWrongCapitalsource=");
		builder.append(this.compareDataWrongCapitalsource);
		builder.append(", compareDataNotInFile=");
		builder.append(this.compareDataNotInFile);
		builder.append(", compareDataNotInDatabase=");
		builder.append(this.compareDataNotInDatabase);
		builder.append("]");
		return builder.toString();
	}

}
