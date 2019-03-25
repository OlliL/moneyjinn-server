package org.laladev.moneyjinn.server.controller.contractpartner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.core.rest.model.ErrorResponse;
import org.laladev.moneyjinn.core.rest.model.contractpartner.CreateContractpartnerRequest;
import org.laladev.moneyjinn.core.rest.model.contractpartner.CreateContractpartnerResponse;
import org.laladev.moneyjinn.core.rest.model.transport.ContractpartnerTransport;
import org.laladev.moneyjinn.core.rest.model.transport.ValidationItemTransport;
import org.laladev.moneyjinn.model.Contractpartner;
import org.laladev.moneyjinn.model.ContractpartnerID;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.server.builder.ContractpartnerTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.builder.ValidationItemTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.laladev.moneyjinn.service.api.IContractpartnerService;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.jdbc.Sql;

public class CreateContractpartnerTest extends AbstractControllerTest {

	@Inject
	private IContractpartnerService contractpartnerService;

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

	private void testError(final ContractpartnerTransport transport, final ErrorCode errorCode) throws Exception {
		final CreateContractpartnerRequest request = new CreateContractpartnerRequest();

		request.setContractpartnerTransport(transport);

		final List<ValidationItemTransport> validationItems = new ArrayList<>();
		validationItems
				.add(new ValidationItemTransportBuilder().withKey(null).withError(errorCode.getErrorCode()).build());

		final CreateContractpartnerResponse expected = new CreateContractpartnerResponse();
		expected.setValidationItemTransports(validationItems);
		expected.setResult(Boolean.FALSE);

		final CreateContractpartnerResponse actual = super.callUsecaseWithContent("", this.method, request, false,
				CreateContractpartnerResponse.class);

		Assert.assertEquals(expected, actual);

	}

	@Test
	public void test_ContractpartnernameAlreadyExisting_Error() throws Exception {

		final ContractpartnerTransport transport = new ContractpartnerTransportBuilder().forNewContractpartner()
				.build();
		transport.setName(ContractpartnerTransportBuilder.CONTRACTPARTNER1_NAME);

		this.testError(transport, ErrorCode.NAME_ALREADY_EXISTS);
	}

	@Test
	public void test_emptyContractpartnername_Error() throws Exception {
		final ContractpartnerTransport transport = new ContractpartnerTransportBuilder().forNewContractpartner()
				.build();
		transport.setName("");

		this.testError(transport, ErrorCode.NAME_MUST_NOT_BE_EMPTY);
	}

	@Test
	public void test_nullContractpartnername_Error() throws Exception {
		final ContractpartnerTransport transport = new ContractpartnerTransportBuilder().forNewContractpartner()
				.build();
		transport.setName(null);

		this.testError(transport, ErrorCode.NAME_MUST_NOT_BE_EMPTY);
	}

	@Test
	public void test_standardRequest_SuccessfullNoContent() throws Exception {
		final CreateContractpartnerRequest request = new CreateContractpartnerRequest();

		final ContractpartnerTransport transport = new ContractpartnerTransportBuilder().forNewContractpartner()
				.build();

		request.setContractpartnerTransport(transport);

		final CreateContractpartnerResponse expected = new CreateContractpartnerResponse();
		expected.setContractpartnerId(ContractpartnerTransportBuilder.NEXT_ID);

		final CreateContractpartnerResponse actual = super.callUsecaseWithContent("", this.method, request, false,
				CreateContractpartnerResponse.class);

		Assert.assertEquals(expected, actual);

		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final ContractpartnerID contractpartnerId = new ContractpartnerID(ContractpartnerTransportBuilder.NEXT_ID);
		final Contractpartner contractpartner = this.contractpartnerService.getContractpartnerById(userId,
				contractpartnerId);

		Assert.assertEquals(ContractpartnerTransportBuilder.NEXT_ID, contractpartner.getId().getId());
		Assert.assertEquals(ContractpartnerTransportBuilder.NEWCONTRACTPARTNER_NAME, contractpartner.getName());
	}

	@Test
	public void test_differentUserIdSet_ButIgnoredAndAlwaysCreatedWithOwnUserId() throws Exception {
		final CreateContractpartnerRequest request = new CreateContractpartnerRequest();

		final ContractpartnerTransport transport = new ContractpartnerTransportBuilder().forNewContractpartner()
				.build();
		transport.setUserid(UserTransportBuilder.ADMIN_ID);
		request.setContractpartnerTransport(transport);

		final CreateContractpartnerResponse expected = new CreateContractpartnerResponse();
		expected.setContractpartnerId(ContractpartnerTransportBuilder.NEXT_ID);

		final CreateContractpartnerResponse actual = super.callUsecaseWithContent("", this.method, request, false,
				CreateContractpartnerResponse.class);

		Assert.assertEquals(expected, actual);

		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final ContractpartnerID contractpartnerId = new ContractpartnerID(ContractpartnerTransportBuilder.NEXT_ID);
		final Contractpartner contractpartner = this.contractpartnerService.getContractpartnerById(userId,
				contractpartnerId);

		Assert.assertEquals(ContractpartnerTransportBuilder.NEXT_ID, contractpartner.getId().getId());
		Assert.assertEquals(ContractpartnerTransportBuilder.NEWCONTRACTPARTNER_NAME, contractpartner.getName());
	}

	@Test
	public void test_checkDefaults_SuccessfullNoContent() throws Exception {
		final CreateContractpartnerRequest request = new CreateContractpartnerRequest();

		final ContractpartnerTransport transport = new ContractpartnerTransportBuilder().forNewContractpartner()
				.build();
		transport.setValidFrom(null);
		transport.setValidTil(null);
		request.setContractpartnerTransport(transport);

		final CreateContractpartnerResponse expected = new CreateContractpartnerResponse();
		expected.setContractpartnerId(ContractpartnerTransportBuilder.NEXT_ID);

		final CreateContractpartnerResponse actual = super.callUsecaseWithContent("", this.method, request, false,
				CreateContractpartnerResponse.class);

		Assert.assertEquals(expected, actual);

		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final ContractpartnerID contractpartnerId = new ContractpartnerID(ContractpartnerTransportBuilder.NEXT_ID);
		final Contractpartner contractpartner = this.contractpartnerService.getContractpartnerById(userId,
				contractpartnerId);

		Assert.assertEquals(ContractpartnerTransportBuilder.NEXT_ID, contractpartner.getId().getId());
		Assert.assertEquals(ContractpartnerTransportBuilder.NEWCONTRACTPARTNER_NAME, contractpartner.getName());
		Assert.assertEquals(LocalDate.now(), contractpartner.getValidFrom());
		Assert.assertEquals(LocalDate.parse("2999-12-31"), contractpartner.getValidTil());
	}

	@Test
	public void test_optionalFields_SuccessfullNoContent() throws Exception {
		final CreateContractpartnerRequest request = new CreateContractpartnerRequest();

		final ContractpartnerTransport transport = new ContractpartnerTransportBuilder().forNewContractpartner()
				.build();
		transport.setStreet(null);
		transport.setPostcode(null);
		transport.setTown(null);
		transport.setCountry(null);
		transport.setMoneyflowComment(null);
		transport.setPostingAccountId(null);
		request.setContractpartnerTransport(transport);

		final CreateContractpartnerResponse expected = new CreateContractpartnerResponse();
		expected.setContractpartnerId(ContractpartnerTransportBuilder.NEXT_ID);

		final CreateContractpartnerResponse actual = super.callUsecaseWithContent("", this.method, request, false,
				CreateContractpartnerResponse.class);

		Assert.assertEquals(expected, actual);

		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final ContractpartnerID contractpartnerId = new ContractpartnerID(ContractpartnerTransportBuilder.NEXT_ID);
		final Contractpartner contractpartner = this.contractpartnerService.getContractpartnerById(userId,
				contractpartnerId);

		Assert.assertEquals(ContractpartnerTransportBuilder.NEXT_ID, contractpartner.getId().getId());
		Assert.assertEquals(ContractpartnerTransportBuilder.NEWCONTRACTPARTNER_NAME, contractpartner.getName());
		Assert.assertNull(contractpartner.getStreet());
		Assert.assertNull(contractpartner.getPostcode());
		Assert.assertNull(contractpartner.getTown());
		Assert.assertNull(contractpartner.getCountry());
		Assert.assertNull(contractpartner.getMoneyflowComment());
		Assert.assertNull(contractpartner.getPostingAccount());
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

		final CreateContractpartnerRequest request = new CreateContractpartnerRequest();

		final ContractpartnerTransport transport = new ContractpartnerTransportBuilder().forNewContractpartner()
				.build();
		transport.setStreet(null);
		transport.setPostcode(null);
		transport.setTown(null);
		transport.setCountry(null);
		transport.setMoneyflowComment(null);
		transport.setPostingAccountId(null);
		request.setContractpartnerTransport(transport);

		final CreateContractpartnerResponse expected = new CreateContractpartnerResponse();
		expected.setContractpartnerId(1l);

		final CreateContractpartnerResponse actual = super.callUsecaseWithContent("", this.method, request, false,
				CreateContractpartnerResponse.class);

		Assert.assertEquals(expected, actual);

		final UserID userId = new UserID(UserTransportBuilder.ADMIN_ID);
		final ContractpartnerID contractpartnerId = new ContractpartnerID(1l);
		final Contractpartner contractpartner = this.contractpartnerService.getContractpartnerById(userId,
				contractpartnerId);

		Assert.assertEquals(contractpartnerId.getId(), contractpartner.getId().getId());
		Assert.assertEquals(ContractpartnerTransportBuilder.NEWCONTRACTPARTNER_NAME, contractpartner.getName());
	}

}