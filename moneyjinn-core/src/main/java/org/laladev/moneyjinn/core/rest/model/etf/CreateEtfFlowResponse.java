
package org.laladev.moneyjinn.core.rest.model.etf;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.laladev.moneyjinn.core.rest.model.ValidationResponse;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CreateEtfFlowResponse extends ValidationResponse {
  private Long etfFlowId;
}
