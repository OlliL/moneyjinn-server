
package org.laladev.moneyjinn.server.controller.etf;

import java.math.BigDecimal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.model.etf.EtfFlow;
import org.laladev.moneyjinn.model.etf.EtfFlowID;
import org.laladev.moneyjinn.server.builder.EtfFlowTransportBuilder;
import org.laladev.moneyjinn.server.builder.EtfTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractWebUserControllerTest;
import org.laladev.moneyjinn.server.controller.api.EtfControllerApi;
import org.laladev.moneyjinn.server.model.EtfFlowTransport;
import org.laladev.moneyjinn.server.model.UpdateEtfFlowRequest;
import org.laladev.moneyjinn.server.model.ValidationResponse;
import org.laladev.moneyjinn.service.api.IEtfService;

import jakarta.inject.Inject;

class UpdateEtfFlowTest extends AbstractWebUserControllerTest {
	@Inject
	private IEtfService etfService;

	@Override
	protected void loadMethod() {
		super.getMock(EtfControllerApi.class).updateEtfFlow(null);
	}

	@Test
	void test_standardRequest_emptyResponse() throws Exception {
		final UpdateEtfFlowRequest request = new UpdateEtfFlowRequest();
		final EtfFlowTransport transport = new EtfFlowTransportBuilder().forFlow1().build();
		transport.setPrice(BigDecimal.ONE);
		request.setEtfFlowTransport(transport);

		super.callUsecaseExpect204(request);

		final EtfFlow etfFlow = this.etfService.getEtfFlowById(new EtfFlowID(EtfFlowTransportBuilder.ETF_FLOW_1ID));
		Assertions.assertEquals(0, BigDecimal.ONE.compareTo(etfFlow.getPrice()));
	}

	@Test
	void test_noEtfSpecified_errorResponse() throws Exception {
		final UpdateEtfFlowRequest request = new UpdateEtfFlowRequest();
		final EtfFlowTransport transport = new EtfFlowTransportBuilder().forFlow1().build();
		transport.setEtfId(null);
		request.setEtfFlowTransport(transport);

		final ValidationResponse actual = super.callUsecaseExpect422(request, ValidationResponse.class);

		Assertions.assertNotNull(actual.getValidationItemTransports());
		Assertions.assertEquals(1, actual.getValidationItemTransports().size());
		Assertions.assertEquals(ErrorCode.NO_ETF_SPECIFIED.getErrorCode(),
				actual.getValidationItemTransports().get(0).getError());
	}

	@Test
	void test_invalidEtfSpecified_errorResponse() throws Exception {
		final UpdateEtfFlowRequest request = new UpdateEtfFlowRequest();
		final EtfFlowTransport transport = new EtfFlowTransportBuilder().forFlow1().build();
		transport.setEtfId(EtfTransportBuilder.NON_EXISTING_ETF_ID);
		request.setEtfFlowTransport(transport);

		final ValidationResponse actual = super.callUsecaseExpect422(request, ValidationResponse.class);

		Assertions.assertNotNull(actual.getValidationItemTransports());
		Assertions.assertEquals(1, actual.getValidationItemTransports().size());
		Assertions.assertEquals(ErrorCode.NO_ETF_SPECIFIED.getErrorCode(),
				actual.getValidationItemTransports().get(0).getError());
	}

	@Test
	void test_priceNotSet_errorResponse() throws Exception {
		final UpdateEtfFlowRequest request = new UpdateEtfFlowRequest();
		final EtfFlowTransport transport = new EtfFlowTransportBuilder().forFlow1().build();
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
		final UpdateEtfFlowRequest request = new UpdateEtfFlowRequest();
		final EtfFlowTransport transport = new EtfFlowTransportBuilder().forFlow1().build();
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
		final UpdateEtfFlowRequest request = new UpdateEtfFlowRequest();
		final EtfFlowTransport transport = new EtfFlowTransportBuilder().forFlow1().build();
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
		final UpdateEtfFlowRequest request = new UpdateEtfFlowRequest();
		final EtfFlowTransport transport = new EtfFlowTransportBuilder().forFlow1().build();
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
		final UpdateEtfFlowRequest request = new UpdateEtfFlowRequest();
		final EtfFlowTransport transport = new EtfFlowTransportBuilder().forFlow1().build();
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
		final UpdateEtfFlowRequest request = new UpdateEtfFlowRequest();
		final EtfFlowTransport transport = new EtfFlowTransportBuilder().forFlow1().build();
		transport.setNanoseconds(null);
		request.setEtfFlowTransport(transport);

		super.callUsecaseExpect204(request);

		final EtfFlow etfFlow = this.etfService.getEtfFlowById(new EtfFlowID(EtfFlowTransportBuilder.ETF_FLOW_1ID));
		Assertions.assertEquals(0, etfFlow.getTime().getNano());

	}

	@Override
	protected void callUsecaseExpect403ForThisUsecase() throws Exception {
		super.callUsecaseExpect403(new UpdateEtfFlowRequest());
	}

	@Override
	protected void callUsecaseEmptyDatabase() throws Exception {
		super.callUsecaseExpect204(new UpdateEtfFlowRequest());
	}
}