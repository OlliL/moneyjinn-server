
package org.laladev.moneyjinn.server.controller.importedmoneyflowreceipt;

import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.core.rest.model.ErrorResponse;
import org.laladev.moneyjinn.model.access.GroupID;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.moneyflow.ImportedMoneyflowReceipt;
import org.laladev.moneyjinn.model.moneyflow.ImportedMoneyflowReceiptID;
import org.laladev.moneyjinn.model.moneyflow.MoneyflowID;
import org.laladev.moneyjinn.model.moneyflow.MoneyflowReceipt;
import org.laladev.moneyjinn.model.moneyflow.MoneyflowReceiptType;
import org.laladev.moneyjinn.server.builder.GroupTransportBuilder;
import org.laladev.moneyjinn.server.builder.ImportedMoneyflowReceiptTransportBuilder;
import org.laladev.moneyjinn.server.builder.MoneyflowTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.laladev.moneyjinn.service.api.IImportedMoneyflowReceiptService;
import org.laladev.moneyjinn.service.api.IMoneyflowReceiptService;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.jdbc.Sql;

public class ImportImportedMoneyflowReceiptTest extends AbstractControllerTest {
  @Inject
  IImportedMoneyflowReceiptService importedMoneyflowReceiptService;
  @Inject
  IMoneyflowReceiptService moneyflowReceiptService;

  private final HttpMethod method = HttpMethod.POST;
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
  protected String getUsecase() {
    return super.getUsecaseFromTestClassName(this.getClass());
  }

  private void test_import(final Long receiptIdLong, final MoneyflowReceiptType mediaType)
      throws Exception {
    final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
    final GroupID groupId = new GroupID(GroupTransportBuilder.GROUP1_ID);
    final ImportedMoneyflowReceiptID receiptId = new ImportedMoneyflowReceiptID(receiptIdLong);
    final MoneyflowID moneyflowId = new MoneyflowID(MoneyflowTransportBuilder.MONEYFLOW3_ID);

    ImportedMoneyflowReceipt receipt = this.importedMoneyflowReceiptService
        .getImportedMoneyflowReceiptById(userId, groupId, receiptId);
    Assertions.assertNotNull(receipt);

    super.callUsecaseWithoutContent("/" + receiptId.getId() + "/" + moneyflowId.getId(),
        this.method, true, Object.class);

    receipt = this.importedMoneyflowReceiptService.getImportedMoneyflowReceiptById(userId, groupId,
        receiptId);
    Assertions.assertNull(receipt);
    final MoneyflowReceipt moneyflowReceipt = this.moneyflowReceiptService
        .getMoneyflowReceipt(userId, moneyflowId);

    Assertions.assertNotNull(moneyflowReceipt);
    Assertions.assertEquals(mediaType, moneyflowReceipt.getMoneyflowReceiptType());

  }

  @Test
  public void test_standardJpegRequest_emptyResponse() throws Exception {
    this.test_import(ImportedMoneyflowReceiptTransportBuilder.RECEIPT_1ID,
        MoneyflowReceiptType.JPEG);
  }

  @Test
  public void test_standardPdfRequest_emptyResponse() throws Exception {
    this.test_import(ImportedMoneyflowReceiptTransportBuilder.RECEIPT_2ID,
        MoneyflowReceiptType.PDF);
  }

  @Test
  public void test_MoneyflowHasReceiptAlready_errorResponse() throws Exception {
    final ImportedMoneyflowReceiptID receiptId = new ImportedMoneyflowReceiptID(
        ImportedMoneyflowReceiptTransportBuilder.RECEIPT_1ID);
    final MoneyflowID moneyflowId = new MoneyflowID(MoneyflowTransportBuilder.MONEYFLOW1_ID);

    final ErrorResponse error = super.callUsecaseWithoutContent(
        "/" + receiptId.getId() + "/" + moneyflowId.getId(), this.method, false,
        ErrorResponse.class);

    Assertions.assertEquals(ErrorCode.RECEIPT_ALREADY_EXISTS.getErrorCode(), error.getCode());

  }

  @Test
  public void test_AuthorizationRequired_Error() throws Exception {
    this.userName = null;
    this.userPassword = null;
    super.callUsecaseExpect403("/1/1", this.method);
  }

  @Test
  @Sql("classpath:h2defaults.sql")
  public void test_emptyDatabase_noException() throws Exception {
    this.userName = UserTransportBuilder.ADMIN_NAME;
    this.userPassword = UserTransportBuilder.ADMIN_PASSWORD;

    super.callUsecaseWithoutContent("/1/1", this.method, true, Object.class);

  }
}