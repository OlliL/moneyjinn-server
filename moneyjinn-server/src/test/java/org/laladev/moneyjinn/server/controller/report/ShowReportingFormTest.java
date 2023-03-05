
package org.laladev.moneyjinn.server.controller.report;

import jakarta.inject.Inject;
import java.time.LocalDate;
import java.util.Arrays;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.model.PostingAccountID;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.setting.ClientReportingUnselectedPostingAccountIdsSetting;
import org.laladev.moneyjinn.server.builder.PostingAccountTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.laladev.moneyjinn.server.controller.api.ReportControllerApi;
import org.laladev.moneyjinn.server.model.ShowReportingFormResponse;
import org.laladev.moneyjinn.service.api.ISettingService;
import org.springframework.test.context.jdbc.Sql;

class ShowReportingFormTest extends AbstractControllerTest {
  @Inject
  private ISettingService settingService;

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
  protected void loadMethod() {
    super.getMock(ReportControllerApi.class).showReportingForm();
  }

  private ShowReportingFormResponse getDefaultResponse() {
    final ShowReportingFormResponse expected = new ShowReportingFormResponse();

    expected.setMinDate(LocalDate.parse("2008-12-01"));
    expected.setMaxDate(LocalDate.parse("2010-05-03"));
    return expected;
  }

  @Test
   void test_noSetting_defaultsResponse() throws Exception {
    final ShowReportingFormResponse expected = this.getDefaultResponse();
    final ShowReportingFormResponse actual = super.callUsecaseExpect200(
        ShowReportingFormResponse.class);
    Assertions.assertEquals(expected, actual);
  }

  @Test
   void test_witDefaultSelection_defaultsResponse() throws Exception {
    final ClientReportingUnselectedPostingAccountIdsSetting setting = new ClientReportingUnselectedPostingAccountIdsSetting(
        Arrays.asList(new PostingAccountID(PostingAccountTransportBuilder.POSTING_ACCOUNT1_ID),
            new PostingAccountID(PostingAccountTransportBuilder.POSTING_ACCOUNT2_ID)));
    final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
    this.settingService.setClientReportingUnselectedPostingAccountIdsSetting(userId, setting);
    final ShowReportingFormResponse expected = this.getDefaultResponse();
    expected.setPostingAccountIds(Arrays.asList(PostingAccountTransportBuilder.POSTING_ACCOUNT1_ID,
        PostingAccountTransportBuilder.POSTING_ACCOUNT2_ID));
    final ShowReportingFormResponse actual = super.callUsecaseExpect200(
        ShowReportingFormResponse.class);
    Assertions.assertEquals(expected, actual);
  }

  @Test
   void test_AuthorizationRequired_Error() throws Exception {
    this.userName = null;
    this.userPassword = null;

    super.callUsecaseExpect403();
  }

  @Test
  @Sql("classpath:h2defaults.sql")
  void test_emptyDatabase_noException() throws Exception {
    this.userName = UserTransportBuilder.ADMIN_NAME;
    this.userPassword = UserTransportBuilder.ADMIN_PASSWORD;

    super.callUsecaseExpect200(ShowReportingFormResponse.class);
  }
}