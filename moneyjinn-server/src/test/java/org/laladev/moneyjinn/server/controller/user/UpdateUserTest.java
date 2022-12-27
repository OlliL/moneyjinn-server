
package org.laladev.moneyjinn.server.controller.user;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.core.rest.model.transport.GroupTransport;
import org.laladev.moneyjinn.core.rest.model.transport.UserTransport;
import org.laladev.moneyjinn.core.rest.model.transport.ValidationItemTransport;
import org.laladev.moneyjinn.core.rest.model.user.UpdateUserRequest;
import org.laladev.moneyjinn.core.rest.model.user.UpdateUserResponse;
import org.laladev.moneyjinn.core.rest.model.user.transport.AccessRelationTransport;
import org.laladev.moneyjinn.model.access.AccessID;
import org.laladev.moneyjinn.model.access.AccessRelation;
import org.laladev.moneyjinn.model.access.User;
import org.laladev.moneyjinn.model.access.UserAttribute;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.access.UserPermission;
import org.laladev.moneyjinn.server.builder.AccessRelationTransportBuilder;
import org.laladev.moneyjinn.server.builder.DateUtil;
import org.laladev.moneyjinn.server.builder.GroupTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.builder.ValidationItemTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.laladev.moneyjinn.service.api.IAccessRelationService;
import org.laladev.moneyjinn.service.api.IUserService;
import org.springframework.http.HttpMethod;
import jakarta.inject.Inject;

public class UpdateUserTest extends AbstractControllerTest {
  @Inject
  private IUserService userService;
  @Inject
  private IAccessRelationService accessRelationService;
  private final AccessID accessIdUser1 = new AccessID(UserTransportBuilder.USER1_ID);
  private final AccessID accessIdUser2 = new AccessID(UserTransportBuilder.USER2_ID);
  private final AccessRelation accessRelationRoot = new AccessRelation(new AccessID(0l));
  private final AccessRelation accessRelationGroup1 = new AccessRelation(
      new AccessID(GroupTransportBuilder.GROUP1_ID), this.accessRelationRoot);
  private final AccessRelation accessRelationGroup2 = new AccessRelation(
      new AccessID(GroupTransportBuilder.GROUP2_ID), this.accessRelationRoot);
  private final AccessRelation accessRelationAdminGroup = new AccessRelation(
      new AccessID(GroupTransportBuilder.ADMINGROUP_ID), this.accessRelationRoot);
  private final AccessRelation accessRelationUser1Default1 = new AccessRelation(this.accessIdUser1,
      this.accessRelationGroup1, LocalDate.parse("2000-01-01"), LocalDate.parse("2599-12-31"));
  private final AccessRelation accessRelationUser1Default2 = new AccessRelation(this.accessIdUser1,
      this.accessRelationGroup2, LocalDate.parse("2600-01-01"), LocalDate.parse("2699-12-31"));
  private final AccessRelation accessRelationUser1Default3 = new AccessRelation(this.accessIdUser1,
      this.accessRelationGroup1, LocalDate.parse("2700-01-01"), LocalDate.parse("2799-12-31"));
  private final AccessRelation accessRelationUser1Default4 = new AccessRelation(this.accessIdUser1,
      this.accessRelationGroup2, LocalDate.parse("2800-01-01"), LocalDate.parse("2999-12-31"));
  private final HttpMethod method = HttpMethod.PUT;
  private String userName;
  private String userPassword;

  @BeforeEach
  public void setUp() {
    this.userName = UserTransportBuilder.ADMIN_NAME;
    this.userPassword = UserTransportBuilder.ADMIN_PASSWORD;
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

  private void testError(final UserTransport transport,
      final AccessRelationTransport accessRelationTransport, final ErrorCode errorCode)
      throws Exception {
    final UpdateUserRequest request = new UpdateUserRequest();
    request.setUserTransport(transport);
    request.setAccessRelationTransport(accessRelationTransport);
    final UpdateUserResponse expected = new UpdateUserResponse();
    final List<GroupTransport> groupTransports = new ArrayList<>();
    groupTransports.add(new GroupTransportBuilder().forAdminGroup().build());
    groupTransports.add(new GroupTransportBuilder().forGroup1().build());
    groupTransports.add(new GroupTransportBuilder().forGroup2().build());
    groupTransports.add(new GroupTransportBuilder().forGroup3().build());
    expected.setGroupTransports(groupTransports);
    final List<AccessRelationTransport> accessRelationTransports = new ArrayList<>();
    accessRelationTransports
        .add(new AccessRelationTransportBuilder().forUser1_2000_01_01().build());
    accessRelationTransports
        .add(new AccessRelationTransportBuilder().forUser1_2600_01_01().build());
    accessRelationTransports
        .add(new AccessRelationTransportBuilder().forUser1_2700_01_01().build());
    accessRelationTransports
        .add(new AccessRelationTransportBuilder().forUser1_2800_01_01().build());
    expected.setAccessRelationTransports(accessRelationTransports);
    final List<ValidationItemTransport> validationItems = new ArrayList<>();
    validationItems.add(new ValidationItemTransportBuilder().withKey(transport.getId().intValue())
        .withError(errorCode.getErrorCode()).build());
    expected.setValidationItemTransports(validationItems);
    expected.setResult(Boolean.FALSE);
    final UpdateUserResponse actual = super.callUsecaseWithContent("", this.method, request, false,
        UpdateUserResponse.class);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_UsernameAlreadyExisting_Error() throws Exception {
    final UserTransport transport = new UserTransportBuilder().forUser1().build();
    transport.setUserName(UserTransportBuilder.USER2_NAME);
    this.testError(transport, null, ErrorCode.USER_WITH_SAME_NAME_ALREADY_EXISTS);
  }

  @Test
  public void test_EmptyUsername_Error() throws Exception {
    final UserTransport transport = new UserTransportBuilder().forUser1().build();
    transport.setUserName("");
    this.testError(transport, null, ErrorCode.NAME_MUST_NOT_BE_EMPTY);
  }

  @Test
  public void test_AccessRelationAndPasswordEmpty_SuccessfullPasswordNotChanged() throws Exception {
    final UpdateUserRequest request = new UpdateUserRequest();
    final UserTransport transport = new UserTransportBuilder().forUser1().build();
    transport.setUserIsNew(Short.valueOf((short) 0));
    request.setUserTransport(transport);
    final UpdateUserResponse actual = super.callUsecaseWithContent("", this.method, request, false,
        UpdateUserResponse.class);
    Assertions.assertTrue(actual.getResult());
    final User user = this.userService.getUserById(new UserID(UserTransportBuilder.USER1_ID));
    Assertions.assertEquals(UserTransportBuilder.USER1_PASSWORD_SHA1, user.getPassword());
    Assertions.assertEquals(Arrays.asList(UserAttribute.NONE), user.getAttributes());
  }

  @Test
  public void test_AccessRelationEmpty_Successfull() throws Exception {
    final UpdateUserRequest request = new UpdateUserRequest();
    final UserTransport transport = new UserTransportBuilder().forUser1().build();
    transport.setUserPassword("123");
    transport.setUserName("hugo");
    transport.setUserCanLogin(Short.valueOf((short) 0));
    transport.setUserIsAdmin(Short.valueOf((short) 1));
    /*
     * this must be ignored by the server as the password is changed (which is done by the admin
     * here so the user MUST reset the password afterwards)
     */
    transport.setUserIsNew(Short.valueOf((short) 0));
    request.setUserTransport(transport);
    final UpdateUserResponse actual = super.callUsecaseWithContent("", this.method, request, false,
        UpdateUserResponse.class);
    Assertions.assertTrue(actual.getResult());
    final User user = this.userService.getUserById(new UserID(UserTransportBuilder.USER1_ID));
    // sha1 of 123 ------vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
    Assertions.assertEquals("40bd001563085fc35165329ea1ff5c5ecbdbbeef", user.getPassword());
    Assertions.assertEquals("hugo", user.getName());
    // instead of NONE -----------------------------vvvvvv
    Assertions.assertEquals(Arrays.asList(UserAttribute.IS_NEW), user.getAttributes());
    Assertions.assertEquals(Arrays.asList(UserPermission.ADMIN), user.getPermissions());
  }

  @Test
  public void test_NoValidFromForAccessRelation_Error() throws Exception {
    final UserTransport transport = new UserTransportBuilder().forUser1().build();
    final AccessRelationTransport accessRelationTransport = new AccessRelationTransportBuilder()
        .forUser1_2000_01_01().build();
    accessRelationTransport.setValidfrom(null);
    this.testError(transport, accessRelationTransport, ErrorCode.VALIDFROM_NOT_DEFINED);
  }

  @Test
  public void test_ValidFromToEarlyForAccessRelation_Error() throws Exception {
    final UserTransport transport = new UserTransportBuilder().forUser1().build();
    final AccessRelationTransport accessRelationTransport = new AccessRelationTransportBuilder()
        .forUser1_2000_01_01().build();
    accessRelationTransport.setValidfrom(new Date(
        LocalDate.now().minusDays(1l).atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()));
    this.testError(transport, accessRelationTransport, ErrorCode.VALIDFROM_EARLIER_THAN_TOMORROW);
  }

  @Test
  public void test_NoGroupForAccessRelation_Error() throws Exception {
    final UserTransport transport = new UserTransportBuilder().forUser1().build();
    final AccessRelationTransport accessRelationTransport = new AccessRelationTransportBuilder()
        .forUser1_2000_01_01().build();
    accessRelationTransport.setRefId(null);
    this.testError(transport, accessRelationTransport, ErrorCode.GROUP_MUST_BE_SPECIFIED);
  }

  private void help_AccessRelation_Testing(final UserTransport transport,
      final AccessRelationTransport accessRelationTransport,
      final List<AccessRelation> expectedAccessRelations) throws Exception {
    final UpdateUserRequest request = new UpdateUserRequest();
    request.setUserTransport(transport);
    request.setAccessRelationTransport(accessRelationTransport);
    final UpdateUserResponse actual = super.callUsecaseWithContent("", this.method, request, false,
        UpdateUserResponse.class);
    Assertions.assertTrue(actual.getResult());
    final List<AccessRelation> accessRelations = this.accessRelationService
        .getAllAccessRelationsById(new UserID(transport.getId()));
    Assertions.assertEquals(expectedAccessRelations, accessRelations);
  }

  /**
   * see Javadoc in AccessRelationService.setAccessRelation() for the cases
   */
  @Test
  public void test_AR_caseA() throws Exception {
    final UserTransport transport = new UserTransportBuilder().forUser2().build();
    final AccessRelationTransport accessRelationTransport = new AccessRelationTransport();
    accessRelationTransport.setId(UserTransportBuilder.USER2_ID);
    accessRelationTransport.setRefId(GroupTransportBuilder.GROUP2_ID);
    accessRelationTransport.setValidfrom(DateUtil.getGmtDate("2100-12-31"));
    final List<AccessRelation> accessRelations = new ArrayList<>();
    accessRelations.add(new AccessRelation(this.accessIdUser2, this.accessRelationGroup1,
        LocalDate.parse("2000-01-01"), LocalDate.parse("2100-12-30")));
    accessRelations.add(new AccessRelation(this.accessIdUser2, this.accessRelationGroup2,
        LocalDate.parse("2100-12-31"), LocalDate.parse("2999-12-31")));
    this.help_AccessRelation_Testing(transport, accessRelationTransport, accessRelations);
  }

  /**
   * see Javadoc in AccessRelationService.setAccessRelation() for the cases
   */
  @Test
  public void test_AR_caseB() throws Exception {
    final UserTransport transport = new UserTransportBuilder().forUser1().build();
    final AccessRelationTransport accessRelationTransport = new AccessRelationTransport();
    accessRelationTransport.setId(UserTransportBuilder.USER1_ID);
    accessRelationTransport.setRefId(GroupTransportBuilder.GROUP1_ID);
    accessRelationTransport.setValidfrom(DateUtil.getGmtDate("2900-01-01"));
    final List<AccessRelation> accessRelations = new ArrayList<>();
    accessRelations.add(this.accessRelationUser1Default1);
    accessRelations.add(this.accessRelationUser1Default2);
    accessRelations.add(this.accessRelationUser1Default3);
    accessRelations.add(new AccessRelation(this.accessIdUser1, this.accessRelationGroup2,
        LocalDate.parse("2800-01-01"), LocalDate.parse("2899-12-31")));
    accessRelations.add(new AccessRelation(this.accessIdUser1, this.accessRelationGroup1,
        LocalDate.parse("2900-01-01"), LocalDate.parse("2999-12-31")));
    this.help_AccessRelation_Testing(transport, accessRelationTransport, accessRelations);
  }

  /**
   * see Javadoc in AccessRelationService.setAccessRelation() for the cases
   */
  @Test
  public void test_AR_caseC() throws Exception {
    final UserTransport transport = new UserTransportBuilder().forUser1().build();
    final AccessRelationTransport accessRelationTransport = new AccessRelationTransport();
    accessRelationTransport.setId(UserTransportBuilder.USER1_ID);
    accessRelationTransport.setRefId(GroupTransportBuilder.GROUP2_ID);
    accessRelationTransport.setValidfrom(DateUtil.getGmtDate("2900-01-01"));
    final List<AccessRelation> accessRelations = new ArrayList<>();
    accessRelations.add(this.accessRelationUser1Default1);
    accessRelations.add(this.accessRelationUser1Default2);
    accessRelations.add(this.accessRelationUser1Default3);
    accessRelations.add(this.accessRelationUser1Default4);
    this.help_AccessRelation_Testing(transport, accessRelationTransport, accessRelations);
  }

  /**
   * see Javadoc in AccessRelationService.setAccessRelation() for the cases
   */
  @Test
  public void test_AR_caseD() throws Exception {
    final UserTransport transport = new UserTransportBuilder().forUser1().build();
    final AccessRelationTransport accessRelationTransport = new AccessRelationTransport();
    accessRelationTransport.setId(UserTransportBuilder.USER1_ID);
    accessRelationTransport.setRefId(GroupTransportBuilder.GROUP1_ID);
    accessRelationTransport.setValidfrom(DateUtil.getGmtDate("2800-01-01"));
    final List<AccessRelation> accessRelations = new ArrayList<>();
    accessRelations.add(this.accessRelationUser1Default1);
    accessRelations.add(this.accessRelationUser1Default2);
    accessRelations.add(new AccessRelation(this.accessIdUser1, this.accessRelationGroup1,
        LocalDate.parse("2700-01-01"), LocalDate.parse("2999-12-31")));
    this.help_AccessRelation_Testing(transport, accessRelationTransport, accessRelations);
  }

  /**
   * see Javadoc in AccessRelationService.setAccessRelation() for the cases
   */
  @Test
  public void test_AR_caseE() throws Exception {
    final UserTransport transport = new UserTransportBuilder().forUser1().build();
    final AccessRelationTransport accessRelationTransport = new AccessRelationTransport();
    accessRelationTransport.setId(UserTransportBuilder.USER1_ID);
    accessRelationTransport.setRefId(GroupTransportBuilder.GROUP1_ID);
    accessRelationTransport.setValidfrom(DateUtil.getGmtDate("2700-01-01"));
    final List<AccessRelation> accessRelations = new ArrayList<>();
    accessRelations.add(this.accessRelationUser1Default1);
    accessRelations.add(this.accessRelationUser1Default2);
    accessRelations.add(this.accessRelationUser1Default3);
    accessRelations.add(this.accessRelationUser1Default4);
    this.help_AccessRelation_Testing(transport, accessRelationTransport, accessRelations);
  }

  /**
   * see Javadoc in AccessRelationService.setAccessRelation() for the cases
   */
  @Test
  public void test_AR_caseF() throws Exception {
    final UserTransport transport = new UserTransportBuilder().forUser1().build();
    final AccessRelationTransport accessRelationTransport = new AccessRelationTransport();
    accessRelationTransport.setId(UserTransportBuilder.USER1_ID);
    accessRelationTransport.setRefId(GroupTransportBuilder.GROUP2_ID);
    accessRelationTransport.setValidfrom(DateUtil.getGmtDate("2780-01-01"));
    final List<AccessRelation> accessRelations = new ArrayList<>();
    accessRelations.add(this.accessRelationUser1Default1);
    accessRelations.add(this.accessRelationUser1Default2);
    accessRelations.add(new AccessRelation(this.accessIdUser1, this.accessRelationGroup1,
        LocalDate.parse("2700-01-01"), LocalDate.parse("2779-12-31")));
    accessRelations.add(new AccessRelation(this.accessIdUser1, this.accessRelationGroup2,
        LocalDate.parse("2780-01-01"), LocalDate.parse("2999-12-31")));
    this.help_AccessRelation_Testing(transport, accessRelationTransport, accessRelations);
  }

  /**
   * see Javadoc in AccessRelationService.setAccessRelation() for the cases
   */
  @Test
  public void test_AR_caseG() throws Exception {
    final UserTransport transport = new UserTransportBuilder().forUser1().build();
    final AccessRelationTransport accessRelationTransport = new AccessRelationTransport();
    accessRelationTransport.setId(UserTransportBuilder.USER1_ID);
    accessRelationTransport.setRefId(GroupTransportBuilder.GROUP1_ID);
    accessRelationTransport.setValidfrom(DateUtil.getGmtDate("2650-01-01"));
    final List<AccessRelation> accessRelations = new ArrayList<>();
    accessRelations.add(this.accessRelationUser1Default1);
    accessRelations.add(new AccessRelation(this.accessIdUser1, this.accessRelationGroup2,
        LocalDate.parse("2600-01-01"), LocalDate.parse("2649-12-31")));
    accessRelations.add(new AccessRelation(this.accessIdUser1, this.accessRelationGroup1,
        LocalDate.parse("2650-01-01"), LocalDate.parse("2799-12-31")));
    accessRelations.add(this.accessRelationUser1Default4);
    this.help_AccessRelation_Testing(transport, accessRelationTransport, accessRelations);
  }

  /**
   * see Javadoc in AccessRelationService.setAccessRelation() for the cases
   */
  @Test
  public void test_AR_caseH() throws Exception {
    final UserTransport transport = new UserTransportBuilder().forUser1().build();
    final AccessRelationTransport accessRelationTransport = new AccessRelationTransport();
    accessRelationTransport.setId(UserTransportBuilder.USER1_ID);
    accessRelationTransport.setRefId(GroupTransportBuilder.GROUP2_ID);
    accessRelationTransport.setValidfrom(DateUtil.getGmtDate("2700-01-01"));
    final List<AccessRelation> accessRelations = new ArrayList<>();
    accessRelations.add(this.accessRelationUser1Default1);
    accessRelations.add(new AccessRelation(this.accessIdUser1, this.accessRelationGroup2,
        LocalDate.parse("2600-01-01"), LocalDate.parse("2999-12-31")));
    this.help_AccessRelation_Testing(transport, accessRelationTransport, accessRelations);
  }

  /**
   * see Javadoc in AccessRelationService.setAccessRelation() for the cases
   */
  @Test
  public void test_AR_caseI() throws Exception {
    final UserTransport transport = new UserTransportBuilder().forUser1().build();
    final AccessRelationTransport accessRelationTransport = new AccessRelationTransport();
    accessRelationTransport.setId(UserTransportBuilder.USER1_ID);
    accessRelationTransport.setRefId(GroupTransportBuilder.ADMINGROUP_ID);
    accessRelationTransport.setValidfrom(DateUtil.getGmtDate("2700-01-01"));
    final List<AccessRelation> accessRelations = new ArrayList<>();
    accessRelations.add(this.accessRelationUser1Default1);
    accessRelations.add(this.accessRelationUser1Default2);
    accessRelations.add(new AccessRelation(this.accessIdUser1, this.accessRelationAdminGroup,
        LocalDate.parse("2700-01-01"), LocalDate.parse("2799-12-31")));
    accessRelations.add(this.accessRelationUser1Default4);
    this.help_AccessRelation_Testing(transport, accessRelationTransport, accessRelations);
  }

  @Test
  public void test_OnlyAdminAllowed_ErrorResponse() throws Exception {
    this.userName = UserTransportBuilder.USER1_NAME;
    this.userPassword = UserTransportBuilder.USER1_PASSWORD;
    final UpdateUserRequest request = new UpdateUserRequest();
    super.callUsecaseExpect403("", this.method, request);
  }

  @Test
  public void test_AuthorizationRequired_Error() throws Exception {
    this.userName = null;
    this.userPassword = null;
    super.callUsecaseExpect403("", this.method);
  }
}