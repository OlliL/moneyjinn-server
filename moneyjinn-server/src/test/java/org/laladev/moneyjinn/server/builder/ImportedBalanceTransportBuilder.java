package org.laladev.moneyjinn.server.builder;

import org.laladev.moneyjinn.server.model.ImportedBalanceTransport;

import java.math.BigDecimal;

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
        super.setBalance(BigDecimal.valueOf(1000L));
        super.setBankCodeCapitalsource(CapitalsourceTransportBuilder.CAPITALSOURCE1_BANKCODE);
        super.setAccountNumberCapitalsource(CapitalsourceTransportBuilder.CAPITALSOURCE1_ACCOUNTNUMBER);
        return this;
    }

    public ImportedBalanceTransportBuilder forOnlyBalanceImportedBalance() {
        super.setBalance(BigDecimal.valueOf(1000L));
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
