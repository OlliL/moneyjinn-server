
package org.laladev.moneyjinn.server.controller.predefmoneyflow;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.model.PreDefMoneyflow;
import org.laladev.moneyjinn.model.PreDefMoneyflowID;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.server.builder.PreDefMoneyflowTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractWebUserControllerTest;
import org.laladev.moneyjinn.server.controller.api.PreDefMoneyflowControllerApi;
import org.laladev.moneyjinn.service.api.IPreDefMoneyflowService;

import jakarta.inject.Inject;

class DeletePreDefMoneyflowByIdTest extends AbstractWebUserControllerTest {
	@Inject
	private IPreDefMoneyflowService preDefMoneyflowService;

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

	@Override
	protected void callUsecaseExpect403ForThisUsecase() throws Exception {
		super.callUsecaseExpect403WithUriVariables(PreDefMoneyflowTransportBuilder.PRE_DEF_MONEYFLOW2_ID);
	}

	@Override
	protected void callUsecaseEmptyDatabase() throws Exception {
		super.callUsecaseExpect204WithUriVariables(PreDefMoneyflowTransportBuilder.PRE_DEF_MONEYFLOW2_ID);
	}
}