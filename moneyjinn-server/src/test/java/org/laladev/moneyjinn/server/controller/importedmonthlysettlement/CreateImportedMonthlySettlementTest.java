
package org.laladev.moneyjinn.server.controller.importedmonthlysettlement;

import java.math.BigDecimal;
import java.time.Month;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.monthlysettlement.ImportedMonthlySettlement;
import org.laladev.moneyjinn.server.builder.CapitalsourceTransportBuilder;
import org.laladev.moneyjinn.server.builder.ImportedMonthlySettlementTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractImportUserControllerTest;
import org.laladev.moneyjinn.server.controller.api.ImportedMonthlySettlementControllerApi;
import org.laladev.moneyjinn.server.model.CreateImportedMonthlySettlementRequest;
import org.laladev.moneyjinn.server.model.ErrorResponse;
import org.laladev.moneyjinn.server.model.ImportedMonthlySettlementTransport;
import org.laladev.moneyjinn.service.api.IImportedMonthlySettlementService;

import jakarta.inject.Inject;

class CreateImportedMonthlySettlementTest extends AbstractImportUserControllerTest {
	@Inject
	private IImportedMonthlySettlementService importedMonthlySettlementService;

	@Override
	protected void loadMethod() {
		super.getMock(ImportedMonthlySettlementControllerApi.class).createImportedMonthlySettlement(null);
	}

	@Test
	void test_standardRequestInsert_SuccessfullNoContent() throws Exception {
		final CreateImportedMonthlySettlementRequest request = new CreateImportedMonthlySettlementRequest();
		final ImportedMonthlySettlementTransport transport = new ImportedMonthlySettlementTransportBuilder()
				.forNewImportedMonthlySettlement().build();
		request.setImportedMonthlySettlementTransport(transport);

		super.callUsecaseExpect204(request);

		final UserID userId = new UserID(transport.getUserid());
		final List<ImportedMonthlySettlement> importedMonthlySettlements = this.importedMonthlySettlementService
				.getImportedMonthlySettlementsByMonth(userId, transport.getYear(), Month.of(transport.getMonth()));
		Assertions.assertNotNull(importedMonthlySettlements);
		Assertions.assertEquals(1, importedMonthlySettlements.size());
		Assertions.assertEquals(ImportedMonthlySettlementTransportBuilder.NEXT_ID,
				importedMonthlySettlements.get(0).getId().getId());
		Assertions.assertEquals(CapitalsourceTransportBuilder.CAPITALSOURCE4_ID,
				importedMonthlySettlements.get(0).getCapitalsource().getId().getId());
	}

	@Test
	void test_standardRequestUpdate_SuccessfullNoContent() throws Exception {
		final CreateImportedMonthlySettlementRequest request = new CreateImportedMonthlySettlementRequest();
		final ImportedMonthlySettlementTransport transport = new ImportedMonthlySettlementTransportBuilder()
				.forImportedMonthlySettlement1().build();
		transport.setAmount(BigDecimal.TEN);
		request.setImportedMonthlySettlementTransport(transport);
		final UserID userId = new UserID(transport.getUserid());
		List<ImportedMonthlySettlement> importedMonthlySettlements = this.importedMonthlySettlementService
				.getImportedMonthlySettlementsByMonth(userId, transport.getYear(), Month.of(transport.getMonth()));
		Assertions.assertNotNull(importedMonthlySettlements);
		Assertions.assertEquals(1, importedMonthlySettlements.size());
		Assertions.assertEquals(ImportedMonthlySettlementTransportBuilder.IMPORTED_MONTHLYSETTLEMENT1_ID,
				importedMonthlySettlements.get(0).getId().getId());
		Assertions.assertTrue(BigDecimal.valueOf(9l).compareTo(importedMonthlySettlements.get(0).getAmount()) == 0);

		super.callUsecaseExpect204(request);

		importedMonthlySettlements = this.importedMonthlySettlementService.getImportedMonthlySettlementsByMonth(userId,
				transport.getYear(), Month.of(transport.getMonth()));
		Assertions.assertNotNull(importedMonthlySettlements);
		Assertions.assertEquals(1, importedMonthlySettlements.size());
		Assertions.assertEquals(ImportedMonthlySettlementTransportBuilder.IMPORTED_MONTHLYSETTLEMENT1_ID,
				importedMonthlySettlements.get(0).getId().getId());
		Assertions.assertTrue(BigDecimal.TEN.compareTo(importedMonthlySettlements.get(0).getAmount()) == 0);
	}

	@Test
	void test_onlyBalanceImportAllowedCapitalsourceInsert_SuccessfullNoContent() throws Exception {
		final CreateImportedMonthlySettlementRequest request = new CreateImportedMonthlySettlementRequest();
		final ImportedMonthlySettlementTransport transport = new ImportedMonthlySettlementTransportBuilder()
				.forOnlyBalanceImportedMonthlySettlement().build();
		request.setImportedMonthlySettlementTransport(transport);

		super.callUsecaseExpect204(request);

		final UserID userId = new UserID(UserTransportBuilder.USER3_ID);
		final List<ImportedMonthlySettlement> importedMonthlySettlements = this.importedMonthlySettlementService
				.getImportedMonthlySettlementsByMonth(userId, 2015, Month.FEBRUARY);
		Assertions.assertNotNull(importedMonthlySettlements);
		Assertions.assertEquals(1, importedMonthlySettlements.size());
		Assertions.assertEquals(ImportedMonthlySettlementTransportBuilder.NEXT_ID,
				importedMonthlySettlements.get(0).getId().getId());
		Assertions.assertEquals(CapitalsourceTransportBuilder.CAPITALSOURCE5_ID,
				importedMonthlySettlements.get(0).getCapitalsource().getId().getId());
	}

	@Test
	void test_capitalsourceNotAllowedToBeImported_errorResponse() throws Exception {
		final CreateImportedMonthlySettlementRequest request = new CreateImportedMonthlySettlementRequest();
		final ImportedMonthlySettlementTransport transport = new ImportedMonthlySettlementTransportBuilder()
				.forNewImportedMonthlySettlement().build();
		transport.setAccountNumberCapitalsource(CapitalsourceTransportBuilder.CAPITALSOURCE2_ACCOUNTNUMBER);
		transport.setBankCodeCapitalsource(CapitalsourceTransportBuilder.CAPITALSOURCE2_BANKCODE);
		request.setImportedMonthlySettlementTransport(transport);
		final ErrorResponse expected = new ErrorResponse();
		expected.setCode(ErrorCode.CAPITALSOURCE_IMPORT_NOT_ALLOWED.getErrorCode());
		expected.setMessage("Import of this capitalsource is not allowed!");
		final ErrorResponse actual = super.callUsecaseExpect400(request, ErrorResponse.class);
		Assertions.assertEquals(expected, actual);
	}

	@Test
	void test_unknownAccountNumber_errorResponse() throws Exception {
		final CreateImportedMonthlySettlementRequest request = new CreateImportedMonthlySettlementRequest();
		final ImportedMonthlySettlementTransport transport = new ImportedMonthlySettlementTransportBuilder()
				.forNewImportedMonthlySettlement().build();
		transport.setAccountNumberCapitalsource("1");
		request.setImportedMonthlySettlementTransport(transport);
		final ErrorResponse expected = new ErrorResponse();
		expected.setCode(ErrorCode.CAPITALSOURCE_NOT_FOUND.getErrorCode());
		expected.setMessage("No matching capitalsource found!");
		final ErrorResponse actual = super.callUsecaseExpect400(request, ErrorResponse.class);
		Assertions.assertEquals(expected, actual);
	}

	@Test
	void test_unknownBankCode_errorResponse() throws Exception {
		final CreateImportedMonthlySettlementRequest request = new CreateImportedMonthlySettlementRequest();
		final ImportedMonthlySettlementTransport transport = new ImportedMonthlySettlementTransportBuilder()
				.forNewImportedMonthlySettlement().build();
		transport.setBankCodeCapitalsource("1");
		request.setImportedMonthlySettlementTransport(transport);
		final ErrorResponse expected = new ErrorResponse();
		expected.setCode(ErrorCode.CAPITALSOURCE_NOT_FOUND.getErrorCode());
		expected.setMessage("No matching capitalsource found!");
		final ErrorResponse actual = super.callUsecaseExpect400(request, ErrorResponse.class);
		Assertions.assertEquals(expected, actual);
	}

	@Override
	protected void callUsecaseExpect403ForThisUsecase() throws Exception {
		super.callUsecaseExpect403();
	}

	@Override
	protected void callUsecaseEmptyDatabase() throws Exception {
		final CreateImportedMonthlySettlementRequest request = new CreateImportedMonthlySettlementRequest();
		final ImportedMonthlySettlementTransport transport = new ImportedMonthlySettlementTransportBuilder()
				.forNewImportedMonthlySettlement().build();
		transport.setBankCodeCapitalsource("1");
		request.setImportedMonthlySettlementTransport(transport);
		final ErrorResponse expected = new ErrorResponse();
		expected.setCode(ErrorCode.CAPITALSOURCE_NOT_FOUND.getErrorCode());
		expected.setMessage("No matching capitalsource found!");
		final ErrorResponse actual = super.callUsecaseExpect400(request, ErrorResponse.class);
		Assertions.assertEquals(expected, actual);
	}
}