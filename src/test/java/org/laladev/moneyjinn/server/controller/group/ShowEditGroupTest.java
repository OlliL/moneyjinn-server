package org.laladev.moneyjinn.server.controller.group;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.laladev.moneyjinn.businesslogic.model.ErrorCode;
import org.laladev.moneyjinn.core.rest.model.ErrorResponse;
import org.laladev.moneyjinn.core.rest.model.group.ShowEditGroupResponse;
import org.laladev.moneyjinn.server.builder.GroupTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.springframework.http.HttpMethod;

public class ShowEditGroupTest extends AbstractControllerTest {

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
		return super.getUsecaseFromTestClassName(this.getClass());
	}

	@Test
	public void test_unknownGroup_emptyResponseObject() throws Exception {
		final ShowEditGroupResponse expected = new ShowEditGroupResponse();
		final ShowEditGroupResponse actual = super.callUsecaseWithoutContent(
				"/" + GroupTransportBuilder.NON_EXISTING_ID, this.method, false, ShowEditGroupResponse.class);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void test_Group1_completeResponseObject() throws Exception {
		final ShowEditGroupResponse expected = new ShowEditGroupResponse();
		expected.setGroupTransport(new GroupTransportBuilder().forGroup1().build());

		final ShowEditGroupResponse actual = super.callUsecaseWithoutContent("/" + GroupTransportBuilder.GROUP1_ID,
				this.method, false, ShowEditGroupResponse.class);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void test_OnlyAdminAllowed_ErrorResponse() throws Exception {
		this.userName = UserTransportBuilder.USER1_NAME;
		this.userPassword = UserTransportBuilder.USER1_PASSWORD;

		final ErrorResponse actual = super.callUsecaseWithoutContent("/" + GroupTransportBuilder.GROUP2_ID, this.method,
				false, ErrorResponse.class);

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