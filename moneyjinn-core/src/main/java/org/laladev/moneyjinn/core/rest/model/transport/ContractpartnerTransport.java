//
// Copyright (c) 2015 Oliver Lehmann <oliver@laladev.org>
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

package org.laladev.moneyjinn.core.rest.model.transport;

import java.sql.Date;

public class ContractpartnerTransport {
	private Long id;
	private Long userid;
	private String name;
	private String street;
	private Integer postcode;
	private String town;
	private Date validTil;
	private Date validFrom;
	private String country;
	private String moneyflowComment;
	private String postingAccountName;
	private Long postingAccountId;

	public ContractpartnerTransport() {
		super();
	}

	public ContractpartnerTransport(final Long id, final Long userid, final String name, final String street,
			final Integer postcode, final String town, final Date validTil, final Date validFrom, final String country,
			final String moneyflowComment, final String postingAccountName, final Long postingAccountId) {
		super();
		this.id = id;
		this.userid = userid;
		this.name = name;
		this.street = street;
		this.postcode = postcode;
		this.town = town;
		this.validTil = validTil;
		this.validFrom = validFrom;
		this.country = country;
		this.moneyflowComment = moneyflowComment;
		this.postingAccountName = postingAccountName;
		this.postingAccountId = postingAccountId;
	}

	public final Long getId() {
		return this.id;
	}

	public final void setId(final Long id) {
		this.id = id;
	}

	public final Long getUserid() {
		return this.userid;
	}

	public final void setUserid(final Long userid) {
		this.userid = userid;
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

	public final Date getValidTil() {
		return this.validTil;
	}

	public final void setValidTil(final Date validTil) {
		this.validTil = validTil;
	}

	public final Date getValidFrom() {
		return this.validFrom;
	}

	public final void setValidFrom(final Date validFrom) {
		this.validFrom = validFrom;
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

	public final String getPostingAccountName() {
		return this.postingAccountName;
	}

	public final void setPostingAccountName(final String postingAccountName) {
		this.postingAccountName = postingAccountName;
	}

	public final Long getPostingAccountId() {
		return this.postingAccountId;
	}

	public final void setPostingAccountId(final Long postingAccountId) {
		this.postingAccountId = postingAccountId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.country == null) ? 0 : this.country.hashCode());
		result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
		result = prime * result + ((this.moneyflowComment == null) ? 0 : this.moneyflowComment.hashCode());
		result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
		result = prime * result + ((this.postcode == null) ? 0 : this.postcode.hashCode());
		result = prime * result + ((this.postingAccountId == null) ? 0 : this.postingAccountId.hashCode());
		result = prime * result + ((this.postingAccountName == null) ? 0 : this.postingAccountName.hashCode());
		result = prime * result + ((this.street == null) ? 0 : this.street.hashCode());
		result = prime * result + ((this.town == null) ? 0 : this.town.hashCode());
		result = prime * result + ((this.userid == null) ? 0 : this.userid.hashCode());
		result = prime * result + ((this.validFrom == null) ? 0 : this.validFrom.hashCode());
		result = prime * result + ((this.validTil == null) ? 0 : this.validTil.hashCode());
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
		final ContractpartnerTransport other = (ContractpartnerTransport) obj;
		if (this.country == null) {
			if (other.country != null) {
				return false;
			}
		} else if (!this.country.equals(other.country)) {
			return false;
		}
		if (this.id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!this.id.equals(other.id)) {
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
		if (this.postingAccountId == null) {
			if (other.postingAccountId != null) {
				return false;
			}
		} else if (!this.postingAccountId.equals(other.postingAccountId)) {
			return false;
		}
		if (this.postingAccountName == null) {
			if (other.postingAccountName != null) {
				return false;
			}
		} else if (!this.postingAccountName.equals(other.postingAccountName)) {
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
		if (this.userid == null) {
			if (other.userid != null) {
				return false;
			}
		} else if (!this.userid.equals(other.userid)) {
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
		builder.append("ContractpartnerTransport [id=");
		builder.append(this.id);
		builder.append(", userid=");
		builder.append(this.userid);
		builder.append(", name=");
		builder.append(this.name);
		builder.append(", street=");
		builder.append(this.street);
		builder.append(", postcode=");
		builder.append(this.postcode);
		builder.append(", town=");
		builder.append(this.town);
		builder.append(", validTil=");
		builder.append(this.validTil);
		builder.append(", validFrom=");
		builder.append(this.validFrom);
		builder.append(", country=");
		builder.append(this.country);
		builder.append(", moneyflowComment=");
		builder.append(this.moneyflowComment);
		builder.append(", postingAccountName=");
		builder.append(this.postingAccountName);
		builder.append(", postingAccountId=");
		builder.append(this.postingAccountId);
		builder.append("]");
		return builder.toString();
	}

}
