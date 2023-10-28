
package org.laladev.moneyjinn.server.controller.crud.contractpartner;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.server.builder.ContractpartnerTransportBuilder;
import org.laladev.moneyjinn.server.model.ContractpartnerTransport;

class ReadOneContractpartnerTest extends AbstractContractpartnerTest {

	@Override
	protected void loadMethod() {
		super.getMock().readOne(null);
	}

	@Test
	void test_HappyCase_ResponseObject() throws Exception {
		final ContractpartnerTransport expected = new ContractpartnerTransportBuilder().forContractpartner1().build();

		final ContractpartnerTransport actual = super.callUsecaseExpect200(ContractpartnerTransport.class,
				ContractpartnerTransportBuilder.CONTRACTPARTNER1_ID);

		Assertions.assertEquals(expected, actual);
	}

	@Test
	void test_notExisting_NotFoundRaised() throws Exception {
		super.callUsecaseExpect404(ContractpartnerTransportBuilder.NON_EXISTING_ID);
	}

	@Override
	protected void callUsecaseExpect403ForThisUsecase() throws Exception {
		super.callUsecaseExpect403WithUriVariables(ContractpartnerTransportBuilder.NON_EXISTING_ID);
	}

	@Override
	protected void callUsecaseEmptyDatabase() throws Exception {
		super.callUsecaseExpect404(ContractpartnerTransportBuilder.NON_EXISTING_ID);
	}
}
