
package org.laladev.moneyjinn.server.builder;

import org.laladev.moneyjinn.server.model.UserTransport;

public class UserTransportBuilder extends UserTransport {
	private static final Integer SHORT_1 = Integer.valueOf("1");
	public static final String ADMIN_NAME = "admin";
	public static final String USER1_NAME = "user1";
	public static final String USER2_NAME = "user2";
	public static final String USER3_NAME = "user3";
	public static final String NEWUSER_NAME = "paul";
	public static final String IMPORTUSER_NAME = "importuser";
	public static final Long ADMIN_ID = 0l;
	public static final Long USER1_ID = 3l;
	public static final Long USER2_ID = 4l;
	public static final Long USER3_ID = 5l;
	public static final Long IMPORTUSER_ID = 9L;
	public static final Long NON_EXISTING_ID = 666l;
	public static final Long NEXT_ID = 10l;
	public static final String USER1_PASSWORD = "111";
	public static final String USER1_PASSWORD_SHA1 = "6216f8a75fd5bb3d5f22b6f9958cdede3fc086c2";
	public static final String USER2_PASSWORD = "222";
	public static final String USER2_PASSWORD_SHA1 = "1c6637a8f2e1f75e06ff9984894d6bd16a3a36a9";
	public static final String USER3_PASSWORD = "222";
	public static final String ADMIN_PASSWORD = "admin";
	public static final String ADMIN_PASSWORD_SHA1 = "d033e22ae348aeb5660fc2140aec35850c4da997";
	public static final String IMPORTUSER_PASSWORD = "importpass";
	public static final String IMPORTUSER_PASSWORD_SHA1 = "272c2d7dec93029abac86fb647b48e0f3bb3aa45";

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
