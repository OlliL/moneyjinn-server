
package org.laladev.moneyjinn.server.controller.crud.etf;

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

class CreateEtfTest extends AbstractEtfTest {
	@Inject
	private IEtfService etfService;
	@Inject
	private EtfFavoriteTestUtil favoriteTestUtil;

	@Override
	protected void loadMethod() {
		super.getMock().create(null, null);
	}

	private void testError(final EtfTransport transport, final ErrorCode errorCode) throws Exception {
		final List<ValidationItemTransport> validationItems = new ArrayList<>();
		validationItems
				.add(new ValidationItemTransportBuilder().withKey(null).withError(errorCode.getErrorCode()).build());
		final ValidationResponse expected = new ValidationResponse();
		expected.setValidationItemTransports(validationItems);
		expected.setResult(Boolean.FALSE);

		final ValidationResponse actual = super.callUsecaseExpect422(transport, ValidationResponse.class);

		Assertions.assertEquals(expected, actual);
	}

	@Test
	void test_standardRequest_Successfull_MinimalReturn() throws Exception {
		final EtfTransport transport = new EtfTransportBuilder().forNewEtf().build();

		final var etfId = new EtfID(EtfTransportBuilder.NEXT_ID);
		final var userId = new UserID(UserTransportBuilder.USER1_ID);

		var etf = this.etfService.getEtfById(userId, etfId);
		Assertions.assertNull(etf);

		super.callUsecaseExpect204Minimal(transport);

		etf = this.etfService.getEtfById(userId, etfId);
		Assertions.assertNotNull(etf);
	}

	@Test
	void test_newFavorite_favoriteMoved() throws Exception {
		final EtfTransport transport = new EtfTransportBuilder().forNewEtf().build();
		transport.setIsFavorite(1);

		final var etfId = new EtfID(EtfTransportBuilder.NEXT_ID);
		final var userId = new UserID(UserTransportBuilder.USER1_ID);

		this.favoriteTestUtil.assertDefaultFavorite(userId);

		super.callUsecaseExpect204Minimal(transport);
		this.favoriteTestUtil.assertDefaultIsNotFavorite(userId);
		this.favoriteTestUtil.assertIsFavorite(userId, etfId);
	}

	@Test
	void test_standardRequest_Successfull_RepresentationReturn() throws Exception {
		final EtfTransport transport = new EtfTransportBuilder().forNewEtf().build();

		final var etfId = new EtfID(EtfTransportBuilder.NEXT_ID);
		final var userId = new UserID(UserTransportBuilder.USER1_ID);

		var etf = this.etfService.getEtfById(userId, etfId);
		Assertions.assertNull(etf);

		final EtfTransport actualTransport = super.callUsecaseExpect200Representation(transport, EtfTransport.class);

		transport.setEtfId(EtfTransportBuilder.NEXT_ID);
		Assertions.assertEquals(transport, actualTransport);

		etf = this.etfService.getEtfById(userId, etfId);
		Assertions.assertNotNull(etf);
	}

	@Test
	void test_standardRequest_Successfull_DefaultReturn() throws Exception {
		final EtfTransport transport = new EtfTransportBuilder().forNewEtf().build();

		final var etfId = new EtfID(EtfTransportBuilder.NEXT_ID);
		final var userId = new UserID(UserTransportBuilder.USER1_ID);

		var etf = this.etfService.getEtfById(userId, etfId);
		Assertions.assertNull(etf);

		super.callUsecaseExpect204(transport);

		etf = this.etfService.getEtfById(userId, etfId);
		Assertions.assertNotNull(etf);
	}

	@Test
	void test_nullIsin_Error() throws Exception {
		final EtfTransport transport = new EtfTransportBuilder().forNewEtf().build();
		transport.setIsin(null);
		this.testError(transport, ErrorCode.ISIN_MUST_NOT_BE_EMPTY);
	}

	@Test
	void test_emptyIsin_Error() throws Exception {
		final EtfTransport transport = new EtfTransportBuilder().forNewEtf().build();
		transport.setIsin(" ");
		this.testError(transport, ErrorCode.ISIN_MUST_NOT_BE_EMPTY);
	}

	@Test
	void test_nullTicker_Error() throws Exception {
		final EtfTransport transport = new EtfTransportBuilder().forNewEtf().build();
		transport.setTicker(null);
		this.testError(transport, ErrorCode.TICKER_MUST_NOT_BE_EMPTY);
	}

	@Test
	void test_emptyTicker_Error() throws Exception {
		final EtfTransport transport = new EtfTransportBuilder().forNewEtf().build();
		transport.setTicker(" ");
		this.testError(transport, ErrorCode.TICKER_MUST_NOT_BE_EMPTY);
	}

	@Test
	void test_nullWkn_Error() throws Exception {
		final EtfTransport transport = new EtfTransportBuilder().forNewEtf().build();
		transport.setWkn(null);
		this.testError(transport, ErrorCode.WKN_MUST_NOT_BE_EMPTY);
	}

	@Test
	void test_emptyWkn_Error() throws Exception {
		final EtfTransport transport = new EtfTransportBuilder().forNewEtf().build();
		transport.setWkn(" ");
		this.testError(transport, ErrorCode.WKN_MUST_NOT_BE_EMPTY);
	}

	@Test
	void test_nullName_Error() throws Exception {
		final EtfTransport transport = new EtfTransportBuilder().forNewEtf().build();
		transport.setName(null);
		this.testError(transport, ErrorCode.NAME_MUST_NOT_BE_EMPTY);
	}

	@Test
	void test_emptyName_Error() throws Exception {
		final EtfTransport transport = new EtfTransportBuilder().forNewEtf().build();
		transport.setName(" ");
		this.testError(transport, ErrorCode.NAME_MUST_NOT_BE_EMPTY);
	}

	@Test
	void test_differentUserIdSet_ButIgnoredAndAlwaysCreatedWithOwnUserId() throws Exception {
		final EtfTransport transport = new EtfTransportBuilder().forNewEtf().build();
		transport.setUserid(UserTransportBuilder.ADMIN_ID);

		super.callUsecaseExpect204(transport);

		final var etfId = new EtfID(EtfTransportBuilder.NEXT_ID);
		final var userId = new UserID(UserTransportBuilder.USER1_ID);

		final var etf = this.etfService.getEtfById(userId, etfId);
		Assertions.assertEquals(EtfTransportBuilder.NEXT_ID, etf.getId().getId());
		Assertions.assertEquals(EtfTransportBuilder.NEW_ETFNAME, etf.getName());
	}

	@Override
	protected void callUsecaseExpect403ForThisUsecase() throws Exception {
		super.callUsecaseExpect403(new EtfTransport());
	}

	@Override
	protected void callUsecaseEmptyDatabase() throws Exception {

		final EtfTransport transport = new EtfTransportBuilder().forNewEtf().build();

		super.callUsecaseExpect204(transport);

		final var userId = new UserID(UserTransportBuilder.ADMIN_ID);
		final var etfId = new EtfID(1L);

		final var etf = this.etfService.getEtfById(userId, etfId);
		Assertions.assertNotNull(etf);
	}
}