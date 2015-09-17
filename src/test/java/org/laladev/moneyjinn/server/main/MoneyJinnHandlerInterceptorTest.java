package org.laladev.moneyjinn.server.main;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.ZonedDateTime;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Test;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.core.rest.model.ErrorResponse;
import org.laladev.moneyjinn.core.rest.model.user.GetUserSettingsForStartupResponse;
import org.laladev.moneyjinn.core.rest.util.RESTAuthorization;
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
			final boolean noResult, final Class<T> clazz, HttpHeaders httpHeaders) throws Exception {

		final String uri = "/moneyflow/server/user/getUserSettingsForStartup/admin";

		if (httpHeaders == null) {
			httpHeaders = this.httpHeadersBuilder.getAuthHeaders(userName, userPassword, dateTime, uri, "",
					HttpMethod.GET);
		}

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
				false, GetUserSettingsForStartupResponse.class, null);

		Assert.assertNotNull(response);
	}

	@Test
	public void test_wrongPassword_ErrorResponse() throws Exception {
		final ZonedDateTime zonedDateTime = ZonedDateTime.now();
		final String userName = UserTransportBuilder.ADMIN_NAME;
		final String userPassword = "Wrong Password";
		final ErrorResponse response = this.callUsecase(zonedDateTime, userName, userPassword, false,
				ErrorResponse.class, null);

		Assert.assertNotNull(response);
		Assert.assertEquals(new Integer(ErrorCode.USERNAME_PASSWORD_WRONG.getErrorCode()), response.getCode());
	}

	@Test
	public void test_wrongUsername_ErrorResponse() throws Exception {
		final ZonedDateTime zonedDateTime = ZonedDateTime.now();
		final String userName = "Non Existing";
		final String userPassword = UserTransportBuilder.ADMIN_PASSWORD;
		final ErrorResponse response = this.callUsecase(zonedDateTime, userName, userPassword, false,
				ErrorResponse.class, null);

		Assert.assertNotNull(response);
		Assert.assertEquals(new Integer(ErrorCode.USERNAME_PASSWORD_WRONG.getErrorCode()), response.getCode());
	}

	@Test
	public void test_TimeToEarly_ErrorResponse() throws Exception {
		final ZonedDateTime zonedDateTime = ZonedDateTime.now().minusMinutes(16l);
		final String userName = UserTransportBuilder.ADMIN_NAME;
		final String userPassword = UserTransportBuilder.ADMIN_PASSWORD;
		final ErrorResponse response = this.callUsecase(zonedDateTime, userName, userPassword, false,
				ErrorResponse.class, null);

		Assert.assertNotNull(response);
		Assert.assertEquals(new Integer(ErrorCode.CLIENT_CLOCK_OFF.getErrorCode()), response.getCode());
	}

	@Test
	public void test_TimeToLate_ErrorResponse() throws Exception {
		final ZonedDateTime zonedDateTime = ZonedDateTime.now().plusMinutes(17l);
		final String userName = UserTransportBuilder.ADMIN_NAME;
		final String userPassword = UserTransportBuilder.ADMIN_PASSWORD;
		final ErrorResponse response = this.callUsecase(zonedDateTime, userName, userPassword, false,
				ErrorResponse.class, null);

		Assert.assertNotNull(response);
		Assert.assertEquals(new Integer(ErrorCode.CLIENT_CLOCK_OFF.getErrorCode()), response.getCode());
	}

	@Test
	public void test_NoAuthHeader_ErrorResponse() throws Exception {
		final ZonedDateTime zonedDateTime = ZonedDateTime.now().plusMinutes(17l);
		final String userName = UserTransportBuilder.ADMIN_NAME;
		final String userPassword = UserTransportBuilder.ADMIN_PASSWORD;
		final HttpHeaders httpHeaders = this.httpHeadersBuilder.getDateHeader(zonedDateTime);

		final ErrorResponse response = this.callUsecase(zonedDateTime, userName, userPassword, false,
				ErrorResponse.class, httpHeaders);

		Assert.assertNotNull(response);
		Assert.assertEquals(new Integer(ErrorCode.LOGGED_OUT.getErrorCode()), response.getCode());
	}

	@Test
	public void test_NoUserInAuthHeader_ErrorResponse() throws Exception {
		final ZonedDateTime zonedDateTime = ZonedDateTime.now();
		final String userName = UserTransportBuilder.ADMIN_NAME;
		final String userPassword = UserTransportBuilder.ADMIN_PASSWORD;
		final HttpHeaders httpHeaders = this.httpHeadersBuilder.getDateHeader(zonedDateTime);
		httpHeaders.add(RESTAuthorization.AUTH_HEADER_NAME, RESTAuthorization.AUTH_HEADER_PREFIX
				+ RESTAuthorization.AUTH_HEADER_SEPERATOR + "aaaaa");

		final ErrorResponse response = this.callUsecase(zonedDateTime, userName, userPassword, false,
				ErrorResponse.class, httpHeaders);

		Assert.assertNotNull(response);
		Assert.assertEquals(new Integer(ErrorCode.LOGGED_OUT.getErrorCode()), response.getCode());
	}

	@Test
	public void test_NoTokenInAuthHeader_ErrorResponse() throws Exception {
		final ZonedDateTime zonedDateTime = ZonedDateTime.now();
		final String userName = UserTransportBuilder.ADMIN_NAME;
		final String userPassword = UserTransportBuilder.ADMIN_PASSWORD;
		final HttpHeaders httpHeaders = this.httpHeadersBuilder.getDateHeader(zonedDateTime);
		httpHeaders.add(RESTAuthorization.AUTH_HEADER_NAME, RESTAuthorization.AUTH_HEADER_PREFIX
				+ "klaus" + RESTAuthorization.AUTH_HEADER_SEPERATOR);

		final ErrorResponse response = this.callUsecase(zonedDateTime, userName, userPassword, false,
				ErrorResponse.class, httpHeaders);

		Assert.assertNotNull(response);
		Assert.assertEquals(new Integer(ErrorCode.LOGGED_OUT.getErrorCode()), response.getCode());
	}

	@Test
	public void test_wrongPrefix_ErrorResponse() throws Exception {
		final ZonedDateTime zonedDateTime = ZonedDateTime.now();
		final String userName = UserTransportBuilder.ADMIN_NAME;
		final String userPassword = UserTransportBuilder.ADMIN_PASSWORD;
		final HttpHeaders httpHeaders = this.httpHeadersBuilder.getDateHeader(zonedDateTime);
		httpHeaders.add(RESTAuthorization.AUTH_HEADER_NAME,
				"XXX" + "klaus" + RESTAuthorization.AUTH_HEADER_SEPERATOR + "aaa");

		final ErrorResponse response = this.callUsecase(zonedDateTime, userName, userPassword, false,
				ErrorResponse.class, httpHeaders);

		Assert.assertNotNull(response);
		Assert.assertEquals(new Integer(ErrorCode.LOGGED_OUT.getErrorCode()), response.getCode());
	}

	@Test
	public void test_missingSeparator_ErrorResponse() throws Exception {
		final ZonedDateTime zonedDateTime = ZonedDateTime.now();
		final String userName = UserTransportBuilder.ADMIN_NAME;
		final String userPassword = UserTransportBuilder.ADMIN_PASSWORD;
		final HttpHeaders httpHeaders = this.httpHeadersBuilder.getDateHeader(zonedDateTime);
		httpHeaders.add(RESTAuthorization.AUTH_HEADER_NAME,
				RESTAuthorization.AUTH_HEADER_PREFIX + "klausaaa");

		final ErrorResponse response = this.callUsecase(zonedDateTime, userName, userPassword, false,
				ErrorResponse.class, httpHeaders);

		Assert.assertNotNull(response);
		Assert.assertEquals(new Integer(ErrorCode.LOGGED_OUT.getErrorCode()), response.getCode());
	}

	@Test
	public void test_LoginNotAllowed_ErrorResponse() throws Exception {
		final ZonedDateTime zonedDateTime = ZonedDateTime.now();
		final String userName = UserTransportBuilder.USER2_NAME;
		final String userPassword = UserTransportBuilder.USER2_PASSWORD;
		final ErrorResponse response = this.callUsecase(zonedDateTime, userName, userPassword, false,
				ErrorResponse.class, null);

		Assert.assertNotNull(response);
		Assert.assertEquals(new Integer(ErrorCode.ACCOUNT_IS_LOCKED.getErrorCode()), response.getCode());
	}

}
