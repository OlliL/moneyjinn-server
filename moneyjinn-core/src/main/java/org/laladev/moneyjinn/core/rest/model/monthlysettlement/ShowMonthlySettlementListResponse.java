
package org.laladev.moneyjinn.core.rest.model.monthlysettlement;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Objects;
import org.laladev.moneyjinn.core.rest.model.AbstractResponse;
import org.laladev.moneyjinn.core.rest.model.transport.MonthlySettlementTransport;

@XmlRootElement(name = "showMonthlySettlementListResponse")
public class ShowMonthlySettlementListResponse extends AbstractResponse {
  @XmlElement(name = "monthlySettlementTransport")
  private List<MonthlySettlementTransport> monthlySettlementTransports;

  public List<MonthlySettlementTransport> getMonthlySettlementTransports() {
    return this.monthlySettlementTransports;
  }

  public void setMonthlySettlementTransports(
      final List<MonthlySettlementTransport> monthlySettlementTransports) {
    this.monthlySettlementTransports = monthlySettlementTransports;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + Objects.hash(this.monthlySettlementTransports);
    return result;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (!super.equals(obj)) {
      return false;
    }
    if (this.getClass() != obj.getClass()) {
      return false;
    }
    final ShowMonthlySettlementListResponse other = (ShowMonthlySettlementListResponse) obj;
    return Objects.equals(this.monthlySettlementTransports, other.monthlySettlementTransports);
  }

  @Override
  public String toString() {
    return "ShowMonthlySettlementListResponse [monthlySettlementTransports="
        + this.monthlySettlementTransports + "]";
  }
}
