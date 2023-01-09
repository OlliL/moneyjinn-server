
package org.laladev.moneyjinn.core.rest.model.contractpartneraccount;

import lombok.Data;
import org.laladev.moneyjinn.core.rest.model.transport.ContractpartnerAccountTransport;

@Data
public abstract class AbstractContractpartnerAccountRequest {
  private ContractpartnerAccountTransport contractpartnerAccountTransport;
}
