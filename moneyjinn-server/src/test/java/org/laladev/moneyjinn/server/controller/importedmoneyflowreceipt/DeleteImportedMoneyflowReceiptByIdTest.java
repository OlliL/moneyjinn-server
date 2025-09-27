package org.laladev.moneyjinn.server.controller.importedmoneyflowreceipt;

import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.model.access.GroupID;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.moneyflow.ImportedMoneyflowReceipt;
import org.laladev.moneyjinn.model.moneyflow.ImportedMoneyflowReceiptID;
import org.laladev.moneyjinn.server.builder.GroupTransportBuilder;
import org.laladev.moneyjinn.server.builder.ImportedMoneyflowReceiptTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractWebUserControllerTest;
import org.laladev.moneyjinn.server.controller.api.ImportedMoneyflowReceiptControllerApi;
import org.laladev.moneyjinn.service.api.IImportedMoneyflowReceiptService;

class DeleteImportedMoneyflowReceiptByIdTest extends AbstractWebUserControllerTest {
    @Inject
    private IImportedMoneyflowReceiptService importedMoneyflowReceiptService;

    @Override
    protected void loadMethod() {
        super.getMock(ImportedMoneyflowReceiptControllerApi.class).deleteImportedMoneyflowReceiptById(null);
    }

    @Test
    void test_standardRequest_emptyResponse() throws Exception {
        final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
        final GroupID groupId = new GroupID(GroupTransportBuilder.GROUP1_ID);
        final ImportedMoneyflowReceiptID receiptId = new ImportedMoneyflowReceiptID(
                ImportedMoneyflowReceiptTransportBuilder.RECEIPT_1ID);

        ImportedMoneyflowReceipt receipt = this.importedMoneyflowReceiptService.getImportedMoneyflowReceiptById(userId,
                groupId, receiptId);
        Assertions.assertNotNull(receipt);

        super.callUsecaseExpect204WithUriVariables(receiptId.getId());

        receipt = this.importedMoneyflowReceiptService.getImportedMoneyflowReceiptById(userId, groupId, receiptId);
        Assertions.assertNull(receipt);
    }

    @Test
    void test_DeleteNotExistingId_emptyResponse() throws Exception {
        super.callUsecaseExpect204WithUriVariables(ImportedMoneyflowReceiptTransportBuilder.NEXT_ID);
    }

    @Override
    protected void callUsecaseExpect403ForThisUsecase() throws Exception {
        super.callUsecaseExpect403WithUriVariables(ImportedMoneyflowReceiptTransportBuilder.NEXT_ID);
    }

    @Override
    protected void callUsecaseEmptyDatabase() throws Exception {
        super.callUsecaseExpect204WithUriVariables(ImportedMoneyflowReceiptTransportBuilder.NEXT_ID);
    }
}