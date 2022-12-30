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

package org.laladev.moneyjinn.model.etf;

import java.util.Objects;
import org.laladev.moneyjinn.model.AbstractEntity;

public class Etf extends AbstractEntity<EtfIsin> {
  private static final long serialVersionUID = 1L;
  private String name;
  private String wkn;
  private String ticker;
  private String chartUrl;

  public final String getName() {
    return this.name;
  }

  public final void setName(final String name) {
    this.name = name;
  }

  public final String getWkn() {
    return this.wkn;
  }

  public final void setWkn(final String wkn) {
    this.wkn = wkn;
  }

  public final String getTicker() {
    return this.ticker;
  }

  public final void setTicker(final String ticker) {
    this.ticker = ticker;
  }

  public final String getChartUrl() {
    return this.chartUrl;
  }

  public final void setChartUrl(final String chartUrl) {
    this.chartUrl = chartUrl;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + Objects.hash(this.chartUrl, this.name, this.ticker, this.wkn);
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
    final Etf other = (Etf) obj;
    return Objects.equals(this.chartUrl, other.chartUrl) && Objects.equals(this.name, other.name)
        && Objects.equals(this.ticker, other.ticker) && Objects.equals(this.wkn, other.wkn);
  }

  @Override
  public String toString() {
    return "Etf [name=" + this.name + ", wkn=" + this.wkn + ", ticker=" + this.ticker
        + ", chartUrl=" + this.chartUrl + "]";
  }

}
