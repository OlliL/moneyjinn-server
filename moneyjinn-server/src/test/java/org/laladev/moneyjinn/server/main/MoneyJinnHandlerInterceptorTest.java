
package org.laladev.moneyjinn.server.main;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.AbstractTest;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.core.rest.model.ErrorResponse;
import org.laladev.moneyjinn.core.rest.model.user.GetUserSettingsForStartupResponse;
import org.laladev.moneyjinn.core.rest.util.RESTAuthorization;
import org.laladev.moneyjinn.server.builder.HttpHeadersBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

public class MoneyJinnHandlerInterceptorTest extends AbstractTest {
  @Autowired
  private ObjectMapper objectMapper;
  @Autowired
  private MockMvc mvc;
  @Autowired
  private HttpHeadersBuilder httpHeadersBuilder;

  private <T> T callUsecase(final ZonedDateTime dateTime, final String userName,
      final String userPassword, final boolean isError, final Class<T> clazz,
      HttpHeaders httpHeaders) throws Exception {
    final String uri = "/moneyflow/server/user/getUserSettingsForStartup/admin";
    if (httpHeaders == null) {
      httpHeaders = this.httpHeadersBuilder.getAuthHeaders(userName, userPassword, dateTime, uri,
          "", HttpMethod.GET);
    }
    ResultMatcher status = status().isOk();
    if (isError) {
      status = status().isForbidden();
    }
    final MvcResult result = this.mvc
        .perform(MockMvcRequestBuilders.get(uri).headers(httpHeaders)
            .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
        .andExpect(status).andReturn();
    final String content = result.getResponse().getContentAsString();
    Assertions.assertNotNull(content);
    Assertions.assertTrue(content.length() > 0);
    final T actual = this.objectMapper.readValue(content, clazz);
    return actual;
  }

  @Test
  public void test_validLogin_RegularResponse() throws Exception {
    final ZonedDateTime zonedDateTime = ZonedDateTime.now();
    final String userName = UserTransportBuilder.ADMIN_NAME;
    final String userPassword = UserTransportBuilder.ADMIN_PASSWORD;
    final GetUserSettingsForStartupResponse response = this.callUsecase(zonedDateTime, userName,
        userPassword, false, GetUserSettingsForStartupResponse.class, null);
    Assertions.assertNotNull(response);
  }

  @Test
  public void test_wrongPassword_ErrorResponse() throws Exception {
    final ZonedDateTime zonedDateTime = ZonedDateTime.now();
    final String userName = UserTransportBuilder.ADMIN_NAME;
    final String userPassword = "Wrong Password";
    final ErrorResponse response = this.callUsecase(zonedDateTime, userName, userPassword, true,
        ErrorResponse.class, null);
    Assertions.assertNotNull(response);
    Assertions.assertEquals(Integer.valueOf(ErrorCode.USERNAME_PASSWORD_WRONG.getErrorCode()),
        response.getCode());
  }

  @Test
  public void test_wrongUsername_ErrorResponse() throws Exception {
    final ZonedDateTime zonedDateTime = ZonedDateTime.now();
    final String userName = "Non Existing";
    final String userPassword = UserTransportBuilder.ADMIN_PASSWORD;
    final ErrorResponse response = this.callUsecase(zonedDateTime, userName, userPassword, true,
        ErrorResponse.class, null);
    Assertions.assertNotNull(response);
    Assertions.assertEquals(Integer.valueOf(ErrorCode.USERNAME_PASSWORD_WRONG.getErrorCode()),
        response.getCode());
  }

  @Test
  public void test_TimeToEarly_ErrorResponse() throws Exception {
    final ZonedDateTime zonedDateTime = ZonedDateTime.now().minusMinutes(16l);
    final String userName = UserTransportBuilder.ADMIN_NAME;
    final String userPassword = UserTransportBuilder.ADMIN_PASSWORD;
    final ErrorResponse response = this.callUsecase(zonedDateTime, userName, userPassword, true,
        ErrorResponse.class, null);
    Assertions.assertNotNull(response);
    Assertions.assertEquals(Integer.valueOf(ErrorCode.CLIENT_CLOCK_OFF.getErrorCode()),
        response.getCode());
  }

  @Test
  public void test_TimeToLate_ErrorResponse() throws Exception {
    final ZonedDateTime zonedDateTime = ZonedDateTime.now().plusMinutes(17l);
    final String userName = UserTransportBuilder.ADMIN_NAME;
    final String userPassword = UserTransportBuilder.ADMIN_PASSWORD;
    final ErrorResponse response = this.callUsecase(zonedDateTime, userName, userPassword, true,
        ErrorResponse.class, null);
    Assertions.assertNotNull(response);
    Assertions.assertEquals(Integer.valueOf(ErrorCode.CLIENT_CLOCK_OFF.getErrorCode()),
        response.getCode());
  }

  @Test
  public void test_NoAuthHeader_ErrorResponse() throws Exception {
    final ZonedDateTime zonedDateTime = ZonedDateTime.now().plusMinutes(17l);
    final String userName = UserTransportBuilder.ADMIN_NAME;
    final String userPassword = UserTransportBuilder.ADMIN_PASSWORD;
    final HttpHeaders httpHeaders = this.httpHeadersBuilder.getDateHeader(zonedDateTime);
    final ErrorResponse response = this.callUsecase(zonedDateTime, userName, userPassword, true,
        ErrorResponse.class, httpHeaders);
    Assertions.assertNotNull(response);
    Assertions.assertEquals(Integer.valueOf(ErrorCode.LOGGED_OUT.getErrorCode()),
        response.getCode());
  }

  @Test
  public void test_NoUserInAuthHeader_ErrorResponse() throws Exception {
    final ZonedDateTime zonedDateTime = ZonedDateTime.now();
    final String userName = UserTransportBuilder.ADMIN_NAME;
    final String userPassword = UserTransportBuilder.ADMIN_PASSWORD;
    final HttpHeaders httpHeaders = this.httpHeadersBuilder.getDateHeader(zonedDateTime);
    httpHeaders.add(RESTAuthorization.AUTH_HEADER_NAME,
        RESTAuthorization.AUTH_HEADER_PREFIX + RESTAuthorization.AUTH_HEADER_SEPERATOR + "aaaaa");
    final ErrorResponse response = this.callUsecase(zonedDateTime, userName, userPassword, true,
        ErrorResponse.class, httpHeaders);
    Assertions.assertNotNull(response);
    Assertions.assertEquals(Integer.valueOf(ErrorCode.LOGGED_OUT.getErrorCode()),
        response.getCode());
  }

  @Test
  public void test_NoTokenInAuthHeader_ErrorResponse() throws Exception {
    final ZonedDateTime zonedDateTime = ZonedDateTime.now();
    final String userName = UserTransportBuilder.ADMIN_NAME;
    final String userPassword = UserTransportBuilder.ADMIN_PASSWORD;
    final HttpHeaders httpHeaders = this.httpHeadersBuilder.getDateHeader(zonedDateTime);
    httpHeaders.add(RESTAuthorization.AUTH_HEADER_NAME,
        RESTAuthorization.AUTH_HEADER_PREFIX + "klaus" + RESTAuthorization.AUTH_HEADER_SEPERATOR);
    final ErrorResponse response = this.callUsecase(zonedDateTime, userName, userPassword, true,
        ErrorResponse.class, httpHeaders);
    Assertions.assertNotNull(response);
    Assertions.assertEquals(Integer.valueOf(ErrorCode.LOGGED_OUT.getErrorCode()),
        response.getCode());
  }

  @Test
  public void test_wrongPrefix_ErrorResponse() throws Exception {
    final ZonedDateTime zonedDateTime = ZonedDateTime.now();
    final String userName = UserTransportBuilder.ADMIN_NAME;
    final String userPassword = UserTransportBuilder.ADMIN_PASSWORD;
    final HttpHeaders httpHeaders = this.httpHeadersBuilder.getDateHeader(zonedDateTime);
    httpHeaders.add(RESTAuthorization.AUTH_HEADER_NAME,
        "XXX" + "klaus" + RESTAuthorization.AUTH_HEADER_SEPERATOR + "aaa");
    final ErrorResponse response = this.callUsecase(zonedDateTime, userName, userPassword, true,
        ErrorResponse.class, httpHeaders);
    Assertions.assertNotNull(response);
    Assertions.assertEquals(Integer.valueOf(ErrorCode.LOGGED_OUT.getErrorCode()),
        response.getCode());
  }

  @Test
  public void test_missingSeparator_ErrorResponse() throws Exception {
    final ZonedDateTime zonedDateTime = ZonedDateTime.now();
    final String userName = UserTransportBuilder.ADMIN_NAME;
    final String userPassword = UserTransportBuilder.ADMIN_PASSWORD;
    final HttpHeaders httpHeaders = this.httpHeadersBuilder.getDateHeader(zonedDateTime);
    httpHeaders.add(RESTAuthorization.AUTH_HEADER_NAME,
        RESTAuthorization.AUTH_HEADER_PREFIX + "klausaaa");
    final ErrorResponse response = this.callUsecase(zonedDateTime, userName, userPassword, true,
        ErrorResponse.class, httpHeaders);
    Assertions.assertNotNull(response);
    Assertions.assertEquals(Integer.valueOf(ErrorCode.LOGGED_OUT.getErrorCode()),
        response.getCode());
  }

  @Test
  public void test_onlyPrefix_ErrorResponse() throws Exception {
    final ZonedDateTime zonedDateTime = ZonedDateTime.now();
    final String userName = UserTransportBuilder.ADMIN_NAME;
    final String userPassword = UserTransportBuilder.ADMIN_PASSWORD;
    final HttpHeaders httpHeaders = this.httpHeadersBuilder.getDateHeader(zonedDateTime);
    httpHeaders.add(RESTAuthorization.AUTH_HEADER_NAME, RESTAuthorization.AUTH_HEADER_PREFIX);
    final ErrorResponse response = this.callUsecase(zonedDateTime, userName, userPassword, true,
        ErrorResponse.class, httpHeaders);
    Assertions.assertNotNull(response);
    Assertions.assertEquals(Integer.valueOf(ErrorCode.LOGGED_OUT.getErrorCode()),
        response.getCode());
  }

  @Test
  public void test_emptyAuthHeader_ErrorResponse() throws Exception {
    final ZonedDateTime zonedDateTime = ZonedDateTime.now();
    final String userName = UserTransportBuilder.ADMIN_NAME;
    final String userPassword = UserTransportBuilder.ADMIN_PASSWORD;
    final HttpHeaders httpHeaders = this.httpHeadersBuilder.getDateHeader(zonedDateTime);
    httpHeaders.add(RESTAuthorization.AUTH_HEADER_NAME, "");
    final ErrorResponse response = this.callUsecase(zonedDateTime, userName, userPassword, true,
        ErrorResponse.class, httpHeaders);
    Assertions.assertNotNull(response);
    Assertions.assertEquals(Integer.valueOf(ErrorCode.LOGGED_OUT.getErrorCode()),
        response.getCode());
  }

  @Test
  public void test_onlySeperator_ErrorResponse() throws Exception {
    final ZonedDateTime zonedDateTime = ZonedDateTime.now();
    final String userName = UserTransportBuilder.ADMIN_NAME;
    final String userPassword = UserTransportBuilder.ADMIN_PASSWORD;
    final HttpHeaders httpHeaders = this.httpHeadersBuilder.getDateHeader(zonedDateTime);
    httpHeaders.add(RESTAuthorization.AUTH_HEADER_NAME,
        RESTAuthorization.AUTH_HEADER_PREFIX + RESTAuthorization.AUTH_HEADER_SEPERATOR);
    final ErrorResponse response = this.callUsecase(zonedDateTime, userName, userPassword, true,
        ErrorResponse.class, httpHeaders);
    Assertions.assertNotNull(response);
    Assertions.assertEquals(Integer.valueOf(ErrorCode.LOGGED_OUT.getErrorCode()),
        response.getCode());
  }

  @Test
  public void test_LoginNotAllowed_ErrorResponse() throws Exception {
    final ZonedDateTime zonedDateTime = ZonedDateTime.now();
    final String userName = UserTransportBuilder.USER2_NAME;
    final String userPassword = UserTransportBuilder.USER2_PASSWORD;
    final ErrorResponse response = this.callUsecase(zonedDateTime, userName, userPassword, true,
        ErrorResponse.class, null);
    Assertions.assertNotNull(response);
    Assertions.assertEquals(Integer.valueOf(ErrorCode.ACCOUNT_IS_LOCKED.getErrorCode()),
        response.getCode());
  }
}
