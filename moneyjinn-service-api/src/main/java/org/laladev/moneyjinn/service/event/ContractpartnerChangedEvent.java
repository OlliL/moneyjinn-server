package org.laladev.moneyjinn.service.event;

import org.laladev.moneyjinn.model.Contractpartner;
import org.springframework.context.ApplicationEvent;

public class ContractpartnerChangedEvent extends ApplicationEvent {
  private static final long serialVersionUID = 1L;
  private final EventType eventType;
  private final Contractpartner contractpartner;

  public ContractpartnerChangedEvent(final Object source, final EventType eventType,
      final Contractpartner contractpartner) {
    super(source);
    this.eventType = eventType;
    this.contractpartner = contractpartner;
  }

  public EventType getEventType() {
    return this.eventType;
  }

  public Contractpartner getContractpartner() {
    return this.contractpartner;
  }
}
