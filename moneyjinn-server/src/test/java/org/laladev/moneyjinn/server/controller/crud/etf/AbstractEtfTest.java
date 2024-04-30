package org.laladev.moneyjinn.server.controller.crud.etf;

import org.laladev.moneyjinn.server.controller.AbstractWebUserControllerTest;
import org.laladev.moneyjinn.server.controller.api.CrudEtfControllerApi;

abstract class AbstractEtfTest extends AbstractWebUserControllerTest {

	protected CrudEtfControllerApi getMock() {
		return super.getMock(CrudEtfControllerApi.class);
	}
}
