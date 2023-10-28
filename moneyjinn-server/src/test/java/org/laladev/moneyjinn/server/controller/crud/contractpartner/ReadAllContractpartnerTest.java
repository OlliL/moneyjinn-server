
package org.laladev.moneyjinn.server.controller.crud.contractpartner;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.server.builder.ContractpartnerTransportBuilder;
import org.laladev.moneyjinn.server.model.ContractpartnerTransport;

class ReadAllContractpartnerTest extends AbstractContractpartnerTest {

	@Override
	protected void loadMethod() {
		super.getMock().readAll();
	}

	private List<ContractpartnerTransport> getCompleteResponse() {
		final List<ContractpartnerTransport> contractpartnerTransports = new ArrayList<>();
		contractpartnerTransports.add(new ContractpartnerTransportBuilder().forContractpartner1().build());
		contractpartnerTransports.add(new ContractpartnerTransportBuilder().forContractpartner2().build());
		contractpartnerTransports.add(new ContractpartnerTransportBuilder().forContractpartner3().build());
		contractpartnerTransports.add(new ContractpartnerTransportBuilder().forContractpartner4().build());
		return contractpartnerTransports;
	}

	@Test
	void test_default_FullResponseObject() throws Exception {
		final List<ContractpartnerTransport> expected = this.getCompleteResponse();

		final ContractpartnerTransport[] actual = super.callUsecaseExpect200(ContractpartnerTransport[].class);

		Assertions.assertArrayEquals(expected.toArray(), actual);
	}

	@Override
	protected void callUsecaseExpect403ForThisUsecase() throws Exception {
		super.callUsecaseExpect403();
	}

	@Override
	protected void callUsecaseEmptyDatabase() throws Exception {
		final ContractpartnerTransport[] expected = {};
		final ContractpartnerTransport[] actual = super.callUsecaseExpect200(ContractpartnerTransport[].class);

		Assertions.assertArrayEquals(expected, actual);
	}
}
