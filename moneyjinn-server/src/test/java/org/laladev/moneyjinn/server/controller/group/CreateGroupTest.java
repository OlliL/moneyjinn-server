package org.laladev.moneyjinn.server.controller.group;

import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.model.access.Group;
import org.laladev.moneyjinn.server.builder.GroupTransportBuilder;
import org.laladev.moneyjinn.server.builder.ValidationItemTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractAdminUserControllerTest;
import org.laladev.moneyjinn.server.controller.api.GroupControllerApi;
import org.laladev.moneyjinn.server.model.*;
import org.laladev.moneyjinn.service.api.IGroupService;

import java.util.ArrayList;
import java.util.List;

class CreateGroupTest extends AbstractAdminUserControllerTest {
    @Inject
    private IGroupService groupService;

    @Override
    protected void loadMethod() {
        super.getMock(GroupControllerApi.class).createGroup(null);
    }

    private void testError(final GroupTransport transport, final ErrorCode errorCode) throws Exception {
        final CreateGroupRequest request = new CreateGroupRequest();
        request.setGroupTransport(transport);
        final List<ValidationItemTransport> validationItems = new ArrayList<>();
        validationItems
                .add(new ValidationItemTransportBuilder().withKey(null).withError(errorCode.getErrorCode()).build());
        final ValidationResponse expected = new ValidationResponse();
        expected.setValidationItemTransports(validationItems);
        expected.setResult(Boolean.FALSE);

        final ValidationResponse actual = super.callUsecaseExpect422(request, ValidationResponse.class);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void test_GroupnameAlreadyExisting_Error() throws Exception {
        final GroupTransport transport = new GroupTransportBuilder().forNewGroup().build();
        transport.setName(GroupTransportBuilder.GROUP1_NAME);
        this.testError(transport, ErrorCode.GROUP_WITH_SAME_NAME_ALREADY_EXISTS);
    }

    @Test
    void test_emptyGroupname_Error() throws Exception {
        final GroupTransport transport = new GroupTransportBuilder().forNewGroup().build();
        transport.setName("");
        this.testError(transport, ErrorCode.NAME_MUST_NOT_BE_EMPTY);
    }

    @Test
    void test_nullGroupname_Error() throws Exception {
        final GroupTransport transport = new GroupTransportBuilder().forNewGroup().build();
        transport.setName(null);
        this.testError(transport, ErrorCode.NAME_MUST_NOT_BE_EMPTY);
    }

    @Test
    void test_standardRequest_Successfull() throws Exception {
        final CreateGroupRequest request = new CreateGroupRequest();
        final GroupTransport transport = new GroupTransportBuilder().forNewGroup().build();
        request.setGroupTransport(transport);

        final CreateGroupResponse actual = super.callUsecaseExpect200(request, CreateGroupResponse.class);

        final Group group = this.groupService.getGroupByName(GroupTransportBuilder.NEWGROUP_NAME);
        Assertions.assertEquals(GroupTransportBuilder.NEXT_ID, group.getId().getId());
        Assertions.assertEquals(GroupTransportBuilder.NEXT_ID, actual.getGroupId());
        Assertions.assertEquals(GroupTransportBuilder.NEWGROUP_NAME, group.getName());
    }

    @Override
    protected void callUsecaseExpect403ForThisUsecase() throws Exception {
        super.callUsecaseExpect403(new CreateGroupRequest());
    }

    @Override
    protected void callUsecaseEmptyDatabase() throws Exception {
        // Groups are always there.
    }
}