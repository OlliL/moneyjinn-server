package org.laladev.moneyjinn.server.controller.capitalsource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.laladev.moneyjinn.core.rest.model.ErrorResponse;
import org.laladev.moneyjinn.core.rest.model.capitalsource.ShowEditCapitalsourceResponse;
import org.laladev.moneyjinn.server.builder.CapitalsourceTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.jdbc.Sql;

public class ShowEditCapitalsourceTest extends AbstractControllerTest {

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
	public void test_unknownCapitalsource_emptyResponseObject() throws Exception {
		final ShowEditCapitalsourceResponse expected = new ShowEditCapitalsourceResponse();
		final ShowEditCapitalsourceResponse actual = super.callUsecaseWithoutContent(
				"/" + CapitalsourceTransportBuilder.NON_EXISTING_ID, this.method, false,
				ShowEditCapitalsourceResponse.class);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void test_Capitalsource1_completeResponseObject() throws Exception {
		final ShowEditCapitalsourceResponse expected = new ShowEditCapitalsourceResponse();
		expected.setCapitalsourceTransport(new CapitalsourceTransportBuilder().forCapitalsource1().build());

		final ShowEditCapitalsourceResponse actual = super.callUsecaseWithoutContent(
				"/" + CapitalsourceTransportBuilder.CAPITALSOURCE1_ID, this.method, false,
				ShowEditCapitalsourceResponse.class);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void test_AuthorizationRequired_Error() throws Exception {
		this.userName = null;
		this.userPassword = null;
		final ErrorResponse actual = super.callUsecaseWithoutContent("/1", this.method, false, ErrorResponse.class);
		Assert.assertEquals(super.accessDeniedErrorResponse(), actual);
	}

	@Test
	@Sql("classpath:h2defaults.sql")
	public void test_emptyDatabase_noException() throws Exception {
		this.userName = UserTransportBuilder.ADMIN_NAME;
		this.userPassword = UserTransportBuilder.ADMIN_PASSWORD;
		final ShowEditCapitalsourceResponse expected = new ShowEditCapitalsourceResponse();
		final ShowEditCapitalsourceResponse actual = super.callUsecaseWithoutContent(
				"/" + CapitalsourceTransportBuilder.NON_EXISTING_ID, this.method, false,
				ShowEditCapitalsourceResponse.class);

		Assert.assertEquals(expected, actual);
	}

}