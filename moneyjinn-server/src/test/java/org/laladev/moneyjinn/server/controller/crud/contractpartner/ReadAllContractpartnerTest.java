
package org.laladev.moneyjinn.server.controller.crud.contractpartner;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.server.builder.ContractpartnerTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.model.ContractpartnerTransport;
import org.springframework.test.context.jdbc.Sql;

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

	@Test
	void test_ImportRoleNotAllowed_ErrorResponse() throws Exception {
		super.setUsername(UserTransportBuilder.IMPORTUSER_NAME);
		super.setPassword(UserTransportBuilder.IMPORTUSER_PASSWORD);

		super.callUsecaseExpect403();
	}

	@Test
	void test_AuthorizationRequired1_Error() throws Exception {
		super.setUsername(null);
		super.setPassword(null);

		super.callUsecaseExpect403();
	}

	@Test
	@Sql("classpath:h2defaults.sql")
	void test_emptyDatabase_noException() throws Exception {
		super.setUsername(UserTransportBuilder.ADMIN_NAME);
		super.setPassword(UserTransportBuilder.ADMIN_PASSWORD);

		final ContractpartnerTransport[] expected = {};
		final ContractpartnerTransport[] actual = super.callUsecaseExpect200(ContractpartnerTransport[].class);

		Assertions.assertArrayEquals(expected, actual);
	}
}
