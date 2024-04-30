
package org.laladev.moneyjinn.server.controller.crud.etfpreliminarylumpsum;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.model.etf.EtfID;
import org.laladev.moneyjinn.model.etf.EtfPreliminaryLumpSumID;
import org.laladev.moneyjinn.model.etf.EtfPreliminaryLumpSumIDValues;
import org.laladev.moneyjinn.server.builder.EtfPreliminaryLumpSumTransportBuilder;
import org.laladev.moneyjinn.server.builder.EtfTransportBuilder;
import org.laladev.moneyjinn.server.builder.ValidationItemTransportBuilder;
import org.laladev.moneyjinn.server.model.ErrorResponse;
import org.laladev.moneyjinn.server.model.EtfPreliminaryLumpSumTransport;
import org.laladev.moneyjinn.server.model.ValidationItemTransport;
import org.laladev.moneyjinn.server.model.ValidationResponse;
import org.laladev.moneyjinn.service.api.IEtfService;

import jakarta.inject.Inject;

class CreateEtfPreliminaryLumpSumTest extends AbstractEtfPreliminaryLumpSumTest {
	@Inject
	private IEtfService etfService;

	@Override
	protected void loadMethod() {
		super.getMock().create(null, null);
	}

	private void testError(final EtfPreliminaryLumpSumTransport transport, final ErrorCode errorCode) throws Exception {
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
		final EtfPreliminaryLumpSumTransport transport = new EtfPreliminaryLumpSumTransportBuilder().forNewYear()
				.build();

		final var etfId = new EtfID(EtfTransportBuilder.ETF_ID_1);
		final var year = Year.of(EtfPreliminaryLumpSumTransportBuilder.NEW_YEAR);
		final EtfPreliminaryLumpSumID id = new EtfPreliminaryLumpSumID(
				new EtfPreliminaryLumpSumIDValues(etfId, year));

		var etfPreliminaryLumpSum = this.etfService.getEtfPreliminaryLumpSum(id);
		Assertions.assertNull(etfPreliminaryLumpSum);

		super.callUsecaseExpect204Minimal(transport);

		etfPreliminaryLumpSum = this.etfService.getEtfPreliminaryLumpSum(id);
		Assertions.assertNotNull(etfPreliminaryLumpSum);
	}

	@Test
	void test_standardRequest_Successfull_RepresentationReturn() throws Exception {
		final EtfPreliminaryLumpSumTransport transport = new EtfPreliminaryLumpSumTransportBuilder().forNewYear()
				.build();

		final var etfId = new EtfID(EtfTransportBuilder.ETF_ID_1);
		final var year = Year.of(EtfPreliminaryLumpSumTransportBuilder.NEW_YEAR);
		final EtfPreliminaryLumpSumID id = new EtfPreliminaryLumpSumID(
				new EtfPreliminaryLumpSumIDValues(etfId, year));

		var etfPreliminaryLumpSum = this.etfService.getEtfPreliminaryLumpSum(id);
		Assertions.assertNull(etfPreliminaryLumpSum);

		final EtfPreliminaryLumpSumTransport actualTransport = super.callUsecaseExpect200Representation(transport,
				EtfPreliminaryLumpSumTransport.class);

		Assertions.assertEquals(transport, actualTransport);

		etfPreliminaryLumpSum = this.etfService.getEtfPreliminaryLumpSum(id);
		Assertions.assertNotNull(etfPreliminaryLumpSum);
	}

	@Test
	void test_standardRequest_Successfull_DefaultReturn() throws Exception {
		final EtfPreliminaryLumpSumTransport transport = new EtfPreliminaryLumpSumTransportBuilder().forNewYear()
				.build();

		final var etfId = new EtfID(EtfTransportBuilder.ETF_ID_1);
		final var year = Year.of(EtfPreliminaryLumpSumTransportBuilder.NEW_YEAR);
		final EtfPreliminaryLumpSumID id = new EtfPreliminaryLumpSumID(
				new EtfPreliminaryLumpSumIDValues(etfId, year));

		var etfPreliminaryLumpSum = this.etfService.getEtfPreliminaryLumpSum(id);
		Assertions.assertNull(etfPreliminaryLumpSum);

		super.callUsecaseExpect204(transport);

		etfPreliminaryLumpSum = this.etfService.getEtfPreliminaryLumpSum(id);
		Assertions.assertNotNull(etfPreliminaryLumpSum);
	}

	@Test
	void test_EtfPreliminaryLumpSumNonExistingEtfId_Error() throws Exception {
		final EtfPreliminaryLumpSumTransport transport = new EtfPreliminaryLumpSumTransportBuilder().forNewYear()
				.build();
		transport.setEtfId(EtfTransportBuilder.NON_EXISTING_ID);
		this.testError(transport, ErrorCode.NO_ETF_SPECIFIED);
	}

	@Test
	void test_EtfPreliminaryLumpSumNonEmptyEtfId_Error() throws Exception {
		final EtfPreliminaryLumpSumTransport transport = new EtfPreliminaryLumpSumTransportBuilder().forNewYear()
				.build();
		transport.setEtfId(null);
		this.testError(transport, ErrorCode.NO_ETF_SPECIFIED);
	}

	@Test
	void test_EtfPreliminaryLumpSumAlreadyExisting_Error() throws Exception {
		final EtfPreliminaryLumpSumTransport transport = new EtfPreliminaryLumpSumTransportBuilder().for2009().build();
		final ErrorResponse actual = super.callUsecaseExpect400(transport, ErrorResponse.class);
		Assertions.assertEquals(ErrorCode.ETF_PRELIMINARY_LUMP_SUM_ALREADY_EXISTS.getErrorCode(), actual.getCode());
	}

	@Override
	protected void callUsecaseExpect403ForThisUsecase() throws Exception {
		super.callUsecaseExpect403(new EtfPreliminaryLumpSumTransport());
	}

	@Override
	protected void callUsecaseEmptyDatabase() throws Exception {

		final EtfPreliminaryLumpSumTransport transport = new EtfPreliminaryLumpSumTransportBuilder().forNewYear()
				.build();

		super.callUsecaseExpect422(transport, ValidationResponse.class);
	}
}