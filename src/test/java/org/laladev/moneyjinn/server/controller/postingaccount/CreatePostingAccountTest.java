package org.laladev.moneyjinn.server.controller.postingaccount;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.laladev.moneyjinn.businesslogic.model.ErrorCode;
import org.laladev.moneyjinn.businesslogic.model.PostingAccount;
import org.laladev.moneyjinn.businesslogic.service.api.IAccessRelationService;
import org.laladev.moneyjinn.businesslogic.service.api.IPostingAccountService;
import org.laladev.moneyjinn.core.rest.model.ErrorResponse;
import org.laladev.moneyjinn.core.rest.model.ValidationResponse;
import org.laladev.moneyjinn.core.rest.model.postingaccount.CreatePostingAccountRequest;
import org.laladev.moneyjinn.core.rest.model.transport.PostingAccountTransport;
import org.laladev.moneyjinn.core.rest.model.transport.ValidationItemTransport;
import org.laladev.moneyjinn.server.builder.PostingAccountTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.builder.ValidationItemTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.springframework.http.HttpMethod;

public class CreatePostingAccountTest extends AbstractControllerTest {

	@Inject
	IPostingAccountService postingAccountService;

	@Inject
	IAccessRelationService accessRelationService;

	private final HttpMethod method = HttpMethod.POST;
	private String userName;
	private String userPassword;

	@Before
	public void setUp() {
		this.userName = UserTransportBuilder.ADMIN_NAME;
		this.userPassword = UserTransportBuilder.ADMIN_PASSWORD;
	}

	@Override
	protected String getUsername() {
		return this.userName;
	}

	@Override
	protected String getPassword() {
		return this.userPassword;
	}

	@Override
	protected String getUsecase() {
		return super.getUsecaseFromTestClassName(this.getClass());
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

		final ValidationResponse actual = super.callUsecaseWithContent("", this.method, request, false,
				ValidationResponse.class);

		Assert.assertEquals(expected, actual);

	}

	@Test
	public void test_PostingAccountnameAlreadyExisting_Error() throws Exception {

		final PostingAccountTransport transport = new PostingAccountTransportBuilder().forNewPostingAccount().build();
		transport.setName(PostingAccountTransportBuilder.POSTING_ACCOUNT1_NAME);

		this.testError(transport, ErrorCode.POSTINGACCOUNT_WITH_SAME_NAME_ALREADY_EXISTS);
	}

	@Test
	public void test_emptyPostingAccountname_Error() throws Exception {
		final PostingAccountTransport transport = new PostingAccountTransportBuilder().forNewPostingAccount().build();
		transport.setName("");

		this.testError(transport, ErrorCode.NAME_MUST_NOT_BE_EMPTY);
	}

	@Test
	public void test_nullPostingAccountname_Error() throws Exception {
		final PostingAccountTransport transport = new PostingAccountTransportBuilder().forNewPostingAccount().build();
		transport.setName(null);

		this.testError(transport, ErrorCode.NAME_MUST_NOT_BE_EMPTY);
	}

	@Test
	public void test_standardRequest_SuccessfullNoContent() throws Exception {
		final CreatePostingAccountRequest request = new CreatePostingAccountRequest();

		final PostingAccountTransport transport = new PostingAccountTransportBuilder().forNewPostingAccount().build();

		request.setPostingAccountTransport(transport);

		super.callUsecaseWithContent("", this.method, request, true, Object.class);

		final PostingAccount postingAccount = this.postingAccountService
				.getPostingAccountByName(PostingAccountTransportBuilder.NEWPOSTING_ACCOUNT_NAME);

		Assert.assertEquals(PostingAccountTransportBuilder.NEXT_ID, postingAccount.getId().getId());
		Assert.assertEquals(PostingAccountTransportBuilder.NEWPOSTING_ACCOUNT_NAME, postingAccount.getName());
	}

	@Test
	public void test_OnlyAdminAllowed_ErrorResponse() throws Exception {
		this.userName = UserTransportBuilder.USER1_NAME;
		this.userPassword = UserTransportBuilder.USER1_PASSWORD;

		final CreatePostingAccountRequest request = new CreatePostingAccountRequest();
		final ErrorResponse actual = super.callUsecaseWithContent("", this.method, request, false, ErrorResponse.class);

		Assert.assertEquals(new Integer(ErrorCode.USER_IS_NO_ADMIN.getErrorCode()), actual.getCode());

	}

	@Test
	public void test_AuthorizationRequired_Error() throws Exception {
		this.userName = null;
		this.userPassword = null;
		final ErrorResponse actual = super.callUsecaseWithoutContent("", this.method, false, ErrorResponse.class);
		Assert.assertEquals(super.accessDeniedErrorResponse(), actual);
	}

}