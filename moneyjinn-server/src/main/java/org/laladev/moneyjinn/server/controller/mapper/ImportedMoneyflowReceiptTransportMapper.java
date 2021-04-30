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

package org.laladev.moneyjinn.server.controller.mapper;

import java.util.Base64;

import org.laladev.moneyjinn.core.mapper.IMapper;
import org.laladev.moneyjinn.core.rest.model.importedmoneyflowreceipt.transport.ImportedMoneyflowReceiptTransport;
import org.laladev.moneyjinn.model.moneyflow.ImportedMoneyflowReceipt;
import org.laladev.moneyjinn.model.moneyflow.ImportedMoneyflowReceiptID;

public class ImportedMoneyflowReceiptTransportMapper
		implements IMapper<ImportedMoneyflowReceipt, ImportedMoneyflowReceiptTransport> {

	@Override
	public ImportedMoneyflowReceipt mapBToA(final ImportedMoneyflowReceiptTransport importedMoneyflowReceiptTransport) {
		final ImportedMoneyflowReceipt importedMoneyflowReceipt = new ImportedMoneyflowReceipt();
		if (importedMoneyflowReceiptTransport.getId() != null) {
			importedMoneyflowReceipt.setId(new ImportedMoneyflowReceiptID(importedMoneyflowReceiptTransport.getId()));
		}

		importedMoneyflowReceipt.setFilename(importedMoneyflowReceiptTransport.getFilename());
		importedMoneyflowReceipt.setMediaType(importedMoneyflowReceiptTransport.getMediaType());
		importedMoneyflowReceipt.setReceipt(Base64.getDecoder().decode(importedMoneyflowReceiptTransport.getReceipt()));

		return importedMoneyflowReceipt;
	}

	@Override
	public ImportedMoneyflowReceiptTransport mapAToB(final ImportedMoneyflowReceipt importedMoneyflowReceipt) {
		final ImportedMoneyflowReceiptID importedMoneyflowReceiptId = importedMoneyflowReceipt.getId();
		final Long id = importedMoneyflowReceiptId == null ? null : importedMoneyflowReceiptId.getId();

		final ImportedMoneyflowReceiptTransport importedMoneyflowReceiptTransport = new ImportedMoneyflowReceiptTransport();
		importedMoneyflowReceiptTransport.setId(id);
		importedMoneyflowReceiptTransport.setFilename(importedMoneyflowReceipt.getFilename());
		importedMoneyflowReceiptTransport.setMediaType(importedMoneyflowReceipt.getMediaType());
		importedMoneyflowReceiptTransport
				.setReceipt(Base64.getEncoder().encodeToString(importedMoneyflowReceipt.getReceipt()));

		return importedMoneyflowReceiptTransport;

	}
}
