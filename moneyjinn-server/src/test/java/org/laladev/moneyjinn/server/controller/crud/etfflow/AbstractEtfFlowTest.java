package org.laladev.moneyjinn.server.controller.crud.etfflow;

import org.laladev.moneyjinn.server.controller.AbstractWebUserControllerTest;
import org.laladev.moneyjinn.server.controller.api.CrudEtfFlowControllerApi;

abstract class AbstractEtfFlowTest extends AbstractWebUserControllerTest {

    protected CrudEtfFlowControllerApi getMock() {
        return super.getMock(CrudEtfFlowControllerApi.class);
    }
}
