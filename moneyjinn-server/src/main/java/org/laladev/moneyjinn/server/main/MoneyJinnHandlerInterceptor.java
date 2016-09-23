package org.laladev.moneyjinn.server.main;

//Copyright (c) 2015 Oliver Lehmann <oliver@laladev.org>
//All rights reserved.
//
//Redistribution and use in source and binary forms, with or without
//modification, are permitted provided that the following conditions
//are met:
//1. Redistributions of source code must retain the above copyright
//notice, this list of conditions and the following disclaimer
//2. Redistributions in binary form must reproduce the above copyright
//notice, this list of conditions and the following disclaimer in the
//documentation and/or other materials provided with the distribution.
//
//THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS'' AND
//ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
//IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
//ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE LIABLE
//FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
//DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
//OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
//HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
//LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
//OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
//SUCH DAMAGE.

import java.lang.annotation.Annotation;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.laladev.moneyjinn.businesslogic.model.access.User;
import org.laladev.moneyjinn.businesslogic.model.access.UserPermission;
import org.laladev.moneyjinn.businesslogic.model.exception.BusinessException;
import org.laladev.moneyjinn.businesslogic.service.api.IUserService;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.core.rest.util.RESTAuthorization;
import org.laladev.moneyjinn.server.annotation.RequiresAuthorization;
import org.laladev.moneyjinn.server.annotation.RequiresPermissionAdmin;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * {@link MoneyJinnHandlerInterceptor} takes care of the authentication of the client and also sets the {@link HttpStatus} to 204 if the
 * {@link HttpServletResponse} body is empty.
 * 
 * @author olivleh1
 * @since 0.0.1
 */
@Named
public class MoneyJinnHandlerInterceptor extends HandlerInterceptorAdapter {
	private static final long MAX_MINUTES_CLOCK_OFF = 15L;

	MoneyJinnRequestWrapper requestWrapper;

	@Inject
	private IUserService userService;
	@Inject
	private RESTAuthorization restAuthorization;
	@Inject
	private SessionEnvironment sessionEnvironment;
	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(RESTAuthorization.DATE_HEADER_FORMAT, Locale.US);

	@Override
	public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {

		boolean requiresAuthorization = false;
		boolean requiresAdmin = false;

		final Annotation[] argAnnotations = ((HandlerMethod) handler).getMethod().getAnnotations();

		for (final Annotation annotation : argAnnotations) {
			if (RequiresAuthorization.class.isInstance(annotation)) {
				requiresAuthorization = true;
			}
			if (RequiresPermissionAdmin.class.isInstance(annotation)) {
				requiresAdmin = true;
			}
		}

		if (requiresAuthorization || requiresAdmin) {
			final String dateHeaderString = ((MoneyJinnRequestWrapper) request).getHeader(RESTAuthorization.DATE_HEADER_NAME);

			final String clientAuthorization = ((MoneyJinnRequestWrapper) request).getHeader(RESTAuthorization.AUTH_HEADER_NAME);

			String userName = null;
			String hmacHash = null;

			if (clientAuthorization != null && clientAuthorization.substring(0, 3).equals(RESTAuthorization.AUTH_HEADER_PREFIX)) {
				final String[] authorizationArray = clientAuthorization.substring(3).split(RESTAuthorization.AUTH_HEADER_SEPERATOR);
				if (authorizationArray.length == 2) {
					userName = authorizationArray[0];
					hmacHash = authorizationArray[1];
				}
			}

			if (userName == null || userName.length() == 0 || hmacHash == null || hmacHash.length() == 0 || dateHeaderString == null) {
				throw new BusinessException("Access Denied! You are not logged on!", ErrorCode.LOGGED_OUT);
			}

			final ZonedDateTime dateHeaderLocalDateTime = ZonedDateTime.parse(dateHeaderString, this.formatter);

			final long minutes = ChronoUnit.MINUTES.between(ZonedDateTime.now(), dateHeaderLocalDateTime);
			if (Math.abs(minutes) > MAX_MINUTES_CLOCK_OFF) {
				throw new BusinessException("Your clock is more than 15 minutes off", ErrorCode.CLIENT_CLOCK_OFF);
			}

			final User user = this.userService.getUserByName(userName);
			if (user == null) {
				throw new BusinessException("Wrong username or password!", ErrorCode.USERNAME_PASSWORD_WRONG);
			}

			final String contentType = request.getContentType();
			final byte[] body = ((MoneyJinnRequestWrapper) request).getBody().getBytes();
			final String method = ((MoneyJinnRequestWrapper) request).getMethod().toString();
			final String requestURL = ((MoneyJinnRequestWrapper) request).getRequestURI();
			final String serverAuthorization = this.restAuthorization.getRESTAuthorization(user.getPassword().getBytes(), method, contentType, requestURL,
					dateHeaderString, body, userName);

			if (serverAuthorization.equals(clientAuthorization)) {
				if (!user.getPermissions().contains(UserPermission.LOGIN)) {
					throw new BusinessException("Your account has been locked!", ErrorCode.ACCOUNT_IS_LOCKED);
				}
				if (requiresAdmin && !user.getPermissions().contains(UserPermission.ADMIN)) {
					throw new BusinessException("You must be an admin to access this functionality!", ErrorCode.USER_IS_NO_ADMIN);
				}
				this.sessionEnvironment.setUserID(user.getId());
			} else {
				throw new BusinessException("Wrong username or password!", ErrorCode.USERNAME_PASSWORD_WRONG);
			}
		}
		return super.preHandle(request, response, handler);

	}

	@Override
	public void postHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler, final ModelAndView modelAndView)
			throws Exception {
		if (response.getContentType() == null) {
			response.setStatus(HttpStatus.NO_CONTENT.value());
		}
	}

}
