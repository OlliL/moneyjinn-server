//
// Copyright (c) 2023-2024 Oliver Lehmann <lehmann@ans-netz.de>
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

package org.laladev.moneyjinn.server.config.websocket;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSocketMessageBroker
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
	private final AuthChannelInterceptorAdapter authChannelInterceptorAdapter;
	@Value("${org.laladev.moneyjinn.server.websocket.heartbeat.server}")
	private long heartbeatServer;
	@Value("${org.laladev.moneyjinn.server.websocket.heartbeat.client}")
	private long heartbeatClient;

	@Override
	public void configureMessageBroker(final MessageBrokerRegistry config) {
		final ThreadPoolTaskScheduler te = new ThreadPoolTaskScheduler();
		te.setPoolSize(1);
		te.setThreadNamePrefix("wss-heartbeat-thread-");
		te.initialize();

		config.enableSimpleBroker("/topic").setTaskScheduler(te)
				.setHeartbeatValue(new long[] { this.heartbeatServer, this.heartbeatClient });
		config.setApplicationDestinationPrefixes("/app");

	}

	@Override
	public void registerStompEndpoints(final StompEndpointRegistry registry) {
		registry.addEndpoint("/websocket").setAllowedOrigins("*");
	}

	@Override
	public void configureClientInboundChannel(final ChannelRegistration registration) {
		registration.interceptors(this.authChannelInterceptorAdapter);
	}

}