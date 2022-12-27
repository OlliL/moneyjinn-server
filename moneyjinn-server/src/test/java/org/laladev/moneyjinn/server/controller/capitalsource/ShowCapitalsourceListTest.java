
package org.laladev.moneyjinn.server.controller.capitalsource;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.core.rest.model.capitalsource.ShowCapitalsourceListResponse;
import org.laladev.moneyjinn.core.rest.model.transport.CapitalsourceTransport;
import org.laladev.moneyjinn.model.access.AccessID;
import org.laladev.moneyjinn.model.access.Group;
import org.laladev.moneyjinn.model.access.GroupID;
import org.laladev.moneyjinn.model.access.User;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.capitalsource.Capitalsource;
import org.laladev.moneyjinn.model.capitalsource.CapitalsourceImport;
import org.laladev.moneyjinn.model.capitalsource.CapitalsourceState;
import org.laladev.moneyjinn.model.capitalsource.CapitalsourceType;
import org.laladev.moneyjinn.model.setting.ClientMaxRowsSetting;
import org.laladev.moneyjinn.server.builder.CapitalsourceTransportBuilder;
import org.laladev.moneyjinn.server.builder.DateUtil;
import org.laladev.moneyjinn.server.builder.GroupTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.laladev.moneyjinn.server.controller.mapper.CapitalsourceImportMapper;
import org.laladev.moneyjinn.server.controller.mapper.CapitalsourceStateMapper;
import org.laladev.moneyjinn.server.controller.mapper.CapitalsourceTypeMapper;
import org.laladev.moneyjinn.service.api.ICapitalsourceService;
import org.laladev.moneyjinn.service.impl.SettingService;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.jdbc.Sql;
import jakarta.inject.Inject;

public class ShowCapitalsourceListTest extends AbstractControllerTest {
  @Inject
  private SettingService settingService;
  @Inject
  private ICapitalsourceService capitalsourceService;
  private final HttpMethod method = HttpMethod.GET;
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

  private ShowCapitalsourceListResponse getCompleteResponse() {
    final ShowCapitalsourceListResponse expected = new ShowCapitalsourceListResponse();
    expected.setInitials(new HashSet<>(Arrays.asList('A', 'S', 'X')));
    final List<CapitalsourceTransport> capitalsourceTransports = new ArrayList<>();
    capitalsourceTransports.add(new CapitalsourceTransportBuilder().forCapitalsource1().build());
    capitalsourceTransports.add(new CapitalsourceTransportBuilder().forCapitalsource2().build());
    capitalsourceTransports.add(new CapitalsourceTransportBuilder().forCapitalsource3().build());
    capitalsourceTransports.add(new CapitalsourceTransportBuilder().forCapitalsource4().build());
    capitalsourceTransports.add(new CapitalsourceTransportBuilder().forCapitalsource5().build());
    capitalsourceTransports.add(new CapitalsourceTransportBuilder().forCapitalsource6().build());
    expected.setCapitalsourceTransports(capitalsourceTransports);
    expected.setCurrentlyValid(false);
    return expected;
  }

  private ShowCapitalsourceListResponse getCurrentlyValidResponse() {
    final ShowCapitalsourceListResponse expected = new ShowCapitalsourceListResponse();
    expected.setInitials(new HashSet<>(Arrays.asList('A', 'S', 'X')));
    final List<CapitalsourceTransport> capitalsourceTransports = new ArrayList<>();
    capitalsourceTransports.add(new CapitalsourceTransportBuilder().forCapitalsource1().build());
    capitalsourceTransports.add(new CapitalsourceTransportBuilder().forCapitalsource2().build());
    capitalsourceTransports.add(new CapitalsourceTransportBuilder().forCapitalsource5().build());
    capitalsourceTransports.add(new CapitalsourceTransportBuilder().forCapitalsource6().build());
    expected.setCapitalsourceTransports(capitalsourceTransports);
    expected.setCurrentlyValid(true);
    return expected;
  }

  @Test
  public void test_default_FullResponseObject() throws Exception {
    final ClientMaxRowsSetting setting = new ClientMaxRowsSetting(10);
    this.settingService.setClientMaxRowsSetting(new AccessID(UserTransportBuilder.USER1_ID),
        setting);
    // set default to 0
    ShowCapitalsourceListResponse expected = this.getCompleteResponse();
    ShowCapitalsourceListResponse actual = super.callUsecaseWithoutContent("/currentlyValid/0",
        this.method, false, ShowCapitalsourceListResponse.class);
    Assertions.assertEquals(expected, actual);
    // now the new default 0 must be taken
    actual = super.callUsecaseWithoutContent("/currentlyValid", this.method, false,
        ShowCapitalsourceListResponse.class);
    Assertions.assertEquals(expected, actual);
    // this must change the default-setting to 1
    expected = this.getCurrentlyValidResponse();
    actual = super.callUsecaseWithoutContent("/currentlyValid/1", this.method, false,
        ShowCapitalsourceListResponse.class);
    Assertions.assertEquals(expected, actual);
    // now the default 1 must be taken
    actual = super.callUsecaseWithoutContent("/currentlyValid", this.method, false,
        ShowCapitalsourceListResponse.class);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_MaxRowSettingReached_OnlyInitials() throws Exception {
    final ShowCapitalsourceListResponse expected = new ShowCapitalsourceListResponse();
    expected.setInitials(new HashSet<>(Arrays.asList('A', 'S', 'X')));
    final ClientMaxRowsSetting setting = new ClientMaxRowsSetting(1);
    this.settingService.setClientMaxRowsSetting(new AccessID(UserTransportBuilder.USER1_ID),
        setting);
    final ShowCapitalsourceListResponse actual = super.callUsecaseWithoutContent(
        "/currentlyValid/0", this.method, false, ShowCapitalsourceListResponse.class);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_explicitAll_FullResponseObject() throws Exception {
    final ClientMaxRowsSetting setting = new ClientMaxRowsSetting(1);
    this.settingService.setClientMaxRowsSetting(new AccessID(UserTransportBuilder.USER1_ID),
        setting);
    // set default to 0
    ShowCapitalsourceListResponse expected = this.getCompleteResponse();
    ShowCapitalsourceListResponse actual = super.callUsecaseWithoutContent("/all/currentlyValid/0",
        this.method, false, ShowCapitalsourceListResponse.class);
    Assertions.assertEquals(expected, actual);
    // now the new default 0 must be taken
    actual = super.callUsecaseWithoutContent("/all/currentlyValid", this.method, false,
        ShowCapitalsourceListResponse.class);
    Assertions.assertEquals(expected, actual);
    // this must change the default-setting to 1
    expected = this.getCurrentlyValidResponse();
    actual = super.callUsecaseWithoutContent("/all/currentlyValid/1", this.method, false,
        ShowCapitalsourceListResponse.class);
    Assertions.assertEquals(expected, actual);
    // now the default 1 must be taken
    actual = super.callUsecaseWithoutContent("/all/currentlyValid", this.method, false,
        ShowCapitalsourceListResponse.class);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_initialS_AResponseObject() throws Exception {
    // set default to 0
    ShowCapitalsourceListResponse expected = new ShowCapitalsourceListResponse();
    expected.setInitials(new HashSet<>(Arrays.asList('A', 'S', 'X')));
    List<CapitalsourceTransport> capitalsourceTransports = new ArrayList<>();
    capitalsourceTransports.add(new CapitalsourceTransportBuilder().forCapitalsource2().build());
    capitalsourceTransports.add(new CapitalsourceTransportBuilder().forCapitalsource3().build());
    expected.setCapitalsourceTransports(capitalsourceTransports);
    expected.setCurrentlyValid(false);
    ShowCapitalsourceListResponse actual = super.callUsecaseWithoutContent("/S/currentlyValid/0",
        this.method, false, ShowCapitalsourceListResponse.class);
    Assertions.assertEquals(expected, actual);
    // now the new default 0 must be taken
    actual = super.callUsecaseWithoutContent("/S/currentlyValid", this.method, false,
        ShowCapitalsourceListResponse.class);
    Assertions.assertEquals(expected, actual);
    // this must change the default-setting to 1
    expected = new ShowCapitalsourceListResponse();
    expected.setInitials(new HashSet<>(Arrays.asList('A', 'S', 'X')));
    capitalsourceTransports = new ArrayList<>();
    capitalsourceTransports.add(new CapitalsourceTransportBuilder().forCapitalsource2().build());
    expected.setCapitalsourceTransports(capitalsourceTransports);
    expected.setCurrentlyValid(true);
    actual = super.callUsecaseWithoutContent("/S/currentlyValid/1", this.method, false,
        ShowCapitalsourceListResponse.class);
    Assertions.assertEquals(expected, actual);
    // now the default 1 must be taken
    actual = super.callUsecaseWithoutContent("/S/currentlyValid", this.method, false,
        ShowCapitalsourceListResponse.class);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_initialUnderscore_AResponseObject() throws Exception {
    // make sure that requesting data starting with _ only returns matching data and _ is not
    // interpreted as LIKE SQL special char
    final Capitalsource capitalsource = new Capitalsource();
    capitalsource.setUser(new User(new UserID(UserTransportBuilder.USER1_ID)));
    capitalsource.setAccess(new Group(new GroupID(GroupTransportBuilder.GROUP1_ID)));
    capitalsource.setComment("_1");
    capitalsource.setGroupUse(false);
    capitalsource.setImportAllowed(CapitalsourceImport.NOT_ALLOWED);
    capitalsource.setState(CapitalsourceState.CACHE);
    capitalsource.setType(CapitalsourceType.CURRENT_ASSET);
    capitalsource.setValidFrom(LocalDate.now());
    capitalsource.setValidTil(LocalDate.now());
    this.capitalsourceService.createCapitalsource(capitalsource);
    final CapitalsourceTransport capitalsourceTransport = new CapitalsourceTransport();
    capitalsourceTransport.setId(CapitalsourceTransportBuilder.NEXT_ID);
    capitalsourceTransport.setUserid(UserTransportBuilder.USER1_ID);
    capitalsourceTransport.setType(CapitalsourceTypeMapper.map(capitalsource.getType()));
    capitalsourceTransport.setState(CapitalsourceStateMapper.map(capitalsource.getState()));
    capitalsourceTransport.setComment(capitalsource.getComment());
    capitalsourceTransport
        .setValidFrom(DateUtil.getGmtDate(capitalsource.getValidFrom().toString()));
    capitalsourceTransport.setValidTil(DateUtil.getGmtDate(capitalsource.getValidTil().toString()));
    capitalsourceTransport.setGroupUse(null);
    capitalsourceTransport
        .setImportAllowed(CapitalsourceImportMapper.map(capitalsource.getImportAllowed()));
    final ShowCapitalsourceListResponse expected = new ShowCapitalsourceListResponse();
    expected.setInitials(new HashSet<>(Arrays.asList('A', 'S', 'X', '_')));
    final List<CapitalsourceTransport> capitalsourceTransports = new ArrayList<>();
    capitalsourceTransports.add(capitalsourceTransport);
    expected.setCapitalsourceTransports(capitalsourceTransports);
    expected.setCurrentlyValid(false);
    final ShowCapitalsourceListResponse actual = super.callUsecaseWithoutContent(
        "/_/currentlyValid/0", this.method, false, ShowCapitalsourceListResponse.class);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_initialPercent_HttpStatus400NoContent() throws Exception {
    super.callUsecaseExpect400("/%", this.method);
  }

  @Test
  public void test_AuthorizationRequired1_Error() throws Exception {
    this.userName = null;
    this.userPassword = null;
    super.callUsecaseExpect403("/currentlyValid", this.method);
  }

  @Test
  public void test_AuthorizationRequired2_Error() throws Exception {
    this.userName = null;
    this.userPassword = null;
    super.callUsecaseExpect403("/all/currentlyValid", this.method);
  }

  @Test
  public void test_AuthorizationRequired3_Error() throws Exception {
    this.userName = null;
    this.userPassword = null;
    super.callUsecaseExpect403("/currentlyValid/0", this.method);
  }

  @Test
  public void test_AuthorizationRequired4_Error() throws Exception {
    this.userName = null;
    this.userPassword = null;
    super.callUsecaseExpect403("/all/currentlyValid/0", this.method);
  }

  @Test
  @Sql("classpath:h2defaults.sql")
  public void test_emptyDatabase_noException() throws Exception {
    this.userName = UserTransportBuilder.ADMIN_NAME;
    this.userPassword = UserTransportBuilder.ADMIN_PASSWORD;
    final ShowCapitalsourceListResponse expected = new ShowCapitalsourceListResponse();
    final ShowCapitalsourceListResponse actual = super.callUsecaseWithoutContent(
        "/all/currentlyValid/0", this.method, false, ShowCapitalsourceListResponse.class);
    Assertions.assertEquals(expected, actual);
  }
}
