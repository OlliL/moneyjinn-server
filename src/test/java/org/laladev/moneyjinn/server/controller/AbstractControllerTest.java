package org.laladev.moneyjinn.server.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.security.NoSuchAlgorithmException;
import java.time.ZonedDateTime;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Before;
import org.laladev.moneyjinn.businesslogic.model.ErrorCode;
import org.laladev.moneyjinn.core.rest.model.ErrorResponse;
import org.laladev.moneyjinn.server.AbstractMvcTest;
import org.laladev.moneyjinn.server.builder.HttpHeadersBuilder;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class AbstractControllerTest extends AbstractMvcTest {
	@Inject
	private ObjectMapper objectMapper;
	@Inject
	private MockMvc mvc;
	@Inject
	private CacheManager cacheManager;
	@Inject
	private HttpHeadersBuilder httpHeadersBuilder;

	protected abstract String getUsername();

	protected abstract String getPassword();

	private HttpHeaders getAuthHeaders(final String uri, final String body, final HttpMethod httpMethod) {
		final String userName = this.getUsername();
		final String userPassword = this.getPassword();
		try {
			return this.httpHeadersBuilder.getAuthHeaders(userName, userPassword, ZonedDateTime.now(), uri, body,
					httpMethod);
		} catch (final NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	protected abstract String getUsecase();

	/**
	 * Input "user" and "ShowEditUserTest" and get "user/showEditUser" back
	 *
	 * @param prefix
	 * @param clazz
	 * @return
	 */
	protected String getUsecaseFromTestClassName(final String prefix, final Class<?> clazz) {
		final String usecase = clazz.getSimpleName().replace("Test", "");
		return prefix + "/" + Character.toLowerCase(usecase.charAt(0)) + usecase.substring(1);
	}

	protected <T> T callUsecaseWithoutContent(final String uriParameters, final HttpMethod httpMethod,
			final boolean noResult, final Class<T> clazz) throws Exception {
		return this.callUsecase(uriParameters, httpMethod, "", noResult, clazz);
	}

	protected <T> T callUsecaseWithContent(final String uriParameters, final HttpMethod httpMethod, final Object body,
			final boolean noResult, final Class<T> clazz) throws Exception {
		final String bodyStr = this.objectMapper.writeValueAsString(body);
		return this.callUsecase(uriParameters, httpMethod, bodyStr, noResult, clazz);
	}

	private <T> T callUsecase(final String uriParameters, final HttpMethod httpMethod, final String body,
			final boolean noResult, final Class<T> clazz) throws Exception {

		ResultMatcher status = status().isOk();
		if (noResult) {
			status = status().isNoContent();
		}

		MockHttpServletRequestBuilder builder = null;
		// final URI uri = new URI("/moneyflow/server/" + this.getUsecase() + uriParameters);
		final String uri = "/moneyflow/server/" + this.getUsecase() + uriParameters;

		switch (httpMethod) {
		case GET:
			builder = MockMvcRequestBuilders.get(uri);
			break;
		case DELETE:
			builder = MockMvcRequestBuilders.delete(uri);
			break;
		case PUT:
			builder = MockMvcRequestBuilders.put(uri).content(body);
			break;
		case POST:
			builder = MockMvcRequestBuilders.post(uri).content(body);
			break;
		default:
			break;

		}

		// builder.headers(this.getAuthHeaders(uri.getPath(), body, httpMethod));
		builder.headers(this.getAuthHeaders(uri, body, httpMethod));

		final MvcResult result = this.mvc
				.perform(builder.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status).andReturn();

		final String content = result.getResponse().getContentAsString();
		Assert.assertNotNull(content);

		if (!noResult) {
			Assert.assertTrue(content.length() > 0);

			final T actual = this.objectMapper.readValue(content, clazz);

			return actual;
		} else {
			Assert.assertTrue(content.length() == 0);
			return null;
		}

	}

	protected ErrorResponse accessDeniedErrorResponse() {
		final ErrorResponse expected = new ErrorResponse();
		expected.setCode(ErrorCode.LOGGED_OUT.getErrorCode());
		expected.setMessage("Access Denied! You are not logged on!");

		return expected;
	}

	/**
	 * Clears all Caches before each test execution
	 */
	@Before
	public void before() {
		this.cacheManager.getCacheNames().stream().map(this.cacheManager::getCache).forEach(Cache::clear);
	}
}
