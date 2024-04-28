
package org.laladev.moneyjinn.server.controller.crud.etf;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.model.etf.EtfIsin;
import org.laladev.moneyjinn.model.etf.EtfPreliminaryLumpSum;
import org.laladev.moneyjinn.server.builder.EtfPreliminaryLumpSumTransportBuilder;
import org.laladev.moneyjinn.server.builder.EtfTransportBuilder;
import org.laladev.moneyjinn.server.builder.ValidationItemTransportBuilder;
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
		validationItems.add(new ValidationItemTransportBuilder().withKey(transport.getIsin())
				.withError(errorCode.getErrorCode()).build());
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

		super.callUsecaseExpect204Minimal(transport);

		final var id = new EtfIsin(EtfTransportBuilder.ISIN);
		final var year = Year.of(EtfPreliminaryLumpSumTransportBuilder.NEW_YEAR);
		final EtfPreliminaryLumpSum etfPreliminaryLumpSum = this.etfService.getEtfPreliminaryLumpSum(id, year);
		Assertions.assertEquals(id, etfPreliminaryLumpSum.getId());
		Assertions.assertEquals(year, etfPreliminaryLumpSum.getYear());
	}

	@Test
	void test_standardRequest_Successfull_RepresentationReturn() throws Exception {
		final EtfPreliminaryLumpSumTransport transport = new EtfPreliminaryLumpSumTransportBuilder().forNewYear()
				.build();

		final EtfPreliminaryLumpSumTransport actualTransport = super.callUsecaseExpect200Representation(transport,
				EtfPreliminaryLumpSumTransport.class);

		Assertions.assertEquals(transport, actualTransport);

		final var id = new EtfIsin(EtfTransportBuilder.ISIN);
		final var year = Year.of(EtfPreliminaryLumpSumTransportBuilder.NEW_YEAR);
		final EtfPreliminaryLumpSum etfPreliminaryLumpSum = this.etfService.getEtfPreliminaryLumpSum(id, year);
		Assertions.assertEquals(id, etfPreliminaryLumpSum.getId());
		Assertions.assertEquals(year, etfPreliminaryLumpSum.getYear());
	}

	@Test
	void test_standardRequest_Successfull_DefaultReturn() throws Exception {
		final EtfPreliminaryLumpSumTransport transport = new EtfPreliminaryLumpSumTransportBuilder().forNewYear()
				.build();

		super.callUsecaseExpect204(transport);

		final var id = new EtfIsin(EtfTransportBuilder.ISIN);
		final var year = Year.of(EtfPreliminaryLumpSumTransportBuilder.NEW_YEAR);
		final EtfPreliminaryLumpSum etfPreliminaryLumpSum = this.etfService.getEtfPreliminaryLumpSum(id, year);
		Assertions.assertEquals(id, etfPreliminaryLumpSum.getId());
		Assertions.assertEquals(year, etfPreliminaryLumpSum.getYear());
	}

	@Test
	void test_EtfPreliminaryLumpSumNonExistingIsin_Error() throws Exception {
		final EtfPreliminaryLumpSumTransport transport = new EtfPreliminaryLumpSumTransportBuilder().forNewYear()
				.build();
		transport.setIsin(EtfTransportBuilder.NON_EXISTING_ISIN);
		this.testError(transport, ErrorCode.NO_ETF_SPECIFIED);
	}

	@Test
	void test_EtfPreliminaryLumpSumNonEmptyIsin_Error() throws Exception {
		final EtfPreliminaryLumpSumTransport transport = new EtfPreliminaryLumpSumTransportBuilder().forNewYear()
				.build();
		transport.setIsin(null);
		this.testError(transport, ErrorCode.NO_ETF_SPECIFIED);
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