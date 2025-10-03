//Copyright (c) 2015-2025 Oliver Lehmann <lehmann@ans-netz.de>
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

package org.laladev.moneyjinn.server.controller.impl;

import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import org.laladev.moneyjinn.converter.javatypes.BooleanToIntegerMapper;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.capitalsource.CapitalsourceID;
import org.laladev.moneyjinn.model.comparedata.CompareDataFormat;
import org.laladev.moneyjinn.model.comparedata.CompareDataFormatID;
import org.laladev.moneyjinn.model.comparedata.CompareDataResult;
import org.laladev.moneyjinn.model.setting.ClientCompareDataSelectedCapitalsource;
import org.laladev.moneyjinn.model.setting.ClientCompareDataSelectedFormat;
import org.laladev.moneyjinn.model.setting.ClientCompareDataSelectedSourceIsFile;
import org.laladev.moneyjinn.server.controller.api.CompareDataControllerApi;
import org.laladev.moneyjinn.server.controller.mapper.CompareDataFormatTransportMapper;
import org.laladev.moneyjinn.server.controller.mapper.CompareDataResponseMapper;
import org.laladev.moneyjinn.server.model.CompareDataRequest;
import org.laladev.moneyjinn.server.model.CompareDataResponse;
import org.laladev.moneyjinn.server.model.ShowCompareDataFormResponse;
import org.laladev.moneyjinn.service.api.ICompareDataService;
import org.laladev.moneyjinn.service.api.IImportedMoneyflowService;
import org.laladev.moneyjinn.service.api.ISettingService;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

@RestController
@Transactional(propagation = Propagation.REQUIRES_NEW)
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class CompareDataController extends AbstractController implements CompareDataControllerApi {
    private final ISettingService settingService;
    private final ICompareDataService compareDataService;
    private final IImportedMoneyflowService importedMoneyflowService;

    private final CompareDataResponseMapper compareDataResponseMapper;
    private final CompareDataFormatTransportMapper compareDataFormatTransportMapper;
    private final BooleanToIntegerMapper booleanToIntegerMapper;

    @Override
    public ResponseEntity<ShowCompareDataFormResponse> showCompareDataForm() {
        final UserID userId = super.getUserId();
        final ShowCompareDataFormResponse response = new ShowCompareDataFormResponse();
        final List<CompareDataFormat> compareDataFormats = this.compareDataService.getAllCompareDataFormats();

        if (compareDataFormats != null && !compareDataFormats.isEmpty()) {
            response.setCompareDataFormatTransports(this.compareDataFormatTransportMapper.mapAToB(compareDataFormats));

            this.settingService.getClientCompareDataSelectedFormat(userId).ifPresent(
                    s -> response.setSelectedDataFormat(s.getSetting().getId()));
            this.settingService.getClientCompareDataSelectedSourceIsFile(userId).ifPresent(
                    s -> response.setSelectedSourceIsFile(this.booleanToIntegerMapper.mapAToB(s.getSetting())));
            this.settingService.getClientCompareDataSelectedCapitalsource(userId).ifPresent(
                    s -> response.setSelectedCapitalsourceId(s.getSetting().getId()));

        }

        return ResponseEntity.ok(response);
    }

    @Override
    @SuppressWarnings("java:S2589")
    public ResponseEntity<CompareDataResponse> compareData(@RequestBody final CompareDataRequest request) {
        final UserID userId = super.getUserId();
        final LocalDate startDate = request.getStartDate();
        final LocalDate endDate = request.getEndDate();
        final boolean useImportedData = request.getUseImportedData() != null && request.getUseImportedData() == 1;

        CompareDataResult compareDataResult = null;
        if (request.getCapitalsourceId() != null && endDate != null && startDate != null) {
            final CapitalsourceID capitalsourceId = new CapitalsourceID(request.getCapitalsourceId());
            this.settingService.setClientCompareDataSelectedCapitalsource(userId,
                    new ClientCompareDataSelectedCapitalsource(capitalsourceId));

            if (useImportedData) {
                compareDataResult = this.compareDataImport(userId, capitalsourceId, startDate, endDate);
            } else if (request.getFileContents() != null && request.getFormatId() != null) {
                compareDataResult = this.compareDataFile(userId, capitalsourceId, startDate, endDate,
                        request.getFormatId(), request.getFileContents());
            }
        }

        return ResponseEntity.ok(this.compareDataResponseMapper.mapAToB(compareDataResult));
    }

    private CompareDataResult compareDataImport(final UserID userId, final CapitalsourceID capitalsourceId,
                                                final LocalDate startDate, final LocalDate endDate) {
        this.settingService.setClientCompareDataSelectedSourceIsFile(userId,
                new ClientCompareDataSelectedSourceIsFile(Boolean.TRUE));

        final var importedMoneyflows = this.importedMoneyflowService.getAllImportedMoneyflowsByCapitalsourceIds(userId,
                Collections.singletonList(capitalsourceId), startDate, endDate);
        return this.compareDataService.compareDataImport(userId, capitalsourceId, startDate, endDate,
                importedMoneyflows);
    }

    private CompareDataResult compareDataFile(final UserID userId, final CapitalsourceID capitalsourceId,
                                              final LocalDate startDate, final LocalDate endDate,
                                              final Long formatId, final String fileContents1) {
        final CompareDataFormatID compareDataFormatId = new CompareDataFormatID(formatId);
        final byte[] fileContentBytes = Base64.getMimeDecoder().decode(fileContents1);
        final String fileContents = new String(fileContentBytes, StandardCharsets.UTF_8);

        this.settingService.setClientCompareDataSelectedFormat(userId,
                new ClientCompareDataSelectedFormat(compareDataFormatId));
        this.settingService.setClientCompareDataSelectedSourceIsFile(userId,
                new ClientCompareDataSelectedSourceIsFile(Boolean.FALSE));

        return this.compareDataService.compareDataFile(userId, compareDataFormatId, capitalsourceId, startDate, endDate,
                fileContents);
    }
}
