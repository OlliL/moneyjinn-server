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
import org.laladev.moneyjinn.model.comparedata.*;
import org.laladev.moneyjinn.model.moneyflow.ImportedMoneyflow;
import org.laladev.moneyjinn.model.setting.ClientCompareDataSelectedCapitalsource;
import org.laladev.moneyjinn.model.setting.ClientCompareDataSelectedFormat;
import org.laladev.moneyjinn.model.setting.ClientCompareDataSelectedSourceIsFile;
import org.laladev.moneyjinn.server.controller.api.CompareDataControllerApi;
import org.laladev.moneyjinn.server.controller.mapper.CompareDataDatasetTransportMapper;
import org.laladev.moneyjinn.server.controller.mapper.CompareDataFormatTransportMapper;
import org.laladev.moneyjinn.server.controller.mapper.MoneyflowTransportMapper;
import org.laladev.moneyjinn.server.model.*;
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
import java.util.ArrayList;
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

    private final CompareDataDatasetTransportMapper compareDataDatasetTransportMapper;
    private final CompareDataFormatTransportMapper compareDataFormatTransportMapper;
    private final MoneyflowTransportMapper moneyflowTransportMapper;
    private final BooleanToIntegerMapper booleanToIntegerMapper;

    @Override
    public ResponseEntity<ShowCompareDataFormResponse> showCompareDataForm() {
        final UserID userId = super.getUserId();
        final ShowCompareDataFormResponse response = new ShowCompareDataFormResponse();
        final List<CompareDataFormat> compareDataFormats = this.compareDataService.getAllCompareDataFormats();
        if (compareDataFormats != null && !compareDataFormats.isEmpty()) {
            final List<CompareDataFormatTransport> responseCompareDataFormats = this.compareDataFormatTransportMapper
                    .mapAToB(compareDataFormats);
            response.setCompareDataFormatTransports(responseCompareDataFormats);

            this.settingService.getClientCompareDataSelectedFormat(userId)
                    .ifPresent(s -> response.setSelectedDataFormat(s.getSetting().getId()));
            this.settingService.getClientCompareDataSelectedSourceIsFile(userId).ifPresent(
                    s -> response.setSelectedSourceIsFile(this.booleanToIntegerMapper.mapAToB(s.getSetting())));
            this.settingService.getClientCompareDataSelectedCapitalsource(userId)
                    .ifPresent(s -> response.setSelectedCapitalsourceId(s.getSetting().getId()));

        }
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<CompareDataResponse> compareData(@RequestBody final CompareDataRequest request) {
        final UserID userId = super.getUserId();
        final CompareDataResponse response = new CompareDataResponse();
        CompareDataResult compareDataResult = null;
        if (request.getCapitalsourceId() != null && request.getEndDate() != null && request.getStartDate() != null) {
            final CapitalsourceID capitalsourceId = new CapitalsourceID(request.getCapitalsourceId());
            final LocalDate startDate = request.getStartDate();
            final LocalDate endDate = request.getEndDate();
            final boolean useImportedData = request.getUseImportedData() != null && request.getUseImportedData() == 1;
            if (!useImportedData && request.getFileContents() != null && request.getFormatId() != null) {
                final CompareDataFormatID compareDataFormatId = new CompareDataFormatID(request.getFormatId());
                final byte[] fileContentBytes = Base64.getMimeDecoder().decode(request.getFileContents());
                final String fileContents = new String(fileContentBytes, StandardCharsets.UTF_8);
                compareDataResult = this.compareDataService.compareDataFile(userId, compareDataFormatId,
                        capitalsourceId, startDate, endDate, fileContents);
                final ClientCompareDataSelectedFormat settingFormat = new ClientCompareDataSelectedFormat(
                        compareDataFormatId);
                this.settingService.setClientCompareDataSelectedFormat(userId, settingFormat);
                this.settingService.setClientCompareDataSelectedSourceIsFile(userId,
                        new ClientCompareDataSelectedSourceIsFile(Boolean.FALSE));
            } else if (useImportedData) {
                final List<ImportedMoneyflow> importedMoneyflows = this.importedMoneyflowService
                        .getAllImportedMoneyflowsByCapitalsourceIds(userId, Collections.singletonList(capitalsourceId),
                                startDate, endDate);
                compareDataResult = this.compareDataService.compareDataImport(userId, capitalsourceId, startDate,
                        endDate, importedMoneyflows);
                this.settingService.setClientCompareDataSelectedSourceIsFile(userId,
                        new ClientCompareDataSelectedSourceIsFile(Boolean.TRUE));
            }
            if (compareDataResult != null) {
                final List<CompareDataNotInDatabase> compareDataNotInDatabaseList = compareDataResult
                        .getCompareDataNotInDatabase();
                final List<CompareDataMatching> compareDataMatchingList = compareDataResult.getCompareDataMatching();
                final List<CompareDataWrongCapitalsource> compareDataWrongCapitalsourceList = compareDataResult
                        .getCompareDataWrongCapitalsource();
                final List<CompareDataNotInFile> compareDataNotInFileList = compareDataResult.getCompareDataNotInFile();
                if (compareDataNotInDatabaseList != null && !compareDataNotInDatabaseList.isEmpty()) {
                    final List<CompareDataNotInDatabaseTransport> transports = new ArrayList<>();
                    for (final CompareDataNotInDatabase compareDataNotInDatabase : compareDataNotInDatabaseList) {
                        final CompareDataNotInDatabaseTransport compareDataNotInDatabaseTransport = new CompareDataNotInDatabaseTransport();
                        compareDataNotInDatabaseTransport.setCompareDataDatasetTransport(
                                this.compareDataDatasetTransportMapper
                                        .mapAToB(compareDataNotInDatabase.getCompareDataDataset()));
                        transports.add(compareDataNotInDatabaseTransport);
                    }
                    response.setCompareDataNotInDatabaseTransports(transports);
                }
                if (compareDataMatchingList != null && !compareDataMatchingList.isEmpty()) {
                    final List<CompareDataMatchingTransport> transports = new ArrayList<>();
                    for (final CompareDataMatching compareDataMatching : compareDataMatchingList) {
                        final CompareDataMatchingTransport compareDataMatchingTransport = new CompareDataMatchingTransport();
                        compareDataMatchingTransport
                                .setCompareDataDatasetTransport(this.compareDataDatasetTransportMapper
                                        .mapAToB(compareDataMatching.getCompareDataDataset()));
                        compareDataMatchingTransport.setMoneyflowTransport(
                                this.moneyflowTransportMapper.mapAToB(compareDataMatching.getMoneyflow()));
                        transports.add(compareDataMatchingTransport);
                    }
                    response.setCompareDataMatchingTransports(transports);

                }
                if (compareDataWrongCapitalsourceList != null && !compareDataWrongCapitalsourceList.isEmpty()) {
                    final List<CompareDataWrongCapitalsourceTransport> transports = new ArrayList<>();
                    for (final CompareDataWrongCapitalsource compareDataWrongCapitalsource : compareDataWrongCapitalsourceList) {
                        final CompareDataWrongCapitalsourceTransport compareDataWrongCapitalsourceTransport = new CompareDataWrongCapitalsourceTransport();
                        compareDataWrongCapitalsourceTransport.setCompareDataDatasetTransport(
                                this.compareDataDatasetTransportMapper
                                        .mapAToB(compareDataWrongCapitalsource.getCompareDataDataset()));
                        compareDataWrongCapitalsourceTransport.setMoneyflowTransport(
                                this.moneyflowTransportMapper.mapAToB(compareDataWrongCapitalsource.getMoneyflow()));
                        transports.add(compareDataWrongCapitalsourceTransport);
                    }
                    response.setCompareDataWrongCapitalsourceTransports(transports);
                }
                if (compareDataNotInFileList != null && !compareDataNotInFileList.isEmpty()) {
                    final List<CompareDataNotInFileTransport> transports = new ArrayList<>();
                    for (final CompareDataNotInFile compareDataNotInFile : compareDataNotInFileList) {
                        final CompareDataNotInFileTransport compareDataNotInFileTransport = new CompareDataNotInFileTransport();
                        compareDataNotInFileTransport.setMoneyflowTransport(
                                this.moneyflowTransportMapper.mapAToB(compareDataNotInFile.getMoneyflow()));
                        transports.add(compareDataNotInFileTransport);
                    }
                    response.setCompareDataNotInFileTransports(transports);
                }
            }
            final ClientCompareDataSelectedCapitalsource settingCapitalsource = new ClientCompareDataSelectedCapitalsource(
                    capitalsourceId);
            this.settingService.setClientCompareDataSelectedCapitalsource(userId, settingCapitalsource);
        }
        return ResponseEntity.ok(response);
    }
}
