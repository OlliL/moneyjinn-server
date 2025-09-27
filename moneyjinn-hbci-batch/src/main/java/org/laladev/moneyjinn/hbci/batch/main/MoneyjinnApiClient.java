//
// Copyright (c) 2023-2025 Oliver Lehmann <lehmann@ans-netz.de>
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

package org.laladev.moneyjinn.hbci.batch.main;

import org.laladev.moneyjinn.hbci.backend.ApiClient;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Builder;

public final class MoneyjinnApiClient {
    private static ApiClient apiClient;
    private static String jwtToken;

    private MoneyjinnApiClient() {
    }

    public static void initialize() {
        final Builder httpClientBuilder = HttpClient.newBuilder();
        final CookieManager cookieManager = new CookieManager();
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
