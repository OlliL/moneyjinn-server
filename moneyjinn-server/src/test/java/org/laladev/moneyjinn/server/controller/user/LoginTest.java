package org.laladev.moneyjinn.server.controller.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.laladev.moneyjinn.server.controller.api.UserControllerApi;
import org.laladev.moneyjinn.server.model.ErrorResponse;
import org.laladev.moneyjinn.server.model.LoginRequest;
import org.laladev.moneyjinn.server.model.LoginResponse;
import org.springframework.test.context.jdbc.Sql;

class LoginTest extends AbstractControllerTest {

    @BeforeEach
    void setUp() {
        super.setUsername(null);
        super.setPassword(null);
    }

    @Override
    protected void loadMethod() {
        super.getMock(UserControllerApi.class).login(null);
    }

    @Test
    void test_RegularUser_Successfull() throws Exception {
        final String username = UserTransportBuilder.USER1_NAME;
        final String password = UserTransportBuilder.USER1_PASSWORD;

        final LoginRequest request = new LoginRequest();
        request.setUserName(username);
        request.setUserPassword(password);

        final LoginResponse response = super.callUsecaseExpect200(request, LoginResponse.class);

        Assertions.assertEquals(new UserTransportBuilder().forUser1().build(), response.getUserTransport());

        Assertions.assertNotNull(response.getToken());
        Assertions.assertNotNull(response.getRefreshToken());
    }

    @Test
    void test_AdminUser_Successfull() throws Exception {
        final String username = UserTransportBuilder.ADMIN_NAME;
        final String password = UserTransportBuilder.ADMIN_PASSWORD;

        final LoginRequest request = new LoginRequest();
        request.setUserName(username);
        request.setUserPassword(password);

        final LoginResponse response = super.callUsecaseExpect200(request, LoginResponse.class);

        Assertions.assertEquals(new UserTransportBuilder().forAdmin().build(), response.getUserTransport());

        Assertions.assertNotNull(response.getToken());
        Assertions.assertNotNull(response.getRefreshToken());
    }

    @Test
    void test_ImportUser_Successfull() throws Exception {
        final String username = UserTransportBuilder.IMPORTUSER_NAME;
        final String password = UserTransportBuilder.IMPORTUSER_PASSWORD;

        final LoginRequest request = new LoginRequest();
        request.setUserName(username);
        request.setUserPassword(password);

        final LoginResponse response = super.callUsecaseExpect200(request, LoginResponse.class);

        Assertions.assertEquals(new UserTransportBuilder().forImportUser().build(), response.getUserTransport());

        Assertions.assertNotNull(response.getToken());
        Assertions.assertNotNull(response.getRefreshToken());
    }

    @Test
    void test_LockedUser_ErrorResponse() throws Exception {
        final String username = UserTransportBuilder.USER2_NAME;
        final String password = UserTransportBuilder.USER2_PASSWORD;

        final LoginRequest request = new LoginRequest();
        request.setUserName(username);
        request.setUserPassword(password);

        final ErrorResponse response = super.callUsecaseExpect403(request, ErrorResponse.class);

        Assertions.assertEquals(response.getCode(), ErrorCode.ACCOUNT_IS_LOCKED.getErrorCode());
    }

    @Test
    void test_WrongPassword_ErrorResponse() throws Exception {
        final String username = UserTransportBuilder.USER2_NAME;
        final String password = "wrong password";

        final LoginRequest request = new LoginRequest();
        request.setUserName(username);
        request.setUserPassword(password);

        super.callUsecaseExpect403(request);

    }

    @Test
    @Sql(scripts = {"classpath:h2reinit.sql", "classpath:h2defaults.sql"})
    void test_emptyDatabase_noException() throws Exception {
        super.setUsername(UserTransportBuilder.ADMIN_NAME);
        super.setPassword(UserTransportBuilder.ADMIN_PASSWORD);

        final LoginRequest request = new LoginRequest();
        request.setUserName(UserTransportBuilder.ADMIN_NAME);
        request.setUserPassword(UserTransportBuilder.ADMIN_PASSWORD);

        super.callUsecaseExpect200(request, LoginResponse.class);
    }
}