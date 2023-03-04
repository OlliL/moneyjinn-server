
package org.laladev.moneyjinn.server.controller.contractpartner;

import jakarta.inject.Inject;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.model.Contractpartner;
import org.laladev.moneyjinn.model.ContractpartnerID;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.server.builder.ContractpartnerTransportBuilder;
import org.laladev.moneyjinn.server.builder.PostingAccountTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.builder.ValidationItemTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.laladev.moneyjinn.server.controller.api.ContractpartnerControllerApi;
import org.laladev.moneyjinn.server.model.ContractpartnerTransport;
import org.laladev.moneyjinn.server.model.UpdateContractpartnerRequest;
import org.laladev.moneyjinn.server.model.ValidationItemTransport;
import org.laladev.moneyjinn.server.model.ValidationResponse;
import org.laladev.moneyjinn.service.api.IContractpartnerService;
import org.springframework.test.context.jdbc.Sql;

public class UpdateContractpartnerTest extends AbstractControllerTest {
  @Inject
  private IContractpartnerService contractpartnerService;

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
    return super.getMethodFromTestClassName(ContractpartnerControllerApi.class, this.getClass());
  }

  private void testError(final ContractpartnerTransport transport, final ErrorCode errorCode)
      throws Exception {
    final UpdateContractpartnerRequest request = new UpdateContractpartnerRequest();
    request.setContractpartnerTransport(transport);
    final List<ValidationItemTransport> validationItems = new ArrayList<>();
    validationItems.add(new ValidationItemTransportBuilder().withKey(transport.getId().toString())
        .withError(errorCode.getErrorCode()).build());
    final ValidationResponse expected = new ValidationResponse();
    expected.setValidationItemTransports(validationItems);
    expected.setResult(Boolean.FALSE);
    final ValidationResponse actual = super.callUsecaseExpect422(request, ValidationResponse.class);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_ContractpartnernameAlreadyExisting_Error() throws Exception {
    final ContractpartnerTransport transport = new ContractpartnerTransportBuilder()
        .forContractpartner2().build();
    transport.setName(ContractpartnerTransportBuilder.CONTRACTPARTNER1_NAME);
    this.testError(transport, ErrorCode.NAME_ALREADY_EXISTS);
  }

  @Test
  public void test_emptyContractpartnername_Error() throws Exception {
    final ContractpartnerTransport transport = new ContractpartnerTransportBuilder()
        .forContractpartner2().build();
    transport.setName("");
    this.testError(transport, ErrorCode.NAME_MUST_NOT_BE_EMPTY);
  }

  @Test
  public void test_invalidPostingAccount_Error() throws Exception {
    final ContractpartnerTransport transport = new ContractpartnerTransportBuilder()
        .forNewContractpartner().build();
    transport.setPostingAccountId(PostingAccountTransportBuilder.NON_EXISTING_ID);
    this.testError(transport, ErrorCode.POSTING_ACCOUNT_NOT_SPECIFIED);
  }

  @Test
  public void test_ValidTilBeforeValidFrom_Error() throws Exception {
    final ContractpartnerTransport transport = new ContractpartnerTransportBuilder()
        .forContractpartner2().build();
    transport.setValidTil(LocalDate.parse("2000-01-01"));
    transport.setValidFrom(LocalDate.parse("2010-01-01"));
    this.testError(transport, ErrorCode.VALIDFROM_AFTER_VALIDTIL);
  }

  @Test
  public void test_ValidityPeriodOutOfUsage_Error() throws Exception {
    final ContractpartnerTransport transport = new ContractpartnerTransportBuilder()
        .forContractpartner1().build();
    transport.setValidFrom(LocalDate.parse("2010-01-01"));
    this.testError(transport, ErrorCode.MONEYFLOWS_OUTSIDE_VALIDITY_PERIOD);
  }

  @Test
  public void test_standardRequest_Successfull() throws Exception {
    final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
    final ContractpartnerID contractpartnerId = new ContractpartnerID(
        ContractpartnerTransportBuilder.CONTRACTPARTNER1_ID);
    final UpdateContractpartnerRequest request = new UpdateContractpartnerRequest();
    final ContractpartnerTransport transport = new ContractpartnerTransportBuilder()
        .forContractpartner1().build();
    transport.setName("hugo");
    request.setContractpartnerTransport(transport);

    super.callUsecaseExpect204(request);

    final Contractpartner contractpartner = this.contractpartnerService
        .getContractpartnerById(userId, contractpartnerId);
    Assertions.assertEquals(ContractpartnerTransportBuilder.CONTRACTPARTNER1_ID,
        contractpartner.getId().getId());
    Assertions.assertEquals("hugo", contractpartner.getName());
  }

  @Test
  public void test_ContractpartnerFromSameGroupButNotMe_SuccessfullNoContent() throws Exception {
    final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
    final ContractpartnerID contractpartnerId = new ContractpartnerID(
        ContractpartnerTransportBuilder.CONTRACTPARTNER3_ID);
    final UpdateContractpartnerRequest request = new UpdateContractpartnerRequest();
    final ContractpartnerTransport transport = new ContractpartnerTransportBuilder()
        .forContractpartner3().build();
    transport.setName("hugo");
    request.setContractpartnerTransport(transport);

    super.callUsecaseExpect204(request);

    final Contractpartner contractpartner = this.contractpartnerService
        .getContractpartnerById(userId, contractpartnerId);
    Assertions.assertEquals(ContractpartnerTransportBuilder.CONTRACTPARTNER3_ID,
        contractpartner.getId().getId());
    Assertions.assertEquals("hugo", contractpartner.getName());
  }

  @Test
  public void test_ContractpartnerFromDifferentGroup_notSuccessfull() throws Exception {
    final UserID userId = new UserID(UserTransportBuilder.ADMIN_ID);
    final ContractpartnerID contractpartnerId = new ContractpartnerID(
        ContractpartnerTransportBuilder.CONTRACTPARTNER5_ID);
    final UpdateContractpartnerRequest request = new UpdateContractpartnerRequest();
    final ContractpartnerTransport transport = new ContractpartnerTransportBuilder()
        .forContractpartner3().build();
    transport.setName("hugo");
    request.setContractpartnerTransport(transport);

    super.callUsecaseExpect204(request);

    final Contractpartner contractpartner = this.contractpartnerService
        .getContractpartnerById(userId, contractpartnerId);
    Assertions.assertEquals(ContractpartnerTransportBuilder.CONTRACTPARTNER5_ID,
        contractpartner.getId().getId());
    Assertions.assertEquals(ContractpartnerTransportBuilder.CONTRACTPARTNER5_NAME,
        contractpartner.getName());
  }

  @Test
  public void test_AuthorizationRequired_Error() throws Exception {
    this.userName = null;
    this.userPassword = null;

    super.callUsecaseExpect403(new UpdateContractpartnerRequest());
  }

  @Test
  @Sql("classpath:h2defaults.sql")
  public void test_emptyDatabase_noException() throws Exception {
    this.userName = UserTransportBuilder.ADMIN_NAME;
    this.userPassword = UserTransportBuilder.ADMIN_PASSWORD;
    final UpdateContractpartnerRequest request = new UpdateContractpartnerRequest();
    final ContractpartnerTransport transport = new ContractpartnerTransportBuilder()
        .forContractpartner1().build();
    transport.setPostingAccountId(null); // otherwise not existing id referenced
    request.setContractpartnerTransport(transport);

    super.callUsecaseExpect204(request);
  }
}