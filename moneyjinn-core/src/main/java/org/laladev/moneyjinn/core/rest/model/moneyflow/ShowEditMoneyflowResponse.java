//
// Copyright (c) 2015-2021 Oliver Lehmann <lehmann@ans-netz.de>
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

package org.laladev.moneyjinn.core.rest.model.moneyflow;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Objects;
import org.laladev.moneyjinn.core.rest.model.transport.MoneyflowSplitEntryTransport;
import org.laladev.moneyjinn.core.rest.model.transport.MoneyflowTransport;

@XmlRootElement(name = "showEditMoneyflowResponse")
public class ShowEditMoneyflowResponse {
  @XmlElement(name = "moneyflowSplitEntryTransport")
  private List<MoneyflowSplitEntryTransport> moneyflowSplitEntryTransports;
  private MoneyflowTransport moneyflowTransport;
  private boolean hasReceipt;

  public List<MoneyflowSplitEntryTransport> getMoneyflowSplitEntryTransports() {
    return this.moneyflowSplitEntryTransports;
  }

  public void setMoneyflowSplitEntryTransports(
      final List<MoneyflowSplitEntryTransport> moneyflowSplitEntryTransports) {
    this.moneyflowSplitEntryTransports = moneyflowSplitEntryTransports;
  }

  public MoneyflowTransport getMoneyflowTransport() {
    return this.moneyflowTransport;
  }

  public void setMoneyflowTransport(final MoneyflowTransport moneyflowTransport) {
    this.moneyflowTransport = moneyflowTransport;
  }

  public boolean isHasReceipt() {
    return this.hasReceipt;
  }

  public void setHasReceipt(final boolean hasReceipt) {
    this.hasReceipt = hasReceipt;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.hasReceipt, this.moneyflowSplitEntryTransports,
        this.moneyflowTransport);
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
    final ShowEditMoneyflowResponse other = (ShowEditMoneyflowResponse) obj;
    return this.hasReceipt == other.hasReceipt
        && Objects.equals(this.moneyflowSplitEntryTransports, other.moneyflowSplitEntryTransports)
        && Objects.equals(this.moneyflowTransport, other.moneyflowTransport);
  }

  @Override
  public String toString() {
    return "ShowEditMoneyflowResponse [moneyflowSplitEntryTransports="
        + this.moneyflowSplitEntryTransports + ", moneyflowTransport=" + this.moneyflowTransport
        + ", hasReceipt=" + this.hasReceipt + "]";
  }

}
