
package org.laladev.moneyjinn.server.controller.crud.etf;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.etf.EtfID;
import org.laladev.moneyjinn.server.builder.EtfTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.builder.ValidationItemTransportBuilder;
import org.laladev.moneyjinn.server.model.EtfTransport;
import org.laladev.moneyjinn.server.model.ValidationItemTransport;
import org.laladev.moneyjinn.server.model.ValidationResponse;
import org.laladev.moneyjinn.service.api.IEtfService;

import jakarta.inject.Inject;

class UpdateEtfTest extends AbstractEtfTest {
	@Inject
	private IEtfService etfService;

	@Override
	protected void loadMethod() {
		super.getMock().update(null, null);
	}

	private void testError(final EtfTransport transport, final ErrorCode errorCode) throws Exception {
		final List<ValidationItemTransport> validationItems = new ArrayList<>();
		validationItems.add(new ValidationItemTransportBuilder().withKey(EtfTransportBuilder.ETF_ID_1.toString())
				.withError(errorCode.getErrorCode()).build());
		final ValidationResponse expected = new ValidationResponse();
		expected.setValidationItemTransports(validationItems);
		expected.setResult(Boolean.FALSE);

		final ValidationResponse actual = super.callUsecaseExpect422(transport, ValidationResponse.class);

		assertEquals(expected, actual);
	}

	@Test
	void test_standardRequest_Successfull_MinimalReturn() throws Exception {
		final EtfTransport transport = new EtfTransportBuilder().forEtf1().build();

		final var etfId = new EtfID(EtfTransportBuilder.ETF_ID_1);
		final var userId = new UserID(UserTransportBuilder.USER1_ID);

		transport.setName("hugo");
		super.callUsecaseExpect204Minimal(transport);

		final var etf = this.etfService.getEtfById(userId, etfId);
		assertNotNull(etf);
		assertEquals("hugo", etf.getName());
	}

	@Test
	void test_standardRequest_Successfull_RepresentationReturn() throws Exception {
		final EtfTransport transport = new EtfTransportBuilder().forEtf1().build();

		final var etfId = new EtfID(EtfTransportBuilder.ETF_ID_1);
		final var userId = new UserID(UserTransportBuilder.USER1_ID);

		transport.setName("hugo");
		final EtfTransport actualTransport = super.callUsecaseExpect200Representation(transport, EtfTransport.class);

		// TODO preferedReturn can't handle this currently
		transport.setIsFavorite(null);
		assertEquals(transport, actualTransport);

		final var etf = this.etfService.getEtfById(userId, etfId);
		assertNotNull(etf);
		assertEquals("hugo", etf.getName());
	}

	@Test
	void test_standardRequest_Successfull_DefaultReturn() throws Exception {
		final EtfTransport transport = new EtfTransportBuilder().forEtf1().build();

		final var etfId = new EtfID(EtfTransportBuilder.ETF_ID_1);
		final var userId = new UserID(UserTransportBuilder.USER1_ID);

		transport.setName("hugo");
		super.callUsecaseExpect204(transport);

		final var etf = this.etfService.getEtfById(userId, etfId);
		assertNotNull(etf);
		assertEquals("hugo", etf.getName());
	}

	@Test
	void test_nullIsin_Error() throws Exception {
		final EtfTransport transport = new EtfTransportBuilder().forEtf1().build();
		transport.setIsin(null);
		this.testError(transport, ErrorCode.ISIN_MUST_NOT_BE_EMPTY);
	}

	@Test
	void test_emptyIsin_Error() throws Exception {
		final EtfTransport transport = new EtfTransportBuilder().forEtf1().build();
		transport.setIsin(" ");
		this.testError(transport, ErrorCode.ISIN_MUST_NOT_BE_EMPTY);
	}

	@Test
	void test_nullTicker_Error() throws Exception {
		final EtfTransport transport = new EtfTransportBuilder().forEtf1().build();
		transport.setTicker(null);
		this.testError(transport, ErrorCode.TICKER_MUST_NOT_BE_EMPTY);
	}

	@Test
	void test_emptyTicker_Error() throws Exception {
		final EtfTransport transport = new EtfTransportBuilder().forEtf1().build();
		transport.setTicker(" ");
		this.testError(transport, ErrorCode.TICKER_MUST_NOT_BE_EMPTY);
	}

	@Test
	void test_nullWkn_Error() throws Exception {
		final EtfTransport transport = new EtfTransportBuilder().forEtf1().build();
		transport.setWkn(null);
		this.testError(transport, ErrorCode.WKN_MUST_NOT_BE_EMPTY);
	}

	@Test
	void test_emptyWkn_Error() throws Exception {
		final EtfTransport transport = new EtfTransportBuilder().forEtf1().build();
		transport.setWkn(" ");
		this.testError(transport, ErrorCode.WKN_MUST_NOT_BE_EMPTY);
	}

	@Test
	void test_nullName_Error() throws Exception {
		final EtfTransport transport = new EtfTransportBuilder().forEtf1().build();
		transport.setName(null);
		this.testError(transport, ErrorCode.NAME_MUST_NOT_BE_EMPTY);
	}

	@Test
	void test_emptyName_Error() throws Exception {
		final EtfTransport transport = new EtfTransportBuilder().forEtf1().build();
		transport.setName(" ");
		this.testError(transport, ErrorCode.NAME_MUST_NOT_BE_EMPTY);
	}

	@Test
	void test_differentUserIdSet_ButIgnoredAndAlwaysCreatedWithOwnUserId() throws Exception {
		final EtfTransport transport = new EtfTransportBuilder().forEtf1().build();
		transport.setUserid(UserTransportBuilder.ADMIN_ID);
		transport.setName("hugo");

		super.callUsecaseExpect204(transport);

		final var etfId = new EtfID(EtfTransportBuilder.ETF_ID_1);
		final var userId = new UserID(UserTransportBuilder.USER1_ID);

		final var etf = this.etfService.getEtfById(userId, etfId);
		assertEquals(EtfTransportBuilder.ETF_ID_1, etf.getId().getId());
		assertEquals("hugo", etf.getName());
	}

	@Test
	void test_etfFromOtherGroup_nothingHappened() throws Exception {
		final EtfTransport transport = new EtfTransportBuilder().forEtf2().build();
		transport.setName("hugo");

		final var etfId = new EtfID(EtfTransportBuilder.ETF_ID_2);
		final var userId = new UserID(UserTransportBuilder.ADMIN_ID);

		var etf = this.etfService.getEtfById(userId, etfId);
		Assertions.assertNotEquals("hugo", etf.getName());

		super.callUsecaseExpect204(transport);

		etf = this.etfService.getEtfById(userId, etfId);
		Assertions.assertNotEquals("hugo", etf.getName());
	}

	@Override
	protected void callUsecaseExpect403ForThisUsecase() throws Exception {
		super.callUsecaseExpect403(new EtfTransport());
	}

	@Override
	protected void callUsecaseEmptyDatabase() throws Exception {

		final EtfTransport transport = new EtfTransportBuilder().forEtf1().build();

		super.callUsecaseExpect204(transport);
	}
}