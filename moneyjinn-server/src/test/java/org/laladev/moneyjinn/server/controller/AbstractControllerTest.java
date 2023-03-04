
package org.laladev.moneyjinn.server.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Inject;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.laladev.moneyjinn.AbstractTest;
import org.laladev.moneyjinn.server.config.JwtCache;
import org.laladev.moneyjinn.server.model.LoginRequest;
import org.laladev.moneyjinn.server.model.LoginResponse;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public abstract class AbstractControllerTest extends AbstractTest {
  @Inject
  private ObjectMapper objectMapper;
  @Inject
  private MockMvc mvc;
  @Inject
  private CacheManager cacheManager;
  @Inject
  private JwtCache jwtCache;
  private String overrideJwtToken;

  protected abstract String getUsername();

  protected abstract String getPassword();

  protected String getUsecase() {
    throw new UnsupportedOperationException("child class must implement this!");
  }

  protected Method getMethod() {
    throw new UnsupportedOperationException("child class must implement this!");
  }

  public void setOverrideJwtToken(final String overrideJwtToken) {
    this.overrideJwtToken = overrideJwtToken;
  }

  protected Method getMethodFromTestClassName(final Class<?> apiClazz, final Class<?> testClazz) {
    final String usecase = testClazz.getSimpleName().replace("Test", "");
    final String methodName = Character.toLowerCase(usecase.charAt(0)) + usecase.substring(1);

    final Collection<Method> methods = new ArrayList<>();
    Collections.addAll(methods, apiClazz.getDeclaredMethods());

    final Method method = methods.stream().filter(m -> methodName.equals(m.getName())).findFirst()
        .get();

    return method;
  }

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

  protected HttpHeaders getAuthorizationHeader() throws Exception {
    final HttpHeaders httpHeaders = new HttpHeaders();

    if (this.overrideJwtToken != null) {
      httpHeaders.add("Authorization", "Bearer " + this.overrideJwtToken);
      return httpHeaders;
    }

    final String username = this.getUsername();
    final String password = this.getPassword();
    if (username == null || password == null) {
      return httpHeaders;
    }
    final LoginRequest loginRequest = new LoginRequest();
    loginRequest.setUserName(username);
    loginRequest.setUserPassword(password);
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

  protected <T> T callUsecaseExpect200(final String uriParameters, final HttpMethod httpMethod,
      final Class<T> clazz) throws Exception {
    return this.callUsecase(uriParameters, httpMethod, "", false, clazz, HttpStatus.OK);
  }

  protected <T> T callUsecaseExpect200(final HttpMethod httpMethod, final Class<T> clazz)
      throws Exception {
    return this.callUsecase("", httpMethod, "", false, clazz, HttpStatus.OK);
  }

  protected <T> T callUsecaseExpect200(final HttpMethod httpMethod, final Object body,
      final Class<T> clazz) throws Exception {
    final String bodyStr = this.objectMapper.writeValueAsString(body);
    return this.callUsecase("", httpMethod, bodyStr, false, clazz, HttpStatus.OK);
  }

  protected <T> T callUsecaseExpect204(final HttpMethod httpMethod, final Object body)
      throws Exception {
    final String bodyStr = this.objectMapper.writeValueAsString(body);
    return this.callUsecase("", httpMethod, bodyStr, true, null, HttpStatus.NO_CONTENT);
  }

  protected <T> T callUsecaseExpect204(final String uriParameters, final HttpMethod httpMethod)
      throws Exception {
    return this.callUsecase(uriParameters, httpMethod, "", true, null, HttpStatus.NO_CONTENT);
  }

  protected <T> T callUsecaseExpect400(final HttpMethod httpMethod, final Object body,
      final Class<T> clazz) throws Exception {
    final String bodyStr = this.objectMapper.writeValueAsString(body);
    return this.callUsecase("", httpMethod, bodyStr, false, clazz, HttpStatus.BAD_REQUEST);
  }

  protected <T> T callUsecaseExpect400(final String uriParameters, final HttpMethod httpMethod,
      final Class<T> clazz) throws Exception {
    return this.callUsecase(uriParameters, httpMethod, "", false, clazz, HttpStatus.BAD_REQUEST);
  }

  protected void callUsecaseExpect403(final String uriParameters, final HttpMethod httpMethod)
      throws Exception {
    this.callUsecase(uriParameters, httpMethod, "", true, null, HttpStatus.FORBIDDEN);
  }

  protected void callUsecaseExpect403(final HttpMethod httpMethod) throws Exception {
    this.callUsecase("", httpMethod, "", true, null, HttpStatus.FORBIDDEN);
  }

  protected <T> T callUsecaseExpect403(final HttpMethod httpMethod, final Object body,
      final Class<T> clazz) throws Exception {
    final String bodyStr = this.objectMapper.writeValueAsString(body);
    return this.callUsecase("", httpMethod, bodyStr, false, clazz, HttpStatus.FORBIDDEN);
  }

  protected void callUsecaseExpect403(final HttpMethod httpMethod, final Object body)
      throws Exception {
    final String bodyStr = this.objectMapper.writeValueAsString(body);
    this.callUsecase("", httpMethod, bodyStr, true, null, HttpStatus.FORBIDDEN);
  }

  protected <T> T callUsecaseExpect422(final HttpMethod httpMethod, final Object body,
      final Class<T> clazz) throws Exception {
    final String bodyStr = this.objectMapper.writeValueAsString(body);
    return this.callUsecase("", httpMethod, bodyStr, false, clazz, HttpStatus.UNPROCESSABLE_ENTITY);
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
    return this.executeCall(noResult, clazz, status, builder);
  }

  private <T> T executeCall(final boolean noResult, final Class<T> clazz, final HttpStatus status,
      final MockHttpServletRequestBuilder builder) throws Exception, UnsupportedEncodingException,
      JsonProcessingException, JsonMappingException {

    builder.headers(this.getAuthorizationHeader());

    final MvcResult result = this.mvc.perform(builder.contentType(MediaType.APPLICATION_JSON)
        .with(SecurityMockMvcRequestPostProcessors.csrf()).accept(MediaType.APPLICATION_JSON)
        .characterEncoding(StandardCharsets.UTF_8.name())).andReturn();

    final String content = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
    Assertions.assertNotNull(content);
    Assertions.assertEquals(status.value(), result.getResponse().getStatus(), content);

    if (!noResult) {
      Assertions.assertTrue(content.length() > 0,
          "Response is empty, but 'noResult' was not specified!");
      final T actual = this.objectMapper.readValue(content, clazz);
      return actual;
    }

    Assertions.assertTrue(content.length() == 0);
    return null;
  }

  protected <T> T callUsecaseExpect200(final Object body, final Class<T> clazz) throws Exception {
    final String bodyStr = this.objectMapper.writeValueAsString(body);
    return this.callUsecaseNew(bodyStr, false, clazz, HttpStatus.OK);
  }

  protected <T> T callUsecaseExpect200(final Class<T> clazz) throws Exception {
    return this.callUsecaseNew(null, false, clazz, HttpStatus.OK);
  }

  protected <T> T callUsecaseExpect200(final Class<T> clazz, final Object... uriVariables)
      throws Exception {
    return this.callUsecaseNew(null, false, clazz, HttpStatus.OK, uriVariables);
  }

  protected <T> T callUsecaseExpect204(final Object body) throws Exception {
    final String bodyStr = this.objectMapper.writeValueAsString(body);
    return this.callUsecaseNew(bodyStr, true, null, HttpStatus.NO_CONTENT);
  }

  protected <T> T callUsecaseExpect204WithUriVariables(final Object... uriVariables)
      throws Exception {
    return this.callUsecaseNew(null, true, null, HttpStatus.NO_CONTENT, uriVariables);
  }

  protected <T> T callUsecaseExpect400(final Object body, final Class<T> clazz) throws Exception {
    final String bodyStr = this.objectMapper.writeValueAsString(body);
    return this.callUsecaseNew(bodyStr, false, clazz, HttpStatus.BAD_REQUEST);
  }

  protected <T> T callUsecaseExpect400(final Class<T> clazz, final Object... uriVariables)
      throws Exception {
    return this.callUsecaseNew(null, false, clazz, HttpStatus.BAD_REQUEST, uriVariables);
  }

  protected void callUsecaseExpect403() throws Exception {
    this.callUsecaseNew(null, true, null, HttpStatus.FORBIDDEN);
  }

  protected void callUsecaseExpect403WithUriVariables(final Object... uriVariables)
      throws Exception {
    this.callUsecaseNew(null, true, null, HttpStatus.FORBIDDEN, uriVariables);
  }

  protected void callUsecaseExpect403(final Object body) throws Exception {
    final String bodyStr = this.objectMapper.writeValueAsString(body);
    this.callUsecaseNew(bodyStr, true, null, HttpStatus.FORBIDDEN);
  }

  protected <T> T callUsecaseExpect422(final Object body, final Class<T> clazz) throws Exception {
    final String bodyStr = this.objectMapper.writeValueAsString(body);
    return this.callUsecaseNew(bodyStr, false, clazz, HttpStatus.UNPROCESSABLE_ENTITY);
  }

  private <T> T callUsecaseNew(final String body, final boolean noResult,
      final Class<T> responseClazz, final HttpStatus expectedHttpStatus,
      final Object... uriVariables) throws Exception {

    final boolean myNoResult = responseClazz == null;

    final Method method = this.getMethod();

    final RequestMapping requestMapping = AnnotatedElementUtils.findMergedAnnotation(method,
        RequestMapping.class);

    if (requestMapping == null) {
      throw new IllegalArgumentException("No @RequestMapping on: " + method.toGenericString());
    }
    final String[] paths = requestMapping.path();
    final RequestMethod[] requestMethods = requestMapping.method();
    final String path = paths[0];
    final RequestMethod requestMethod = requestMethods[0];

    MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
        .request(HttpMethod.valueOf(requestMethod.name()), path, uriVariables);

    if (body != null) {
      builder = builder.content(body);
    }

    return this.executeCall(myNoResult, responseClazz, expectedHttpStatus, builder);
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
