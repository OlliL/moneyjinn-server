//
// Copyright (c) 2014-2015 Oliver Lehmann <oliver@laladev.org>
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

	public static final String dateHeaderFormat = "EEE, dd MMM yyyy HH:mm:ss z";
	public static final String dateHeaderName = "Requestdate";
	public static final String authenticationHeaderName = "Authentication";

	private MessageDigest md5MD;
	private Mac hmacSHA1Mac;
	private final Base64 base64 = new Base64();

	public RESTAuthorization() {
		try {
			this.md5MD = MessageDigest.getInstance("MD5");
			this.hmacSHA1Mac = Mac.getInstance("HmacSHA1");
		} catch (final NoSuchAlgorithmException e) {
		}

	}

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
	 */
	public final String getRESTAuthorization(byte[] secret, final String httpVerb, final String contentType,
			final String url, final String date, final byte[] body, String ident) {
		String authString = null;
		if (secret == null) {
			secret = new String(" ").getBytes();
		}

		if (ident == null) {
			ident = new String();
		}

		this.md5MD.reset();
		try {
			this.hmacSHA1Mac.reset();
		} catch (final NullPointerException e) {
			// The android implementation of OpenSSLMac does not check if the
			// mac was initialized before it resets. The first reset before an
			// init was called always raises a NPE
		}

		final StringBuilder stringToSign = new StringBuilder(httpVerb);
		stringToSign.append("\n");
		if (body.length > 0) {
			final byte[] md5 = BytesToHexConverter.convert(this.md5MD.digest(body)).getBytes();
			stringToSign.append(new String(md5));
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

		try {
			final SecretKeySpec signingKey = new SecretKeySpec(secret, this.hmacSHA1Mac.getAlgorithm());
			this.hmacSHA1Mac.init(signingKey);
			final byte[] rawHmac = this.hmacSHA1Mac.doFinal(stringToSign.toString().getBytes("UTF-8"));
			final byte[] hexBytes = BytesToHexConverter.convert(rawHmac).getBytes();
			final byte[] base64Bytes = this.base64.encode(hexBytes);
			authString = new String(base64Bytes);
		} catch (final InvalidKeyException e) {
		} catch (final UnsupportedEncodingException e) {
		}

		return "MNF" + ident + ":" + authString;
	}
}
