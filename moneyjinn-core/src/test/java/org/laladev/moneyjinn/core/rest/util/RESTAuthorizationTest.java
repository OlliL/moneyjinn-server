package org.laladev.moneyjinn.core.rest.util;

import java.security.NoSuchAlgorithmException;

import org.junit.Assert;
import org.junit.Test;
import org.laladev.moneyjinn.core.rest.util.RESTAuthorization;

public class RESTAuthorizationTest {

	public RESTAuthorization restAuthorization = new RESTAuthorization();

	@Test
	public void testRESTAuthorization_empty_body() throws NoSuchAlgorithmException {
		final byte[] secret = new String("bda87124768b63ab0c5d802fd4d509b227e7ef4c").getBytes();
		final String httpVerb = "GET";
		final String contentType = null;
		final String url = "/moneyflow/server/user/getUserSettingsForStartup/klaus";
		final String date = "Sat, 22 Aug 2015 18:02:50  GMT";
		final byte[] body = String.valueOf("").getBytes();
		final String ident = "klaus";
		final String key = this.restAuthorization.getRESTAuthorization(secret, httpVerb, contentType, url, date, body,
				ident);
		Assert.assertEquals("MNFklaus:MDM2MTc1YjIyNzU3NmZhY2QwMTM3ZDVkMjZlY2RlYmIwMzQ4MjI1ZQ==", key);
	}
}