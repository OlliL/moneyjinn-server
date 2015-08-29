package org.laladev.moneyjinn.core.rest.model.transport;

public class UserTransport {
	private Long id;
	private String userName;
	private String userPassword;
	private Short userIsAdmin;
	private Short userIsNew;
	private Short userCanLogin;

	public final Long getId() {
		return id;
	}

	public final void setId(final Long id) {
		this.id = id;
	}

	public final String getUserName() {
		return userName;
	}

	public final void setUserName(final String userName) {
		this.userName = userName;
	}

	public final String getUserPassword() {
		return userPassword;
	}

	public final void setUserPassword(final String userPassword) {
		this.userPassword = userPassword;
	}

	public final Short getUserIsAdmin() {
		return userIsAdmin;
	}

	public final void setUserIsAdmin(final Short userIsAdmin) {
		this.userIsAdmin = userIsAdmin;
	}

	public final Short getUserIsNew() {
		return userIsNew;
	}

	public final void setUserIsNew(final Short userIsNew) {
		this.userIsNew = userIsNew;
	}

	public final Short getUserCanLogin() {
		return userCanLogin;
	}

	public final void setUserCanLogin(final Short userCanLogin) {
		this.userCanLogin = userCanLogin;
	}

}
