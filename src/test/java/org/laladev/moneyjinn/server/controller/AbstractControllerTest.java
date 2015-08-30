package org.laladev.moneyjinn.server.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.laladev.moneyjinn.api.MoneyjinnProfiles;
import org.laladev.moneyjinn.server.config.MoneyjinnConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { MoneyjinnConfiguration.class })
@WebAppConfiguration
@EnableAutoConfiguration
@ActiveProfiles(MoneyjinnProfiles.TEST)
@SqlGroup({ @Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = { "classpath:h2defaults.sql",
		"classpath:testdata.sql" }) })
public abstract class AbstractControllerTest {
	@Inject
	private ObjectMapper objectMapper;
	@Inject
	private MockMvc mvc;
	@Inject
	private CacheManager cacheManager;

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
		ResultMatcher status = status().isOk();
		if (noResult) {
			status = status().isNoContent();
		}

		MockHttpServletRequestBuilder builder = null;
		switch (httpMethod) {
		case GET:
			builder = MockMvcRequestBuilders.get("/moneyflow/" + this.getUsecase() + uriParameters);
			break;
		case DELETE:
			builder = MockMvcRequestBuilders.delete("/moneyflow/" + this.getUsecase() + uriParameters);
			break;
		default:
			break;

		}
		final MvcResult result = this.mvc.perform(builder.accept(MediaType.APPLICATION_JSON)).andExpect(status)
				.andReturn();

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

	protected <T> T callUsecaseWithContent(final String uriParameters, final HttpMethod httpMethod, final Object body,
			final boolean noResult, final Class<T> clazz) throws Exception {
		final String bodyStr = this.objectMapper.writeValueAsString(body);

		ResultMatcher status = status().isOk();
		if (noResult) {
			status = status().isNoContent();
		}

		MockHttpServletRequestBuilder builder = null;
		switch (httpMethod) {
		case PUT:
			builder = MockMvcRequestBuilders.put("/moneyflow/" + this.getUsecase() + uriParameters);
			break;
		case POST:
			builder = MockMvcRequestBuilders.post("/moneyflow/" + this.getUsecase() + uriParameters);
			break;
		default:
			break;

		}
		final MvcResult result = this.mvc.perform(
				builder.content(bodyStr).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
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

	/**
	 * Clears all Caches before each test execution
	 */
	@Before
	public void before() {
		this.cacheManager.getCacheNames().stream().map(this.cacheManager::getCache).forEach(Cache::clear);
	}
}
