package org.laladev.moneyjinn.server.controller.predefmoneyflow;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.laladev.moneyjinn.core.rest.model.ErrorResponse;
import org.laladev.moneyjinn.model.PreDefMoneyflow;
import org.laladev.moneyjinn.model.PreDefMoneyflowID;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.server.builder.PreDefMoneyflowTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.laladev.moneyjinn.service.api.IPreDefMoneyflowService;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.jdbc.Sql;

public class DeletePreDefMoneyflowTest extends AbstractControllerTest {

	@Inject
	private IPreDefMoneyflowService preDefMoneyflowService;

	private final HttpMethod method = HttpMethod.DELETE;
	private String userName;
	private String userPassword;

	@Before
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
	protected String getUsecase() {
		return super.getUsecaseFromTestClassName(this.getClass());
	}

	@Test
	public void test_regularPreDefMoneyflow_SuccessfullNoContent() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final PreDefMoneyflowID preDefMoneyflowId = new PreDefMoneyflowID(
				PreDefMoneyflowTransportBuilder.PRE_DEF_MONEYFLOW1_ID);

		PreDefMoneyflow preDefMoneyflow = this.preDefMoneyflowService.getPreDefMoneyflowById(userId, preDefMoneyflowId);

		Assert.assertNotNull(preDefMoneyflow);

		super.callUsecaseWithoutContent("/" + PreDefMoneyflowTransportBuilder.PRE_DEF_MONEYFLOW1_ID, this.method, true,
				Object.class);

		preDefMoneyflow = this.preDefMoneyflowService.getPreDefMoneyflowById(userId, preDefMoneyflowId);

		Assert.assertNull(preDefMoneyflow);
	}

	@Test
	public void test_nonExistingPreDefMoneyflow_SuccessfullNoContent() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final PreDefMoneyflowID preDefMoneyflowId = new PreDefMoneyflowID(
				PreDefMoneyflowTransportBuilder.NON_EXISTING_ID);

		PreDefMoneyflow preDefMoneyflow = this.preDefMoneyflowService.getPreDefMoneyflowById(userId, preDefMoneyflowId);

		Assert.assertNull(preDefMoneyflow);

		super.callUsecaseWithoutContent("/" + PreDefMoneyflowTransportBuilder.NON_EXISTING_ID, this.method, true,
				Object.class);

		preDefMoneyflow = this.preDefMoneyflowService.getPreDefMoneyflowById(userId, preDefMoneyflowId);

		Assert.assertNull(preDefMoneyflow);
	}

	@Test
	public void test_PreDefMoneyflowOwnedBySomeoneElse_noDeletionHappend() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER3_ID);
		final PreDefMoneyflowID preDefMoneyflowId = new PreDefMoneyflowID(
				PreDefMoneyflowTransportBuilder.PRE_DEF_MONEYFLOW2_ID);

		PreDefMoneyflow preDefMoneyflow = this.preDefMoneyflowService.getPreDefMoneyflowById(userId, preDefMoneyflowId);

		Assert.assertNotNull(preDefMoneyflow);

		super.callUsecaseWithoutContent("/" + PreDefMoneyflowTransportBuilder.PRE_DEF_MONEYFLOW2_ID, this.method, true,
				Object.class);

		preDefMoneyflow = this.preDefMoneyflowService.getPreDefMoneyflowById(userId, preDefMoneyflowId);

		Assert.assertNotNull(preDefMoneyflow);
	}

	@Test
	public void test_AuthorizationRequired_Error() throws Exception {
		this.userName = null;
		this.userPassword = null;
		final ErrorResponse actual = super.callUsecaseWithoutContent("/1", this.method, false, ErrorResponse.class);
		Assert.assertEquals(super.accessDeniedErrorResponse(), actual);
	}

	@Test
	@Sql("classpath:h2defaults.sql")
	public void test_emptyDatabase_noException() throws Exception {
		this.userName = UserTransportBuilder.ADMIN_NAME;
		this.userPassword = UserTransportBuilder.ADMIN_PASSWORD;

		super.callUsecaseWithoutContent("/" + PreDefMoneyflowTransportBuilder.PRE_DEF_MONEYFLOW2_ID, this.method, true,
				Object.class);
	}

}