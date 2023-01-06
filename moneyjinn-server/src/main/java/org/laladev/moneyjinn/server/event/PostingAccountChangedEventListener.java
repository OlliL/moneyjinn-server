package org.laladev.moneyjinn.server.event;

import org.laladev.moneyjinn.core.rest.model.wsevent.PostingAccountChangedEventTransport;
import org.laladev.moneyjinn.server.controller.mapper.PostingAccountTransportMapper;
import org.laladev.moneyjinn.service.event.PostingAccountChangedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class PostingAccountChangedEventListener
    implements ApplicationListener<PostingAccountChangedEvent> {

  @Autowired
  SimpMessagingTemplate simpMessagingTemplate;
  private final PostingAccountTransportMapper mapper = new PostingAccountTransportMapper();

  @Override
  public void onApplicationEvent(final PostingAccountChangedEvent event) {
    final PostingAccountChangedEventTransport eventTransport = new PostingAccountChangedEventTransport();

    eventTransport.setEventType(event.getEventType().name());
    eventTransport.setPostingAccountTransport(this.mapper.mapAToB(event.getPostingAccount()));

    this.simpMessagingTemplate.convertAndSend("/topic/postingAccountChanged", eventTransport);
  }

}
