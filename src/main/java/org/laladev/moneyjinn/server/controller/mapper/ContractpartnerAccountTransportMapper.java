package org.laladev.moneyjinn.server.controller.mapper;

import org.laladev.moneyjinn.businesslogic.model.BankAccount;
import org.laladev.moneyjinn.businesslogic.model.Contractpartner;
import org.laladev.moneyjinn.businesslogic.model.ContractpartnerAccount;
import org.laladev.moneyjinn.businesslogic.model.ContractpartnerAccountID;
import org.laladev.moneyjinn.businesslogic.model.ContractpartnerID;
import org.laladev.moneyjinn.core.mapper.IMapper;
import org.laladev.moneyjinn.core.rest.model.transport.ContractpartnerAccountTransport;

public class ContractpartnerAccountTransportMapper
		implements IMapper<ContractpartnerAccount, ContractpartnerAccountTransport> {

	@Override
	public ContractpartnerAccount mapBToA(final ContractpartnerAccountTransport contractpartnerAccountTransport) {

		BankAccount bankAccount = null;
		if (contractpartnerAccountTransport.getAccountNumber() != null
				&& contractpartnerAccountTransport.getBankCode() != null) {
			bankAccount = new BankAccount(contractpartnerAccountTransport.getAccountNumber(),
					contractpartnerAccountTransport.getBankCode());
		}
		final ContractpartnerAccountID contractpartnerAccountId = new ContractpartnerAccountID(
				contractpartnerAccountTransport.getId());
		final Contractpartner contractpartner = new Contractpartner(
				new ContractpartnerID(contractpartnerAccountTransport.getContractpartnerid()));
		return new ContractpartnerAccount(contractpartnerAccountId, contractpartner, bankAccount);
	}

	@Override
	public ContractpartnerAccountTransport mapAToB(final ContractpartnerAccount contractpartnerAccount) {
		final ContractpartnerAccountTransport contractpartnerAccountTransport = new ContractpartnerAccountTransport();
		contractpartnerAccountTransport.setId(contractpartnerAccount.getId().getId());
		contractpartnerAccountTransport.setAccountNumber(contractpartnerAccount.getBankAccount().getAccountNumber());
		contractpartnerAccountTransport.setBankCode(contractpartnerAccount.getBankAccount().getBankCode());
		contractpartnerAccountTransport
				.setContractpartnerid(contractpartnerAccount.getContractpartner().getId().getId());

		return contractpartnerAccountTransport;
	}
}
