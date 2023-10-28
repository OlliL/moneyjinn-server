
package org.laladev.moneyjinn.server.controller.predefmoneyflow;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.model.PreDefMoneyflow;
import org.laladev.moneyjinn.model.PreDefMoneyflowID;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.server.builder.PreDefMoneyflowTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.laladev.moneyjinn.server.controller.api.PreDefMoneyflowControllerApi;
import org.laladev.moneyjinn.service.api.IPreDefMoneyflowService;
import org.springframework.test.context.jdbc.Sql;

import jakarta.inject.Inject;

class DeletePreDefMoneyflowByIdTest extends AbstractControllerTest {
	@Inject
	private IPreDefMoneyflowService preDefMoneyflowService;

	private String userName;
	private String userPassword;

	@BeforeEach
	public void setUp() {
		this.userName = UserTransportBuilder.USER1_NAME;
		this.userPassword = UserTransportBuilder.USER1_PASSWORD;
	}

	@Override
	protected String getUsername() {
		return this.userName;
	}

	@Override
	protected String getPassword() {
		return this.userPassword;
	}

	@Override
	protected void loadMethod() {
		super.getMock(PreDefMoneyflowControllerApi.class).deletePreDefMoneyflowById(null);
	}

	@Test
	void test_regularPreDefMoneyflow_SuccessfullNoContent() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final PreDefMoneyflowID preDefMoneyflowId = new PreDefMoneyflowID(
				PreDefMoneyflowTransportBuilder.PRE_DEF_MONEYFLOW1_ID);
		PreDefMoneyflow preDefMoneyflow = this.preDefMoneyflowService.getPreDefMoneyflowById(userId, preDefMoneyflowId);
		Assertions.assertNotNull(preDefMoneyflow);

		super.callUsecaseExpect204WithUriVariables(PreDefMoneyflowTransportBuilder.PRE_DEF_MONEYFLOW1_ID);

		preDefMoneyflow = this.preDefMoneyflowService.getPreDefMoneyflowById(userId, preDefMoneyflowId);
		Assertions.assertNull(preDefMoneyflow);
	}

	@Test
	void test_nonExistingPreDefMoneyflow_SuccessfullNoContent() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final PreDefMoneyflowID preDefMoneyflowId = new PreDefMoneyflowID(
				PreDefMoneyflowTransportBuilder.NON_EXISTING_ID);
		PreDefMoneyflow preDefMoneyflow = this.preDefMoneyflowService.getPreDefMoneyflowById(userId, preDefMoneyflowId);
		Assertions.assertNull(preDefMoneyflow);

		super.callUsecaseExpect204WithUriVariables(PreDefMoneyflowTransportBuilder.NON_EXISTING_ID);

		preDefMoneyflow = this.preDefMoneyflowService.getPreDefMoneyflowById(userId, preDefMoneyflowId);
		Assertions.assertNull(preDefMoneyflow);
	}

	@Test
	void test_PreDefMoneyflowOwnedBySomeoneElse_noDeletionHappend() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER3_ID);
		final PreDefMoneyflowID preDefMoneyflowId = new PreDefMoneyflowID(
				PreDefMoneyflowTransportBuilder.PRE_DEF_MONEYFLOW2_ID);
		PreDefMoneyflow preDefMoneyflow = this.preDefMoneyflowService.getPreDefMoneyflowById(userId, preDefMoneyflowId);
		Assertions.assertNotNull(preDefMoneyflow);

		super.callUsecaseExpect204WithUriVariables(PreDefMoneyflowTransportBuilder.PRE_DEF_MONEYFLOW2_ID);

		preDefMoneyflow = this.preDefMoneyflowService.getPreDefMoneyflowById(userId, preDefMoneyflowId);
		Assertions.assertNotNull(preDefMoneyflow);
	}

	@Test
	void test_ImportRoleNotAllowed_ErrorResponse() throws Exception {
		this.userName = UserTransportBuilder.IMPORTUSER_NAME;
		this.userPassword = UserTransportBuilder.IMPORTUSER_PASSWORD;

		super.callUsecaseExpect403WithUriVariables(PreDefMoneyflowTransportBuilder.PRE_DEF_MONEYFLOW2_ID);
	}

	@Test
	void test_AuthorizationRequired_Error() throws Exception {
		this.userName = null;
		this.userPassword = null;

		super.callUsecaseExpect403WithUriVariables(PreDefMoneyflowTransportBuilder.PRE_DEF_MONEYFLOW2_ID);
	}

	@Test
	@Sql("classpath:h2defaults.sql")
	void test_emptyDatabase_noException() throws Exception {
		this.userName = UserTransportBuilder.ADMIN_NAME;
		this.userPassword = UserTransportBuilder.ADMIN_PASSWORD;

		super.callUsecaseExpect204WithUriVariables(PreDefMoneyflowTransportBuilder.PRE_DEF_MONEYFLOW2_ID);
	}
}