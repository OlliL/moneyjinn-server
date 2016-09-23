package org.laladev.moneyjinn.server.controller.capitalsource;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.core.rest.model.ErrorResponse;
import org.laladev.moneyjinn.core.rest.model.ValidationResponse;
import org.laladev.moneyjinn.core.rest.model.capitalsource.UpdateCapitalsourceRequest;
import org.laladev.moneyjinn.core.rest.model.transport.CapitalsourceTransport;
import org.laladev.moneyjinn.core.rest.model.transport.ValidationItemTransport;
import org.laladev.moneyjinn.model.access.GroupID;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.capitalsource.Capitalsource;
import org.laladev.moneyjinn.model.capitalsource.CapitalsourceID;
import org.laladev.moneyjinn.model.capitalsource.CapitalsourceImport;
import org.laladev.moneyjinn.server.builder.CapitalsourceTransportBuilder;
import org.laladev.moneyjinn.server.builder.DateUtil;
import org.laladev.moneyjinn.server.builder.GroupTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.builder.ValidationItemTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.laladev.moneyjinn.service.api.IAccessRelationService;
import org.laladev.moneyjinn.service.api.ICapitalsourceService;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.jdbc.Sql;

public class UpdateCapitalsourceTest extends AbstractControllerTest {

	@Inject
	ICapitalsourceService capitalsourceService;

	@Inject
	IAccessRelationService accessRelationService;

	private final HttpMethod method = HttpMethod.PUT;
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

	private void testError(final CapitalsourceTransport transport, final ErrorCode errorCode) throws Exception {
		final UpdateCapitalsourceRequest request = new UpdateCapitalsourceRequest();

		request.setCapitalsourceTransport(transport);

		final List<ValidationItemTransport> validationItems = new ArrayList<>();
		validationItems.add(new ValidationItemTransportBuilder().withKey(transport.getId().intValue())
				.withError(errorCode.getErrorCode()).build());

		final ValidationResponse expected = new ValidationResponse();
		expected.setValidationItemTransports(validationItems);
		expected.setResult(Boolean.FALSE);

		final ValidationResponse actual = super.callUsecaseWithContent("", this.method, request, false,
				ValidationResponse.class);

		Assert.assertEquals(expected, actual);

	}

	@Test
	public void test_CapitalsourcenameAlreadyExisting_Error() throws Exception {

		final CapitalsourceTransport transport = new CapitalsourceTransportBuilder().forCapitalsource2().build();
		transport.setComment(CapitalsourceTransportBuilder.CAPITALSOURCE1_COMMENT);

		this.testError(transport, ErrorCode.NAME_ALREADY_EXISTS);
	}

	@Test
	public void test_EmptyCapitalsourcename_Error() throws Exception {
		final CapitalsourceTransport transport = new CapitalsourceTransportBuilder().forCapitalsource2().build();
		transport.setComment("");

		this.testError(transport, ErrorCode.COMMENT_IS_NOT_SET);
	}

	@Test
	public void test_ValidTilBeforeValidFrom_Error() throws Exception {
		final CapitalsourceTransport transport = new CapitalsourceTransportBuilder().forCapitalsource2().build();
		transport.setValidTil(DateUtil.getGMTDate("2000-01-01"));
		transport.setValidFrom(DateUtil.getGMTDate("2010-01-01"));

		this.testError(transport, ErrorCode.VALIDFROM_AFTER_VALIDTIL);
	}

	@Test
	public void test_ValidityPeriodOutOfUsage_Error() throws Exception {
		final CapitalsourceTransport transport = new CapitalsourceTransportBuilder().forCapitalsource1().build();
		transport.setValidFrom(DateUtil.getGMTDate("2010-01-01"));

		this.testError(transport, ErrorCode.CAPITALSOURCE_IN_USE_PERIOD);
	}

	@Test
	public void test_standardRequest_SuccessfullNoContent() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final GroupID groupId = new GroupID(GroupTransportBuilder.GROUP1_ID);
		final CapitalsourceID capitalsourceId = new CapitalsourceID(CapitalsourceTransportBuilder.CAPITALSOURCE1_ID);

		final UpdateCapitalsourceRequest request = new UpdateCapitalsourceRequest();

		final CapitalsourceTransport transport = new CapitalsourceTransportBuilder().forCapitalsource1().build();
		transport.setComment("hugo");
		request.setCapitalsourceTransport(transport);

		super.callUsecaseWithContent("", this.method, request, true, Object.class);

		final Capitalsource capitalsource = this.capitalsourceService.getCapitalsourceById(userId, groupId,
				capitalsourceId);

		Assert.assertEquals(CapitalsourceTransportBuilder.CAPITALSOURCE1_ID, capitalsource.getId().getId());
		Assert.assertEquals("hugo", capitalsource.getComment());
	}

	@Test
	public void test_setImportAllowedAll_SuccessfullNoContent() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final GroupID groupId = new GroupID(GroupTransportBuilder.GROUP1_ID);
		final CapitalsourceID capitalsourceId = new CapitalsourceID(CapitalsourceTransportBuilder.CAPITALSOURCE2_ID);

		final UpdateCapitalsourceRequest request = new UpdateCapitalsourceRequest();

		final CapitalsourceTransport transport = new CapitalsourceTransportBuilder().forCapitalsource2().build();
		transport.setImportAllowed((short) 1);
		request.setCapitalsourceTransport(transport);

		super.callUsecaseWithContent("", this.method, request, true, Object.class);

		final Capitalsource capitalsource = this.capitalsourceService.getCapitalsourceById(userId, groupId,
				capitalsourceId);

		Assert.assertEquals(CapitalsourceTransportBuilder.CAPITALSOURCE2_ID, capitalsource.getId().getId());
		Assert.assertEquals(CapitalsourceImport.ALL_ALLOWED, capitalsource.getImportAllowed());
	}

	@Test
	public void test_setImportAllowedOnlyBalance_SuccessfullNoContent() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final GroupID groupId = new GroupID(GroupTransportBuilder.GROUP1_ID);
		final CapitalsourceID capitalsourceId = new CapitalsourceID(CapitalsourceTransportBuilder.CAPITALSOURCE2_ID);

		final UpdateCapitalsourceRequest request = new UpdateCapitalsourceRequest();

		final CapitalsourceTransport transport = new CapitalsourceTransportBuilder().forCapitalsource2().build();
		transport.setImportAllowed((short) 2);
		request.setCapitalsourceTransport(transport);

		super.callUsecaseWithContent("", this.method, request, true, Object.class);

		final Capitalsource capitalsource = this.capitalsourceService.getCapitalsourceById(userId, groupId,
				capitalsourceId);

		Assert.assertEquals(CapitalsourceTransportBuilder.CAPITALSOURCE2_ID, capitalsource.getId().getId());
		Assert.assertEquals(CapitalsourceImport.BALANCE_ALLOWED, capitalsource.getImportAllowed());
	}

	@Test
	public void test_setImportAllowedNotAllowed_SuccessfullNoContent() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final GroupID groupId = new GroupID(GroupTransportBuilder.GROUP1_ID);
		final CapitalsourceID capitalsourceId = new CapitalsourceID(CapitalsourceTransportBuilder.CAPITALSOURCE1_ID);

		final UpdateCapitalsourceRequest request = new UpdateCapitalsourceRequest();

		final CapitalsourceTransport transport = new CapitalsourceTransportBuilder().forCapitalsource1().build();
		transport.setImportAllowed((short) 0);
		request.setCapitalsourceTransport(transport);

		super.callUsecaseWithContent("", this.method, request, true, Object.class);

		final Capitalsource capitalsource = this.capitalsourceService.getCapitalsourceById(userId, groupId,
				capitalsourceId);

		Assert.assertEquals(CapitalsourceTransportBuilder.CAPITALSOURCE1_ID, capitalsource.getId().getId());
		Assert.assertEquals(CapitalsourceImport.NOT_ALLOWED, capitalsource.getImportAllowed());
	}

	@Test
	public void test_ProvisionAsset_SuccessfullNoContent() throws Exception {
		this.userName = UserTransportBuilder.USER3_NAME;
		this.userPassword = UserTransportBuilder.USER3_PASSWORD;
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final GroupID groupId = new GroupID(GroupTransportBuilder.GROUP1_ID);
		final CapitalsourceID capitalsourceId = new CapitalsourceID(CapitalsourceTransportBuilder.CAPITALSOURCE4_ID);

		final UpdateCapitalsourceRequest request = new UpdateCapitalsourceRequest();

		final CapitalsourceTransport transport = new CapitalsourceTransportBuilder().forCapitalsource4().build();
		transport.setComment("hugo");
		request.setCapitalsourceTransport(transport);

		super.callUsecaseWithContent("", this.method, request, true, Object.class);

		final Capitalsource capitalsource = this.capitalsourceService.getCapitalsourceById(userId, groupId,
				capitalsourceId);

		Assert.assertEquals(CapitalsourceTransportBuilder.CAPITALSOURCE4_ID, capitalsource.getId().getId());
		Assert.assertEquals("hugo", capitalsource.getComment());
	}

	@Test
	public void test_LongTermAsset_SuccessfullNoContent() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final GroupID groupId = new GroupID(GroupTransportBuilder.GROUP1_ID);
		final CapitalsourceID capitalsourceId = new CapitalsourceID(CapitalsourceTransportBuilder.CAPITALSOURCE2_ID);

		final UpdateCapitalsourceRequest request = new UpdateCapitalsourceRequest();

		final CapitalsourceTransport transport = new CapitalsourceTransportBuilder().forCapitalsource2().build();
		transport.setComment("hugo");
		request.setCapitalsourceTransport(transport);

		super.callUsecaseWithContent("", this.method, request, true, Object.class);

		final Capitalsource capitalsource = this.capitalsourceService.getCapitalsourceById(userId, groupId,
				capitalsourceId);

		Assert.assertEquals(CapitalsourceTransportBuilder.CAPITALSOURCE2_ID, capitalsource.getId().getId());
		Assert.assertEquals("hugo", capitalsource.getComment());
	}

	@Test
	public void test_editCapitalsourceOwnedBySomeoneElse_notSuccessfull() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final GroupID groupId = new GroupID(GroupTransportBuilder.GROUP1_ID);
		final CapitalsourceID capitalsourceId = new CapitalsourceID(CapitalsourceTransportBuilder.CAPITALSOURCE3_ID);

		final UpdateCapitalsourceRequest request = new UpdateCapitalsourceRequest();

		final CapitalsourceTransport transport = new CapitalsourceTransportBuilder().forCapitalsource1().build();
		transport.setComment("hugo");
		request.setCapitalsourceTransport(transport);

		super.callUsecaseWithContent("", this.method, request, true, Object.class);

		final Capitalsource capitalsource = this.capitalsourceService.getCapitalsourceById(userId, groupId,
				capitalsourceId);

		Assert.assertEquals(CapitalsourceTransportBuilder.CAPITALSOURCE3_ID, capitalsource.getId().getId());
		Assert.assertEquals(CapitalsourceTransportBuilder.CAPITALSOURCE3_COMMENT, capitalsource.getComment());
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

		final UpdateCapitalsourceRequest request = new UpdateCapitalsourceRequest();
		final CapitalsourceTransport transport = new CapitalsourceTransportBuilder().forCapitalsource1().build();
		request.setCapitalsourceTransport(transport);

		super.callUsecaseWithContent("", this.method, request, true, Object.class);
	}

}