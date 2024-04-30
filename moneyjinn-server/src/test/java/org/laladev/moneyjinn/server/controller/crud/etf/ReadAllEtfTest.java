
package org.laladev.moneyjinn.server.controller.crud.etf;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.server.builder.EtfTransportBuilder;
import org.laladev.moneyjinn.server.model.EtfTransport;

class ReadAllEtfTest extends AbstractEtfTest {

	@Override
	protected void loadMethod() {
		super.getMock().readAll();
	}

	private List<EtfTransport> getCompleteResponse() {
		final List<EtfTransport> etfTransports = new ArrayList<>();
		etfTransports.add(new EtfTransportBuilder().forEtf1().build());
		etfTransports.add(new EtfTransportBuilder().forEtf3().build());
		etfTransports.add(new EtfTransportBuilder().forEtf4().build());
		return etfTransports;
	}

	@Test
	void test_FullResponseObject() throws Exception {
		final List<EtfTransport> expected = this.getCompleteResponse();

		final EtfTransport[] actual = super.callUsecaseExpect200(EtfTransport[].class);

		Assertions.assertArrayEquals(expected.toArray(), actual);

	}

	@Override
	protected void callUsecaseExpect403ForThisUsecase() throws Exception {
		super.callUsecaseExpect403();
	}

	@Override
	protected void callUsecaseEmptyDatabase() throws Exception {
		final EtfTransport[] expected = {};
		final EtfTransport[] actual = super.callUsecaseExpect200(EtfTransport[].class);

		Assertions.assertArrayEquals(expected, actual);
	}
}
