package org.laladev.moneyjinn.server.builder;

import org.laladev.moneyjinn.core.rest.model.transport.UserTransport;

public class UserTransportBuilder extends UserTransport {
	private static final Short SHORT_1 = Short.valueOf("1");
	private static final Short SHORT_0 = Short.valueOf("0");

	public UserTransportBuilder forAdmin() {
		super.setId(2l);
		super.setUserCanLogin(SHORT_1);
		super.setUserIsAdmin(SHORT_1);
		super.setUserIsNew(SHORT_1);
		super.setUserName("admin");
		super.setUserPassword("d033e22ae348aeb5660fc2140aec35850c4da997");
		return this;
	}

	public UserTransportBuilder forUser1() {
		super.setId(3l);
		super.setUserCanLogin(SHORT_1);
		super.setUserIsAdmin(SHORT_1);
		super.setUserIsNew(SHORT_1);
		super.setUserName("user1");
		super.setUserPassword("111");
		return this;
	}

	public UserTransportBuilder forUser2() {
		super.setId(4l);
		super.setUserCanLogin(null);
		super.setUserIsAdmin(null);
		super.setUserIsNew(null);
		super.setUserName("user2");
		super.setUserPassword("222");
		return this;
	}

	public UserTransport build() {
		final UserTransport transport = new UserTransport();

		transport.setId(super.getId());
		transport.setUserCanLogin(super.getUserCanLogin());
		transport.setUserIsAdmin(super.getUserCanLogin());
		transport.setUserIsNew(super.getUserIsNew());
		transport.setUserName(super.getUserName());
		transport.setUserPassword(super.getUserPassword());

		return transport;
	}
}
