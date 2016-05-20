//
// Copyright (c) 2015 Oliver Lehmann <oliver@laladev.org>
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions
// are met:
// 1. Redistributions of source code must retain the above copyright
// notice, this list of conditions and the following disclaimer
// 2. Redistributions in binary form must reproduce the above copyright
// notice, this list of conditions and the following disclaimer in the
// documentation and/or other materials provided with the distribution.
//
// THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS'' AND
// ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE LIABLE
// FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
// DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
// OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
// HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
// LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
// OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE.
//

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

	/**
	 * Default Constructor because ID can be empty for newly created Groups.
	 */
	public User() {
	}

	public User(final UserID id) {
		super(id);
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
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((this.attributes == null) ? 0 : this.attributes.hashCode());
		result = prime * result + ((this.password == null) ? 0 : this.password.hashCode());
		result = prime * result + ((this.permissions == null) ? 0 : this.permissions.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		final User other = (User) obj;
		if (this.attributes == null) {
			if (other.attributes != null) {
				return false;
			}
		} else if (!this.attributes.equals(other.attributes)) {
			return false;
		}
		if (this.password == null) {
			if (other.password != null) {
				return false;
			}
		} else if (!this.password.equals(other.password)) {
			return false;
		}
		if (this.permissions == null) {
			if (other.permissions != null) {
				return false;
			}
		} else if (!this.permissions.equals(other.permissions)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("User [password=");
		builder.append(this.password);
		builder.append(", attributes=");
		builder.append(this.attributes);
		builder.append(", permissions=");
		builder.append(this.permissions);
		builder.append(", getName()=");
		builder.append(this.getName());
		builder.append(", getId()=");
		builder.append(this.getId());
		builder.append("]");
		return builder.toString();
	}

}
