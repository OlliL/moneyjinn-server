
package org.laladev.moneyjinn.server.controller.importedmoneyflowreceipt;

import java.util.ArrayList;
import java.util.Collections;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.server.builder.ImportedMoneyflowReceiptTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.laladev.moneyjinn.server.controller.api.ImportedMoneyflowReceiptControllerApi;
import org.laladev.moneyjinn.server.model.ImportedMoneyflowReceiptTransport;
import org.laladev.moneyjinn.server.model.ShowImportImportedMoneyflowReceiptsResponse;
import org.springframework.test.context.jdbc.Sql;

public class ShowImportImportedMoneyflowReceiptsTest extends AbstractControllerTest {
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
        .showImportImportedMoneyflowReceipts();
  }

  @Test
  public void test_standardRequest_emptyResponse() throws Exception {
    final ShowImportImportedMoneyflowReceiptsResponse expected = new ShowImportImportedMoneyflowReceiptsResponse();
    final ArrayList<ImportedMoneyflowReceiptTransport> transporter = new ArrayList<>();
    transporter.add(new ImportedMoneyflowReceiptTransportBuilder().forReceipt1().build());
    transporter.add(new ImportedMoneyflowReceiptTransportBuilder().forReceipt2().build());
    expected.setImportedMoneyflowReceiptTransports(transporter);

    final ShowImportImportedMoneyflowReceiptsResponse actual = super.callUsecaseExpect200(
        ShowImportImportedMoneyflowReceiptsResponse.class);

    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_AuthorizationRequired_Error() throws Exception {
    this.userName = null;
    this.userPassword = null;

    super.callUsecaseExpect403();
  }

  @Test
  @Sql("classpath:h2defaults.sql")
  public void test_emptyDatabase_noException() throws Exception {
    this.userName = UserTransportBuilder.ADMIN_NAME;
    this.userPassword = UserTransportBuilder.ADMIN_PASSWORD;

    final ShowImportImportedMoneyflowReceiptsResponse expected = new ShowImportImportedMoneyflowReceiptsResponse();
    expected.setImportedMoneyflowReceiptTransports(Collections.emptyList());

    final ShowImportImportedMoneyflowReceiptsResponse actual = super.callUsecaseExpect200(
        ShowImportImportedMoneyflowReceiptsResponse.class);

    Assertions.assertEquals(expected, actual);
  }
}