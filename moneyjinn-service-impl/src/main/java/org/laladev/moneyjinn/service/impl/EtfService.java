//
// Copyright (c) 2021 Oliver Lehmann <lehmann@ans-netz.de>
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

package org.laladev.moneyjinn.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.model.etf.Etf;
import org.laladev.moneyjinn.model.etf.EtfFlow;
import org.laladev.moneyjinn.model.etf.EtfFlowID;
import org.laladev.moneyjinn.model.etf.EtfIsin;
import org.laladev.moneyjinn.model.etf.EtfValue;
import org.laladev.moneyjinn.model.exception.BusinessException;
import org.laladev.moneyjinn.model.validation.ValidationResult;
import org.laladev.moneyjinn.model.validation.ValidationResultItem;
import org.laladev.moneyjinn.service.api.IEtfService;
import org.laladev.moneyjinn.service.dao.EtfDao;
import org.laladev.moneyjinn.service.dao.data.EtfData;
import org.laladev.moneyjinn.service.dao.data.EtfFlowData;
import org.laladev.moneyjinn.service.dao.data.EtfValueData;
import org.laladev.moneyjinn.service.dao.data.mapper.EtfDataMapper;
import org.laladev.moneyjinn.service.dao.data.mapper.EtfFlowDataMapper;
import org.laladev.moneyjinn.service.dao.data.mapper.EtfValueDataMapper;
import org.springframework.util.Assert;

@Named
public class EtfService extends AbstractService implements IEtfService {

	@Inject
	private EtfDao etfDao;

	@Override
	protected void addBeanMapper() {
		super.registerBeanMapper(new EtfFlowDataMapper());
		super.registerBeanMapper(new EtfValueDataMapper());
		super.registerBeanMapper(new EtfDataMapper());
	}

	@Override
	public List<Etf> getAllEtf() {
		final List<EtfData> etfData = this.etfDao.getAllEtf();
		return super.mapList(etfData, Etf.class);
	}

	@Override
	public List<EtfFlow> getAllEtfFlowsUntil(final EtfIsin isin, final LocalDateTime timeUntil) {
		final List<EtfFlowData> etfFlowData = this.etfDao.getAllFlowsUntil(isin.getId(), timeUntil);
		return super.mapList(etfFlowData, EtfFlow.class);
	}

	@Override
	public EtfValue getEtfValueEndOfMonth(final EtfIsin isin, final Short year, final Month month) {
		final EtfValueData etfValueData = this.etfDao.getEtfValueForMonth(isin.getId(), year, month);
		return super.map(etfValueData, EtfValue.class);
	}

	@Override
	public ValidationResult validateEtfFlow(final EtfFlow etfFlow) {
		Assert.notNull(etfFlow, "etfFlow must not be null!");

		final ValidationResult validationResult = new ValidationResult();

		if (etfFlow.getTime() == null) {
			validationResult.addValidationResultItem(
					new ValidationResultItem(etfFlow.getId(), ErrorCode.BOOKINGDATE_IN_WRONG_FORMAT));
		}

		if (etfFlow.getAmount() == null || etfFlow.getAmount().compareTo(BigDecimal.ZERO) == 0) {
			validationResult
					.addValidationResultItem(new ValidationResultItem(etfFlow.getId(), ErrorCode.PIECES_NOT_SET));
		}

		if (etfFlow.getPrice() == null || etfFlow.getPrice().compareTo(BigDecimal.ZERO) == 0) {
			validationResult
					.addValidationResultItem(new ValidationResultItem(etfFlow.getId(), ErrorCode.PRICE_NOT_SET));
		}

		if (etfFlow.getIsin() == null || etfFlow.getIsin().getId().trim().isEmpty()) {
			validationResult
					.addValidationResultItem(new ValidationResultItem(etfFlow.getId(), ErrorCode.NO_ETF_SPECIFIED));
		} else {
			final EtfData etfData = this.etfDao.getEtfById(etfFlow.getIsin().getId());
			if (etfData == null) {
				validationResult
						.addValidationResultItem(new ValidationResultItem(etfFlow.getId(), ErrorCode.NO_ETF_SPECIFIED));
			}
		}

		return validationResult;
	}

	@Override
	public EtfFlow getEtfFlowById(final EtfFlowID etfFlowId) {
		Assert.notNull(etfFlowId, "etfFlowId must not be null!");
		final EtfFlowData etfFlowData = this.etfDao.getEtfFowById(etfFlowId.getId());
		return super.map(etfFlowData, EtfFlow.class);
	}

	@Override
	public EtfFlowID createEtfFlow(final EtfFlow etfFlow) {
		final ValidationResult validationResult = this.validateEtfFlow(etfFlow);

		if (!validationResult.isValid() && !validationResult.getValidationResultItems().isEmpty()) {
			final ValidationResultItem validationResultItem = validationResult.getValidationResultItems().get(0);
			throw new BusinessException("EtfFlow creation failed!", validationResultItem.getError());
		}

		final EtfFlowData etfFlowData = super.map(etfFlow, EtfFlowData.class);
		final Long etfFlowId = this.etfDao.createEtfFlow(etfFlowData);

		return new EtfFlowID(etfFlowId);
	}

	@Override
	public void updateEtfFlow(final EtfFlow etfFlow) {
		final ValidationResult validationResult = this.validateEtfFlow(etfFlow);

		if (!validationResult.isValid() && !validationResult.getValidationResultItems().isEmpty()) {
			final ValidationResultItem validationResultItem = validationResult.getValidationResultItems().get(0);
			throw new BusinessException("EtfFlow update failed!", validationResultItem.getError());
		}

		final EtfFlowData etfFlowData = super.map(etfFlow, EtfFlowData.class);
		this.etfDao.updateEtfFlow(etfFlowData);

	}

	@Override
	public void deleteEtfFlow(final EtfFlowID etfFlowId) {
		Assert.notNull(etfFlowId, "etfFlowId must not be null!");

		this.etfDao.deleteEtfFlow(etfFlowId.getId());
	}
}
