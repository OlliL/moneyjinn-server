package org.laladev.moneyjinn.server.controller.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.laladev.moneyjinn.core.rest.model.etf.ListEtfOverviewResponse;
import org.laladev.moneyjinn.core.rest.model.etf.transport.EtfTransport;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.etf.Etf;
import org.laladev.moneyjinn.model.etf.EtfFlow;
import org.laladev.moneyjinn.model.etf.EtfValue;
import org.laladev.moneyjinn.server.annotation.RequiresAuthorization;
import org.laladev.moneyjinn.service.api.IEtfService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Transactional(propagation = Propagation.REQUIRES_NEW)
@RequestMapping("/moneyflow/server/etf/")
// TODO: Multi-User
// TODO: Multi-ETF
// TODO: ETF-Master-Data
// TODO: Entry ETF Buys/Sels
public class EtfController extends AbstractController {

	@Inject
	private IEtfService etfService;

	@RequestMapping(value = "listEtfOverview/{year}/{month}", method = { RequestMethod.GET })
	@RequiresAuthorization
	public ListEtfOverviewResponse listEtfOverview(@PathVariable(value = "year") final Short requestYear,
			@PathVariable(value = "month") final Short requestMonth) {
		final UserID userId = super.getUserId();

		final Month month = Month.of(requestMonth.intValue());
		final LocalDate beginOfMonth = LocalDate.of(requestYear.intValue(), month, 1);
		final LocalDate endOfMonth = beginOfMonth.with(TemporalAdjusters.lastDayOfMonth());

		final ListEtfOverviewResponse response = new ListEtfOverviewResponse();
		final List<EtfTransport> transports = new ArrayList<>();

		final List<Etf> etfs = this.etfService.getAllEtf();
		for (final Etf etf : etfs) {
			final EtfValue etfValue = this.etfService.getEtfValueEndOfMonth(etf.getId(), requestYear, month);
			final List<EtfFlow> etfFlows = this.etfService.getAllEtfFlowsUntil(etf.getId(), endOfMonth);

			final EtfTransport transport = new EtfTransport();
			transport.setIsin(etf.getId().getId());
			transport.setName(etf.getName());
			transport.setChartUrl(etf.getChartUrl());

			transport.setBuyPrice(etfValue.getBuyPrice());
			transport.setSellPrice(etfValue.getSellPrice());
			transport.setPricesTimestamp(Timestamp.valueOf(etfValue.getChangeDate()));

			BigDecimal amount = BigDecimal.ZERO;
			BigDecimal spentValue = BigDecimal.ZERO;
			for (final EtfFlow flow : etfFlows) {
				amount = amount.add(flow.getAmount());
				spentValue = spentValue.add(flow.getAmount().multiply(flow.getPrice()));
			}
			transport.setAmount(amount);
			transport.setSpentValue(spentValue);

			transports.add(transport);
		}

		response.setEtfTransports(transports);
		return response;
	}

	@Override
	protected void addBeanMapper() {
		// TODO Auto-generated method stub

	}

}
