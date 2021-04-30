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

package org.laladev.moneyjinn.service.dao.data;

import java.util.Arrays;

public class ImportedMoneyflowReceiptData {
	private Long id;
	private Long macIdCreator;
	private Long macIdAccessor;
	private byte[] receipt;
	private String filename;
	private String mediaType;

	public final Long getId() {
		return this.id;
	}

	public final void setId(final Long id) {
		this.id = id;
	}

	public final Long getMacIdCreator() {
		return this.macIdCreator;
	}

	public final void setMacIdCreator(final Long macIdCreator) {
		this.macIdCreator = macIdCreator;
	}

	public final Long getMacIdAccessor() {
		return this.macIdAccessor;
	}

	public final void setMacIdAccessor(final Long macIdAccessor) {
		this.macIdAccessor = macIdAccessor;
	}

	public final byte[] getReceipt() {
		return this.receipt;
	}

	public final void setReceipt(final byte[] receipt) {
		this.receipt = receipt;
	}

	public final String getFilename() {
		return this.filename;
	}

	public final void setFilename(final String filename) {
		this.filename = filename;
	}

	public final String getMediaType() {
		return this.mediaType;
	}

	public final void setMediaType(final String mediaType) {
		this.mediaType = mediaType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.filename == null) ? 0 : this.filename.hashCode());
		result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
		result = prime * result + ((this.macIdAccessor == null) ? 0 : this.macIdAccessor.hashCode());
		result = prime * result + ((this.macIdCreator == null) ? 0 : this.macIdCreator.hashCode());
		result = prime * result + ((this.mediaType == null) ? 0 : this.mediaType.hashCode());
		result = prime * result + Arrays.hashCode(this.receipt);
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
		final ImportedMoneyflowReceiptData other = (ImportedMoneyflowReceiptData) obj;
		if (this.filename == null) {
			if (other.filename != null) {
				return false;
			}
		} else if (!this.filename.equals(other.filename)) {
			return false;
		}
		if (this.id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!this.id.equals(other.id)) {
			return false;
		}
		if (this.macIdAccessor == null) {
			if (other.macIdAccessor != null) {
				return false;
			}
		} else if (!this.macIdAccessor.equals(other.macIdAccessor)) {
			return false;
		}
		if (this.macIdCreator == null) {
			if (other.macIdCreator != null) {
				return false;
			}
		} else if (!this.macIdCreator.equals(other.macIdCreator)) {
			return false;
		}
		if (this.mediaType == null) {
			if (other.mediaType != null) {
				return false;
			}
		} else if (!this.mediaType.equals(other.mediaType)) {
			return false;
		}
		if (!Arrays.equals(this.receipt, other.receipt)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("ImportedMoneyflowReceiptData [id=");
		builder.append(this.id);
		builder.append(", macIdCreator=");
		builder.append(this.macIdCreator);
		builder.append(", macIdAccessor=");
		builder.append(this.macIdAccessor);
		builder.append(", receipt=");
		builder.append(Arrays.toString(this.receipt));
		builder.append(", filename=");
		builder.append(this.filename);
		builder.append(", mediaType=");
		builder.append(this.mediaType);
		builder.append("]");
		return builder.toString();
	}

}
