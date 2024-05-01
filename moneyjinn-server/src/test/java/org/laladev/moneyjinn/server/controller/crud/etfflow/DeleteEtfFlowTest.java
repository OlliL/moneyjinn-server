
package org.laladev.moneyjinn.server.controller.crud.etfflow;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.etf.EtfFlowID;
import org.laladev.moneyjinn.server.builder.EtfFlowTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.service.api.IEtfService;

import jakarta.inject.Inject;

class DeleteEtfFlowTest extends AbstractEtfFlowTest {
	@Inject
	private IEtfService etfService;

	@Override
	protected void loadMethod() {
		super.getMock().delete(null);
	}

	@Test
	void test_standardRequest_Succesful() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final var etfFlowId = new EtfFlowID(EtfFlowTransportBuilder.ETF_FLOW_10ID);
		var etfFlow = this.etfService.getEtfFlowById(userId, etfFlowId);
		Assertions.assertNotNull(etfFlow);

		super.callUsecaseExpect204WithUriVariables(EtfFlowTransportBuilder.ETF_FLOW_10ID);

		etfFlow = this.etfService.getEtfFlowById(userId, etfFlowId);
		Assertions.assertNull(etfFlow);
	}

	@Test
	void test_notExisting_Succesful() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final var etfFlowId = new EtfFlowID(EtfFlowTransportBuilder.NON_EXISTING_ID);
		var etfFlow = this.etfService.getEtfFlowById(userId, etfFlowId);
		Assertions.assertNull(etfFlow);

		super.callUsecaseExpect204WithUriVariables(EtfFlowTransportBuilder.NON_EXISTING_ID);

		etfFlow = this.etfService.getEtfFlowById(userId, etfFlowId);
		Assertions.assertNull(etfFlow);
	}

	@Override
	protected void callUsecaseExpect403ForThisUsecase() throws Exception {
		super.callUsecaseExpect403WithUriVariables(EtfFlowTransportBuilder.NON_EXISTING_ID);
	}

	@Override
	protected void callUsecaseEmptyDatabase() throws Exception {
		super.callUsecaseExpect204WithUriVariables(EtfFlowTransportBuilder.NON_EXISTING_ID);
	}
}