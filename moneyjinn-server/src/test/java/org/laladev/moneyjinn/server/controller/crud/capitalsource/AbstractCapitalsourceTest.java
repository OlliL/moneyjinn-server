package org.laladev.moneyjinn.server.controller.crud.capitalsource;

import org.laladev.moneyjinn.server.controller.AbstractWebUserControllerTest;
import org.laladev.moneyjinn.server.controller.api.CrudCapitalsourceControllerApi;

abstract class AbstractCapitalsourceTest extends AbstractWebUserControllerTest {

	protected CrudCapitalsourceControllerApi getMock() {
		return super.getMock(CrudCapitalsourceControllerApi.class);
	}
}
