package org.laladev.moneyjinn.server.controller.moneyflow;

import java.util.List;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.laladev.moneyjinn.core.rest.model.ErrorResponse;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.moneyflow.Moneyflow;
import org.laladev.moneyjinn.model.moneyflow.MoneyflowID;
import org.laladev.moneyjinn.model.moneyflow.MoneyflowReceipt;
import org.laladev.moneyjinn.model.moneyflow.MoneyflowSplitEntry;
import org.laladev.moneyjinn.server.builder.MoneyflowTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.laladev.moneyjinn.service.api.IMoneyflowReceiptService;
import org.laladev.moneyjinn.service.api.IMoneyflowService;
import org.laladev.moneyjinn.service.api.IMoneyflowSplitEntryService;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.jdbc.Sql;

public class DeleteMoneyflowByIdTest extends AbstractControllerTest {

	@Inject
	private IMoneyflowService moneyflowService;
	@Inject
	private IMoneyflowSplitEntryService moneyflowSplitEntryService;
	@Inject
	private IMoneyflowReceiptService moneyflowReceiptService;

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
	public void test_regularMoneyflow_SuccessfullNoContent() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final MoneyflowID moneyflowId = new MoneyflowID(MoneyflowTransportBuilder.MONEYFLOW1_ID);

		Moneyflow moneyflow = this.moneyflowService.getMoneyflowById(userId, moneyflowId);

		Assert.assertNotNull(moneyflow);

		super.callUsecaseWithoutContent("/" + MoneyflowTransportBuilder.MONEYFLOW1_ID, this.method, true, Object.class);

		// Validate that everything was deleted (and do not rely on foreign key constraints)
		moneyflow = this.moneyflowService.getMoneyflowById(userId, moneyflowId);
		Assert.assertNull(moneyflow);

		final MoneyflowReceipt moneyflowReceipt = this.moneyflowReceiptService.getMoneyflowReceipt(userId, moneyflowId);
		Assert.assertNull(moneyflowReceipt);

		final List<MoneyflowSplitEntry> moneyflowSplitEntries = this.moneyflowSplitEntryService
				.getMoneyflowSplitEntries(userId, moneyflowId);
		Assert.assertTrue(moneyflowSplitEntries.isEmpty());
	}

	@Test
	public void test_nonExistingMoneyflow_SuccessfullNoContent() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final MoneyflowID moneyflowId = new MoneyflowID(MoneyflowTransportBuilder.NON_EXISTING_ID);

		Moneyflow moneyflow = this.moneyflowService.getMoneyflowById(userId, moneyflowId);

		Assert.assertNull(moneyflow);

		super.callUsecaseWithoutContent("/" + MoneyflowTransportBuilder.NON_EXISTING_ID, this.method, true,
				Object.class);

		moneyflow = this.moneyflowService.getMoneyflowById(userId, moneyflowId);

		Assert.assertNull(moneyflow);
	}

	@Test
	public void test_MoneyflowOwnedBySomeoneElse_noDeletionHappend() throws Exception {
		this.userName = UserTransportBuilder.USER3_NAME;
		this.userPassword = UserTransportBuilder.USER3_PASSWORD;
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final MoneyflowID moneyflowId = new MoneyflowID(MoneyflowTransportBuilder.MONEYFLOW1_ID);

		Moneyflow moneyflow = this.moneyflowService.getMoneyflowById(userId, moneyflowId);

		Assert.assertNotNull(moneyflow);

		super.callUsecaseWithoutContent("/" + MoneyflowTransportBuilder.MONEYFLOW1_ID, this.method, true, Object.class);

		moneyflow = this.moneyflowService.getMoneyflowById(userId, moneyflowId);

		Assert.assertNotNull(moneyflow);
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
		super.callUsecaseWithoutContent("/" + MoneyflowTransportBuilder.MONEYFLOW1_ID, this.method, true, Object.class);
	}

}