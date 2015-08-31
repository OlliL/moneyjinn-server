package org.laladev.moneyjinn.server.builder;

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
	private MessageDigest sha1MD;

	protected HttpHeadersBuilder() {
		this.restAuthorization = new RESTAuthorization();
		this.dateFormatter = DateTimeFormatter.ofPattern(RESTAuthorization.dateHeaderFormat, Locale.US)
				.withZone(ZoneId.of("GMT"));
		try {
			this.sha1MD = MessageDigest.getInstance("SHA1");
		} catch (final NoSuchAlgorithmException e) {
		}
	}

	public HttpHeaders getAuthHeaders(final String userName, final String userPassword, final ZonedDateTime dateTime,
			final String uri, final String body, final HttpMethod httpMethod) {
		this.sha1MD.reset();

		final String date = this.dateFormatter.format(dateTime);

		final String contentType = MediaType.APPLICATION_JSON_VALUE;
		final byte[] secret = BytesToHexConverter.convert(this.sha1MD.digest(userPassword.getBytes())).getBytes();

		final String authString = this.restAuthorization.getRESTAuthorization(secret, httpMethod.toString(),
				contentType, uri, date, body.getBytes(), userName);

		final HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add(RESTAuthorization.dateHeaderName, date);
		httpHeaders.add(RESTAuthorization.authenticationHeaderName, authString.trim());

		return httpHeaders;

	}

}
