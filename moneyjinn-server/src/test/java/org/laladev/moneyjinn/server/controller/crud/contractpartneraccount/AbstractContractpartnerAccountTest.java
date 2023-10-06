package org.laladev.moneyjinn.server.controller.crud.contractpartneraccount;

import org.junit.jupiter.api.BeforeEach;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.laladev.moneyjinn.server.controller.api.CrudContractpartnerAccountControllerApi;

abstract class AbstractContractpartnerAccountTest extends AbstractControllerTest {

	@BeforeEach
	public void setUp() {
		super.setUsername(UserTransportBuilder.USER1_NAME);
		super.setPassword(UserTransportBuilder.USER1_PASSWORD);
	}

	protected CrudContractpartnerAccountControllerApi getMock() {
		return super.getMock(CrudContractpartnerAccountControllerApi.class);
	}
}
