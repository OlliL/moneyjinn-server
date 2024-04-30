
package org.laladev.moneyjinn.server.controller.crud.etf;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.server.builder.EtfTransportBuilder;
import org.laladev.moneyjinn.server.model.EtfTransport;

class ReadOneEtfTest extends AbstractEtfTest {

	@Override
	protected void loadMethod() {
		super.getMock().readOne(null);
	}

	@Test
	void test_HappyCase_ResponseObject() throws Exception {
		final EtfTransport expected = new EtfTransportBuilder().forEtf1().build();

		final EtfTransport actual = super.callUsecaseExpect200(EtfTransport.class, EtfTransportBuilder.ETF_ID_1);

		Assertions.assertEquals(expected, actual);
	}

	@Test
	void test_notExisting_NotFoundRaised() throws Exception {
		super.callUsecaseExpect404(EtfTransportBuilder.NON_EXISTING_ID);
	}

	@Override
	protected void callUsecaseExpect403ForThisUsecase() throws Exception {
		super.callUsecaseExpect403WithUriVariables(EtfTransportBuilder.NON_EXISTING_ID);
	}

	@Override
	protected void callUsecaseEmptyDatabase() throws Exception {
		super.callUsecaseExpect404(EtfTransportBuilder.NON_EXISTING_ID);
	}

}
