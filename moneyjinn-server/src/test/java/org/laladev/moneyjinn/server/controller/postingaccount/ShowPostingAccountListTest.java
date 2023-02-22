
package org.laladev.moneyjinn.server.controller.postingaccount;

import jakarta.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.server.builder.PostingAccountTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.laladev.moneyjinn.server.model.PostingAccountTransport;
import org.laladev.moneyjinn.server.model.ShowPostingAccountListResponse;
import org.laladev.moneyjinn.service.api.IPostingAccountService;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.jdbc.Sql;

public class ShowPostingAccountListTest extends AbstractControllerTest {
  @Inject
  private IPostingAccountService postingAccountService;
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

  private ShowPostingAccountListResponse getCompleteResponse() {
    final ShowPostingAccountListResponse expected = new ShowPostingAccountListResponse();
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
    final ShowPostingAccountListResponse actual = super.callUsecaseExpect200(this.method,
        ShowPostingAccountListResponse.class);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_AuthorizationRequired1_Error() throws Exception {
    this.userName = null;
    this.userPassword = null;
    super.callUsecaseExpect403("", this.method);
  }

  @Test
  @Sql("classpath:h2defaults.sql")
  public void test_emptyDatabase_noException() throws Exception {
    this.userName = UserTransportBuilder.ADMIN_NAME;
    this.userPassword = UserTransportBuilder.ADMIN_PASSWORD;
    final ShowPostingAccountListResponse expected = new ShowPostingAccountListResponse();
    final ShowPostingAccountListResponse actual = super.callUsecaseExpect200(this.method,
        ShowPostingAccountListResponse.class);
    Assertions.assertEquals(expected, actual);
  }
}
