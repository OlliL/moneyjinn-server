package org.laladev.moneyjinn.server.builder;

import java.math.BigDecimal;

import org.laladev.moneyjinn.core.rest.model.report.transport.ReportTurnoverCapitalsourceTransport;

public class ReportTurnoverCapitalsourceTransportBuilder extends ReportTurnoverCapitalsourceTransport {

	public ReportTurnoverCapitalsourceTransportBuilder forReport_Capitalsource6() {

		super.setCapitalsourceType(CapitalsourceTransportBuilder.CAPITALSOURCE6_TYPE);
		super.setCapitalsourceState(CapitalsourceTransportBuilder.CAPITALSOURCE6_STATE);
		super.setCapitalsourceComment(CapitalsourceTransportBuilder.CAPITALSOURCE6_COMMENT);
		super.setAmountBeginOfMonthFixed(BigDecimal.ZERO);
		super.setAmountEndOfMonthCalculated(BigDecimal.ZERO);
		super.setAmountEndOfMonthFixed(null);

		return this;
	}

	public ReportTurnoverCapitalsourceTransportBuilder withAmountCurrentZero() {
		super.setAmountCurrent(BigDecimal.ZERO);
		return this;
	}

	public ReportTurnoverCapitalsourceTransportBuilder forReport_2008_12_Capitalsource1() {

		super.setCapitalsourceType(CapitalsourceTransportBuilder.CAPITALSOURCE1_TYPE);
		super.setCapitalsourceState(CapitalsourceTransportBuilder.CAPITALSOURCE1_STATE);
		super.setCapitalsourceComment(CapitalsourceTransportBuilder.CAPITALSOURCE1_COMMENT);
		super.setAmountBeginOfMonthFixed(new BigDecimal("-0.10"));
		super.setAmountEndOfMonthCalculated(MonthlySettlementTransportBuilder.MONTHLYSETTLEMENT1_AMOUNT);
		super.setAmountEndOfMonthFixed(MonthlySettlementTransportBuilder.MONTHLYSETTLEMENT1_AMOUNT);

		return this;
	}

	public ReportTurnoverCapitalsourceTransportBuilder forReport_2008_12_Capitalsource2() {

		super.setCapitalsourceType(CapitalsourceTransportBuilder.CAPITALSOURCE2_TYPE);
		super.setCapitalsourceState(CapitalsourceTransportBuilder.CAPITALSOURCE2_STATE);
		super.setCapitalsourceComment(CapitalsourceTransportBuilder.CAPITALSOURCE2_COMMENT);
		super.setAmountBeginOfMonthFixed(MonthlySettlementTransportBuilder.MONTHLYSETTLEMENT2_AMOUNT);
		super.setAmountEndOfMonthCalculated(MonthlySettlementTransportBuilder.MONTHLYSETTLEMENT2_AMOUNT);
		super.setAmountEndOfMonthFixed(MonthlySettlementTransportBuilder.MONTHLYSETTLEMENT2_AMOUNT);

		return this;
	}

	public ReportTurnoverCapitalsourceTransportBuilder forReport_2008_12_Capitalsource4() {

		super.setCapitalsourceType(CapitalsourceTransportBuilder.CAPITALSOURCE4_TYPE);
		super.setCapitalsourceState(CapitalsourceTransportBuilder.CAPITALSOURCE4_STATE);
		super.setCapitalsourceComment(CapitalsourceTransportBuilder.CAPITALSOURCE4_COMMENT);
		super.setAmountBeginOfMonthFixed(MonthlySettlementTransportBuilder.MONTHLYSETTLEMENT3_AMOUNT);
		super.setAmountEndOfMonthCalculated(MonthlySettlementTransportBuilder.MONTHLYSETTLEMENT3_AMOUNT);
		super.setAmountEndOfMonthFixed(MonthlySettlementTransportBuilder.MONTHLYSETTLEMENT3_AMOUNT);

		return this;
	}

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

	public ReportTurnoverCapitalsourceTransportBuilder forReport_2009_01_Capitalsource4() {

		super.setCapitalsourceType(CapitalsourceTransportBuilder.CAPITALSOURCE4_TYPE);
		super.setCapitalsourceState(CapitalsourceTransportBuilder.CAPITALSOURCE4_STATE);
		super.setCapitalsourceComment(CapitalsourceTransportBuilder.CAPITALSOURCE4_COMMENT);
		super.setAmountBeginOfMonthFixed(MonthlySettlementTransportBuilder.MONTHLYSETTLEMENT3_AMOUNT);
		super.setAmountEndOfMonthCalculated(MonthlySettlementTransportBuilder.MONTHLYSETTLEMENT3_AMOUNT);
		super.setAmountEndOfMonthFixed(MonthlySettlementTransportBuilder.MONTHLYSETTLEMENT3_AMOUNT);

		return this;
	}

	public ReportTurnoverCapitalsourceTransportBuilder forReport_2009_12_Capitalsource1() {

		super.setCapitalsourceType(CapitalsourceTransportBuilder.CAPITALSOURCE1_TYPE);
		super.setCapitalsourceState(CapitalsourceTransportBuilder.CAPITALSOURCE1_STATE);
		super.setCapitalsourceComment(CapitalsourceTransportBuilder.CAPITALSOURCE1_COMMENT);
		super.setAmountBeginOfMonthFixed(new BigDecimal("8.90"));
		super.setAmountEndOfMonthCalculated(new BigDecimal("8.90"));
		super.setAmountEndOfMonthFixed(new BigDecimal("8.90"));

		return this;
	}

	public ReportTurnoverCapitalsourceTransportBuilder forReport_2009_12_Capitalsource2() {

		super.setCapitalsourceType(CapitalsourceTransportBuilder.CAPITALSOURCE2_TYPE);
		super.setCapitalsourceState(CapitalsourceTransportBuilder.CAPITALSOURCE2_STATE);
		super.setCapitalsourceComment(CapitalsourceTransportBuilder.CAPITALSOURCE2_COMMENT);
		super.setAmountBeginOfMonthFixed(new BigDecimal("100.00"));
		super.setAmountEndOfMonthCalculated(new BigDecimal("110.00"));
		super.setAmountEndOfMonthFixed(new BigDecimal("110.00"));

		return this;
	}

	public ReportTurnoverCapitalsourceTransportBuilder forReport_2009_12_Capitalsource4() {

		super.setCapitalsourceType(CapitalsourceTransportBuilder.CAPITALSOURCE4_TYPE);
		super.setCapitalsourceState(CapitalsourceTransportBuilder.CAPITALSOURCE4_STATE);
		super.setCapitalsourceComment(CapitalsourceTransportBuilder.CAPITALSOURCE4_COMMENT);
		super.setAmountBeginOfMonthFixed(MonthlySettlementTransportBuilder.MONTHLYSETTLEMENT3_AMOUNT);
		super.setAmountEndOfMonthCalculated(MonthlySettlementTransportBuilder.MONTHLYSETTLEMENT3_AMOUNT);
		super.setAmountEndOfMonthFixed(MonthlySettlementTransportBuilder.MONTHLYSETTLEMENT3_AMOUNT);

		return this;
	}

	public ReportTurnoverCapitalsourceTransportBuilder forReport_2010_01_Capitalsource1() {

		super.setCapitalsourceType(CapitalsourceTransportBuilder.CAPITALSOURCE1_TYPE);
		super.setCapitalsourceState(CapitalsourceTransportBuilder.CAPITALSOURCE1_STATE);
		super.setCapitalsourceComment(CapitalsourceTransportBuilder.CAPITALSOURCE1_COMMENT);
		super.setAmountBeginOfMonthFixed(new BigDecimal("8.90"));
		super.setAmountEndOfMonthCalculated(new BigDecimal("8.90"));
		super.setAmountEndOfMonthFixed(new BigDecimal("8.90"));

		return this;
	}

	public ReportTurnoverCapitalsourceTransportBuilder forReport_2010_01_Capitalsource2() {

		super.setCapitalsourceType(CapitalsourceTransportBuilder.CAPITALSOURCE2_TYPE);
		super.setCapitalsourceState(CapitalsourceTransportBuilder.CAPITALSOURCE2_STATE);
		super.setCapitalsourceComment(CapitalsourceTransportBuilder.CAPITALSOURCE2_COMMENT);
		super.setAmountBeginOfMonthFixed(new BigDecimal("110.00"));
		super.setAmountEndOfMonthCalculated(new BigDecimal("100.00"));
		super.setAmountEndOfMonthFixed(new BigDecimal("100.00"));

		return this;
	}

	public ReportTurnoverCapitalsourceTransportBuilder forReport_2010_01_Capitalsource4() {

		super.setCapitalsourceType(CapitalsourceTransportBuilder.CAPITALSOURCE4_TYPE);
		super.setCapitalsourceState(CapitalsourceTransportBuilder.CAPITALSOURCE4_STATE);
		super.setCapitalsourceComment(CapitalsourceTransportBuilder.CAPITALSOURCE4_COMMENT);
		super.setAmountBeginOfMonthFixed(MonthlySettlementTransportBuilder.MONTHLYSETTLEMENT3_AMOUNT);
		super.setAmountEndOfMonthCalculated(MonthlySettlementTransportBuilder.MONTHLYSETTLEMENT3_AMOUNT);

		return this;
	}

	public ReportTurnoverCapitalsourceTransportBuilder forReport_2010_02_Capitalsource1() {

		super.setCapitalsourceType(CapitalsourceTransportBuilder.CAPITALSOURCE1_TYPE);
		super.setCapitalsourceState(CapitalsourceTransportBuilder.CAPITALSOURCE1_STATE);
		super.setCapitalsourceComment(CapitalsourceTransportBuilder.CAPITALSOURCE1_COMMENT);
		super.setAmountBeginOfMonthFixed(new BigDecimal("8.90"));
		super.setAmountEndOfMonthCalculated(new BigDecimal("8.90"));
		super.setAmountEndOfMonthFixed(new BigDecimal("8.90"));

		return this;
	}

	public ReportTurnoverCapitalsourceTransportBuilder forReport_2010_02_Capitalsource2() {

		super.setCapitalsourceType(CapitalsourceTransportBuilder.CAPITALSOURCE2_TYPE);
		super.setCapitalsourceState(CapitalsourceTransportBuilder.CAPITALSOURCE2_STATE);
		super.setCapitalsourceComment(CapitalsourceTransportBuilder.CAPITALSOURCE2_COMMENT);
		super.setAmountBeginOfMonthFixed(new BigDecimal("100.00"));
		super.setAmountEndOfMonthCalculated(new BigDecimal("110.00"));
		super.setAmountEndOfMonthFixed(new BigDecimal("110.00"));

		return this;
	}

	public ReportTurnoverCapitalsourceTransportBuilder forReport_2010_02_Capitalsource4() {

		super.setCapitalsourceType(CapitalsourceTransportBuilder.CAPITALSOURCE4_TYPE);
		super.setCapitalsourceState(CapitalsourceTransportBuilder.CAPITALSOURCE4_STATE);
		super.setCapitalsourceComment(CapitalsourceTransportBuilder.CAPITALSOURCE4_COMMENT);
		super.setAmountBeginOfMonthFixed(BigDecimal.ZERO);
		super.setAmountEndOfMonthCalculated(BigDecimal.ZERO);

		return this;
	}

	public ReportTurnoverCapitalsourceTransportBuilder forReport_2010_03_Capitalsource1() {

		super.setCapitalsourceType(CapitalsourceTransportBuilder.CAPITALSOURCE1_TYPE);
		super.setCapitalsourceState(CapitalsourceTransportBuilder.CAPITALSOURCE1_STATE);
		super.setCapitalsourceComment(CapitalsourceTransportBuilder.CAPITALSOURCE1_COMMENT);
		super.setAmountBeginOfMonthFixed(new BigDecimal("8.90"));
		super.setAmountEndOfMonthCalculated(new BigDecimal("8.90"));
		super.setAmountEndOfMonthFixed(new BigDecimal("8.90"));

		return this;
	}

	public ReportTurnoverCapitalsourceTransportBuilder forReport_2010_03_Capitalsource2() {

		super.setCapitalsourceType(CapitalsourceTransportBuilder.CAPITALSOURCE2_TYPE);
		super.setCapitalsourceState(CapitalsourceTransportBuilder.CAPITALSOURCE2_STATE);
		super.setCapitalsourceComment(CapitalsourceTransportBuilder.CAPITALSOURCE2_COMMENT);
		super.setAmountBeginOfMonthFixed(new BigDecimal("110.00"));
		super.setAmountEndOfMonthCalculated(new BigDecimal("100.00"));
		super.setAmountEndOfMonthFixed(new BigDecimal("100.00"));

		return this;
	}

	public ReportTurnoverCapitalsourceTransportBuilder forReport_2010_03_Capitalsource4() {

		super.setCapitalsourceType(CapitalsourceTransportBuilder.CAPITALSOURCE4_TYPE);
		super.setCapitalsourceState(CapitalsourceTransportBuilder.CAPITALSOURCE4_STATE);
		super.setCapitalsourceComment(CapitalsourceTransportBuilder.CAPITALSOURCE4_COMMENT);
		super.setAmountBeginOfMonthFixed(BigDecimal.ZERO);
		super.setAmountEndOfMonthCalculated(BigDecimal.ZERO);
		super.setAmountEndOfMonthFixed(MonthlySettlementTransportBuilder.MONTHLYSETTLEMENT3_AMOUNT);

		return this;
	}

	public ReportTurnoverCapitalsourceTransportBuilder forReport_2010_05_Capitalsource1() {

		super.setCapitalsourceType(CapitalsourceTransportBuilder.CAPITALSOURCE1_TYPE);
		super.setCapitalsourceState(CapitalsourceTransportBuilder.CAPITALSOURCE1_STATE);
		super.setCapitalsourceComment(CapitalsourceTransportBuilder.CAPITALSOURCE1_COMMENT);
		super.setAmountBeginOfMonthFixed(BigDecimal.ZERO);
		super.setAmountEndOfMonthCalculated(BigDecimal.ZERO);
		super.setAmountCurrent(BigDecimal.ZERO);

		return this;
	}

	public ReportTurnoverCapitalsourceTransportBuilder forReport_2010_05_Capitalsource2() {

		super.setCapitalsourceType(CapitalsourceTransportBuilder.CAPITALSOURCE2_TYPE);
		super.setCapitalsourceState(CapitalsourceTransportBuilder.CAPITALSOURCE2_STATE);
		super.setCapitalsourceComment(CapitalsourceTransportBuilder.CAPITALSOURCE2_COMMENT);
		super.setAmountBeginOfMonthFixed(new BigDecimal("110.00"));
		super.setAmountEndOfMonthCalculated(new BigDecimal("100.00"));

		return this;
	}

	@SuppressWarnings("deprecation")
	public ReportTurnoverCapitalsourceTransportBuilder forReport_2010_05_Capitalsource4() {

		super.setCapitalsourceType(CapitalsourceTransportBuilder.CAPITALSOURCE4_TYPE);
		super.setCapitalsourceState(CapitalsourceTransportBuilder.CAPITALSOURCE4_STATE);
		super.setCapitalsourceComment(CapitalsourceTransportBuilder.CAPITALSOURCE4_COMMENT);
		super.setAmountBeginOfMonthFixed(MonthlySettlementTransportBuilder.MONTHLYSETTLEMENT3_AMOUNT);
		super.setAmountEndOfMonthCalculated(MonthlySettlementTransportBuilder.MONTHLYSETTLEMENT3_AMOUNT);

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
