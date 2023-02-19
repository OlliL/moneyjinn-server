
package org.laladev.moneyjinn.server.controller.capitalsource;

import jakarta.inject.Inject;
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
import org.laladev.moneyjinn.model.capitalsource.CapitalsourceImport;
import org.laladev.moneyjinn.server.builder.CapitalsourceTransportBuilder;
import org.laladev.moneyjinn.server.builder.GroupTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.builder.ValidationItemTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.laladev.moneyjinn.server.model.CapitalsourceTransport;
import org.laladev.moneyjinn.server.model.UpdateCapitalsourceRequest;
import org.laladev.moneyjinn.server.model.ValidationItemTransport;
import org.laladev.moneyjinn.server.model.ValidationResponse;
import org.laladev.moneyjinn.service.api.ICapitalsourceService;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.jdbc.Sql;

public class UpdateCapitalsourceTest extends AbstractControllerTest {
  @Inject
  private ICapitalsourceService capitalsourceService;
  private final HttpMethod method = HttpMethod.PUT;
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

  private void testError(final CapitalsourceTransport transport, final ErrorCode errorCode)
      throws Exception {
    final UpdateCapitalsourceRequest request = new UpdateCapitalsourceRequest();
    request.setCapitalsourceTransport(transport);
    final List<ValidationItemTransport> validationItems = new ArrayList<>();
    validationItems.add(new ValidationItemTransportBuilder().withKey(transport.getId().toString())
        .withError(errorCode.getErrorCode()).build());
    final ValidationResponse expected = new ValidationResponse();
    expected.setValidationItemTransports(validationItems);
    expected.setResult(Boolean.FALSE);
    final ValidationResponse actual = super.callUsecaseExpect422(this.method, request,
        ValidationResponse.class);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_CapitalsourcenameAlreadyExisting_Error() throws Exception {
    final CapitalsourceTransport transport = new CapitalsourceTransportBuilder().forCapitalsource2()
        .build();
    transport.setComment(CapitalsourceTransportBuilder.CAPITALSOURCE1_COMMENT);
    this.testError(transport, ErrorCode.NAME_ALREADY_EXISTS);
  }

  @Test
  public void test_EmptyCapitalsourcename_Error() throws Exception {
    final CapitalsourceTransport transport = new CapitalsourceTransportBuilder().forCapitalsource2()
        .build();
    transport.setComment("");
    this.testError(transport, ErrorCode.COMMENT_IS_NOT_SET);
  }

  @Test
  public void test_ValidTilBeforeValidFrom_Error() throws Exception {
    final CapitalsourceTransport transport = new CapitalsourceTransportBuilder().forCapitalsource2()
        .build();
    transport.setValidTil(LocalDate.parse("2000-01-01"));
    transport.setValidFrom(LocalDate.parse("2010-01-01"));
    this.testError(transport, ErrorCode.VALIDFROM_AFTER_VALIDTIL);
  }

  @Test
  public void test_ValidityPeriodOutOfUsage_Error() throws Exception {
    final CapitalsourceTransport transport = new CapitalsourceTransportBuilder().forCapitalsource1()
        .build();
    transport.setValidFrom(LocalDate.parse("2010-01-01"));
    this.testError(transport, ErrorCode.CAPITALSOURCE_IN_USE_PERIOD);
  }

  @Test
  public void test_standardRequest_Successfull() throws Exception {
    final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
    final GroupID groupId = new GroupID(GroupTransportBuilder.GROUP1_ID);
    final CapitalsourceID capitalsourceId = new CapitalsourceID(
        CapitalsourceTransportBuilder.CAPITALSOURCE1_ID);
    final UpdateCapitalsourceRequest request = new UpdateCapitalsourceRequest();
    final CapitalsourceTransport transport = new CapitalsourceTransportBuilder().forCapitalsource1()
        .build();
    transport.setComment("hugo");
    request.setCapitalsourceTransport(transport);

    super.callUsecaseExpect204(this.method, request);

    final Capitalsource capitalsource = this.capitalsourceService.getCapitalsourceById(userId,
        groupId, capitalsourceId);
    Assertions.assertEquals(CapitalsourceTransportBuilder.CAPITALSOURCE1_ID,
        capitalsource.getId().getId());
    Assertions.assertEquals("hugo", capitalsource.getComment());
  }

  @Test
  public void test_standardRequestWithCredit_SuccessfullNoContent() throws Exception {
    this.userName = UserTransportBuilder.USER3_NAME;
    this.userPassword = UserTransportBuilder.USER3_PASSWORD;
    final UserID userId = new UserID(UserTransportBuilder.USER3_ID);
    final GroupID groupId = new GroupID(GroupTransportBuilder.GROUP1_ID);
    final CapitalsourceID capitalsourceId = new CapitalsourceID(
        CapitalsourceTransportBuilder.CAPITALSOURCE5_ID);
    final UpdateCapitalsourceRequest request = new UpdateCapitalsourceRequest();
    final CapitalsourceTransport transport = new CapitalsourceTransportBuilder().forCapitalsource5()
        .build();
    transport.setComment("hugo");
    request.setCapitalsourceTransport(transport);

    super.callUsecaseExpect204(this.method, request);

    final Capitalsource capitalsource = this.capitalsourceService.getCapitalsourceById(userId,
        groupId, capitalsourceId);
    Assertions.assertEquals(CapitalsourceTransportBuilder.CAPITALSOURCE5_ID,
        capitalsource.getId().getId());
    Assertions.assertEquals("hugo", capitalsource.getComment());
  }

  @Test
  public void test_Bic8Digits_fillesUpTo11Digits() throws Exception {
    final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
    final GroupID groupId = new GroupID(GroupTransportBuilder.GROUP1_ID);
    final CapitalsourceID capitalsourceId = new CapitalsourceID(
        CapitalsourceTransportBuilder.CAPITALSOURCE1_ID);
    final UpdateCapitalsourceRequest request = new UpdateCapitalsourceRequest();
    final CapitalsourceTransport transport = new CapitalsourceTransportBuilder().forCapitalsource1()
        .build();
    transport.setComment("hugo");
    transport.setBankCode("ABCDEFGH");
    request.setCapitalsourceTransport(transport);

    super.callUsecaseExpect204(this.method, request);

    final Capitalsource capitalsource = this.capitalsourceService.getCapitalsourceById(userId,
        groupId, capitalsourceId);
    Assertions.assertEquals(CapitalsourceTransportBuilder.CAPITALSOURCE1_ID,
        capitalsource.getId().getId());
    Assertions.assertEquals("hugo", capitalsource.getComment());
    Assertions.assertEquals(transport.getBankCode() + "XXX",
        capitalsource.getBankAccount().getBankCode());
  }

  @Test
  public void test_setImportAllowedAll_Successfull() throws Exception {
    final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
    final GroupID groupId = new GroupID(GroupTransportBuilder.GROUP1_ID);
    final CapitalsourceID capitalsourceId = new CapitalsourceID(
        CapitalsourceTransportBuilder.CAPITALSOURCE2_ID);
    final UpdateCapitalsourceRequest request = new UpdateCapitalsourceRequest();
    final CapitalsourceTransport transport = new CapitalsourceTransportBuilder().forCapitalsource2()
        .build();
    transport.setImportAllowed(1);
    request.setCapitalsourceTransport(transport);

    super.callUsecaseExpect204(this.method, request);

    final Capitalsource capitalsource = this.capitalsourceService.getCapitalsourceById(userId,
        groupId, capitalsourceId);
    Assertions.assertEquals(CapitalsourceTransportBuilder.CAPITALSOURCE2_ID,
        capitalsource.getId().getId());
    Assertions.assertEquals(CapitalsourceImport.ALL_ALLOWED, capitalsource.getImportAllowed());
  }

  @Test
  public void test_setImportAllowedOnlyBalance_Successfull() throws Exception {
    final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
    final GroupID groupId = new GroupID(GroupTransportBuilder.GROUP1_ID);
    final CapitalsourceID capitalsourceId = new CapitalsourceID(
        CapitalsourceTransportBuilder.CAPITALSOURCE2_ID);
    final UpdateCapitalsourceRequest request = new UpdateCapitalsourceRequest();
    final CapitalsourceTransport transport = new CapitalsourceTransportBuilder().forCapitalsource2()
        .build();
    transport.setImportAllowed(2);
    request.setCapitalsourceTransport(transport);

    super.callUsecaseExpect204(this.method, request);

    final Capitalsource capitalsource = this.capitalsourceService.getCapitalsourceById(userId,
        groupId, capitalsourceId);
    Assertions.assertEquals(CapitalsourceTransportBuilder.CAPITALSOURCE2_ID,
        capitalsource.getId().getId());
    Assertions.assertEquals(CapitalsourceImport.BALANCE_ALLOWED, capitalsource.getImportAllowed());
  }

  @Test
  public void test_setImportAllowedNotAllowed_Successfull() throws Exception {
    final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
    final GroupID groupId = new GroupID(GroupTransportBuilder.GROUP1_ID);
    final CapitalsourceID capitalsourceId = new CapitalsourceID(
        CapitalsourceTransportBuilder.CAPITALSOURCE1_ID);
    final UpdateCapitalsourceRequest request = new UpdateCapitalsourceRequest();
    final CapitalsourceTransport transport = new CapitalsourceTransportBuilder().forCapitalsource1()
        .build();
    transport.setImportAllowed(0);
    request.setCapitalsourceTransport(transport);

    super.callUsecaseExpect204(this.method, request);

    final Capitalsource capitalsource = this.capitalsourceService.getCapitalsourceById(userId,
        groupId, capitalsourceId);
    Assertions.assertEquals(CapitalsourceTransportBuilder.CAPITALSOURCE1_ID,
        capitalsource.getId().getId());
    Assertions.assertEquals(CapitalsourceImport.NOT_ALLOWED, capitalsource.getImportAllowed());
  }

  @Test
  public void test_ProvisionAsset_Successfull() throws Exception {
    this.userName = UserTransportBuilder.USER3_NAME;
    this.userPassword = UserTransportBuilder.USER3_PASSWORD;
    final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
    final GroupID groupId = new GroupID(GroupTransportBuilder.GROUP1_ID);
    final CapitalsourceID capitalsourceId = new CapitalsourceID(
        CapitalsourceTransportBuilder.CAPITALSOURCE4_ID);
    final UpdateCapitalsourceRequest request = new UpdateCapitalsourceRequest();
    final CapitalsourceTransport transport = new CapitalsourceTransportBuilder().forCapitalsource4()
        .build();
    transport.setComment("hugo");
    request.setCapitalsourceTransport(transport);

    super.callUsecaseExpect204(this.method, request);

    final Capitalsource capitalsource = this.capitalsourceService.getCapitalsourceById(userId,
        groupId, capitalsourceId);
    Assertions.assertEquals(CapitalsourceTransportBuilder.CAPITALSOURCE4_ID,
        capitalsource.getId().getId());
    Assertions.assertEquals("hugo", capitalsource.getComment());
  }

  @Test
  public void test_LongTermAsset_Successfull() throws Exception {
    final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
    final GroupID groupId = new GroupID(GroupTransportBuilder.GROUP1_ID);
    final CapitalsourceID capitalsourceId = new CapitalsourceID(
        CapitalsourceTransportBuilder.CAPITALSOURCE2_ID);
    final UpdateCapitalsourceRequest request = new UpdateCapitalsourceRequest();
    final CapitalsourceTransport transport = new CapitalsourceTransportBuilder().forCapitalsource2()
        .build();
    transport.setComment("hugo");
    request.setCapitalsourceTransport(transport);

    super.callUsecaseExpect204(this.method, request);

    final Capitalsource capitalsource = this.capitalsourceService.getCapitalsourceById(userId,
        groupId, capitalsourceId);
    Assertions.assertEquals(CapitalsourceTransportBuilder.CAPITALSOURCE2_ID,
        capitalsource.getId().getId());
    Assertions.assertEquals("hugo", capitalsource.getComment());
  }

  @Test
  public void test_editCapitalsourceOwnedBySomeoneElse_notSuccessfull() throws Exception {
    final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
    final GroupID groupId = new GroupID(GroupTransportBuilder.GROUP1_ID);
    final CapitalsourceID capitalsourceId = new CapitalsourceID(
        CapitalsourceTransportBuilder.CAPITALSOURCE3_ID);
    final UpdateCapitalsourceRequest request = new UpdateCapitalsourceRequest();
    final CapitalsourceTransport transport = new CapitalsourceTransportBuilder().forCapitalsource1()
        .build();
    transport.setComment("hugo");
    request.setCapitalsourceTransport(transport);

    super.callUsecaseExpect204(this.method, request);

    final Capitalsource capitalsource = this.capitalsourceService.getCapitalsourceById(userId,
        groupId, capitalsourceId);
    Assertions.assertEquals(CapitalsourceTransportBuilder.CAPITALSOURCE3_ID,
        capitalsource.getId().getId());
    Assertions.assertEquals(CapitalsourceTransportBuilder.CAPITALSOURCE3_COMMENT,
        capitalsource.getComment());
  }

  @Test
  public void test_AuthorizationRequired_Error() throws Exception {
    this.userName = null;
    this.userPassword = null;
    super.callUsecaseExpect403("", this.method);
  }

  @Test
  @Sql("classpath:h2defaults.sql")
  public void test_emptyDatabase_noException() throws Exception {
    this.userName = UserTransportBuilder.ADMIN_NAME;
    this.userPassword = UserTransportBuilder.ADMIN_PASSWORD;
    final UpdateCapitalsourceRequest request = new UpdateCapitalsourceRequest();
    final CapitalsourceTransport transport = new CapitalsourceTransportBuilder().forCapitalsource1()
        .build();
    request.setCapitalsourceTransport(transport);

    super.callUsecaseExpect204(this.method, request);
  }
}