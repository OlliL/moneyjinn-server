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

package org.laladev.moneyjinn.businesslogic.service.api;

import java.time.LocalDate;
import java.util.List;

import org.laladev.moneyjinn.businesslogic.model.access.UserID;
import org.laladev.moneyjinn.businesslogic.model.capitalsource.CapitalsourceID;
import org.laladev.moneyjinn.businesslogic.model.comparedata.CompareDataFormat;
import org.laladev.moneyjinn.businesslogic.model.comparedata.CompareDataFormatID;
import org.laladev.moneyjinn.businesslogic.model.comparedata.CompareDataResult;

/**
 * <p>
 * CompareDataService is the Core Service handling everything around an {@link CompareDataResult}
 * and {@link CompareDataFormat}.
 * </p>
 *
 * <p>
 * CompareDataService is the Core Service handling operations around an {@link CompareDataResult}
 * and {@link CompareDataFormat}. It mainly processes an input file and compares it with the data
 * stored in the database to validate it for correctness. How to check it is defined in
 * {@link CompareDataFormat} and the check results are given back with a {@link CompareDataResult}.
 * </p>
 * <p>
 * The main datasource is the Table <code>cmp_data_formats</code>.
 * </p>
 *
 * @author Oliver Lehmann
 * @since 0.0.1
 *
 */
public interface ICompareDataService {

	public CompareDataFormat getCompareDataFormatById(CompareDataFormatID compareDataFormatId);

	public List<CompareDataFormat> getAllCompareDataFormats();

	public CompareDataResult compareData(UserID userId, CompareDataFormatID compareDataFormatId,
			CapitalsourceID capitalsourceId, LocalDate startDate, LocalDate endDate, String fileContents);
}