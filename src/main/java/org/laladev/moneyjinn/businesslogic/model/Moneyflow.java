package org.laladev.moneyjinn.businesslogic.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.laladev.moneyjinn.businesslogic.model.access.Group;
import org.laladev.moneyjinn.businesslogic.model.access.User;
import org.laladev.moneyjinn.businesslogic.model.capitalsource.Capitalsource;

public class Moneyflow extends AbstractEntity<MoneyflowID> {
	private static final long serialVersionUID = 1L;
	private User user;
	private Group group;
	private LocalDate bookingDate;
	private LocalDate invoiceDate;
	private BigDecimal amount;
	private Capitalsource capitalsource;
	private Contractpartner contractpartner;
	private String comment;
	private boolean privat;
	private PostingAccount postingAccount;

	public final User getUser() {
		return this.user;
	}

	public final void setUser(final User user) {
		this.user = user;
	}

	public final Group getGroup() {
		return this.group;
	}

	public final void setGroup(final Group group) {
		this.group = group;
	}

	public final LocalDate getBookingDate() {
		return this.bookingDate;
	}

	public final void setBookingDate(final LocalDate bookingDate) {
		this.bookingDate = bookingDate;
	}

	public final LocalDate getInvoiceDate() {
		return this.invoiceDate;
	}

	public final void setInvoiceDate(final LocalDate invoiceDate) {
		this.invoiceDate = invoiceDate;
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

	public final boolean isPrivat() {
		return this.privat;
	}

	public final void setPrivat(final boolean privat) {
		this.privat = privat;
	}

	public final PostingAccount getPostingAccount() {
		return this.postingAccount;
	}

	public final void setPostingAccount(final PostingAccount postingAccount) {
		this.postingAccount = postingAccount;
	}

}
