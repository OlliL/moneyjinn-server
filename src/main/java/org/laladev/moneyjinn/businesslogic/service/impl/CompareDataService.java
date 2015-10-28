//
// Copyright (c) 2015 Oliver Lehmann <oliver@laladev.org>
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

package org.laladev.moneyjinn.businesslogic.service.impl;

import java.time.LocalDate;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.laladev.moneyjinn.businesslogic.dao.CompareDataFormatDao;
import org.laladev.moneyjinn.businesslogic.model.access.UserID;
import org.laladev.moneyjinn.businesslogic.model.capitalsource.CapitalsourceID;
import org.laladev.moneyjinn.businesslogic.model.comparedata.CompareDataFormat;
import org.laladev.moneyjinn.businesslogic.model.comparedata.CompareDataFormatID;
import org.laladev.moneyjinn.businesslogic.model.comparedata.CompareDataResult;
import org.laladev.moneyjinn.businesslogic.service.api.ICompareDataService;

@Named
public class CompareDataService extends AbstractService implements ICompareDataService {
	@Inject
	private CompareDataFormatDao compareDataFormatDao;

	@Override
	protected void addBeanMapper() {
		// no Mapper needed
	}

	@Override
	public CompareDataFormat getCompareDataFormatById(final CompareDataFormatID compareDataFormatId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CompareDataFormat> getAllCompareDataFormats() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CompareDataResult compareData(final UserID userId, final CompareDataFormatID compareDataFormatId,
			final CapitalsourceID capitalsourceId, final LocalDate startDate, final LocalDate endDate,
			final String fileContents) {
		// TODO Auto-generated method stub
		return null;
	}

}
