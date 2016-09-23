package org.laladev.moneyjinn.server.controller.capitalsource;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.laladev.moneyjinn.businesslogic.model.access.GroupID;
import org.laladev.moneyjinn.businesslogic.model.access.UserID;
import org.laladev.moneyjinn.businesslogic.model.capitalsource.Capitalsource;
import org.laladev.moneyjinn.businesslogic.model.capitalsource.CapitalsourceID;
import org.laladev.moneyjinn.businesslogic.service.api.ICapitalsourceService;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.core.rest.model.ErrorResponse;
import org.laladev.moneyjinn.server.builder.CapitalsourceTransportBuilder;
import org.laladev.moneyjinn.server.builder.GroupTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.jdbc.Sql;

public class DeleteCapitalsourceByIdTest extends AbstractControllerTest {

	@Inject
	ICapitalsourceService capitalsourceService;

	private final HttpMethod method = HttpMethod.DELETE;
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
	public void test_regularCapitalsourceNoData_SuccessfullNoContent() throws Exception {
		this.userName = UserTransportBuilder.USER3_NAME;
		this.userPassword = UserTransportBuilder.USER3_PASSWORD;
		final UserID userId = new UserID(UserTransportBuilder.USER3_ID);
		final GroupID groupId = new GroupID(GroupTransportBuilder.GROUP1_ID);
		final CapitalsourceID capitalsourceId = new CapitalsourceID(CapitalsourceTransportBuilder.CAPITALSOURCE3_ID);

		Capitalsource capitalsource = this.capitalsourceService.getCapitalsourceById(userId, groupId, capitalsourceId);

		Assert.assertNotNull(capitalsource);

		super.callUsecaseWithoutContent("/" + CapitalsourceTransportBuilder.CAPITALSOURCE3_ID, this.method, true,
				Object.class);

		capitalsource = this.capitalsourceService.getCapitalsourceById(userId, groupId, capitalsourceId);

		Assert.assertNull(capitalsource);
	}

	@Test
	public void test_nonExistingCapitalsource_SuccessfullNoContent() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final GroupID groupId = new GroupID(GroupTransportBuilder.GROUP1_ID);
		final CapitalsourceID capitalsourceId = new CapitalsourceID(CapitalsourceTransportBuilder.NON_EXISTING_ID);

		Capitalsource capitalsource = this.capitalsourceService.getCapitalsourceById(userId, groupId, capitalsourceId);

		Assert.assertNull(capitalsource);

		super.callUsecaseWithoutContent("/" + CapitalsourceTransportBuilder.NON_EXISTING_ID, this.method, true,
				Object.class);

		capitalsource = this.capitalsourceService.getCapitalsourceById(userId, groupId, capitalsourceId);

		Assert.assertNull(capitalsource);
	}

	@Test
	public void test_regularCapitalsourceWithData_ErrorResponse() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final GroupID groupId = new GroupID(GroupTransportBuilder.GROUP1_ID);
		final CapitalsourceID capitalsourceId = new CapitalsourceID(CapitalsourceTransportBuilder.CAPITALSOURCE1_ID);

		final ErrorResponse expected = new ErrorResponse();
		expected.setCode(ErrorCode.CAPITALSOURCE_STILL_REFERENCED.getErrorCode());
		expected.setMessage("You may not delete a source of capital while it is referenced by a flow of money!");

		Capitalsource capitalsource = this.capitalsourceService.getCapitalsourceById(userId, groupId, capitalsourceId);

		Assert.assertNotNull(capitalsource);

		final ErrorResponse response = super.callUsecaseWithoutContent(
				"/" + CapitalsourceTransportBuilder.CAPITALSOURCE1_ID, this.method, false, ErrorResponse.class);

		capitalsource = this.capitalsourceService.getCapitalsourceById(userId, groupId, capitalsourceId);

		Assert.assertNotNull(capitalsource);
		Assert.assertEquals(expected, response);
	}

	@Test
	public void test_deleteCapitalsourceOwnedBySomeoneElse_notSuccessfull() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final GroupID groupId = new GroupID(GroupTransportBuilder.GROUP1_ID);
		final CapitalsourceID capitalsourceId = new CapitalsourceID(CapitalsourceTransportBuilder.CAPITALSOURCE3_ID);

		Capitalsource capitalsource = this.capitalsourceService.getCapitalsourceById(userId, groupId, capitalsourceId);

		Assert.assertNotNull(capitalsource);

		super.callUsecaseWithoutContent("/" + CapitalsourceTransportBuilder.CAPITALSOURCE3_ID, this.method, true,
				Object.class);

		capitalsource = this.capitalsourceService.getCapitalsourceById(userId, groupId, capitalsourceId);

		Assert.assertNotNull(capitalsource);
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
		super.callUsecaseWithoutContent("/" + CapitalsourceTransportBuilder.NON_EXISTING_ID, this.method, true,
				Object.class);
	}

}