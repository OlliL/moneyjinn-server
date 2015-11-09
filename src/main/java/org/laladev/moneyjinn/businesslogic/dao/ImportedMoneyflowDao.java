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

package org.laladev.moneyjinn.businesslogic.dao;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.laladev.moneyjinn.businesslogic.dao.data.ImportedMoneyflowData;
import org.laladev.moneyjinn.businesslogic.dao.mapper.IImportedMoneyflowDaoMapper;

@Named
public class ImportedMoneyflowDao {
	@Inject
	IImportedMoneyflowDaoMapper mapper;

	public Integer countImportedMoneyflows(final Long userId, final List<Long> capitalsourceIds) {
		return this.mapper.countImportedMoneyflows(capitalsourceIds);
	}

	public List<ImportedMoneyflowData> getAllImportedMoneyflowsByCapitalsourceIds(final Long userId,
			final List<Long> capitalsourceIdLongs) {
		return this.mapper.getAllImportedMoneyflowsByCapitalsourceIds(capitalsourceIdLongs);
	}

	public void deleteImportedMoneyflowById(final Long userId, final Long importedMoneyflowId) {
		this.mapper.deleteImportedMoneyflowById(importedMoneyflowId);
	}

	public void createImportedMoneyflow(final ImportedMoneyflowData importedMoneyflowData) {
		this.mapper.createImportedMoneyflow(importedMoneyflowData);
	}

}
