//Copyright (c) 2015-2023 Oliver Lehmann <lehmann@ans-netz.de>
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

import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.model.BankAccount;
import org.laladev.moneyjinn.model.ImportedBalance;
import org.laladev.moneyjinn.model.capitalsource.Capitalsource;
import org.laladev.moneyjinn.model.capitalsource.CapitalsourceImport;
import org.laladev.moneyjinn.model.exception.BusinessException;
import org.laladev.moneyjinn.model.validation.ValidationResult;
import org.laladev.moneyjinn.server.controller.api.ImportedBalanceControllerApi;
import org.laladev.moneyjinn.server.controller.mapper.ImportedBalanceTransportMapper;
import org.laladev.moneyjinn.server.model.CreateImportedBalanceRequest;
import org.laladev.moneyjinn.server.model.ImportedBalanceTransport;
import org.laladev.moneyjinn.service.api.ICapitalsourceService;
import org.laladev.moneyjinn.service.api.IImportedBalanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Transactional(propagation = Propagation.REQUIRES_NEW)

@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class ImportedBalanceController extends AbstractController
    implements ImportedBalanceControllerApi {
  private final ICapitalsourceService capitalsourceService;
  private final IImportedBalanceService importedBalanceService;
  private final ImportedBalanceTransportMapper importedBalanceTransportMapper;

  @Override
  @PostConstruct
  protected void addBeanMapper() {
    super.registerBeanMapper(this.importedBalanceTransportMapper);
  }

  @Override
  public ResponseEntity<Void> createImportedBalance(
      @RequestBody final CreateImportedBalanceRequest request) {
    final ImportedBalanceTransport importedBalanceTransport = request.getImportedBalanceTransport();
    final ImportedBalance importedBalance = super.map(importedBalanceTransport,
        ImportedBalance.class);
    final BankAccount bankAccount = new BankAccount(
        importedBalanceTransport.getAccountNumberCapitalsource(),
        importedBalanceTransport.getBankCodeCapitalsource());
    final LocalDateTime now = importedBalance.getDate();
    final Capitalsource capitalsource = this.capitalsourceService.getCapitalsourceByAccount(null,
        bankAccount, now.toLocalDate());
    if (capitalsource != null) {
      if (capitalsource.getImportAllowed() == CapitalsourceImport.NOT_ALLOWED) {
        throw new BusinessException("Import of this capitalsource is not allowed!",
            ErrorCode.CAPITALSOURCE_IMPORT_NOT_ALLOWED);
      }
      importedBalance.setCapitalsource(capitalsource);
      final ValidationResult validationResult = this.importedBalanceService
          .validateImportedBalance(importedBalance);

      this.throwValidationExceptionIfInvalid(validationResult);

      this.importedBalanceService.upsertImportedBalance(importedBalance);

      return ResponseEntity.noContent().build();
    } else {
      throw new BusinessException("No matching capitalsource found!",
          ErrorCode.CAPITALSOURCE_NOT_FOUND);
    }
  }
}
