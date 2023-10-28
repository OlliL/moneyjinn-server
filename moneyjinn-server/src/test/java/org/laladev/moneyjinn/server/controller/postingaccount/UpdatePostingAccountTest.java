
package org.laladev.moneyjinn.server.controller.postingaccount;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.model.PostingAccount;
import org.laladev.moneyjinn.model.PostingAccountID;
import org.laladev.moneyjinn.server.builder.PostingAccountTransportBuilder;
import org.laladev.moneyjinn.server.builder.ValidationItemTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractAdminUserControllerTest;
import org.laladev.moneyjinn.server.controller.api.PostingAccountControllerApi;
import org.laladev.moneyjinn.server.model.PostingAccountTransport;
import org.laladev.moneyjinn.server.model.UpdatePostingAccountRequest;
import org.laladev.moneyjinn.server.model.ValidationItemTransport;
import org.laladev.moneyjinn.server.model.ValidationResponse;
import org.laladev.moneyjinn.service.api.IPostingAccountService;

import jakarta.inject.Inject;

class UpdatePostingAccountTest extends AbstractAdminUserControllerTest {
	@Inject
	private IPostingAccountService postingAccountService;

	@Override
	protected void loadMethod() {
		super.getMock(PostingAccountControllerApi.class).updatePostingAccount(null);
	}

	private void testError(final PostingAccountTransport transport, final ErrorCode errorCode) throws Exception {
		final UpdatePostingAccountRequest request = new UpdatePostingAccountRequest();
		request.setPostingAccountTransport(transport);
		final List<ValidationItemTransport> validationItems = new ArrayList<>();
		validationItems.add(new ValidationItemTransportBuilder().withKey(transport.getId().toString())
				.withError(errorCode.getErrorCode()).build());
		final ValidationResponse expected = new ValidationResponse();
		expected.setValidationItemTransports(validationItems);
		expected.setResult(Boolean.FALSE);

		final ValidationResponse actual = super.callUsecaseExpect422(request, ValidationResponse.class);

		Assertions.assertEquals(expected, actual);
	}

	@Test
	void test_PostingAccountnameAlreadyExisting_Error() throws Exception {
		final PostingAccountTransport transport = new PostingAccountTransportBuilder().forPostingAccount2().build();
		transport.setName(PostingAccountTransportBuilder.POSTING_ACCOUNT1_NAME);
		this.testError(transport, ErrorCode.POSTINGACCOUNT_WITH_SAME_NAME_ALREADY_EXISTS);
	}

	@Test
	void test_EmptyPostingAccountname_Error() throws Exception {
		final PostingAccountTransport transport = new PostingAccountTransportBuilder().forPostingAccount2().build();
		transport.setName("");
		this.testError(transport, ErrorCode.NAME_MUST_NOT_BE_EMPTY);
	}

	@Test
	void test_standardRequest_Successfull() throws Exception {
		final UpdatePostingAccountRequest request = new UpdatePostingAccountRequest();
		final PostingAccountTransport transport = new PostingAccountTransportBuilder().forPostingAccount1().build();
		transport.setName("hugo");
		request.setPostingAccountTransport(transport);

		super.callUsecaseExpect204(request);

		final PostingAccount postingAccount = this.postingAccountService
				.getPostingAccountById(new PostingAccountID(PostingAccountTransportBuilder.POSTING_ACCOUNT1_ID));
		Assertions.assertEquals(PostingAccountTransportBuilder.POSTING_ACCOUNT1_ID, postingAccount.getId().getId());
		Assertions.assertEquals("hugo", postingAccount.getName());
	}

	@Override
	protected void callUsecaseExpect403ForThisUsecase() throws Exception {
		super.callUsecaseExpect403(new UpdatePostingAccountRequest());
	}

	@Override
	protected void callUsecaseEmptyDatabase() throws Exception {
		final UpdatePostingAccountRequest request = new UpdatePostingAccountRequest();
		final PostingAccountTransport transport = new PostingAccountTransportBuilder().forPostingAccount1().build();
		request.setPostingAccountTransport(transport);

		super.callUsecaseExpect204(request);
	}
}