package org.laladev.moneyjinn.server.builder;

import java.math.BigDecimal;

import org.laladev.moneyjinn.core.rest.model.transport.ImportedMonthlySettlementTransport;

public class ImportedMonthlySettlementTransportBuilder extends ImportedMonthlySettlementTransport {

	public static final Long IMPORTED_MONTHLYSETTLEMENT1_ID = 1l;
	public static final Long NON_EXISTING_ID = 666l;
	public static final Long NEXT_ID = 2l;

	public ImportedMonthlySettlementTransportBuilder withAmount(final BigDecimal amount) {
		super.setAmount(amount);
		return this;
	}

	public ImportedMonthlySettlementTransportBuilder withMonth(final int month) {
		super.setMonth((short) month);
		return this;
	}

	public ImportedMonthlySettlementTransportBuilder withYear(final int year) {
		super.setYear((short) year);
		return this;
	}

	public ImportedMonthlySettlementTransportBuilder withId(final Long id) {
		super.setId(id);
		return this;
	}

	public ImportedMonthlySettlementTransportBuilder forImportedMonthlySettlement1() {
		super.setId(IMPORTED_MONTHLYSETTLEMENT1_ID);
		super.setExternalid("A");
		super.setUserid(UserTransportBuilder.USER3_ID);
		super.setAmount(new BigDecimal("9.00"));
		super.setMonth((short) 1);
		super.setYear((short) 2009);
		super.setBankCodeCapitalsource(CapitalsourceTransportBuilder.CAPITALSOURCE4_BANKCODE);
		super.setAccountNumberCapitalsource(CapitalsourceTransportBuilder.CAPITALSOURCE4_ACCOUNTNUMBER);
		return this;
	}

	public ImportedMonthlySettlementTransportBuilder forNewImportedMonthlySettlement() {
		super.setId(NON_EXISTING_ID);
		super.setExternalid("B");
		super.setUserid(UserTransportBuilder.USER3_ID);
		super.setAmount(BigDecimal.valueOf(1000l));
		super.setMonth((short) 2);
		super.setYear((short) 2009);
		super.setBankCodeCapitalsource(CapitalsourceTransportBuilder.CAPITALSOURCE4_BANKCODE);
		super.setAccountNumberCapitalsource(CapitalsourceTransportBuilder.CAPITALSOURCE4_ACCOUNTNUMBER);
		return this;
	}

	public ImportedMonthlySettlementTransport build() {
		final ImportedMonthlySettlementTransport transport = new ImportedMonthlySettlementTransport();

		transport.setId(super.getId());
		transport.setExternalid(super.getExternalid());
		transport.setUserid(super.getUserid());
		transport.setAmount(super.getAmount());
		transport.setMonth(super.getMonth());
		transport.setYear(super.getYear());
		transport.setBankCodeCapitalsource(super.getBankCodeCapitalsource());
		transport.setAccountNumberCapitalsource(super.getAccountNumberCapitalsource());

		return transport;
	}
}
