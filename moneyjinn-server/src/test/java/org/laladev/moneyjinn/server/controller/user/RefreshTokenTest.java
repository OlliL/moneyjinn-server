
package org.laladev.moneyjinn.server.controller.user;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.model.access.User;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.access.UserRole;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.config.jwt.JwtTokenProvider;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.laladev.moneyjinn.server.controller.api.UserControllerApi;
import org.laladev.moneyjinn.server.model.ErrorResponse;
import org.laladev.moneyjinn.server.model.LoginRequest;
import org.laladev.moneyjinn.server.model.LoginResponse;
import org.laladev.moneyjinn.service.api.IUserService;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.inject.Inject;

class RefreshTokenTest extends AbstractControllerTest {
	@Inject
	private JwtTokenProvider jwtTokenProvider;
	@Inject
	private ObjectMapper objectMapper;
	@Inject
	private MockMvc mvc;
	@Inject
	private IUserService userService;

	@BeforeEach
	public void setUp() {
		super.setUsername(null);
		super.setPassword(null);
	}

	@Override
	protected void loadMethod() {
		super.getMock(UserControllerApi.class).refreshToken();
	}

	/**
	 * Use the supplied refresh token as Bearer token to get a new Token.
	 *
	 * @throws Exception
	 */
	@BeforeEach
	public void beforeTestClass() throws Exception {
		final String username = UserTransportBuilder.USER1_NAME;
		final String password = UserTransportBuilder.USER1_PASSWORD;

		final LoginRequest loginRequest = new LoginRequest();
		loginRequest.setUserName(username);
		loginRequest.setUserPassword(password);
		final String uri = "/moneyflow/server/user/login";
		final String body = this.objectMapper.writeValueAsString(loginRequest);
		final MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post(uri).content(body);
		final MvcResult result = this.mvc.perform(builder.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).characterEncoding(StandardCharsets.UTF_8.name())).andReturn();
		final String content = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
		Assertions.assertNotNull(content);
		Assertions.assertTrue(content.length() > 0);
		final LoginResponse actual = this.objectMapper.readValue(content, LoginResponse.class);
		super.setOverrideJwtToken(actual.getRefreshToken());
	}

	@Test
	void test_RegularUser_Successfull() throws Exception {

		final LoginResponse response = super.callUsecaseExpect200(LoginResponse.class);

		Assertions.assertEquals(new UserTransportBuilder().forUser1().build(), response.getUserTransport());
		Assertions.assertTrue(this.jwtTokenProvider.validateToken(response.getToken()));
		Assertions.assertTrue(this.jwtTokenProvider.validateToken(response.getRefreshToken()));
	}

	@Test
	void test_LockedUser_ErrorResponse() throws Exception {
		final User user = this.userService.getUserById(new UserID(UserTransportBuilder.USER1_ID));
		user.setRole(UserRole.INACTIVE);
		this.userService.updateUser(user);

		final ErrorResponse response = super.callUsecaseExpect403(ErrorResponse.class);

		Assertions.assertEquals(response.getCode(), ErrorCode.ACCOUNT_IS_LOCKED.getErrorCode());
	}
}