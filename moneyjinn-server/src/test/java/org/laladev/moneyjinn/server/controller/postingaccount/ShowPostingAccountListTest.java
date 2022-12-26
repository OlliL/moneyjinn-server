
package org.laladev.moneyjinn.server.controller.postingaccount;

import jakarta.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.core.rest.model.ErrorResponse;
import org.laladev.moneyjinn.core.rest.model.postingaccount.ShowPostingAccountListResponse;
import org.laladev.moneyjinn.core.rest.model.transport.PostingAccountTransport;
import org.laladev.moneyjinn.model.PostingAccount;
import org.laladev.moneyjinn.model.access.AccessID;
import org.laladev.moneyjinn.model.setting.ClientMaxRowsSetting;
import org.laladev.moneyjinn.server.builder.PostingAccountTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.laladev.moneyjinn.service.api.IPostingAccountService;
import org.laladev.moneyjinn.service.impl.SettingService;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.jdbc.Sql;

public class ShowPostingAccountListTest extends AbstractControllerTest {
  @Inject
  private SettingService settingService;
  @Inject
  private IPostingAccountService postingAccountService;
  private final HttpMethod method = HttpMethod.GET;
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

  private ShowPostingAccountListResponse getCompleteResponse() {
    final ShowPostingAccountListResponse expected = new ShowPostingAccountListResponse();
    expected.setInitials(new HashSet<>(Arrays.asList('P', 'X')));
    final List<PostingAccountTransport> postingAccountTransports = new ArrayList<>();
    postingAccountTransports.add(new PostingAccountTransportBuilder().forPostingAccount1().build());
    postingAccountTransports.add(new PostingAccountTransportBuilder().forPostingAccount2().build());
    postingAccountTransports.add(new PostingAccountTransportBuilder().forPostingAccount3().build());
    expected.setPostingAccountTransports(postingAccountTransports);
    return expected;
  }

  @Test
  public void test_default_FullResponseObject() throws Exception {
    final ShowPostingAccountListResponse expected = this.getCompleteResponse();
    final ShowPostingAccountListResponse actual = super.callUsecaseWithoutContent("", this.method,
        false, ShowPostingAccountListResponse.class);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_OnlyAdminAllowed_ErrorResponse() throws Exception {
    this.userName = UserTransportBuilder.USER1_NAME;
    this.userPassword = UserTransportBuilder.USER1_PASSWORD;
    final ShowPostingAccountListResponse expected = this.getCompleteResponse();
    final ShowPostingAccountListResponse actual = super.callUsecaseWithoutContent("/all",
        this.method, false, ShowPostingAccountListResponse.class);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_MaxRowSettingReached_OnlyInitials() throws Exception {
    final ShowPostingAccountListResponse expected = new ShowPostingAccountListResponse();
    expected.setInitials(new HashSet<>(Arrays.asList('P', 'X')));
    final ClientMaxRowsSetting setting = new ClientMaxRowsSetting(1);
    this.settingService.setClientMaxRowsSetting(new AccessID(UserTransportBuilder.ADMIN_ID),
        setting);
    final ShowPostingAccountListResponse actual = super.callUsecaseWithoutContent("", this.method,
        false, ShowPostingAccountListResponse.class);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_explicitAll_FullResponseObject() throws Exception {
    final ShowPostingAccountListResponse expected = this.getCompleteResponse();
    final ClientMaxRowsSetting setting = new ClientMaxRowsSetting(1);
    this.settingService.setClientMaxRowsSetting(new AccessID(UserTransportBuilder.ADMIN_ID),
        setting);
    final ShowPostingAccountListResponse actual = super.callUsecaseWithoutContent("/all",
        this.method, false, ShowPostingAccountListResponse.class);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_initialA_AResponseObject() throws Exception {
    final ShowPostingAccountListResponse expected = new ShowPostingAccountListResponse();
    expected.setInitials(new HashSet<>(Arrays.asList('P', 'X')));
    final List<PostingAccountTransport> postingAccountTransports = new ArrayList<>();
    postingAccountTransports.add(new PostingAccountTransportBuilder().forPostingAccount3().build());
    expected.setPostingAccountTransports(postingAccountTransports);
    final ShowPostingAccountListResponse actual = super.callUsecaseWithoutContent("/X", this.method,
        false, ShowPostingAccountListResponse.class);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_initialUnderscore_AResponseObject() throws Exception {
    // make sure that requesting data starting with _ only returns matching data and _ is not
    // interpreted as LIKE SQL special char
    final PostingAccount postingAccount = new PostingAccount();
    postingAccount.setName("_1");
    this.postingAccountService.createPostingAccount(postingAccount);
    final PostingAccountTransport postingAccountTransport = new PostingAccountTransport();
    postingAccountTransport.setId(PostingAccountTransportBuilder.NEXT_ID);
    postingAccountTransport.setName(postingAccount.getName());
    final ShowPostingAccountListResponse expected = new ShowPostingAccountListResponse();
    expected.setInitials(new HashSet<>(Arrays.asList('P', 'X', '_')));
    final List<PostingAccountTransport> postingAccountTransports = new ArrayList<>();
    postingAccountTransports.add(postingAccountTransport);
    expected.setPostingAccountTransports(postingAccountTransports);
    final ShowPostingAccountListResponse actual = super.callUsecaseWithoutContent("/_", this.method,
        false, ShowPostingAccountListResponse.class);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_AuthorizationRequired1_Error() throws Exception {
    this.userName = null;
    this.userPassword = null;
    final ErrorResponse actual = super.callUsecaseWithoutContent("", this.method, false,
        ErrorResponse.class);
    Assertions.assertEquals(super.accessDeniedErrorResponse(), actual);
  }

  @Test
  public void test_AuthorizationRequired2_Error() throws Exception {
    this.userName = null;
    this.userPassword = null;
    final ErrorResponse actual = super.callUsecaseWithoutContent("/all", this.method, false,
        ErrorResponse.class);
    Assertions.assertEquals(super.accessDeniedErrorResponse(), actual);
  }

  @Test
  @Sql("classpath:h2defaults.sql")
  public void test_emptyDatabase_noException() throws Exception {
    this.userName = UserTransportBuilder.ADMIN_NAME;
    this.userPassword = UserTransportBuilder.ADMIN_PASSWORD;
    final ShowPostingAccountListResponse expected = new ShowPostingAccountListResponse();
    final ShowPostingAccountListResponse actual = super.callUsecaseWithoutContent("", this.method,
        false, ShowPostingAccountListResponse.class);
    Assertions.assertEquals(expected, actual);
  }
}
