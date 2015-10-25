package org.laladev.moneyjinn.server.builder;

import java.math.BigDecimal;

import org.laladev.moneyjinn.core.rest.model.transport.ReportTurnoverCapitalsourceTransport;

public class ReportTurnoverCapitalsourceTransportBuilder extends ReportTurnoverCapitalsourceTransport {

	public ReportTurnoverCapitalsourceTransportBuilder forReport_2009_01_Capitalsource1() {

		super.setCapitalsourceType(CapitalsourceTransportBuilder.CAPITALSOURCE1_TYPE);
		super.setCapitalsourceState(CapitalsourceTransportBuilder.CAPITALSOURCE1_STATE);
		super.setCapitalsourceComment(CapitalsourceTransportBuilder.CAPITALSOURCE1_COMMENT);
		super.setAmountBeginOfMonthFixed(MonthlySettlementTransportBuilder.MONTHLYSETTLEMENT1_AMOUNT);
		super.setAmountEndOfMonthCalculated(new BigDecimal("8.90"));
		super.setAmountEndOfMonthFixed(new BigDecimal("8.90"));

		return this;
	}

	public ReportTurnoverCapitalsourceTransportBuilder forReport_2009_01_Capitalsource2() {

		super.setCapitalsourceType(CapitalsourceTransportBuilder.CAPITALSOURCE2_TYPE);
		super.setCapitalsourceState(CapitalsourceTransportBuilder.CAPITALSOURCE2_STATE);
		super.setCapitalsourceComment(CapitalsourceTransportBuilder.CAPITALSOURCE2_COMMENT);
		super.setAmountBeginOfMonthFixed(MonthlySettlementTransportBuilder.MONTHLYSETTLEMENT2_AMOUNT);
		super.setAmountEndOfMonthCalculated(MonthlySettlementTransportBuilder.MONTHLYSETTLEMENT2_AMOUNT);
		super.setAmountEndOfMonthFixed(MonthlySettlementTransportBuilder.MONTHLYSETTLEMENT2_AMOUNT);

		return this;
	}

	@SuppressWarnings("deprecation")
	public ReportTurnoverCapitalsourceTransportBuilder forReport_2009_01_Capitalsource4() {

		super.setCapitalsourceType(CapitalsourceTransportBuilder.CAPITALSOURCE4_TYPE);
		super.setCapitalsourceState(CapitalsourceTransportBuilder.CAPITALSOURCE4_STATE);
		super.setCapitalsourceComment(CapitalsourceTransportBuilder.CAPITALSOURCE4_COMMENT);
		super.setAmountBeginOfMonthFixed(MonthlySettlementTransportBuilder.MONTHLYSETTLEMENT3_AMOUNT);
		super.setAmountEndOfMonthCalculated(MonthlySettlementTransportBuilder.MONTHLYSETTLEMENT3_AMOUNT);
		super.setAmountEndOfMonthFixed(MonthlySettlementTransportBuilder.MONTHLYSETTLEMENT3_AMOUNT);
		// super.setAmountCurrent(new BigDecimal("9.00"));
		// super.setAmountCurrentState(new Timestamp(109, 11, 1, 20, 20, 20, 0));

		return this;
	}

	public ReportTurnoverCapitalsourceTransport build() {
		final ReportTurnoverCapitalsourceTransport transport = new ReportTurnoverCapitalsourceTransport();

		transport.setCapitalsourceType(super.getCapitalsourceType());
		transport.setCapitalsourceState(super.getCapitalsourceState());
		transport.setCapitalsourceComment(super.getCapitalsourceComment());
		transport.setAmountBeginOfMonthFixed(super.getAmountBeginOfMonthFixed());
		transport.setAmountEndOfMonthFixed(super.getAmountEndOfMonthFixed());
		transport.setAmountEndOfMonthCalculated(super.getAmountEndOfMonthCalculated());
		transport.setAmountCurrent(super.getAmountCurrent());
		transport.setAmountCurrentState(super.getAmountCurrentState());

		return transport;
	}
}
