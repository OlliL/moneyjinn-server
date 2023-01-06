package org.laladev.moneyjinn.service.event;

import org.laladev.moneyjinn.model.PostingAccount;
import org.springframework.context.ApplicationEvent;

public class PostingAccountChangedEvent extends ApplicationEvent {
  private static final long serialVersionUID = 1L;
  private final EventType eventType;
  private final PostingAccount postingAccount;

  public PostingAccountChangedEvent(final Object source, final EventType eventType,
      final PostingAccount postingAccount) {
    super(source);
    this.eventType = eventType;
    this.postingAccount = postingAccount;
  }

  public EventType getEventType() {
    return this.eventType;
  }

  public PostingAccount getPostingAccount() {
    return this.postingAccount;
  }
}
