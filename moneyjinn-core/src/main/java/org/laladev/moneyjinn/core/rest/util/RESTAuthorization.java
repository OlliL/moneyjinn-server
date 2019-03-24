//
// Copyright (c) 2014-2015 Oliver Lehmann <lehmann@ans-netz.de>
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
// $Id: RESTAuthorization.java,v 1.6 2015/08/29 00:13:26 olivleh1 Exp $
//
package org.laladev.moneyjinn.core.rest.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class RESTAuthorization {

	public static final String DATE_HEADER_FORMAT = "EEE, dd MMM yyyy HH:mm:ss z";
	public static final String DATE_HEADER_NAME = "Requestdate";
	public static final String AUTH_HEADER_NAME = "Authentication";
	public static final String AUTH_HEADER_PREFIX = "MNF";
	public static final String AUTH_HEADER_SEPERATOR = ":";
	private static final String MAC_ALGORITHM = "HmacSHA1";

	private final Base64 base64 = new Base64();

	/**
	 * This function works basically as described in
	 * <a href= "http://docs.aws.amazon.com/AmazonS3/latest/dev/RESTAuthentication.html" >Amazon
	 * Simple Storage Service - REST Authentication</a>
	 *
	 * @param secret
	 * @param httpVerb
	 * @param contentType
	 * @param url
	 * @param date
	 * @param body
	 * @param ident
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 * @throws InvalidKeyException
	 */
	public final String getRESTAuthorization(final byte[] password, final String httpVerb, final String contentType,
			final String url, final String date, final byte[] body, final String username)
			throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
		String authString = null;
		byte[] secret = password;
		if (secret == null) {
			secret = new String(" ").getBytes();
		}

		String ident = username;
		if (ident == null) {
			ident = new String();
		}

		final StringBuilder stringToSign = new StringBuilder(httpVerb);
		stringToSign.append("\n");

		if (body.length > 0) {
			stringToSign.append(this.getMD5(body));
		}

		stringToSign.append("\n");

		if (contentType != null) {
			stringToSign.append(contentType);
		}

		stringToSign.append("\n");
		stringToSign.append(date);
		stringToSign.append("\n");
		stringToSign.append("\n");
		stringToSign.append(url);

		final byte[] rawHmac = this.getRawHmac(stringToSign, secret);
		final byte[] hexBytes = BytesToHexConverter.convert(rawHmac).getBytes();
		final byte[] base64Bytes = this.base64.encode(hexBytes);
		authString = new String(base64Bytes);

		return AUTH_HEADER_PREFIX + ident + AUTH_HEADER_SEPERATOR + authString;
	}

	private String getMD5(final byte[] body) throws NoSuchAlgorithmException {
		final MessageDigest md5MD = MessageDigest.getInstance("MD5");
		return BytesToHexConverter.convert(md5MD.digest(body));
	}

	private byte[] getRawHmac(final StringBuilder stringToSign, final byte[] secret)
			throws InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException {
		final Mac hmacSHA1Mac = Mac.getInstance(MAC_ALGORITHM);
		hmacSHA1Mac.init(new SecretKeySpec(secret, MAC_ALGORITHM));
		return hmacSHA1Mac.doFinal(stringToSign.toString().getBytes("UTF-8"));
	}
}
