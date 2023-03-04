
package org.laladev.moneyjinn.server.controller.capitalsource;

import jakarta.inject.Inject;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.laladev.moneyjinn.server.controller.api.CapitalsourceControllerApi;
import org.laladev.moneyjinn.server.model.CapitalsourceTransport;
import org.laladev.moneyjinn.server.model.CreateCapitalsourceRequest;
import org.laladev.moneyjinn.server.model.CreateCapitalsourceResponse;
import org.laladev.moneyjinn.server.model.ValidationItemTransport;
import org.laladev.moneyjinn.server.model.ValidationResponse;
import org.laladev.moneyjinn.service.api.ICapitalsourceService;
import org.springframework.test.context.jdbc.Sql;

public class CreateCapitalsourceTest extends AbstractControllerTest {
  @Inject
  private ICapitalsourceService capitalsourceService;

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
  protected Method getMethod() {
    return super.getMethodFromTestClassName(CapitalsourceControllerApi.class, this.getClass());
  }

  private void testError(final CapitalsourceTransport transport, final ErrorCode errorCode)
      throws Exception {
    final CreateCapitalsourceRequest request = new CreateCapitalsourceRequest();
    request.setCapitalsourceTransport(transport);
    final List<ValidationItemTransport> validationItems = new ArrayList<>();
    validationItems.add(new ValidationItemTransportBuilder().withKey(null)
        .withError(errorCode.getErrorCode()).build());
    final ValidationResponse expected = new ValidationResponse();
    expected.setValidationItemTransports(validationItems);
    expected.setResult(Boolean.FALSE);

    final ValidationResponse actual = super.callUsecaseExpect422(request, ValidationResponse.class);

    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_CapitalsourcenameAlreadyExisting_Error() throws Exception {
    final CapitalsourceTransport transport = new CapitalsourceTransportBuilder()
        .forNewCapitalsource().build();
    transport.setComment(CapitalsourceTransportBuilder.CAPITALSOURCE1_COMMENT);
    this.testError(transport, ErrorCode.NAME_ALREADY_EXISTS);
  }

  @Test
  public void test_emptyCapitalsourcename_Error() throws Exception {
    final CapitalsourceTransport transport = new CapitalsourceTransportBuilder()
        .forNewCapitalsource().build();
    transport.setComment("");
    this.testError(transport, ErrorCode.COMMENT_IS_NOT_SET);
  }

  @Test
  public void test_nullCapitalsourcename_Error() throws Exception {
    final CapitalsourceTransport transport = new CapitalsourceTransportBuilder()
        .forNewCapitalsource().build();
    transport.setComment(null);
    this.testError(transport, ErrorCode.COMMENT_IS_NOT_SET);
  }

  @Test
  public void test_ToLongAccountnumber_Error() throws Exception {
    final CapitalsourceTransport transport = new CapitalsourceTransportBuilder()
        .forNewCapitalsource().build();
    transport.setAccountNumber("12345678901234567890123456789012345");
    this.testError(transport, ErrorCode.ACCOUNT_NUMBER_TO_LONG);
  }

  @Test
  public void test_ToLongBankcode_Error() throws Exception {
    final CapitalsourceTransport transport = new CapitalsourceTransportBuilder()
        .forNewCapitalsource().build();
    transport.setBankCode("123456789012");
    this.testError(transport, ErrorCode.BANK_CODE_TO_LONG);
  }

  @Test
  public void test_AccountnumberInvalidChar_Error() throws Exception {
    final CapitalsourceTransport transport = new CapitalsourceTransportBuilder()
        .forNewCapitalsource().build();
    transport.setAccountNumber("+");
    this.testError(transport, ErrorCode.ACCOUNT_NUMBER_CONTAINS_ILLEGAL_CHARS);
  }

  @Test
  public void test_BankcodeInvalidChar_Error() throws Exception {
    final CapitalsourceTransport transport = new CapitalsourceTransportBuilder()
        .forNewCapitalsource().build();
    transport.setBankCode("+");
    this.testError(transport, ErrorCode.BANK_CODE_CONTAINS_ILLEGAL_CHARS);
  }

  @Test
  public void test_standardRequest_Successfull() throws Exception {
    final CreateCapitalsourceRequest request = new CreateCapitalsourceRequest();
    final CapitalsourceTransport transport = new CapitalsourceTransportBuilder()
        .forNewCapitalsource().build();
    request.setCapitalsourceTransport(transport);
    final CreateCapitalsourceResponse expected = new CreateCapitalsourceResponse();
    expected.setCapitalsourceId(CapitalsourceTransportBuilder.NEXT_ID);

    final CreateCapitalsourceResponse actual = super.callUsecaseExpect200(request,
        CreateCapitalsourceResponse.class);

    Assertions.assertEquals(expected, actual);
    final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
    final GroupID groupId = new GroupID(GroupTransportBuilder.GROUP1_ID);
    final CapitalsourceID capitalsourceId = new CapitalsourceID(
        CapitalsourceTransportBuilder.NEXT_ID);
    final Capitalsource capitalsource = this.capitalsourceService.getCapitalsourceById(userId,
        groupId, capitalsourceId);
    Assertions.assertEquals(CapitalsourceTransportBuilder.NEXT_ID, capitalsource.getId().getId());
    Assertions.assertEquals(CapitalsourceTransportBuilder.NEWCAPITALSOURCE_COMMENT,
        capitalsource.getComment());
  }

  @Test
  public void test_Bic8Digits_fillesUpTo11Digits() throws Exception {
    final CreateCapitalsourceRequest request = new CreateCapitalsourceRequest();
    final CapitalsourceTransport transport = new CapitalsourceTransportBuilder()
        .forNewCapitalsource().build();
    transport.setBankCode("ABCDEFGH");
    request.setCapitalsourceTransport(transport);
    final CreateCapitalsourceResponse expected = new CreateCapitalsourceResponse();
    expected.setCapitalsourceId(CapitalsourceTransportBuilder.NEXT_ID);

    final CreateCapitalsourceResponse actual = super.callUsecaseExpect200(request,
        CreateCapitalsourceResponse.class);

    Assertions.assertEquals(expected, actual);
    final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
    final GroupID groupId = new GroupID(GroupTransportBuilder.GROUP1_ID);
    final CapitalsourceID capitalsourceId = new CapitalsourceID(
        CapitalsourceTransportBuilder.NEXT_ID);
    final Capitalsource capitalsource = this.capitalsourceService.getCapitalsourceById(userId,
        groupId, capitalsourceId);
    Assertions.assertEquals(CapitalsourceTransportBuilder.NEXT_ID, capitalsource.getId().getId());
    Assertions.assertEquals(CapitalsourceTransportBuilder.NEWCAPITALSOURCE_COMMENT,
        capitalsource.getComment());
    Assertions.assertEquals(transport.getBankCode() + "XXX",
        capitalsource.getBankAccount().getBankCode());
  }

  @Test
  public void test_differentUserIdSet_ButIgnoredAndAlwaysCreatedWithOwnUserId() throws Exception {
    final CreateCapitalsourceRequest request = new CreateCapitalsourceRequest();
    final CapitalsourceTransport transport = new CapitalsourceTransportBuilder()
        .forNewCapitalsource().build();
    transport.setUserid(UserTransportBuilder.ADMIN_ID);
    request.setCapitalsourceTransport(transport);
    final CreateCapitalsourceResponse expected = new CreateCapitalsourceResponse();
    expected.setCapitalsourceId(CapitalsourceTransportBuilder.NEXT_ID);

    final CreateCapitalsourceResponse actual = super.callUsecaseExpect200(request,
        CreateCapitalsourceResponse.class);

    Assertions.assertEquals(expected, actual);
    final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
    final GroupID groupId = new GroupID(GroupTransportBuilder.GROUP1_ID);
    final CapitalsourceID capitalsourceId = new CapitalsourceID(
        CapitalsourceTransportBuilder.NEXT_ID);
    final Capitalsource capitalsource = this.capitalsourceService.getCapitalsourceById(userId,
        groupId, capitalsourceId);
    Assertions.assertEquals(CapitalsourceTransportBuilder.NEXT_ID, capitalsource.getId().getId());
    Assertions.assertEquals(CapitalsourceTransportBuilder.NEWCAPITALSOURCE_COMMENT,
        capitalsource.getComment());
  }

  @Test
  public void test_checkDefaults_Successfull() throws Exception {
    final CreateCapitalsourceRequest request = new CreateCapitalsourceRequest();
    final CapitalsourceTransport transport = new CapitalsourceTransportBuilder()
        .forNewCapitalsource().build();
    transport.setValidFrom(null);
    transport.setValidTil(null);
    transport.setState(null);
    transport.setType(null);
    transport.setAccountNumber(null);
    transport.setBankCode(null);
    request.setCapitalsourceTransport(transport);
    final CreateCapitalsourceResponse expected = new CreateCapitalsourceResponse();
    expected.setCapitalsourceId(CapitalsourceTransportBuilder.NEXT_ID);

    final CreateCapitalsourceResponse actual = super.callUsecaseExpect200(request,
        CreateCapitalsourceResponse.class);

    Assertions.assertEquals(expected, actual);
    final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
    final GroupID groupId = new GroupID(GroupTransportBuilder.GROUP1_ID);
    final CapitalsourceID capitalsourceId = new CapitalsourceID(
        CapitalsourceTransportBuilder.NEXT_ID);
    final Capitalsource capitalsource = this.capitalsourceService.getCapitalsourceById(userId,
        groupId, capitalsourceId);
    Assertions.assertEquals(CapitalsourceTransportBuilder.NEXT_ID, capitalsource.getId().getId());
    Assertions.assertEquals(CapitalsourceTransportBuilder.NEWCAPITALSOURCE_COMMENT,
        capitalsource.getComment());
    Assertions.assertEquals(CapitalsourceState.CACHE, capitalsource.getState());
    Assertions.assertEquals(CapitalsourceType.CURRENT_ASSET, capitalsource.getType());
    Assertions.assertEquals(LocalDate.now(), capitalsource.getValidFrom());
    Assertions.assertEquals(LocalDate.parse("2999-12-31"), capitalsource.getValidTil());
    Assertions.assertNull(capitalsource.getBankAccount());
  }

  @Test
  public void test_AuthorizationRequired_Error() throws Exception {
    this.userName = null;
    this.userPassword = null;

    super.callUsecaseExpect403(new CreateCapitalsourceRequest());
  }

  @Test
  @Sql("classpath:h2defaults.sql")
  public void test_emptyDatabase_noException() throws Exception {
    this.userName = UserTransportBuilder.ADMIN_NAME;
    this.userPassword = UserTransportBuilder.ADMIN_PASSWORD;
    final CreateCapitalsourceRequest request = new CreateCapitalsourceRequest();
    final CapitalsourceTransport transport = new CapitalsourceTransportBuilder()
        .forNewCapitalsource().build();
    request.setCapitalsourceTransport(transport);
    final CreateCapitalsourceResponse expected = new CreateCapitalsourceResponse();
    expected.setCapitalsourceId(1L);

    final CreateCapitalsourceResponse actual = super.callUsecaseExpect200(request,
        CreateCapitalsourceResponse.class);

    Assertions.assertEquals(expected, actual);
    final UserID userId = new UserID(UserTransportBuilder.ADMIN_ID);
    final GroupID groupId = new GroupID(GroupTransportBuilder.ADMINGROUP_ID);
    final CapitalsourceID capitalsourceId = new CapitalsourceID(1l);
    final Capitalsource capitalsource = this.capitalsourceService.getCapitalsourceById(userId,
        groupId, capitalsourceId);
    Assertions.assertEquals(Long.valueOf(1l), capitalsource.getId().getId());
    Assertions.assertEquals(CapitalsourceTransportBuilder.NEWCAPITALSOURCE_COMMENT,
        capitalsource.getComment());
  }
}