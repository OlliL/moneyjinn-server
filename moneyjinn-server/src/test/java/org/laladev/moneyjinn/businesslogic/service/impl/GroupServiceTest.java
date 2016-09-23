package org.laladev.moneyjinn.businesslogic.service.impl;

import javax.inject.Inject;

import org.junit.Test;
import org.laladev.moneyjinn.AbstractTest;
import org.laladev.moneyjinn.businesslogic.model.access.Group;
import org.laladev.moneyjinn.businesslogic.model.exception.BusinessException;
import org.laladev.moneyjinn.businesslogic.service.api.IGroupService;

public class GroupServiceTest extends AbstractTest {
	@Inject
	private IGroupService groupService;

	@Test(expected = BusinessException.class)
	public void test_createWithInvalidEntity_raisesException() {
		final Group group = new Group();

		this.groupService.createGroup(group);
	}

	@Test(expected = BusinessException.class)
	public void test_updateWithInvalidEntity_raisesException() {
		final Group group = new Group();

		this.groupService.updateGroup(group);
	}
}
