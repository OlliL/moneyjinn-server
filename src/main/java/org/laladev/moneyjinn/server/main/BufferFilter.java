package org.laladev.moneyjinn.server.main;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * This filter buffers the Body of the request to access it in the {@see AuthenticationInterceptor}
 * where it would be normally not be possible. Unfortunately the Interceptor is needed because
 * raised Exceptions here are NOT handled by {@see ErrorResponseExceptionHandler}
 *
 * @author olivleh1
 *
 */
public class BufferFilter implements Filter {
	@Override
	public void init(final FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, final ServletResponse response, final FilterChain chain)
			throws IOException, ServletException {
		request = new MoneyJinnRequestWrapper((HttpServletRequest) request);
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
	}

}
