
package org.laladev.moneyjinn.model.monthlysettlement;

public class ImportedMonthlySettlement
    extends AbstractMonthlySettlement<ImportedMonthlySettlementID> {
  private static final long serialVersionUID = 1L;
  private String externalId;

  public final String getExternalId() {
    return this.externalId;
  }

  public final void setExternalId(final String externalId) {
    this.externalId = externalId;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((this.externalId == null) ? 0 : this.externalId.hashCode());
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
    final ImportedMonthlySettlement other = (ImportedMonthlySettlement) obj;
    if (this.externalId == null) {
      if (other.externalId != null) {
        return false;
      }
    } else if (!this.externalId.equals(other.externalId)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    final StringBuilder builder = new StringBuilder();
    builder.append("ImportedMonthlySettlement [externalId=");
    builder.append(this.externalId);
    builder.append(", toString()=");
    builder.append(super.toString());
    builder.append("]");
    return builder.toString();
  }
}
