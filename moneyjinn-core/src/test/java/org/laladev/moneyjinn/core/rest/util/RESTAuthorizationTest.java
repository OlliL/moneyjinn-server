package org.laladev.moneyjinn.core.rest.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import org.junit.Assert;
import org.junit.Test;

public class RESTAuthorizationTest {

	public RESTAuthorization restAuthorization = new RESTAuthorization();

	@Test
	public void testRESTAuthorization_emptyBody()
			throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
		final byte[] secret = new String("bda87124768b63ab0c5d802fd4d509b227e7ef4c").getBytes();
		final String httpVerb = "GET";
		final String contentType = "application/json";
		final String url = "/moneyflow/server/user/getUserSettingsForStartup/klaus";
		final String date = "Sat, 22 Aug 2015 18:02:50  GMT";
		final byte[] body = String.valueOf("").getBytes();
		final String ident = "klaus";
		final String key = this.restAuthorization.getRESTAuthorization(secret, httpVerb, contentType, url, date, body,
				ident);
		Assert.assertEquals("MNFklaus:ODg0MjgwMTBiNzhlZTI1NmU4MGE0NWYxNmVlYjYyNTEwYTM3YzJiMA==", key);
	}

	@Test
	public void testRESTAuthorization_withBody()
			throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
		final byte[] secret = new String("bda87124768b63ab0c5d802fd4d509b227e7ef4c").getBytes();
		final String httpVerb = "GET";
		final String contentType = "application/json";
		final String url = "/moneyflow/server/user/getUserSettingsForStartup/klaus";
		final String date = "Sat, 22 Aug 2015 18:02:50  GMT";
		final byte[] body = String.valueOf("{}").getBytes();
		final String ident = "klaus";
		final String key = this.restAuthorization.getRESTAuthorization(secret, httpVerb, contentType, url, date, body,
				ident);
		Assert.assertEquals("MNFklaus:MjQ1NTZlNzJiOTg5MTg1ODhjNTc1MmEyMmFiNmIxNTU2MjFiNDdmMQ==", key);
	}

	@Test
	public void testOptionalFieldsNull_noNPE()
			throws InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException {
		final byte[] secret = null;
		final String httpVerb = "GET";
		final String contentType = null;
		final String url = "/moneyflow/server/user/getUserSettingsForStartup/klaus";
		final String date = "Sat, 22 Aug 2015 18:02:50  GMT";
		final byte[] body = String.valueOf("").getBytes();
		final String ident = null;
		this.restAuthorization.getRESTAuthorization(secret, httpVerb, contentType, url, date, body, ident);
	}
}