
package org.laladev.moneyjinn.server.controller;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.StreamSupport;

import org.junit.jupiter.api.Assertions;
import org.laladev.moneyjinn.AbstractTest;
import org.laladev.moneyjinn.config.JwtCache;
import org.laladev.moneyjinn.server.controller.api.UserControllerApi;
import org.laladev.moneyjinn.server.model.LoginRequest;
import org.laladev.moneyjinn.server.model.LoginResponse;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.inject.Inject;

public abstract class AbstractControllerTest extends AbstractTest {
	protected static final String HEADER_PREFER = "Prefer";
	private static final String HEADER_PREFERENCE_APPLIED = "Preference-Applied";
	private static final String RETURN = "return=";
	private static final String RETURN_MINIMAL = RETURN + "minimal";
	private static final String RETURN_REPRESENTATION = RETURN + "representation";

	private static final HttpHeaders HEADER_RETURN_MINIMAL = new HttpHeaders();
	private static final HttpHeaders HEADER_RETURN_REPRESENTATION = new HttpHeaders();

	@Inject
	private ObjectMapper objectMapper;
	@Inject
	private MockMvc mvc;
	@Inject
	private JwtCache jwtCache;
	private String overrideJwtToken;
	private Method method = null;
	private String userName;
	private String userPassword;

	{
		HEADER_RETURN_MINIMAL.add(HEADER_PREFER, RETURN_MINIMAL);
		HEADER_RETURN_REPRESENTATION.add(HEADER_PREFER, RETURN_REPRESENTATION);
	}

	protected abstract void loadMethod();

	protected void setUsername(final String userName) {
		this.userName = userName;
	}

	protected void setPassword(final String password) {
		this.userPassword = password;
	}

	protected void setOverrideJwtToken(final String overrideJwtToken) {
		this.overrideJwtToken = overrideJwtToken;
	}

	protected <T> T getMock(final Class<T> clazz) {
		return Mockito.mock(clazz, Mockito.withSettings().invocationListeners(methodInvocationReport -> {
			this.method = ((InvocationOnMock) methodInvocationReport.getInvocation()).getMethod();
		}));
	}

	protected HttpHeaders getAuthorizationHeader() throws Exception {
		final HttpHeaders httpHeaders = new HttpHeaders();

		if (this.overrideJwtToken != null) {
			httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + this.overrideJwtToken);
			return httpHeaders;
		}

		if (this.userName == null || this.userPassword == null) {
			return httpHeaders;
		}

		final LoginRequest loginRequest = new LoginRequest();
		loginRequest.setUserName(this.userName);
		loginRequest.setUserPassword(this.userPassword);

		String jwtToken = this.jwtCache.getJwt(loginRequest);

		if (jwtToken == null) {

			final Method[] myMethod = new Method[1];
			Mockito.mock(UserControllerApi.class, Mockito.withSettings().invocationListeners(methodInvocationReport -> {
				myMethod[0] = ((InvocationOnMock) methodInvocationReport.getInvocation()).getMethod();
			})).login(null);

			final MockHttpServletRequestBuilder builder = this.createMockHttpServletRequestBuilder(myMethod[0],
					loginRequest);

			final MvcResult result = this.mvc.perform(builder.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON).characterEncoding(StandardCharsets.UTF_8.name())).andReturn();
			final String content = result.getResponse().getContentAsString(StandardCharsets.UTF_8);

			Assertions.assertNotNull(content);
			Assertions.assertFalse(content.isEmpty());
			Assertions.assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus(), content);

			final LoginResponse actual = this.objectMapper.readValue(content, LoginResponse.class);
			jwtToken = actual.getToken();
			this.jwtCache.putJwt(loginRequest, jwtToken);
		}
		httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken);
		return httpHeaders;
	}

	private <T> T executeCall(final Class<T> clazz, final HttpStatus status,
			final MockHttpServletRequestBuilder builder) throws Exception {

		builder.headers(this.getAuthorizationHeader());

		final MvcResult result = this.mvc.perform(
				builder.contentType(MediaType.APPLICATION_JSON).with(SecurityMockMvcRequestPostProcessors.csrf())
						.accept(MediaType.APPLICATION_JSON).characterEncoding(StandardCharsets.UTF_8.name()))
				.andReturn();

		final String returnHeaderValue = this.getReturnFromPreferHeader(result);
		final String returnHeaderAppliedValue = this.getReturnFromPreferenceAppliedHeader(result);

		if (RETURN_REPRESENTATION.equals(returnHeaderValue) || RETURN_MINIMAL.equals(returnHeaderValue)) {
			Assertions.assertEquals(returnHeaderValue, returnHeaderAppliedValue);
		}

		final String content = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
		Assertions.assertNotNull(content);

		Assertions.assertEquals(status.value(), result.getResponse().getStatus(), content);

		if (clazz != null) {
			Assertions.assertFalse(content.isEmpty(),
					"Response is empty, but a deserialisation class was specified!");
			return this.objectMapper.readValue(content, clazz);
		}

		Assertions.assertEquals(0, content.length());
		return null;
	}

	private String getReturnFromPreferenceAppliedHeader(final MvcResult result) {
		final List<String> headerPreferenceApplied = result.getResponse().getHeaders(HEADER_PREFERENCE_APPLIED);
		return Optional.ofNullable(headerPreferenceApplied)
				.orElse(Collections.emptyList()).stream().map(String::toLowerCase).filter(p -> p.startsWith(RETURN))
				.findFirst().orElse("");
	}

	private String getReturnFromPreferHeader(final MvcResult result) {
		final Enumeration<String> headerPrefer = result.getRequest().getHeaders(HEADER_PREFER);
		return StreamSupport.stream(Spliterators.spliteratorUnknownSize(
				Optional.ofNullable(headerPrefer).orElse(Collections.emptyEnumeration()).asIterator(),
				Spliterator.ORDERED), false).map(String::toLowerCase).filter(p -> p.startsWith(RETURN)).findFirst()
				.orElse("");
	}

	protected <T> T callUsecaseExpect200(final Object body, final Class<T> clazz) throws Exception {
		return this.callUsecaseNew(body, clazz, HttpStatus.OK, null);
	}

	protected <T> T callUsecaseExpect200Representation(final Object body, final Class<T> clazz) throws Exception {
		return this.callUsecaseNew(body, clazz, HttpStatus.OK, HEADER_RETURN_REPRESENTATION);
	}

	protected <T> T callUsecaseExpect200(final Class<T> clazz, final Object... uriVariables) throws Exception {
		return this.callUsecaseNew(null, clazz, HttpStatus.OK, null, uriVariables);
	}

	protected <T> T callUsecaseExpect204(final Object body) throws Exception {
		return this.callUsecaseNew(body, null, HttpStatus.NO_CONTENT, null);
	}

	protected <T> T callUsecaseExpect500(final Object body, final Class<T> clazz) throws Exception {
		return this.callUsecaseNew(body, clazz, HttpStatus.INTERNAL_SERVER_ERROR, null);
	}

	protected <T> T callUsecaseExpect204Minimal(final Object body) throws Exception {
		return this.callUsecaseNew(body, null, HttpStatus.NO_CONTENT, HEADER_RETURN_MINIMAL);
	}

	protected <T> T callUsecaseExpect204WithUriVariables(final Object... uriVariables) throws Exception {
		return this.callUsecaseNew(null, null, HttpStatus.NO_CONTENT, null, uriVariables);
	}

	protected <T> T callUsecaseExpect400(final Object body, final Class<T> clazz) throws Exception {
		return this.callUsecaseNew(body, clazz, HttpStatus.BAD_REQUEST, null);
	}

	protected <T> T callUsecaseExpect400(final Class<T> clazz, final Object... uriVariables) throws Exception {
		return this.callUsecaseNew(null, clazz, HttpStatus.BAD_REQUEST, null, uriVariables);
	}

	protected void callUsecaseExpect403() throws Exception {
		this.callUsecaseNew(null, null, HttpStatus.FORBIDDEN, null);
	}

	protected void callUsecaseExpect403WithUriVariables(final Object... uriVariables) throws Exception {
		this.callUsecaseNew(null, null, HttpStatus.FORBIDDEN, null, uriVariables);
	}

	protected <T> T callUsecaseExpect403(final Class<T> clazz) throws Exception {
		return this.callUsecaseNew(null, clazz, HttpStatus.FORBIDDEN, null);
	}

	protected void callUsecaseExpect403(final Object body) throws Exception {
		this.callUsecaseNew(body, null, HttpStatus.FORBIDDEN, null);
	}

	protected <T> T callUsecaseExpect403(final Object body, final Class<T> clazz) throws Exception {
		return this.callUsecaseNew(body, clazz, HttpStatus.FORBIDDEN, null);
	}

	protected void callUsecaseExpect404(final Object... uriVariables) throws Exception {
		this.callUsecaseNew(null, null, HttpStatus.NOT_FOUND, null, uriVariables);
	}

	protected <T> T callUsecaseExpect422(final Object body, final Class<T> clazz) throws Exception {
		return this.callUsecaseNew(body, clazz, HttpStatus.UNPROCESSABLE_ENTITY, null);
	}

	private <T> T callUsecaseNew(final Object body, final Class<T> responseClazz, final HttpStatus expectedHttpStatus,
			final HttpHeaders httpHeaders, final Object... uriVariables) throws Exception {

		if (this.method == null) {
			this.loadMethod();
		}

		final MockHttpServletRequestBuilder builder = this.createMockHttpServletRequestBuilder(this.method, body,
				uriVariables);

		if (httpHeaders != null) {
			builder.headers(httpHeaders);
		}

		return this.executeCall(responseClazz, expectedHttpStatus, builder);
	}

	private MockHttpServletRequestBuilder createMockHttpServletRequestBuilder(final Method method, final Object body,
			final Object... uriVariables) throws JsonProcessingException {
		final RequestMapping requestMapping = AnnotatedElementUtils.findMergedAnnotation(method, RequestMapping.class);

		if (requestMapping == null) {
			throw new IllegalArgumentException("No @RequestMapping on: " + method.toGenericString());
		}
		final String[] paths = requestMapping.path();
		final RequestMethod[] requestMethods = requestMapping.method();
		final String path = paths[0];
		final RequestMethod requestMethod = requestMethods[0];

		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.request(HttpMethod.valueOf(requestMethod.name()),
				path, uriVariables);

		if (body != null) {
			final String bodyStr = this.objectMapper.writeValueAsString(body);
			builder = builder.content(bodyStr);
		}
		return builder;
	}
}
