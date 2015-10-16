package org.laladev.moneyjinn.businesslogic.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.laladev.moneyjinn.businesslogic.model.capitalsource.Capitalsource;

public class ImportedBalance {
	private Capitalsource capitalsource;
	private LocalDateTime date;
	private BigDecimal balance;

	public final Capitalsource getCapitalsource() {
		return this.capitalsource;
	}

	public final void setCapitalsource(final Capitalsource capitalsource) {
		this.capitalsource = capitalsource;
	}

	public final LocalDateTime getDate() {
		return this.date;
	}

	public final void setDate(final LocalDateTime date) {
		this.date = date;
	}

	public final BigDecimal getBalance() {
		return this.balance;
	}

	public final void setBalance(final BigDecimal balance) {
		this.balance = balance;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.balance == null) ? 0 : this.balance.hashCode());
		result = prime * result + ((this.capitalsource == null) ? 0 : this.capitalsource.hashCode());
		result = prime * result + ((this.date == null) ? 0 : this.date.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		final ImportedBalance other = (ImportedBalance) obj;
		if (this.balance == null) {
			if (other.balance != null) {
				return false;
			}
		} else if (!this.balance.equals(other.balance)) {
			return false;
		}
		if (this.capitalsource == null) {
			if (other.capitalsource != null) {
				return false;
			}
		} else if (!this.capitalsource.equals(other.capitalsource)) {
			return false;
		}
		if (this.date == null) {
			if (other.date != null) {
				return false;
			}
		} else if (!this.date.equals(other.date)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("ImportedBalance [capitalsource=");
		builder.append(this.capitalsource);
		builder.append(", date=");
		builder.append(this.date);
		builder.append(", balance=");
		builder.append(this.balance);
		builder.append("]");
		return builder.toString();
	}

}
