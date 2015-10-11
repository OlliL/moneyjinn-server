package org.laladev.moneyjinn.server.builder;

import java.math.BigDecimal;

import org.laladev.moneyjinn.core.rest.model.transport.MonthlySettlementTransport;

public class MonthlySettlementTransportBuilder extends MonthlySettlementTransport {

	public static final Long MONTHLYSETTLEMENT1_ID = 1l;
	public static final Long MONTHLYSETTLEMENT2_ID = 2l;
	public static final Long MONTHLYSETTLEMENT3_ID = 3l;
	public static final Long NON_EXISTING_ID = 666l;
	public static final Long NEXT_ID = 4l;

	public MonthlySettlementTransportBuilder withAmount(final BigDecimal amount) {
		super.setAmount(amount);
		return this;
	}

	public MonthlySettlementTransportBuilder forMonthlySettlement1() {
		super.setId(MONTHLYSETTLEMENT1_ID);
		super.setUserid(UserTransportBuilder.USER1_ID);
		super.setAmount(new BigDecimal("10.00"));
		super.setCapitalsourcecomment(CapitalsourceTransportBuilder.CAPITALSOURCE1_COMMENT);
		super.setCapitalsourcegroupuse(CapitalsourceTransportBuilder.CAPITALSOURCE1_GROUP_USE);
		super.setCapitalsourceid(CapitalsourceTransportBuilder.CAPITALSOURCE1_ID);
		super.setMonth((short) 12);
		super.setYear((short) 2008);
		return this;
	}

	public MonthlySettlementTransportBuilder forMonthlySettlement2() {
		super.setId(MONTHLYSETTLEMENT2_ID);
		super.setUserid(UserTransportBuilder.USER1_ID);
		super.setAmount(new BigDecimal("100.00"));
		super.setCapitalsourcecomment(CapitalsourceTransportBuilder.CAPITALSOURCE2_COMMENT);
		super.setCapitalsourcegroupuse(CapitalsourceTransportBuilder.CAPITALSOURCE2_GROUP_USE);
		super.setCapitalsourceid(CapitalsourceTransportBuilder.CAPITALSOURCE2_ID);
		super.setMonth((short) 12);
		super.setYear((short) 2008);
		return this;
	}

	public MonthlySettlementTransportBuilder forMonthlySettlement3() {
		super.setId(MONTHLYSETTLEMENT3_ID);
		super.setUserid(UserTransportBuilder.USER3_ID);
		super.setAmount(new BigDecimal("1000.00"));
		super.setCapitalsourcecomment(CapitalsourceTransportBuilder.CAPITALSOURCE4_COMMENT);
		super.setCapitalsourcegroupuse(CapitalsourceTransportBuilder.CAPITALSOURCE4_GROUP_USE);
		super.setCapitalsourceid(CapitalsourceTransportBuilder.CAPITALSOURCE4_ID);
		super.setMonth((short) 12);
		super.setYear((short) 2008);
		return this;
	}

	public MonthlySettlementTransportBuilder forNewMonthlySettlement() {
		super.setId(NON_EXISTING_ID);
		super.setUserid(UserTransportBuilder.USER3_ID);
		super.setAmount(BigDecimal.valueOf(1000l));
		super.setCapitalsourcecomment(CapitalsourceTransportBuilder.CAPITALSOURCE1_COMMENT);
		super.setCapitalsourcegroupuse(CapitalsourceTransportBuilder.CAPITALSOURCE1_GROUP_USE);
		super.setCapitalsourceid(CapitalsourceTransportBuilder.CAPITALSOURCE1_ID);
		super.setMonth((short) 1);
		super.setYear((short) 2009);
		return this;
	}

	public MonthlySettlementTransport build() {
		final MonthlySettlementTransport transport = new MonthlySettlementTransport();

		transport.setId(super.getId());
		transport.setUserid(super.getUserid());
		transport.setAmount(super.getAmount());
		transport.setCapitalsourcecomment(super.getCapitalsourcecomment());
		transport.setCapitalsourcegroupuse(super.getCapitalsourcegroupuse());
		transport.setCapitalsourceid(super.getCapitalsourceid());
		transport.setMonth(super.getMonth());
		transport.setYear(super.getYear());

		return transport;
	}
}
