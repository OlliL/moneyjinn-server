
package org.laladev.moneyjinn.server.controller.capitalsource;

import jakarta.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.server.builder.CapitalsourceTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.laladev.moneyjinn.server.controller.api.CapitalsourceControllerApi;
import org.laladev.moneyjinn.server.model.CapitalsourceTransport;
import org.laladev.moneyjinn.server.model.ShowCapitalsourceListResponse;
import org.laladev.moneyjinn.service.api.ICapitalsourceService;
import org.springframework.test.context.jdbc.Sql;

class ShowCapitalsourceListTest extends AbstractControllerTest {
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
  protected void loadMethod() {
    super.getMock(CapitalsourceControllerApi.class).showCapitalsourceList();
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
   void test_FullResponseObject() throws Exception {
    final ShowCapitalsourceListResponse expected = this.getCompleteResponse();

    final ShowCapitalsourceListResponse actual = super.callUsecaseExpect200(
        ShowCapitalsourceListResponse.class);

    Assertions.assertEquals(expected, actual);

  }

  @Test
   void test_AuthorizationRequired_Error() throws Exception {
    this.userName = null;
    this.userPassword = null;

    super.callUsecaseExpect403(new ShowCapitalsourceListResponse());
  }

  @Test
  @Sql("classpath:h2defaults.sql")
  void test_emptyDatabase_noException() throws Exception {
    this.userName = UserTransportBuilder.ADMIN_NAME;
    this.userPassword = UserTransportBuilder.ADMIN_PASSWORD;
    final ShowCapitalsourceListResponse expected = new ShowCapitalsourceListResponse();

    final ShowCapitalsourceListResponse actual = super.callUsecaseExpect200(
        ShowCapitalsourceListResponse.class);

    Assertions.assertEquals(expected, actual);
  }
}
