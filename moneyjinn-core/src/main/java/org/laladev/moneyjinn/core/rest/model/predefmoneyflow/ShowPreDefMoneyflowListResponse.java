
package org.laladev.moneyjinn.core.rest.model.predefmoneyflow;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

//
//Copyright (c) 2015 Oliver Lehmann <lehmann@ans-netz.de>
//All rights reserved.
//
//Redistribution and use in source and binary forms, with or without
//modification, are permitted provided that the following conditions
//are met:
//1. Redistributions of source code must retain the above copyright
//notice, this list of conditions and the following disclaimer
//2. Redistributions in binary form must reproduce the above copyright
//notice, this list of conditions and the following disclaimer in the
//documentation and/or other materials provided with the distribution.
//
//THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS'' AND
//ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
//IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
//ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE LIABLE
//FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
//DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
//OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
//HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
//LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
//OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
//SUCH DAMAGE.
//

import java.util.List;
import java.util.Objects;
import org.laladev.moneyjinn.core.rest.model.AbstractResponse;
import org.laladev.moneyjinn.core.rest.model.transport.PreDefMoneyflowTransport;

@XmlRootElement(name = "showPreDefMoneyflowListResponse")
public class ShowPreDefMoneyflowListResponse extends AbstractResponse {
  @XmlElement(name = "preDefMoneyflowTransport")
  private List<PreDefMoneyflowTransport> preDefMoneyflowTransports;

  public List<PreDefMoneyflowTransport> getPreDefMoneyflowTransports() {
    return this.preDefMoneyflowTransports;
  }

  public void setPreDefMoneyflowTransports(
      final List<PreDefMoneyflowTransport> preDefMoneyflowTransports) {
    this.preDefMoneyflowTransports = preDefMoneyflowTransports;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + Objects.hash(this.preDefMoneyflowTransports);
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
    final ShowPreDefMoneyflowListResponse other = (ShowPreDefMoneyflowListResponse) obj;
    return Objects.equals(this.preDefMoneyflowTransports, other.preDefMoneyflowTransports);
  }

  @Override
  public String toString() {
    return "ShowPreDefMoneyflowListResponse [preDefMoneyflowTransports="
        + this.preDefMoneyflowTransports + "]";
  }

}
