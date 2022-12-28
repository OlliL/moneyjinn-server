
package org.laladev.moneyjinn.server.controller.importedmoneyflow;

import jakarta.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.core.rest.model.importedmoneyflow.ShowAddImportedMoneyflowsResponse;
import org.laladev.moneyjinn.core.rest.model.transport.ImportedMoneyflowTransport;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.moneyflow.ImportedMoneyflowID;
import org.laladev.moneyjinn.server.builder.ImportedMoneyflowTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.laladev.moneyjinn.service.api.IImportedMoneyflowService;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.jdbc.Sql;

public class ShowAddImportedMoneyflowsTest extends AbstractControllerTest {
  @Inject
  private IImportedMoneyflowService importedMoneyflowService;
  private final HttpMethod method = HttpMethod.GET;
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

  @Test
  public void test_standardRequest_Successfull() throws Exception {
    final ShowAddImportedMoneyflowsResponse expected = new ShowAddImportedMoneyflowsResponse();
    final List<ImportedMoneyflowTransport> importedMoneyflowTransports = new ArrayList<>();
    importedMoneyflowTransports
        .add(new ImportedMoneyflowTransportBuilder().forImportedMoneyflow1().build());
    importedMoneyflowTransports
        .add(new ImportedMoneyflowTransportBuilder().forImportedMoneyflow2().build());
    expected.setImportedMoneyflowTransports(importedMoneyflowTransports);
    final ShowAddImportedMoneyflowsResponse actual = super.callUsecaseWithoutContent("",
        this.method, false, ShowAddImportedMoneyflowsResponse.class);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_noImportedData_emptyResponse() throws Exception {
    final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
    this.importedMoneyflowService.deleteImportedMoneyflowById(userId,
        new ImportedMoneyflowID(ImportedMoneyflowTransportBuilder.IMPORTED_MONEYFLOW1_ID));
    this.importedMoneyflowService.deleteImportedMoneyflowById(userId,
        new ImportedMoneyflowID(ImportedMoneyflowTransportBuilder.IMPORTED_MONEYFLOW2_ID));
    final ShowAddImportedMoneyflowsResponse expected = new ShowAddImportedMoneyflowsResponse();
    final ShowAddImportedMoneyflowsResponse actual = super.callUsecaseWithoutContent("",
        this.method, false, ShowAddImportedMoneyflowsResponse.class);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_AuthorizationRequired_Error() throws Exception {
    this.userName = null;
    this.userPassword = null;
    super.callUsecaseExpect403("111", this.method);
  }

  @Test
  @Sql("classpath:h2defaults.sql")
  public void test_emptyDatabase_noException() throws Exception {
    this.userName = UserTransportBuilder.ADMIN_NAME;
    this.userPassword = UserTransportBuilder.ADMIN_PASSWORD;
    final ShowAddImportedMoneyflowsResponse expected = new ShowAddImportedMoneyflowsResponse();
    final ShowAddImportedMoneyflowsResponse actual = super.callUsecaseWithoutContent("",
        this.method, false, ShowAddImportedMoneyflowsResponse.class);
    Assertions.assertEquals(expected, actual);
  }
}