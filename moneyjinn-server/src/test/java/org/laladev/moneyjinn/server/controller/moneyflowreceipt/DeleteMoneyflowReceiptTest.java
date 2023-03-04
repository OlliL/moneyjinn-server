
package org.laladev.moneyjinn.server.controller.moneyflowreceipt;

import jakarta.inject.Inject;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.moneyflow.MoneyflowID;
import org.laladev.moneyjinn.model.moneyflow.MoneyflowReceipt;
import org.laladev.moneyjinn.server.builder.MoneyflowTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.laladev.moneyjinn.server.controller.api.MoneyflowReceiptControllerApi;
import org.laladev.moneyjinn.service.api.IMoneyflowReceiptService;
import org.springframework.test.context.jdbc.Sql;

public class DeleteMoneyflowReceiptTest extends AbstractControllerTest {
  @Inject
  IMoneyflowReceiptService moneyflowReceiptService;

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
    return super.getMethodFromTestClassName(MoneyflowReceiptControllerApi.class, this.getClass());
  }

  @Test
  public void test_unknownMoneyflowId_NoContent() throws Exception {
    super.callUsecaseExpect204WithUriVariables(MoneyflowTransportBuilder.NON_EXISTING_ID);
  }

  @Test
  public void test_MoneyflowId1_SuccessfullNoContent() throws Exception {
    final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
    final MoneyflowID moneyflowId = new MoneyflowID(MoneyflowTransportBuilder.MONEYFLOW1_ID);

    MoneyflowReceipt receipt = this.moneyflowReceiptService.getMoneyflowReceipt(userId,
        moneyflowId);
    Assertions.assertNotNull(receipt);
    Assertions.assertEquals(receipt.getId().getId(), 1L);

    super.callUsecaseExpect204WithUriVariables(MoneyflowTransportBuilder.MONEYFLOW1_ID);

    receipt = this.moneyflowReceiptService.getMoneyflowReceipt(userId, moneyflowId);
    Assertions.assertNull(receipt);
  }

  @Test
  public void test_AuthorizationRequired_Error() throws Exception {
    this.userName = null;
    this.userPassword = null;

    super.callUsecaseExpect403WithUriVariables(MoneyflowTransportBuilder.MONEYFLOW1_ID);
  }

  @Test
  @Sql("classpath:h2defaults.sql")
  public void test_emptyDatabase_noException() throws Exception {
    this.userName = UserTransportBuilder.ADMIN_NAME;
    this.userPassword = UserTransportBuilder.ADMIN_PASSWORD;

    super.callUsecaseExpect204WithUriVariables(MoneyflowTransportBuilder.MONEYFLOW1_ID);
  }
}