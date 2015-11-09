package org.laladev.moneyjinn.server.controller.importedbalance;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.laladev.moneyjinn.businesslogic.model.ImportedBalance;
import org.laladev.moneyjinn.businesslogic.model.access.GroupID;
import org.laladev.moneyjinn.businesslogic.model.access.UserID;
import org.laladev.moneyjinn.businesslogic.model.capitalsource.Capitalsource;
import org.laladev.moneyjinn.businesslogic.model.capitalsource.CapitalsourceID;
import org.laladev.moneyjinn.businesslogic.service.api.ICapitalsourceService;
import org.laladev.moneyjinn.businesslogic.service.api.IImportedBalanceService;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.core.rest.model.ErrorResponse;
import org.laladev.moneyjinn.core.rest.model.importedbalance.CreateImportedBalanceRequest;
import org.laladev.moneyjinn.core.rest.model.transport.ImportedBalanceTransport;
import org.laladev.moneyjinn.server.builder.CapitalsourceTransportBuilder;
import org.laladev.moneyjinn.server.builder.GroupTransportBuilder;
import org.laladev.moneyjinn.server.builder.ImportedBalanceTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.jdbc.Sql;

public class CreateImportedBalanceTest extends AbstractControllerTest {

	@Inject
	IImportedBalanceService importedBalanceService;

	@Inject
	ICapitalsourceService capitalsourceService;

	private final HttpMethod method = HttpMethod.POST;
	private String userName;
	private String userPassword;

	@Before
	public void setUp() {
		this.userName = null;
		this.userPassword = null;
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
	public void test_standardRequestInsert_SuccessfullNoContent() throws Exception {
		final CreateImportedBalanceRequest request = new CreateImportedBalanceRequest();

		final ImportedBalanceTransport transport = new ImportedBalanceTransportBuilder().forNewImportedBalance()
				.build();

		request.setImportedBalanceTransport(transport);

		super.callUsecaseWithContent("", this.method, request, true, Object.class);

		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final List<ImportedBalance> importedBalances = this.importedBalanceService
				.getAllImportedBalancesByCapitalsourceIds(userId,
						Arrays.asList(new CapitalsourceID(CapitalsourceTransportBuilder.CAPITALSOURCE1_ID)));

		Assert.assertNotNull(importedBalances);
		Assert.assertEquals(1, importedBalances.size());
		Assert.assertTrue(transport.getBalance().compareTo(importedBalances.get(0).getBalance()) == 0);
		Assert.assertEquals(CapitalsourceTransportBuilder.CAPITALSOURCE1_ID,
				importedBalances.get(0).getCapitalsource().getId().getId());
	}

	@Test
	public void test_standardRequestUpdate_SuccessfullNoContent() throws Exception {
		final CreateImportedBalanceRequest request = new CreateImportedBalanceRequest();

		final ImportedBalanceTransport transport = new ImportedBalanceTransportBuilder().forImportedBalance1()
				.withBalance(BigDecimal.TEN).build();

		request.setImportedBalanceTransport(transport);

		final UserID userId = new UserID(UserTransportBuilder.USER3_ID);
		final GroupID groupId = new GroupID(GroupTransportBuilder.GROUP1_ID);
		final CapitalsourceID capitalsourceId = new CapitalsourceID(CapitalsourceTransportBuilder.CAPITALSOURCE4_ID);

		final Capitalsource capitalsource = this.capitalsourceService.getCapitalsourceById(userId, groupId,
				capitalsourceId);
		capitalsource.setValidTil(LocalDate.now().plusDays(1l));

		// Capitalsource 4 is no longer valid but we need it valid for this test, so "modify" it :-/
		this.capitalsourceService.updateCapitalsource(capitalsource);

		super.callUsecaseWithContent("", this.method, request, true, Object.class);

		final List<ImportedBalance> importedBalances = this.importedBalanceService
				.getAllImportedBalancesByCapitalsourceIds(userId, Arrays.asList(capitalsourceId));

		Assert.assertNotNull(importedBalances);
		Assert.assertEquals(1, importedBalances.size());
		Assert.assertTrue(BigDecimal.TEN.compareTo(importedBalances.get(0).getBalance()) == 0);
		Assert.assertEquals(CapitalsourceTransportBuilder.CAPITALSOURCE4_ID,
				importedBalances.get(0).getCapitalsource().getId().getId());
	}

	@Test
	public void test_capitalsourceNotAllowedToBeImported_errorResponse() throws Exception {
		final CreateImportedBalanceRequest request = new CreateImportedBalanceRequest();

		final ImportedBalanceTransport transport = new ImportedBalanceTransportBuilder().forNewImportedBalance()
				.build();
		transport.setAccountNumberCapitalsource(CapitalsourceTransportBuilder.CAPITALSOURCE2_ACCOUNTNUMBER);
		transport.setBankCodeCapitalsource(CapitalsourceTransportBuilder.CAPITALSOURCE2_BANKCODE);

		request.setImportedBalanceTransport(transport);

		final ErrorResponse expected = new ErrorResponse();
		expected.setCode(ErrorCode.CAPITALSOURCE_IMPORT_NOT_ALLOWED.getErrorCode());
		expected.setMessage("Import of this capitalsource is not allowed!");

		final ErrorResponse actual = super.callUsecaseWithContent("", this.method, request, false, ErrorResponse.class);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void test_unknownAccountNumber_errorResponse() throws Exception {
		final CreateImportedBalanceRequest request = new CreateImportedBalanceRequest();

		final ImportedBalanceTransport transport = new ImportedBalanceTransportBuilder().forNewImportedBalance()
				.build();
		transport.setAccountNumberCapitalsource("1");

		request.setImportedBalanceTransport(transport);

		final ErrorResponse expected = new ErrorResponse();
		expected.setCode(ErrorCode.CAPITALSOURCE_NOT_FOUND.getErrorCode());
		expected.setMessage("No matching capitalsource found!");

		final ErrorResponse actual = super.callUsecaseWithContent("", this.method, request, false, ErrorResponse.class);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void test_unknownBankCode_errorResponse() throws Exception {
		final CreateImportedBalanceRequest request = new CreateImportedBalanceRequest();

		final ImportedBalanceTransport transport = new ImportedBalanceTransportBuilder().forNewImportedBalance()
				.build();
		transport.setBankCodeCapitalsource("1");

		request.setImportedBalanceTransport(transport);

		final ErrorResponse expected = new ErrorResponse();
		expected.setCode(ErrorCode.CAPITALSOURCE_NOT_FOUND.getErrorCode());
		expected.setMessage("No matching capitalsource found!");

		final ErrorResponse actual = super.callUsecaseWithContent("", this.method, request, false, ErrorResponse.class);

		Assert.assertEquals(expected, actual);
	}

	@Test
	@Sql("classpath:h2defaults.sql")
	public void test_emptyDatabase_noException() throws Exception {
		final CreateImportedBalanceRequest request = new CreateImportedBalanceRequest();

		final ImportedBalanceTransport transport = new ImportedBalanceTransportBuilder().forNewImportedBalance()
				.build();
		transport.setBankCodeCapitalsource("1");

		request.setImportedBalanceTransport(transport);

		final ErrorResponse expected = new ErrorResponse();
		expected.setCode(ErrorCode.CAPITALSOURCE_NOT_FOUND.getErrorCode());
		expected.setMessage("No matching capitalsource found!");

		final ErrorResponse actual = super.callUsecaseWithContent("", this.method, request, false, ErrorResponse.class);

		Assert.assertEquals(expected, actual);
	}

}