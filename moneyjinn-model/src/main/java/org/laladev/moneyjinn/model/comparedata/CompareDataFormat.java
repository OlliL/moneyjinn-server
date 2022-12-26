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

package org.laladev.moneyjinn.model.comparedata;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Pattern;
import org.laladev.moneyjinn.model.AbstractEntity;

public class CompareDataFormat extends AbstractEntity<CompareDataFormatID> {
  private static final long serialVersionUID = 1L;
  private CompareDataFormatType type;
  private String name;
  private List<String> startTrigger;
  private Character delimiter;
  private Short positionDate;
  private Short positionPartner;
  private Short positionAmount;
  private Short positionComment;
  private DateTimeFormatter formatDate;
  private Character formatAmountDecimal;
  private Character formatAmountThousand;
  private Short positionPartnerAlternative;
  private Short partnerAlternativeIndicatorPosition;
  private Pattern partnerAlternativeIndicator;
  private Short positionCreditDebitIndicator;
  private Pattern creditIndicator;

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

  public final List<String> getStartTrigger() {
    return this.startTrigger;
  }

  public final void setStartTrigger(final List<String> startTrigger) {
    this.startTrigger = startTrigger;
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

  public final Short getPartnerAlternativeIndicatorPosition() {
    return this.partnerAlternativeIndicatorPosition;
  }

  public final void setPartnerAlternativeIndicatorPosition(
      final Short partnerAlternativeIndicatorPosition) {
    this.partnerAlternativeIndicatorPosition = partnerAlternativeIndicatorPosition;
  }

  public final Pattern getPartnerAlternativeIndicator() {
    return this.partnerAlternativeIndicator;
  }

  public final void setPartnerAlternativeIndicator(final Pattern partnerAlternativeIndicator) {
    this.partnerAlternativeIndicator = partnerAlternativeIndicator;
  }

  public final Short getPositionCreditDebitIndicator() {
    return this.positionCreditDebitIndicator;
  }

  public final void setPositionCreditDebitIndicator(final Short positionCreditDebitIndicator) {
    this.positionCreditDebitIndicator = positionCreditDebitIndicator;
  }

  public final Pattern getCreditIndicator() {
    return this.creditIndicator;
  }

  public final void setCreditIndicator(final Pattern creditIndicator) {
    this.creditIndicator = creditIndicator;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result
        + ((this.creditIndicator == null) ? 0 : this.creditIndicator.hashCode());
    result = prime * result + ((this.delimiter == null) ? 0 : this.delimiter.hashCode());
    result = prime * result
        + ((this.formatAmountDecimal == null) ? 0 : this.formatAmountDecimal.hashCode());
    result = prime * result
        + ((this.formatAmountThousand == null) ? 0 : this.formatAmountThousand.hashCode());
    result = prime * result + ((this.formatDate == null) ? 0 : this.formatDate.hashCode());
    result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
    result = prime * result + ((this.partnerAlternativeIndicator == null) ? 0
        : this.partnerAlternativeIndicator.hashCode());
    result = prime * result + ((this.partnerAlternativeIndicatorPosition == null) ? 0
        : this.partnerAlternativeIndicatorPosition.hashCode());
    result = prime * result + ((this.positionAmount == null) ? 0 : this.positionAmount.hashCode());
    result = prime * result
        + ((this.positionComment == null) ? 0 : this.positionComment.hashCode());
    result = prime * result + ((this.positionCreditDebitIndicator == null) ? 0
        : this.positionCreditDebitIndicator.hashCode());
    result = prime * result + ((this.positionDate == null) ? 0 : this.positionDate.hashCode());
    result = prime * result
        + ((this.positionPartner == null) ? 0 : this.positionPartner.hashCode());
    result = prime * result + ((this.positionPartnerAlternative == null) ? 0
        : this.positionPartnerAlternative.hashCode());
    result = prime * result + ((this.startTrigger == null) ? 0 : this.startTrigger.hashCode());
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
    if (this.creditIndicator == null) {
      if (other.creditIndicator != null) {
        return false;
      }
    } else if (!this.creditIndicator.equals(other.creditIndicator)) {
      return false;
    }
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
    if (this.partnerAlternativeIndicator == null) {
      if (other.partnerAlternativeIndicator != null) {
        return false;
      }
    } else if (!this.partnerAlternativeIndicator.equals(other.partnerAlternativeIndicator)) {
      return false;
    }
    if (this.partnerAlternativeIndicatorPosition == null) {
      if (other.partnerAlternativeIndicatorPosition != null) {
        return false;
      }
    } else if (!this.partnerAlternativeIndicatorPosition
        .equals(other.partnerAlternativeIndicatorPosition)) {
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
    if (this.positionCreditDebitIndicator == null) {
      if (other.positionCreditDebitIndicator != null) {
        return false;
      }
    } else if (!this.positionCreditDebitIndicator.equals(other.positionCreditDebitIndicator)) {
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
    if (this.startTrigger == null) {
      if (other.startTrigger != null) {
        return false;
      }
    } else if (!this.startTrigger.equals(other.startTrigger)) {
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
    builder.append(", startTrigger=");
    builder.append(this.startTrigger);
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
    builder.append(", partnerAlternativeIndicatorPosition=");
    builder.append(this.partnerAlternativeIndicatorPosition);
    builder.append(", partnerAlternativeIndicator=");
    builder.append(this.partnerAlternativeIndicator);
    builder.append(", positionCreditDebitIndicator=");
    builder.append(this.positionCreditDebitIndicator);
    builder.append(", creditIndicator=");
    builder.append(this.creditIndicator);
    builder.append("]");
    return builder.toString();
  }
}