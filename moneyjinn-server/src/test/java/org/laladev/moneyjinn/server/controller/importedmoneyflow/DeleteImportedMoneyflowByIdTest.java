
package org.laladev.moneyjinn.server.controller.importedmoneyflow;

import jakarta.inject.Inject;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.capitalsource.CapitalsourceID;
import org.laladev.moneyjinn.model.moneyflow.ImportedMoneyflow;
import org.laladev.moneyjinn.model.moneyflow.ImportedMoneyflowStatus;
import org.laladev.moneyjinn.server.builder.CapitalsourceTransportBuilder;
import org.laladev.moneyjinn.server.builder.ImportedMoneyflowTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.laladev.moneyjinn.server.controller.api.ImportedMoneyflowControllerApi;
import org.laladev.moneyjinn.service.api.IImportedMoneyflowService;
import org.springframework.test.context.jdbc.Sql;

class DeleteImportedMoneyflowByIdTest extends AbstractControllerTest {
  @Inject
  private IImportedMoneyflowService importedMoneyflowService;

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
    super.getMock(ImportedMoneyflowControllerApi.class).deleteImportedMoneyflowById(null);
  }

  @Test
   void test_standardRequest_emptyResponse() throws Exception {
    final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
    final List<CapitalsourceID> capitalsourceIds = Arrays
        .asList(new CapitalsourceID(CapitalsourceTransportBuilder.CAPITALSOURCE1_ID));
    List<ImportedMoneyflow> importedMoneyflows = this.importedMoneyflowService
        .getAllImportedMoneyflowsByCapitalsourceIds(userId, capitalsourceIds, null);
    Assertions.assertNotNull(importedMoneyflows);
    final int sizeBeforeDelete = importedMoneyflows.size();
    importedMoneyflows = this.importedMoneyflowService.getAllImportedMoneyflowsByCapitalsourceIds(
        userId, capitalsourceIds, ImportedMoneyflowStatus.CREATED);
    Assertions.assertNotNull(importedMoneyflows);
    final int sizeBeforeDeleteInStateCreated = importedMoneyflows.size();

    super.callUsecaseExpect204WithUriVariables(
        ImportedMoneyflowTransportBuilder.IMPORTED_MONEYFLOW1_ID);

    importedMoneyflows = this.importedMoneyflowService.getAllImportedMoneyflowsByCapitalsourceIds(
        userId, capitalsourceIds, ImportedMoneyflowStatus.CREATED);
    Assertions.assertNotNull(importedMoneyflows);
    Assertions.assertEquals(sizeBeforeDeleteInStateCreated - 1, importedMoneyflows.size());
    // No delete happend - it is only marked as "ignored"
    importedMoneyflows = this.importedMoneyflowService
        .getAllImportedMoneyflowsByCapitalsourceIds(userId, capitalsourceIds, null);
    Assertions.assertNotNull(importedMoneyflows);
    Assertions.assertEquals(sizeBeforeDelete, importedMoneyflows.size());
  }

  @Test
   void test_AuthorizationRequired_Error() throws Exception {
    this.userName = null;
    this.userPassword = null;

    super.callUsecaseExpect403WithUriVariables(
        ImportedMoneyflowTransportBuilder.IMPORTED_MONEYFLOW1_ID);
  }

  @Test
  @Sql("classpath:h2defaults.sql")
  void test_emptyDatabase_noException() throws Exception {
    this.userName = UserTransportBuilder.ADMIN_NAME;
    this.userPassword = UserTransportBuilder.ADMIN_PASSWORD;

    super.callUsecaseExpect204WithUriVariables(
        ImportedMoneyflowTransportBuilder.IMPORTED_MONEYFLOW1_ID);
  }
}