package org.laladev.moneyjinn.server.main;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.ZonedDateTime;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Test;
import org.laladev.moneyjinn.businesslogic.model.ErrorCode;
import org.laladev.moneyjinn.core.rest.model.ErrorResponse;
import org.laladev.moneyjinn.core.rest.model.user.GetUserSettingsForStartupResponse;
import org.laladev.moneyjinn.server.AbstractMvcTest;
import org.laladev.moneyjinn.server.builder.HttpHeadersBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

public class MoneyJinnHandlerInterceptorTest extends AbstractMvcTest {
	@Inject
	private ObjectMapper objectMapper;
	@Inject
	private MockMvc mvc;
	@Inject
	private HttpHeadersBuilder httpHeadersBuilder;

	private <T> T callUsecase(final ZonedDateTime dateTime, final String userName, final String userPassword,
			final boolean noResult, final Class<T> clazz) throws Exception {

		final String uri = "/moneyflow/user/getUserSettingsForStartup/admin";

		final HttpHeaders httpHeaders = this.httpHeadersBuilder.getAuthHeaders(userName, userPassword,
				ZonedDateTime.now(), uri, "", HttpMethod.GET);

		ResultMatcher status = status().isOk();
		if (noResult) {
			status = status().isNoContent();
		}

		final MvcResult result = this.mvc.perform(MockMvcRequestBuilders.get(uri).headers(httpHeaders)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status)
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

	@Test
	public void test_validLogin_RegularResponse() throws Exception {
		final ZonedDateTime zonedDateTime = ZonedDateTime.now();
		final String userName = UserTransportBuilder.ADMIN_NAME;
		final String userPassword = UserTransportBuilder.ADMIN_PASSWORD;
		final GetUserSettingsForStartupResponse response = this.callUsecase(zonedDateTime, userName, userPassword,
				false, GetUserSettingsForStartupResponse.class);

		Assert.assertNotNull(response);
	}

	@Test

	public void test_wrongPassword_ErrorResponse() throws Exception {
		final ZonedDateTime zonedDateTime = ZonedDateTime.now();
		final String userName = UserTransportBuilder.ADMIN_NAME;
		final String userPassword = "Wrong Password";
		final ErrorResponse response = this.callUsecase(zonedDateTime, userName, userPassword, false,
				ErrorResponse.class);

		Assert.assertNotNull(response);
		Assert.assertEquals(new Integer(ErrorCode.USERNAME_PASSWORD_WRONG.getErrorCode()), response.getCode());
	}
}
