package org.laladev.moneyjinn.server.builder;

import org.laladev.moneyjinn.server.model.UserTransport;

public class UserTransportBuilder extends UserTransport {
    public static final String ADMIN_NAME = "admin";
    public static final String USER1_NAME = "user1";
    public static final String USER2_NAME = "user2";
    public static final String USER3_NAME = "user3";
    public static final String NEWUSER_NAME = "paul";
    public static final String IMPORTUSER_NAME = "importuser";
    public static final Long ADMIN_ID = 0L;
    public static final Long USER1_ID = 3L;
    public static final Long USER2_ID = 4L;
    public static final Long USER3_ID = 5L;
    public static final Long IMPORTUSER_ID = 9L;
    public static final Long NON_EXISTING_ID = 666L;
    public static final Long NEXT_ID = 10L;
    public static final String USER1_PASSWORD = "111";
    public static final String USER1_PASSWORD_ENCODED = "$2a$10$PQ54whtOGLu4u/arNe/X3uxTf6SiyaGGbHGzKyZfeZ/5gtdVKeiai";
    public static final String USER2_PASSWORD = "222";
    public static final String USER3_PASSWORD = "222";
    public static final String ADMIN_PASSWORD = "admin";
    public static final String IMPORTUSER_PASSWORD = "importpass";
    private static final Integer SHORT_1 = Integer.valueOf("1");

    public UserTransportBuilder forAdmin() {
        super.setId(ADMIN_ID);
        super.setRole(RoleEnum.ADMIN);
        super.setUserIsNew(SHORT_1);
        super.setUserName(ADMIN_NAME);
        return this;
    }

    public UserTransportBuilder forUser1() {
        super.setId(USER1_ID);
        super.setRole(RoleEnum.STANDARD);
        super.setUserIsNew(SHORT_1);
        super.setUserName(USER1_NAME);
        return this;
    }

    public UserTransportBuilder forUser2() {
        super.setId(USER2_ID);
        super.setRole(RoleEnum.INACTIVE);
        super.setUserIsNew(null);
        super.setUserName(USER2_NAME);
        return this;
    }

    public UserTransportBuilder forUser3() {
        super.setId(USER3_ID);
        super.setRole(RoleEnum.STANDARD);
        super.setUserIsNew(null);
        super.setUserName(USER3_NAME);
        return this;
    }

    public UserTransportBuilder forImportUser() {
        super.setId(IMPORTUSER_ID);
        super.setRole(RoleEnum.IMPORT);
        super.setUserIsNew(null);
        super.setUserName(IMPORTUSER_NAME);
        return this;
    }

    public UserTransportBuilder forNewUser() {
        super.setId(NON_EXISTING_ID);
        super.setRole(RoleEnum.ADMIN);
        super.setUserIsNew(null);
        super.setUserName(NEWUSER_NAME);
        super.setUserPassword(IMPORTUSER_PASSWORD);
        return this;
    }

    public UserTransport build() {
        final UserTransport transport = new UserTransport();
        transport.setId(super.getId());
        transport.setRole(super.getRole());
        transport.setUserIsNew(super.getUserIsNew());
        transport.setUserName(super.getUserName());
        transport.setUserPassword(super.getUserPassword());
        return transport;
    }
}
