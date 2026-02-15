package org.laladev.moneyjinn.server.controller.crud.contractpartnermatching;

import org.laladev.moneyjinn.server.controller.AbstractWebUserControllerTest;
import org.laladev.moneyjinn.server.controller.api.CrudContractpartnerMatchingControllerApi;

abstract class AbstractContractpartnerMatchingTest extends AbstractWebUserControllerTest {

    protected CrudContractpartnerMatchingControllerApi getMock() {
        return super.getMock(CrudContractpartnerMatchingControllerApi.class);
    }
}
