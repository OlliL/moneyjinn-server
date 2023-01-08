
package org.laladev.moneyjinn.core.rest.model.transport;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class MoneyflowSplitEntryTransport {
  private Long id;
  private Long moneyflowid;
  private BigDecimal amount;
  private String comment;
  private Long postingaccountid;
  private String postingaccountname;
}
