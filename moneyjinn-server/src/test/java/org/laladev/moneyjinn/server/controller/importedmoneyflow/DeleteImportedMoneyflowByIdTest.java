package org.laladev.moneyjinn.server.controller.importedmoneyflow;

import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.capitalsource.CapitalsourceID;
import org.laladev.moneyjinn.model.moneyflow.ImportedMoneyflow;
import org.laladev.moneyjinn.model.moneyflow.ImportedMoneyflowStatus;
import org.laladev.moneyjinn.server.builder.CapitalsourceTransportBuilder;
import org.laladev.moneyjinn.server.builder.ImportedMoneyflowTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractWebUserControllerTest;
import org.laladev.moneyjinn.server.controller.api.ImportedMoneyflowControllerApi;
import org.laladev.moneyjinn.service.api.IImportedMoneyflowService;

import java.util.List;

class DeleteImportedMoneyflowByIdTest extends AbstractWebUserControllerTest {
    @Inject
    private IImportedMoneyflowService importedMoneyflowService;

    @Override
    protected void loadMethod() {
        super.getMock(ImportedMoneyflowControllerApi.class).deleteImportedMoneyflowById(null);
    }

    @Test
    void test_standardRequest_emptyResponse() throws Exception {
        final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
        final List<CapitalsourceID> capitalsourceIds =
                List.of(new CapitalsourceID(CapitalsourceTransportBuilder.CAPITALSOURCE1_ID));
        List<ImportedMoneyflow> importedMoneyflows = this.importedMoneyflowService
                .getAllImportedMoneyflowsByCapitalsourceIds(userId, capitalsourceIds, null);
        Assertions.assertNotNull(importedMoneyflows);
        final int sizeBeforeDelete = importedMoneyflows.size();
        importedMoneyflows = this.importedMoneyflowService.getAllImportedMoneyflowsByCapitalsourceIds(userId,
                capitalsourceIds, ImportedMoneyflowStatus.CREATED);
        Assertions.assertNotNull(importedMoneyflows);
        final int sizeBeforeDeleteInStateCreated = importedMoneyflows.size();

        super.callUsecaseExpect204WithUriVariables(ImportedMoneyflowTransportBuilder.IMPORTED_MONEYFLOW1_ID);

        importedMoneyflows = this.importedMoneyflowService.getAllImportedMoneyflowsByCapitalsourceIds(userId,
                capitalsourceIds, ImportedMoneyflowStatus.CREATED);
        Assertions.assertNotNull(importedMoneyflows);
        Assertions.assertEquals(sizeBeforeDeleteInStateCreated - 1, importedMoneyflows.size());
        // No delete happend - it is only marked as "ignored"
        importedMoneyflows = this.importedMoneyflowService.getAllImportedMoneyflowsByCapitalsourceIds(userId,
                capitalsourceIds, null);
        Assertions.assertNotNull(importedMoneyflows);
        Assertions.assertEquals(sizeBeforeDelete, importedMoneyflows.size());
    }

    @Override
    protected void callUsecaseExpect403ForThisUsecase() throws Exception {
        super.callUsecaseExpect403WithUriVariables(ImportedMoneyflowTransportBuilder.IMPORTED_MONEYFLOW1_ID);
    }

    @Override
    protected void callUsecaseEmptyDatabase() throws Exception {
        super.callUsecaseExpect204WithUriVariables(ImportedMoneyflowTransportBuilder.IMPORTED_MONEYFLOW1_ID);
    }
}