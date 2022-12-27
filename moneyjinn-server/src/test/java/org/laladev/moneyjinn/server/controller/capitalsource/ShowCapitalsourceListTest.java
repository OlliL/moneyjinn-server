
package org.laladev.moneyjinn.server.controller.capitalsource;

import jakarta.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.core.rest.model.capitalsource.ShowCapitalsourceListResponse;
import org.laladev.moneyjinn.core.rest.model.transport.CapitalsourceTransport;
import org.laladev.moneyjinn.server.builder.CapitalsourceTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.laladev.moneyjinn.service.api.ICapitalsourceService;
import org.laladev.moneyjinn.service.impl.SettingService;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.jdbc.Sql;

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
    final List<CapitalsourceTransport> capitalsourceTransports = new ArrayList<>();
    capitalsourceTransports.add(new CapitalsourceTransportBuilder().forCapitalsource1().build());
    capitalsourceTransports.add(new CapitalsourceTransportBuilder().forCapitalsource2().build());
    capitalsourceTransports.add(new CapitalsourceTransportBuilder().forCapitalsource3().build());
    capitalsourceTransports.add(new CapitalsourceTransportBuilder().forCapitalsource4().build());
    capitalsourceTransports.add(new CapitalsourceTransportBuilder().forCapitalsource5().build());
    capitalsourceTransports.add(new CapitalsourceTransportBuilder().forCapitalsource6().build());
    expected.setCapitalsourceTransports(capitalsourceTransports);
    return expected;
  }

  @Test
  public void test_FullResponseObject() throws Exception {
    final ShowCapitalsourceListResponse expected = this.getCompleteResponse();
    final ShowCapitalsourceListResponse actual = super.callUsecaseWithoutContent("", this.method,
        false, ShowCapitalsourceListResponse.class);
    Assertions.assertEquals(expected, actual);

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
    final ShowCapitalsourceListResponse expected = new ShowCapitalsourceListResponse();
    final ShowCapitalsourceListResponse actual = super.callUsecaseWithoutContent("", this.method,
        false, ShowCapitalsourceListResponse.class);
    Assertions.assertEquals(expected, actual);
  }
}
