package org.laladev.moneyjinn.server.controller.crud.contractpartner;

import org.junit.jupiter.api.BeforeEach;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.laladev.moneyjinn.server.controller.api.CrudContractpartnerControllerApi;

abstract class AbstractContractpartnerTest extends AbstractControllerTest {

	@BeforeEach
	public void setUp() {
		super.setUsername(UserTransportBuilder.USER1_NAME);
		super.setPassword(UserTransportBuilder.USER1_PASSWORD);
	}

	protected CrudContractpartnerControllerApi getMock() {
		return super.getMock(CrudContractpartnerControllerApi.class);
	}
}
