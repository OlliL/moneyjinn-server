
package org.laladev.moneyjinn.service.dao.data;

import lombok.Data;

@Data
public class EtfData {
  private String isin;
  private String name;
  private String wkn;
  private String ticker;
  private String chartUrl;
}
