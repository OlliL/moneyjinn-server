
package org.laladev.moneyjinn.businesslogic.service.impl;

import jakarta.inject.Inject;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.AbstractTest;
import org.laladev.moneyjinn.model.Contractpartner;
import org.laladev.moneyjinn.model.ContractpartnerID;
import org.laladev.moneyjinn.model.access.Group;
import org.laladev.moneyjinn.model.access.GroupID;
import org.laladev.moneyjinn.model.access.User;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.exception.BusinessException;
import org.laladev.moneyjinn.server.builder.ContractpartnerTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.service.api.IContractpartnerService;

public class ContractpartnerServiceTest extends AbstractTest {
  @Inject
  private IContractpartnerService contractpartnerService;

  @Test
  public void test_validateNullUser_raisesException() {
    final Contractpartner contractpartner = new Contractpartner();
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      this.contractpartnerService.validateContractpartner(contractpartner);
    });
  }

  @Test
  public void test_createWithInvalidEntity_raisesException() {
    final Contractpartner contractpartner = new Contractpartner();
    contractpartner.setUser(new User(new UserID(1L)));
    contractpartner.setAccess(new Group(new GroupID(1L)));
    Assertions.assertThrows(BusinessException.class, () -> {
      this.contractpartnerService.createContractpartner(contractpartner);
    });
  }

  @Test
  public void test_updateWithInvalidEntity_raisesException() {
    final Contractpartner contractpartner = new Contractpartner();
    contractpartner.setUser(new User(new UserID(1L)));
    contractpartner.setAccess(new Group(new GroupID(1L)));
    Assertions.assertThrows(BusinessException.class, () -> {
      this.contractpartnerService.updateContractpartner(contractpartner);
    });
  }

  @Test
  public void test_userAeditsContractpartner_userBsameGroupSeesCachedChange() {
    final UserID user1Id = new UserID(UserTransportBuilder.USER1_ID);
    final UserID user2Id = new UserID(UserTransportBuilder.USER2_ID);
    // this caches
    Contractpartner contractpartner = this.contractpartnerService.getContractpartnerById(user1Id,
        new ContractpartnerID(ContractpartnerTransportBuilder.CONTRACTPARTNER1_ID));
    contractpartner = this.contractpartnerService.getContractpartnerById(user2Id,
        new ContractpartnerID(ContractpartnerTransportBuilder.CONTRACTPARTNER1_ID));
    final String name = String.valueOf(System.currentTimeMillis());
    contractpartner.getUser().setId(user2Id);
    contractpartner.setName(name);
    // this should also modify the cache of user 1!
    this.contractpartnerService.updateContractpartner(contractpartner);
    // this should now retrieve the changed cache entry!
    contractpartner = this.contractpartnerService.getContractpartnerById(user1Id,
        new ContractpartnerID(ContractpartnerTransportBuilder.CONTRACTPARTNER1_ID));
    Assertions.assertEquals(name, contractpartner.getName());
  }

  @Test
  public void test_userAaddsAContractpartner_userBsameGroupSeessItTooBecauseCacheWasReset() {
    final UserID user1Id = new UserID(UserTransportBuilder.USER1_ID);
    final UserID user2Id = new UserID(UserTransportBuilder.USER2_ID);
    // this caches
    final List<Contractpartner> allContractpartners1 = this.contractpartnerService
        .getAllContractpartners(user1Id);
    final Contractpartner contractpartner = this.contractpartnerService.getContractpartnerById(
        user2Id, new ContractpartnerID(ContractpartnerTransportBuilder.CONTRACTPARTNER1_ID));
    final String name = String.valueOf(System.currentTimeMillis());
    contractpartner.getUser().setId(user2Id);
    contractpartner.setName(name);
    // this should also modify the cache of user 1!
    this.contractpartnerService.createContractpartner(contractpartner);
    final List<Contractpartner> allContractpartners2 = this.contractpartnerService
        .getAllContractpartners(user1Id);
    // Cache of user1 should have been invalidated and the added Contractpartner should be now
    // in
    // the List of all partners.
    Assertions.assertNotEquals(allContractpartners1.size(), allContractpartners2.size());
  }
}
