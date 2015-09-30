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

package org.laladev.moneyjinn.server.controller.impl;

import org.laladev.moneyjinn.core.rest.model.ValidationResponse;
import org.laladev.moneyjinn.core.rest.model.monthlysettlement.ShowMonthlySettlementCreateResponse;
import org.laladev.moneyjinn.core.rest.model.monthlysettlement.ShowMonthlySettlementDeleteResponse;
import org.laladev.moneyjinn.core.rest.model.monthlysettlement.ShowMonthlySettlementListResponse;
import org.laladev.moneyjinn.core.rest.model.monthlysettlement.UpsertMonthlySettlementRequest;
import org.laladev.moneyjinn.server.annotation.RequiresAuthorization;
import org.laladev.moneyjinn.server.controller.mapper.ImportedMonthlySettlementTransportMapper;
import org.laladev.moneyjinn.server.controller.mapper.MonthlySettlementTransportMapper;
import org.laladev.moneyjinn.server.controller.mapper.ValidationItemTransportMapper;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Transactional(propagation = Propagation.REQUIRES_NEW)
@RequestMapping("/moneyflow/server/monthlysettlement/")
public class MonthlySettlementController extends AbstractController {

	@Override
	protected void addBeanMapper() {
		this.registerBeanMapper(new MonthlySettlementTransportMapper());
		this.registerBeanMapper(new ImportedMonthlySettlementTransportMapper());
		this.registerBeanMapper(new ValidationItemTransportMapper());
	}

	@RequestMapping(value = "showMonthlySettlementList", method = { RequestMethod.GET })
	@RequiresAuthorization
	public ShowMonthlySettlementListResponse showMonthlySettlementList() {
		return this.showMonthlySettlementList(null, null);
	}

	@RequestMapping(value = "showMonthlySettlementList/{year}", method = { RequestMethod.GET })
	@RequiresAuthorization
	public ShowMonthlySettlementListResponse showMonthlySettlementList(@PathVariable(value = "year") final Short year) {
		return this.showMonthlySettlementList(year, null);
	}

	@RequestMapping(value = "showMonthlySettlementList/{year}/{month}", method = { RequestMethod.GET })
	@RequiresAuthorization
	public ShowMonthlySettlementListResponse showMonthlySettlementList(@PathVariable(value = "year") final Short year,
			@PathVariable(value = "month") final Short month) {
		return null;
	}

	@RequestMapping(value = "showMonthlySettlementCreate", method = { RequestMethod.GET })
	@RequiresAuthorization
	public ShowMonthlySettlementCreateResponse showMonthlySettlementCreate() {
		return this.showMonthlySettlementCreate(null, null);
	}

	@RequestMapping(value = "showMonthlySettlementCreate/{year}/{month}", method = { RequestMethod.GET })
	@RequiresAuthorization
	public ShowMonthlySettlementCreateResponse showMonthlySettlementCreate(
			@PathVariable(value = "year") final Short year, @PathVariable(value = "month") final Short month) {
		return null;
	}

	@RequestMapping(value = "showMonthlySettlementDelete/{year}/{month}", method = { RequestMethod.GET })
	@RequiresAuthorization
	public ShowMonthlySettlementDeleteResponse showMonthlySettlementDelete(
			@PathVariable(value = "year") final Short year, @PathVariable(value = "month") final Short month) {
		return null;
	}

	@RequestMapping(value = "upsertMonthlySettlement", method = { RequestMethod.POST })
	@RequiresAuthorization
	public ValidationResponse upsertMonthlySettlement(@RequestBody final UpsertMonthlySettlementRequest request) {
		return null;
	}

	@RequestMapping(value = "deleteMonthlySettlement/{year}/{month}", method = { RequestMethod.DELETE })
	@RequiresAuthorization
	public void deleteMonthlySettlement(@PathVariable(value = "year") final Short year,
			@PathVariable(value = "month") final Short month) {
	}
}
