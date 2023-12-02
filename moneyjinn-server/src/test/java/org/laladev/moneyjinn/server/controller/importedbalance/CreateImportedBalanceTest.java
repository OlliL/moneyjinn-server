
package org.laladev.moneyjinn.server.controller.importedbalance;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.model.ImportedBalance;
import org.laladev.moneyjinn.model.access.GroupID;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.capitalsource.Capitalsource;
import org.laladev.moneyjinn.model.capitalsource.CapitalsourceID;
import org.laladev.moneyjinn.server.builder.CapitalsourceTransportBuilder;
import org.laladev.moneyjinn.server.builder.GroupTransportBuilder;
import org.laladev.moneyjinn.server.builder.ImportedBalanceTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractImportUserControllerTest;
import org.laladev.moneyjinn.server.controller.api.ImportedBalanceControllerApi;
import org.laladev.moneyjinn.server.model.CreateImportedBalanceRequest;
import org.laladev.moneyjinn.server.model.ErrorResponse;
import org.laladev.moneyjinn.server.model.ImportedBalanceTransport;
import org.laladev.moneyjinn.service.api.ICapitalsourceService;
import org.laladev.moneyjinn.service.api.IImportedBalanceService;

import jakarta.inject.Inject;

class CreateImportedBalanceTest extends AbstractImportUserControllerTest {
	@Inject
	private IImportedBalanceService importedBalanceService;
	@Inject
	private ICapitalsourceService capitalsourceService;

	@Override
	protected void loadMethod() {
		super.getMock(ImportedBalanceControllerApi.class).createImportedBalance(null);
	}

	@Test
	void test_standardRequestInsert_SuccessfullNoContent() throws Exception {
		final CreateImportedBalanceRequest request = new CreateImportedBalanceRequest();
		final ImportedBalanceTransport transport = new ImportedBalanceTransportBuilder().forNewImportedBalance()
				.build();
		request.setImportedBalanceTransport(transport);

		super.callUsecaseExpect204(request);

		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final List<ImportedBalance> importedBalances = this.importedBalanceService
				.getAllImportedBalancesByCapitalsourceIds(userId,
						Arrays.asList(new CapitalsourceID(CapitalsourceTransportBuilder.CAPITALSOURCE1_ID)));
		Assertions.assertNotNull(importedBalances);
		Assertions.assertEquals(1, importedBalances.size());
		Assertions.assertEquals(0, transport.getBalance().compareTo(importedBalances.get(0).getBalance()));
		Assertions.assertEquals(CapitalsourceTransportBuilder.CAPITALSOURCE1_ID,
				importedBalances.get(0).getCapitalsource().getId().getId());
	}

	@Test
	void test_standardRequestUpdate_SuccessfullNoContent() throws Exception {
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
		// Capitalsource 4 is no longer valid but we need it valid for this test, so
		// "modify" it :-/
		this.capitalsourceService.updateCapitalsource(capitalsource);

		super.callUsecaseExpect204(request);

		final List<ImportedBalance> importedBalances = this.importedBalanceService
				.getAllImportedBalancesByCapitalsourceIds(userId, Arrays.asList(capitalsourceId));
		Assertions.assertNotNull(importedBalances);
		Assertions.assertEquals(1, importedBalances.size());
		Assertions.assertEquals(0, BigDecimal.TEN.compareTo(importedBalances.get(0).getBalance()));
		Assertions.assertEquals(CapitalsourceTransportBuilder.CAPITALSOURCE4_ID,
				importedBalances.get(0).getCapitalsource().getId().getId());
	}

	@Test
	void test_onlyBalanceImportAllowedCapitalsourceInsert_SuccessfullNoContent() throws Exception {
		final CreateImportedBalanceRequest request = new CreateImportedBalanceRequest();
		final ImportedBalanceTransport transport = new ImportedBalanceTransportBuilder().forOnlyBalanceImportedBalance()
				.build();
		request.setImportedBalanceTransport(transport);

		super.callUsecaseExpect204(request);

		final UserID userId = new UserID(UserTransportBuilder.USER3_ID);
		final List<ImportedBalance> importedBalances = this.importedBalanceService
				.getAllImportedBalancesByCapitalsourceIds(userId,
						Arrays.asList(new CapitalsourceID(CapitalsourceTransportBuilder.CAPITALSOURCE5_ID)));
		Assertions.assertNotNull(importedBalances);
		Assertions.assertEquals(1, importedBalances.size());
		Assertions.assertEquals(0, transport.getBalance().compareTo(importedBalances.get(0).getBalance()));
		Assertions.assertEquals(CapitalsourceTransportBuilder.CAPITALSOURCE5_ID,
				importedBalances.get(0).getCapitalsource().getId().getId());
	}

	@Test
	void test_capitalsourceNotAllowedToBeImported_errorResponse() throws Exception {
		final CreateImportedBalanceRequest request = new CreateImportedBalanceRequest();
		final ImportedBalanceTransport transport = new ImportedBalanceTransportBuilder().forNewImportedBalance()
				.build();
		transport.setAccountNumberCapitalsource(CapitalsourceTransportBuilder.CAPITALSOURCE2_ACCOUNTNUMBER);
		transport.setBankCodeCapitalsource(CapitalsourceTransportBuilder.CAPITALSOURCE2_BANKCODE);
		request.setImportedBalanceTransport(transport);
		final ErrorResponse expected = new ErrorResponse();
		expected.setCode(ErrorCode.CAPITALSOURCE_IMPORT_NOT_ALLOWED.getErrorCode());
		expected.setMessage("Import of this capitalsource is not allowed!");

		final ErrorResponse actual = super.callUsecaseExpect400(request, ErrorResponse.class);

		Assertions.assertEquals(expected, actual);
	}

	@Test
	void test_unknownAccountNumber_errorResponse() throws Exception {
		final CreateImportedBalanceRequest request = new CreateImportedBalanceRequest();
		final ImportedBalanceTransport transport = new ImportedBalanceTransportBuilder().forNewImportedBalance()
				.build();
		transport.setAccountNumberCapitalsource("1");
		request.setImportedBalanceTransport(transport);
		final ErrorResponse expected = new ErrorResponse();
		expected.setCode(ErrorCode.CAPITALSOURCE_NOT_FOUND.getErrorCode());
		expected.setMessage("No matching capitalsource found!");

		final ErrorResponse actual = super.callUsecaseExpect400(request, ErrorResponse.class);

		Assertions.assertEquals(expected, actual);
	}

	@Test
	void test_unknownBankCode_errorResponse() throws Exception {
		final CreateImportedBalanceRequest request = new CreateImportedBalanceRequest();
		final ImportedBalanceTransport transport = new ImportedBalanceTransportBuilder().forNewImportedBalance()
				.build();
		transport.setBankCodeCapitalsource("1");
		request.setImportedBalanceTransport(transport);
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
		final CreateImportedBalanceRequest request = new CreateImportedBalanceRequest();
		final ImportedBalanceTransport transport = new ImportedBalanceTransportBuilder().forNewImportedBalance()
				.build();
		transport.setBankCodeCapitalsource("1");
		request.setImportedBalanceTransport(transport);
		final ErrorResponse expected = new ErrorResponse();
		expected.setCode(ErrorCode.CAPITALSOURCE_NOT_FOUND.getErrorCode());
		expected.setMessage("No matching capitalsource found!");

		final ErrorResponse actual = super.callUsecaseExpect400(request, ErrorResponse.class);

		Assertions.assertEquals(expected, actual);
	}
}