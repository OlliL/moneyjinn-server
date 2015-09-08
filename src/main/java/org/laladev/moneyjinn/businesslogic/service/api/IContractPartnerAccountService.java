package org.laladev.moneyjinn.businesslogic.service.api;

import org.laladev.moneyjinn.businesslogic.model.ContractpartnerID;
import org.laladev.moneyjinn.businesslogic.model.access.UserID;

public interface IContractPartnerAccountService {
	public void deleteContractpartnerAccounts(UserID userId, ContractpartnerID contractpartnerId);
}
