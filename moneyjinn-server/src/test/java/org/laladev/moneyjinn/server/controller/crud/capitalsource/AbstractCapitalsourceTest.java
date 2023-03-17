package org.laladev.moneyjinn.server.controller.crud.capitalsource;

import org.junit.jupiter.api.BeforeEach;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.laladev.moneyjinn.server.controller.api.CrudCapitalsourceControllerApi;

abstract class AbstractCapitalsourceTest extends AbstractControllerTest {

  @BeforeEach
  public void setUp() {
    super.setUsername(UserTransportBuilder.USER1_NAME);
    super.setPassword(UserTransportBuilder.USER1_PASSWORD);
  }

  protected CrudCapitalsourceControllerApi getMock() {
    return super.getMock(CrudCapitalsourceControllerApi.class);
  }
}
