
package org.laladev.moneyjinn.server.controller.etf;

import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.model.etf.EtfFlow;
import org.laladev.moneyjinn.model.etf.EtfFlowID;
import org.laladev.moneyjinn.server.builder.EtfFlowTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.laladev.moneyjinn.service.api.IEtfService;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.jdbc.Sql;

public class DeleteEtfFlowTest extends AbstractControllerTest {
  @Inject
  IEtfService etfService;

  private final HttpMethod method = HttpMethod.DELETE;
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
  public void test_standardRequest_emptyResponse() throws Exception {
    final EtfFlowID etfFlowId = new EtfFlowID(EtfFlowTransportBuilder.ETF_FLOW_1ID);

    EtfFlow etfFlow = this.etfService.getEtfFlowById(etfFlowId);
    Assertions.assertNotNull(etfFlow);

    super.callUsecaseExpect204("/" + EtfFlowTransportBuilder.ETF_FLOW_1ID, this.method);

    etfFlow = this.etfService.getEtfFlowById(etfFlowId);
    Assertions.assertNull(etfFlow);
  }

  @Test
  public void test_DeleteNotExistingId_emptyResponse() throws Exception {
    super.callUsecaseExpect204("/" + EtfFlowTransportBuilder.NEXT_ID, this.method);
  }

  @Test
  public void test_AuthorizationRequired_Error() throws Exception {
    this.userName = null;
    this.userPassword = null;

    super.callUsecaseExpect403("/" + EtfFlowTransportBuilder.ETF_FLOW_1ID, this.method);
  }

  @Test
  @Sql("classpath:h2defaults.sql")
  public void test_emptyDatabase_noException() throws Exception {
    this.userName = UserTransportBuilder.ADMIN_NAME;
    this.userPassword = UserTransportBuilder.ADMIN_PASSWORD;

    super.callUsecaseExpect204("/" + EtfFlowTransportBuilder.ETF_FLOW_1ID, this.method);
  }
}