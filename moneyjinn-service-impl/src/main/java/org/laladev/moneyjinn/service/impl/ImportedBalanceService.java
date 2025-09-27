//
// Copyright (c) 2015-2025 Oliver Lehmann <lehmann@ans-netz.de>
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

package org.laladev.moneyjinn.service.impl;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.laladev.moneyjinn.model.ImportedBalance;
import org.laladev.moneyjinn.model.access.User;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.capitalsource.CapitalsourceID;
import org.laladev.moneyjinn.model.validation.ValidationResult;
import org.laladev.moneyjinn.service.api.IAccessRelationService;
import org.laladev.moneyjinn.service.api.ICapitalsourceService;
import org.laladev.moneyjinn.service.api.IImportedBalanceService;
import org.laladev.moneyjinn.service.api.IUserService;
import org.laladev.moneyjinn.service.dao.ImportedBalanceDao;
import org.laladev.moneyjinn.service.dao.data.ImportedBalanceData;
import org.laladev.moneyjinn.service.dao.data.mapper.ImportedBalanceDataMapper;

import java.util.Collections;
import java.util.List;

@Named
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class ImportedBalanceService extends AbstractService implements IImportedBalanceService {
    private final ImportedBalanceDao importedBalanceDao;
    private final IUserService userService;
    private final ICapitalsourceService capitalsourceService;
    private final IAccessRelationService accessRelationService;
    private final ImportedBalanceDataMapper importedBalanceDataMapper;

    private ImportedBalance mapImportedBalanceData(final UserID userId, final ImportedBalanceData importedBalanceData) {
        if (importedBalanceData != null) {
            final ImportedBalance importedBalance = this.importedBalanceDataMapper.mapBToA(importedBalanceData);
            importedBalance.setUser(new User(userId));

            this.userService.enrichEntity(importedBalance);
            this.accessRelationService.enrichEntity(importedBalance, importedBalance.getDate().toLocalDate());
            this.capitalsourceService.enrichEntity(importedBalance);

            return importedBalance;
        }
        return null;
    }

    private List<ImportedBalance> mapImportedBalanceDataList(final UserID userId,
                                                             final List<ImportedBalanceData> importedBalanceDataList) {
        return importedBalanceDataList.stream().map(element -> this.mapImportedBalanceData(userId, element)).toList();
    }

    @Override
    public ValidationResult validateImportedBalance(final ImportedBalance importedBalance) {
        return new ValidationResult();
    }

    @Override
    public List<ImportedBalance> getAllImportedBalancesByCapitalsourceIds(@NonNull final UserID userId,
                                                                          @NonNull final List<CapitalsourceID> capitalsourceIds) {

        if (capitalsourceIds.isEmpty()) {
            return Collections.emptyList();
        }

        final List<Long> capitalsourceIdLongs = capitalsourceIds.stream().map(CapitalsourceID::getId).toList();
        final List<ImportedBalanceData> importedBalanceDataList = this.importedBalanceDao
                .getAllImportedBalancesByCapitalsourceIds(capitalsourceIdLongs);
        return this.mapImportedBalanceDataList(userId, importedBalanceDataList);
    }

    @Override
    public void upsertImportedBalance(@NonNull final ImportedBalance importedBalance) {
        final ImportedBalanceData importedBalanceData = this.importedBalanceDataMapper.mapAToB(importedBalance);
        this.importedBalanceDao.upsertImportedBalance(importedBalanceData);
    }
}
