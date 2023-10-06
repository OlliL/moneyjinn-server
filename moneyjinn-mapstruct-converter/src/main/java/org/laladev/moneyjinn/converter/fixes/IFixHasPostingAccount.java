package org.laladev.moneyjinn.converter.fixes;

import org.laladev.moneyjinn.model.IHasPostingAccount;
import org.mapstruct.AfterMapping;
import org.mapstruct.MappingTarget;

public interface IFixHasPostingAccount {
	// work around https://github.com/mapstruct/mapstruct/issues/1166
	@AfterMapping
	default void fixHasPostingAccount(@MappingTarget final IHasPostingAccount entity) {
		if (entity.getPostingAccount() != null && entity.getPostingAccount().getId() == null) {
			entity.setPostingAccount(null);
		}
	}
}
