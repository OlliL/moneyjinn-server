package org.laladev.moneyjinn.server.controller.impl;

import javax.inject.Inject;

import org.laladev.moneyjinn.api.AbstractMapperSupport;
import org.laladev.moneyjinn.businesslogic.model.access.UserID;
import org.laladev.moneyjinn.server.main.SessionEnvironment;

public abstract class AbstractController extends AbstractMapperSupport {
	@Inject
	private SessionEnvironment sessionEnvironment;

	protected UserID getUserId() {
		return this.sessionEnvironment.getUserID();
	}

	protected void setUserId(final UserID userId) {
		this.sessionEnvironment.setUserID(userId);
	}

}
