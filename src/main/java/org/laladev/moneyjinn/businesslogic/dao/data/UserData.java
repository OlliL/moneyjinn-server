package org.laladev.moneyjinn.businesslogic.dao.data;

public class UserData {
	private Long id;
	private String name;
	private String password;
	private boolean permLogin;
	private boolean permAdmin;
	private boolean attChangePassword;

	public final Long getId() {
		return id;
	}

	public final void setId(final Long id) {
		this.id = id;
	}

	public final String getName() {
		return name;
	}

	public final void setName(final String name) {
		this.name = name;
	}

	public final String getPassword() {
		return password;
	}

	public final void setPassword(final String password) {
		this.password = password;
	}

	public final boolean isPermLogin() {
		return permLogin;
	}

	public final void setPermLogin(final boolean permLogin) {
		this.permLogin = permLogin;
	}

	public final boolean isPermAdmin() {
		return permAdmin;
	}

	public final void setPermAdmin(final boolean permAdmin) {
		this.permAdmin = permAdmin;
	}

	public final boolean isAttChangePassword() {
		return attChangePassword;
	}

	public final void setAttChangePassword(final boolean attChangePassword) {
		this.attChangePassword = attChangePassword;
	}

}
