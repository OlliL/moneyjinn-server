
package org.laladev.moneyjinn.core.rest.model.contractpartner;

import lombok.Data;
import org.laladev.moneyjinn.core.rest.model.transport.ContractpartnerTransport;

@Data
public abstract class AbstractContractpartnerRequest {
  private ContractpartnerTransport contractpartnerTransport;
}
