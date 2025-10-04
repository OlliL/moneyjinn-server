package org.laladev.moneyjinn.businesslogic.service.impl;

import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.AbstractTest;
import org.laladev.moneyjinn.model.PostingAccount;
import org.laladev.moneyjinn.model.exception.BusinessException;
import org.laladev.moneyjinn.service.api.IPostingAccountService;

import static org.junit.jupiter.api.Assertions.assertThrows;

class PostingAccountServiceTest extends AbstractTest {
    @Inject
    private IPostingAccountService postingAccountService;

    @Test
    void test_createWithInvalidEntity_raisesException() {
        final PostingAccount postingAccount = new PostingAccount();
        assertThrows(BusinessException.class,
                () -> this.postingAccountService.createPostingAccount(postingAccount));
    }

    @Test
    void test_updateWithInvalidEntity_raisesException() {
        final PostingAccount postingAccount = new PostingAccount();
        assertThrows(BusinessException.class,
                () -> this.postingAccountService.updatePostingAccount(postingAccount));
    }
}
