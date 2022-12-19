package org.laladev.moneyjinn.server.controller.postingaccount;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.core.rest.model.ErrorResponse;
import org.laladev.moneyjinn.core.rest.model.postingaccount.ShowDeletePostingAccountResponse;
import org.laladev.moneyjinn.server.builder.PostingAccountTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.jdbc.Sql;

public class ShowDeletePostingAccountTest extends AbstractControllerTest {

	private final HttpMethod method = HttpMethod.GET;
	private String userName;
	private String userPassword;

	@BeforeEach
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
	public void test_unknownPostingAccount_emptyResponseObject() throws Exception {
		final ShowDeletePostingAccountResponse expected = new ShowDeletePostingAccountResponse();
		final ShowDeletePostingAccountResponse actual = super.callUsecaseWithoutContent(
				"/" + PostingAccountTransportBuilder.NON_EXISTING_ID, this.method, false,
				ShowDeletePostingAccountResponse.class);

		Assertions.assertEquals(expected, actual);
	}

	@Test
	public void test_PostingAccount1_completeResponseObject() throws Exception {
		final ShowDeletePostingAccountResponse expected = new ShowDeletePostingAccountResponse();
		expected.setPostingAccountTransport(new PostingAccountTransportBuilder().forPostingAccount1().build());

		final ShowDeletePostingAccountResponse actual = super.callUsecaseWithoutContent(
				"/" + PostingAccountTransportBuilder.POSTING_ACCOUNT1_ID, this.method, false,
				ShowDeletePostingAccountResponse.class);

		Assertions.assertEquals(expected, actual);
	}

	@Test
	public void test_OnlyAdminAllowed_ErrorResponse() throws Exception {
		this.userName = UserTransportBuilder.USER1_NAME;
		this.userPassword = UserTransportBuilder.USER1_PASSWORD;

		final ErrorResponse actual = super.callUsecaseWithoutContent(
				"/" + PostingAccountTransportBuilder.POSTING_ACCOUNT2_ID, this.method, false, ErrorResponse.class);

		Assertions.assertEquals(Integer.valueOf(ErrorCode.USER_IS_NO_ADMIN.getErrorCode()), actual.getCode());

	}

	@Test
	public void test_AuthorizationRequired_Error() throws Exception {
		this.userName = null;
		this.userPassword = null;
		final ErrorResponse actual = super.callUsecaseWithoutContent("/1", this.method, false, ErrorResponse.class);
		Assertions.assertEquals(super.accessDeniedErrorResponse(), actual);
	}

	@Test
	@Sql("classpath:h2defaults.sql")
	public void test_emptyDatabase_noException() throws Exception {
		this.userName = UserTransportBuilder.ADMIN_NAME;
		this.userPassword = UserTransportBuilder.ADMIN_PASSWORD;

		super.callUsecaseWithoutContent("/" + PostingAccountTransportBuilder.POSTING_ACCOUNT1_ID, this.method, false,
				ShowDeletePostingAccountResponse.class);
	}

}