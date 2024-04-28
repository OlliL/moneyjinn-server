package org.laladev.moneyjinn.server.controller.crud.etf;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.server.builder.EtfPreliminaryLumpSumTransportBuilder;
import org.laladev.moneyjinn.server.builder.EtfTransportBuilder;
import org.laladev.moneyjinn.server.model.EtfPreliminaryLumpSumTransport;

public class ReadOneEtfPreliminaryLumpSumTest extends AbstractEtfPreliminaryLumpSumTest {

	@Override
	protected void loadMethod() {
		super.getMock().readOne(null, null);
	}

	@Test
	void test_HappyCase_ResponseObject() throws Exception {
		final EtfPreliminaryLumpSumTransport expected = new EtfPreliminaryLumpSumTransportBuilder().for2009().build();
		final EtfPreliminaryLumpSumTransport actual = super.callUsecaseExpect200(EtfPreliminaryLumpSumTransport.class,
				EtfTransportBuilder.ISIN, 2009);

		Assertions.assertNotNull(actual);
		Assertions.assertEquals(expected, actual);

	}

	@Test
	void test_notExisting_NotFoundRaised() throws Exception {
		super.callUsecaseExpect404(EtfTransportBuilder.ISIN, EtfPreliminaryLumpSumTransportBuilder.NON_EXISTING_YEAR);
	}

	@Override
	protected void callUsecaseExpect403ForThisUsecase() throws Exception {
		super.callUsecaseExpect403WithUriVariables(EtfTransportBuilder.ISIN,
				EtfPreliminaryLumpSumTransportBuilder.YEAR_2009);
	}

	@Override
	protected void callUsecaseEmptyDatabase() throws Exception {
		super.callUsecaseExpect404(EtfTransportBuilder.ISIN, EtfPreliminaryLumpSumTransportBuilder.YEAR_2009);
	}

}
