
package org.laladev.moneyjinn.server.controller.crud.etf;

import java.time.Year;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.model.etf.EtfIsin;
import org.laladev.moneyjinn.model.etf.EtfPreliminaryLumpSumID;
import org.laladev.moneyjinn.model.etf.EtfPreliminaryLumpSumIDValues;
import org.laladev.moneyjinn.server.builder.EtfPreliminaryLumpSumTransportBuilder;
import org.laladev.moneyjinn.server.builder.EtfTransportBuilder;
import org.laladev.moneyjinn.service.api.IEtfService;

import jakarta.inject.Inject;

class DeletePreliminaryLumpSumTest extends AbstractEtfPreliminaryLumpSumTest {
	@Inject
	private IEtfService etfService;

	@Override
	protected void loadMethod() {
		super.getMock().delete(null, null);
	}

	@Test
	void test_happyCase_SuccessfullNoContent() throws Exception {
		super.callUsecaseExpect204WithUriVariables(EtfTransportBuilder.ISIN,
				EtfPreliminaryLumpSumTransportBuilder.YEAR_2009);

		final var etfIsin = new EtfIsin(EtfTransportBuilder.ISIN);
		final var year = Year.of(EtfPreliminaryLumpSumTransportBuilder.YEAR_2009);
		final EtfPreliminaryLumpSumID id = new EtfPreliminaryLumpSumID(
				new EtfPreliminaryLumpSumIDValues(etfIsin, year));
		final var etfPreliminaryLumpSum = this.etfService.getEtfPreliminaryLumpSum(id);

		Assertions.assertNull(etfPreliminaryLumpSum);
	}

	@Test
	void test_nonExisting_SuccessfullNoContent() throws Exception {
		super.callUsecaseExpect204WithUriVariables(EtfTransportBuilder.NON_EXISTING_ISIN,
				EtfPreliminaryLumpSumTransportBuilder.YEAR_2009);

		final var etfIsin = new EtfIsin(EtfTransportBuilder.NON_EXISTING_ISIN);
		final var year = Year.of(EtfPreliminaryLumpSumTransportBuilder.YEAR_2009);
		final EtfPreliminaryLumpSumID id = new EtfPreliminaryLumpSumID(
				new EtfPreliminaryLumpSumIDValues(etfIsin, year));
		final var etfPreliminaryLumpSum = this.etfService.getEtfPreliminaryLumpSum(id);

		Assertions.assertNull(etfPreliminaryLumpSum);
	}

	@Override
	protected void callUsecaseExpect403ForThisUsecase() throws Exception {
		super.callUsecaseExpect403WithUriVariables(EtfTransportBuilder.ISIN,
				EtfPreliminaryLumpSumTransportBuilder.YEAR_2009);
	}

	@Override
	protected void callUsecaseEmptyDatabase() throws Exception {
		super.callUsecaseExpect204WithUriVariables(EtfTransportBuilder.ISIN,
				EtfPreliminaryLumpSumTransportBuilder.YEAR_2009);
	}
}