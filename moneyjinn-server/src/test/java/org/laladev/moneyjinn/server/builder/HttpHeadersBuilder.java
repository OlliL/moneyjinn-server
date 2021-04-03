package org.laladev.moneyjinn.server.builder;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import javax.inject.Named;

import org.laladev.moneyjinn.core.rest.util.BytesToHexConverter;
import org.laladev.moneyjinn.core.rest.util.RESTAuthorization;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

@Named
public class HttpHeadersBuilder {
	private final RESTAuthorization restAuthorization;
	private final DateTimeFormatter dateFormatter;
	private MessageDigest sha1Md;

	protected HttpHeadersBuilder() {
		this.restAuthorization = new RESTAuthorization();
		this.dateFormatter = DateTimeFormatter.ofPattern(RESTAuthorization.DATE_HEADER_FORMAT, Locale.US)
				.withZone(ZoneId.of("GMT"));
		try {
			this.sha1Md = MessageDigest.getInstance("SHA1");
		} catch (final NoSuchAlgorithmException e) {
			// Ignore Exception
		}
	}

	public HttpHeaders getDateHeader(final ZonedDateTime dateTime) {
		final String date = this.dateFormatter.format(dateTime);
		final HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add(RESTAuthorization.DATE_HEADER_NAME, date);
		return httpHeaders;
	}

	public HttpHeaders getAuthHeaders(final String userName, final String userPassword, final ZonedDateTime dateTime,
			final String uri, final String body, final HttpMethod httpMethod)
			throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
		this.sha1Md.reset();
		final HttpHeaders httpHeaders = this.getDateHeader(dateTime);
		if (userName != null && userPassword != null) {

			final String contentType = MediaType.APPLICATION_JSON_VALUE;
			final byte[] secret = BytesToHexConverter.convert(this.sha1Md.digest(userPassword.getBytes())).getBytes();

			final String authString = this.restAuthorization.getRESTAuthorization(secret, httpMethod.toString(),
					contentType, uri, httpHeaders.getFirst(RESTAuthorization.DATE_HEADER_NAME), body.getBytes(),
					userName);

			httpHeaders.add(RESTAuthorization.AUTH_HEADER_NAME, authString.trim());

		}
		return httpHeaders;

	}

}
