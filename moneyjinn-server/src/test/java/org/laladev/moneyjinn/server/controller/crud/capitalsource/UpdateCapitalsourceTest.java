
package org.laladev.moneyjinn.server.controller.crud.capitalsource;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.model.access.GroupID;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.capitalsource.Capitalsource;
import org.laladev.moneyjinn.model.capitalsource.CapitalsourceID;
import org.laladev.moneyjinn.model.capitalsource.CapitalsourceImport;
import org.laladev.moneyjinn.server.builder.CapitalsourceTransportBuilder;
import org.laladev.moneyjinn.server.builder.GroupTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.builder.ValidationItemTransportBuilder;
import org.laladev.moneyjinn.server.model.CapitalsourceTransport;
import org.laladev.moneyjinn.server.model.ValidationItemTransport;
import org.laladev.moneyjinn.server.model.ValidationResponse;
import org.laladev.moneyjinn.service.api.ICapitalsourceService;
import org.springframework.test.context.jdbc.Sql;

import jakarta.inject.Inject;

class UpdateCapitalsourceTest extends AbstractCapitalsourceTest {
	@Inject
	private ICapitalsourceService capitalsourceService;

	@Override
	protected void loadMethod() {
		super.getMock().update(null, null);
	}

	private void testError(final CapitalsourceTransport transport, final ErrorCode errorCode) throws Exception {
		final List<ValidationItemTransport> validationItems = new ArrayList<>();
		validationItems.add(new ValidationItemTransportBuilder().withKey(transport.getId().toString())
				.withError(errorCode.getErrorCode()).build());
		final ValidationResponse expected = new ValidationResponse();
		expected.setValidationItemTransports(validationItems);
		expected.setResult(Boolean.FALSE);

		final ValidationResponse actual = super.callUsecaseExpect422(transport, ValidationResponse.class);

		Assertions.assertEquals(expected, actual);
	}

	@Test
	void test_CapitalsourcenameAlreadyExisting_Error() throws Exception {
		final CapitalsourceTransport transport = new CapitalsourceTransportBuilder().forCapitalsource2().build();
		transport.setComment(CapitalsourceTransportBuilder.CAPITALSOURCE1_COMMENT);
		this.testError(transport, ErrorCode.NAME_ALREADY_EXISTS);
	}

	@Test
	void test_EmptyCapitalsourcename_Error() throws Exception {
		final CapitalsourceTransport transport = new CapitalsourceTransportBuilder().forCapitalsource2().build();
		transport.setComment("");
		this.testError(transport, ErrorCode.COMMENT_IS_NOT_SET);
	}

	@Test
	void test_ValidTilBeforeValidFrom_Error() throws Exception {
		final CapitalsourceTransport transport = new CapitalsourceTransportBuilder().forCapitalsource2().build();
		transport.setValidTil(LocalDate.parse("2000-01-01"));
		transport.setValidFrom(LocalDate.parse("2010-01-01"));
		this.testError(transport, ErrorCode.VALIDFROM_AFTER_VALIDTIL);
	}

	@Test
	void test_ValidityPeriodOutOfUsage_Error() throws Exception {
		final CapitalsourceTransport transport = new CapitalsourceTransportBuilder().forCapitalsource1().build();
		transport.setValidFrom(LocalDate.parse("2010-01-01"));
		this.testError(transport, ErrorCode.CAPITALSOURCE_IN_USE_PERIOD);
	}

	@Test
	void test_standardRequest_Successfull_DefaultReturn() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final GroupID groupId = new GroupID(GroupTransportBuilder.GROUP1_ID);
		final CapitalsourceID capitalsourceId = new CapitalsourceID(CapitalsourceTransportBuilder.CAPITALSOURCE1_ID);
		final CapitalsourceTransport transport = new CapitalsourceTransportBuilder().forCapitalsource1().build();
		transport.setComment("hugo");

		super.callUsecaseExpect204(transport);

		final Capitalsource capitalsource = this.capitalsourceService.getCapitalsourceById(userId, groupId,
				capitalsourceId);
		Assertions.assertEquals(CapitalsourceTransportBuilder.CAPITALSOURCE1_ID, capitalsource.getId().getId());
		Assertions.assertEquals("hugo", capitalsource.getComment());
	}

	@Test
	void test_standardRequest_Successfull_MinimalReturn() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final GroupID groupId = new GroupID(GroupTransportBuilder.GROUP1_ID);
		final CapitalsourceID capitalsourceId = new CapitalsourceID(CapitalsourceTransportBuilder.CAPITALSOURCE1_ID);
		final CapitalsourceTransport transport = new CapitalsourceTransportBuilder().forCapitalsource1().build();
		transport.setComment("hugo");

		super.callUsecaseExpect204Minimal(transport);

		final Capitalsource capitalsource = this.capitalsourceService.getCapitalsourceById(userId, groupId,
				capitalsourceId);
		Assertions.assertEquals(CapitalsourceTransportBuilder.CAPITALSOURCE1_ID, capitalsource.getId().getId());
		Assertions.assertEquals("hugo", capitalsource.getComment());
	}

	@Test
	void test_standardRequest_Successfull_RepresentationReturn() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final GroupID groupId = new GroupID(GroupTransportBuilder.GROUP1_ID);
		final CapitalsourceID capitalsourceId = new CapitalsourceID(CapitalsourceTransportBuilder.CAPITALSOURCE1_ID);
		final CapitalsourceTransport transport = new CapitalsourceTransportBuilder().forCapitalsource1().build();
		transport.setComment("hugo");

		final CapitalsourceTransport actual = super.callUsecaseExpect200Representation(transport,
				CapitalsourceTransport.class);

		Assertions.assertEquals(transport, actual);

		final Capitalsource capitalsource = this.capitalsourceService.getCapitalsourceById(userId, groupId,
				capitalsourceId);
		Assertions.assertEquals(CapitalsourceTransportBuilder.CAPITALSOURCE1_ID, capitalsource.getId().getId());
		Assertions.assertEquals("hugo", capitalsource.getComment());
	}

	@Test
	void test_standardRequestWithCredit_SuccessfullNoContent() throws Exception {
		super.setUsername(UserTransportBuilder.USER3_NAME);
		super.setPassword(UserTransportBuilder.USER3_PASSWORD);
		final UserID userId = new UserID(UserTransportBuilder.USER3_ID);
		final GroupID groupId = new GroupID(GroupTransportBuilder.GROUP1_ID);
		final CapitalsourceID capitalsourceId = new CapitalsourceID(CapitalsourceTransportBuilder.CAPITALSOURCE5_ID);
		final CapitalsourceTransport transport = new CapitalsourceTransportBuilder().forCapitalsource5().build();
		transport.setComment("hugo");

		super.callUsecaseExpect204(transport);

		final Capitalsource capitalsource = this.capitalsourceService.getCapitalsourceById(userId, groupId,
				capitalsourceId);
		Assertions.assertEquals(CapitalsourceTransportBuilder.CAPITALSOURCE5_ID, capitalsource.getId().getId());
		Assertions.assertEquals("hugo", capitalsource.getComment());
	}

	@Test
	void test_Bic8Digits_fillesUpTo11Digits() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final GroupID groupId = new GroupID(GroupTransportBuilder.GROUP1_ID);
		final CapitalsourceID capitalsourceId = new CapitalsourceID(CapitalsourceTransportBuilder.CAPITALSOURCE1_ID);
		final CapitalsourceTransport transport = new CapitalsourceTransportBuilder().forCapitalsource1().build();
		transport.setComment("hugo");
		transport.setBankCode("ABCDEFGH");

		super.callUsecaseExpect204(transport);

		final Capitalsource capitalsource = this.capitalsourceService.getCapitalsourceById(userId, groupId,
				capitalsourceId);
		Assertions.assertEquals(CapitalsourceTransportBuilder.CAPITALSOURCE1_ID, capitalsource.getId().getId());
		Assertions.assertEquals("hugo", capitalsource.getComment());
		Assertions.assertEquals(transport.getBankCode() + "XXX", capitalsource.getBankAccount().getBankCode());
	}

	@Test
	void test_setImportAllowedAll_Successfull() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final GroupID groupId = new GroupID(GroupTransportBuilder.GROUP1_ID);
		final CapitalsourceID capitalsourceId = new CapitalsourceID(CapitalsourceTransportBuilder.CAPITALSOURCE2_ID);
		final CapitalsourceTransport transport = new CapitalsourceTransportBuilder().forCapitalsource2().build();
		transport.setImportAllowed(1);

		super.callUsecaseExpect204(transport);

		final Capitalsource capitalsource = this.capitalsourceService.getCapitalsourceById(userId, groupId,
				capitalsourceId);
		Assertions.assertEquals(CapitalsourceTransportBuilder.CAPITALSOURCE2_ID, capitalsource.getId().getId());
		Assertions.assertEquals(CapitalsourceImport.ALL_ALLOWED, capitalsource.getImportAllowed());
	}

	@Test
	void test_setImportAllowedOnlyBalance_Successfull() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final GroupID groupId = new GroupID(GroupTransportBuilder.GROUP1_ID);
		final CapitalsourceID capitalsourceId = new CapitalsourceID(CapitalsourceTransportBuilder.CAPITALSOURCE2_ID);
		final CapitalsourceTransport transport = new CapitalsourceTransportBuilder().forCapitalsource2().build();
		transport.setImportAllowed(2);

		super.callUsecaseExpect204(transport);

		final Capitalsource capitalsource = this.capitalsourceService.getCapitalsourceById(userId, groupId,
				capitalsourceId);
		Assertions.assertEquals(CapitalsourceTransportBuilder.CAPITALSOURCE2_ID, capitalsource.getId().getId());
		Assertions.assertEquals(CapitalsourceImport.BALANCE_ALLOWED, capitalsource.getImportAllowed());
	}

	@Test
	void test_setImportAllowedNotAllowed_Successfull() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final GroupID groupId = new GroupID(GroupTransportBuilder.GROUP1_ID);
		final CapitalsourceID capitalsourceId = new CapitalsourceID(CapitalsourceTransportBuilder.CAPITALSOURCE1_ID);
		final CapitalsourceTransport transport = new CapitalsourceTransportBuilder().forCapitalsource1().build();
		transport.setImportAllowed(0);

		super.callUsecaseExpect204(transport);

		final Capitalsource capitalsource = this.capitalsourceService.getCapitalsourceById(userId, groupId,
				capitalsourceId);
		Assertions.assertEquals(CapitalsourceTransportBuilder.CAPITALSOURCE1_ID, capitalsource.getId().getId());
		Assertions.assertEquals(CapitalsourceImport.NOT_ALLOWED, capitalsource.getImportAllowed());
	}

	@Test
	void test_ProvisionAsset_Successfull() throws Exception {
		super.setUsername(UserTransportBuilder.USER3_NAME);
		super.setPassword(UserTransportBuilder.USER3_PASSWORD);
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final GroupID groupId = new GroupID(GroupTransportBuilder.GROUP1_ID);
		final CapitalsourceID capitalsourceId = new CapitalsourceID(CapitalsourceTransportBuilder.CAPITALSOURCE4_ID);
		final CapitalsourceTransport transport = new CapitalsourceTransportBuilder().forCapitalsource4().build();
		transport.setComment("hugo");

		super.callUsecaseExpect204(transport);

		final Capitalsource capitalsource = this.capitalsourceService.getCapitalsourceById(userId, groupId,
				capitalsourceId);
		Assertions.assertEquals(CapitalsourceTransportBuilder.CAPITALSOURCE4_ID, capitalsource.getId().getId());
		Assertions.assertEquals("hugo", capitalsource.getComment());
	}

	@Test
	void test_LongTermAsset_Successfull() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final GroupID groupId = new GroupID(GroupTransportBuilder.GROUP1_ID);
		final CapitalsourceID capitalsourceId = new CapitalsourceID(CapitalsourceTransportBuilder.CAPITALSOURCE2_ID);
		final CapitalsourceTransport transport = new CapitalsourceTransportBuilder().forCapitalsource2().build();
		transport.setComment("hugo");

		super.callUsecaseExpect204(transport);

		final Capitalsource capitalsource = this.capitalsourceService.getCapitalsourceById(userId, groupId,
				capitalsourceId);
		Assertions.assertEquals(CapitalsourceTransportBuilder.CAPITALSOURCE2_ID, capitalsource.getId().getId());
		Assertions.assertEquals("hugo", capitalsource.getComment());
	}

	@Test
	void test_editCapitalsourceOwnedBySomeoneElse_notSuccessfull() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final GroupID groupId = new GroupID(GroupTransportBuilder.GROUP1_ID);
		final CapitalsourceID capitalsourceId = new CapitalsourceID(CapitalsourceTransportBuilder.CAPITALSOURCE3_ID);
		final CapitalsourceTransport transport = new CapitalsourceTransportBuilder().forCapitalsource1().build();
		transport.setComment("hugo");

		super.callUsecaseExpect204(transport);

		final Capitalsource capitalsource = this.capitalsourceService.getCapitalsourceById(userId, groupId,
				capitalsourceId);
		Assertions.assertEquals(CapitalsourceTransportBuilder.CAPITALSOURCE3_ID, capitalsource.getId().getId());
		Assertions.assertEquals(CapitalsourceTransportBuilder.CAPITALSOURCE3_COMMENT, capitalsource.getComment());
	}

	@Test
	void test_ImportRoleNotAllowed_ErrorResponse() throws Exception {
		super.setUsername(UserTransportBuilder.IMPORTUSER_NAME);
		super.setPassword(UserTransportBuilder.IMPORTUSER_PASSWORD);

		super.callUsecaseExpect403(new CapitalsourceTransport());
	}

	@Test
	void test_AuthorizationRequired_Error() throws Exception {
		super.setUsername(null);
		super.setPassword(null);

		super.callUsecaseExpect403(new CapitalsourceTransport());
	}

	@Test
	@Sql("classpath:h2defaults.sql")
	void test_emptyDatabase_noException() throws Exception {
		super.setUsername(UserTransportBuilder.ADMIN_NAME);
		super.setPassword(UserTransportBuilder.ADMIN_PASSWORD);
		final CapitalsourceTransport transport = new CapitalsourceTransportBuilder().forCapitalsource1().build();

		super.callUsecaseExpect204(transport);
	}
}