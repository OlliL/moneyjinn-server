
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
import org.laladev.moneyjinn.model.capitalsource.CapitalsourceState;
import org.laladev.moneyjinn.model.capitalsource.CapitalsourceType;
import org.laladev.moneyjinn.server.builder.CapitalsourceTransportBuilder;
import org.laladev.moneyjinn.server.builder.GroupTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.builder.ValidationItemTransportBuilder;
import org.laladev.moneyjinn.server.model.CapitalsourceTransport;
import org.laladev.moneyjinn.server.model.ValidationItemTransport;
import org.laladev.moneyjinn.server.model.ValidationResponse;
import org.laladev.moneyjinn.service.api.ICapitalsourceService;

import jakarta.inject.Inject;

class CreateCapitalsourceTest extends AbstractCapitalsourceTest {
	@Inject
	private ICapitalsourceService capitalsourceService;

	@Override
	protected void loadMethod() {
		super.getMock().create(null, null);
	}

	private void testError(final CapitalsourceTransport transport, final ErrorCode errorCode) throws Exception {
		final List<ValidationItemTransport> validationItems = new ArrayList<>();
		validationItems
				.add(new ValidationItemTransportBuilder().withKey(null).withError(errorCode.getErrorCode()).build());

		final ValidationResponse expected = new ValidationResponse();
		expected.setValidationItemTransports(validationItems);
		expected.setResult(Boolean.FALSE);

		final ValidationResponse actual = super.callUsecaseExpect422(transport, ValidationResponse.class);

		Assertions.assertEquals(expected, actual);
	}

	@Test
	void test_CapitalsourcenameAlreadyExisting_Error() throws Exception {
		final CapitalsourceTransport transport = new CapitalsourceTransportBuilder().forNewCapitalsource().build();
		transport.setComment(CapitalsourceTransportBuilder.CAPITALSOURCE1_COMMENT);
		this.testError(transport, ErrorCode.NAME_ALREADY_EXISTS);
	}

	@Test
	void test_emptyCapitalsourcename_Error() throws Exception {
		final CapitalsourceTransport transport = new CapitalsourceTransportBuilder().forNewCapitalsource().build();
		transport.setComment("");
		this.testError(transport, ErrorCode.COMMENT_IS_NOT_SET);
	}

	@Test
	void test_nullCapitalsourcename_Error() throws Exception {
		final CapitalsourceTransport transport = new CapitalsourceTransportBuilder().forNewCapitalsource().build();
		transport.setComment(null);
		this.testError(transport, ErrorCode.COMMENT_IS_NOT_SET);
	}

	@Test
	void test_ToLongAccountnumber_Error() throws Exception {
		final CapitalsourceTransport transport = new CapitalsourceTransportBuilder().forNewCapitalsource().build();
		transport.setAccountNumber("12345678901234567890123456789012345");
		this.testError(transport, ErrorCode.ACCOUNT_NUMBER_TO_LONG);
	}

	@Test
	void test_ToLongBankcode_Error() throws Exception {
		final CapitalsourceTransport transport = new CapitalsourceTransportBuilder().forNewCapitalsource().build();
		transport.setBankCode("123456789012");
		this.testError(transport, ErrorCode.BANK_CODE_TO_LONG);
	}

	@Test
	void test_AccountnumberInvalidChar_Error() throws Exception {
		final CapitalsourceTransport transport = new CapitalsourceTransportBuilder().forNewCapitalsource().build();
		transport.setAccountNumber("+");
		this.testError(transport, ErrorCode.ACCOUNT_NUMBER_CONTAINS_ILLEGAL_CHARS);
	}

	@Test
	void test_BankcodeInvalidChar_Error() throws Exception {
		final CapitalsourceTransport transport = new CapitalsourceTransportBuilder().forNewCapitalsource().build();
		transport.setBankCode("+");
		this.testError(transport, ErrorCode.BANK_CODE_CONTAINS_ILLEGAL_CHARS);
	}

	@Test
	void test_standardRequest_Successfull_MinimalReturn() throws Exception {
		final CapitalsourceTransport transport = new CapitalsourceTransportBuilder().forNewCapitalsource().build();

		super.callUsecaseExpect204Minimal(transport);

		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final GroupID groupId = new GroupID(GroupTransportBuilder.GROUP1_ID);
		final CapitalsourceID capitalsourceId = new CapitalsourceID(CapitalsourceTransportBuilder.NEXT_ID);
		final Capitalsource capitalsource = this.capitalsourceService.getCapitalsourceById(userId, groupId,
				capitalsourceId);
		Assertions.assertEquals(CapitalsourceTransportBuilder.NEXT_ID, capitalsource.getId().getId());
		Assertions.assertEquals(CapitalsourceTransportBuilder.NEWCAPITALSOURCE_COMMENT, capitalsource.getComment());
	}

	@Test
	void test_standardRequest_Successfull_RepresentationReturn() throws Exception {
		final CapitalsourceTransport transport = new CapitalsourceTransportBuilder().forNewCapitalsource().build();

		final CapitalsourceTransport actualTransport = super.callUsecaseExpect200Representation(transport,
				CapitalsourceTransport.class);

		Assertions.assertEquals(CapitalsourceTransportBuilder.NEXT_ID, actualTransport.getId());

		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final GroupID groupId = new GroupID(GroupTransportBuilder.GROUP1_ID);
		final CapitalsourceID capitalsourceId = new CapitalsourceID(CapitalsourceTransportBuilder.NEXT_ID);
		final Capitalsource capitalsource = this.capitalsourceService.getCapitalsourceById(userId, groupId,
				capitalsourceId);
		Assertions.assertEquals(CapitalsourceTransportBuilder.NEXT_ID, capitalsource.getId().getId());
		Assertions.assertEquals(CapitalsourceTransportBuilder.NEWCAPITALSOURCE_COMMENT, capitalsource.getComment());
	}

	@Test
	void test_standardRequest_Successfull_DefaultReturn() throws Exception {
		final CapitalsourceTransport transport = new CapitalsourceTransportBuilder().forNewCapitalsource().build();

		super.callUsecaseExpect204(transport);

		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final GroupID groupId = new GroupID(GroupTransportBuilder.GROUP1_ID);
		final CapitalsourceID capitalsourceId = new CapitalsourceID(CapitalsourceTransportBuilder.NEXT_ID);
		final Capitalsource capitalsource = this.capitalsourceService.getCapitalsourceById(userId, groupId,
				capitalsourceId);
		Assertions.assertEquals(CapitalsourceTransportBuilder.NEXT_ID, capitalsource.getId().getId());
		Assertions.assertEquals(CapitalsourceTransportBuilder.NEWCAPITALSOURCE_COMMENT, capitalsource.getComment());
	}

	@Test
	void test_Bic8Digits_fillesUpTo11Digits() throws Exception {
		final CapitalsourceTransport transport = new CapitalsourceTransportBuilder().forNewCapitalsource().build();
		transport.setBankCode("ABCDEFGH");

		super.callUsecaseExpect204(transport);

		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final GroupID groupId = new GroupID(GroupTransportBuilder.GROUP1_ID);
		final CapitalsourceID capitalsourceId = new CapitalsourceID(CapitalsourceTransportBuilder.NEXT_ID);
		final Capitalsource capitalsource = this.capitalsourceService.getCapitalsourceById(userId, groupId,
				capitalsourceId);
		Assertions.assertEquals(CapitalsourceTransportBuilder.NEXT_ID, capitalsource.getId().getId());
		Assertions.assertEquals(CapitalsourceTransportBuilder.NEWCAPITALSOURCE_COMMENT, capitalsource.getComment());
		Assertions.assertEquals(transport.getBankCode() + "XXX", capitalsource.getBankAccount().getBankCode());
	}

	@Test
	void test_differentUserIdSet_ButIgnoredAndAlwaysCreatedWithOwnUserId() throws Exception {
		final CapitalsourceTransport transport = new CapitalsourceTransportBuilder().forNewCapitalsource().build();
		transport.setUserid(UserTransportBuilder.ADMIN_ID);

		super.callUsecaseExpect204(transport);

		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final GroupID groupId = new GroupID(GroupTransportBuilder.GROUP1_ID);
		final CapitalsourceID capitalsourceId = new CapitalsourceID(CapitalsourceTransportBuilder.NEXT_ID);
		final Capitalsource capitalsource = this.capitalsourceService.getCapitalsourceById(userId, groupId,
				capitalsourceId);
		Assertions.assertEquals(CapitalsourceTransportBuilder.NEXT_ID, capitalsource.getId().getId());
		Assertions.assertEquals(CapitalsourceTransportBuilder.NEWCAPITALSOURCE_COMMENT, capitalsource.getComment());
	}

	@Test
	void test_checkDefaults_Successfull() throws Exception {
		final CapitalsourceTransport transport = new CapitalsourceTransportBuilder().forNewCapitalsource().build();
		transport.setValidFrom(null);
		transport.setValidTil(null);
		transport.setState(null);
		transport.setType(null);
		transport.setAccountNumber(null);
		transport.setBankCode(null);

		super.callUsecaseExpect204(transport);

		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final GroupID groupId = new GroupID(GroupTransportBuilder.GROUP1_ID);
		final CapitalsourceID capitalsourceId = new CapitalsourceID(CapitalsourceTransportBuilder.NEXT_ID);
		final Capitalsource capitalsource = this.capitalsourceService.getCapitalsourceById(userId, groupId,
				capitalsourceId);
		Assertions.assertEquals(CapitalsourceTransportBuilder.NEXT_ID, capitalsource.getId().getId());
		Assertions.assertEquals(CapitalsourceTransportBuilder.NEWCAPITALSOURCE_COMMENT, capitalsource.getComment());
		Assertions.assertEquals(CapitalsourceState.CASH, capitalsource.getState());
		Assertions.assertEquals(CapitalsourceType.CURRENT_ASSET, capitalsource.getType());
		Assertions.assertEquals(LocalDate.now(), capitalsource.getValidFrom());
		Assertions.assertEquals(LocalDate.parse("2999-12-31"), capitalsource.getValidTil());
		Assertions.assertNull(capitalsource.getBankAccount());
	}

	@Override
	protected void callUsecaseExpect403ForThisUsecase() throws Exception {
		super.callUsecaseExpect403(new CapitalsourceTransport());
	}

	@Override
	protected void callUsecaseEmptyDatabase() throws Exception {
		final CapitalsourceTransport transport = new CapitalsourceTransportBuilder().forNewCapitalsource().build();

		super.callUsecaseExpect204(transport);

		final UserID userId = new UserID(UserTransportBuilder.ADMIN_ID);
		final GroupID groupId = new GroupID(GroupTransportBuilder.ADMINGROUP_ID);
		final CapitalsourceID capitalsourceId = new CapitalsourceID(1l);
		final Capitalsource capitalsource = this.capitalsourceService.getCapitalsourceById(userId, groupId,
				capitalsourceId);
		Assertions.assertEquals(Long.valueOf(1l), capitalsource.getId().getId());
		Assertions.assertEquals(CapitalsourceTransportBuilder.NEWCAPITALSOURCE_COMMENT, capitalsource.getComment());
	}
}