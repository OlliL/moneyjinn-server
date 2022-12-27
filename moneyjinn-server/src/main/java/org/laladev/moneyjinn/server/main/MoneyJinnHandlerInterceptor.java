
package org.laladev.moneyjinn.server.main;

import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * sets the {@link HttpStatus} to 204 if the {@link HttpServletResponse} body is empty.
 *
 * @author olivleh1
 * @since 0.0.1
 */
@Named
public class MoneyJinnHandlerInterceptor implements HandlerInterceptor {
  @Override
  public void postHandle(final HttpServletRequest request, final HttpServletResponse response,
      final Object handler, final ModelAndView modelAndView) throws Exception {
    if (response.getContentType() == null) {
      response.setStatus(HttpStatus.NO_CONTENT.value());
    }
  }
}
