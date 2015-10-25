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

package org.laladev.moneyjinn.businesslogic.dao;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

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
import javax.inject.Inject;
import javax.inject.Named;

import org.laladev.moneyjinn.businesslogic.dao.data.MoneyflowData;
import org.laladev.moneyjinn.businesslogic.dao.mapper.IMoneyflowDaoMapper;

@Named
public class MoneyflowDao {

	@Inject
	IMoneyflowDaoMapper mapper;

	public Long createMoneyflow(final MoneyflowData moneyflowData) {
		this.mapper.createMoneyflow(moneyflowData);
		return moneyflowData.getId();
	}

	public MoneyflowData getMoneyflowById(final Long userId, final Long id) {
		return this.mapper.getMoneyflowById(userId, id);
	}

	public void updateMoneyflow(final MoneyflowData moneyflowData) {
		this.mapper.updateMoneyflow(moneyflowData);
	}

	public void deleteMoneyflow(final Long groupId, final Long id) {
		this.mapper.deleteMoneyflow(groupId, id);
	}

	public List<Short> getAllYears(final Long userId) {
		return this.mapper.getAllYears(userId);
	}

	public List<Short> getAllMonth(final Long userId, final Date beginOfYear, final Date endOfYear) {
		return this.mapper.getAllMonth(userId, beginOfYear, endOfYear);
	}

	public BigDecimal getSumAmountByDateRangeForCapitalsourceIds(final Long userId, final Date validFrom,
			final Date validTil, final List<Long> capitalsourceIds) {
		return this.mapper.getSumAmountByDateRangeForCapitalsourceIds(userId, validFrom, validTil, capitalsourceIds);
	}

	public List<MoneyflowData> getAllMoneyflowsByDateRange(final Long userId, final Date dateFrom, final Date dateTil) {
		return this.mapper.getAllMoneyflowsByDateRange(userId, dateFrom, dateTil);
	}

	public boolean monthHasMoneyflows(final Long userId, final Date dateFrom, final Date dateTil) {
		final Boolean result = this.mapper.monthHasMoneyflows(userId, dateFrom, dateTil);
		if (result == null) {
			return false;
		}
		return result;
	}

}
