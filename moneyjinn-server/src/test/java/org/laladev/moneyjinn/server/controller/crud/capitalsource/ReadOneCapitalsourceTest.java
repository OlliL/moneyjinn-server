
package org.laladev.moneyjinn.server.controller.crud.capitalsource;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.server.builder.CapitalsourceTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.model.CapitalsourceTransport;
import org.springframework.test.context.jdbc.Sql;

class ReadOneCapitalsourceTest extends AbstractCapitalsourceTest {

	@Override
	protected void loadMethod() {
		super.getMock().readOne(null);
	}

	@Test
	void test_HappyCase_ResponseObject() throws Exception {
		final CapitalsourceTransport expected = new CapitalsourceTransportBuilder().forCapitalsource1().build();

		final CapitalsourceTransport actual = super.callUsecaseExpect200(CapitalsourceTransport.class,
				CapitalsourceTransportBuilder.CAPITALSOURCE1_ID);

		Assertions.assertEquals(expected, actual);
	}

	@Test
	void test_notExisting_NotFoundRaised() throws Exception {
		super.callUsecaseExpect404(CapitalsourceTransportBuilder.NON_EXISTING_ID);
	}

	@Test
	void test_ImportRoleNotAllowed_ErrorResponse() throws Exception {
		super.setUsername(UserTransportBuilder.IMPORTUSER_NAME);
		super.setPassword(UserTransportBuilder.IMPORTUSER_PASSWORD);

		super.callUsecaseExpect403WithUriVariables(CapitalsourceTransportBuilder.NON_EXISTING_ID);
	}

	@Test
	void test_AuthorizationRequired_Error() throws Exception {
		super.setUsername(null);
		super.setPassword(null);

		super.callUsecaseExpect403WithUriVariables(CapitalsourceTransportBuilder.NON_EXISTING_ID);
	}

	@Test
	@Sql("classpath:h2defaults.sql")
	void test_emptyDatabase_noException() throws Exception {
		super.setUsername(UserTransportBuilder.ADMIN_NAME);
		super.setPassword(UserTransportBuilder.ADMIN_PASSWORD);

		super.callUsecaseExpect404(CapitalsourceTransportBuilder.NON_EXISTING_ID);
	}
}
