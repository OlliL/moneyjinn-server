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

package org.laladev.moneyjinn.core.rest.model.etf;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.laladev.moneyjinn.core.rest.model.etf.transport.EtfFlowTransport;
import org.laladev.moneyjinn.core.rest.model.etf.transport.EtfTransport;

@XmlRootElement(name = "listEtfFlowsResponse")
public class ListEtfFlowsResponse {
	@XmlElement(name = "etfTransport")
	private List<EtfTransport> etfTransports;
	@XmlElement(name = "etfFlowTransport")
	private List<EtfFlowTransport> etfFlowTransports;

	public final List<EtfTransport> getEtfTransports() {
		return this.etfTransports;
	}

	public final void setEtfTransports(final List<EtfTransport> etfTransports) {
		this.etfTransports = etfTransports;
	}

	public final List<EtfFlowTransport> getEtfFlowTransports() {
		return this.etfFlowTransports;
	}

	public final void setEtfFlowTransports(final List<EtfFlowTransport> etfFlowTransports) {
		this.etfFlowTransports = etfFlowTransports;
	}
}
