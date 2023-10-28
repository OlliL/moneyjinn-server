
package org.laladev.moneyjinn.server.controller.etf;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.model.etf.EtfFlow;
import org.laladev.moneyjinn.model.etf.EtfFlowID;
import org.laladev.moneyjinn.server.builder.EtfFlowTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.laladev.moneyjinn.server.controller.api.EtfControllerApi;
import org.laladev.moneyjinn.service.api.IEtfService;
import org.springframework.test.context.jdbc.Sql;

import jakarta.inject.Inject;

class DeleteEtfFlowTest extends AbstractControllerTest {
	@Inject
	IEtfService etfService;

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

	@Test
	void test_ImportRoleNotAllowed_ErrorResponse() throws Exception {
		this.userName = UserTransportBuilder.IMPORTUSER_NAME;
		this.userPassword = UserTransportBuilder.IMPORTUSER_PASSWORD;

		super.callUsecaseExpect403WithUriVariables(EtfFlowTransportBuilder.ETF_FLOW_1ID);
	}

	@Test
	void test_AuthorizationRequired_Error() throws Exception {
		this.userName = null;
		this.userPassword = null;

		super.callUsecaseExpect403WithUriVariables(EtfFlowTransportBuilder.ETF_FLOW_1ID);
	}

	@Test
	@Sql("classpath:h2defaults.sql")
	void test_emptyDatabase_noException() throws Exception {
		this.userName = UserTransportBuilder.ADMIN_NAME;
		this.userPassword = UserTransportBuilder.ADMIN_PASSWORD;

		super.callUsecaseExpect204WithUriVariables(EtfFlowTransportBuilder.ETF_FLOW_1ID);
	}
}