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

	public Capitalsource() {
	}

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

	public final boolean isAsset() {
		return this.getType() == CapitalsourceType.CURRENT_ASSET || this.getType() == CapitalsourceType.LONG_TERM_ASSET;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((this.access == null) ? 0 : this.access.hashCode());
		result = prime * result + ((this.bankAccount == null) ? 0 : this.bankAccount.hashCode());
		result = prime * result + ((this.comment == null) ? 0 : this.comment.hashCode());
		result = prime * result + (this.groupUse ? 1231 : 1237);
		result = prime * result + (this.importAllowed ? 1231 : 1237);
		result = prime * result + ((this.state == null) ? 0 : this.state.hashCode());
		result = prime * result + ((this.type == null) ? 0 : this.type.hashCode());
		result = prime * result + ((this.user == null) ? 0 : this.user.hashCode());
		result = prime * result + ((this.validFrom == null) ? 0 : this.validFrom.hashCode());
		result = prime * result + ((this.validTil == null) ? 0 : this.validTil.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		final Capitalsource other = (Capitalsource) obj;
		if (this.access == null) {
			if (other.access != null) {
				return false;
			}
		} else if (!this.access.equals(other.access)) {
			return false;
		}
		if (this.bankAccount == null) {
			if (other.bankAccount != null) {
				return false;
			}
		} else if (!this.bankAccount.equals(other.bankAccount)) {
			return false;
		}
		if (this.comment == null) {
			if (other.comment != null) {
				return false;
			}
		} else if (!this.comment.equals(other.comment)) {
			return false;
		}
		if (this.groupUse != other.groupUse) {
			return false;
		}
		if (this.importAllowed != other.importAllowed) {
			return false;
		}
		if (this.state != other.state) {
			return false;
		}
		if (this.type != other.type) {
			return false;
		}
		if (this.user == null) {
			if (other.user != null) {
				return false;
			}
		} else if (!this.user.equals(other.user)) {
			return false;
		}
		if (this.validFrom == null) {
			if (other.validFrom != null) {
				return false;
			}
		} else if (!this.validFrom.equals(other.validFrom)) {
			return false;
		}
		if (this.validTil == null) {
			if (other.validTil != null) {
				return false;
			}
		} else if (!this.validTil.equals(other.validTil)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("Capitalsource [user=");
		builder.append(this.user);
		builder.append(", access=");
		builder.append(this.access);
		builder.append(", type=");
		builder.append(this.type);
		builder.append(", state=");
		builder.append(this.state);
		builder.append(", bankAccount=");
		builder.append(this.bankAccount);
		builder.append(", comment=");
		builder.append(this.comment);
		builder.append(", validTil=");
		builder.append(this.validTil);
		builder.append(", validFrom=");
		builder.append(this.validFrom);
		builder.append(", groupUse=");
		builder.append(this.groupUse);
		builder.append(", importAllowed=");
		builder.append(this.importAllowed);
		builder.append(", getId()=");
		builder.append(this.getId());
		builder.append("]");
		return builder.toString();
	}

}
