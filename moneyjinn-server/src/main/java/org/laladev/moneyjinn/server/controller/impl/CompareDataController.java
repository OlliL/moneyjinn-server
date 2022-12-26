//Copyright (c) 2015-2017 Oliver Lehmann <lehmann@ans-netz.de>
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
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import java.util.function.Predicate;
import org.laladev.moneyjinn.core.rest.model.comparedata.CompareDataRequest;
import org.laladev.moneyjinn.core.rest.model.comparedata.CompareDataResponse;
import org.laladev.moneyjinn.core.rest.model.comparedata.ShowCompareDataFormResponse;
import org.laladev.moneyjinn.core.rest.model.comparedata.transport.CompareDataDatasetTransport;
import org.laladev.moneyjinn.core.rest.model.comparedata.transport.CompareDataFormatTransport;
import org.laladev.moneyjinn.core.rest.model.comparedata.transport.CompareDataMatchingTransport;
import org.laladev.moneyjinn.core.rest.model.comparedata.transport.CompareDataNotInDatabaseTransport;
import org.laladev.moneyjinn.core.rest.model.comparedata.transport.CompareDataNotInFileTransport;
import org.laladev.moneyjinn.core.rest.model.comparedata.transport.CompareDataWrongCapitalsourceTransport;
import org.laladev.moneyjinn.core.rest.model.transport.CapitalsourceTransport;
import org.laladev.moneyjinn.core.rest.model.transport.MoneyflowTransport;
import org.laladev.moneyjinn.model.access.Group;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.capitalsource.Capitalsource;
import org.laladev.moneyjinn.model.capitalsource.CapitalsourceID;
import org.laladev.moneyjinn.model.capitalsource.CapitalsourceState;
import org.laladev.moneyjinn.model.capitalsource.CapitalsourceType;
import org.laladev.moneyjinn.model.comparedata.CompareDataFormat;
import org.laladev.moneyjinn.model.comparedata.CompareDataFormatID;
import org.laladev.moneyjinn.model.comparedata.CompareDataMatching;
import org.laladev.moneyjinn.model.comparedata.CompareDataNotInDatabase;
import org.laladev.moneyjinn.model.comparedata.CompareDataNotInFile;
import org.laladev.moneyjinn.model.comparedata.CompareDataResult;
import org.laladev.moneyjinn.model.comparedata.CompareDataWrongCapitalsource;
import org.laladev.moneyjinn.model.setting.ClientCompareDataSelectedCapitalsource;
import org.laladev.moneyjinn.model.setting.ClientCompareDataSelectedFormat;
import org.laladev.moneyjinn.model.setting.ClientCompareDataSelectedSourceIsFile;
import org.laladev.moneyjinn.server.annotation.RequiresAuthorization;
import org.laladev.moneyjinn.server.controller.mapper.CapitalsourceTransportMapper;
import org.laladev.moneyjinn.server.controller.mapper.CompareDataDatasetTransportMapper;
import org.laladev.moneyjinn.server.controller.mapper.CompareDataFormatTransportMapper;
import org.laladev.moneyjinn.server.controller.mapper.MoneyflowTransportMapper;
import org.laladev.moneyjinn.service.api.IAccessRelationService;
import org.laladev.moneyjinn.service.api.ICapitalsourceService;
import org.laladev.moneyjinn.service.api.ICompareDataService;
import org.laladev.moneyjinn.service.api.ISettingService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Transactional(propagation = Propagation.REQUIRES_NEW)
@RequestMapping("/moneyflow/server/comparedata/")
public class CompareDataController extends AbstractController {
  @Inject
  private IAccessRelationService accessRelationService;
  @Inject
  private ISettingService settingService;
  @Inject
  private ICompareDataService compareDataService;
  @Inject
  private ICapitalsourceService capitalsourceService;

  @Override
  protected void addBeanMapper() {
    super.registerBeanMapper(new CompareDataFormatTransportMapper());
    super.registerBeanMapper(new CapitalsourceTransportMapper());
    super.registerBeanMapper(new MoneyflowTransportMapper());
    super.registerBeanMapper(new CompareDataDatasetTransportMapper());
  }

  @RequestMapping(value = "showCompareDataForm", method = { RequestMethod.GET })
  @RequiresAuthorization
  public ShowCompareDataFormResponse showCompareDataForm() {
    final UserID userId = super.getUserId();
    final LocalDate today = LocalDate.now();
    final ShowCompareDataFormResponse response = new ShowCompareDataFormResponse();
    final List<CompareDataFormat> compareDataFormats = this.compareDataService
        .getAllCompareDataFormats();
    if (compareDataFormats != null && !compareDataFormats.isEmpty()) {
      final List<CompareDataFormatTransport> responseCompareDataFormats = super.mapList(
          compareDataFormats, CompareDataFormatTransport.class);
      response.setCompareDataFormatTransports(responseCompareDataFormats);
      final ClientCompareDataSelectedFormat selectedDataFormat = this.settingService
          .getClientCompareDataSelectedFormat(userId);
      if (selectedDataFormat != null) {
        response.setSelectedDataFormat(selectedDataFormat.getSetting().getId());
      }
      final ClientCompareDataSelectedSourceIsFile selectedSourceIsFile = this.settingService
          .getClientCompareDataSelectedSourceIsFile(userId);
      if (selectedSourceIsFile != null && selectedSourceIsFile.getSetting().equals(Boolean.TRUE)) {
        response.setSelectedSourceIsFile((short) 1);
      }
      final List<Capitalsource> capitalsources = this.capitalsourceService
          .getGroupCapitalsourcesByDateRange(userId, today, today);
      if (capitalsources != null && !capitalsources.isEmpty()) {
        final Predicate<Capitalsource> capitalsourcePredicate = c -> c
            .getType() != CapitalsourceType.CURRENT_ASSET
            || c.getState() != CapitalsourceState.NON_CACHE;
        capitalsources.removeIf(capitalsourcePredicate);
        response.setCapitalsourceTransports(
            super.mapList(capitalsources, CapitalsourceTransport.class));
        final ClientCompareDataSelectedCapitalsource selectedCapitalsource = this.settingService
            .getClientCompareDataSelectedCapitalsource(userId);
        if (selectedCapitalsource != null) {
          response.setSelectedCapitalsourceId(selectedCapitalsource.getSetting().getId());
        }
      }
    }
    return response;
  }

  @RequestMapping(value = "compareData", method = { RequestMethod.PUT })
  @RequiresAuthorization
  public CompareDataResponse compareData(@RequestBody final CompareDataRequest request) {
    final UserID userId = super.getUserId();
    final CompareDataResponse response = new CompareDataResponse();
    CompareDataResult compareDataResult = null;
    if (request.getCapitalsourceId() != null && request.getEndDate() != null
        && request.getStartDate() != null) {
      final CapitalsourceID capitalsourceId = new CapitalsourceID(request.getCapitalsourceId());
      final LocalDate startDate = request.getStartDate().toLocalDate();
      final LocalDate endDate = request.getEndDate().toLocalDate();
      final boolean useImportedData = request.getUseImportedData() != null
          && request.getUseImportedData().compareTo((short) 1) == 0;
      if (!useImportedData && request.getFileContents() != null && request.getFormatId() != null) {
        final CompareDataFormatID compareDataFormatId = new CompareDataFormatID(
            request.getFormatId());
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
        compareDataResult = this.compareDataService.compareDataImport(userId, capitalsourceId,
            startDate, endDate);
        this.settingService.setClientCompareDataSelectedSourceIsFile(userId,
            new ClientCompareDataSelectedSourceIsFile(Boolean.TRUE));
      }
      if (compareDataResult != null) {
        final List<CompareDataNotInDatabase> compareDataNotInDatabaseList = compareDataResult
            .getCompareDataNotInDatabase();
        final List<CompareDataMatching> compareDataMatchingList = compareDataResult
            .getCompareDataMatching();
        final List<CompareDataWrongCapitalsource> compareDataWrongCapitalsourceList = compareDataResult
            .getCompareDataWrongCapitalsource();
        final List<CompareDataNotInFile> compareDataNotInFileList = compareDataResult
            .getCompareDataNotInFile();
        if (compareDataNotInDatabaseList != null && !compareDataNotInDatabaseList.isEmpty()) {
          for (final CompareDataNotInDatabase compareDataNotInDatabase : compareDataNotInDatabaseList) {
            final CompareDataNotInDatabaseTransport compareDataNotInDatabaseTransport = new CompareDataNotInDatabaseTransport();
            compareDataNotInDatabaseTransport.setCompareDataDatasetTransport(
                super.map(compareDataNotInDatabase.getCompareDataDataset(),
                    CompareDataDatasetTransport.class));
            response.addCompareDataNotInDatabaseTransport(compareDataNotInDatabaseTransport);
          }
        }
        if (compareDataMatchingList != null && !compareDataMatchingList.isEmpty()) {
          for (final CompareDataMatching compareDataMatching : compareDataMatchingList) {
            final CompareDataMatchingTransport compareDataMatchingTransport = new CompareDataMatchingTransport();
            compareDataMatchingTransport.setCompareDataDatasetTransport(super.map(
                compareDataMatching.getCompareDataDataset(), CompareDataDatasetTransport.class));
            compareDataMatchingTransport.setMoneyflowTransport(
                super.map(compareDataMatching.getMoneyflow(), MoneyflowTransport.class));
            response.addCompareDataMatchingTransport(compareDataMatchingTransport);
          }
        }
        if (compareDataWrongCapitalsourceList != null
            && !compareDataWrongCapitalsourceList.isEmpty()) {
          for (final CompareDataWrongCapitalsource compareDataWrongCapitalsource : compareDataWrongCapitalsourceList) {
            final CompareDataWrongCapitalsourceTransport compareDataWrongCapitalsourceTransport = new CompareDataWrongCapitalsourceTransport();
            compareDataWrongCapitalsourceTransport.setCompareDataDatasetTransport(
                super.map(compareDataWrongCapitalsource.getCompareDataDataset(),
                    CompareDataDatasetTransport.class));
            compareDataWrongCapitalsourceTransport.setMoneyflowTransport(
                super.map(compareDataWrongCapitalsource.getMoneyflow(), MoneyflowTransport.class));
            response
                .addCompareDataWrongCapitalsourceTransport(compareDataWrongCapitalsourceTransport);
          }
        }
        if (compareDataNotInFileList != null && !compareDataNotInFileList.isEmpty()) {
          for (final CompareDataNotInFile compareDataNotInFile : compareDataNotInFileList) {
            final CompareDataNotInFileTransport compareDataNotInFileTransport = new CompareDataNotInFileTransport();
            compareDataNotInFileTransport.setMoneyflowTransport(
                super.map(compareDataNotInFile.getMoneyflow(), MoneyflowTransport.class));
            response.addCompareDataNotInFileTransport(compareDataNotInFileTransport);
          }
        }
      }
      final Group accessor = this.accessRelationService.getAccessor(userId);
      final Capitalsource capitalsource = this.capitalsourceService.getCapitalsourceById(userId,
          accessor.getId(), capitalsourceId);
      response.setCapitalsourceTransport(super.map(capitalsource, CapitalsourceTransport.class));
      final ClientCompareDataSelectedCapitalsource settingCapitalsource = new ClientCompareDataSelectedCapitalsource(
          capitalsourceId);
      this.settingService.setClientCompareDataSelectedCapitalsource(userId, settingCapitalsource);
    }
    return response;
  }
}
