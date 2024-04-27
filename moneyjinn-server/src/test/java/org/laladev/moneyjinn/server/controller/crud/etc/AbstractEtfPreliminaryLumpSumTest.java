package org.laladev.moneyjinn.server.controller.crud.etc;

import org.laladev.moneyjinn.server.controller.AbstractWebUserControllerTest;
import org.laladev.moneyjinn.server.controller.api.CrudEtfPreliminaryLumpSumControllerApi;

abstract class AbstractEtfPreliminaryLumpSumTest extends AbstractWebUserControllerTest {

	protected CrudEtfPreliminaryLumpSumControllerApi getMock() {
		return super.getMock(CrudEtfPreliminaryLumpSumControllerApi.class);
	}
}
