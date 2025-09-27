//
// Copyright (c) 2022-2025 Oliver Lehmann <lehmann@ans-netz.de>
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

import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.messaging.support.NativeMessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.*;
import java.util.logging.Level;

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
        if (token == null)
            return null;

        final var validatedTokenOptional = this.getValidatedToken(token);

        if (validatedTokenOptional.isEmpty())
            return null;

        final var validatedToken = validatedTokenOptional.get();

        final String username = this.getUsername(validatedToken);
        final Long userId = this.getUserId(validatedToken);

        if (username == null || userId == null)
            return null;

        final List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        if (this.isRefreshToken(validatedToken)) {
            grantedAuthorities.add(new RefreshOnlyGrantedAuthority());
        } else {
            this.getRoles(validatedToken).forEach(p -> grantedAuthorities.add(new SimpleGrantedAuthority(p)));
        }

        final var userDetails = new org.springframework.security.core.userdetails.User(username, "",
                grantedAuthorities);

        final var authenticationToken = new PreAuthenticatedAuthenticationToken(userDetails, "",
                userDetails.getAuthorities());
        authenticationToken.setDetails(userId);

        return authenticationToken;
    }

    private String getUsername(final Claims payload) {
        return payload.getSubject();
    }

    @SuppressWarnings("unchecked")
    private List<String> getRoles(final Claims payload) {
        return payload.get(CLAIM_ROLES, List.class);
    }

    private Long getUserId(final Claims payload) {
        return payload.get(CLAIM_USERID, Long.class);
    }

    private boolean isRefreshToken(final Claims payload) {
        final Collection<?> roles = payload.get(CLAIM_ROLES, Collection.class);
        return (roles != null && roles.contains(RefreshOnlyGrantedAuthority.ROLE));
    }

    public String resolveToken(final HttpServletRequest req) {
        final String bearerToken = req.getHeader(HttpHeaders.AUTHORIZATION);
        return this.extractFromHeader(bearerToken);
    }

    public String resolveToken(final NativeMessageHeaderAccessor accessor) {
        final String bearerToken = accessor.getFirstNativeHeader(HttpHeaders.AUTHORIZATION);
        return this.extractFromHeader(bearerToken);
    }

    private String extractFromHeader(final String bearerToken) {
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }

    private Optional<Claims> getValidatedToken(final String token) {
        try {
            return Optional.of(Jwts.parser().verifyWith(this.secretKey).build().parseSignedClaims(token).getPayload());
        } catch (final MalformedJwtException e) {
            log.log(Level.SEVERE, "Invalid JWT token: {}", e.getMessage());
        } catch (final ExpiredJwtException e) {
            log.log(Level.SEVERE, "JWT token is expired: {}", e.getMessage());
        } catch (final UnsupportedJwtException e) {
            log.log(Level.SEVERE, "JWT token is unsupported: {}", e.getMessage());
        } catch (final IllegalArgumentException | NullPointerException e) {
            log.log(Level.SEVERE, "JWT claims string is empty: {}", e.getMessage());
        }
        return Optional.empty();
    }
}