package org.laladev.moneyjinn.server.config.websocket;

import jakarta.inject.Inject;
import org.laladev.moneyjinn.server.jwt.JwtTokenProvider;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class AuthChannelInterceptorAdapter implements ChannelInterceptor {
  @Inject
  private JwtTokenProvider jwtTokenProvider;

  @Override
  public Message<?> preSend(final Message<?> message, final MessageChannel channel)
      throws AuthenticationException {

    final StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message,
        StompHeaderAccessor.class);

    if (StompCommand.CONNECT == accessor.getCommand()) {
      String token = null;
      final String bearerToken = accessor.getFirstNativeHeader("Authorization");
      if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
        token = bearerToken.substring(7, bearerToken.length());
      }
      if (token != null && this.jwtTokenProvider.validateToken(token)) {
        final Authentication auth = this.jwtTokenProvider.getAuthentication(token);
        accessor.setUser(auth);
      }
    }
    return message;
  }
}