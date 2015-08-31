package org.laladev.moneyjinn.server.builder;

import org.laladev.moneyjinn.core.rest.model.transport.UserTransport;

public class UserTransportBuilder extends UserTransport {
	private static final Short SHORT_1 = Short.valueOf("1");

	public static final String ADMIN_NAME = "admin";
	public static final String USER1_NAME = "user1";
	public static final String USER2_NAME = "user2";
	public static final String NEWUSER_NAME = "paul";

	public static final Long ADMIN_ID = 2l;
	public static final Long USER1_ID = 3l;
	public static final Long USER2_ID = 4l;
	public static final Long NON_EXISTING_ID = 666l;
	public static final Long NEXT_ID = 7l;

	public static final String USER1_PASSWORD = "111";
	public static final String USER1_PASSWORD_SHA1 = "6216f8a75fd5bb3d5f22b6f9958cdede3fc086c2";
	public static final String USER2_PASSWORD = "222";
	public static final String USER2_PASSWORD_SHA1 = "1c6637a8f2e1f75e06ff9984894d6bd16a3a36a9";
	public static final String ADMIN_PASSWORD = "admin";

	public UserTransportBuilder forAdmin() {
		super.setId(ADMIN_ID);
		super.setUserCanLogin(SHORT_1);
		super.setUserIsAdmin(SHORT_1);
		super.setUserIsNew(SHORT_1);
		super.setUserName(ADMIN_NAME);
		return this;
	}

	public UserTransportBuilder forUser1() {
		super.setId(USER1_ID);
		super.setUserCanLogin(SHORT_1);
		super.setUserIsAdmin(null);
		super.setUserIsNew(SHORT_1);
		super.setUserName(USER1_NAME);
		return this;
	}

	public UserTransportBuilder forUser2() {
		super.setId(USER2_ID);
		super.setUserCanLogin(null);
		super.setUserIsAdmin(null);
		super.setUserIsNew(null);
		super.setUserName(USER2_NAME);
		return this;
	}

	public UserTransportBuilder forNewUser() {
		super.setId(NON_EXISTING_ID);
		super.setUserCanLogin(SHORT_1);
		super.setUserIsAdmin(SHORT_1);
		super.setUserIsNew(null);
		super.setUserName(NEWUSER_NAME);
		return this;
	}

	public UserTransport build() {
		final UserTransport transport = new UserTransport();

		transport.setId(super.getId());
		transport.setUserCanLogin(super.getUserCanLogin());
		transport.setUserIsAdmin(super.getUserIsAdmin());
		transport.setUserIsNew(super.getUserIsNew());
		transport.setUserName(super.getUserName());

		return transport;
	}
}
