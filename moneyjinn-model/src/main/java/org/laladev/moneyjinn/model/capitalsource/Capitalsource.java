
package org.laladev.moneyjinn.model.capitalsource;

import java.time.LocalDate;

import org.laladev.moneyjinn.model.AbstractEntity;
import org.laladev.moneyjinn.model.BankAccount;
import org.laladev.moneyjinn.model.IHasBankAccount;
import org.laladev.moneyjinn.model.IHasUser;
import org.laladev.moneyjinn.model.access.Group;
import org.laladev.moneyjinn.model.access.User;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
public class Capitalsource extends AbstractEntity<CapitalsourceID> implements IHasBankAccount, IHasUser {
	private static final long serialVersionUID = 1L;
	private User user;
	private Group access;
	private CapitalsourceType type;
	private CapitalsourceState state;
	private BankAccount bankAccount;
	private String comment;
	private LocalDate validTil;
	private LocalDate validFrom;
	private boolean groupUse;
	private CapitalsourceImport importAllowed;

	public Capitalsource(final CapitalsourceID id) {
		super.setId(id);
	}

	public final boolean isAsset() {
		return this.getType() == CapitalsourceType.CURRENT_ASSET || this.getType() == CapitalsourceType.LONG_TERM_ASSET;
	}
}
