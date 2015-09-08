package org.laladev.moneyjinn.server.controller.contractpartner;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.laladev.moneyjinn.businesslogic.model.Contractpartner;
import org.laladev.moneyjinn.businesslogic.model.ContractpartnerID;
import org.laladev.moneyjinn.businesslogic.model.ErrorCode;
import org.laladev.moneyjinn.businesslogic.model.access.UserID;
import org.laladev.moneyjinn.businesslogic.service.api.IContractpartnerService;
import org.laladev.moneyjinn.core.rest.model.ErrorResponse;
import org.laladev.moneyjinn.server.builder.ContractpartnerTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.springframework.http.HttpMethod;

public class DeleteContractpartnerTest extends AbstractControllerTest {

	@Inject
	IContractpartnerService contractpartnerService;

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
		return super.getUsecaseFromTestClassName("contractpartner", this.getClass());
	}

	@Test
	public void test_regularContractpartnerNoData_SuccessfullNoContent() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final ContractpartnerID contractpartnerId = new ContractpartnerID(
				ContractpartnerTransportBuilder.CONTRACTPARTNER2_ID);

		Contractpartner contractpartner = this.contractpartnerService.getContractpartnerById(userId, contractpartnerId);

		Assert.assertNotNull(contractpartner);

		super.callUsecaseWithoutContent("/" + ContractpartnerTransportBuilder.CONTRACTPARTNER2_ID, this.method, true,
				Object.class);

		contractpartner = this.contractpartnerService.getContractpartnerById(userId, contractpartnerId);

		Assert.assertNull(contractpartner);
	}

	@Test
	public void test_nonExistingContractpartner_SuccessfullNoContent() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final ContractpartnerID contractpartnerId = new ContractpartnerID(
				ContractpartnerTransportBuilder.NON_EXISTING_ID);

		Contractpartner contractpartner = this.contractpartnerService.getContractpartnerById(userId, contractpartnerId);

		Assert.assertNull(contractpartner);

		super.callUsecaseWithoutContent("/" + ContractpartnerTransportBuilder.NON_EXISTING_ID, this.method, true,
				Object.class);

		contractpartner = this.contractpartnerService.getContractpartnerById(userId, contractpartnerId);

		Assert.assertNull(contractpartner);
	}

	@Test
	public void test_regularContractpartnerWithData_ErrorResponse() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final ContractpartnerID contractpartnerId = new ContractpartnerID(
				ContractpartnerTransportBuilder.CONTRACTPARTNER1_ID);

		final ErrorResponse expected = new ErrorResponse();
		expected.setCode(ErrorCode.CONTRACTPARTNER_IN_USE.getErrorCode());
		expected.setMessage("You may not delete a contractual partner who is still referenced by a flow of money!");

		Contractpartner contractpartner = this.contractpartnerService.getContractpartnerById(userId, contractpartnerId);

		Assert.assertNotNull(contractpartner);

		final ErrorResponse response = super.callUsecaseWithoutContent(
				"/" + ContractpartnerTransportBuilder.CONTRACTPARTNER1_ID, this.method, false, ErrorResponse.class);

		contractpartner = this.contractpartnerService.getContractpartnerById(userId, contractpartnerId);

		Assert.assertNotNull(contractpartner);
		Assert.assertEquals(expected, response);
	}

	@Test
	public void test_ContractpartnerFromSameGroupButNotMe_SuccessfullNoContent() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final ContractpartnerID contractpartnerId = new ContractpartnerID(
				ContractpartnerTransportBuilder.CONTRACTPARTNER3_ID);

		Contractpartner contractpartner = this.contractpartnerService.getContractpartnerById(userId, contractpartnerId);

		Assert.assertNotNull(contractpartner);

		super.callUsecaseWithoutContent("/" + ContractpartnerTransportBuilder.CONTRACTPARTNER3_ID, this.method, true,
				Object.class);

		contractpartner = this.contractpartnerService.getContractpartnerById(userId, contractpartnerId);

		Assert.assertNull(contractpartner);
	}

	@Test
	public void test_ContractpartnerFromDifferentGroup_notSuccessfull() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.ADMIN_ID);
		final ContractpartnerID contractpartnerId = new ContractpartnerID(
				ContractpartnerTransportBuilder.CONTRACTPARTNER5_ID);

		Contractpartner contractpartner = this.contractpartnerService.getContractpartnerById(userId, contractpartnerId);

		Assert.assertNotNull(contractpartner);

		super.callUsecaseWithoutContent("/" + ContractpartnerTransportBuilder.CONTRACTPARTNER5_ID, this.method, true,
				Object.class);

		contractpartner = this.contractpartnerService.getContractpartnerById(userId, contractpartnerId);

		Assert.assertNotNull(contractpartner);
	}

}