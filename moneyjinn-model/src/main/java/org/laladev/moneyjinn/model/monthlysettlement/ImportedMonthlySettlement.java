
package org.laladev.moneyjinn.model.monthlysettlement;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ImportedMonthlySettlement
    extends AbstractMonthlySettlement<ImportedMonthlySettlementID> {
  private static final long serialVersionUID = 1L;
  private String externalId;
}
