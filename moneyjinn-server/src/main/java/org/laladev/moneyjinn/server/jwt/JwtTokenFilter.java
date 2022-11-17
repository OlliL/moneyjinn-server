package org.laladev.moneyjinn.server.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

public class JwtTokenFilter extends GenericFilterBean {
	private final JwtTokenProvider jwtTokenProvider;

	public JwtTokenFilter(final JwtTokenProvider jwtTokenProvider) {
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@Override
	public void doFilter(final ServletRequest req, final ServletResponse res, final FilterChain filterChain)
			throws IOException, ServletException {
		final String token = this.jwtTokenProvider.resolveToken((HttpServletRequest) req);
		if (token != null && this.jwtTokenProvider.validateToken(token)) {
			final Authentication auth = token != null ? this.jwtTokenProvider.getAuthentication(token) : null;
			SecurityContextHolder.getContext().setAuthentication(auth);
		}
		filterChain.doFilter(req, res);
	}
}