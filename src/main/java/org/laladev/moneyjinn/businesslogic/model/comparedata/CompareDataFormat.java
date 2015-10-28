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

import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

import org.laladev.moneyjinn.businesslogic.model.AbstractEntity;

public class CompareDataFormat extends AbstractEntity<CompareDataFormatID> {
	private static final long serialVersionUID = 1L;
	private CompareDataFormatType type;
	private String name;
	private Pattern startline;
	private Character delimiter;
	private Short positionDate;
	private Short positionPartner;
	private Short positionAmount;
	private Short positionComment;
	private DateTimeFormatter formatDate;
	private Character formatAmountDecimal;
	private Character formatAmountThousand;
	private Short positionPartnerAlternative;
	private Short positionPartnerAlternativePositionKey;
	private Pattern positionPartnerAlternativeKeyword;

	public final CompareDataFormatType getType() {
		return this.type;
	}

	public final void setType(final CompareDataFormatType type) {
		this.type = type;
	}

	public final String getName() {
		return this.name;
	}

	public final void setName(final String name) {
		this.name = name;
	}

	public final Pattern getStartline() {
		return this.startline;
	}

	public final void setStartline(final Pattern startline) {
		this.startline = startline;
	}

	public final Character getDelimiter() {
		return this.delimiter;
	}

	public final void setDelimiter(final Character delimiter) {
		this.delimiter = delimiter;
	}

	public final Short getPositionDate() {
		return this.positionDate;
	}

	public final void setPositionDate(final Short positionDate) {
		this.positionDate = positionDate;
	}

	public final Short getPositionPartner() {
		return this.positionPartner;
	}

	public final void setPositionPartner(final Short positionPartner) {
		this.positionPartner = positionPartner;
	}

	public final Short getPositionAmount() {
		return this.positionAmount;
	}

	public final void setPositionAmount(final Short positionAmount) {
		this.positionAmount = positionAmount;
	}

	public final Short getPositionComment() {
		return this.positionComment;
	}

	public final void setPositionComment(final Short positionComment) {
		this.positionComment = positionComment;
	}

	public final DateTimeFormatter getFormatDate() {
		return this.formatDate;
	}

	public final void setFormatDate(final DateTimeFormatter formatDate) {
		this.formatDate = formatDate;
	}

	public final Character getFormatAmountDecimal() {
		return this.formatAmountDecimal;
	}

	public final void setFormatAmountDecimal(final Character formatAmountDecimal) {
		this.formatAmountDecimal = formatAmountDecimal;
	}

	public final Character getFormatAmountThousand() {
		return this.formatAmountThousand;
	}

	public final void setFormatAmountThousand(final Character formatAmountThousand) {
		this.formatAmountThousand = formatAmountThousand;
	}

	public final Short getPositionPartnerAlternative() {
		return this.positionPartnerAlternative;
	}

	public final void setPositionPartnerAlternative(final Short positionPartnerAlternative) {
		this.positionPartnerAlternative = positionPartnerAlternative;
	}

	public final Short getPositionPartnerAlternativePositionKey() {
		return this.positionPartnerAlternativePositionKey;
	}

	public final void setPositionPartnerAlternativePositionKey(final Short positionPartnerAlternativePositionKey) {
		this.positionPartnerAlternativePositionKey = positionPartnerAlternativePositionKey;
	}

	public final Pattern getPositionPartnerAlternativeKeyword() {
		return this.positionPartnerAlternativeKeyword;
	}

	public final void setPositionPartnerAlternativeKeyword(final Pattern positionPartnerAlternativeKeyword) {
		this.positionPartnerAlternativeKeyword = positionPartnerAlternativeKeyword;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((this.delimiter == null) ? 0 : this.delimiter.hashCode());
		result = prime * result + ((this.formatAmountDecimal == null) ? 0 : this.formatAmountDecimal.hashCode());
		result = prime * result + ((this.formatAmountThousand == null) ? 0 : this.formatAmountThousand.hashCode());
		result = prime * result + ((this.formatDate == null) ? 0 : this.formatDate.hashCode());
		result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
		result = prime * result + ((this.positionAmount == null) ? 0 : this.positionAmount.hashCode());
		result = prime * result + ((this.positionComment == null) ? 0 : this.positionComment.hashCode());
		result = prime * result + ((this.positionDate == null) ? 0 : this.positionDate.hashCode());
		result = prime * result + ((this.positionPartner == null) ? 0 : this.positionPartner.hashCode());
		result = prime * result
				+ ((this.positionPartnerAlternative == null) ? 0 : this.positionPartnerAlternative.hashCode());
		result = prime * result + ((this.positionPartnerAlternativeKeyword == null) ? 0
				: this.positionPartnerAlternativeKeyword.hashCode());
		result = prime * result + ((this.positionPartnerAlternativePositionKey == null) ? 0
				: this.positionPartnerAlternativePositionKey.hashCode());
		result = prime * result + ((this.startline == null) ? 0 : this.startline.hashCode());
		result = prime * result + ((this.type == null) ? 0 : this.type.hashCode());
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
		final CompareDataFormat other = (CompareDataFormat) obj;
		if (this.delimiter == null) {
			if (other.delimiter != null) {
				return false;
			}
		} else if (!this.delimiter.equals(other.delimiter)) {
			return false;
		}
		if (this.formatAmountDecimal == null) {
			if (other.formatAmountDecimal != null) {
				return false;
			}
		} else if (!this.formatAmountDecimal.equals(other.formatAmountDecimal)) {
			return false;
		}
		if (this.formatAmountThousand == null) {
			if (other.formatAmountThousand != null) {
				return false;
			}
		} else if (!this.formatAmountThousand.equals(other.formatAmountThousand)) {
			return false;
		}
		if (this.formatDate == null) {
			if (other.formatDate != null) {
				return false;
			}
		} else if (!this.formatDate.equals(other.formatDate)) {
			return false;
		}
		if (this.name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!this.name.equals(other.name)) {
			return false;
		}
		if (this.positionAmount == null) {
			if (other.positionAmount != null) {
				return false;
			}
		} else if (!this.positionAmount.equals(other.positionAmount)) {
			return false;
		}
		if (this.positionComment == null) {
			if (other.positionComment != null) {
				return false;
			}
		} else if (!this.positionComment.equals(other.positionComment)) {
			return false;
		}
		if (this.positionDate == null) {
			if (other.positionDate != null) {
				return false;
			}
		} else if (!this.positionDate.equals(other.positionDate)) {
			return false;
		}
		if (this.positionPartner == null) {
			if (other.positionPartner != null) {
				return false;
			}
		} else if (!this.positionPartner.equals(other.positionPartner)) {
			return false;
		}
		if (this.positionPartnerAlternative == null) {
			if (other.positionPartnerAlternative != null) {
				return false;
			}
		} else if (!this.positionPartnerAlternative.equals(other.positionPartnerAlternative)) {
			return false;
		}
		if (this.positionPartnerAlternativeKeyword == null) {
			if (other.positionPartnerAlternativeKeyword != null) {
				return false;
			}
		} else if (!this.positionPartnerAlternativeKeyword.equals(other.positionPartnerAlternativeKeyword)) {
			return false;
		}
		if (this.positionPartnerAlternativePositionKey == null) {
			if (other.positionPartnerAlternativePositionKey != null) {
				return false;
			}
		} else if (!this.positionPartnerAlternativePositionKey.equals(other.positionPartnerAlternativePositionKey)) {
			return false;
		}
		if (this.startline == null) {
			if (other.startline != null) {
				return false;
			}
		} else if (!this.startline.equals(other.startline)) {
			return false;
		}
		if (this.type != other.type) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("CompareDataFormat [type=");
		builder.append(this.type);
		builder.append(", name=");
		builder.append(this.name);
		builder.append(", startline=");
		builder.append(this.startline);
		builder.append(", delimiter=");
		builder.append(this.delimiter);
		builder.append(", positionDate=");
		builder.append(this.positionDate);
		builder.append(", positionPartner=");
		builder.append(this.positionPartner);
		builder.append(", positionAmount=");
		builder.append(this.positionAmount);
		builder.append(", positionComment=");
		builder.append(this.positionComment);
		builder.append(", formatDate=");
		builder.append(this.formatDate);
		builder.append(", formatAmountDecimal=");
		builder.append(this.formatAmountDecimal);
		builder.append(", formatAmountThousand=");
		builder.append(this.formatAmountThousand);
		builder.append(", positionPartnerAlternative=");
		builder.append(this.positionPartnerAlternative);
		builder.append(", positionPartnerAlternativePositionKey=");
		builder.append(this.positionPartnerAlternativePositionKey);
		builder.append(", positionPartnerAlternativeKeyword=");
		builder.append(this.positionPartnerAlternativeKeyword);
		builder.append("]");
		return builder.toString();
	}

}
