package org.laladev.moneyjinn.server.controller.report;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.laladev.moneyjinn.businesslogic.model.PostingAccountID;
import org.laladev.moneyjinn.businesslogic.model.access.UserID;
import org.laladev.moneyjinn.businesslogic.model.setting.ClientReportingUnselectedPostingAccountIdsSetting;
import org.laladev.moneyjinn.businesslogic.service.api.IMonthlySettlementService;
import org.laladev.moneyjinn.businesslogic.service.api.ISettingService;
import org.laladev.moneyjinn.core.rest.model.ErrorResponse;
import org.laladev.moneyjinn.core.rest.model.report.ShowReportingFormResponse;
import org.laladev.moneyjinn.core.rest.model.transport.PostingAccountTransport;
import org.laladev.moneyjinn.server.builder.PostingAccountTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.jdbc.Sql;

public class ShowReportingFormTest extends AbstractControllerTest {

	@Inject
	ISettingService settingService;
	@Inject
	IMonthlySettlementService monthlySettlementService;

	private final HttpMethod method = HttpMethod.GET;
	private String userName;
	private String userPassword;

	@Before
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

	private ShowReportingFormResponse getDefaultResponse() {
		final ShowReportingFormResponse expected = new ShowReportingFormResponse();

		final List<PostingAccountTransport> postingAccountTransports = new ArrayList<>();
		postingAccountTransports.add(new PostingAccountTransportBuilder().forPostingAccount1().build());
		postingAccountTransports.add(new PostingAccountTransportBuilder().forPostingAccount2().build());
		postingAccountTransports.add(new PostingAccountTransportBuilder().forPostingAccount3().build());
		expected.setPostingAccountTransports(postingAccountTransports);

		expected.setAllYears(Arrays.asList((short) 2008, (short) 2009, (short) 2010));
		return expected;
	}

	@Test
	public void test_noSetting_defaultsResponse() throws Exception {
		final ShowReportingFormResponse expected = this.getDefaultResponse();

		final ShowReportingFormResponse actual = super.callUsecaseWithoutContent("", this.method, false,
				ShowReportingFormResponse.class);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void test_witDefaultSelection_defaultsResponse() throws Exception {
		final ClientReportingUnselectedPostingAccountIdsSetting setting = new ClientReportingUnselectedPostingAccountIdsSetting(
				Arrays.asList(new PostingAccountID(PostingAccountTransportBuilder.POSTING_ACCOUNT1_ID),
						new PostingAccountID(PostingAccountTransportBuilder.POSTING_ACCOUNT2_ID)));
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		this.settingService.setClientReportingUnselectedPostingAccountIdsSetting(userId, setting);

		final ShowReportingFormResponse expected = this.getDefaultResponse();
		expected.setPostingAccountIds(Arrays.asList(PostingAccountTransportBuilder.POSTING_ACCOUNT1_ID,
				PostingAccountTransportBuilder.POSTING_ACCOUNT2_ID));

		final ShowReportingFormResponse actual = super.callUsecaseWithoutContent("", this.method, false,
				ShowReportingFormResponse.class);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void test_AuthorizationRequired_Error() throws Exception {
		this.userName = null;
		this.userPassword = null;
		final ErrorResponse actual = super.callUsecaseWithoutContent("", this.method, false, ErrorResponse.class);
		Assert.assertEquals(super.accessDeniedErrorResponse(), actual);
	}

	@Test
	@Sql("classpath:h2defaults.sql")
	public void test_emptyDatabase_noException() throws Exception {
		this.userName = UserTransportBuilder.ADMIN_NAME;
		this.userPassword = UserTransportBuilder.ADMIN_PASSWORD;
		super.callUsecaseWithoutContent("", this.method, false, ShowReportingFormResponse.class);
	}
}