package org.laladev.moneyjinn.server.main;

import java.lang.annotation.Annotation;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.laladev.moneyjinn.businesslogic.model.ErrorCode;
import org.laladev.moneyjinn.businesslogic.model.access.User;
import org.laladev.moneyjinn.businesslogic.model.access.UserPermission;
import org.laladev.moneyjinn.businesslogic.model.exception.BusinessException;
import org.laladev.moneyjinn.businesslogic.service.api.IUserService;
import org.laladev.moneyjinn.core.rest.util.RESTAuthorization;
import org.laladev.moneyjinn.server.annotation.RequiresAuthorization;
import org.laladev.moneyjinn.server.annotation.RequiresPermissionAdmin;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * {@link MoneyJinnHandlerInterceptor} takes care of the authentication of the client and also sets
 * the {@link HttpStatus} to 204 if the {@link HttpServletResponse} body is empty.
 *
 * @author olivleh1
 * @since 0.0.1
 */
@Named
public class MoneyJinnHandlerInterceptor extends HandlerInterceptorAdapter {
	MoneyJinnRequestWrapper requestWrapper;

	@Inject
	private IUserService userService;
	@Inject
	private RESTAuthorization restAuthorization;
	@Inject
	private SessionEnvironment sessionEnvironment;

	@Override
	public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler)
			throws Exception {

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
			final String dateHeaderString = ((MoneyJinnRequestWrapper) request)
					.getHeader(RESTAuthorization.dateHeaderName);

			final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(RESTAuthorization.dateHeaderFormat,
					Locale.US);
			final ZonedDateTime dateHeaderLocalDateTime = ZonedDateTime.parse(dateHeaderString, formatter);

			final String clientAuthorization = ((MoneyJinnRequestWrapper) request)
					.getHeader(RESTAuthorization.authenticationHeaderName);

			String userName = null;
			String hmacHash = null;

			if (clientAuthorization != null) {
				if (clientAuthorization.substring(0, 3).equals(RESTAuthorization.authenticationHeaderPrefix)) {
					final String[] authorizationArray = clientAuthorization.substring(3)
							.split(RESTAuthorization.authenticationHeaderSeparator);
					if (authorizationArray.length == 2) {
						userName = authorizationArray[0];
						hmacHash = authorizationArray[1];
					}
				}
			}

			if (userName == null || userName.length() == 0 || hmacHash == null || hmacHash.length() == 0
					|| dateHeaderString == null) {
				throw new BusinessException("Access Denied! You are not logged on!", ErrorCode.LOGGED_OUT);
			}

			final long minutes = ChronoUnit.MINUTES.between(ZonedDateTime.now(), dateHeaderLocalDateTime);
			if (Math.abs(minutes) > 15l) {
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
			final String serverAuthorization = this.restAuthorization.getRESTAuthorization(
					user.getPassword().getBytes(), method, contentType, requestURL, dateHeaderString, body, userName);

			if (clientAuthorization.equals(serverAuthorization)) {
				if (!user.getPermissions().contains(UserPermission.LOGIN)) {
					throw new BusinessException("Your account has been locked!", ErrorCode.ACCOUNT_IS_LOCKED);
				}
				if (requiresAdmin && !user.getPermissions().contains(UserPermission.ADMIN)) {
					throw new BusinessException("You must be an admin to access this functionality!",
							ErrorCode.USER_IS_NO_ADMIN);
				}
				this.sessionEnvironment.setUserID(user.getId());
			} else {
				throw new BusinessException("Wrong username or password!", ErrorCode.USERNAME_PASSWORD_WRONG);
			}
		}
		return super.preHandle(request, response, handler);

	}

	@Override
	public void postHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler,
			final ModelAndView modelAndView) throws Exception {
		if (response.getContentType() == null) {
			response.setStatus(HttpStatus.NO_CONTENT.value());
		}
	}

}
