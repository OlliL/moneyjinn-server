
package org.laladev.moneyjinn.server.controller.contractpartner;

import jakarta.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.server.builder.ContractpartnerTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.laladev.moneyjinn.server.model.ContractpartnerTransport;
import org.laladev.moneyjinn.server.model.ShowContractpartnerListResponse;
import org.laladev.moneyjinn.service.api.IContractpartnerService;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.jdbc.Sql;

public class ShowContractpartnerListTest extends AbstractControllerTest {
  @Inject
  private IContractpartnerService contractpartnerService;
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

  private ShowContractpartnerListResponse getCompleteResponse() {
    final ShowContractpartnerListResponse expected = new ShowContractpartnerListResponse();
    final List<ContractpartnerTransport> contractpartnerTransports = new ArrayList<>();
    contractpartnerTransports
        .add(new ContractpartnerTransportBuilder().forContractpartner1().build());
    contractpartnerTransports
        .add(new ContractpartnerTransportBuilder().forContractpartner2().build());
    contractpartnerTransports
        .add(new ContractpartnerTransportBuilder().forContractpartner3().build());
    contractpartnerTransports
        .add(new ContractpartnerTransportBuilder().forContractpartner4().build());
    expected.setContractpartnerTransports(contractpartnerTransports);
    return expected;
  }

  @Test
  public void test_default_FullResponseObject() throws Exception {
    final ShowContractpartnerListResponse expected = this.getCompleteResponse();
    final ShowContractpartnerListResponse actual = super.callUsecaseExpect200(this.method,
        ShowContractpartnerListResponse.class);
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
    final ShowContractpartnerListResponse expected = new ShowContractpartnerListResponse();
    final ShowContractpartnerListResponse actual = super.callUsecaseExpect200(this.method,
        ShowContractpartnerListResponse.class);
    Assertions.assertEquals(expected, actual);
  }
}
