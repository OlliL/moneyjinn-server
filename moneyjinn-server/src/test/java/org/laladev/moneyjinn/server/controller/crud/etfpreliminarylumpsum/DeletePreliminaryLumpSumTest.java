
package org.laladev.moneyjinn.server.controller.crud.etfpreliminarylumpsum;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.etf.EtfPreliminaryLumpSumID;
import org.laladev.moneyjinn.server.builder.EtfPreliminaryLumpSumTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.service.api.IEtfService;

import jakarta.inject.Inject;

class DeletePreliminaryLumpSumTest extends AbstractEtfPreliminaryLumpSumTest {
	@Inject
	private IEtfService etfService;

	@Override
	protected void loadMethod() {
		super.getMock().delete(null);
	}

	@Test
	void test_happyCase_SuccessfullNoContent() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final var id = new EtfPreliminaryLumpSumID(EtfPreliminaryLumpSumTransportBuilder.ID_2009);

		var etfPreliminaryLumpSum = this.etfService.getEtfPreliminaryLumpSum(userId, id);
		Assertions.assertNotNull(etfPreliminaryLumpSum);

		super.callUsecaseExpect204WithUriVariables(EtfPreliminaryLumpSumTransportBuilder.ID_2009);

		etfPreliminaryLumpSum = this.etfService.getEtfPreliminaryLumpSum(userId, id);
		Assertions.assertNull(etfPreliminaryLumpSum);
	}

	@Test
	void test_nonExisting_SuccessfullNoContent() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final var id = new EtfPreliminaryLumpSumID(EtfPreliminaryLumpSumTransportBuilder.NON_EXISTING_ID);

		final var etfPreliminaryLumpSum = this.etfService.getEtfPreliminaryLumpSum(userId, id);

		Assertions.assertNull(etfPreliminaryLumpSum);

		super.callUsecaseExpect204WithUriVariables(EtfPreliminaryLumpSumTransportBuilder.NON_EXISTING_ID);
	}

	@Test
	void test_etfFromOtherGroup_nothingHappens() throws Exception {
		super.setUsername(UserTransportBuilder.ADMIN_NAME);
		super.setPassword(UserTransportBuilder.ADMIN_PASSWORD);

		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final var id = new EtfPreliminaryLumpSumID(EtfPreliminaryLumpSumTransportBuilder.ID_2009);

		var etfPreliminaryLumpSum = this.etfService.getEtfPreliminaryLumpSum(userId, id);
		Assertions.assertNotNull(etfPreliminaryLumpSum);

		super.callUsecaseExpect204WithUriVariables(EtfPreliminaryLumpSumTransportBuilder.ID_2009);

		etfPreliminaryLumpSum = this.etfService.getEtfPreliminaryLumpSum(userId, id);
		Assertions.assertNotNull(etfPreliminaryLumpSum);
	}

	@Override
	protected void callUsecaseExpect403ForThisUsecase() throws Exception {
		super.callUsecaseExpect403WithUriVariables(EtfPreliminaryLumpSumTransportBuilder.ID_2009);
	}

	@Override
	protected void callUsecaseEmptyDatabase() throws Exception {
		super.callUsecaseExpect204WithUriVariables(EtfPreliminaryLumpSumTransportBuilder.ID_2009);
	}
}