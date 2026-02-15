package org.laladev.moneyjinn.server.builder;

import org.laladev.moneyjinn.server.model.CompareDataDatasetTransport;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CompareDataDatasetTransportBuilder extends CompareDataDatasetTransport {
    public CompareDataDatasetTransportBuilder withPartner(final String partner) {
        super.setPartner(partner);
        return this;
    }

    public CompareDataDatasetTransportBuilder forCompareDataImportDataset1() {
        super.setAmount(new BigDecimal("-5.00"));
        super.setBookingDate(LocalDate.parse("2010-05-03"));
        super.setInvoiceDate(LocalDate.parse("2010-05-03"));
        super.setPartner("Partner");
        super.setComment("generated");
        super.setContractpartnerid(ContractpartnerTransportBuilder.CONTRACTPARTNER1_ID);
        super.setContractpartnername(ContractpartnerTransportBuilder.CONTRACTPARTNER1_NAME);
        return this;
    }

    public CompareDataDatasetTransportBuilder forCompareDataImportDataset2() {
        super.setAmount(new BigDecimal("-5.00"));
        super.setBookingDate(LocalDate.parse("2010-05-20"));
        super.setInvoiceDate(LocalDate.parse("2010-05-20"));
        super.setPartner("Partner");
        super.setComment("generated");
        super.setContractpartnerid(ContractpartnerTransportBuilder.CONTRACTPARTNER1_ID);
        super.setContractpartnername(ContractpartnerTransportBuilder.CONTRACTPARTNER1_NAME);
        return this;
    }

    public CompareDataDatasetTransportBuilder forCompareDataImportDataset3() {
        super.setAmount(new BigDecimal("-5.00"));
        super.setBookingDate(LocalDate.parse("2010-05-05"));
        super.setInvoiceDate(LocalDate.parse("2010-05-05"));
        super.setPartner("Sartner");
        super.setComment("generated");
        super.setContractpartnerid(ContractpartnerTransportBuilder.CONTRACTPARTNER4_ID);
        super.setContractpartnername(ContractpartnerTransportBuilder.CONTRACTPARTNER4_NAME);
        return this;
    }

    public CompareDataDatasetTransportBuilder forCompareDataImportDataset4() {
        super.setAmount(new BigDecimal("-5.00"));
        super.setBookingDate(LocalDate.parse("2010-05-03"));
        super.setInvoiceDate(LocalDate.parse("2010-05-03"));
        super.setPartner("Partner");
        super.setComment(ContractpartnerMatchingTransportBuilder.CONTRACTPARTNER_MATCHING3_MATCHING_TEXT + " ABC");
        super.setContractpartnerid(ContractpartnerTransportBuilder.CONTRACTPARTNER4_ID);
        super.setContractpartnername(ContractpartnerTransportBuilder.CONTRACTPARTNER4_NAME);
        super.setMoneyflowComment("mmf-comment");
        super.setPostingAccountId(PostingAccountTransportBuilder.POSTING_ACCOUNT3_ID);
        super.setPostingAccountName(PostingAccountTransportBuilder.POSTING_ACCOUNT3_NAME);
        return this;
    }

    public CompareDataDatasetTransportBuilder forCompareDataDataset1() {
        super.setAmount(new BigDecimal("-5.00"));
        super.setBookingDate(LocalDate.parse("2010-05-03"));
        super.setPartner("Qartner2");
        super.setComment("test1");
        return this;
    }

    public CompareDataDatasetTransportBuilder forCompareDataDataset2() {
        super.setAmount(new BigDecimal("1000.00"));
        super.setBookingDate(LocalDate.parse("2010-05-13"));
        super.setPartner("Finanzkasse Köln-Süd");
        super.setComment("test2");
        return this;
    }

    public CompareDataDatasetTransportBuilder forCompareDataDataset3() {
        super.setAmount(new BigDecimal("10.00"));
        super.setBookingDate(LocalDate.parse("2010-02-01"));
        super.setPartner("Hugo");
        super.setComment("test1");
        return this;
    }

    public CompareDataDatasetTransportBuilder forCompareDataDataset4() {
        super.setAmount(new BigDecimal("-10.00"));
        super.setBookingDate(LocalDate.parse("2010-01-10"));
        super.setPartner("Paul");
        super.setComment("TEST1");
        super.setContractpartnerid(ContractpartnerTransportBuilder.CONTRACTPARTNER1_ID);
        super.setContractpartnername(ContractpartnerTransportBuilder.CONTRACTPARTNER1_NAME);
        return this;
    }

    public CompareDataDatasetTransportBuilder forCompareDataDataset1SpardaBank() {
        super.setAmount(new BigDecimal("-5.00"));
        super.setBookingDate(LocalDate.parse("2010-05-03"));
        super.setComment("Qartner2test1");
        return this;
    }

    public CompareDataDatasetTransportBuilder forCompareDataDataset2SpardaBank() {
        super.setAmount(new BigDecimal("1000.00"));
        super.setBookingDate(LocalDate.parse("2010-05-13"));
        super.setComment("Finanzkasse Köln-Südtest2");
        return this;
    }

    public CompareDataDatasetTransport build() {
        final CompareDataDatasetTransport transport = new CompareDataDatasetTransport();
        transport.setAmount(super.getAmount());
        transport.setBookingDate(super.getBookingDate());
        transport.setInvoiceDate(super.getInvoiceDate());
        transport.setPartner(super.getPartner());
        transport.setComment(super.getComment());
        transport.setContractpartnerid(super.getContractpartnerid());
        transport.setContractpartnername(super.getContractpartnername());
        transport.setMoneyflowComment(super.getMoneyflowComment());
        transport.setPostingAccountId(super.getPostingAccountId());
        transport.setPostingAccountName(super.getPostingAccountName());
        return transport;
    }
}
