package org.laladev.moneyjinn.businesslogic.service.impl;

import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.AbstractTest;
import org.laladev.moneyjinn.model.ContractpartnerAccount;
import org.laladev.moneyjinn.model.ContractpartnerAccountID;
import org.laladev.moneyjinn.model.ContractpartnerID;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.exception.BusinessException;
import org.laladev.moneyjinn.server.builder.ContractpartnerAccountTransportBuilder;
import org.laladev.moneyjinn.server.builder.ContractpartnerTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.service.api.IContractpartnerAccountService;

import java.util.List;

class ContractpartnerAccountServiceTest extends AbstractTest {
    @Inject
    private IContractpartnerAccountService contractpartnerAccountService;

    @Test
    void test_createWithInvalidEntity_raisesException() {
        final ContractpartnerAccount contractpartnerAccount = new ContractpartnerAccount();
        final UserID userId = new UserID(1L);
        Assertions.assertThrows(BusinessException.class, () -> this.contractpartnerAccountService.createContractpartnerAccount(userId, contractpartnerAccount));
    }

    @Test
    void test_updateWithInvalidEntity_raisesException() {
        final ContractpartnerAccount contractpartnerAccount = new ContractpartnerAccount();
        final UserID userId = new UserID(1L);
        Assertions.assertThrows(BusinessException.class, () -> this.contractpartnerAccountService.updateContractpartnerAccount(userId, contractpartnerAccount));
    }

    @Test
    void test_userAeditsContractpartnerAccount_userBsameGroupSeesCachedChange() {
        final UserID user1Id = new UserID(UserTransportBuilder.USER1_ID);
        final UserID user2Id = new UserID(UserTransportBuilder.USER2_ID);
        // this caches
        this.contractpartnerAccountService.getContractpartnerAccountById(user2Id, new ContractpartnerAccountID(
                ContractpartnerAccountTransportBuilder.CONTRACTPARTNER_ACCOUNT1_ID));
        ContractpartnerAccount contractpartnerAccount = this.contractpartnerAccountService
                .getContractpartnerAccountById(user1Id,
                        new ContractpartnerAccountID(
                                ContractpartnerAccountTransportBuilder.CONTRACTPARTNER_ACCOUNT1_ID));
        final String comment = String.valueOf(System.currentTimeMillis());
        contractpartnerAccount.getBankAccount().setAccountNumber(comment);
        // this should also modify the cache of user 1!
        this.contractpartnerAccountService.updateContractpartnerAccount(user1Id, contractpartnerAccount);
        // this should now retrieve the changed cache entry!
        contractpartnerAccount = this.contractpartnerAccountService.getContractpartnerAccountById(user2Id,
                new ContractpartnerAccountID(ContractpartnerAccountTransportBuilder.CONTRACTPARTNER_ACCOUNT1_ID));
        Assertions.assertEquals(comment, contractpartnerAccount.getBankAccount().getAccountNumber());
    }

    @Test
    void test_userAaddsAContractpartnerAccount_userBsameGroupSeessItTooBecauseCacheWasReset() {
        final UserID user1Id = new UserID(UserTransportBuilder.USER1_ID);
        final UserID user2Id = new UserID(UserTransportBuilder.USER2_ID);
        final ContractpartnerID contractpartnerId = new ContractpartnerID(
                ContractpartnerTransportBuilder.CONTRACTPARTNER1_ID);
        // this caches
        final List<ContractpartnerAccount> allContractpartnerAccounts1 = this.contractpartnerAccountService
                .getContractpartnerAccounts(user1Id, contractpartnerId);
        final ContractpartnerAccount contractpartnerAccount = this.contractpartnerAccountService
                .getContractpartnerAccountById(user2Id, new ContractpartnerAccountID(
                        ContractpartnerAccountTransportBuilder.CONTRACTPARTNER_ACCOUNT1_ID));
        final String comment = String.valueOf(System.currentTimeMillis());
        contractpartnerAccount.getBankAccount().setAccountNumber(comment);
        // this should also modify the cache of user 1!
        this.contractpartnerAccountService.createContractpartnerAccount(user2Id, contractpartnerAccount);
        final List<ContractpartnerAccount> allContractpartnerAccounts2 = this.contractpartnerAccountService
                .getContractpartnerAccounts(user1Id, contractpartnerId);
        // Cache of user1 should have been invalidated and the added
        // ContractpartnerAccount should
        // be now in the List of all ContractpartnerAccounts.
        Assertions.assertNotEquals(allContractpartnerAccounts1.size(), allContractpartnerAccounts2.size());
    }
}
