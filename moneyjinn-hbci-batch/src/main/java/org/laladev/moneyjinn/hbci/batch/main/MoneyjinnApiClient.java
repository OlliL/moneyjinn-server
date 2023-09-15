package org.laladev.moneyjinn.hbci.batch.main;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Builder;

import org.laladev.moneyjinn.hbci.backend.ApiClient;

public final class MoneyjinnApiClient {
	private static ApiClient apiClient;
	private static String jwtToken;
	private static CookieManager cookieManager;

	private MoneyjinnApiClient() {
	}

	public static void initialize() {
		final Builder httpClientBuilder = HttpClient.newBuilder();
		cookieManager = new CookieManager();
		CookieHandler.setDefault(cookieManager);
		httpClientBuilder.cookieHandler(CookieHandler.getDefault());

		apiClient = new ApiClient();
		apiClient.setHttpClientBuilder(httpClientBuilder);
		apiClient.setRequestInterceptor(builder -> {
			builder.header("Authorization", "Bearer " + jwtToken);
			cookieManager.getCookieStore().getCookies().stream().filter(cookie -> "XSRF-TOKEN".equals(cookie.getName()))
					.findFirst().ifPresent(cookie -> builder.header("X-XSRF-TOKEN", cookie.getValue()));
		});
	}

	public static ApiClient getApiClient() {
		return apiClient;
	}

	public static void setJwtToken(final String token) {
		jwtToken = token;
	}
}
