package org.laladev.moneyjinn.businesslogic.model.access;

import java.util.Collection;

/**
 * Describes a User in the System, its ID, name, password, {@link UserAttribute}s and
 * {@link UserPermission}s.
 *
 * @author olivleh1
 *
 */
public class User extends AbstractAccess<UserID> {
	private static final long serialVersionUID = 1L;
	private String password;
	private Collection<UserAttribute> attributes;
	private Collection<UserPermission> permissions;

	public User() {
	}

	public User(final UserID id, final String name, final String password, final Collection<UserAttribute> attributes,
			final Collection<UserPermission> permissions) {
		super(id, name);
		this.password = password;
		this.attributes = attributes;
		this.permissions = permissions;
	}

	public final String getPassword() {
		return this.password;
	}

	public final void setPassword(final String password) {
		this.password = password;
	}

	public final Collection<UserAttribute> getAttributes() {
		return this.attributes;
	}

	public final void setAttributes(final Collection<UserAttribute> attributes) {
		this.attributes = attributes;
	}

	public final Collection<UserPermission> getPermissions() {
		return this.permissions;
	}

	public final void setPermissions(final Collection<UserPermission> permissions) {
		this.permissions = permissions;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("User [password=").append(this.password).append(", attributes=").append(this.attributes)
				.append(", permissions=").append(this.permissions).append(", getName()=").append(this.getName())
				.append(", getId()=").append(this.getId()).append("]");
		return builder.toString();
	}

}
