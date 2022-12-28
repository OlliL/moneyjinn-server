
package org.laladev.moneyjinn.server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Inject;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.laladev.moneyjinn.AbstractTest;
import org.laladev.moneyjinn.core.rest.model.user.LoginRequest;
import org.laladev.moneyjinn.core.rest.model.user.LoginResponse;
import org.laladev.moneyjinn.server.config.JwtCache;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

public abstract class AbstractControllerTest extends AbstractTest {
  @Inject
  private ObjectMapper objectMapper;
  @Inject
  private MockMvc mvc;
  @Inject
  private CacheManager cacheManager;
  @Inject
  private JwtCache jwtCache;

  protected abstract String getUsername();

  protected abstract String getPassword();

  protected abstract String getUsecase();

  /**
   * Returns the Usecase URL for the given class. Input "ShowEditUserTest.class" and get
   * "user/showEditUser" back.
   *
   * @param clazz
   *                The Usecase-Class
   * @return Usecase-URL
   */
  protected String getUsecaseFromTestClassName(final Class<?> clazz) {
    final String usecase = clazz.getSimpleName().replace("Test", "");
    final String packageName = clazz.getPackage().getName();
    final String module = packageName.substring(packageName.lastIndexOf(".") + 1);
    return module + "/" + Character.toLowerCase(usecase.charAt(0)) + usecase.substring(1);
  }

  private HttpHeaders getAuthorizationHeader() throws Exception {
    final String username = this.getUsername();
    final String password = this.getPassword();
    final HttpHeaders httpHeaders = new HttpHeaders();
    if (username == null || password == null) {
      return httpHeaders;
    }
    final LoginRequest loginRequest = new LoginRequest();
    loginRequest.setUserName(this.getUsername());
    loginRequest.setUserPassword(this.getPassword());
    String jwtToken = this.jwtCache.getJwt(loginRequest);
    if (jwtToken == null) {
      final String uri = "/moneyflow/server/user/login";
      final String body = this.objectMapper.writeValueAsString(loginRequest);
      final MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post(uri).content(body);
      final MvcResult result = this.mvc
          .perform(builder.contentType(MediaType.APPLICATION_JSON)
              .accept(MediaType.APPLICATION_JSON).characterEncoding(StandardCharsets.UTF_8.name()))
          .andReturn();
      final String content = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
      Assertions.assertNotNull(content);
      Assertions.assertTrue(content.length() > 0);
      final LoginResponse actual = this.objectMapper.readValue(content, LoginResponse.class);
      jwtToken = actual.getToken();
      this.jwtCache.putJwt(loginRequest, jwtToken);
    }
    httpHeaders.add("Authorization", "Bearer " + jwtToken);
    return httpHeaders;
  }

  protected void callUsecaseExpect403(final String uriParameters, final HttpMethod httpMethod)
      throws Exception {
    this.callUsecase(uriParameters, httpMethod, "", true, null, HttpStatus.FORBIDDEN);
  }

  protected <T> T callUsecaseExpect403(final String uriParameters, final HttpMethod httpMethod,
      final Object body, final Class<T> clazz) throws Exception {
    final String bodyStr = this.objectMapper.writeValueAsString(body);
    return this.callUsecase(uriParameters, httpMethod, bodyStr, false, clazz, HttpStatus.FORBIDDEN);
  }

  protected <T> T callUsecaseWithoutContent(final String uriParameters, final HttpMethod httpMethod,
      final boolean noResult, final Class<T> clazz) throws Exception {
    HttpStatus status = HttpStatus.OK;
    if (noResult) {
      status = HttpStatus.NO_CONTENT;
    }
    return this.callUsecase(uriParameters, httpMethod, "", noResult, clazz, status);
  }

  protected <T> T callUsecaseWithContent(final String uriParameters, final HttpMethod httpMethod,
      final Object body, final boolean noResult, final Class<T> clazz) throws Exception {
    final String bodyStr = this.objectMapper.writeValueAsString(body);
    HttpStatus status = HttpStatus.OK;
    if (noResult) {
      status = HttpStatus.NO_CONTENT;
    }
    return this.callUsecase(uriParameters, httpMethod, bodyStr, noResult, clazz, status);
  }

  private <T> T callUsecase(final String uriParameters, final HttpMethod httpMethod,
      final String body, final boolean noResult, final Class<T> clazz, final HttpStatus status)
      throws Exception {
    MockHttpServletRequestBuilder builder = null;
    final String uri = "/moneyflow/server/" + this.getUsecase() + uriParameters;
    switch (httpMethod.name()) {
      case "GET":
        builder = MockMvcRequestBuilders.get(uri);
        break;
      case "DELETE":
        builder = MockMvcRequestBuilders.delete(uri);
        break;
      case "PUT":
        builder = MockMvcRequestBuilders.put(uri).content(body);
        break;
      case "POST":
        builder = MockMvcRequestBuilders.post(uri).content(body);
        break;
      case "OPTIONS":
        builder = MockMvcRequestBuilders.options(uri);
        break;
      default:
        throw new UnsupportedOperationException("httpMethod " + httpMethod + " not supported");
    }
    builder.headers(this.getAuthorizationHeader());
    final MvcResult result = this.mvc.perform(builder.contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON).characterEncoding(StandardCharsets.UTF_8.name()))
        .andReturn();
    final String content = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
    Assertions.assertNotNull(content);
    Assertions.assertEquals(status.value(), result.getResponse().getStatus(), content);
    if (!noResult) {
      Assertions.assertTrue(content.length() > 0);
      final T actual = this.objectMapper.readValue(content, clazz);
      return actual;
    }
    Assertions.assertTrue(content.length() == 0);
    return null;
  }

  /**
   * Clears all Caches before each test execution.
   */
  @Override
  @BeforeEach
  public void before() {
    this.cacheManager.getCacheNames().stream().map(this.cacheManager::getCache)
        .forEach(Cache::clear);
  }
}
