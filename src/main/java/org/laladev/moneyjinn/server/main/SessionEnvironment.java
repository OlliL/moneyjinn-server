package org.laladev.moneyjinn.server.main;

import org.laladev.moneyjinn.businesslogic.model.access.UserID;

public class SessionEnvironment {
	private UserID userID;

	public UserID getUserID() {
		return this.userID;
	}

	public void setUserID(final UserID userID) {
		this.userID = userID;
	}

}
