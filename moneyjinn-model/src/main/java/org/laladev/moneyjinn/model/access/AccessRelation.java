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

package org.laladev.moneyjinn.model.access;

import java.time.LocalDate;
import org.laladev.moneyjinn.model.AbstractEntity;

public class AccessRelation extends AbstractEntity<AccessID> implements Cloneable {
  private static final long serialVersionUID = 1L;
  private AccessRelation parentAccessRelation;
  private LocalDate validFrom;
  private LocalDate validTil;

  public AccessRelation() {
    // Default Constructor because ID can be empty for newly created AccessRelations.
  }

  public AccessRelation(final AccessID id) {
    super.setId(id);
  }

  public AccessRelation(final AccessID id, final AccessRelation parentAccessRelation,
      final LocalDate validFrom, final LocalDate validTil) {
    super.setId(id);
    this.parentAccessRelation = parentAccessRelation;
    this.validFrom = validFrom;
    this.validTil = validTil;
  }

  public AccessRelation(final AccessID id, final AccessRelation parentAccessRelation) {
    super.setId(id);
    this.parentAccessRelation = parentAccessRelation;
  }

  public final AccessRelation getParentAccessRelation() {
    return this.parentAccessRelation;
  }

  public final void setParentAccessRelation(final AccessRelation parentAccessRelation) {
    this.parentAccessRelation = parentAccessRelation;
  }

  public final LocalDate getValidFrom() {
    return this.validFrom;
  }

  public final void setValidFrom(final LocalDate validFrom) {
    this.validFrom = validFrom;
  }

  public final LocalDate getValidTil() {
    return this.validTil;
  }

  public final void setValidTil(final LocalDate validTil) {
    this.validTil = validTil;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result
        + ((this.parentAccessRelation == null) ? 0 : this.parentAccessRelation.hashCode());
    result = prime * result + ((this.validFrom == null) ? 0 : this.validFrom.hashCode());
    result = prime * result + ((this.validTil == null) ? 0 : this.validTil.hashCode());
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
    final AccessRelation other = (AccessRelation) obj;
    if (this.parentAccessRelation == null) {
      if (other.parentAccessRelation != null) {
        return false;
      }
    } else if (!this.parentAccessRelation.equals(other.parentAccessRelation)) {
      return false;
    }
    if (this.validFrom == null) {
      if (other.validFrom != null) {
        return false;
      }
    } else if (!this.validFrom.equals(other.validFrom)) {
      return false;
    }
    if (this.validTil == null) {
      if (other.validTil != null) {
        return false;
      }
    } else if (!this.validTil.equals(other.validTil)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    final StringBuilder builder = new StringBuilder();
    builder.append("AccessRelation [parentAccessRelation=");
    builder.append(this.parentAccessRelation);
    builder.append(", validFrom=");
    builder.append(this.validFrom);
    builder.append(", validTil=");
    builder.append(this.validTil);
    builder.append(", getId()=");
    builder.append(this.getId());
    builder.append("]");
    return builder.toString();
  }

  @Override
  public AccessRelation clone() throws CloneNotSupportedException {
    final AccessRelation accessRelation = (AccessRelation) super.clone();
    accessRelation.setId(this.getId().clone());
    if (this.getParentAccessRelation() != null) {
      accessRelation.setParentAccessRelation(this.getParentAccessRelation().clone());
    }
    return accessRelation;
  }
}
