package org.laladev.moneyjinn.config;

import org.laladev.moneyjinn.server.model.LoginRequest;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class JwtCache {
    private final ConcurrentHashMap<LoginRequest, String> jwtCache = new ConcurrentHashMap<>();

    public String getJwt(final LoginRequest req) {
        return this.jwtCache.get(req);
    }

    public void putJwt(final LoginRequest req, final String jwt) {
        this.jwtCache.put(req, jwt);
    }
}
