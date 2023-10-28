package org.laladev.moneyjinn.server.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.springframework.test.context.jdbc.Sql;

public abstract class AbstractWebUserControllerTest extends AbstractControllerTest {

	abstract protected void callUsecaseExpect403ForThisUsecase() throws Exception;

	abstract protected void callUsecaseEmptyDatabase() throws Exception;

	@BeforeEach
	public void setUp() {
		super.setUsername(UserTransportBuilder.USER1_NAME);
		super.setPassword(UserTransportBuilder.USER1_PASSWORD);
	}

	@Test
	void test_ImportRoleNotAllowed_ErrorResponse() throws Exception {
		super.setUsername(UserTransportBuilder.IMPORTUSER_NAME);
		super.setPassword(UserTransportBuilder.IMPORTUSER_PASSWORD);

		this.callUsecaseExpect403ForThisUsecase();
	}

	@Test
	void test_AuthorizationRequired_Error() throws Exception {
		super.setUsername(null);
		super.setPassword(null);

		this.callUsecaseExpect403ForThisUsecase();
	}

	@Test
	@Sql("classpath:h2defaults.sql")
	void test_emptyDatabase_noException() throws Exception {
		super.setUsername(UserTransportBuilder.ADMIN_NAME);
		super.setPassword(UserTransportBuilder.ADMIN_PASSWORD);

		this.callUsecaseEmptyDatabase();
	}
}
