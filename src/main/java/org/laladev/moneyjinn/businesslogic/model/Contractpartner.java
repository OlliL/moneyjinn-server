package org.laladev.moneyjinn.businesslogic.model;

import java.time.LocalDate;

import org.laladev.moneyjinn.businesslogic.model.access.Group;
import org.laladev.moneyjinn.businesslogic.model.access.User;

public class Contractpartner extends AbstractEntity<ContractpartnerID> {
	private static final long serialVersionUID = 1L;
	private User user;
	private Group access;
	private String name;
	private String street;
	private Integer postcode;
	private String town;
	private String country;
	private String moneyflowComment;
	private PostingAccount postingAccount;
	private LocalDate validTil;
	private LocalDate validFrom;

	public Contractpartner(final ContractpartnerID contractpartnerID) {
		super.setId(contractpartnerID);
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

	public final String getName() {
		return this.name;
	}

	public final void setName(final String name) {
		this.name = name;
	}

	public final String getStreet() {
		return this.street;
	}

	public final void setStreet(final String street) {
		this.street = street;
	}

	public final Integer getPostcode() {
		return this.postcode;
	}

	public final void setPostcode(final Integer postcode) {
		this.postcode = postcode;
	}

	public final String getTown() {
		return this.town;
	}

	public final void setTown(final String town) {
		this.town = town;
	}

	public final String getCountry() {
		return this.country;
	}

	public final void setCountry(final String country) {
		this.country = country;
	}

	public final String getMoneyflowComment() {
		return this.moneyflowComment;
	}

	public final void setMoneyflowComment(final String moneyflowComment) {
		this.moneyflowComment = moneyflowComment;
	}

	public final PostingAccount getPostingAccount() {
		return this.postingAccount;
	}

	public final void setPostingAccount(final PostingAccount postingAccount) {
		this.postingAccount = postingAccount;
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

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("Contractpartner [user=");
		builder.append(this.user);
		builder.append(", access=");
		builder.append(this.access);
		builder.append(", name=");
		builder.append(this.name);
		builder.append(", street=");
		builder.append(this.street);
		builder.append(", postcode=");
		builder.append(this.postcode);
		builder.append(", town=");
		builder.append(this.town);
		builder.append(", country=");
		builder.append(this.country);
		builder.append(", moneyflowComment=");
		builder.append(this.moneyflowComment);
		builder.append(", postingAccount=");
		builder.append(this.postingAccount);
		builder.append(", validTil=");
		builder.append(this.validTil);
		builder.append(", validFrom=");
		builder.append(this.validFrom);
		builder.append(", getId()=");
		builder.append(this.getId());
		builder.append("]");
		return builder.toString();
	}

}
