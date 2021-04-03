package org.laladev.moneyjinn.server.controller.importedmoneyflow;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.core.rest.model.ErrorResponse;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.capitalsource.CapitalsourceID;
import org.laladev.moneyjinn.model.moneyflow.ImportedMoneyflow;
import org.laladev.moneyjinn.model.moneyflow.ImportedMoneyflowStatus;
import org.laladev.moneyjinn.server.builder.CapitalsourceTransportBuilder;
import org.laladev.moneyjinn.server.builder.ImportedMoneyflowTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.laladev.moneyjinn.service.api.IImportedMoneyflowService;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.jdbc.Sql;

public class DeleteImportedMoneyflowByIdTest extends AbstractControllerTest {

	@Inject
	private IImportedMoneyflowService importedMoneyflowService;

	private final HttpMethod method = HttpMethod.DELETE;
	private String userName;
	private String userPassword;

	@BeforeEach
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
	public void test_standardRequest_emptyResponse() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final List<CapitalsourceID> capitalsourceIds = Arrays
				.asList(new CapitalsourceID(CapitalsourceTransportBuilder.CAPITALSOURCE1_ID));

		List<ImportedMoneyflow> importedMoneyflows = this.importedMoneyflowService
				.getAllImportedMoneyflowsByCapitalsourceIds(userId, capitalsourceIds, null);
		Assertions.assertNotNull(importedMoneyflows);
		final int sizeBeforeDelete = importedMoneyflows.size();

		importedMoneyflows = this.importedMoneyflowService.getAllImportedMoneyflowsByCapitalsourceIds(userId,
				capitalsourceIds, ImportedMoneyflowStatus.CREATED);
		Assertions.assertNotNull(importedMoneyflows);
		final int sizeBeforeDeleteInStateCreated = importedMoneyflows.size();

		super.callUsecaseWithoutContent("/" + ImportedMoneyflowTransportBuilder.IMPORTED_MONEYFLOW1_ID, this.method,
				true, Object.class);

		importedMoneyflows = this.importedMoneyflowService.getAllImportedMoneyflowsByCapitalsourceIds(userId,
				capitalsourceIds, ImportedMoneyflowStatus.CREATED);

		Assertions.assertNotNull(importedMoneyflows);
		Assertions.assertEquals(sizeBeforeDeleteInStateCreated - 1, importedMoneyflows.size());

		// No delete happend - it is only marked as "ignored"
		importedMoneyflows = this.importedMoneyflowService.getAllImportedMoneyflowsByCapitalsourceIds(userId,
				capitalsourceIds, null);

		Assertions.assertNotNull(importedMoneyflows);
		Assertions.assertEquals(sizeBeforeDelete, importedMoneyflows.size());
	}

	@Test
	public void test_AuthorizationRequired_Error() throws Exception {
		this.userName = null;
		this.userPassword = null;
		final ErrorResponse actual = super.callUsecaseWithoutContent("/1", this.method, false, ErrorResponse.class);
		Assertions.assertEquals(super.accessDeniedErrorResponse(), actual);
	}

	@Test
	@Sql("classpath:h2defaults.sql")
	public void test_emptyDatabase_noException() throws Exception {
		this.userName = UserTransportBuilder.ADMIN_NAME;
		this.userPassword = UserTransportBuilder.ADMIN_PASSWORD;
		super.callUsecaseWithoutContent("/" + ImportedMoneyflowTransportBuilder.IMPORTED_MONEYFLOW1_ID, this.method,
				true, Object.class);
	}

}