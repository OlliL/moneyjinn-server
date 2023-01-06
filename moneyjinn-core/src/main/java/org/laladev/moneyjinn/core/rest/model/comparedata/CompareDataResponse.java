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

package org.laladev.moneyjinn.core.rest.model.comparedata;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.laladev.moneyjinn.core.rest.model.AbstractResponse;
import org.laladev.moneyjinn.core.rest.model.comparedata.transport.CompareDataMatchingTransport;
import org.laladev.moneyjinn.core.rest.model.comparedata.transport.CompareDataNotInDatabaseTransport;
import org.laladev.moneyjinn.core.rest.model.comparedata.transport.CompareDataNotInFileTransport;
import org.laladev.moneyjinn.core.rest.model.comparedata.transport.CompareDataWrongCapitalsourceTransport;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@XmlRootElement(name = "compareDataResponse")
public class CompareDataResponse extends AbstractResponse {
  @XmlElement(name = "compareDataMatchingTransport")
  private final List<CompareDataMatchingTransport> compareDataMatchingTransports = new ArrayList<>();
  @XmlElement(name = "compareDataWrongCapitalsourceTransport")
  private final List<CompareDataWrongCapitalsourceTransport> compareDataWrongCapitalsourceTransports = new ArrayList<>();
  @XmlElement(name = "compareDataNotInFileTransport")
  private final List<CompareDataNotInFileTransport> compareDataNotInFileTransports = new ArrayList<>();
  @XmlElement(name = "compareDataNotInDatabaseTransport")
  private final List<CompareDataNotInDatabaseTransport> compareDataNotInDatabaseTransports = new ArrayList<>();

  public final void addCompareDataMatchingTransport(
      final CompareDataMatchingTransport compareDataMatchingTransport) {
    this.compareDataMatchingTransports.add(compareDataMatchingTransport);
  }

  public final void addCompareDataWrongCapitalsourceTransport(
      final CompareDataWrongCapitalsourceTransport compareDataWrongCapitalsourceTransport) {
    this.compareDataWrongCapitalsourceTransports.add(compareDataWrongCapitalsourceTransport);
  }

  public final void addCompareDataNotInFileTransport(
      final CompareDataNotInFileTransport compareDataNotInFileTransport) {
    this.compareDataNotInFileTransports.add(compareDataNotInFileTransport);
  }

  public final void addCompareDataNotInDatabaseTransport(
      final CompareDataNotInDatabaseTransport compareDataNotInDatabaseTransport) {
    this.compareDataNotInDatabaseTransports.add(compareDataNotInDatabaseTransport);
  }
}
