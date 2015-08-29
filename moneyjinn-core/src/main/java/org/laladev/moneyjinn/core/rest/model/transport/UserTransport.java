package org.laladev.moneyjinn.core.rest.model.transport;

public class UserTransport {
	private Long id;
	private String userName;
	private String userPassword;
	private Short userIsAdmin;
	private Short userIsNew;
	private Short userCanLogin;

	public final Long getId() {
		return this.id;
	}

	public final void setId(final Long id) {
		this.id = id;
	}

	public final String getUserName() {
		return this.userName;
	}

	public final void setUserName(final String userName) {
		this.userName = userName;
	}

	public final String getUserPassword() {
		return this.userPassword;
	}

	public final void setUserPassword(final String userPassword) {
		this.userPassword = userPassword;
	}

	public final Short getUserIsAdmin() {
		return this.userIsAdmin;
	}

	public final void setUserIsAdmin(final Short userIsAdmin) {
		this.userIsAdmin = userIsAdmin;
	}

	public final Short getUserIsNew() {
		return this.userIsNew;
	}

	public final void setUserIsNew(final Short userIsNew) {
		this.userIsNew = userIsNew;
	}

	public final Short getUserCanLogin() {
		return this.userCanLogin;
	}

	public final void setUserCanLogin(final Short userCanLogin) {
		this.userCanLogin = userCanLogin;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
		result = prime * result + ((this.userCanLogin == null) ? 0 : this.userCanLogin.hashCode());
		result = prime * result + ((this.userIsAdmin == null) ? 0 : this.userIsAdmin.hashCode());
		result = prime * result + ((this.userIsNew == null) ? 0 : this.userIsNew.hashCode());
		result = prime * result + ((this.userName == null) ? 0 : this.userName.hashCode());
		result = prime * result + ((this.userPassword == null) ? 0 : this.userPassword.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		final UserTransport other = (UserTransport) obj;
		if (this.id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!this.id.equals(other.id)) {
			return false;
		}
		if (this.userCanLogin == null) {
			if (other.userCanLogin != null) {
				return false;
			}
		} else if (!this.userCanLogin.equals(other.userCanLogin)) {
			return false;
		}
		if (this.userIsAdmin == null) {
			if (other.userIsAdmin != null) {
				return false;
			}
		} else if (!this.userIsAdmin.equals(other.userIsAdmin)) {
			return false;
		}
		if (this.userIsNew == null) {
			if (other.userIsNew != null) {
				return false;
			}
		} else if (!this.userIsNew.equals(other.userIsNew)) {
			return false;
		}
		if (this.userName == null) {
			if (other.userName != null) {
				return false;
			}
		} else if (!this.userName.equals(other.userName)) {
			return false;
		}
		if (this.userPassword == null) {
			if (other.userPassword != null) {
				return false;
			}
		} else if (!this.userPassword.equals(other.userPassword)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("UserTransport [id=");
		builder.append(this.id);
		builder.append(", userName=");
		builder.append(this.userName);
		builder.append(", userPassword=");
		builder.append(this.userPassword);
		builder.append(", userIsAdmin=");
		builder.append(this.userIsAdmin);
		builder.append(", userIsNew=");
		builder.append(this.userIsNew);
		builder.append(", userCanLogin=");
		builder.append(this.userCanLogin);
		builder.append("]");
		return builder.toString();
	}

}
