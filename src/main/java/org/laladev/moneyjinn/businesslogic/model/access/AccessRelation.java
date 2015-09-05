package org.laladev.moneyjinn.businesslogic.model.access;

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

import java.time.LocalDate;

import org.laladev.moneyjinn.businesslogic.model.AbstractEntity;

public class AccessRelation extends AbstractEntity<AccessID> implements Cloneable {
	private static final long serialVersionUID = 1L;
	private AccessRelation parentAccessRelation;
	private LocalDate validFrom;
	private LocalDate validTil;

	public AccessRelation(final AccessID id) {
		super.setId(id);
	}

	public AccessRelation(final AccessID id, final AccessRelation parentAccessRelation, final LocalDate validFrom,
			final LocalDate validTil) {
		super.setId(id);
		this.parentAccessRelation = parentAccessRelation;
		this.validFrom = validFrom;
		this.validTil = validTil;
	}

	public AccessRelation(final AccessID id, final AccessRelation parentAccessRelation) {
		super.setId(id);
		this.parentAccessRelation = parentAccessRelation;
	}

	public AccessRelation() {
	}

	public final AccessRelation getParentAccessRelation() {
		return this.parentAccessRelation;
	}

	public final void setParentAccessRelation(final AccessRelation parentAccessRelation) {
		this.parentAccessRelation = parentAccessRelation;
	}

	public final LocalDate getValidFrom() {
		return this.validFrom;
	}

	public final void setValidFrom(final LocalDate validFrom) {
		this.validFrom = validFrom;
	}

	public final LocalDate getValidTil() {
		return this.validTil;
	}

	public final void setValidTil(final LocalDate validTil) {
		this.validTil = validTil;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("AccessRelation [refId=").append(this.parentAccessRelation).append(", validFrom=")
				.append(this.validFrom).append(", validTil=").append(this.validTil).append(", getId()=")
				.append(this.getId()).append("]");
		return builder.toString();
	}

	@Override
	public AccessRelation clone() throws CloneNotSupportedException {
		final AccessRelation accessRelation = (AccessRelation) super.clone();
		accessRelation.setId(this.getId().clone());
		if (this.getParentAccessRelation() != null) {
			accessRelation.setParentAccessRelation(this.getParentAccessRelation().clone());
		}
		return accessRelation;
	}
}
