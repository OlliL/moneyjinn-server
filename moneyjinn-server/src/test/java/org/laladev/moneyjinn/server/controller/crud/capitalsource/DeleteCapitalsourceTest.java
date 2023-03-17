
package org.laladev.moneyjinn.server.controller.crud.capitalsource;

import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.model.access.GroupID;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.capitalsource.Capitalsource;
import org.laladev.moneyjinn.model.capitalsource.CapitalsourceID;
import org.laladev.moneyjinn.server.builder.CapitalsourceTransportBuilder;
import org.laladev.moneyjinn.server.builder.GroupTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.model.ErrorResponse;
import org.laladev.moneyjinn.service.api.ICapitalsourceService;
import org.springframework.test.context.jdbc.Sql;

class DeleteCapitalsourceTest extends AbstractCapitalsourceTest {
  @Inject
  private ICapitalsourceService capitalsourceService;

  @Override
  protected void loadMethod() {
    super.getMock().delete(null);
  }

  @Test
  void test_regularCapitalsourceNoData_SuccessfullNoContent() throws Exception {
    super.setUsername(UserTransportBuilder.USER3_NAME);
    super.setPassword(UserTransportBuilder.USER3_PASSWORD);
    final UserID userId = new UserID(UserTransportBuilder.USER3_ID);
    final GroupID groupId = new GroupID(GroupTransportBuilder.GROUP1_ID);
    final CapitalsourceID capitalsourceId = new CapitalsourceID(
        CapitalsourceTransportBuilder.CAPITALSOURCE3_ID);
    Capitalsource capitalsource = this.capitalsourceService.getCapitalsourceById(userId, groupId,
        capitalsourceId);
    Assertions.assertNotNull(capitalsource);

    super.callUsecaseExpect204WithUriVariables(CapitalsourceTransportBuilder.CAPITALSOURCE3_ID);

    capitalsource = this.capitalsourceService.getCapitalsourceById(userId, groupId,
        capitalsourceId);
    Assertions.assertNull(capitalsource);
  }

  @Test
  void test_nonExistingCapitalsource_SuccessfullNoContent() throws Exception {
    final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
    final GroupID groupId = new GroupID(GroupTransportBuilder.GROUP1_ID);
    final CapitalsourceID capitalsourceId = new CapitalsourceID(
        CapitalsourceTransportBuilder.NON_EXISTING_ID);
    Capitalsource capitalsource = this.capitalsourceService.getCapitalsourceById(userId, groupId,
        capitalsourceId);
    Assertions.assertNull(capitalsource);

    super.callUsecaseExpect204WithUriVariables(CapitalsourceTransportBuilder.NON_EXISTING_ID);

    capitalsource = this.capitalsourceService.getCapitalsourceById(userId, groupId,
        capitalsourceId);
    Assertions.assertNull(capitalsource);
  }

  @Test
  void test_regularCapitalsourceWithData_ErrorResponse() throws Exception {
    final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
    final GroupID groupId = new GroupID(GroupTransportBuilder.GROUP1_ID);
    final CapitalsourceID capitalsourceId = new CapitalsourceID(
        CapitalsourceTransportBuilder.CAPITALSOURCE1_ID);
    final ErrorResponse expected = new ErrorResponse();
    expected.setCode(ErrorCode.CAPITALSOURCE_STILL_REFERENCED.getErrorCode());
    expected.setMessage(
        "You may not delete a source of capital while it is referenced by a flow of money!");
    Capitalsource capitalsource = this.capitalsourceService.getCapitalsourceById(userId, groupId,
        capitalsourceId);
    Assertions.assertNotNull(capitalsource);

    final ErrorResponse actual = super.callUsecaseExpect400(ErrorResponse.class,
        CapitalsourceTransportBuilder.CAPITALSOURCE1_ID);

    capitalsource = this.capitalsourceService.getCapitalsourceById(userId, groupId,
        capitalsourceId);
    Assertions.assertNotNull(capitalsource);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  void test_deleteCapitalsourceOwnedBySomeoneElse_notSuccessfull() throws Exception {
    final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
    final GroupID groupId = new GroupID(GroupTransportBuilder.GROUP1_ID);
    final CapitalsourceID capitalsourceId = new CapitalsourceID(
        CapitalsourceTransportBuilder.CAPITALSOURCE3_ID);
    Capitalsource capitalsource = this.capitalsourceService.getCapitalsourceById(userId, groupId,
        capitalsourceId);
    Assertions.assertNotNull(capitalsource);

    super.callUsecaseExpect204WithUriVariables(CapitalsourceTransportBuilder.CAPITALSOURCE3_ID);
    capitalsource = this.capitalsourceService.getCapitalsourceById(userId, groupId,
        capitalsourceId);

    Assertions.assertNotNull(capitalsource);
  }

  @Test
  void test_AuthorizationRequired_Error() throws Exception {
    super.setUsername(null);
    super.setPassword(null);

    super.callUsecaseExpect403WithUriVariables(CapitalsourceTransportBuilder.NON_EXISTING_ID);
  }

  @Test
  @Sql("classpath:h2defaults.sql")
  void test_emptyDatabase_noException() throws Exception {
    super.setUsername(UserTransportBuilder.ADMIN_NAME);
    super.setPassword(UserTransportBuilder.ADMIN_PASSWORD);

    super.callUsecaseExpect204WithUriVariables(CapitalsourceTransportBuilder.NON_EXISTING_ID);
  }
}