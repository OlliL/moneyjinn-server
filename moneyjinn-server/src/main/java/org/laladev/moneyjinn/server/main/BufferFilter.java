package org.laladev.moneyjinn.server.main;

//Copyright (c) 2015 Oliver Lehmann <lehmann@ans-netz.de>
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

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.laladev.moneyjinn.core.rest.util.RESTAuthorization;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * This filter buffers the Body of the request to access it in the {@see AuthenticationInterceptor}
 * where it would be normally not be possible. Unfortunately the Interceptor is needed because
 * raised Exceptions here are NOT handled by {@see ErrorResponseExceptionHandler}
 *
 * @author olivleh1
 *
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class BufferFilter implements Filter {
	@Override
	public void init(final FilterConfig filterConfig) throws ServletException {
		// noop
	}

	@Override
	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
			throws IOException, ServletException {
		final ServletRequest req = new MoneyJinnRequestWrapper((HttpServletRequest) request);
		((HttpServletResponse) response).setHeader("Access-Control-Allow-Origin", "*");
		((HttpServletResponse) response).setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
		((HttpServletResponse) response).setHeader("Access-Control-Max-Age", "3600");
		((HttpServletResponse) response).setHeader("Access-Control-Allow-Headers",
				"Content-Type," + RESTAuthorization.DATE_HEADER_NAME + ", " + RESTAuthorization.AUTH_HEADER_NAME + ", "
						+ "Authorization");
		((HttpServletResponse) response).setHeader("Access-Control-Allow-Credentials", "true");
		((HttpServletResponse) response).setHeader("Access-Control-Allow-Private-Network", "true");
		chain.doFilter(req, response);
	}

	@Override
	public void destroy() {
		// noop
	}

}
