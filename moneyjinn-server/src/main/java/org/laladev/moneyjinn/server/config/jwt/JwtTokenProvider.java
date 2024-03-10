//
// Copyright (c) 2022-2024 Oliver Lehmann <lehmann@ans-netz.de>
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions
// are met:
// 1. Redistributions of source code must retain the above copyright
// notice, this list of conditions and the following disclaimer
// 2. Redistributions in binary form must reproduce the above copyright
// notice, this list of conditions and the following disclaimer in the
// documentation and/or other materials provided with the distribution.
//
// THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS'' AND
// ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE LIABLE
// FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
// DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
// OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
// HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
// LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
// OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE.
//

package org.laladev.moneyjinn.server.config.jwt;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.java.Log;

@Component
@Log
public class JwtTokenProvider {
	private static final String CLAIM_USERID = "uid";
	private static final String CLAIM_ROLES = "roles";
	private final SecretKey secretKey = Jwts.SIG.HS512.key().build();
	@Value("${security.jwt.token.expiration-time-in-ms}")
	private long validityInMilliseconds;
	@Value("${security.jwt.token.refresh-expiration-time-in-ms}")
	private long refreshValidityInMilliseconds;

	public String createToken(final String username, final List<String> roles, final Long userId) {
		final Claims claims = Jwts.claims()//
				.subject(username)//
				.add(CLAIM_ROLES, roles)//
				.add(CLAIM_USERID, userId)//
				.build();

		final Instant now = Instant.now();
		final Instant expiration = now.plusMillis(this.validityInMilliseconds);

		return Jwts.builder()//
				.claims(claims)//
				.issuedAt(Date.from(now))//
				.expiration(Date.from(expiration))//
				.signWith(this.secretKey)//
				.compact();
	}

	public String createRefreshToken(final String username, final Long userId) {
		final Claims claims = Jwts.claims().subject(username)//
				.add(CLAIM_ROLES, Arrays.asList(RefreshOnlyGrantedAuthority.ROLE))//
				.add(CLAIM_USERID, userId)//
				.build();

		final Instant now = Instant.now();
		final Instant expiration = now.plusMillis(this.refreshValidityInMilliseconds);

		return Jwts.builder()//
				.claims(claims)//
				.issuedAt(Date.from(now))//
				.expiration(Date.from(expiration))//
				.signWith(this.secretKey)//
				.compact();
	}

	public Authentication getAuthentication(final String token) {
		PreAuthenticatedAuthenticationToken authenticationToken = null;

		final String username = this.getUsername(token);
		final Long userId = this.getUserId(token);
		final List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

		if (username != null && userId != null) {

			if (this.isRefreshToken(token)) {
				grantedAuthorities.add(new RefreshOnlyGrantedAuthority());
			} else {
				this.getRoles(token).forEach(p -> grantedAuthorities.add(new SimpleGrantedAuthority(p)));
			}

			final org.springframework.security.core.userdetails.User userDetails = new org.springframework.security.core.userdetails.User(
					username, "", grantedAuthorities);

			authenticationToken = new PreAuthenticatedAuthenticationToken(userDetails, "",
					userDetails.getAuthorities());
			authenticationToken.setDetails(userId);
		}

		return authenticationToken;
	}

	private String getUsername(final String token) {
		return Jwts.parser().verifyWith(this.secretKey).build().parseSignedClaims(token).getPayload().getSubject();
	}

	@SuppressWarnings("unchecked")
	private List<String> getRoles(final String token) {
		return Jwts.parser().verifyWith(this.secretKey).build().parseSignedClaims(token).getPayload().get(CLAIM_ROLES,
				List.class);
	}

	private Long getUserId(final String token) {
		return Jwts.parser().verifyWith(this.secretKey).build().parseSignedClaims(token).getPayload().get(CLAIM_USERID,
				Long.class);
	}

	public boolean isRefreshToken(final String token) {
		final Collection<?> roles = Jwts.parser().verifyWith(this.secretKey).build().parseSignedClaims(token)
				.getPayload().get(CLAIM_ROLES, Collection.class);
		return (roles != null && roles.contains(RefreshOnlyGrantedAuthority.ROLE));
	}

	public String resolveToken(final HttpServletRequest req) {
		final String bearerToken = req.getHeader(HttpHeaders.AUTHORIZATION);
		if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7, bearerToken.length());
		}
		return null;
	}

	public boolean validateToken(final String token) {
		try {
			Jwts.parser().verifyWith(this.secretKey).build().parseSignedClaims(token);
			return true;
		} catch (final MalformedJwtException e) {
			log.log(Level.SEVERE, "Invalid JWT token: {}", e.getMessage());
		} catch (final ExpiredJwtException e) {
			log.log(Level.SEVERE, "JWT token is expired: {}", e.getMessage());
		} catch (final UnsupportedJwtException e) {
			log.log(Level.SEVERE, "JWT token is unsupported: {}", e.getMessage());
		} catch (final IllegalArgumentException e) {
			log.log(Level.SEVERE, "JWT claims string is empty: {}", e.getMessage());
		}
		return false;
	}
}