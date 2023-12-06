
package org.laladev.moneyjinn.server.controller.etf;

import java.math.BigDecimal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.server.builder.EtfFlowTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractWebUserControllerTest;
import org.laladev.moneyjinn.server.controller.api.EtfControllerApi;
import org.laladev.moneyjinn.server.model.CalcEtfSaleResponse;
import org.laladev.moneyjinn.server.model.CreateEtfFlowRequest;
import org.laladev.moneyjinn.server.model.CreateEtfFlowResponse;
import org.laladev.moneyjinn.server.model.EtfFlowTransport;
import org.laladev.moneyjinn.server.model.ValidationResponse;

class CreateEtfFlowTest extends AbstractWebUserControllerTest {
	@Override
	protected void loadMethod() {
		super.getMock(EtfControllerApi.class).createEtfFlow(null);
	}

	@Test
	void test_standardRequest_emptyResponse() throws Exception {
		final CreateEtfFlowRequest request = new CreateEtfFlowRequest();
		final EtfFlowTransport transport = new EtfFlowTransportBuilder().forNewFlow().build();
		request.setEtfFlowTransport(transport);

		final CreateEtfFlowResponse expected = new CreateEtfFlowResponse();
		expected.setEtfFlowId(EtfFlowTransportBuilder.NEXT_ID);

		final CreateEtfFlowResponse actual = super.callUsecaseExpect200(request, CreateEtfFlowResponse.class);

		Assertions.assertEquals(expected, actual);
	}

	@Test
	void test_noEtfSpecified_errorResponse() throws Exception {
		final CreateEtfFlowRequest request = new CreateEtfFlowRequest();
		final EtfFlowTransport transport = new EtfFlowTransportBuilder().forNewFlow().build();
		transport.setIsin(null);
		request.setEtfFlowTransport(transport);

		final ValidationResponse actual = super.callUsecaseExpect422(request, ValidationResponse.class);

		Assertions.assertNotNull(actual.getValidationItemTransports());
		Assertions.assertEquals(1, actual.getValidationItemTransports().size());
		Assertions.assertEquals(ErrorCode.NO_ETF_SPECIFIED.getErrorCode(),
				actual.getValidationItemTransports().get(0).getError());
	}

	@Test
	void test_invalidEtfSpecified_errorResponse() throws Exception {
		final CreateEtfFlowRequest request = new CreateEtfFlowRequest();
		final EtfFlowTransport transport = new EtfFlowTransportBuilder().forNewFlow().build();
		transport.setIsin("NOTEXISTING");
		request.setEtfFlowTransport(transport);

		final ValidationResponse actual = super.callUsecaseExpect422(request, ValidationResponse.class);

		Assertions.assertNotNull(actual.getValidationItemTransports());
		Assertions.assertEquals(1, actual.getValidationItemTransports().size());
		Assertions.assertEquals(ErrorCode.NO_ETF_SPECIFIED.getErrorCode(),
				actual.getValidationItemTransports().get(0).getError());
	}

	@Test
	void test_priceNotSet_errorResponse() throws Exception {
		final CreateEtfFlowRequest request = new CreateEtfFlowRequest();
		final EtfFlowTransport transport = new EtfFlowTransportBuilder().forNewFlow().build();
		transport.setPrice(null);
		request.setEtfFlowTransport(transport);

		final ValidationResponse actual = super.callUsecaseExpect422(request, ValidationResponse.class);

		Assertions.assertNotNull(actual.getValidationItemTransports());
		Assertions.assertEquals(1, actual.getValidationItemTransports().size());
		Assertions.assertEquals(ErrorCode.PRICE_NOT_SET.getErrorCode(),
				actual.getValidationItemTransports().get(0).getError());
	}

	@Test
	void test_priceIsZero_errorResponse() throws Exception {
		final CreateEtfFlowRequest request = new CreateEtfFlowRequest();
		final EtfFlowTransport transport = new EtfFlowTransportBuilder().forNewFlow().build();
		transport.setPrice(BigDecimal.ZERO);
		request.setEtfFlowTransport(transport);

		final ValidationResponse actual = super.callUsecaseExpect422(request, ValidationResponse.class);
		Assertions.assertNotNull(actual.getValidationItemTransports());
		Assertions.assertEquals(1, actual.getValidationItemTransports().size());
		Assertions.assertEquals(ErrorCode.PRICE_NOT_SET.getErrorCode(),
				actual.getValidationItemTransports().get(0).getError());
	}

	@Test
	void test_amountNotSet_errorResponse() throws Exception {
		final CreateEtfFlowRequest request = new CreateEtfFlowRequest();
		final EtfFlowTransport transport = new EtfFlowTransportBuilder().forNewFlow().build();
		transport.setAmount(null);
		request.setEtfFlowTransport(transport);

		final ValidationResponse actual = super.callUsecaseExpect422(request, ValidationResponse.class);
		Assertions.assertNotNull(actual.getValidationItemTransports());
		Assertions.assertEquals(1, actual.getValidationItemTransports().size());
		Assertions.assertEquals(ErrorCode.PIECES_NOT_SET.getErrorCode(),
				actual.getValidationItemTransports().get(0).getError());
	}

	@Test
	void test_amountIsZero_errorResponse() throws Exception {
		final CreateEtfFlowRequest request = new CreateEtfFlowRequest();
		final EtfFlowTransport transport = new EtfFlowTransportBuilder().forNewFlow().build();
		transport.setAmount(BigDecimal.ZERO);
		request.setEtfFlowTransport(transport);

		final ValidationResponse actual = super.callUsecaseExpect422(request, ValidationResponse.class);
		Assertions.assertNotNull(actual.getValidationItemTransports());
		Assertions.assertEquals(1, actual.getValidationItemTransports().size());
		Assertions.assertEquals(ErrorCode.PIECES_NOT_SET.getErrorCode(),
				actual.getValidationItemTransports().get(0).getError());
	}

	@Test
	void test_timeIsNull_errorResponse() throws Exception {
		final CreateEtfFlowRequest request = new CreateEtfFlowRequest();
		final EtfFlowTransport transport = new EtfFlowTransportBuilder().forNewFlow().build();
		transport.setTimestamp(null);
		request.setEtfFlowTransport(transport);

		final ValidationResponse actual = super.callUsecaseExpect422(request, ValidationResponse.class);
		Assertions.assertNotNull(actual.getValidationItemTransports());
		Assertions.assertEquals(1, actual.getValidationItemTransports().size());
		Assertions.assertEquals(ErrorCode.BOOKINGDATE_IN_WRONG_FORMAT.getErrorCode(),
				actual.getValidationItemTransports().get(0).getError());
	}

	@Test
	void test_nanosecondsNotSet_emptyResponse() throws Exception {
		final CreateEtfFlowRequest request = new CreateEtfFlowRequest();
		final EtfFlowTransport transport = new EtfFlowTransportBuilder().forNewFlow().build();
		transport.setNanoseconds(null);
		request.setEtfFlowTransport(transport);

		final CreateEtfFlowResponse expected = new CreateEtfFlowResponse();
		expected.setEtfFlowId(EtfFlowTransportBuilder.NEXT_ID);

		final CreateEtfFlowResponse actual = super.callUsecaseExpect200(request, CreateEtfFlowResponse.class);

		Assertions.assertEquals(expected, actual);
	}

	@Override
	protected void callUsecaseExpect403ForThisUsecase() throws Exception {
		super.callUsecaseExpect403(new CreateEtfFlowRequest());
	}

	@Override
	protected void callUsecaseEmptyDatabase() throws Exception {
		super.callUsecaseExpect200(new CreateEtfFlowRequest(), CalcEtfSaleResponse.class);
	}
}