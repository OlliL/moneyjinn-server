
package org.laladev.moneyjinn.server.controller.importedmoneyflowreceipt;

import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.model.access.GroupID;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.moneyflow.ImportedMoneyflowReceipt;
import org.laladev.moneyjinn.model.moneyflow.ImportedMoneyflowReceiptID;
import org.laladev.moneyjinn.server.builder.GroupTransportBuilder;
import org.laladev.moneyjinn.server.builder.ImportedMoneyflowReceiptTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.laladev.moneyjinn.server.controller.api.ImportedMoneyflowReceiptControllerApi;
import org.laladev.moneyjinn.service.api.IImportedMoneyflowReceiptService;
import org.springframework.test.context.jdbc.Sql;

class DeleteImportedMoneyflowReceiptByIdTest extends AbstractControllerTest {
  @Inject
  IImportedMoneyflowReceiptService importedMoneyflowReceiptService;

  private String userName;
  private String userPassword;

  @BeforeEach
  public void setUp() {
    this.userName = UserTransportBuilder.USER1_NAME;
    this.userPassword = UserTransportBuilder.USER1_PASSWORD;
  }

  @Override
  protected String getUsername() {
    return this.userName;
  }

  @Override
  protected String getPassword() {
    return this.userPassword;
  }

  @Override
  protected void loadMethod() {
    super.getMock(ImportedMoneyflowReceiptControllerApi.class)
        .deleteImportedMoneyflowReceiptById(null);
  }

  @Test
   void test_standardRequest_emptyResponse() throws Exception {
    final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
    final GroupID groupId = new GroupID(GroupTransportBuilder.GROUP1_ID);
    final ImportedMoneyflowReceiptID receiptId = new ImportedMoneyflowReceiptID(
        ImportedMoneyflowReceiptTransportBuilder.RECEIPT_1ID);

    ImportedMoneyflowReceipt receipt = this.importedMoneyflowReceiptService
        .getImportedMoneyflowReceiptById(userId, groupId, receiptId);
    Assertions.assertNotNull(receipt);

    super.callUsecaseExpect204WithUriVariables(receiptId.getId());

    receipt = this.importedMoneyflowReceiptService.getImportedMoneyflowReceiptById(userId, groupId,
        receiptId);
    Assertions.assertNull(receipt);
  }

  @Test
   void test_DeleteNotExistingId_emptyResponse() throws Exception {
    super.callUsecaseExpect204WithUriVariables(ImportedMoneyflowReceiptTransportBuilder.NEXT_ID);
  }

  @Test
   void test_AuthorizationRequired_Error() throws Exception {
    this.userName = null;
    this.userPassword = null;

    super.callUsecaseExpect403WithUriVariables(ImportedMoneyflowReceiptTransportBuilder.NEXT_ID);
  }

  @Test
  @Sql("classpath:h2defaults.sql")
  void test_emptyDatabase_noException() throws Exception {
    this.userName = UserTransportBuilder.ADMIN_NAME;
    this.userPassword = UserTransportBuilder.ADMIN_PASSWORD;

    super.callUsecaseExpect204WithUriVariables(ImportedMoneyflowReceiptTransportBuilder.NEXT_ID);

  }
}