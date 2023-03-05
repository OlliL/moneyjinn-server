
package org.laladev.moneyjinn.server.controller.comparedata;

import jakarta.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.capitalsource.CapitalsourceID;
import org.laladev.moneyjinn.model.comparedata.CompareDataFormatID;
import org.laladev.moneyjinn.model.setting.ClientCompareDataSelectedCapitalsource;
import org.laladev.moneyjinn.model.setting.ClientCompareDataSelectedFormat;
import org.laladev.moneyjinn.model.setting.ClientCompareDataSelectedSourceIsFile;
import org.laladev.moneyjinn.server.builder.CapitalsourceTransportBuilder;
import org.laladev.moneyjinn.server.builder.CompareDataFormatTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.laladev.moneyjinn.server.controller.api.CompareDataControllerApi;
import org.laladev.moneyjinn.server.model.CompareDataFormatTransport;
import org.laladev.moneyjinn.server.model.ShowCompareDataFormResponse;
import org.laladev.moneyjinn.service.api.ISettingService;
import org.springframework.test.context.jdbc.Sql;

class ShowCompareDataFormTest extends AbstractControllerTest {
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
    super.getMock(CompareDataControllerApi.class).showCompareDataForm();
  }

  private ShowCompareDataFormResponse getDefaultResponse() {
    final ShowCompareDataFormResponse expected = new ShowCompareDataFormResponse();
    final List<CompareDataFormatTransport> compareDataFormatTransports = new ArrayList<>();
    compareDataFormatTransports
        .add(new CompareDataFormatTransportBuilder().forCompareDataFormat2().build());
    compareDataFormatTransports
        .add(new CompareDataFormatTransportBuilder().forCompareDataFormat3().build());
    compareDataFormatTransports
        .add(new CompareDataFormatTransportBuilder().forCompareDataFormat4().build());
    compareDataFormatTransports
        .add(new CompareDataFormatTransportBuilder().forCompareDataFormat5().build());
    compareDataFormatTransports
        .add(new CompareDataFormatTransportBuilder().forCompareDataFormat6().build());
    expected.setCompareDataFormatTransports(compareDataFormatTransports);
    return expected;
  }

  @Test
   void test_noSetting_defaultsResponse() throws Exception {
    final ShowCompareDataFormResponse expected = this.getDefaultResponse();

    final ShowCompareDataFormResponse actual = super.callUsecaseExpect200(false,
        ShowCompareDataFormResponse.class);

    Assertions.assertEquals(expected, actual);
  }

  @Test
   void test_witDefaultSelection_defaultsResponse() throws Exception {
    final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
    final ClientCompareDataSelectedCapitalsource settingCapitalsource = new ClientCompareDataSelectedCapitalsource(
        new CapitalsourceID(CapitalsourceTransportBuilder.CAPITALSOURCE1_ID));
    this.settingService.setClientCompareDataSelectedCapitalsource(userId, settingCapitalsource);
    final ClientCompareDataSelectedFormat settingFormat = new ClientCompareDataSelectedFormat(
        new CompareDataFormatID(CompareDataFormatTransportBuilder.COMPARE_DATA_FORMAT2_ID));
    this.settingService.setClientCompareDataSelectedFormat(userId, settingFormat);
    final ClientCompareDataSelectedSourceIsFile settingSource = new ClientCompareDataSelectedSourceIsFile(
        Boolean.TRUE);
    this.settingService.setClientCompareDataSelectedSourceIsFile(userId, settingSource);
    final ShowCompareDataFormResponse expected = this.getDefaultResponse();
    expected.setSelectedCapitalsourceId(CapitalsourceTransportBuilder.CAPITALSOURCE1_ID);
    expected.setSelectedDataFormat(CompareDataFormatTransportBuilder.COMPARE_DATA_FORMAT2_ID);
    expected.setSelectedSourceIsFile(1);

    final ShowCompareDataFormResponse actual = super.callUsecaseExpect200(false,
        ShowCompareDataFormResponse.class);

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

    super.callUsecaseExpect200(ShowCompareDataFormResponse.class);
  }
}