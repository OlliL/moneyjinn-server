package org.laladev.moneyjinn.server.controller.user;

import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.model.access.User;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractAdminUserControllerTest;
import org.laladev.moneyjinn.server.controller.api.UserControllerApi;
import org.laladev.moneyjinn.server.model.ErrorResponse;
import org.laladev.moneyjinn.service.api.IUserService;

class DeleteUserByIdTest extends AbstractAdminUserControllerTest {
    @Inject
    private IUserService userService;

    @Override
    protected void loadMethod() {
        super.getMock(UserControllerApi.class).deleteUserById(null);
    }

    @Test
    void test_regularUserNoData_SuccessfullNoContent() throws Exception {
        User user = this.userService.getUserById(new UserID(UserTransportBuilder.USER2_ID));
        Assertions.assertNotNull(user);

        super.callUsecaseExpect204WithUriVariables(UserTransportBuilder.USER2_ID);

        user = this.userService.getUserById(new UserID(UserTransportBuilder.USER2_ID));
        Assertions.assertNull(user);
    }

    @Test
    void test_importUserNoData_SuccessfullNoContent() throws Exception {
        User user = this.userService.getUserById(new UserID(UserTransportBuilder.IMPORTUSER_ID));
        Assertions.assertNotNull(user);

        super.callUsecaseExpect204WithUriVariables(UserTransportBuilder.IMPORTUSER_ID);

        user = this.userService.getUserById(new UserID(UserTransportBuilder.IMPORTUSER_ID));
        Assertions.assertNull(user);
    }

    @Test
    void test_nonExistingUser_SuccessfullNoContent() throws Exception {
        User user = this.userService.getUserById(new UserID(UserTransportBuilder.NON_EXISTING_ID));
        Assertions.assertNull(user);

        super.callUsecaseExpect204WithUriVariables(UserTransportBuilder.NON_EXISTING_ID);

        user = this.userService.getUserById(new UserID(UserTransportBuilder.NON_EXISTING_ID));
        Assertions.assertNull(user);
    }

    @Test
    void test_regularUserWithData_SuccessfullNoContent() throws Exception {
        final ErrorResponse expected = new ErrorResponse();
        expected.setCode(ErrorCode.USER_HAS_DATA.getErrorCode());
        expected.setMessage("This user has already entered data and may therefore not be deleted!");
        User user = this.userService.getUserById(new UserID(UserTransportBuilder.USER3_ID));
        Assertions.assertNotNull(user);

        final ErrorResponse actual = super.callUsecaseExpect400(ErrorResponse.class, UserTransportBuilder.USER3_ID);

        user = this.userService.getUserById(new UserID(UserTransportBuilder.USER3_ID));
        Assertions.assertNotNull(user);
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