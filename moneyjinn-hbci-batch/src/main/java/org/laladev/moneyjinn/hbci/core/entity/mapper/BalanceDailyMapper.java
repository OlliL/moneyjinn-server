//
// Copyright (c) 2015-2025 Oliver Lehmann <lehmann@ans-netz.de>
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

package org.laladev.moneyjinn.hbci.core.entity.mapper;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.kapott.hbci.structures.Konto;
import org.kapott.hbci.structures.Saldo;
import org.kapott.hbci.structures.Value;
import org.laladev.moneyjinn.hbci.core.entity.BalanceDaily;

public class BalanceDailyMapper {
	public BalanceDaily map(final Saldo balance, final Value lineOfCredit, final Konto account) {
		final BalanceDaily balanceDaily = new BalanceDaily();
		balanceDaily.setMyIban(account.iban);
		balanceDaily.setMyBic(account.bic);
		balanceDaily.setMyAccountnumber(Long.valueOf(account.number));
		balanceDaily.setMyBankcode(Integer.valueOf(account.blz));

		balanceDaily.setBalanceCurrency(account.curr);
		balanceDaily.setBalanceAvailableValue(balance.value.getBigDecimalValue());

		if (lineOfCredit != null) {
			balanceDaily.setLineOfCreditValue(lineOfCredit.getBigDecimalValue());
		} else {
			balanceDaily.setLineOfCreditValue(BigDecimal.ZERO);
		}
		balanceDaily.setBalanceDate(LocalDate.now());
		final Instant instant = Instant.ofEpochMilli(balance.timestamp.getTime());
		balanceDaily.setLastTransactionDate(LocalDateTime.ofInstant(instant, ZoneId.systemDefault()));
		balanceDaily.setLastBalanceUpdate(LocalDateTime.now());
		return balanceDaily;
	}
}
