
package org.laladev.moneyjinn.server.controller.etf;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.model.etf.EtfFlow;
import org.laladev.moneyjinn.model.etf.EtfFlowID;
import org.laladev.moneyjinn.server.builder.EtfFlowTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractWebUserControllerTest;
import org.laladev.moneyjinn.server.controller.api.EtfControllerApi;
import org.laladev.moneyjinn.service.api.IEtfService;

import jakarta.inject.Inject;

class DeleteEtfFlowTest extends AbstractWebUserControllerTest {
	@Inject
	private IEtfService etfService;

	@Override
	protected void loadMethod() {
		super.getMock(EtfControllerApi.class).deleteEtfFlow(null);
	}

	@Test
	void test_standardRequest_emptyResponse() throws Exception {
		final EtfFlowID etfFlowId = new EtfFlowID(EtfFlowTransportBuilder.ETF_FLOW_1ID);

		EtfFlow etfFlow = this.etfService.getEtfFlowById(etfFlowId);
		Assertions.assertNotNull(etfFlow);

		super.callUsecaseExpect204WithUriVariables(EtfFlowTransportBuilder.ETF_FLOW_1ID);

		etfFlow = this.etfService.getEtfFlowById(etfFlowId);
		Assertions.assertNull(etfFlow);
	}

	@Test
	void test_DeleteNotExistingId_emptyResponse() throws Exception {
		super.callUsecaseExpect204WithUriVariables(EtfFlowTransportBuilder.NEXT_ID);
	}

	@Override
	protected void callUsecaseExpect403ForThisUsecase() throws Exception {
		super.callUsecaseExpect403WithUriVariables(EtfFlowTransportBuilder.ETF_FLOW_1ID);
	}

	@Override
	protected void callUsecaseEmptyDatabase() throws Exception {
		super.callUsecaseExpect204WithUriVariables(EtfFlowTransportBuilder.ETF_FLOW_1ID);
	}

}