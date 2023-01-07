
package org.laladev.moneyjinn.service.dao.data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class EtfValueData {
  private String isin;
  private LocalDate date;
  private BigDecimal buyPrice;
  private BigDecimal sellPrice;
  private LocalDateTime changedate;
}
