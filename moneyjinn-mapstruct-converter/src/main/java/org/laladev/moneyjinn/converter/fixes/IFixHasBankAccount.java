package org.laladev.moneyjinn.converter.fixes;

import org.laladev.moneyjinn.model.IHasBankAccount;
import org.mapstruct.AfterMapping;
import org.mapstruct.MappingTarget;

public interface IFixHasBankAccount {
  // work around https://github.com/mapstruct/mapstruct/issues/1166
  @AfterMapping
  default void fixHasBankAccount(@MappingTarget final IHasBankAccount entity) {
    if (entity != null && entity.getBankAccount() != null
        && entity.getBankAccount().getAccountNumber() == null
        && entity.getBankAccount().getBankCode() == null) {
      entity.setBankAccount(null);
    }
  }
}
