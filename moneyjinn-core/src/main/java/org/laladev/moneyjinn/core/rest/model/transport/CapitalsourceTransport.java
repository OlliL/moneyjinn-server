package org.laladev.moneyjinn.core.rest.model.transport;

//
//Copyright (c) 2014-2015 Oliver Lehmann <oliver@laladev.org>
//All rights reserved.
//
//Redistribution and use in source and binary forms, with or without
//modification, are permitted provided that the following conditions
//are met:
//1. Redistributions of source code must retain the above copyright
//notice, this list of conditions and the following disclaimer
//2. Redistributions in binary form must reproduce the above copyright
//notice, this list of conditions and the following disclaimer in the
//documentation and/or other materials provided with the distribution.
//
//THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS'' AND
//ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
//IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
//ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE LIABLE
//FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
//DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
//OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
//HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
//LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
//OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
//SUCH DAMAGE.
//
//$Id: CapitalsourceTransport.java,v 1.5 2015/08/24 17:24:29 olivleh1 Exp $
//

import java.sql.Date;

public class CapitalsourceTransport {
	private Long id;
	private Long userid;
	private Short type;
	private Short state;
	private String accountNumber;
	private String bankCode;
	private String comment;
	private Date validTil;
	private Date validFrom;
	private Short groupUse;
	private Short importAllowed;

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

	public final Short getType() {
		return this.type;
	}

	public final void setType(final Short type) {
		this.type = type;
	}

	public final Short getState() {
		return this.state;
	}

	public final void setState(final Short state) {
		this.state = state;
	}

	public final String getAccountNumber() {
		return this.accountNumber;
	}

	public final void setAccountNumber(final String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public final String getBankCode() {
		return this.bankCode;
	}

	public final void setBankCode(final String bankCode) {
		this.bankCode = bankCode;
	}

	public final String getComment() {
		return this.comment;
	}

	public final void setComment(final String comment) {
		this.comment = comment;
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

	public final Short getGroupUse() {
		return this.groupUse;
	}

	public final void setGroupUse(final Short groupUse) {
		this.groupUse = groupUse;
	}

	public final Short getImportAllowed() {
		return this.importAllowed;
	}

	public final void setImportAllowed(final Short importAllowed) {
		this.importAllowed = importAllowed;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.accountNumber == null) ? 0 : this.accountNumber.hashCode());
		result = prime * result + ((this.bankCode == null) ? 0 : this.bankCode.hashCode());
		result = prime * result + ((this.comment == null) ? 0 : this.comment.hashCode());
		result = prime * result + ((this.groupUse == null) ? 0 : this.groupUse.hashCode());
		result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
		result = prime * result + ((this.importAllowed == null) ? 0 : this.importAllowed.hashCode());
		result = prime * result + ((this.state == null) ? 0 : this.state.hashCode());
		result = prime * result + ((this.type == null) ? 0 : this.type.hashCode());
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
		final CapitalsourceTransport other = (CapitalsourceTransport) obj;
		if (this.accountNumber == null) {
			if (other.accountNumber != null) {
				return false;
			}
		} else if (!this.accountNumber.equals(other.accountNumber)) {
			return false;
		}
		if (this.bankCode == null) {
			if (other.bankCode != null) {
				return false;
			}
		} else if (!this.bankCode.equals(other.bankCode)) {
			return false;
		}
		if (this.comment == null) {
			if (other.comment != null) {
				return false;
			}
		} else if (!this.comment.equals(other.comment)) {
			return false;
		}
		if (this.groupUse == null) {
			if (other.groupUse != null) {
				return false;
			}
		} else if (!this.groupUse.equals(other.groupUse)) {
			return false;
		}
		if (this.id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!this.id.equals(other.id)) {
			return false;
		}
		if (this.importAllowed == null) {
			if (other.importAllowed != null) {
				return false;
			}
		} else if (!this.importAllowed.equals(other.importAllowed)) {
			return false;
		}
		if (this.state == null) {
			if (other.state != null) {
				return false;
			}
		} else if (!this.state.equals(other.state)) {
			return false;
		}
		if (this.type == null) {
			if (other.type != null) {
				return false;
			}
		} else if (!this.type.equals(other.type)) {
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
		builder.append("CapitalsourceTransport [id=");
		builder.append(this.id);
		builder.append(", userid=");
		builder.append(this.userid);
		builder.append(", type=");
		builder.append(this.type);
		builder.append(", state=");
		builder.append(this.state);
		builder.append(", accountNumber=");
		builder.append(this.accountNumber);
		builder.append(", bankCode=");
		builder.append(this.bankCode);
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
		builder.append("]");
		return builder.toString();
	}

}
