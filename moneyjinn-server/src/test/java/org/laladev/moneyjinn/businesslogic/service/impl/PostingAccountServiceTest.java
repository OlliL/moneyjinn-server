
package org.laladev.moneyjinn.businesslogic.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.AbstractTest;
import org.laladev.moneyjinn.model.PostingAccount;
import org.laladev.moneyjinn.model.exception.BusinessException;
import org.laladev.moneyjinn.service.api.IPostingAccountService;

import jakarta.inject.Inject;

class PostingAccountServiceTest extends AbstractTest {
	@Inject
	private IPostingAccountService postingAccountService;

	@Test
	void test_createWithInvalidEntity_raisesException() {
		final PostingAccount postingAccount = new PostingAccount();
		Assertions.assertThrows(BusinessException.class, () -> {
			this.postingAccountService.createPostingAccount(postingAccount);
		});
	}

	@Test
	void test_updateWithInvalidEntity_raisesException() {
		final PostingAccount postingAccount = new PostingAccount();
		Assertions.assertThrows(BusinessException.class, () -> {
			this.postingAccountService.updatePostingAccount(postingAccount);
		});
	}
}
