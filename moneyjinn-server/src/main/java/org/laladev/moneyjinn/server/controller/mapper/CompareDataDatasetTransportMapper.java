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

import java.sql.Date;

import org.laladev.moneyjinn.core.mapper.IMapper;
import org.laladev.moneyjinn.core.rest.model.comparedata.transport.CompareDataDatasetTransport;
import org.laladev.moneyjinn.model.comparedata.CompareDataDataset;

public class CompareDataDatasetTransportMapper implements IMapper<CompareDataDataset, CompareDataDatasetTransport> {

	@Override
	public CompareDataDataset mapBToA(final CompareDataDatasetTransport compareDataDatasetTransport) {
		throw new UnsupportedOperationException("Mapping not supported!");
	}

	@Override
	public CompareDataDatasetTransport mapAToB(final CompareDataDataset compareDataDataset) {
		final CompareDataDatasetTransport compareDataDatasetTransport = new CompareDataDatasetTransport();

		compareDataDatasetTransport.setAmount(compareDataDataset.getAmount());
		compareDataDatasetTransport.setPartner(compareDataDataset.getPartner());
		compareDataDatasetTransport.setComment(compareDataDataset.getComment());

		if (compareDataDataset.getBookingDate() != null) {
			final Date bookingDate = Date.valueOf(compareDataDataset.getBookingDate());
			compareDataDatasetTransport.setBookingDate(bookingDate);
		}

		if (compareDataDataset.getInvoiceDate() != null) {
			final Date invoiceDate = Date.valueOf(compareDataDataset.getInvoiceDate());
			compareDataDatasetTransport.setInvoiceDate(invoiceDate);
		}

		return compareDataDatasetTransport;
	}
}
