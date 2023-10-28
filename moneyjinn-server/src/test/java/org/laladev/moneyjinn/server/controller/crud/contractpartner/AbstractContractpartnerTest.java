package org.laladev.moneyjinn.server.controller.crud.contractpartner;

import org.laladev.moneyjinn.server.controller.AbstractWebUserControllerTest;
import org.laladev.moneyjinn.server.controller.api.CrudContractpartnerControllerApi;

abstract class AbstractContractpartnerTest extends AbstractWebUserControllerTest {

	protected CrudContractpartnerControllerApi getMock() {
		return super.getMock(CrudContractpartnerControllerApi.class);
	}
}
