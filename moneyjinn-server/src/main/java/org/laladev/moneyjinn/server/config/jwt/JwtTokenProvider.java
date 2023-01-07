//
// Copyright (c) 2022-2023 Oliver Lehmann <lehmann@ans-netz.de>
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

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.crypto.SecretKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {
  private final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
  @Value("${security.jwt.token.expiration-time-in-ms}")
  private long validityInMilliseconds;
  @Value("${security.jwt.token.refresh-expiration-time-in-ms}")
  private long refreshValidityInMilliseconds;
  @Autowired
  private UserDetailsService userDetailsService;
  Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

  public String createToken(final String username, final List<String> roles) {
    final Claims claims = Jwts.claims().setSubject(username);
    claims.put("roles", roles);
    final Date now = new Date();
    final Date validity = new Date(now.getTime() + this.validityInMilliseconds);
    return Jwts.builder()//
        .setClaims(claims)//
        .setIssuedAt(now)//
        .setExpiration(validity)//
        .signWith(this.secretKey)//
        .compact();
  }

  public String createRefreshToken(final String username, final List<String> roles) {
    final Claims claims = Jwts.claims().setSubject(username);
    claims.put("roles", roles);
    final Date now = new Date();
    final Date validity = new Date(now.getTime() + this.refreshValidityInMilliseconds);
    return Jwts.builder()//
        .setClaims(claims)//
        .setIssuedAt(now)//
        .setExpiration(validity)//
        .signWith(this.secretKey)//
        .compact();
  }

  public Authentication getAuthentication(final String token) {
    final UserDetails userDetails = this.userDetailsService
        .loadUserByUsername(this.getUsername(token));
    if (userDetails != null) {
      if (this.isRefreshToken(token)) {
        return new UsernamePasswordAuthenticationToken(userDetails, "",
            Arrays.asList(new RefreshOnlyGrantedAuthority()));
      }
      return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
    return null;
  }

  private String getUsername(final String token) {
    return Jwts.parserBuilder().setSigningKey(this.secretKey).build().parseClaimsJws(token)
        .getBody().getSubject();
  }

  public boolean isRefreshToken(final String token) {
    final Collection<?> roles = Jwts.parserBuilder().setSigningKey(this.secretKey).build()
        .parseClaimsJws(token).getBody().get("roles", Collection.class);
    if (roles != null && roles.contains(RefreshOnlyGrantedAuthority.ROLE)) {
      return true;
    }
    return false;
  }

  public String resolveToken(final HttpServletRequest req) {
    final String bearerToken = req.getHeader("Authorization");
    if (bearerToken == null) {
      final String token = req.getParameter("token");
      return token;
    }
    if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7, bearerToken.length());
    }
    return null;
  }

  public boolean validateToken(final String authToken) {
    try {
      Jwts.parserBuilder().setSigningKey(this.secretKey).build().parseClaimsJws(authToken);
      return true;
    } catch (final MalformedJwtException e) {
      this.logger.error("Invalid JWT token: {}", e.getMessage());
    } catch (final ExpiredJwtException e) {
      this.logger.error("JWT token is expired: {}", e.getMessage());
    } catch (final UnsupportedJwtException e) {
      this.logger.error("JWT token is unsupported: {}", e.getMessage());
    } catch (final IllegalArgumentException e) {
      this.logger.error("JWT claims string is empty: {}", e.getMessage());
    }
    return false;
  }
}