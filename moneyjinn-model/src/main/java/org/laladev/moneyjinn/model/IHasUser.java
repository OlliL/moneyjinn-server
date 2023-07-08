package org.laladev.moneyjinn.model;

import org.laladev.moneyjinn.model.access.User;

public interface IHasUser {
  User getUser();

  void setUser(User user);
}
