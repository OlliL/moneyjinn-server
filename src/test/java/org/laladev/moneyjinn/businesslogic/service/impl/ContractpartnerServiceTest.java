package org.laladev.moneyjinn.businesslogic.service.impl;

import java.util.List;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.laladev.moneyjinn.AbstractTest;
import org.laladev.moneyjinn.businesslogic.model.Contractpartner;
import org.laladev.moneyjinn.businesslogic.model.ContractpartnerID;
import org.laladev.moneyjinn.businesslogic.model.access.User;
import org.laladev.moneyjinn.businesslogic.model.access.UserID;
import org.laladev.moneyjinn.businesslogic.model.exception.BusinessException;
import org.laladev.moneyjinn.businesslogic.service.api.IContractpartnerService;
import org.laladev.moneyjinn.server.builder.ContractpartnerTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;

public class ContractpartnerServiceTest extends AbstractTest {
	@Inject
	private IContractpartnerService contractpartnerService;

	@Test(expected = IllegalArgumentException.class)
	public void test_validateNullUser_raisesException() {
		final Contractpartner contractpartner = new Contractpartner();

		this.contractpartnerService.validateContractpartner(contractpartner);
	}

	@Test(expected = BusinessException.class)
	public void test_createWithInvalidEntity_raisesException() {
		final Contractpartner contractpartner = new Contractpartner();
		contractpartner.setUser(new User(new UserID(1L)));

		this.contractpartnerService.createContractpartner(contractpartner);
	}

	@Test(expected = BusinessException.class)
	public void test_updateWithInvalidEntity_raisesException() {
		final Contractpartner contractpartner = new Contractpartner();
		contractpartner.setUser(new User(new UserID(1L)));

		this.contractpartnerService.updateContractpartner(contractpartner);
	}

	@Test
	@Ignore("this does not work yet - see wiki TODO list")
	public void test_userAeditsContractpartner_userBsameGroupSeesCachedChange() {
		final UserID user1ID = new UserID(UserTransportBuilder.USER1_ID);
		final UserID user2ID = new UserID(UserTransportBuilder.USER2_ID);

		// this caches
		Contractpartner contractpartner = this.contractpartnerService.getContractpartnerById(user1ID,
				new ContractpartnerID(ContractpartnerTransportBuilder.CONTRACTPARTNER1_ID));

		contractpartner = this.contractpartnerService.getContractpartnerById(user2ID,
				new ContractpartnerID(ContractpartnerTransportBuilder.CONTRACTPARTNER1_ID));

		final String name = String.valueOf(System.currentTimeMillis());

		contractpartner.getUser().setId(user2ID);
		contractpartner.setName(name);

		// this should also modify the cache of user 1!
		this.contractpartnerService.updateContractpartner(contractpartner);

		// this should now retrieve the changed cache entry!
		contractpartner = this.contractpartnerService.getContractpartnerById(user1ID,
				new ContractpartnerID(ContractpartnerTransportBuilder.CONTRACTPARTNER1_ID));

		Assert.assertEquals(name, contractpartner.getName());
	}

	@Test
	@Ignore("this does not work yet - see wiki TODO list")
	public void test_userAaddsAContractpartner_userBsameGroupSeessItTooBecauseCacheWasReset() {
		final UserID user1ID = new UserID(UserTransportBuilder.USER1_ID);
		final UserID user2ID = new UserID(UserTransportBuilder.USER2_ID);

		// this caches
		final List<Contractpartner> allContractpartners1 = this.contractpartnerService.getAllContractpartners(user1ID);

		final Contractpartner contractpartner = this.contractpartnerService.getContractpartnerById(user2ID,
				new ContractpartnerID(ContractpartnerTransportBuilder.CONTRACTPARTNER1_ID));

		final String name = String.valueOf(System.currentTimeMillis());

		contractpartner.getUser().setId(user2ID);
		contractpartner.setName(name);

		// this should also modify the cache of user 1!
		this.contractpartnerService.createContractpartner(contractpartner);

		final List<Contractpartner> allContractpartners2 = this.contractpartnerService.getAllContractpartners(user1ID);

		// Cache of user1 should have been invalidated and the added Contractparter should be now in
		// the List of all partners.
		Assert.assertNotEquals(allContractpartners1.size(), allContractpartners2.size());
	}
}
