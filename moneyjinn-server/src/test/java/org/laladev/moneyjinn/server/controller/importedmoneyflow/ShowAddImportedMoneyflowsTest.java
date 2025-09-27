package org.laladev.moneyjinn.server.controller.importedmoneyflow;

import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.moneyflow.ImportedMoneyflowID;
import org.laladev.moneyjinn.server.builder.ImportedMoneyflowTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractWebUserControllerTest;
import org.laladev.moneyjinn.server.controller.api.ImportedMoneyflowControllerApi;
import org.laladev.moneyjinn.server.model.ImportedMoneyflowTransport;
import org.laladev.moneyjinn.server.model.ShowAddImportedMoneyflowsResponse;
import org.laladev.moneyjinn.service.api.IImportedMoneyflowService;

import java.util.ArrayList;
import java.util.List;

class ShowAddImportedMoneyflowsTest extends AbstractWebUserControllerTest {
    @Inject
    private IImportedMoneyflowService importedMoneyflowService;

    @Override
    protected void loadMethod() {
        super.getMock(ImportedMoneyflowControllerApi.class).showAddImportedMoneyflows();
    }

    @Test
    void test_standardRequest_Successfull() throws Exception {
        final ShowAddImportedMoneyflowsResponse expected = new ShowAddImportedMoneyflowsResponse();
        final List<ImportedMoneyflowTransport> importedMoneyflowTransports = new ArrayList<>();
        importedMoneyflowTransports.add(new ImportedMoneyflowTransportBuilder().forImportedMoneyflow1().build());
        importedMoneyflowTransports.add(new ImportedMoneyflowTransportBuilder().forImportedMoneyflow2().build());
        expected.setImportedMoneyflowTransports(importedMoneyflowTransports);

        final ShowAddImportedMoneyflowsResponse actual = super.callUsecaseExpect200(
                ShowAddImportedMoneyflowsResponse.class);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void test_noImportedData_emptyResponse() throws Exception {
        final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
        this.importedMoneyflowService.deleteImportedMoneyflowById(userId,
                new ImportedMoneyflowID(ImportedMoneyflowTransportBuilder.IMPORTED_MONEYFLOW1_ID));
        this.importedMoneyflowService.deleteImportedMoneyflowById(userId,
                new ImportedMoneyflowID(ImportedMoneyflowTransportBuilder.IMPORTED_MONEYFLOW2_ID));
        final ShowAddImportedMoneyflowsResponse expected = new ShowAddImportedMoneyflowsResponse();

        final ShowAddImportedMoneyflowsResponse actual = super.callUsecaseExpect200(
                ShowAddImportedMoneyflowsResponse.class);

        Assertions.assertEquals(expected, actual);
    }

    @Override
    protected void callUsecaseExpect403ForThisUsecase() throws Exception {
        super.callUsecaseExpect403();
    }

    @Override
    protected void callUsecaseEmptyDatabase() throws Exception {
        final ShowAddImportedMoneyflowsResponse expected = new ShowAddImportedMoneyflowsResponse();

        final ShowAddImportedMoneyflowsResponse actual = super.callUsecaseExpect200(
                ShowAddImportedMoneyflowsResponse.class);

        Assertions.assertEquals(expected, actual);
    }
}