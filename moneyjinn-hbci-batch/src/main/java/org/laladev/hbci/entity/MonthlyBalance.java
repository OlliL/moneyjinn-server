//
// Copyright (c) 2014-2015 Oliver Lehmann <oliver@laladev.org>
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

package org.laladev.hbci.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "monthlybalance")
public final class MonthlyBalance implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String iban;
	private String bic;
	private Integer year;
	private Integer month;
	private BigDecimal balance;

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
	public final String getIban() {
		return iban;
	}

	public final void setIban(final String iban) {
		this.iban = iban;
	}

	@Column
	public final String getBic() {
		return bic;
	}

	public final void setBic(final String bic) {
		this.bic = bic;
	}

	@Column
	public final Integer getYear() {
		return year;
	}

	public final void setYear(final Integer year) {
		this.year = year;
	}

	@Column
	public final Integer getMonth() {
		return month;
	}

	public final void setMonth(final Integer month) {
		this.month = month;
	}

	@Column
	public final BigDecimal getBalance() {
		return balance;
	}

	public final void setBalance(final BigDecimal balance) {
		this.balance = balance;
	}

}
