package org.laladev.moneyjinn.server.builder;

import org.laladev.moneyjinn.core.rest.model.transport.UserTransport;

public class UserTransportBuilder extends UserTransport {
	private static final Short SHORT_1 = Short.valueOf("1");
	private static final Short SHORT_0 = Short.valueOf("0");

	public static final String ADMIN_NAME = "admin";
	public static final String USER1_NAME = "user1";
	public static final String USER2_NAME = "user2";

	public static final Long ADMIN_ID = 2l;
	public static final Long USER1_ID = 3l;
	public static final Long USER2_ID = 4l;

	public static final String USER1_PASSWORD = "111";

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
		super.setUserIsAdmin(SHORT_1);
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

	public UserTransport build() {
		final UserTransport transport = new UserTransport();

		transport.setId(super.getId());
		transport.setUserCanLogin(super.getUserCanLogin());
		transport.setUserIsAdmin(super.getUserCanLogin());
		transport.setUserIsNew(super.getUserIsNew());
		transport.setUserName(super.getUserName());

		return transport;
	}
}
