package org.laladev.moneyjinn.server.controller.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.server.builder.AccessRelationTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractAdminUserControllerTest;
import org.laladev.moneyjinn.server.controller.api.UserControllerApi;
import org.laladev.moneyjinn.server.model.AccessRelationTransport;
import org.laladev.moneyjinn.server.model.ShowEditUserResponse;

import java.util.ArrayList;
import java.util.List;

class ShowEditUserTest extends AbstractAdminUserControllerTest {

    @Override
    protected void loadMethod() {
        super.getMock(UserControllerApi.class).showEditUser(null);
    }

    @Test
    void test_unknownUser_emptyResponseObject() throws Exception {
        final ShowEditUserResponse expected = new ShowEditUserResponse();

        final ShowEditUserResponse actual = super.callUsecaseExpect200(ShowEditUserResponse.class,
                UserTransportBuilder.NON_EXISTING_ID);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void test_User1_completeResponseObject() throws Exception {
        final ShowEditUserResponse expected = new ShowEditUserResponse();
        final List<AccessRelationTransport> accessRelationTransports = new ArrayList<>();
        accessRelationTransports.add(new AccessRelationTransportBuilder().forUser1_2000_01_01().build());
        accessRelationTransports.add(new AccessRelationTransportBuilder().forUser1_2600_01_01().build());
        accessRelationTransports.add(new AccessRelationTransportBuilder().forUser1_2700_01_01().build());
        accessRelationTransports.add(new AccessRelationTransportBuilder().forUser1_2800_01_01().build());
        expected.setAccessRelationTransports(accessRelationTransports);

        final ShowEditUserResponse actual = super.callUsecaseExpect200(ShowEditUserResponse.class,
                UserTransportBuilder.USER1_ID);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void test_User2_completeResponseObject() throws Exception {
        final ShowEditUserResponse expected = new ShowEditUserResponse();
        final List<AccessRelationTransport> accessRelationTransports = new ArrayList<>();
        accessRelationTransports.add(new AccessRelationTransportBuilder().forUser2_2000_01_01().build());
        expected.setAccessRelationTransports(accessRelationTransports);

        final ShowEditUserResponse actual = super.callUsecaseExpect200(ShowEditUserResponse.class,
                UserTransportBuilder.USER2_ID);

        Assertions.assertEquals(expected, actual);
    }

    @Override
    protected void callUsecaseExpect403ForThisUsecase() throws Exception {
        super.callUsecaseExpect403WithUriVariables(UserTransportBuilder.USER2_ID);
    }

    @Override
    protected void callUsecaseEmptyDatabase() throws Exception {
        // Users are always there.

    }
}