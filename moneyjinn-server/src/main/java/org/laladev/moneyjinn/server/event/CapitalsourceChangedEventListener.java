package org.laladev.moneyjinn.server.event;

import org.laladev.moneyjinn.core.rest.model.wsevent.CapitalsourceChangedEventTransport;
import org.laladev.moneyjinn.server.controller.mapper.CapitalsourceTransportMapper;
import org.laladev.moneyjinn.service.event.CapitalsourceChangedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class CapitalsourceChangedEventListener
    implements ApplicationListener<CapitalsourceChangedEvent> {

  @Autowired
  SimpMessagingTemplate simpMessagingTemplate;
  private final CapitalsourceTransportMapper mapper = new CapitalsourceTransportMapper();

  @Override
  public void onApplicationEvent(final CapitalsourceChangedEvent event) {
    final CapitalsourceChangedEventTransport eventTransport = new CapitalsourceChangedEventTransport();

    eventTransport.setEventType(event.getEventType().name());
    eventTransport.setCapitalsourceTransport(this.mapper.mapAToB(event.getCapitalsource()));

    this.simpMessagingTemplate.convertAndSend("/topic/capitalsourceChanged", eventTransport);
  }

}
