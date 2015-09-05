package org.laladev.moneyjinn.server.controller.postingaccount;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.laladev.moneyjinn.businesslogic.model.ErrorCode;
import org.laladev.moneyjinn.core.rest.model.ErrorResponse;
import org.laladev.moneyjinn.core.rest.model.postingaccount.ShowEditPostingAccountResponse;
import org.laladev.moneyjinn.server.builder.PostingAccountTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.springframework.http.HttpMethod;

public class ShowEditPostingAccountTest extends AbstractControllerTest {

	private final HttpMethod method = HttpMethod.GET;
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
		return super.getUsecaseFromTestClassName("postingaccount", this.getClass());
	}

	@Test
	public void test_unknownPostingAccount_emptyResponseObject() throws Exception {
		final ShowEditPostingAccountResponse expected = new ShowEditPostingAccountResponse();
		final ShowEditPostingAccountResponse actual = super.callUsecaseWithoutContent(
				"/" + PostingAccountTransportBuilder.NON_EXISTING_ID, this.method, false,
				ShowEditPostingAccountResponse.class);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void test_PostingAccount1_completeResponseObject() throws Exception {
		final ShowEditPostingAccountResponse expected = new ShowEditPostingAccountResponse();
		expected.setPostingAccountTransport(new PostingAccountTransportBuilder().forPostingAccount1().build());

		final ShowEditPostingAccountResponse actual = super.callUsecaseWithoutContent(
				"/" + PostingAccountTransportBuilder.POSTING_ACCOUNT1_ID, this.method, false,
				ShowEditPostingAccountResponse.class);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void test_OnlyAdminAllowed_ErrorResponse() throws Exception {
		this.userName = UserTransportBuilder.USER1_NAME;
		this.userPassword = UserTransportBuilder.USER1_PASSWORD;

		final ErrorResponse actual = super.callUsecaseWithoutContent(
				"/" + PostingAccountTransportBuilder.POSTING_ACCOUNT2_ID, this.method, false, ErrorResponse.class);

		Assert.assertEquals(new Integer(ErrorCode.USER_IS_NO_ADMIN.getErrorCode()), actual.getCode());

	}

}