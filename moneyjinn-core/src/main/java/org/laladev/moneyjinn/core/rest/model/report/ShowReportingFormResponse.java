//
//Copyright (c) 2015-2021 Oliver Lehmann <lehmann@ans-netz.de>
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

package org.laladev.moneyjinn.core.rest.model.report;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.sql.Date;
import java.util.List;
import java.util.Objects;
import org.laladev.moneyjinn.core.rest.model.AbstractResponse;

@XmlRootElement(name = "showReportingFormResponse")
public class ShowReportingFormResponse extends AbstractResponse {
  @XmlElement(name = "postingAccountIdsNo")
  private List<Long> postingAccountIds;
  private Date minDate;
  private Date maxDate;

  public List<Long> getPostingAccountIds() {
    return this.postingAccountIds;
  }

  public void setPostingAccountIds(final List<Long> postingAccountIds) {
    this.postingAccountIds = postingAccountIds;
  }

  public Date getMinDate() {
    return this.minDate;
  }

  public void setMinDate(final Date minDate) {
    this.minDate = minDate;
  }

  public Date getMaxDate() {
    return this.maxDate;
  }

  public void setMaxDate(final Date maxDate) {
    this.maxDate = maxDate;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + Objects.hash(this.maxDate, this.minDate, this.postingAccountIds);
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
    final ShowReportingFormResponse other = (ShowReportingFormResponse) obj;
    return Objects.equals(this.maxDate, other.maxDate)
        && Objects.equals(this.minDate, other.minDate)
        && Objects.equals(this.postingAccountIds, other.postingAccountIds);
  }

  @Override
  public String toString() {
    return "ShowReportingFormResponse [postingAccountIds=" + this.postingAccountIds + ", minDate="
        + this.minDate + ", maxDate=" + this.maxDate + "]";
  }
}
