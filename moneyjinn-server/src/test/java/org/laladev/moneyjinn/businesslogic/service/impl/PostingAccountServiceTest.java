package org.laladev.moneyjinn.businesslogic.service.impl;

import javax.inject.Inject;

import org.junit.Test;
import org.laladev.moneyjinn.AbstractTest;
import org.laladev.moneyjinn.model.PostingAccount;
import org.laladev.moneyjinn.model.exception.BusinessException;
import org.laladev.moneyjinn.service.api.IPostingAccountService;

public class PostingAccountServiceTest extends AbstractTest {
	@Inject
	private IPostingAccountService postingAccountService;

	@Test(expected = BusinessException.class)
	public void test_createWithInvalidEntity_raisesException() {
		final PostingAccount postingAccount = new PostingAccount();

		this.postingAccountService.createPostingAccount(postingAccount);
	}

	@Test(expected = BusinessException.class)
	public void test_updateWithInvalidEntity_raisesException() {
		final PostingAccount postingAccount = new PostingAccount();

		this.postingAccountService.updatePostingAccount(postingAccount);
	}
}
