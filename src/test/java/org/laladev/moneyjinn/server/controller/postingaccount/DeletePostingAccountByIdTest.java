package org.laladev.moneyjinn.server.controller.postingaccount;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.laladev.moneyjinn.businesslogic.model.ErrorCode;
import org.laladev.moneyjinn.businesslogic.model.PostingAccount;
import org.laladev.moneyjinn.businesslogic.model.PostingAccountID;
import org.laladev.moneyjinn.businesslogic.service.api.IPostingAccountService;
import org.laladev.moneyjinn.core.rest.model.ErrorResponse;
import org.laladev.moneyjinn.server.builder.PostingAccountTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.springframework.http.HttpMethod;

public class DeletePostingAccountByIdTest extends AbstractControllerTest {

	@Inject
	IPostingAccountService postingAccountService;

	private final HttpMethod method = HttpMethod.DELETE;
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

	@Test
	public void test_regularPostingAccountNoData_SuccessfullNoContent() throws Exception {
		PostingAccount postingAccount = this.postingAccountService
				.getPostingAccountById(new PostingAccountID(PostingAccountTransportBuilder.POSTING_ACCOUNT3_ID));

		Assert.assertNotNull(postingAccount);

		super.callUsecaseWithoutContent("/" + PostingAccountTransportBuilder.POSTING_ACCOUNT3_ID, this.method, true,
				Object.class);

		postingAccount = this.postingAccountService
				.getPostingAccountById(new PostingAccountID(PostingAccountTransportBuilder.POSTING_ACCOUNT3_ID));

		Assert.assertNull(postingAccount);
	}

	@Test
	public void test_nonExistingPostingAccount_SuccessfullNoContent() throws Exception {
		PostingAccount postingAccount = this.postingAccountService
				.getPostingAccountById(new PostingAccountID(PostingAccountTransportBuilder.NON_EXISTING_ID));

		Assert.assertNull(postingAccount);

		super.callUsecaseWithoutContent("/" + PostingAccountTransportBuilder.NON_EXISTING_ID, this.method, true,
				Object.class);

		postingAccount = this.postingAccountService
				.getPostingAccountById(new PostingAccountID(PostingAccountTransportBuilder.NON_EXISTING_ID));

		Assert.assertNull(postingAccount);
	}

	@Test
	public void test_regularPostingAccountWithData_SuccessfullNoContent() throws Exception {
		final ErrorResponse expected = new ErrorResponse();
		expected.setCode(ErrorCode.POSTINGACCOUNT_STILL_REFERENCED.getErrorCode());
		expected.setMessage(
				"The posting account cannot be deleted because it is still referenced by a flow of money or a predefined flow of money!");

		PostingAccount postingAccount = this.postingAccountService
				.getPostingAccountById(new PostingAccountID(PostingAccountTransportBuilder.POSTING_ACCOUNT1_ID));

		Assert.assertNotNull(postingAccount);

		final ErrorResponse response = super.callUsecaseWithoutContent(
				"/" + PostingAccountTransportBuilder.POSTING_ACCOUNT1_ID, this.method, false, ErrorResponse.class);

		postingAccount = this.postingAccountService
				.getPostingAccountById(new PostingAccountID(PostingAccountTransportBuilder.POSTING_ACCOUNT1_ID));

		Assert.assertNotNull(postingAccount);

		Assert.assertEquals(expected, response);
	}

	@Test
	public void test_OnlyAdminAllowed_ErrorResponse() throws Exception {
		this.userName = UserTransportBuilder.USER1_NAME;
		this.userPassword = UserTransportBuilder.USER1_PASSWORD;

		final ErrorResponse actual = super.callUsecaseWithoutContent(
				"/" + PostingAccountTransportBuilder.POSTING_ACCOUNT1_ID, this.method, false, ErrorResponse.class);

		Assert.assertEquals(new Integer(ErrorCode.USER_IS_NO_ADMIN.getErrorCode()), actual.getCode());

	}

	@Test
	public void test_AuthorizationRequired_Error() throws Exception {
		this.userName = null;
		this.userPassword = null;
		final ErrorResponse actual = super.callUsecaseWithoutContent("/1", this.method, false, ErrorResponse.class);
		Assert.assertEquals(super.accessDeniedErrorResponse(), actual);
	}

}