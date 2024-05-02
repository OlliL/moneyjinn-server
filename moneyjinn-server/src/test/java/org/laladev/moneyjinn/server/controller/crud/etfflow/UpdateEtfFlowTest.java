
package org.laladev.moneyjinn.server.controller.crud.etfflow;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.etf.EtfFlowID;
import org.laladev.moneyjinn.server.builder.EtfFlowTransportBuilder;
import org.laladev.moneyjinn.server.builder.EtfTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.builder.ValidationItemTransportBuilder;
import org.laladev.moneyjinn.server.model.EtfFlowTransport;
import org.laladev.moneyjinn.server.model.ValidationItemTransport;
import org.laladev.moneyjinn.server.model.ValidationResponse;
import org.laladev.moneyjinn.service.api.IEtfService;

import jakarta.inject.Inject;

class UpdateEtfFlowTest extends AbstractEtfFlowTest {
	private static final BigDecimal BIGDECIMAL_123_01 = new BigDecimal("123.010");
	@Inject
	private IEtfService etfService;

	@Override
	protected void loadMethod() {
		super.getMock().update(null, null);
	}

	private void testError(final EtfFlowTransport transport, final ErrorCode errorCode) throws Exception {
		final List<ValidationItemTransport> validationItems = new ArrayList<>();
		validationItems.add(new ValidationItemTransportBuilder()
				.withKey(EtfFlowTransportBuilder.ETF_FLOW_11ID.toString()).withError(errorCode.getErrorCode()).build());
		final ValidationResponse expected = new ValidationResponse();
		expected.setValidationItemTransports(validationItems);
		expected.setResult(Boolean.FALSE);

		final ValidationResponse actual = super.callUsecaseExpect422(transport, ValidationResponse.class);

		assertEquals(expected, actual);
	}

	@Test
	void test_standardRequest_Successfull_MinimalReturn() throws Exception {
		final EtfFlowTransport transport = new EtfFlowTransportBuilder().forFlow11().build();

		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final var etfFlowId = new EtfFlowID(EtfFlowTransportBuilder.ETF_FLOW_11ID);

		transport.setAmount(BIGDECIMAL_123_01);
		super.callUsecaseExpect204Minimal(transport);

		final var etfFlow = this.etfService.getEtfFlowById(userId, etfFlowId);
		assertNotNull(etfFlow);
		assertEquals(BIGDECIMAL_123_01, etfFlow.getAmount());
	}

	@Test
	void test_standardRequest_Successfull_RepresentationReturn() throws Exception {
		final EtfFlowTransport transport = new EtfFlowTransportBuilder().forFlow11().build();

		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final var etfFlowId = new EtfFlowID(EtfFlowTransportBuilder.ETF_FLOW_11ID);

		transport.setAmount(BIGDECIMAL_123_01);
		final EtfFlowTransport actualTransport = super.callUsecaseExpect200Representation(transport,
				EtfFlowTransport.class);

		assertEquals(transport, actualTransport);

		final var etfFlow = this.etfService.getEtfFlowById(userId, etfFlowId);
		assertNotNull(etfFlow);
		assertEquals(BIGDECIMAL_123_01, etfFlow.getAmount());
	}

	@Test
	void test_standardRequest_Successfull_DefaultReturn() throws Exception {
		final EtfFlowTransport transport = new EtfFlowTransportBuilder().forFlow11().build();

		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final var etfFlowId = new EtfFlowID(EtfFlowTransportBuilder.ETF_FLOW_11ID);

		transport.setAmount(BIGDECIMAL_123_01);
		super.callUsecaseExpect204(transport);

		final var etfFlow = this.etfService.getEtfFlowById(userId, etfFlowId);
		assertNotNull(etfFlow);
		assertEquals(BIGDECIMAL_123_01, etfFlow.getAmount());
	}

	@Test
	void test_invalidEtfSpecified_Error() throws Exception {
		final EtfFlowTransport transport = new EtfFlowTransportBuilder().forFlow11().build();
		transport.setEtfId(EtfTransportBuilder.NON_EXISTING_ID);
		this.testError(transport, ErrorCode.NO_ETF_SPECIFIED);
	}

	@Test
	void test_noEtfSpecified_Error() throws Exception {
		final EtfFlowTransport transport = new EtfFlowTransportBuilder().forFlow11().build();
		transport.setEtfId(null);
		this.testError(transport, ErrorCode.NO_ETF_SPECIFIED);
	}

	@Test
	void test_priceNotSet_Error() throws Exception {
		final EtfFlowTransport transport = new EtfFlowTransportBuilder().forFlow11().build();
		transport.setPrice(null);
		this.testError(transport, ErrorCode.PRICE_NOT_SET);
	}

	@Test
	void test_priceIsZero_Error() throws Exception {
		final EtfFlowTransport transport = new EtfFlowTransportBuilder().forFlow11().build();
		transport.setPrice(BigDecimal.ZERO);
		this.testError(transport, ErrorCode.PRICE_NOT_SET);
	}

	@Test
	void test_amountNotSet_Error() throws Exception {
		final EtfFlowTransport transport = new EtfFlowTransportBuilder().forFlow11().build();
		transport.setAmount(null);
		this.testError(transport, ErrorCode.PIECES_NOT_SET);
	}

	@Test
	void test_amountIsZero_Error() throws Exception {
		final EtfFlowTransport transport = new EtfFlowTransportBuilder().forFlow11().build();
		transport.setAmount(BigDecimal.ZERO);
		this.testError(transport, ErrorCode.PIECES_NOT_SET);
	}

	@Test
	void test_timeIsNull_Error() throws Exception {
		final EtfFlowTransport transport = new EtfFlowTransportBuilder().forFlow11().build();
		transport.setTimestamp(null);
		this.testError(transport, ErrorCode.BOOKINGDATE_IN_WRONG_FORMAT);
	}

	@Test
	void test_etfFromOtherGroup_Error() throws Exception {
		final EtfFlowTransport transport = new EtfFlowTransportBuilder().forFlow11().build();
		transport.setEtfId(EtfTransportBuilder.ETF_ID_2);
		this.testError(transport, ErrorCode.NO_ETF_SPECIFIED);
	}

	@Override
	protected void callUsecaseExpect403ForThisUsecase() throws Exception {
		super.callUsecaseExpect403(new EtfFlowTransport());
	}

	@Override
	protected void callUsecaseEmptyDatabase() throws Exception {

		final EtfFlowTransport transport = new EtfFlowTransportBuilder().forFlow1().build();

		super.callUsecaseExpect422(transport, ValidationResponse.class);
	}
}