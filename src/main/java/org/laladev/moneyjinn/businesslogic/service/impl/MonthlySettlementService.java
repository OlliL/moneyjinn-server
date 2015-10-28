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

package org.laladev.moneyjinn.businesslogic.service.impl;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;

import org.laladev.moneyjinn.businesslogic.dao.MonthlySettlementDao;
import org.laladev.moneyjinn.businesslogic.dao.data.MonthlySettlementData;
import org.laladev.moneyjinn.businesslogic.dao.data.mapper.MonthlySettlementDataMapper;
import org.laladev.moneyjinn.businesslogic.model.access.Group;
import org.laladev.moneyjinn.businesslogic.model.access.GroupID;
import org.laladev.moneyjinn.businesslogic.model.access.User;
import org.laladev.moneyjinn.businesslogic.model.access.UserID;
import org.laladev.moneyjinn.businesslogic.model.capitalsource.Capitalsource;
import org.laladev.moneyjinn.businesslogic.model.capitalsource.CapitalsourceID;
import org.laladev.moneyjinn.businesslogic.model.monthlysettlement.MonthlySettlement;
import org.laladev.moneyjinn.businesslogic.model.validation.ValidationResult;
import org.laladev.moneyjinn.businesslogic.model.validation.ValidationResultItem;
import org.laladev.moneyjinn.businesslogic.service.api.IAccessRelationService;
import org.laladev.moneyjinn.businesslogic.service.api.ICapitalsourceService;
import org.laladev.moneyjinn.businesslogic.service.api.IMonthlySettlementService;
import org.laladev.moneyjinn.businesslogic.service.api.IUserService;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.springframework.util.Assert;

@Named
public class MonthlySettlementService extends AbstractService implements IMonthlySettlementService {
	@Inject
	MonthlySettlementDao monthlySettlementDao;
	@Inject
	IUserService userService;
	@Inject
	ICapitalsourceService capitalsourceService;
	@Inject
	IAccessRelationService accessRelationService;

	@Override
	protected void addBeanMapper() {
		super.registerBeanMapper(new MonthlySettlementDataMapper());
	}

	private ValidationResult validateMonthlySettlement(final MonthlySettlement monthlySettlement) {
		Assert.notNull(monthlySettlement);
		Assert.notNull(monthlySettlement.getCapitalsource());
		Assert.notNull(monthlySettlement.getCapitalsource().getId());
		Assert.notNull(monthlySettlement.getUser());
		Assert.notNull(monthlySettlement.getUser().getId());
		Assert.notNull(monthlySettlement.getGroup());
		Assert.notNull(monthlySettlement.getGroup().getId());

		final ValidationResult validationResult = new ValidationResult();

		final Capitalsource capitalsource = this.capitalsourceService.getCapitalsourceById(
				monthlySettlement.getUser().getId(), monthlySettlement.getGroup().getId(),
				monthlySettlement.getCapitalsource().getId());

		// You must not change, or create MonthlySettlements for Capitalsources not belonging to
		// you.
		if (capitalsource == null || !capitalsource.getUser().getId().equals(monthlySettlement.getUser().getId())) {
			validationResult.addValidationResultItem(
					new ValidationResultItem(monthlySettlement.getId(), ErrorCode.CAPITALSOURCE_DOES_NOT_EXIST));
		}
		return validationResult;
	}

	private final MonthlySettlement mapMonthlySettlementData(final MonthlySettlementData monthlySettlementData) {
		if (monthlySettlementData != null) {
			final MonthlySettlement monthlySettlement = super.map(monthlySettlementData, MonthlySettlement.class);
			final UserID userId = monthlySettlement.getUser().getId();
			final User user = this.userService.getUserById(userId);
			final LocalDate endOfMonth = LocalDate.of(monthlySettlement.getYear(), monthlySettlement.getMonth(), 1)
					.with(TemporalAdjusters.lastDayOfMonth());
			final Group accessor = this.accessRelationService.getAccessor(userId, endOfMonth);
			final GroupID groupId = accessor.getId();
			monthlySettlement.setUser(user);
			monthlySettlement.setGroup(accessor);

			Capitalsource capitalsource = monthlySettlement.getCapitalsource();
			if (capitalsource != null) {
				final CapitalsourceID capitalsourceId = capitalsource.getId();
				capitalsource = this.capitalsourceService.getCapitalsourceById(userId, groupId, capitalsourceId);
				monthlySettlement.setCapitalsource(capitalsource);
			}

			return monthlySettlement;
		}
		return null;
	}

	private final List<MonthlySettlement> mapMonthlySettlementDataList(
			final List<MonthlySettlementData> monthlySettlementDataList) {
		return monthlySettlementDataList.stream().map(element -> this.mapMonthlySettlementData(element))
				.collect(Collectors.toCollection(ArrayList::new));
	}

	@Override
	public List<Short> getAllYears(final UserID userId) {
		Assert.notNull(userId);

		return this.monthlySettlementDao.getAllYears(userId.getId());
	}

	@Override
	public List<Month> getAllMonth(final UserID userId, final Short year) {
		Assert.notNull(userId);
		Assert.notNull(year);

		final List<Short> allMonths = this.monthlySettlementDao.getAllMonth(userId.getId(), year);
		if (allMonths != null) {
			return allMonths.stream().map(m -> Month.of(m.intValue())).collect(Collectors.toCollection(ArrayList::new));
		}
		return new ArrayList<>();
	}

	@Override
	public List<MonthlySettlement> getAllMonthlySettlementsByYearMonth(final UserID userId, final Short year,
			final Month month) {
		Assert.notNull(userId);
		Assert.notNull(year);
		Assert.notNull(month);

		final List<MonthlySettlementData> monthlySettlementDatas = this.monthlySettlementDao
				.getAllMonthlySettlementsByYearMonth(userId.getId(), year, (short) month.getValue());

		return this.mapMonthlySettlementDataList(monthlySettlementDatas);
	}

	@Override
	public LocalDate getMaxSettlementDate(final UserID userId) {
		Assert.notNull(userId);

		final Date maxSettlementDate = this.monthlySettlementDao.getMaxSettlementDate(userId.getId());
		if (maxSettlementDate != null) {
			return maxSettlementDate.toLocalDate();
		}
		return null;
	}

	@Override
	public boolean checkMonthlySettlementsExists(final UserID userId, final Short year, final Month month) {
		Assert.notNull(userId);
		Assert.notNull(year);
		Assert.notNull(month);

		return this.monthlySettlementDao.checkMonthlySettlementsExists(userId.getId(), year, (short) month.getValue());
	}

	@Override
	public ValidationResult upsertMonthlySettlements(final List<MonthlySettlement> monthlySettlements) {
		Assert.notNull(monthlySettlements);

		final ValidationResult validationResult = new ValidationResult();
		monthlySettlements.stream()
				.forEach(ms -> validationResult.mergeValidationResult(this.validateMonthlySettlement(ms)));

		if (validationResult.isValid()) {
			final List<MonthlySettlementData> monthlySettlementDatas = super.mapList(monthlySettlements,
					MonthlySettlementData.class);

			monthlySettlementDatas.stream().forEach(msd -> this.monthlySettlementDao.upsertMonthlySettlement(msd));
		}

		return validationResult;
	}

	@Override
	public void deleteMonthlySettlement(final UserID userId, final Short year, final Month month) {
		Assert.notNull(userId);
		Assert.notNull(year);
		Assert.notNull(month);

		this.monthlySettlementDao.deleteMonthlySettlement(userId.getId(), year, (short) month.getValue());
	}

	@Override
	public List<MonthlySettlement> getAllMonthlySettlementsByRangeAndCapitalsource(final UserID userId,
			final LocalDate begin, final LocalDate end, final List<CapitalsourceID> capitalsourceIds) {
		Assert.notNull(userId);
		Assert.notNull(begin);
		Assert.notNull(end);
		Assert.notEmpty(capitalsourceIds);

		final List<Long> capitalsourceIdLongs = capitalsourceIds.stream().map(CapitalsourceID::getId)
				.collect(Collectors.toCollection(ArrayList::new));
		final List<MonthlySettlementData> monthlySettlementDatas = this.monthlySettlementDao
				.getAllMonthlySettlementsByRangeAndCapitalsource(userId.getId(), begin.getYear(), begin.getMonthValue(),
						end.getYear(), end.getMonthValue(), capitalsourceIdLongs);

		return this.mapMonthlySettlementDataList(monthlySettlementDatas);
	}

}
