package org.laladev.moneyjinn.server.controller.capitalsource;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.laladev.moneyjinn.businesslogic.model.ErrorCode;
import org.laladev.moneyjinn.businesslogic.model.access.GroupID;
import org.laladev.moneyjinn.businesslogic.model.access.UserID;
import org.laladev.moneyjinn.businesslogic.model.capitalsource.Capitalsource;
import org.laladev.moneyjinn.businesslogic.model.capitalsource.CapitalsourceID;
import org.laladev.moneyjinn.businesslogic.model.capitalsource.CapitalsourceState;
import org.laladev.moneyjinn.businesslogic.model.capitalsource.CapitalsourceType;
import org.laladev.moneyjinn.businesslogic.service.api.IAccessRelationService;
import org.laladev.moneyjinn.businesslogic.service.api.ICapitalsourceService;
import org.laladev.moneyjinn.core.rest.model.ErrorResponse;
import org.laladev.moneyjinn.core.rest.model.ValidationResponse;
import org.laladev.moneyjinn.core.rest.model.capitalsource.CreateCapitalsourceRequest;
import org.laladev.moneyjinn.core.rest.model.transport.CapitalsourceTransport;
import org.laladev.moneyjinn.core.rest.model.transport.ValidationItemTransport;
import org.laladev.moneyjinn.server.builder.CapitalsourceTransportBuilder;
import org.laladev.moneyjinn.server.builder.GroupTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.builder.ValidationItemTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.springframework.http.HttpMethod;

public class CreateCapitalsourceTest extends AbstractControllerTest {

	@Inject
	ICapitalsourceService capitalsourceService;

	@Inject
	IAccessRelationService accessRelationService;

	private final HttpMethod method = HttpMethod.POST;
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
		final CreateCapitalsourceRequest request = new CreateCapitalsourceRequest();

		request.setCapitalsourceTransport(transport);

		final List<ValidationItemTransport> validationItems = new ArrayList<>();
		validationItems
				.add(new ValidationItemTransportBuilder().withKey(null).withError(errorCode.getErrorCode()).build());

		final ValidationResponse expected = new ValidationResponse();
		expected.setValidationItemTransports(validationItems);
		expected.setResult(Boolean.FALSE);

		final ValidationResponse actual = super.callUsecaseWithContent("", this.method, request, false,
				ValidationResponse.class);

		Assert.assertEquals(expected, actual);

	}

	@Test
	public void test_CapitalsourcenameAlreadyExisting_Error() throws Exception {

		final CapitalsourceTransport transport = new CapitalsourceTransportBuilder().forNewCapitalsource().build();
		transport.setComment(CapitalsourceTransportBuilder.CAPITALSOURCE1_COMMENT);

		this.testError(transport, ErrorCode.NAME_ALREADY_EXISTS);
	}

	@Test
	public void test_emptyCapitalsourcename_Error() throws Exception {
		final CapitalsourceTransport transport = new CapitalsourceTransportBuilder().forNewCapitalsource().build();
		transport.setComment("");

		this.testError(transport, ErrorCode.COMMENT_IS_NOT_SET);
	}

	@Test
	public void test_nullCapitalsourcename_Error() throws Exception {
		final CapitalsourceTransport transport = new CapitalsourceTransportBuilder().forNewCapitalsource().build();
		transport.setComment(null);

		this.testError(transport, ErrorCode.COMMENT_IS_NOT_SET);
	}

	@Test
	public void test_ToLongAccountnumber_Error() throws Exception {
		final CapitalsourceTransport transport = new CapitalsourceTransportBuilder().forNewCapitalsource().build();
		transport.setAccountNumber("12345678901234567890123456789012345");

		this.testError(transport, ErrorCode.ACCOUNT_NUMBER_TO_LONG);
	}

	@Test
	public void test_ToLongBankcode_Error() throws Exception {
		final CapitalsourceTransport transport = new CapitalsourceTransportBuilder().forNewCapitalsource().build();
		transport.setBankCode("123456789012");

		this.testError(transport, ErrorCode.BANK_CODE_TO_LONG);
	}

	@Test
	public void test_AccountnumberInvalidChar_Error() throws Exception {
		final CapitalsourceTransport transport = new CapitalsourceTransportBuilder().forNewCapitalsource().build();
		transport.setAccountNumber("+");

		this.testError(transport, ErrorCode.ACCOUNT_NUMBER_CONTAINS_ILLEGAL_CHARS);
	}

	@Test
	public void test_BankcodeInvalidChar_Error() throws Exception {
		final CapitalsourceTransport transport = new CapitalsourceTransportBuilder().forNewCapitalsource().build();
		transport.setBankCode("+");

		this.testError(transport, ErrorCode.BANK_CODE_CONTAINS_ILLEGAL_CHARS);
	}

	@Test
	public void test_standardRequest_SuccessfullNoContent() throws Exception {
		final CreateCapitalsourceRequest request = new CreateCapitalsourceRequest();

		final CapitalsourceTransport transport = new CapitalsourceTransportBuilder().forNewCapitalsource().build();

		request.setCapitalsourceTransport(transport);

		super.callUsecaseWithContent("", this.method, request, true, Object.class);

		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final GroupID groupId = new GroupID(GroupTransportBuilder.GROUP1_ID);
		final CapitalsourceID capitalsourceId = new CapitalsourceID(CapitalsourceTransportBuilder.NEXT_ID);
		final Capitalsource capitalsource = this.capitalsourceService.getCapitalsourceById(userId, groupId,
				capitalsourceId);

		Assert.assertEquals(CapitalsourceTransportBuilder.NEXT_ID, capitalsource.getId().getId());
		Assert.assertEquals(CapitalsourceTransportBuilder.NEWCAPITALSOURCE_COMMENT, capitalsource.getComment());
	}

	@Test
	public void test_differentUserIdSet_ButIgnoredAndAlwaysCreatedWithOwnUserId() throws Exception {
		final CreateCapitalsourceRequest request = new CreateCapitalsourceRequest();

		final CapitalsourceTransport transport = new CapitalsourceTransportBuilder().forNewCapitalsource().build();
		transport.setUserid(UserTransportBuilder.ADMIN_ID);

		request.setCapitalsourceTransport(transport);

		super.callUsecaseWithContent("", this.method, request, true, Object.class);

		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final GroupID groupId = new GroupID(GroupTransportBuilder.GROUP1_ID);
		final CapitalsourceID capitalsourceId = new CapitalsourceID(CapitalsourceTransportBuilder.NEXT_ID);
		final Capitalsource capitalsource = this.capitalsourceService.getCapitalsourceById(userId, groupId,
				capitalsourceId);

		Assert.assertEquals(CapitalsourceTransportBuilder.NEXT_ID, capitalsource.getId().getId());
		Assert.assertEquals(CapitalsourceTransportBuilder.NEWCAPITALSOURCE_COMMENT, capitalsource.getComment());
	}

	@Test
	public void test_checkDefaults_SuccessfullNoContent() throws Exception {
		final CreateCapitalsourceRequest request = new CreateCapitalsourceRequest();

		final CapitalsourceTransport transport = new CapitalsourceTransportBuilder().forNewCapitalsource().build();
		transport.setValidFrom(null);
		transport.setValidTil(null);
		transport.setState(null);
		transport.setType(null);
		transport.setAccountNumber(null);
		transport.setBankCode(null);
		request.setCapitalsourceTransport(transport);

		super.callUsecaseWithContent("", this.method, request, true, Object.class);

		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final GroupID groupId = new GroupID(GroupTransportBuilder.GROUP1_ID);
		final CapitalsourceID capitalsourceId = new CapitalsourceID(CapitalsourceTransportBuilder.NEXT_ID);
		final Capitalsource capitalsource = this.capitalsourceService.getCapitalsourceById(userId, groupId,
				capitalsourceId);

		Assert.assertEquals(CapitalsourceTransportBuilder.NEXT_ID, capitalsource.getId().getId());
		Assert.assertEquals(CapitalsourceTransportBuilder.NEWCAPITALSOURCE_COMMENT, capitalsource.getComment());
		Assert.assertEquals(CapitalsourceState.CACHE, capitalsource.getState());
		Assert.assertEquals(CapitalsourceType.CURRENT_ASSET, capitalsource.getType());
		Assert.assertEquals(LocalDate.now(), capitalsource.getValidFrom());
		Assert.assertEquals(LocalDate.parse("2999-12-31"), capitalsource.getValidTil());
		Assert.assertNull(capitalsource.getBankAccount());
	}

	@Test
	public void test_AuthorizationRequired_Error() throws Exception {
		this.userName = null;
		this.userPassword = null;
		final ErrorResponse actual = super.callUsecaseWithoutContent("", this.method, false, ErrorResponse.class);
		Assert.assertEquals(super.accessDeniedErrorResponse(), actual);
	}

}