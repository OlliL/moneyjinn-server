package org.laladev.moneyjinn.server.controller.predefmoneyflow;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.laladev.moneyjinn.businesslogic.model.ErrorCode;
import org.laladev.moneyjinn.businesslogic.model.PreDefMoneyflow;
import org.laladev.moneyjinn.businesslogic.model.PreDefMoneyflowID;
import org.laladev.moneyjinn.businesslogic.model.access.UserID;
import org.laladev.moneyjinn.businesslogic.service.api.IAccessRelationService;
import org.laladev.moneyjinn.businesslogic.service.api.IPreDefMoneyflowService;
import org.laladev.moneyjinn.core.rest.model.ErrorResponse;
import org.laladev.moneyjinn.core.rest.model.predefmoneyflow.UpdatePreDefMoneyflowRequest;
import org.laladev.moneyjinn.core.rest.model.predefmoneyflow.UpdatePreDefMoneyflowResponse;
import org.laladev.moneyjinn.core.rest.model.transport.CapitalsourceTransport;
import org.laladev.moneyjinn.core.rest.model.transport.ContractpartnerTransport;
import org.laladev.moneyjinn.core.rest.model.transport.PostingAccountTransport;
import org.laladev.moneyjinn.core.rest.model.transport.PreDefMoneyflowTransport;
import org.laladev.moneyjinn.core.rest.model.transport.ValidationItemTransport;
import org.laladev.moneyjinn.server.builder.CapitalsourceTransportBuilder;
import org.laladev.moneyjinn.server.builder.ContractpartnerTransportBuilder;
import org.laladev.moneyjinn.server.builder.PostingAccountTransportBuilder;
import org.laladev.moneyjinn.server.builder.PreDefMoneyflowTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.builder.ValidationItemTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.springframework.http.HttpMethod;

public class UpdatePreDefMoneyflowTest extends AbstractControllerTest {

	@Inject
	IPreDefMoneyflowService preDefMoneyflowService;

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
		return super.getUsecaseFromTestClassName("predefmoneyflow", this.getClass());
	}

	private void testError(final PreDefMoneyflowTransport transport, final ErrorCode errorCode,
			final CapitalsourceTransport extraCapitalsource, final ContractpartnerTransport extraContractpartner)
					throws Exception {
		final UpdatePreDefMoneyflowRequest request = new UpdatePreDefMoneyflowRequest();

		request.setPreDefMoneyflowTransport(transport);

		final List<ValidationItemTransport> validationItems = new ArrayList<>();
		validationItems.add(new ValidationItemTransportBuilder().withKey(transport.getId().intValue())
				.withError(errorCode.getErrorCode()).build());

		final UpdatePreDefMoneyflowResponse expected = new UpdatePreDefMoneyflowResponse();
		final List<PostingAccountTransport> postingAccountTransports = new ArrayList<>();
		postingAccountTransports.add(new PostingAccountTransportBuilder().forPostingAccount1().build());
		postingAccountTransports.add(new PostingAccountTransportBuilder().forPostingAccount2().build());
		postingAccountTransports.add(new PostingAccountTransportBuilder().forPostingAccount3().build());
		expected.setPostingAccountTransports(postingAccountTransports);

		final List<ContractpartnerTransport> contractpartnerTransports = new ArrayList<>();
		contractpartnerTransports.add(new ContractpartnerTransportBuilder().forContractpartner1().build());
		contractpartnerTransports.add(new ContractpartnerTransportBuilder().forContractpartner2().build());
		if (extraContractpartner != null) {
			contractpartnerTransports.add(extraContractpartner);
		}
		expected.setContractpartnerTransports(contractpartnerTransports);

		final List<CapitalsourceTransport> capitalsourceTransports = new ArrayList<>();
		capitalsourceTransports.add(new CapitalsourceTransportBuilder().forCapitalsource1().build());
		capitalsourceTransports.add(new CapitalsourceTransportBuilder().forCapitalsource2().build());
		if (extraCapitalsource != null) {
			capitalsourceTransports.add(extraCapitalsource);
		}
		expected.setCapitalsourceTransports(capitalsourceTransports);
		expected.setValidationItemTransports(validationItems);
		expected.setResult(Boolean.FALSE);

		final UpdatePreDefMoneyflowResponse actual = super.callUsecaseWithContent("", this.method, request, false,
				UpdatePreDefMoneyflowResponse.class);

		Assert.assertEquals(expected.getErrorResponse(), actual.getErrorResponse());
		Assert.assertEquals(expected.getResult(), actual.getResult());
		Assert.assertEquals(expected.getValidationItemTransports(), actual.getValidationItemTransports());
		Assert.assertEquals(expected.getPostingAccountTransports(), actual.getPostingAccountTransports());
		Assert.assertEquals(expected.getCapitalsourceTransports(), actual.getCapitalsourceTransports());
		Assert.assertEquals(expected.getContractpartnerTransports(), actual.getContractpartnerTransports());
		Assert.assertEquals(expected, actual);

	}

	@Test
	public void test_emptyComment_Error() throws Exception {
		final PreDefMoneyflowTransport transport = new PreDefMoneyflowTransportBuilder().forNewPreDefMoneyflow()
				.build();
		transport.setComment("");

		this.testError(transport, ErrorCode.COMMENT_IS_NOT_SET, null, null);
	}

	@Test
	public void test_nullComment_Error() throws Exception {
		final PreDefMoneyflowTransport transport = new PreDefMoneyflowTransportBuilder().forNewPreDefMoneyflow()
				.build();
		transport.setComment(null);

		this.testError(transport, ErrorCode.COMMENT_IS_NOT_SET, null, null);
	}

	@Test
	public void test_nullCapitalsource_Error() throws Exception {
		final PreDefMoneyflowTransport transport = new PreDefMoneyflowTransportBuilder().forNewPreDefMoneyflow()
				.build();
		transport.setCapitalsourceid(null);

		this.testError(transport, ErrorCode.CAPITALSOURCE_IS_NOT_SET, null, null);
	}

	@Test
	public void test_notExistingCapitalsource_Error() throws Exception {
		final PreDefMoneyflowTransport transport = new PreDefMoneyflowTransportBuilder().forNewPreDefMoneyflow()
				.build();
		transport.setCapitalsourceid(CapitalsourceTransportBuilder.NON_EXISTING_ID);

		this.testError(transport, ErrorCode.CAPITALSOURCE_DOES_NOT_EXIST, null, null);
	}

	@Test
	public void test_noLongerValidCapitalsource_Error() throws Exception {
		final PreDefMoneyflowTransport transport = new PreDefMoneyflowTransportBuilder().forNewPreDefMoneyflow()
				.build();
		transport.setCapitalsourceid(CapitalsourceTransportBuilder.CAPITALSOURCE3_ID);
		final CapitalsourceTransport extraCapitalsource = new CapitalsourceTransportBuilder().forCapitalsource3()
				.build();

		this.testError(transport, ErrorCode.CAPITALSOURCE_USE_OUT_OF_VALIDITY, extraCapitalsource, null);
	}

	@Test
	public void test_nullContractpartner_Error() throws Exception {
		final PreDefMoneyflowTransport transport = new PreDefMoneyflowTransportBuilder().forNewPreDefMoneyflow()
				.build();
		transport.setContractpartnerid(null);

		this.testError(transport, ErrorCode.CONTRACTPARTNER_IS_NOT_SET, null, null);
	}

	@Test
	public void test_notExistingContractpartner_Error() throws Exception {
		final PreDefMoneyflowTransport transport = new PreDefMoneyflowTransportBuilder().forNewPreDefMoneyflow()
				.build();
		transport.setContractpartnerid(ContractpartnerTransportBuilder.NON_EXISTING_ID);

		this.testError(transport, ErrorCode.CONTRACTPARTNER_DOES_NOT_EXIST, null, null);
	}

	@Test
	public void test_noLongerValidContractpartner_Error() throws Exception {
		final PreDefMoneyflowTransport transport = new PreDefMoneyflowTransportBuilder().forNewPreDefMoneyflow()
				.build();
		transport.setContractpartnerid(ContractpartnerTransportBuilder.CONTRACTPARTNER3_ID);
		final ContractpartnerTransport extraContractpartner = new ContractpartnerTransportBuilder()
				.forContractpartner3().build();

		this.testError(transport, ErrorCode.CONTRACTPARTNER_NO_LONGER_VALID, null, extraContractpartner);
	}

	@Test
	public void test_nullAmount_Error() throws Exception {
		final PreDefMoneyflowTransport transport = new PreDefMoneyflowTransportBuilder().forNewPreDefMoneyflow()
				.build();
		transport.setAmount(null);

		this.testError(transport, ErrorCode.AMOUNT_IS_ZERO, null, null);
	}

	@Test
	public void test_zeroAmount_Error() throws Exception {
		final PreDefMoneyflowTransport transport = new PreDefMoneyflowTransportBuilder().forNewPreDefMoneyflow()
				.build();
		transport.setAmount(BigDecimal.ZERO);

		this.testError(transport, ErrorCode.AMOUNT_IS_ZERO, null, null);
	}

	// make sure it 0 is compared with compareTo not with equals
	@Test
	public void test_0_00Amount_Error() throws Exception {
		final PreDefMoneyflowTransport transport = new PreDefMoneyflowTransportBuilder().forNewPreDefMoneyflow()
				.build();
		transport.setAmount(new BigDecimal("0.00000"));

		this.testError(transport, ErrorCode.AMOUNT_IS_ZERO, null, null);
	}

	@Test
	public void test_nullPostingAccount_Error() throws Exception {
		final PreDefMoneyflowTransport transport = new PreDefMoneyflowTransportBuilder().forNewPreDefMoneyflow()
				.build();
		transport.setPostingaccountid(null);

		this.testError(transport, ErrorCode.POSTING_ACCOUNT_NOT_SPECIFIED, null, null);
	}

	@Test
	public void test_notExistingPostingAccount_Error() throws Exception {
		final PreDefMoneyflowTransport transport = new PreDefMoneyflowTransportBuilder().forNewPreDefMoneyflow()
				.build();
		transport.setPostingaccountid(PostingAccountTransportBuilder.NON_EXISTING_ID);

		this.testError(transport, ErrorCode.POSTING_ACCOUNT_NOT_SPECIFIED, null, null);
	}

	@Test
	public void test_notExisting_NothingHappend() throws Exception {
		final UpdatePreDefMoneyflowRequest request = new UpdatePreDefMoneyflowRequest();

		final PreDefMoneyflowTransport transport = new PreDefMoneyflowTransportBuilder().forNewPreDefMoneyflow()
				.build();

		request.setPreDefMoneyflowTransport(transport);

		super.callUsecaseWithContent("", this.method, request, true, Object.class);

		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final PreDefMoneyflowID preDefMoneyflowId = new PreDefMoneyflowID(PreDefMoneyflowTransportBuilder.NEXT_ID);
		final PreDefMoneyflow preDefMoneyflow = this.preDefMoneyflowService.getPreDefMoneyflowById(userId,
				preDefMoneyflowId);

		Assert.assertNull(preDefMoneyflow);
	}

	@Test
	public void test_AuthorizationRequired_Error() throws Exception {
		this.userName = null;
		this.userPassword = null;
		final ErrorResponse actual = super.callUsecaseWithoutContent("", this.method, false, ErrorResponse.class);
		Assert.assertEquals(super.accessDeniedErrorResponse(), actual);
	}

}