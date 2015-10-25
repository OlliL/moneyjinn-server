package org.laladev.moneyjinn.server.controller.report;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.laladev.moneyjinn.core.rest.model.ErrorResponse;
import org.laladev.moneyjinn.core.rest.model.report.ListReportsResponse;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.jdbc.Sql;

public class ListReportsTest extends AbstractControllerTest {

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

	@Test
	public void test_noArguments_defaultsResponse() throws Exception {
		final ListReportsResponse expected = new ListReportsResponse();
		expected.setYear((short) 2009);
		expected.setAllYears(Arrays.asList((short) 2009));
		expected.setAllMonth(Arrays.asList((short) 1));

		final ListReportsResponse actual = super.callUsecaseWithoutContent("", this.method, false,
				ListReportsResponse.class);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void test_onlyYear_defaultsResponse() throws Exception {
		final ListReportsResponse expected = new ListReportsResponse();
		expected.setYear((short) 2009);
		expected.setAllYears(Arrays.asList((short) 2009));
		expected.setAllMonth(Arrays.asList((short) 1));

		final ListReportsResponse actual = super.callUsecaseWithoutContent("/2009", this.method, false,
				ListReportsResponse.class);

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
		super.callUsecaseWithoutContent("", this.method, false, ListReportsResponse.class);
	}
}