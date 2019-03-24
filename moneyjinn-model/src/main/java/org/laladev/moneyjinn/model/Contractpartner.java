//
// Copyright (c) 2015 Oliver Lehmann <lehmann@ans-netz.de>
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions
// are met:
// 1. Redistributions of source code must retain the above copyright
// notice, this list of conditions and the following disclaimer
// 2. Redistributions in binary form must reproduce the above copyright
// notice, this list of conditions and the following disclaimer in the
// documentation and/or other materials provided with the distribution.
//
// THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS'' AND
// ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE LIABLE
// FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
// DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
// OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
// HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
// LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
// OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE.
//

package org.laladev.moneyjinn.model;

import java.time.LocalDate;

import org.laladev.moneyjinn.model.access.Group;
import org.laladev.moneyjinn.model.access.User;

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

	public Contractpartner() {
		// Default Constructor because ID can be empty for newly created Contractpartner.
	}

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
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((this.access == null) ? 0 : this.access.hashCode());
		result = prime * result + ((this.country == null) ? 0 : this.country.hashCode());
		result = prime * result + ((this.moneyflowComment == null) ? 0 : this.moneyflowComment.hashCode());
		result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
		result = prime * result + ((this.postcode == null) ? 0 : this.postcode.hashCode());
		result = prime * result + ((this.postingAccount == null) ? 0 : this.postingAccount.hashCode());
		result = prime * result + ((this.street == null) ? 0 : this.street.hashCode());
		result = prime * result + ((this.town == null) ? 0 : this.town.hashCode());
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
		final Contractpartner other = (Contractpartner) obj;
		if (this.access == null) {
			if (other.access != null) {
				return false;
			}
		} else if (!this.access.equals(other.access)) {
			return false;
		}
		if (this.country == null) {
			if (other.country != null) {
				return false;
			}
		} else if (!this.country.equals(other.country)) {
			return false;
		}
		if (this.moneyflowComment == null) {
			if (other.moneyflowComment != null) {
				return false;
			}
		} else if (!this.moneyflowComment.equals(other.moneyflowComment)) {
			return false;
		}
		if (this.name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!this.name.equals(other.name)) {
			return false;
		}
		if (this.postcode == null) {
			if (other.postcode != null) {
				return false;
			}
		} else if (!this.postcode.equals(other.postcode)) {
			return false;
		}
		if (this.postingAccount == null) {
			if (other.postingAccount != null) {
				return false;
			}
		} else if (!this.postingAccount.equals(other.postingAccount)) {
			return false;
		}
		if (this.street == null) {
			if (other.street != null) {
				return false;
			}
		} else if (!this.street.equals(other.street)) {
			return false;
		}
		if (this.town == null) {
			if (other.town != null) {
				return false;
			}
		} else if (!this.town.equals(other.town)) {
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
