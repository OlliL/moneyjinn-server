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

package org.laladev.moneyjinn.model.moneyflow;

import java.util.Arrays;
import org.laladev.moneyjinn.model.AbstractEntity;
import org.laladev.moneyjinn.model.access.Group;
import org.laladev.moneyjinn.model.access.User;

public class ImportedMoneyflowReceipt extends AbstractEntity<ImportedMoneyflowReceiptID> {
  private static final long serialVersionUID = 1L;
  private User user;
  private Group access;
  private byte[] receipt;
  private String filename;
  private String mediaType;

  public final User getUser() {
    return this.user;
  }

  public final void setUser(final User user) {
    this.user = user;
  }

  public final Group getAccess() {
    return this.access;
  }

  public final void setAccess(final Group access) {
    this.access = access;
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
    int result = super.hashCode();
    result = prime * result + ((this.access == null) ? 0 : this.access.hashCode());
    result = prime * result + ((this.filename == null) ? 0 : this.filename.hashCode());
    result = prime * result + ((this.mediaType == null) ? 0 : this.mediaType.hashCode());
    result = prime * result + Arrays.hashCode(this.receipt);
    result = prime * result + ((this.user == null) ? 0 : this.user.hashCode());
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
    final ImportedMoneyflowReceipt other = (ImportedMoneyflowReceipt) obj;
    if (this.access == null) {
      if (other.access != null) {
        return false;
      }
    } else if (!this.access.equals(other.access)) {
      return false;
    }
    if (this.filename == null) {
      if (other.filename != null) {
        return false;
      }
    } else if (!this.filename.equals(other.filename)) {
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
    if (this.user == null) {
      if (other.user != null) {
        return false;
      }
    } else if (!this.user.equals(other.user)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    final StringBuilder builder = new StringBuilder();
    builder.append("ImportedMoneyflowReceipt [user=");
    builder.append(this.user);
    builder.append(", access=");
    builder.append(this.access);
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
