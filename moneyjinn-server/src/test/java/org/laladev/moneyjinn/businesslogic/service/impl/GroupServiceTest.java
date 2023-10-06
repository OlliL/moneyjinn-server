
package org.laladev.moneyjinn.businesslogic.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.AbstractTest;
import org.laladev.moneyjinn.model.access.Group;
import org.laladev.moneyjinn.model.exception.BusinessException;
import org.laladev.moneyjinn.service.api.IGroupService;

import jakarta.inject.Inject;

class GroupServiceTest extends AbstractTest {
	@Inject
	private IGroupService groupService;

	@Test
	void test_createWithInvalidEntity_raisesException() {
		final Group group = new Group();
		Assertions.assertThrows(BusinessException.class, () -> {
			this.groupService.createGroup(group);
		});
	}

	@Test
	void test_updateWithInvalidEntity_raisesException() {
		final Group group = new Group();
		Assertions.assertThrows(BusinessException.class, () -> {
			this.groupService.updateGroup(group);
		});
	}
}
