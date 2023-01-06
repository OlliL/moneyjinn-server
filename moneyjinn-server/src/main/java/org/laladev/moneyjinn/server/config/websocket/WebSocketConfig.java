package org.laladev.moneyjinn.server.config.websocket;

import jakarta.inject.Inject;
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

@Configuration
@EnableWebSocketMessageBroker
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
  @Inject
  private AuthChannelInterceptorAdapter authChannelInterceptorAdapter;
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
    // FIX for https://github.com/spring-projects/spring-security/issues/12378
    registry.addEndpoint("/websocket").addInterceptors(new EagerCsrfTokenHandshakeInterceptor())
        .setAllowedOrigins("*");
  }

  @Override
  public void configureClientInboundChannel(final ChannelRegistration registration) {
    registration.interceptors(this.authChannelInterceptorAdapter);
  }

}