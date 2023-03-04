
package org.laladev.moneyjinn.server.controller.importedmoneyflowreceipt;

import jakarta.inject.Inject;
import java.lang.reflect.Method;
import java.util.Base64;
import java.util.Collections;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.model.access.GroupID;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.moneyflow.ImportedMoneyflowReceipt;
import org.laladev.moneyjinn.model.moneyflow.ImportedMoneyflowReceiptID;
import org.laladev.moneyjinn.server.builder.GroupTransportBuilder;
import org.laladev.moneyjinn.server.builder.ImportedMoneyflowReceiptTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.laladev.moneyjinn.server.controller.api.ImportedMoneyflowReceiptControllerApi;
import org.laladev.moneyjinn.server.model.CreateImportedMoneyflowReceiptsRequest;
import org.laladev.moneyjinn.server.model.ErrorResponse;
import org.laladev.moneyjinn.server.model.ImportedMoneyflowReceiptTransport;
import org.laladev.moneyjinn.server.model.ValidationResponse;
import org.laladev.moneyjinn.service.api.IImportedMoneyflowReceiptService;
import org.springframework.test.context.jdbc.Sql;

public class CreateImportedMoneyflowReceiptsTest extends AbstractControllerTest {
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
  protected Method getMethod() {
    return super.getMethodFromTestClassName(ImportedMoneyflowReceiptControllerApi.class,
        this.getClass());
  }

  private void test_supportedFile_CreatedAndEmptyResponse(
      final ImportedMoneyflowReceiptTransport transport, final Long userIdLong,
      final Long groupIdLong) throws Exception {
    final CreateImportedMoneyflowReceiptsRequest request = new CreateImportedMoneyflowReceiptsRequest();
    request.setImportedMoneyflowReceiptTransports(Collections.singletonList(transport));

    super.callUsecaseExpect204(request);

    final UserID userId = new UserID(userIdLong);
    final GroupID groupId = new GroupID(groupIdLong);
    final ImportedMoneyflowReceiptID importedMoneyflowReceiptId = new ImportedMoneyflowReceiptID(
        transport.getId());
    final ImportedMoneyflowReceipt receipt = this.importedMoneyflowReceiptService
        .getImportedMoneyflowReceiptById(userId, groupId, importedMoneyflowReceiptId);

    Assertions.assertNotNull(receipt);
    Assertions.assertEquals(transport.getFilename(), receipt.getFilename());
    Assertions.assertEquals(transport.getMediaType(), receipt.getMediaType());
    Assertions.assertEquals(transport.getReceipt(),
        Base64.getEncoder().encodeToString(receipt.getReceipt()));

  }

  @Test
  public void test_standardJpegRequest_emptyResponse() throws Exception {
    this.test_supportedFile_CreatedAndEmptyResponse(
        new ImportedMoneyflowReceiptTransportBuilder().forJpegReceipt().build(),
        UserTransportBuilder.USER1_ID, GroupTransportBuilder.GROUP1_ID);
  }

  @Test
  public void test_standardPdfRequest_emptyResponse() throws Exception {
    this.test_supportedFile_CreatedAndEmptyResponse(
        new ImportedMoneyflowReceiptTransportBuilder().forPdfReceipt().build(),
        UserTransportBuilder.USER1_ID, GroupTransportBuilder.GROUP1_ID);
  }

  @Test
  public void test_standardPngRequest_errorResponse() throws Exception {
    final CreateImportedMoneyflowReceiptsRequest request = new CreateImportedMoneyflowReceiptsRequest();
    request.setImportedMoneyflowReceiptTransports(Collections
        .singletonList(new ImportedMoneyflowReceiptTransportBuilder().forPngReceipt().build()));

    final ValidationResponse actual = super.callUsecaseExpect422(request, ValidationResponse.class);

    Assertions.assertEquals(ErrorCode.UNSUPPORTED_MEDIA_TYPE.getErrorCode(),
        actual.getValidationItemTransports().get(0).getError());
  }

  @Test
  public void test_invalidBase64Request_errorResponse() throws Exception {
    final CreateImportedMoneyflowReceiptsRequest request = new CreateImportedMoneyflowReceiptsRequest();
    final ImportedMoneyflowReceiptTransport transport = new ImportedMoneyflowReceiptTransportBuilder()
        .forPngReceipt().build();
    transport.setReceipt("-----");
    request.setImportedMoneyflowReceiptTransports(Collections.singletonList(transport));

    final ErrorResponse actual = super.callUsecaseExpect400(request, ErrorResponse.class);

    Assertions.assertEquals(ErrorCode.WRONG_FILE_FORMAT.getErrorCode(), actual.getCode());
  }

  @Test
  public void test_AuthorizationRequired_Error() throws Exception {
    this.userName = null;
    this.userPassword = null;

    super.callUsecaseExpect403(new CreateImportedMoneyflowReceiptsRequest());
  }

  @Test
  @Sql("classpath:h2defaults.sql")
  public void test_emptyDatabase_noException() throws Exception {
    this.userName = UserTransportBuilder.ADMIN_NAME;
    this.userPassword = UserTransportBuilder.ADMIN_PASSWORD;

    this.test_supportedFile_CreatedAndEmptyResponse(
        new ImportedMoneyflowReceiptTransportBuilder().forReceipt1().build(),
        UserTransportBuilder.ADMIN_ID, GroupTransportBuilder.ADMINGROUP_ID);
  }
}