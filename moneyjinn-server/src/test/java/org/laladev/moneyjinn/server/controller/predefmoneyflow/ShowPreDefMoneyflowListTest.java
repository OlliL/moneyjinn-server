
package org.laladev.moneyjinn.server.controller.predefmoneyflow;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.core.rest.model.predefmoneyflow.ShowPreDefMoneyflowListResponse;
import org.laladev.moneyjinn.core.rest.model.transport.PreDefMoneyflowTransport;
import org.laladev.moneyjinn.server.builder.PreDefMoneyflowTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.jdbc.Sql;

public class ShowPreDefMoneyflowListTest extends AbstractControllerTest {
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

  private ShowPreDefMoneyflowListResponse getCompleteResponse() {
    final ShowPreDefMoneyflowListResponse expected = new ShowPreDefMoneyflowListResponse();
    final List<PreDefMoneyflowTransport> preDefMoneyflowTransports = new ArrayList<>();
    preDefMoneyflowTransports
        .add(new PreDefMoneyflowTransportBuilder().forPreDefMoneyflow1().build());
    preDefMoneyflowTransports
        .add(new PreDefMoneyflowTransportBuilder().forPreDefMoneyflow3().build());
    expected.setPreDefMoneyflowTransports(preDefMoneyflowTransports);
    return expected;
  }

  @Test
  public void test_default_FullResponseObject() throws Exception {
    final ShowPreDefMoneyflowListResponse expected = this.getCompleteResponse();
    final ShowPreDefMoneyflowListResponse actual = super.callUsecaseWithoutContent("", this.method,
        false, ShowPreDefMoneyflowListResponse.class);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_AuthorizationRequired1_Error() throws Exception {
    this.userName = null;
    this.userPassword = null;
    super.callUsecaseExpect403("", this.method);
  }

  @Test
  @Sql("classpath:h2defaults.sql")
  public void test_emptyDatabase_noException() throws Exception {
    this.userName = UserTransportBuilder.ADMIN_NAME;
    this.userPassword = UserTransportBuilder.ADMIN_PASSWORD;
    final ShowPreDefMoneyflowListResponse expected = new ShowPreDefMoneyflowListResponse();
    final ShowPreDefMoneyflowListResponse actual = super.callUsecaseWithoutContent("", this.method,
        false, ShowPreDefMoneyflowListResponse.class);
    Assertions.assertEquals(expected, actual);
  }

}
