package org.laladev.moneyjinn.server.main;

import org.laladev.moneyjinn.model.access.UserID;

public class SessionEnvironment {
	private UserID userId;

	public UserID getUserId() {
		return this.userId;
	}

	public void setUserId(final UserID userId) {
		this.userId = userId;
	}

}
