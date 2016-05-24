package org.laladev.moneyjinn.server.builder;

import java.math.BigDecimal;

import org.laladev.moneyjinn.core.rest.model.transport.ImportedBalanceTransport;

public class ImportedBalanceTransportBuilder extends ImportedBalanceTransport {

	public ImportedBalanceTransportBuilder withBalance(final BigDecimal balance) {
		super.setBalance(balance);
		return this;
	}

	public ImportedBalanceTransportBuilder forImportedBalance1() {
		super.setBalance(new BigDecimal("9.00"));
		super.setBankCodeCapitalsource(CapitalsourceTransportBuilder.CAPITALSOURCE4_BANKCODE);
		super.setAccountNumberCapitalsource(CapitalsourceTransportBuilder.CAPITALSOURCE4_ACCOUNTNUMBER);
		return this;
	}

	public ImportedBalanceTransportBuilder forNewImportedBalance() {
		super.setBalance(BigDecimal.valueOf(1000l));
		super.setBankCodeCapitalsource(CapitalsourceTransportBuilder.CAPITALSOURCE1_BANKCODE);
		super.setAccountNumberCapitalsource(CapitalsourceTransportBuilder.CAPITALSOURCE1_ACCOUNTNUMBER);
		return this;
	}

	public ImportedBalanceTransportBuilder forOnlyBalanceImportedBalance() {
		super.setBalance(BigDecimal.valueOf(1000l));
		super.setBankCodeCapitalsource(CapitalsourceTransportBuilder.CAPITALSOURCE5_BANKCODE);
		super.setAccountNumberCapitalsource(CapitalsourceTransportBuilder.CAPITALSOURCE5_ACCOUNTNUMBER);
		return this;
	}

	public ImportedBalanceTransport build() {
		final ImportedBalanceTransport transport = new ImportedBalanceTransport();

		transport.setBalance(super.getBalance());
		transport.setBankCodeCapitalsource(super.getBankCodeCapitalsource());
		transport.setAccountNumberCapitalsource(super.getAccountNumberCapitalsource());

		return transport;
	}
}
