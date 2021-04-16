package org.laladev.moneyjinn.server.controller.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.laladev.moneyjinn.core.rest.model.etf.ListEtfFlowsResponse;
import org.laladev.moneyjinn.core.rest.model.etf.ListEtfOverviewResponse;
import org.laladev.moneyjinn.core.rest.model.etf.transport.EtfFlowTransport;
import org.laladev.moneyjinn.core.rest.model.etf.transport.EtfSummaryTransport;
import org.laladev.moneyjinn.core.rest.model.etf.transport.EtfTransport;
import org.laladev.moneyjinn.model.etf.Etf;
import org.laladev.moneyjinn.model.etf.EtfFlow;
import org.laladev.moneyjinn.model.etf.EtfValue;
import org.laladev.moneyjinn.server.annotation.RequiresAuthorization;
import org.laladev.moneyjinn.server.controller.mapper.EtfFlowTransportMapper;
import org.laladev.moneyjinn.server.controller.mapper.EtfTransportMapper;
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
		final Month month = Month.of(requestMonth.intValue());
		final LocalDate beginOfMonth = LocalDate.of(requestYear.intValue(), month, 1);
		final LocalDate endOfMonth = beginOfMonth.with(TemporalAdjusters.lastDayOfMonth());

		final ListEtfOverviewResponse response = new ListEtfOverviewResponse();
		final List<EtfSummaryTransport> transports = new ArrayList<>();

		final List<Etf> etfs = this.etfService.getAllEtf();
		for (final Etf etf : etfs) {
			final EtfValue etfValue = this.etfService.getEtfValueEndOfMonth(etf.getId(), requestYear, month);
			final List<EtfFlow> etfFlows = this.etfService.getAllEtfFlowsUntil(etf.getId(), endOfMonth);

			final EtfSummaryTransport transport = new EtfSummaryTransport();
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

		response.setEtfSummaryTransports(transports);
		return response;
	}

	@RequestMapping(value = "listEtfFlows", method = { RequestMethod.GET })
	@RequiresAuthorization
	public ListEtfFlowsResponse listEtfFlows() {
		final ListEtfFlowsResponse response = new ListEtfFlowsResponse();

		final List<Etf> etfs = this.etfService.getAllEtf();
		response.setEtfTransports(super.mapList(etfs, EtfTransport.class));

		final List<EtfFlowTransport> transports = new ArrayList<>();
		for (final Etf etf : etfs) {
			final List<EtfFlow> etfFlows = this.etfService.getAllEtfFlowsUntil(etf.getId(), LocalDate.now());
			transports.addAll(super.mapList(etfFlows, EtfFlowTransport.class));
		}
		response.setEtfFlowTransports(transports);

		return response;
	}

	@Override
	protected void addBeanMapper() {
		super.registerBeanMapper(new EtfFlowTransportMapper());
		super.registerBeanMapper(new EtfTransportMapper());

	}

}
