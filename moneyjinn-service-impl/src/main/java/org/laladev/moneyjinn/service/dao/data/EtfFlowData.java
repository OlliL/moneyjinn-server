
package org.laladev.moneyjinn.service.dao.data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class EtfFlowData {
  private Long etfflowid;
  private LocalDateTime flowdate;
  private String isin;
  private BigDecimal amount;
  private BigDecimal price;
}
