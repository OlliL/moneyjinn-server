
package org.laladev.moneyjinn.server.controller.crud.capitalsource;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.server.builder.CapitalsourceTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.model.CapitalsourceTransport;
import org.springframework.test.context.jdbc.Sql;

class ReadAllCapitalsourceTest extends AbstractCapitalsourceTest {

  @Override
  protected void loadMethod() {
    super.getMock().readAll();
  }

  private List<CapitalsourceTransport> getCompleteResponse() {
    final List<CapitalsourceTransport> capitalsourceTransports = new ArrayList<>();
    capitalsourceTransports.add(new CapitalsourceTransportBuilder().forCapitalsource1().build());
    capitalsourceTransports.add(new CapitalsourceTransportBuilder().forCapitalsource2().build());
    capitalsourceTransports.add(new CapitalsourceTransportBuilder().forCapitalsource3().build());
    capitalsourceTransports.add(new CapitalsourceTransportBuilder().forCapitalsource4().build());
    capitalsourceTransports.add(new CapitalsourceTransportBuilder().forCapitalsource5().build());
    capitalsourceTransports.add(new CapitalsourceTransportBuilder().forCapitalsource6().build());
    return capitalsourceTransports;
  }

  @Test
  void test_FullResponseObject() throws Exception {
    final List<CapitalsourceTransport> expected = this.getCompleteResponse();

    final CapitalsourceTransport[] actual = super.callUsecaseExpect200(
        CapitalsourceTransport[].class);

    Assertions.assertArrayEquals(expected.toArray(), actual);

  }

  @Test
  void test_AuthorizationRequired_Error() throws Exception {
    super.setUsername(null);
    super.setPassword(null);

    super.callUsecaseExpect403(null);
  }

  @Test
  @Sql("classpath:h2defaults.sql")
  void test_emptyDatabase_noException() throws Exception {
    super.setUsername(UserTransportBuilder.ADMIN_NAME);
    super.setPassword(UserTransportBuilder.ADMIN_PASSWORD);

    final CapitalsourceTransport[] expected = {};
    final CapitalsourceTransport[] actual = super.callUsecaseExpect200(
        CapitalsourceTransport[].class);

    Assertions.assertArrayEquals(expected, actual);
  }
}
