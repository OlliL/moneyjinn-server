
package org.laladev.moneyjinn.server.main;

import org.laladev.moneyjinn.service.api.IUserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * {@link MoneyJinnHandlerInterceptor} takes care of the authentication of the client and also sets
 * the {@link HttpStatus} to 204 if the {@link HttpServletResponse} body is empty.
 *
 * @author olivleh1
 * @since 0.0.1
 */
@Named
public class MoneyJinnHandlerInterceptor implements HandlerInterceptor {
  @Inject
  private IUserService userService;
  @Inject
  private SessionEnvironment sessionEnvironment;

  @Override
  public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response,
      final Object handler) throws Exception {
    final boolean requiresAuthorization = false;
    final boolean requiresAdmin = false;
    if (handler instanceof HandlerMethod) {
      // final Annotation[] argAnnotations = ((HandlerMethod) handler).getMethod().getAnnotations();
      // for (final Annotation annotation : argAnnotations) {
      // if (RequiresAuthorization.class.isInstance(annotation)) {
      // requiresAuthorization = true;
      // }
      // if (RequiresPermissionAdmin.class.isInstance(annotation)) {
      // requiresAdmin = true;
      // }
      // }
      // if (requiresAuthorization || requiresAdmin) {
      // final Authentication authentication = SecurityContextHolder.getContext()
      // .getAuthentication();
      // if (authentication != null) {
      // final boolean isRefreshOnlyAccess = authentication.getAuthorities().stream()
      // .filter(a -> RefreshOnlyGrantedAuthority.ROLE.equals(a.getAuthority())).findFirst()
      // .isPresent();
      // if (authentication.getName() != null && !isRefreshOnlyAccess) {
      // final String username = authentication.getName();
      // final User user = this.userService.getUserByName(username);
      // if (user != null) {
      // requiresAuthorization = false;
      // if (requiresAdmin && !user.getPermissions().contains(UserPermission.ADMIN)) {
      // throw new BusinessException("You must be an admin to access this functionality!",
      // ErrorCode.USER_IS_NO_ADMIN);
      // } else {
      // requiresAdmin = false;
      // }
      // }
      // }
      // }
      // }
      // if (requiresAuthorization || requiresAdmin) {
      // response.setStatus(403);
      // throw new BusinessException("Access Denied! You are not logged on!", ErrorCode.LOGGED_OUT);
      // }
      return true;
    }
    return false;
  }

  @Override
  public void postHandle(final HttpServletRequest request, final HttpServletResponse response,
      final Object handler, final ModelAndView modelAndView) throws Exception {
    if (response.getContentType() == null) {
      response.setStatus(HttpStatus.NO_CONTENT.value());
    }
  }
}
