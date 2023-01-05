package org.laladev.moneyjinn.server.config.websocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.web.socket.EnableWebSocketSecurity;
import org.springframework.security.messaging.access.intercept.MessageMatcherDelegatingAuthorizationManager;

@Configuration
@EnableWebSocketSecurity
public class WebSocketSecurityConfig {

  @Bean
  AuthorizationManager<Message<?>> messageAuthorizationManager(
      final MessageMatcherDelegatingAuthorizationManager.Builder messages) {
    messages.nullDestMatcher().authenticated();
    messages.anyMessage().authenticated();

    return messages.build();
  }

}

// @Configuration
// public class WebSocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {
//
// @Override
// protected void configureInbound(final MessageSecurityMetadataSourceRegistry messages) {
// messages.anyMessage().authenticated();
// }
//
// @Override
// protected boolean sameOriginDisabled() {
// return true;
// }
//
// }