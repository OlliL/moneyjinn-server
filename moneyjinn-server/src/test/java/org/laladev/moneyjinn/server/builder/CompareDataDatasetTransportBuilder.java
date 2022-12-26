
package org.laladev.moneyjinn.server.builder;

import java.math.BigDecimal;
import org.laladev.moneyjinn.core.rest.model.comparedata.transport.CompareDataDatasetTransport;

public class CompareDataDatasetTransportBuilder extends CompareDataDatasetTransport {
  public CompareDataDatasetTransportBuilder withPartner(final String partner) {
    super.setPartner(partner);
    return this;
  }

  public CompareDataDatasetTransportBuilder forCompareDataImportDataset1() {
    super.setAmount(new BigDecimal("-5.00"));
    super.setBookingDate(DateUtil.getGmtDate("2010-05-03"));
    super.setInvoiceDate(DateUtil.getGmtDate("2010-05-03"));
    super.setPartner("Partner");
    super.setComment("generated");
    return this;
  }

  public CompareDataDatasetTransportBuilder forCompareDataImportDataset2() {
    super.setAmount(new BigDecimal("-5.00"));
    super.setBookingDate(DateUtil.getGmtDate("2010-05-20"));
    super.setInvoiceDate(DateUtil.getGmtDate("2010-05-20"));
    super.setPartner("Partner");
    super.setComment("generated");
    return this;
  }

  public CompareDataDatasetTransportBuilder forCompareDataImportDataset3() {
    super.setAmount(new BigDecimal("-5.00"));
    super.setBookingDate(DateUtil.getGmtDate("2010-05-05"));
    super.setInvoiceDate(DateUtil.getGmtDate("2010-05-05"));
    super.setPartner("Sartner");
    super.setComment("generated");
    return this;
  }

  public CompareDataDatasetTransportBuilder forCompareDataDataset1() {
    super.setAmount(new BigDecimal("-5.00"));
    super.setBookingDate(DateUtil.getGmtDate("2010-05-03"));
    super.setPartner("Qartner2");
    super.setComment("test1");
    return this;
  }

  public CompareDataDatasetTransportBuilder forCompareDataDataset2() {
    super.setAmount(new BigDecimal("1000.00"));
    super.setBookingDate(DateUtil.getGmtDate("2010-05-13"));
    super.setPartner("Finanzkasse Köln-Süd");
    super.setComment("test2");
    return this;
  }

  public CompareDataDatasetTransportBuilder forCompareDataDataset3() {
    super.setAmount(new BigDecimal("10.00"));
    super.setBookingDate(DateUtil.getGmtDate("2010-02-01"));
    super.setPartner("Hugo");
    super.setComment("test1");
    return this;
  }

  public CompareDataDatasetTransportBuilder forCompareDataDataset4() {
    super.setAmount(new BigDecimal("-10.00"));
    super.setBookingDate(DateUtil.getGmtDate("2010-01-10"));
    super.setPartner("Paul");
    super.setComment("test1");
    return this;
  }

  public CompareDataDatasetTransportBuilder forCompareDataDataset1SpardaBank() {
    super.setAmount(new BigDecimal("-5.00"));
    super.setBookingDate(DateUtil.getGmtDate("2010-05-03"));
    super.setComment("Qartner2test1");
    return this;
  }

  public CompareDataDatasetTransportBuilder forCompareDataDataset2SpardaBank() {
    super.setAmount(new BigDecimal("1000.00"));
    super.setBookingDate(DateUtil.getGmtDate("2010-05-13"));
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
    return transport;
  }
}
