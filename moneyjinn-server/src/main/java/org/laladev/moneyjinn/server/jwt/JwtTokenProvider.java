package org.laladev.moneyjinn.server.jwt;

import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenProvider {
	@Value("${security.jwt.token.secret-key}")
	private String secretKey;
	@Value("${security.jwt.token.expire-length}")
	private long validityInMilliseconds;
	@Autowired
	private UserDetailsService userDetailsService;

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
				.signWith(SignatureAlgorithm.HS256, this.secretKey)//
				.compact();
	}

	public Authentication getAuthentication(final String token) {
		final UserDetails userDetails = this.userDetailsService.loadUserByUsername(this.getUsername(token));
		if (userDetails != null) {
			return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
		}
		return null;
	}

	private String getUsername(final String token) {
		return Jwts.parser().setSigningKey(this.secretKey).parseClaimsJws(token).getBody().getSubject();
	}

	public String resolveToken(final HttpServletRequest req) {
		final String bearerToken = req.getHeader("Authorization");
		if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7, bearerToken.length());
		}
		return null;
	}

	public boolean validateToken(final String token) {
		try {
			final Jws<Claims> claims = Jwts.parser().setSigningKey(this.secretKey).parseClaimsJws(token);
			if (claims.getBody().getExpiration().before(new Date())) {
				return false;
			}
			return true;
		} catch (JwtException | IllegalArgumentException e) {
			throw new RuntimeException("Expired or invalid JWT token");
		}
	}
}