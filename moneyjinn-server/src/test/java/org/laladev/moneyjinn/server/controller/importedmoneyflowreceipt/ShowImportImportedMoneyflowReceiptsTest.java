package org.laladev.moneyjinn.server.controller.importedmoneyflowreceipt;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.server.builder.ImportedMoneyflowReceiptTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractWebUserControllerTest;
import org.laladev.moneyjinn.server.controller.api.ImportedMoneyflowReceiptControllerApi;
import org.laladev.moneyjinn.server.model.ImportedMoneyflowReceiptTransport;
import org.laladev.moneyjinn.server.model.ShowImportImportedMoneyflowReceiptsResponse;

import java.util.ArrayList;
import java.util.Collections;

class ShowImportImportedMoneyflowReceiptsTest extends AbstractWebUserControllerTest {
    @Override
    protected void loadMethod() {
        super.getMock(ImportedMoneyflowReceiptControllerApi.class).showImportImportedMoneyflowReceipts();
    }

    @Test
    void test_standardRequest_emptyResponse() throws Exception {
        final ShowImportImportedMoneyflowReceiptsResponse expected = new ShowImportImportedMoneyflowReceiptsResponse();
        final ArrayList<ImportedMoneyflowReceiptTransport> transporter = new ArrayList<>();
        transporter.add(new ImportedMoneyflowReceiptTransportBuilder().forReceipt1().build());
        transporter.add(new ImportedMoneyflowReceiptTransportBuilder().forReceipt2().build());
        expected.setImportedMoneyflowReceiptTransports(transporter);

        final ShowImportImportedMoneyflowReceiptsResponse actual = super.callUsecaseExpect200(
                ShowImportImportedMoneyflowReceiptsResponse.class);

        Assertions.assertEquals(expected, actual);
    }

    @Override
    protected void callUsecaseExpect403ForThisUsecase() throws Exception {
        super.callUsecaseExpect403();
    }

    @Override
    protected void callUsecaseEmptyDatabase() throws Exception {
        final ShowImportImportedMoneyflowReceiptsResponse expected = new ShowImportImportedMoneyflowReceiptsResponse();
        expected.setImportedMoneyflowReceiptTransports(Collections.emptyList());

        final ShowImportImportedMoneyflowReceiptsResponse actual = super.callUsecaseExpect200(
                ShowImportImportedMoneyflowReceiptsResponse.class);

        Assertions.assertEquals(expected, actual);
    }
}