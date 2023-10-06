
package org.laladev.moneyjinn.server.builder;

import java.math.BigDecimal;

import org.laladev.moneyjinn.server.model.CapitalsourceTransport;
import org.laladev.moneyjinn.server.model.MonthlySettlementTransport;

public class MonthlySettlementTransportBuilder extends MonthlySettlementTransport {
	public static final BigDecimal MONTHLYSETTLEMENT1_AMOUNT = new BigDecimal("10.00");
	public static final BigDecimal MONTHLYSETTLEMENT2_AMOUNT = new BigDecimal("100.00");
	public static final BigDecimal MONTHLYSETTLEMENT3_AMOUNT = new BigDecimal("1000.00");
	public static final Long MONTHLYSETTLEMENT1_ID = 1l;
	public static final Long MONTHLYSETTLEMENT2_ID = 2l;
	public static final Long MONTHLYSETTLEMENT3_ID = 3l;
	public static final Long NON_EXISTING_ID = 666l;
	public static final Long NEXT_ID = 52l;

	public MonthlySettlementTransportBuilder withCapitalsource(final CapitalsourceTransport capitalsourceTransport) {
		super.setCapitalsourcecomment(capitalsourceTransport.getComment());
		super.setCapitalsourcegroupuse(capitalsourceTransport.getGroupUse());
		super.setCapitalsourceid(capitalsourceTransport.getId());
		super.setCapitalsourcetype(capitalsourceTransport.getType());
		return this;
	}

	public MonthlySettlementTransportBuilder withAmount(final BigDecimal amount) {
		super.setAmount(amount);
		return this;
	}

	public MonthlySettlementTransportBuilder withUserId(final Long userId) {
		super.setUserid(userId);
		return this;
	}

	public MonthlySettlementTransportBuilder withMonth(final int month) {
		super.setMonth(month);
		return this;
	}

	public MonthlySettlementTransportBuilder withYear(final int year) {
		super.setYear(year);
		return this;
	}

	public MonthlySettlementTransportBuilder withId(final Long id) {
		super.setId(id);
		return this;
	}

	public MonthlySettlementTransportBuilder forMonthlySettlement1() {
		super.setId(MONTHLYSETTLEMENT1_ID);
		super.setUserid(UserTransportBuilder.USER1_ID);
		super.setAmount(MONTHLYSETTLEMENT1_AMOUNT);
		super.setCapitalsourcecomment(CapitalsourceTransportBuilder.CAPITALSOURCE1_COMMENT);
		super.setCapitalsourcegroupuse(CapitalsourceTransportBuilder.CAPITALSOURCE1_GROUP_USE);
		super.setCapitalsourceid(CapitalsourceTransportBuilder.CAPITALSOURCE1_ID);
		super.setCapitalsourcetype(CapitalsourceTransportBuilder.CAPITALSOURCE1_TYPE);
		super.setMonth(12);
		super.setYear(2008);
		return this;
	}

	public MonthlySettlementTransportBuilder forMonthlySettlement2() {
		super.setId(MONTHLYSETTLEMENT2_ID);
		super.setUserid(UserTransportBuilder.USER1_ID);
		super.setAmount(MONTHLYSETTLEMENT2_AMOUNT);
		super.setCapitalsourcecomment(CapitalsourceTransportBuilder.CAPITALSOURCE2_COMMENT);
		super.setCapitalsourcegroupuse(CapitalsourceTransportBuilder.CAPITALSOURCE2_GROUP_USE);
		super.setCapitalsourceid(CapitalsourceTransportBuilder.CAPITALSOURCE2_ID);
		super.setCapitalsourcetype(CapitalsourceTransportBuilder.CAPITALSOURCE2_TYPE);
		super.setMonth(12);
		super.setYear(2008);
		return this;
	}

	public MonthlySettlementTransportBuilder forMonthlySettlement3() {
		super.setId(MONTHLYSETTLEMENT3_ID);
		super.setUserid(UserTransportBuilder.USER3_ID);
		super.setAmount(MONTHLYSETTLEMENT3_AMOUNT);
		super.setCapitalsourcecomment(CapitalsourceTransportBuilder.CAPITALSOURCE4_COMMENT);
		super.setCapitalsourcegroupuse(CapitalsourceTransportBuilder.CAPITALSOURCE4_GROUP_USE);
		super.setCapitalsourceid(CapitalsourceTransportBuilder.CAPITALSOURCE4_ID);
		super.setCapitalsourcetype(CapitalsourceTransportBuilder.CAPITALSOURCE4_TYPE);
		super.setMonth(12);
		super.setYear(2008);
		return this;
	}

	public MonthlySettlementTransportBuilder forNewMonthlySettlement() {
		super.setId(NON_EXISTING_ID);
		super.setUserid(UserTransportBuilder.USER3_ID);
		super.setAmount(BigDecimal.valueOf(1000l));
		super.setCapitalsourcecomment(CapitalsourceTransportBuilder.CAPITALSOURCE1_COMMENT);
		super.setCapitalsourcegroupuse(CapitalsourceTransportBuilder.CAPITALSOURCE1_GROUP_USE);
		super.setCapitalsourceid(CapitalsourceTransportBuilder.CAPITALSOURCE1_ID);
		super.setCapitalsourcetype(CapitalsourceTransportBuilder.CAPITALSOURCE1_TYPE);
		super.setMonth(1);
		super.setYear(2019);
		return this;
	}

	public MonthlySettlementTransportBuilder forCapitalsource6() {
		super.setId(null);
		super.setUserid(UserTransportBuilder.USER3_ID);
		super.setAmount(BigDecimal.ZERO);
		super.setCapitalsourcecomment(CapitalsourceTransportBuilder.CAPITALSOURCE6_COMMENT);
		super.setCapitalsourcegroupuse(CapitalsourceTransportBuilder.CAPITALSOURCE6_GROUP_USE);
		super.setCapitalsourceid(CapitalsourceTransportBuilder.CAPITALSOURCE6_ID);
		super.setCapitalsourcetype(CapitalsourceTransportBuilder.CAPITALSOURCE6_TYPE);
		super.setMonth(5);
		super.setYear(2010);
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
		transport.setCapitalsourcetype(super.getCapitalsourcetype());
		transport.setMonth(super.getMonth());
		transport.setYear(super.getYear());
		return transport;
	}
}
