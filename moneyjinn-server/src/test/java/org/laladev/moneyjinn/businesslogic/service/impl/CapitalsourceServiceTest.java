package org.laladev.moneyjinn.businesslogic.service.impl;

import java.util.List;

import javax.inject.Inject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.AbstractTest;
import org.laladev.moneyjinn.model.access.Group;
import org.laladev.moneyjinn.model.access.GroupID;
import org.laladev.moneyjinn.model.access.User;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.capitalsource.Capitalsource;
import org.laladev.moneyjinn.model.capitalsource.CapitalsourceID;
import org.laladev.moneyjinn.model.exception.BusinessException;
import org.laladev.moneyjinn.server.builder.CapitalsourceTransportBuilder;
import org.laladev.moneyjinn.server.builder.GroupTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.service.api.ICapitalsourceService;

public class CapitalsourceServiceTest extends AbstractTest {
	@Inject
	private ICapitalsourceService capitalsourceService;

	@Test
	public void test_validateNullUser_raisesException() {
		final Capitalsource capitalsource = new Capitalsource();

		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			this.capitalsourceService.validateCapitalsource(capitalsource);
		});
	}

	@Test
	public void test_validateNullAccess_raisesException() {
		final Capitalsource capitalsource = new Capitalsource();
		capitalsource.setUser(new User(new UserID(1l)));

		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			this.capitalsourceService.validateCapitalsource(capitalsource);
		});
	}

	@Test
	public void test_createWithInvalidEntity_raisesException() {
		final Capitalsource capitalsource = new Capitalsource();
		capitalsource.setUser(new User(new UserID(1L)));
		capitalsource.setAccess(new Group(new GroupID(1L)));

		Assertions.assertThrows(BusinessException.class, () -> {
			this.capitalsourceService.createCapitalsource(capitalsource);
		});
	}

	@Test
	public void test_updateWithInvalidEntity_raisesException() {
		final Capitalsource capitalsource = new Capitalsource();
		capitalsource.setUser(new User(new UserID(1l)));
		capitalsource.setAccess(new Group(new GroupID(1L)));

		Assertions.assertThrows(BusinessException.class, () -> {
			this.capitalsourceService.updateCapitalsource(capitalsource);
		});
	}

	@Test
	public void test_userAeditsCapitalsource_userBsameGroupSeesCachedChange() {
		final UserID user1Id = new UserID(UserTransportBuilder.USER1_ID);
		final UserID user2Id = new UserID(UserTransportBuilder.USER2_ID);
		final GroupID groupId = new GroupID(GroupTransportBuilder.GROUP1_ID);

		// this caches
		Capitalsource capitalsource = this.capitalsourceService.getCapitalsourceById(user2Id, groupId,
				new CapitalsourceID(CapitalsourceTransportBuilder.CAPITALSOURCE1_ID));

		capitalsource = this.capitalsourceService.getCapitalsourceById(user1Id, groupId,
				new CapitalsourceID(CapitalsourceTransportBuilder.CAPITALSOURCE1_ID));

		final String comment = String.valueOf(System.currentTimeMillis());

		capitalsource.getUser().setId(user1Id);
		capitalsource.setComment(comment);

		// this should also modify the cache of user 1!
		this.capitalsourceService.updateCapitalsource(capitalsource);

		// this should now retrieve the changed cache entry!
		capitalsource = this.capitalsourceService.getCapitalsourceById(user2Id, groupId,
				new CapitalsourceID(CapitalsourceTransportBuilder.CAPITALSOURCE1_ID));

		Assertions.assertEquals(comment, capitalsource.getComment());
	}

	@Test
	public void test_userAaddsACapitalsource_userBsameGroupSeessItTooBecauseCacheWasReset() {
		final UserID user1Id = new UserID(UserTransportBuilder.USER1_ID);
		final UserID user2Id = new UserID(UserTransportBuilder.USER2_ID);
		final GroupID groupId = new GroupID(GroupTransportBuilder.GROUP1_ID);

		// this caches
		final List<Capitalsource> allCapitalsources1 = this.capitalsourceService.getAllCapitalsources(user1Id);

		final Capitalsource capitalsource = this.capitalsourceService.getCapitalsourceById(user2Id, groupId,
				new CapitalsourceID(CapitalsourceTransportBuilder.CAPITALSOURCE1_ID));

		final String comment = String.valueOf(System.currentTimeMillis());

		capitalsource.getUser().setId(user2Id);
		capitalsource.setComment(comment);

		// this should also modify the cache of user 1!
		this.capitalsourceService.createCapitalsource(capitalsource);

		final List<Capitalsource> allCapitalsources2 = this.capitalsourceService.getAllCapitalsources(user1Id);

		// Cache of user1 should have been invalidated and the added Capitalsource should be now in
		// the List of all Capitalsources.
		Assertions.assertNotEquals(allCapitalsources1.size(), allCapitalsources2.size());
	}

}
