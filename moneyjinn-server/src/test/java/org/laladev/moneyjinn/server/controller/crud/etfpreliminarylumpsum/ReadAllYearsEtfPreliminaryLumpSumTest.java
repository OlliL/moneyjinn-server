package org.laladev.moneyjinn.server.controller.crud.etfpreliminarylumpsum;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.server.builder.EtfPreliminaryLumpSumTransportBuilder;
import org.laladev.moneyjinn.server.builder.EtfTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;

class ReadAllYearsEtfPreliminaryLumpSumTest extends AbstractEtfPreliminaryLumpSumTest {

	@Override
	protected void loadMethod() {
		super.getMock().readAllYears(null);
	}

	@Test
	void test_HappyCase_ResponseObject() throws Exception {
		final Integer[] expected = { EtfPreliminaryLumpSumTransportBuilder.YEAR_2009,
				EtfPreliminaryLumpSumTransportBuilder.YEAR_2010 };
		final Integer[] actual = super.callUsecaseExpect200(Integer[].class, EtfTransportBuilder.ETF_ID_1);

		Assertions.assertNotNull(actual);
		Assertions.assertArrayEquals(expected, actual);

	}

	@Test
	void test_notExisting_NotFoundRaised() throws Exception {
		super.callUsecaseExpect404(EtfTransportBuilder.NON_EXISTING_ID);
	}

	@Test
	void test_etfFromOtherGroup_NotFoundRaised() throws Exception {
		super.setUsername(UserTransportBuilder.ADMIN_NAME);
		super.setPassword(UserTransportBuilder.ADMIN_PASSWORD);
		super.callUsecaseExpect404(EtfTransportBuilder.ETF_ID_1);
	}

	@Override
	protected void callUsecaseExpect403ForThisUsecase() throws Exception {
		super.callUsecaseExpect403WithUriVariables(EtfTransportBuilder.ETF_ID_1);
	}

	@Override
	protected void callUsecaseEmptyDatabase() throws Exception {
		super.callUsecaseExpect404(EtfTransportBuilder.ETF_ID_1);
	}

}
