package org.laladev.moneyjinn.businesslogic.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.laladev.moneyjinn.businesslogic.model.access.User;
import org.laladev.moneyjinn.businesslogic.model.capitalsource.Capitalsource;

public class PreDefMoneyflow extends AbstractEntity<PreDefMoneyflowID> {
	private static final long serialVersionUID = 1L;
	private User user;
	private BigDecimal amount;
	private Capitalsource capitalsource;
	private Contractpartner contractpartner;
	private String comment;
	private LocalDate creationDate;
	private boolean onceAMonth;
	private LocalDate lastUsedDate;
	private PostingAccount postingAccount;

	public final User getUser() {
		return this.user;
	}

	public final void setUser(final User user) {
		this.user = user;
	}

	public final BigDecimal getAmount() {
		return this.amount;
	}

	public final void setAmount(final BigDecimal amount) {
		this.amount = amount;
	}

	public final Capitalsource getCapitalsource() {
		return this.capitalsource;
	}

	public final void setCapitalsource(final Capitalsource capitalsource) {
		this.capitalsource = capitalsource;
	}

	public final Contractpartner getContractpartner() {
		return this.contractpartner;
	}

	public final void setContractpartner(final Contractpartner contractpartner) {
		this.contractpartner = contractpartner;
	}

	public final String getComment() {
		return this.comment;
	}

	public final void setComment(final String comment) {
		this.comment = comment;
	}

	public final boolean isOnceAMonth() {
		return this.onceAMonth;
	}

	public final void setOnceAMonth(final boolean onceAMonth) {
		this.onceAMonth = onceAMonth;
	}

	public final PostingAccount getPostingAccount() {
		return this.postingAccount;
	}

	public final void setPostingAccount(final PostingAccount postingAccount) {
		this.postingAccount = postingAccount;
	}

	public final LocalDate getCreationDate() {
		return this.creationDate;
	}

	public final void setCreationDate(final LocalDate creationDate) {
		this.creationDate = creationDate;
	}

	public final LocalDate getLastUsedDate() {
		return this.lastUsedDate;
	}

	public final void setLastUsedDate(final LocalDate lastUsedDate) {
		this.lastUsedDate = lastUsedDate;
	}

}
