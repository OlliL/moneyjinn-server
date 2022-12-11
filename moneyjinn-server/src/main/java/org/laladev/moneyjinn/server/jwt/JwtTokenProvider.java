package org.laladev.moneyjinn.server.jwt;

import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtTokenProvider {
	@Value("${security.jwt.token.secret-key}")
	private String secretKey;
	@Value("${security.jwt.token.expiration-time-in-ms}")
	private long validityInMilliseconds;
	@Value("${security.jwt.token.refresh-expiration-time-in-ms}")
	private long refreshValidityInMilliseconds;
	@Autowired
	private UserDetailsService userDetailsService;
	Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

	@PostConstruct
	protected void init() {
		this.secretKey = Base64.getEncoder().encodeToString(this.secretKey.getBytes());
	}

	public String createToken(final String username, final List<String> roles) {
		final Claims claims = Jwts.claims().setSubject(username);
		claims.put("roles", roles);
		final Date now = new Date();
		final Date validity = new Date(now.getTime() + this.validityInMilliseconds);
		return Jwts.builder()//
				.setClaims(claims)//
				.setIssuedAt(now)//
				.setExpiration(validity)//
				.signWith(SignatureAlgorithm.HS512, this.secretKey)//
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
				.signWith(SignatureAlgorithm.HS512, this.secretKey)//
				.compact();
	}

	public Authentication getAuthentication(final String token) {
		final UserDetails userDetails = this.userDetailsService.loadUserByUsername(this.getUsername(token));
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
		return Jwts.parser().setSigningKey(this.secretKey).parseClaimsJws(token).getBody().getSubject();
	}

	private boolean isRefreshToken(final String token) {
		final Collection<?> roles = Jwts.parser().setSigningKey(this.secretKey).parseClaimsJws(token).getBody()
				.get("roles", Collection.class);
		if (roles != null && roles.contains(RefreshOnlyGrantedAuthority.ROLE)) {
			return true;
		}
		return false;
	}

	public String resolveToken(final HttpServletRequest req) {
		final String bearerToken = req.getHeader("Authorization");
		if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7, bearerToken.length());
		}
		return null;
	}

	public boolean validateToken(final String authToken) {
		try {
			Jwts.parser().setSigningKey(this.secretKey).parseClaimsJws(authToken);
			return true;
		} catch (final SignatureException e) {
			this.logger.error("Invalid JWT signature: {}", e.getMessage());
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