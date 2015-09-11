package org.laladev.moneyjinn.server.controller.capitalsource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.laladev.moneyjinn.core.rest.model.capitalsource.ShowDeleteCapitalsourceResponse;
import org.laladev.moneyjinn.server.builder.CapitalsourceTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.springframework.http.HttpMethod;

public class ShowDeleteCapitalsourceTest extends AbstractControllerTest {

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
		return super.getUsecaseFromTestClassName("capitalsource", this.getClass());
	}

	@Test
	public void test_unknownCapitalsource_emptyResponseObject() throws Exception {
		final ShowDeleteCapitalsourceResponse expected = new ShowDeleteCapitalsourceResponse();
		final ShowDeleteCapitalsourceResponse actual = super.callUsecaseWithoutContent(
				"/" + CapitalsourceTransportBuilder.NON_EXISTING_ID, this.method, false,
				ShowDeleteCapitalsourceResponse.class);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void test_CapitalsourceOwnedBySomeoneElse_emptyResponseObject() throws Exception {
		this.userName = UserTransportBuilder.USER3_NAME;
		this.userPassword = UserTransportBuilder.USER3_PASSWORD;
		final ShowDeleteCapitalsourceResponse expected = new ShowDeleteCapitalsourceResponse();
		final ShowDeleteCapitalsourceResponse actual = super.callUsecaseWithoutContent(
				"/" + CapitalsourceTransportBuilder.CAPITALSOURCE1_ID, this.method, false,
				ShowDeleteCapitalsourceResponse.class);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void test_Capitalsource1_completeResponseObject() throws Exception {
		final ShowDeleteCapitalsourceResponse expected = new ShowDeleteCapitalsourceResponse();
		expected.setCapitalsourceTransport(new CapitalsourceTransportBuilder().forCapitalsource1().build());

		final ShowDeleteCapitalsourceResponse actual = super.callUsecaseWithoutContent(
				"/" + CapitalsourceTransportBuilder.CAPITALSOURCE1_ID, this.method, false,
				ShowDeleteCapitalsourceResponse.class);

		Assert.assertEquals(expected, actual);
	}

}