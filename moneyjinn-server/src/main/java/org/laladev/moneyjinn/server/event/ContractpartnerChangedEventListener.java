package org.laladev.moneyjinn.server.event;

import org.laladev.moneyjinn.core.rest.model.wsevent.ContractpartnerChangedEventTransport;
import org.laladev.moneyjinn.server.controller.mapper.ContractpartnerTransportMapper;
import org.laladev.moneyjinn.service.event.ContractpartnerChangedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class ContractpartnerChangedEventListener
    implements ApplicationListener<ContractpartnerChangedEvent> {

  @Autowired
  SimpMessagingTemplate simpMessagingTemplate;
  private final ContractpartnerTransportMapper mapper = new ContractpartnerTransportMapper();

  @Override
  public void onApplicationEvent(final ContractpartnerChangedEvent event) {
    final ContractpartnerChangedEventTransport eventTransport = new ContractpartnerChangedEventTransport();

    eventTransport.setEventType(event.getEventType().name());
    eventTransport.setContractpartnerTransport(this.mapper.mapAToB(event.getContractpartner()));

    this.simpMessagingTemplate.convertAndSend("/topic/contractpartnerChanged", eventTransport);
  }

}
