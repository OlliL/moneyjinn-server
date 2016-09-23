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

package org.laladev.moneyjinn.businesslogic.dao.data;

import java.time.LocalDate;

public class AccessFlattenedData {
	private Long id;
	private LocalDate validFrom;
	private LocalDate validTil;
	private Long idLevel1;
	private Long idLevel2;
	private Long idLevel3;
	private Long idLevel4;
	private Long idLevel5;

	public final Long getId() {
		return this.id;
	}

	public final void setId(final Long id) {
		this.id = id;
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

	public final Long getIdLevel1() {
		return this.idLevel1;
	}

	public final void setIdLevel1(final Long idLevel1) {
		this.idLevel1 = idLevel1;
	}

	public final Long getIdLevel2() {
		return this.idLevel2;
	}

	public final void setIdLevel2(final Long idLevel2) {
		this.idLevel2 = idLevel2;
	}

	public final Long getIdLevel3() {
		return this.idLevel3;
	}

	public final void setIdLevel3(final Long idLevel3) {
		this.idLevel3 = idLevel3;
	}

	public final Long getIdLevel4() {
		return this.idLevel4;
	}

	public final void setIdLevel4(final Long idLevel4) {
		this.idLevel4 = idLevel4;
	}

	public final Long getIdLevel5() {
		return this.idLevel5;
	}

	public final void setIdLevel5(final Long idLevel5) {
		this.idLevel5 = idLevel5;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("AccessFlattenedData [id=").append(this.id).append(", validFrom=").append(this.validFrom)
				.append(", validTil=").append(this.validTil).append(", idLevel1=").append(this.idLevel1)
				.append(", idLevel2=").append(this.idLevel2).append(", idLevel3=").append(this.idLevel3)
				.append(", idLevel4=").append(this.idLevel4).append(", idLevel5=").append(this.idLevel5).append("]");
		return builder.toString();
	}

}
