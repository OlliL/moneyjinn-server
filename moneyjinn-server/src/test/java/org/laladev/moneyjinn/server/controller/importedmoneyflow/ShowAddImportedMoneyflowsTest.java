package org.laladev.moneyjinn.server.controller.importedmoneyflow;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.laladev.moneyjinn.core.rest.model.ErrorResponse;
import org.laladev.moneyjinn.core.rest.model.importedmoneyflow.ShowAddImportedMoneyflowsResponse;
import org.laladev.moneyjinn.core.rest.model.transport.CapitalsourceTransport;
import org.laladev.moneyjinn.core.rest.model.transport.ContractpartnerTransport;
import org.laladev.moneyjinn.core.rest.model.transport.ImportedMoneyflowTransport;
import org.laladev.moneyjinn.core.rest.model.transport.PostingAccountTransport;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.moneyflow.ImportedMoneyflowID;
import org.laladev.moneyjinn.server.builder.CapitalsourceTransportBuilder;
import org.laladev.moneyjinn.server.builder.ContractpartnerTransportBuilder;
import org.laladev.moneyjinn.server.builder.ImportedMoneyflowTransportBuilder;
import org.laladev.moneyjinn.server.builder.PostingAccountTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.laladev.moneyjinn.service.api.ICapitalsourceService;
import org.laladev.moneyjinn.service.api.IImportedMoneyflowService;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.jdbc.Sql;

public class ShowAddImportedMoneyflowsTest extends AbstractControllerTest {

	@Inject
	IImportedMoneyflowService importedMoneyflowService;

	@Inject
	ICapitalsourceService capitalsourceService;

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
	public void test_standardRequest_Successfull() throws Exception {
		final ShowAddImportedMoneyflowsResponse expected = new ShowAddImportedMoneyflowsResponse();
		final List<PostingAccountTransport> postingAccountTransports = new ArrayList<>();
		postingAccountTransports.add(new PostingAccountTransportBuilder().forPostingAccount1().build());
		postingAccountTransports.add(new PostingAccountTransportBuilder().forPostingAccount2().build());
		postingAccountTransports.add(new PostingAccountTransportBuilder().forPostingAccount3().build());
		expected.setPostingAccountTransports(postingAccountTransports);

		final List<ContractpartnerTransport> contractpartnerTransports = new ArrayList<>();
		contractpartnerTransports.add(new ContractpartnerTransportBuilder().forContractpartner1().build());
		contractpartnerTransports.add(new ContractpartnerTransportBuilder().forContractpartner2().build());
		expected.setContractpartnerTransports(contractpartnerTransports);

		final List<CapitalsourceTransport> capitalsourceTransports = new ArrayList<>();
		capitalsourceTransports.add(new CapitalsourceTransportBuilder().forCapitalsource1().build());
		capitalsourceTransports.add(new CapitalsourceTransportBuilder().forCapitalsource2().build());
		expected.setCapitalsourceTransports(capitalsourceTransports);

		final List<ImportedMoneyflowTransport> importedMoneyflowTransports = new ArrayList<>();
		importedMoneyflowTransports.add(new ImportedMoneyflowTransportBuilder().forImportedMoneyflow1().build());
		importedMoneyflowTransports.add(new ImportedMoneyflowTransportBuilder().forImportedMoneyflow2().build());
		expected.setImportedMoneyflowTransports(importedMoneyflowTransports);

		final ShowAddImportedMoneyflowsResponse actual = super.callUsecaseWithoutContent("", this.method, false,
				ShowAddImportedMoneyflowsResponse.class);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void test_noImportedData_emptyResponse() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		this.importedMoneyflowService.deleteImportedMoneyflowById(userId,
				new ImportedMoneyflowID(ImportedMoneyflowTransportBuilder.IMPORTED_MONEYFLOW1_ID));
		this.importedMoneyflowService.deleteImportedMoneyflowById(userId,
				new ImportedMoneyflowID(ImportedMoneyflowTransportBuilder.IMPORTED_MONEYFLOW2_ID));

		final ShowAddImportedMoneyflowsResponse expected = new ShowAddImportedMoneyflowsResponse();

		final ShowAddImportedMoneyflowsResponse actual = super.callUsecaseWithoutContent("", this.method, false,
				ShowAddImportedMoneyflowsResponse.class);

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
		final ShowAddImportedMoneyflowsResponse expected = new ShowAddImportedMoneyflowsResponse();
		final ShowAddImportedMoneyflowsResponse actual = super.callUsecaseWithoutContent("", this.method, false,
				ShowAddImportedMoneyflowsResponse.class);

		Assert.assertEquals(expected, actual);
	}

}