package org.laladev.moneyjinn.businesslogic.model.capitalsource;

import java.time.LocalDate;

import org.laladev.moneyjinn.businesslogic.model.AbstractEntity;
import org.laladev.moneyjinn.businesslogic.model.BankAccount;
import org.laladev.moneyjinn.businesslogic.model.access.Group;
import org.laladev.moneyjinn.businesslogic.model.access.User;

public class Capitalsource extends AbstractEntity<CapitalsourceID> {
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
	private boolean importAllowed;

	public Capitalsource(final CapitalsourceID id) {
		super.setId(id);
	}

	public final User getUser() {
		return this.user;
	}

	public final void setUser(final User user) {
		this.user = user;
	}

	public final Group getAccess() {
		return this.access;
	}

	public final void setAccess(final Group access) {
		this.access = access;
	}

	public final CapitalsourceType getType() {
		return this.type;
	}

	public final void setType(final CapitalsourceType type) {
		this.type = type;
	}

	public final CapitalsourceState getState() {
		return this.state;
	}

	public final void setState(final CapitalsourceState state) {
		this.state = state;
	}

	public final BankAccount getBankAccount() {
		return this.bankAccount;
	}

	public final void setBankAccount(final BankAccount bankAccount) {
		this.bankAccount = bankAccount;
	}

	public final String getComment() {
		return this.comment;
	}

	public final void setComment(final String comment) {
		this.comment = comment;
	}

	public final LocalDate getValidTil() {
		return this.validTil;
	}

	public final void setValidTil(final LocalDate validTil) {
		this.validTil = validTil;
	}

	public final LocalDate getValidFrom() {
		return this.validFrom;
	}

	public final void setValidFrom(final LocalDate validFrom) {
		this.validFrom = validFrom;
	}

	public final boolean isGroupUse() {
		return this.groupUse;
	}

	public final void setGroupUse(final boolean groupUse) {
		this.groupUse = groupUse;
	}

	public final boolean isImportAllowed() {
		return this.importAllowed;
	}

	public final void setImportAllowed(final boolean importAllowed) {
		this.importAllowed = importAllowed;
	}

}
