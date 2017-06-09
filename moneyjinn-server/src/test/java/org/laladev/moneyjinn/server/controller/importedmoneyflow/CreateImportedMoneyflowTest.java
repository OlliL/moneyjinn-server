package org.laladev.moneyjinn.server.controller.importedmoneyflow;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Test;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.core.rest.model.ErrorResponse;
import org.laladev.moneyjinn.core.rest.model.importedmoneyflow.CreateImportedMoneyflowRequest;
import org.laladev.moneyjinn.core.rest.model.transport.ImportedMoneyflowTransport;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.capitalsource.CapitalsourceID;
import org.laladev.moneyjinn.model.moneyflow.ImportedMoneyflow;
import org.laladev.moneyjinn.model.moneyflow.ImportedMoneyflowStatus;
import org.laladev.moneyjinn.server.builder.CapitalsourceTransportBuilder;
import org.laladev.moneyjinn.server.builder.ImportedMoneyflowTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.laladev.moneyjinn.service.api.ICapitalsourceService;
import org.laladev.moneyjinn.service.api.IImportedMoneyflowService;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.jdbc.Sql;

public class CreateImportedMoneyflowTest extends AbstractControllerTest {

	@Inject
	IImportedMoneyflowService importedMoneyflowService;

	@Inject
	ICapitalsourceService capitalsourceService;

	private final HttpMethod method = HttpMethod.POST;

	@Override
	protected String getUsername() {
		return null;
	}

	@Override
	protected String getPassword() {
		return null;
	}

	@Override
	protected String getUsecase() {
		return super.getUsecaseFromTestClassName(this.getClass());
	}

	@Test
	public void test_standardRequestInsert_SuccessfullNoContent() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final List<CapitalsourceID> capitalsourceIds = Arrays
				.asList(new CapitalsourceID(CapitalsourceTransportBuilder.CAPITALSOURCE1_ID));
		List<ImportedMoneyflow> importedMoneyflows = this.importedMoneyflowService
				.getAllImportedMoneyflowsByCapitalsourceIds(userId, capitalsourceIds, ImportedMoneyflowStatus.CREATED);

		Assert.assertNotNull(importedMoneyflows);
		final int sizeBeforeInsert = importedMoneyflows.size();

		final CreateImportedMoneyflowRequest request = new CreateImportedMoneyflowRequest();
		final ImportedMoneyflowTransport transport = new ImportedMoneyflowTransportBuilder().forNewImportedMoneyflow()
				.build();

		request.setImportedMoneyflowTransport(transport);
		super.callUsecaseWithContent("", this.method, request, true, Object.class);

		importedMoneyflows = this.importedMoneyflowService.getAllImportedMoneyflowsByCapitalsourceIds(userId,
				capitalsourceIds, ImportedMoneyflowStatus.CREATED);

		Assert.assertNotNull(importedMoneyflows);
		Assert.assertEquals(sizeBeforeInsert + 1, importedMoneyflows.size());
		Assert.assertTrue(transport.getAmount().compareTo(importedMoneyflows.get(2).getAmount()) == 0);
		Assert.assertEquals(CapitalsourceTransportBuilder.CAPITALSOURCE1_ID,
				importedMoneyflows.get(2).getCapitalsource().getId().getId());
	}

	@Test
	public void test_insertwithDuplicateExternalId_NotSuccessfullButIgnored() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final List<CapitalsourceID> capitalsourceIds = Arrays
				.asList(new CapitalsourceID(CapitalsourceTransportBuilder.CAPITALSOURCE1_ID));
		final List<ImportedMoneyflow> importedMoneyflowsOrig = this.importedMoneyflowService
				.getAllImportedMoneyflowsByCapitalsourceIds(userId, capitalsourceIds, null);

		Assert.assertNotNull(importedMoneyflowsOrig);

		final CreateImportedMoneyflowRequest request = new CreateImportedMoneyflowRequest();
		final ImportedMoneyflowTransport transport = new ImportedMoneyflowTransportBuilder().forNewImportedMoneyflow()
				.build();
		transport.setExternalid(ImportedMoneyflowTransportBuilder.IMPORTED_MONEYFLOW1_EXTERNAL_ID);

		request.setImportedMoneyflowTransport(transport);
		super.callUsecaseWithContent("", this.method, request, true, Object.class);

		final List<ImportedMoneyflow> importedMoneyflows = this.importedMoneyflowService
				.getAllImportedMoneyflowsByCapitalsourceIds(userId, capitalsourceIds, null);

		Assert.assertEquals(importedMoneyflowsOrig, importedMoneyflows);
	}

	@Test
	public void test_Bic8Digits_fillesUpTo11Digits() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final List<CapitalsourceID> capitalsourceIds = Arrays
				.asList(new CapitalsourceID(CapitalsourceTransportBuilder.CAPITALSOURCE1_ID));
		List<ImportedMoneyflow> importedMoneyflows = this.importedMoneyflowService
				.getAllImportedMoneyflowsByCapitalsourceIds(userId, capitalsourceIds, ImportedMoneyflowStatus.CREATED);

		Assert.assertNotNull(importedMoneyflows);
		final int sizeBeforeInsert = importedMoneyflows.size();

		final CreateImportedMoneyflowRequest request = new CreateImportedMoneyflowRequest();
		final ImportedMoneyflowTransport transport = new ImportedMoneyflowTransportBuilder().forNewImportedMoneyflow()
				.build();

		transport.setBankCode("ABCDEFGH");
		request.setImportedMoneyflowTransport(transport);
		super.callUsecaseWithContent("", this.method, request, true, Object.class);

		importedMoneyflows = this.importedMoneyflowService.getAllImportedMoneyflowsByCapitalsourceIds(userId,
				capitalsourceIds, ImportedMoneyflowStatus.CREATED);

		Assert.assertNotNull(importedMoneyflows);
		Assert.assertEquals(sizeBeforeInsert + 1, importedMoneyflows.size());
		Assert.assertTrue(transport.getAmount().compareTo(importedMoneyflows.get(2).getAmount()) == 0);
		Assert.assertEquals(CapitalsourceTransportBuilder.CAPITALSOURCE1_ID,
				importedMoneyflows.get(2).getCapitalsource().getId().getId());
		Assert.assertEquals(transport.getBankCode() + "XXX", importedMoneyflows.get(2).getBankAccount().getBankCode());
	}

	@Test
	public void test_emptyContractpartnerBankAccount_SuccessfullNoContent() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final List<CapitalsourceID> capitalsourceIds = Arrays
				.asList(new CapitalsourceID(CapitalsourceTransportBuilder.CAPITALSOURCE1_ID));
		List<ImportedMoneyflow> importedMoneyflows = this.importedMoneyflowService
				.getAllImportedMoneyflowsByCapitalsourceIds(userId, capitalsourceIds, ImportedMoneyflowStatus.CREATED);

		Assert.assertNotNull(importedMoneyflows);
		final int sizeBeforeInsert = importedMoneyflows.size();

		final CreateImportedMoneyflowRequest request = new CreateImportedMoneyflowRequest();
		final ImportedMoneyflowTransport transport = new ImportedMoneyflowTransportBuilder().forNewImportedMoneyflow()
				.build();
		transport.setAccountNumber(null);
		transport.setBankCode(null);
		request.setImportedMoneyflowTransport(transport);
		super.callUsecaseWithContent("", this.method, request, true, Object.class);

		importedMoneyflows = this.importedMoneyflowService.getAllImportedMoneyflowsByCapitalsourceIds(userId,
				capitalsourceIds, ImportedMoneyflowStatus.CREATED);

		Assert.assertNotNull(importedMoneyflows);
		Assert.assertEquals(sizeBeforeInsert + 1, importedMoneyflows.size());
		Assert.assertTrue(transport.getAmount().compareTo(importedMoneyflows.get(2).getAmount()) == 0);
		Assert.assertEquals(CapitalsourceTransportBuilder.CAPITALSOURCE1_ID,
				importedMoneyflows.get(2).getCapitalsource().getId().getId());
	}

	@Test
	public void test_capitalsourceNotAllowedToBeImported_errorResponse() throws Exception {
		final CreateImportedMoneyflowRequest request = new CreateImportedMoneyflowRequest();

		final ImportedMoneyflowTransport transport = new ImportedMoneyflowTransportBuilder().forNewImportedMoneyflow()
				.build();
		transport.setAccountNumberCapitalsource(CapitalsourceTransportBuilder.CAPITALSOURCE2_ACCOUNTNUMBER);
		transport.setBankCodeCapitalsource(CapitalsourceTransportBuilder.CAPITALSOURCE2_BANKCODE);

		request.setImportedMoneyflowTransport(transport);

		final ErrorResponse expected = new ErrorResponse();
		expected.setCode(ErrorCode.CAPITALSOURCE_IMPORT_NOT_ALLOWED.getErrorCode());
		expected.setMessage("Import of this capitalsource is not allowed!");

		final ErrorResponse actual = super.callUsecaseWithContent("", this.method, request, false, ErrorResponse.class);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void test_capitalsourceAllowsOnlyBalancesToBeImported_errorResponse() throws Exception {
		final CreateImportedMoneyflowRequest request = new CreateImportedMoneyflowRequest();

		final ImportedMoneyflowTransport transport = new ImportedMoneyflowTransportBuilder().forNewImportedMoneyflow()
				.build();
		transport.setAccountNumberCapitalsource(CapitalsourceTransportBuilder.CAPITALSOURCE5_ACCOUNTNUMBER);
		transport.setBankCodeCapitalsource(CapitalsourceTransportBuilder.CAPITALSOURCE5_BANKCODE);

		request.setImportedMoneyflowTransport(transport);

		final ErrorResponse expected = new ErrorResponse();
		expected.setCode(ErrorCode.CAPITALSOURCE_IMPORT_NOT_ALLOWED.getErrorCode());
		expected.setMessage("Import of this capitalsource is not allowed!");

		final ErrorResponse actual = super.callUsecaseWithContent("", this.method, request, false, ErrorResponse.class);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void test_unknownAccountNumber_errorResponse() throws Exception {
		final CreateImportedMoneyflowRequest request = new CreateImportedMoneyflowRequest();

		final ImportedMoneyflowTransport transport = new ImportedMoneyflowTransportBuilder().forNewImportedMoneyflow()
				.build();
		transport.setAccountNumberCapitalsource("1");

		request.setImportedMoneyflowTransport(transport);

		final ErrorResponse expected = new ErrorResponse();
		expected.setCode(ErrorCode.CAPITALSOURCE_NOT_FOUND.getErrorCode());
		expected.setMessage("No matching capitalsource found!");

		final ErrorResponse actual = super.callUsecaseWithContent("", this.method, request, false, ErrorResponse.class);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void test_unknownBankCode_errorResponse() throws Exception {
		final CreateImportedMoneyflowRequest request = new CreateImportedMoneyflowRequest();

		final ImportedMoneyflowTransport transport = new ImportedMoneyflowTransportBuilder().forNewImportedMoneyflow()
				.build();
		transport.setBankCodeCapitalsource("1");

		request.setImportedMoneyflowTransport(transport);

		final ErrorResponse expected = new ErrorResponse();
		expected.setCode(ErrorCode.CAPITALSOURCE_NOT_FOUND.getErrorCode());
		expected.setMessage("No matching capitalsource found!");

		final ErrorResponse actual = super.callUsecaseWithContent("", this.method, request, false, ErrorResponse.class);

		Assert.assertEquals(expected, actual);
	}

	@Test
	@Sql("classpath:h2defaults.sql")
	public void test_emptyDatabase_noException() throws Exception {
		final CreateImportedMoneyflowRequest request = new CreateImportedMoneyflowRequest();

		final ImportedMoneyflowTransport transport = new ImportedMoneyflowTransportBuilder().forNewImportedMoneyflow()
				.build();
		transport.setBankCodeCapitalsource("1");

		request.setImportedMoneyflowTransport(transport);

		final ErrorResponse expected = new ErrorResponse();
		expected.setCode(ErrorCode.CAPITALSOURCE_NOT_FOUND.getErrorCode());
		expected.setMessage("No matching capitalsource found!");

		final ErrorResponse actual = super.callUsecaseWithContent("", this.method, request, false, ErrorResponse.class);

		Assert.assertEquals(expected, actual);
	}

}