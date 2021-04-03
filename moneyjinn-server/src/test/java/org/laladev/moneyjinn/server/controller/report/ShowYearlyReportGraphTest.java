package org.laladev.moneyjinn.server.controller.report;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.core.rest.model.ErrorResponse;
import org.laladev.moneyjinn.core.rest.model.report.ShowYearlyReportGraphRequest;
import org.laladev.moneyjinn.core.rest.model.report.ShowYearlyReportGraphResponse;
import org.laladev.moneyjinn.core.rest.model.report.transport.PostingAccountAmountTransport;
import org.laladev.moneyjinn.core.rest.model.transport.PostingAccountTransport;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.setting.ClientReportingUnselectedPostingAccountIdsSetting;
import org.laladev.moneyjinn.server.builder.PostingAccountAmountTransportBuilder;
import org.laladev.moneyjinn.server.builder.PostingAccountTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.laladev.moneyjinn.service.api.ISettingService;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.jdbc.Sql;

public class ShowYearlyReportGraphTest extends AbstractControllerTest {

	@Inject
	private ISettingService settingService;

	private final HttpMethod method = HttpMethod.PUT;
	private String userName;
	private String userPassword;

	@BeforeEach
	public void setUp() {
		this.userName = UserTransportBuilder.USER1_NAME;
		this.userPassword = UserTransportBuilder.USER1_PASSWORD;
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

	@SuppressWarnings("deprecation")
	@Test
	public void test_maxDateRange_response() throws Exception {
		final ShowYearlyReportGraphRequest request = new ShowYearlyReportGraphRequest();
		request.setStartDate(new Date(70, 0, 1));
		request.setEndDate(new Date(199, 11, 31));
		request.setPostingAccountIdsYes(Arrays.asList(PostingAccountTransportBuilder.POSTING_ACCOUNT1_ID,
				PostingAccountTransportBuilder.POSTING_ACCOUNT2_ID));

		final ShowYearlyReportGraphResponse expected = new ShowYearlyReportGraphResponse();
		final List<PostingAccountAmountTransport> postingAccountAmountTransports = new ArrayList<>();
		// Respect Moneyflow Split Entries -1.10 -> -1.00
		postingAccountAmountTransports.add(new PostingAccountAmountTransportBuilder().forPostingAccount1()
				.withDate("2008-01-01").withAmount("10.10").build());
		postingAccountAmountTransports.add(new PostingAccountAmountTransportBuilder().forPostingAccount1()
				.withDate("2009-01-01").withAmount("-1.00").build());
		postingAccountAmountTransports.add(new PostingAccountAmountTransportBuilder().forPostingAccount2()
				.withDate("2009-01-01").withAmount("9.90").build());
		postingAccountAmountTransports.add(new PostingAccountAmountTransportBuilder().forPostingAccount2()
				.withDate("2010-01-01").withAmount("-10.00").build());
		expected.setPostingAccountAmountTransports(postingAccountAmountTransports);

		final List<PostingAccountTransport> postingAccountTransports = new ArrayList<>();
		postingAccountTransports.add(new PostingAccountTransportBuilder().forPostingAccount1().build());
		postingAccountTransports.add(new PostingAccountTransportBuilder().forPostingAccount2().build());
		postingAccountTransports.add(new PostingAccountTransportBuilder().forPostingAccount3().build());
		expected.setPostingAccountTransports(postingAccountTransports);

		final ShowYearlyReportGraphResponse actual = super.callUsecaseWithContent("", this.method, request, false,
				ShowYearlyReportGraphResponse.class);

		Assertions.assertEquals(expected, actual);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void test_empty_PostingAccountIdsYes_nullResponseNoError() throws Exception {
		final ShowYearlyReportGraphRequest request = new ShowYearlyReportGraphRequest();
		request.setStartDate(new Date(70, 0, 1));
		request.setEndDate(new Date(199, 11, 31));

		final ShowYearlyReportGraphResponse expected = new ShowYearlyReportGraphResponse();

		final ShowYearlyReportGraphResponse actual = super.callUsecaseWithContent("", this.method, request, false,
				ShowYearlyReportGraphResponse.class);
		Assertions.assertEquals(expected, actual);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void test_withUnselectedPostingAccountIDs_idsSaved() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);

		final ShowYearlyReportGraphRequest request = new ShowYearlyReportGraphRequest();
		request.setStartDate(new Date(70, 0, 1));
		request.setEndDate(new Date(199, 11, 31));
		request.setPostingAccountIdsYes(Arrays.asList(PostingAccountTransportBuilder.POSTING_ACCOUNT1_ID,
				PostingAccountTransportBuilder.POSTING_ACCOUNT2_ID));
		request.setPostingAccountIdsNo(Arrays.asList(PostingAccountTransportBuilder.POSTING_ACCOUNT3_ID));

		ClientReportingUnselectedPostingAccountIdsSetting setting = this.settingService
				.getClientReportingUnselectedPostingAccountIdsSetting(userId);

		Assertions.assertNull(setting);

		super.callUsecaseWithContent("", this.method, request, false, ShowYearlyReportGraphResponse.class);

		setting = this.settingService.getClientReportingUnselectedPostingAccountIdsSetting(userId);
		Assertions.assertNotNull(setting);
		Assertions.assertNotNull(setting.getSetting());
		Assertions.assertEquals(1, setting.getSetting().size());
		Assertions.assertEquals(PostingAccountTransportBuilder.POSTING_ACCOUNT3_ID, setting.getSetting().get(0).getId());
	}

	@SuppressWarnings("deprecation")
	@Test
	public void test_privateMoneyflows_ignored() throws Exception {
		this.userName = UserTransportBuilder.USER3_NAME;
		this.userPassword = UserTransportBuilder.USER3_PASSWORD;

		final ShowYearlyReportGraphRequest request = new ShowYearlyReportGraphRequest();
		request.setStartDate(new Date(70, 0, 1));
		request.setEndDate(new Date(199, 11, 31));
		request.setPostingAccountIdsYes(Arrays.asList(PostingAccountTransportBuilder.POSTING_ACCOUNT1_ID,
				PostingAccountTransportBuilder.POSTING_ACCOUNT2_ID));

		final ShowYearlyReportGraphResponse expected = new ShowYearlyReportGraphResponse();
		final List<PostingAccountAmountTransport> postingAccountAmountTransports = new ArrayList<>();
		// Respect Moneyflow Split Entries -1.10 -> -1.00
		postingAccountAmountTransports.add(new PostingAccountAmountTransportBuilder().forPostingAccount1()
				.withDate("2008-01-01").withAmount("10.10").build());
		postingAccountAmountTransports.add(new PostingAccountAmountTransportBuilder().forPostingAccount1()
				.withDate("2009-01-01").withAmount("-1.00").build());
		postingAccountAmountTransports.add(new PostingAccountAmountTransportBuilder().forPostingAccount2()
				.withDate("2009-01-01").withAmount("-0.10").build());
		postingAccountAmountTransports.add(new PostingAccountAmountTransportBuilder().forPostingAccount2()
				.withDate("2010-01-01").withAmount("-15.00").build());
		expected.setPostingAccountAmountTransports(postingAccountAmountTransports);

		final List<PostingAccountTransport> postingAccountTransports = new ArrayList<>();
		postingAccountTransports.add(new PostingAccountTransportBuilder().forPostingAccount1().build());
		postingAccountTransports.add(new PostingAccountTransportBuilder().forPostingAccount2().build());
		postingAccountTransports.add(new PostingAccountTransportBuilder().forPostingAccount3().build());
		expected.setPostingAccountTransports(postingAccountTransports);

		final ShowYearlyReportGraphResponse actual = super.callUsecaseWithContent("", this.method, request, false,
				ShowYearlyReportGraphResponse.class);

		Assertions.assertEquals(expected, actual);
	}

	@Test
	public void test_AuthorizationRequired_Error() throws Exception {
		this.userName = null;
		this.userPassword = null;
		final ErrorResponse actual = super.callUsecaseWithoutContent("", this.method, false, ErrorResponse.class);
		Assertions.assertEquals(super.accessDeniedErrorResponse(), actual);
	}

	@Test
	@Sql("classpath:h2defaults.sql")
	public void test_emptyDatabase_noException() throws Exception {
		this.userName = UserTransportBuilder.ADMIN_NAME;
		this.userPassword = UserTransportBuilder.ADMIN_PASSWORD;
		final ShowYearlyReportGraphRequest request = new ShowYearlyReportGraphRequest();
		super.callUsecaseWithContent("", this.method, request, false, ShowYearlyReportGraphResponse.class);
	}

	@SuppressWarnings("deprecation")
	@Test
	@Sql("classpath:h2defaults.sql")
	public void test_emptyDatabaseFakeRequestData_noException() throws Exception {
		this.userName = UserTransportBuilder.ADMIN_NAME;
		this.userPassword = UserTransportBuilder.ADMIN_PASSWORD;
		final ShowYearlyReportGraphRequest request = new ShowYearlyReportGraphRequest();
		request.setStartDate(new Date(110, 3, 1));
		request.setEndDate(new Date(110, 11, 31));
		request.setPostingAccountIdsYes(Arrays.asList(PostingAccountTransportBuilder.POSTING_ACCOUNT1_ID,
				PostingAccountTransportBuilder.POSTING_ACCOUNT2_ID));
		super.callUsecaseWithContent("", this.method, request, false, ShowYearlyReportGraphResponse.class);
	}
}