
package org.laladev.moneyjinn.server.main;

//Copyright (c) 2015, 2018 Oliver Lehmann <lehmann@ans-netz.de>
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
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.model.access.User;
import org.laladev.moneyjinn.model.access.UserPermission;
import org.laladev.moneyjinn.model.exception.BusinessException;
import org.laladev.moneyjinn.server.annotation.RequiresAuthorization;
import org.laladev.moneyjinn.server.annotation.RequiresPermissionAdmin;
import org.laladev.moneyjinn.server.jwt.RefreshOnlyGrantedAuthority;
import org.laladev.moneyjinn.service.api.IUserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    boolean requiresAuthorization = false;
    boolean requiresAdmin = false;
    if (handler instanceof HandlerMethod) {
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
        final Authentication authentication = SecurityContextHolder.getContext()
            .getAuthentication();
        if (authentication != null) {
          final boolean isRefreshOnlyAccess = authentication.getAuthorities().stream()
              .filter(a -> RefreshOnlyGrantedAuthority.ROLE.equals(a.getAuthority())).findFirst()
              .isPresent();
          if (authentication.getName() != null && !isRefreshOnlyAccess) {
            final String username = authentication.getName();
            final User user = this.userService.getUserByName(username);
            if (user != null) {
              requiresAuthorization = false;
              if (requiresAdmin && !user.getPermissions().contains(UserPermission.ADMIN)) {
                throw new BusinessException("You must be an admin to access this functionality!",
                    ErrorCode.USER_IS_NO_ADMIN);
              } else {
                requiresAdmin = false;
              }
            }
          }
        }
      }
      if (requiresAuthorization || requiresAdmin) {
        response.setStatus(403);
        throw new BusinessException("Access Denied! You are not logged on!", ErrorCode.LOGGED_OUT);
      }
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
