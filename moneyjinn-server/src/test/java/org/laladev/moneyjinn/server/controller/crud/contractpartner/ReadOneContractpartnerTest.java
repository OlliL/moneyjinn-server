
package org.laladev.moneyjinn.server.controller.crud.contractpartner;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.server.builder.ContractpartnerTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.model.ContractpartnerTransport;
import org.springframework.test.context.jdbc.Sql;

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

	@Test
	void test_AuthorizationRequired_Error() throws Exception {
		super.setUsername(null);
		super.setPassword(null);

		super.callUsecaseExpect403WithUriVariables(ContractpartnerTransportBuilder.NON_EXISTING_ID);
	}

	@Test
	@Sql("classpath:h2defaults.sql")
	void test_emptyDatabase_noException() throws Exception {
		super.setUsername(UserTransportBuilder.ADMIN_NAME);
		super.setPassword(UserTransportBuilder.ADMIN_PASSWORD);

		super.callUsecaseExpect404(ContractpartnerTransportBuilder.NON_EXISTING_ID);
	}
}
