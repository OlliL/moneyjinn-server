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
import org.laladev.moneyjinn.businesslogic.model.comparedata.CompareDataDataset;
import org.laladev.moneyjinn.businesslogic.model.comparedata.CompareDataFormat;
import org.laladev.moneyjinn.businesslogic.model.comparedata.CompareDataFormatID;
import org.laladev.moneyjinn.businesslogic.model.comparedata.CompareDataResult;
import org.laladev.moneyjinn.businesslogic.model.moneyflow.ImportedMoneyflow;
import org.laladev.moneyjinn.businesslogic.model.moneyflow.Moneyflow;

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

	/**
	 * Returns the {@link CompareDataFormat} specified by the given {@link CompareDataFormatID}.
	 *
	 * @param compareDataFormatId
	 * @return
	 */
	public CompareDataFormat getCompareDataFormatById(CompareDataFormatID compareDataFormatId);

	/**
	 * Returns all existing {@link CompareDataFormat}s.
	 *
	 * @return
	 */
	public List<CompareDataFormat> getAllCompareDataFormats();

	/**
	 * Parses the given fileContens based on the also given {@link CompareDataFormatID} and tries to
	 * match the extracted {@link CompareDataDataset}s to stored {@link Moneyflow}s during the
	 * specified timeframe. The response object contains the comparison results.
	 *
	 * @param userId
	 * @param compareDataFormatId
	 * @param capitalsourceId
	 * @param startDate
	 * @param endDate
	 * @param fileContents
	 * @return
	 */
	public CompareDataResult compareDataFile(UserID userId, CompareDataFormatID compareDataFormatId,
			CapitalsourceID capitalsourceId, LocalDate startDate, LocalDate endDate, String fileContents);

	/**
	 * Loads all {@link ImportedMoneyflow}s and tries to match them to stored {@link Moneyflow}s
	 * during the specified timeframe. The response object contains the comparison results.
	 *
	 * @param userId
	 * @param capitalsourceId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public CompareDataResult compareDataImport(UserID userId, CapitalsourceID capitalsourceId, LocalDate startDate,
			LocalDate endDate);
}