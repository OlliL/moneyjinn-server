package org.laladev.moneyjinn.server.controller.importedmonthlysettlement;

import java.math.BigDecimal;
import java.time.Month;
import java.util.List;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.core.rest.model.ErrorResponse;
import org.laladev.moneyjinn.core.rest.model.importedmonthlysettlement.CreateImportedMonthlySettlementRequest;
import org.laladev.moneyjinn.core.rest.model.transport.ImportedMonthlySettlementTransport;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.monthlysettlement.ImportedMonthlySettlement;
import org.laladev.moneyjinn.server.builder.CapitalsourceTransportBuilder;
import org.laladev.moneyjinn.server.builder.ImportedMonthlySettlementTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.laladev.moneyjinn.service.api.IImportedMonthlySettlementService;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.jdbc.Sql;

public class CreateImportedMonthlySettlementTest extends AbstractControllerTest {

	@Inject
	IImportedMonthlySettlementService importedMonthlySettlementService;

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
		final CreateImportedMonthlySettlementRequest request = new CreateImportedMonthlySettlementRequest();

		final ImportedMonthlySettlementTransport transport = new ImportedMonthlySettlementTransportBuilder()
				.forNewImportedMonthlySettlement().build();

		request.setImportedMonthlySettlementTransport(transport);

		super.callUsecaseWithContent("", this.method, request, true, Object.class);

		final UserID userId = new UserID(transport.getUserid());
		final List<ImportedMonthlySettlement> importedMonthlySettlements = this.importedMonthlySettlementService
				.getImportedMonthlySettlementsByMonth(userId, transport.getYear(), Month.of(transport.getMonth()));

		Assert.assertNotNull(importedMonthlySettlements);
		Assert.assertEquals(1, importedMonthlySettlements.size());
		Assert.assertEquals(ImportedMonthlySettlementTransportBuilder.NEXT_ID,
				importedMonthlySettlements.get(0).getId().getId());
		Assert.assertEquals(CapitalsourceTransportBuilder.CAPITALSOURCE4_ID,
				importedMonthlySettlements.get(0).getCapitalsource().getId().getId());
	}

	@Test
	public void test_standardRequestUpdate_SuccessfullNoContent() throws Exception {
		final CreateImportedMonthlySettlementRequest request = new CreateImportedMonthlySettlementRequest();

		final ImportedMonthlySettlementTransport transport = new ImportedMonthlySettlementTransportBuilder()
				.forImportedMonthlySettlement1().build();

		transport.setAmount(BigDecimal.TEN);
		request.setImportedMonthlySettlementTransport(transport);

		final UserID userId = new UserID(transport.getUserid());

		List<ImportedMonthlySettlement> importedMonthlySettlements = this.importedMonthlySettlementService
				.getImportedMonthlySettlementsByMonth(userId, transport.getYear(), Month.of(transport.getMonth()));

		Assert.assertNotNull(importedMonthlySettlements);
		Assert.assertEquals(1, importedMonthlySettlements.size());
		Assert.assertEquals(ImportedMonthlySettlementTransportBuilder.IMPORTED_MONTHLYSETTLEMENT1_ID,
				importedMonthlySettlements.get(0).getId().getId());
		Assert.assertTrue(BigDecimal.valueOf(9l).compareTo(importedMonthlySettlements.get(0).getAmount()) == 0);

		super.callUsecaseWithContent("", this.method, request, true, Object.class);

		importedMonthlySettlements = this.importedMonthlySettlementService.getImportedMonthlySettlementsByMonth(userId,
				transport.getYear(), Month.of(transport.getMonth()));

		Assert.assertNotNull(importedMonthlySettlements);
		Assert.assertEquals(1, importedMonthlySettlements.size());
		Assert.assertEquals(ImportedMonthlySettlementTransportBuilder.IMPORTED_MONTHLYSETTLEMENT1_ID,
				importedMonthlySettlements.get(0).getId().getId());
		Assert.assertTrue(BigDecimal.TEN.compareTo(importedMonthlySettlements.get(0).getAmount()) == 0);
	}

	@Test
	public void test_onlyBalanceImportAllowedCapitalsourceInsert_SuccessfullNoContent() throws Exception {
		final CreateImportedMonthlySettlementRequest request = new CreateImportedMonthlySettlementRequest();

		final ImportedMonthlySettlementTransport transport = new ImportedMonthlySettlementTransportBuilder()
				.forOnlyBalanceImportedMonthlySettlement().build();

		request.setImportedMonthlySettlementTransport(transport);

		super.callUsecaseWithContent("", this.method, request, true, Object.class);

		final UserID userId = new UserID(UserTransportBuilder.USER3_ID);
		final List<ImportedMonthlySettlement> importedMonthlySettlements = this.importedMonthlySettlementService
				.getImportedMonthlySettlementsByMonth(userId, (short) 2015, Month.FEBRUARY);

		Assert.assertNotNull(importedMonthlySettlements);
		Assert.assertEquals(1, importedMonthlySettlements.size());
		Assert.assertEquals(ImportedMonthlySettlementTransportBuilder.NEXT_ID,
				importedMonthlySettlements.get(0).getId().getId());
		Assert.assertEquals(CapitalsourceTransportBuilder.CAPITALSOURCE5_ID,
				importedMonthlySettlements.get(0).getCapitalsource().getId().getId());
	}

	@Test
	public void test_capitalsourceNotAllowedToBeImported_errorResponse() throws Exception {
		final CreateImportedMonthlySettlementRequest request = new CreateImportedMonthlySettlementRequest();

		final ImportedMonthlySettlementTransport transport = new ImportedMonthlySettlementTransportBuilder()
				.forNewImportedMonthlySettlement().build();
		transport.setAccountNumberCapitalsource(CapitalsourceTransportBuilder.CAPITALSOURCE2_ACCOUNTNUMBER);
		transport.setBankCodeCapitalsource(CapitalsourceTransportBuilder.CAPITALSOURCE2_BANKCODE);

		request.setImportedMonthlySettlementTransport(transport);

		final ErrorResponse expected = new ErrorResponse();
		expected.setCode(ErrorCode.CAPITALSOURCE_IMPORT_NOT_ALLOWED.getErrorCode());
		expected.setMessage("Import of this capitalsource is not allowed!");

		final ErrorResponse actual = super.callUsecaseWithContent("", this.method, request, false, ErrorResponse.class);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void test_unknownAccountNumber_errorResponse() throws Exception {
		final CreateImportedMonthlySettlementRequest request = new CreateImportedMonthlySettlementRequest();

		final ImportedMonthlySettlementTransport transport = new ImportedMonthlySettlementTransportBuilder()
				.forNewImportedMonthlySettlement().build();
		transport.setAccountNumberCapitalsource("1");

		request.setImportedMonthlySettlementTransport(transport);

		final ErrorResponse expected = new ErrorResponse();
		expected.setCode(ErrorCode.CAPITALSOURCE_NOT_FOUND.getErrorCode());
		expected.setMessage("No matching capitalsource found!");

		final ErrorResponse actual = super.callUsecaseWithContent("", this.method, request, false, ErrorResponse.class);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void test_unknownBankCode_errorResponse() throws Exception {
		final CreateImportedMonthlySettlementRequest request = new CreateImportedMonthlySettlementRequest();

		final ImportedMonthlySettlementTransport transport = new ImportedMonthlySettlementTransportBuilder()
				.forNewImportedMonthlySettlement().build();
		transport.setBankCodeCapitalsource("1");

		request.setImportedMonthlySettlementTransport(transport);

		final ErrorResponse expected = new ErrorResponse();
		expected.setCode(ErrorCode.CAPITALSOURCE_NOT_FOUND.getErrorCode());
		expected.setMessage("No matching capitalsource found!");

		final ErrorResponse actual = super.callUsecaseWithContent("", this.method, request, false, ErrorResponse.class);

		Assert.assertEquals(expected, actual);
	}

	@Test
	@Sql("classpath:h2defaults.sql")
	public void test_emptyDatabase_noException() throws Exception {
		final CreateImportedMonthlySettlementRequest request = new CreateImportedMonthlySettlementRequest();

		final ImportedMonthlySettlementTransport transport = new ImportedMonthlySettlementTransportBuilder()
				.forNewImportedMonthlySettlement().build();
		transport.setBankCodeCapitalsource("1");

		request.setImportedMonthlySettlementTransport(transport);

		final ErrorResponse expected = new ErrorResponse();
		expected.setCode(ErrorCode.CAPITALSOURCE_NOT_FOUND.getErrorCode());
		expected.setMessage("No matching capitalsource found!");

		final ErrorResponse actual = super.callUsecaseWithContent("", this.method, request, false, ErrorResponse.class);

		Assert.assertEquals(expected, actual);
	}

}