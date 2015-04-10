package org.laladev.hbci.entity;

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
