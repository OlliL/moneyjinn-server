
package org.laladev.moneyjinn.server.controller.etf;

import jakarta.inject.Inject;
import java.math.BigDecimal;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.core.rest.model.etf.CreateEtfFlowRequest;
import org.laladev.moneyjinn.core.rest.model.etf.CreateEtfFlowResponse;
import org.laladev.moneyjinn.core.rest.model.etf.transport.EtfFlowTransport;
import org.laladev.moneyjinn.server.builder.EtfFlowTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.laladev.moneyjinn.service.api.IEtfService;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.jdbc.Sql;

public class CreateEtfFlowTest extends AbstractControllerTest {
  @Inject
  IEtfService etfService;

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

  @Test
  public void test_standardRequest_emptyResponse() throws Exception {
    final CreateEtfFlowRequest request = new CreateEtfFlowRequest();
    final EtfFlowTransport transport = new EtfFlowTransportBuilder().forNewFlow().build();
    request.setEtfFlowTransport(transport);

    final CreateEtfFlowResponse expected = new CreateEtfFlowResponse();
    expected.setEtfFlowId(EtfFlowTransportBuilder.NEXT_ID);
    expected.setResult(true);

    final CreateEtfFlowResponse actual = super.callUsecaseWithContent("", this.method, request,
        false, CreateEtfFlowResponse.class);

    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_noEtfSpecified_errorResponse() throws Exception {
    final CreateEtfFlowRequest request = new CreateEtfFlowRequest();
    final EtfFlowTransport transport = new EtfFlowTransportBuilder().forNewFlow().build();
    transport.setIsin(null);
    request.setEtfFlowTransport(transport);

    final CreateEtfFlowResponse response = super.callUsecaseWithContent("", this.method, request,
        false, CreateEtfFlowResponse.class);

    Assertions.assertNotNull(response.getValidationItemTransports());
    Assertions.assertEquals(1, response.getValidationItemTransports().size());
    Assertions.assertEquals(ErrorCode.NO_ETF_SPECIFIED.getErrorCode(),
        response.getValidationItemTransports().get(0).getError());
  }

  @Test
  public void test_invalidEtfSpecified_errorResponse() throws Exception {
    final CreateEtfFlowRequest request = new CreateEtfFlowRequest();
    final EtfFlowTransport transport = new EtfFlowTransportBuilder().forNewFlow().build();
    transport.setIsin("NOTEXISTING");
    request.setEtfFlowTransport(transport);

    final CreateEtfFlowResponse response = super.callUsecaseWithContent("", this.method, request,
        false, CreateEtfFlowResponse.class);

    Assertions.assertNotNull(response.getValidationItemTransports());
    Assertions.assertEquals(1, response.getValidationItemTransports().size());
    Assertions.assertEquals(ErrorCode.NO_ETF_SPECIFIED.getErrorCode(),
        response.getValidationItemTransports().get(0).getError());
  }

  @Test
  public void test_priceNotSet_errorResponse() throws Exception {
    final CreateEtfFlowRequest request = new CreateEtfFlowRequest();
    final EtfFlowTransport transport = new EtfFlowTransportBuilder().forNewFlow().build();
    transport.setPrice(null);
    request.setEtfFlowTransport(transport);

    final CreateEtfFlowResponse response = super.callUsecaseWithContent("", this.method, request,
        false, CreateEtfFlowResponse.class);

    Assertions.assertNotNull(response.getValidationItemTransports());
    Assertions.assertEquals(1, response.getValidationItemTransports().size());
    Assertions.assertEquals(ErrorCode.PRICE_NOT_SET.getErrorCode(),
        response.getValidationItemTransports().get(0).getError());
  }

  @Test
  public void test_priceIsZero_errorResponse() throws Exception {
    final CreateEtfFlowRequest request = new CreateEtfFlowRequest();
    final EtfFlowTransport transport = new EtfFlowTransportBuilder().forNewFlow().build();
    transport.setPrice(BigDecimal.ZERO);
    request.setEtfFlowTransport(transport);

    final CreateEtfFlowResponse response = super.callUsecaseWithContent("", this.method, request,
        false, CreateEtfFlowResponse.class);

    Assertions.assertNotNull(response.getValidationItemTransports());
    Assertions.assertEquals(1, response.getValidationItemTransports().size());
    Assertions.assertEquals(ErrorCode.PRICE_NOT_SET.getErrorCode(),
        response.getValidationItemTransports().get(0).getError());
  }

  @Test
  public void test_amountNotSet_errorResponse() throws Exception {
    final CreateEtfFlowRequest request = new CreateEtfFlowRequest();
    final EtfFlowTransport transport = new EtfFlowTransportBuilder().forNewFlow().build();
    transport.setAmount(null);
    request.setEtfFlowTransport(transport);

    final CreateEtfFlowResponse response = super.callUsecaseWithContent("", this.method, request,
        false, CreateEtfFlowResponse.class);

    Assertions.assertNotNull(response.getValidationItemTransports());
    Assertions.assertEquals(1, response.getValidationItemTransports().size());
    Assertions.assertEquals(ErrorCode.PIECES_NOT_SET.getErrorCode(),
        response.getValidationItemTransports().get(0).getError());
  }

  @Test
  public void test_amountIsZero_errorResponse() throws Exception {
    final CreateEtfFlowRequest request = new CreateEtfFlowRequest();
    final EtfFlowTransport transport = new EtfFlowTransportBuilder().forNewFlow().build();
    transport.setAmount(BigDecimal.ZERO);
    request.setEtfFlowTransport(transport);

    final CreateEtfFlowResponse response = super.callUsecaseWithContent("", this.method, request,
        false, CreateEtfFlowResponse.class);

    Assertions.assertNotNull(response.getValidationItemTransports());
    Assertions.assertEquals(1, response.getValidationItemTransports().size());
    Assertions.assertEquals(ErrorCode.PIECES_NOT_SET.getErrorCode(),
        response.getValidationItemTransports().get(0).getError());
  }

  @Test
  public void test_timeIsNull_errorResponse() throws Exception {
    final CreateEtfFlowRequest request = new CreateEtfFlowRequest();
    final EtfFlowTransport transport = new EtfFlowTransportBuilder().forNewFlow().build();
    transport.setTimestamp(null);
    request.setEtfFlowTransport(transport);

    final CreateEtfFlowResponse response = super.callUsecaseWithContent("", this.method, request,
        false, CreateEtfFlowResponse.class);

    Assertions.assertNotNull(response.getValidationItemTransports());
    Assertions.assertEquals(1, response.getValidationItemTransports().size());
    Assertions.assertEquals(ErrorCode.BOOKINGDATE_IN_WRONG_FORMAT.getErrorCode(),
        response.getValidationItemTransports().get(0).getError());
  }

  @Test
  public void test_nanosecondsNotSet_emptyResponse() throws Exception {
    final CreateEtfFlowRequest request = new CreateEtfFlowRequest();
    final EtfFlowTransport transport = new EtfFlowTransportBuilder().forNewFlow().build();
    transport.setNanoseconds(null);
    request.setEtfFlowTransport(transport);

    final CreateEtfFlowResponse expected = new CreateEtfFlowResponse();
    expected.setEtfFlowId(EtfFlowTransportBuilder.NEXT_ID);
    expected.setResult(true);

    final CreateEtfFlowResponse actual = super.callUsecaseWithContent("", this.method, request,
        false, CreateEtfFlowResponse.class);

    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_AuthorizationRequired_Error() throws Exception {
    this.userName = null;
    this.userPassword = null;
    super.callUsecaseExpect403("", this.method);
  }

  @Test
  @Sql("classpath:h2defaults.sql")
  public void test_emptyDatabase_noException() throws Exception {
    this.userName = UserTransportBuilder.ADMIN_NAME;
    this.userPassword = UserTransportBuilder.ADMIN_PASSWORD;

    super.callUsecaseWithContent("", this.method, new CreateEtfFlowRequest(), false,
        CreateEtfFlowResponse.class);

  }
}