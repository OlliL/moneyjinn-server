package org.laladev.moneyjinn.service.event;

import org.laladev.moneyjinn.model.capitalsource.Capitalsource;
import org.springframework.context.ApplicationEvent;

public class CapitalsourceChangedEvent extends ApplicationEvent {
  private static final long serialVersionUID = 1L;
  private final EventType eventType;
  private final Capitalsource capitalsource;

  public CapitalsourceChangedEvent(final Object source, final EventType eventType,
      final Capitalsource capitalsource) {
    super(source);
    this.eventType = eventType;
    this.capitalsource = capitalsource;
  }

  public EventType getEventType() {
    return this.eventType;
  }

  public Capitalsource getCapitalsource() {
    return this.capitalsource;
  }
}
