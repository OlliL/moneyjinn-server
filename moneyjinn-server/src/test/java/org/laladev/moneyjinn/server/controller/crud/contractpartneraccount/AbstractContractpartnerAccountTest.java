package org.laladev.moneyjinn.server.controller.crud.contractpartneraccount;

import org.laladev.moneyjinn.server.controller.AbstractWebUserControllerTest;
import org.laladev.moneyjinn.server.controller.api.CrudContractpartnerAccountControllerApi;

abstract class AbstractContractpartnerAccountTest extends AbstractWebUserControllerTest {

	protected CrudContractpartnerAccountControllerApi getMock() {
		return super.getMock(CrudContractpartnerAccountControllerApi.class);
	}
}
