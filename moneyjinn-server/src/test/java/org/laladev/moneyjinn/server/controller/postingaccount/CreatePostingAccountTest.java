
package org.laladev.moneyjinn.server.controller.postingaccount;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.model.PostingAccount;
import org.laladev.moneyjinn.server.builder.PostingAccountTransportBuilder;
import org.laladev.moneyjinn.server.builder.ValidationItemTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractAdminUserControllerTest;
import org.laladev.moneyjinn.server.controller.api.PostingAccountControllerApi;
import org.laladev.moneyjinn.server.model.CreatePostingAccountRequest;
import org.laladev.moneyjinn.server.model.CreatePostingAccountResponse;
import org.laladev.moneyjinn.server.model.PostingAccountTransport;
import org.laladev.moneyjinn.server.model.ValidationItemTransport;
import org.laladev.moneyjinn.server.model.ValidationResponse;
import org.laladev.moneyjinn.service.api.IPostingAccountService;

import jakarta.inject.Inject;

class CreatePostingAccountTest extends AbstractAdminUserControllerTest {
	@Inject
	private IPostingAccountService postingAccountService;

	@Override
	protected void loadMethod() {
		super.getMock(PostingAccountControllerApi.class).createPostingAccount(null);
	}

	private void testError(final PostingAccountTransport transport, final ErrorCode errorCode) throws Exception {
		final CreatePostingAccountRequest request = new CreatePostingAccountRequest();
		request.setPostingAccountTransport(transport);
		final List<ValidationItemTransport> validationItems = new ArrayList<>();
		validationItems
				.add(new ValidationItemTransportBuilder().withKey(null).withError(errorCode.getErrorCode()).build());
		final ValidationResponse expected = new ValidationResponse();
		expected.setValidationItemTransports(validationItems);
		expected.setResult(Boolean.FALSE);

		final ValidationResponse actual = super.callUsecaseExpect422(request, ValidationResponse.class);

		Assertions.assertEquals(expected, actual);
	}

	@Test
	void test_PostingAccountnameAlreadyExisting_Error() throws Exception {
		final PostingAccountTransport transport = new PostingAccountTransportBuilder().forNewPostingAccount().build();
		transport.setName(PostingAccountTransportBuilder.POSTING_ACCOUNT1_NAME);
		this.testError(transport, ErrorCode.POSTINGACCOUNT_WITH_SAME_NAME_ALREADY_EXISTS);
	}

	@Test
	void test_emptyPostingAccountname_Error() throws Exception {
		final PostingAccountTransport transport = new PostingAccountTransportBuilder().forNewPostingAccount().build();
		transport.setName("");
		this.testError(transport, ErrorCode.NAME_MUST_NOT_BE_EMPTY);
	}

	@Test
	void test_nullPostingAccountname_Error() throws Exception {
		final PostingAccountTransport transport = new PostingAccountTransportBuilder().forNewPostingAccount().build();
		transport.setName(null);
		this.testError(transport, ErrorCode.NAME_MUST_NOT_BE_EMPTY);
	}

	@Test
	void test_standardRequest_SuccessfullNoContent() throws Exception {
		final CreatePostingAccountRequest request = new CreatePostingAccountRequest();
		final PostingAccountTransport transport = new PostingAccountTransportBuilder().forNewPostingAccount().build();
		request.setPostingAccountTransport(transport);
		final CreatePostingAccountResponse expected = new CreatePostingAccountResponse();
		expected.setPostingAccountId(PostingAccountTransportBuilder.NEXT_ID);

		final CreatePostingAccountResponse actual = super.callUsecaseExpect200(request,
				CreatePostingAccountResponse.class);

		Assertions.assertEquals(expected, actual);

		final PostingAccount postingAccount = this.postingAccountService
				.getPostingAccountByName(PostingAccountTransportBuilder.NEWPOSTING_ACCOUNT_NAME);
		Assertions.assertEquals(PostingAccountTransportBuilder.NEXT_ID, postingAccount.getId().getId());
		Assertions.assertEquals(PostingAccountTransportBuilder.NEXT_ID, actual.getPostingAccountId());
		Assertions.assertEquals(PostingAccountTransportBuilder.NEWPOSTING_ACCOUNT_NAME, postingAccount.getName());
	}

	@Override
	protected void callUsecaseExpect403ForThisUsecase() throws Exception {
		super.callUsecaseExpect403(new CreatePostingAccountRequest());
	}

	@Override
	protected void callUsecaseEmptyDatabase() throws Exception {
		final CreatePostingAccountRequest request = new CreatePostingAccountRequest();
		final PostingAccountTransport transport = new PostingAccountTransportBuilder().forNewPostingAccount().build();
		request.setPostingAccountTransport(transport);
		final CreatePostingAccountResponse expected = new CreatePostingAccountResponse();
		expected.setPostingAccountId(1L);

		final CreatePostingAccountResponse actual = super.callUsecaseExpect200(request,
				CreatePostingAccountResponse.class);

		Assertions.assertEquals(expected, actual);
	}
}