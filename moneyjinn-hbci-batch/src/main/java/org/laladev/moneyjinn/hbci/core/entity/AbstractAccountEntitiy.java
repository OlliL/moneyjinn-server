//
//Copyright (c) 2015 Oliver Lehmann <oliver@laladev.org>
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
package org.laladev.moneyjinn.hbci.core.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class AbstractAccountEntitiy {
	private Integer id;
	private String myIban;
	private String myBic;
	private Long myAccountnumber;
	private Integer myBankcode;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	public final Integer getId() {
		return id;
	}

	public final void setId(final Integer id) {
		this.id = id;
	}

	@Column
	public final String getMyIban() {
		return myIban;
	}

	public final void setMyIban(final String myIban) {
		this.myIban = myIban;
	}

	@Column
	public final String getMyBic() {
		return myBic;
	}

	public final void setMyBic(final String myBic) {
		this.myBic = myBic;
	}

	@Column
	public final Long getMyAccountnumber() {
		return myAccountnumber;
	}

	public final void setMyAccountnumber(final Long myAccountnumber) {
		this.myAccountnumber = myAccountnumber;
	}

	@Column
	public final Integer getMyBankcode() {
		return myBankcode;
	}

	public final void setMyBankcode(final Integer myBankcode) {
		this.myBankcode = myBankcode;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((myAccountnumber == null) ? 0 : myAccountnumber.hashCode());
		result = prime * result + ((myBankcode == null) ? 0 : myBankcode.hashCode());
		result = prime * result + ((myBic == null) ? 0 : myBic.hashCode());
		result = prime * result + ((myIban == null) ? 0 : myIban.hashCode());
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
		if (getClass() != obj.getClass()) {
			return false;
		}
		final AbstractAccountEntitiy other = (AbstractAccountEntitiy) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		if (myAccountnumber == null) {
			if (other.myAccountnumber != null) {
				return false;
			}
		} else if (!myAccountnumber.equals(other.myAccountnumber)) {
			return false;
		}
		if (myBankcode == null) {
			if (other.myBankcode != null) {
				return false;
			}
		} else if (!myBankcode.equals(other.myBankcode)) {
			return false;
		}
		if (myBic == null) {
			if (other.myBic != null) {
				return false;
			}
		} else if (!myBic.equals(other.myBic)) {
			return false;
		}
		if (myIban == null) {
			if (other.myIban != null) {
				return false;
			}
		} else if (!myIban.equals(other.myIban)) {
			return false;
		}
		return true;
	}

}
