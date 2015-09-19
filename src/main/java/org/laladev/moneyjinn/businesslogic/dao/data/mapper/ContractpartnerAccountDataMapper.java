package org.laladev.moneyjinn.businesslogic.dao.data.mapper;

import org.laladev.moneyjinn.businesslogic.dao.data.ContractpartnerAccountData;
import org.laladev.moneyjinn.businesslogic.model.BankAccount;
import org.laladev.moneyjinn.businesslogic.model.Contractpartner;
import org.laladev.moneyjinn.businesslogic.model.ContractpartnerAccount;
import org.laladev.moneyjinn.businesslogic.model.ContractpartnerAccountID;
import org.laladev.moneyjinn.businesslogic.model.ContractpartnerID;
import org.laladev.moneyjinn.core.mapper.IMapper;

public class ContractpartnerAccountDataMapper implements IMapper<ContractpartnerAccount, ContractpartnerAccountData> {

	@Override
	public ContractpartnerAccount mapBToA(final ContractpartnerAccountData contractpartnerAccountData) {
		final BankAccount bankAccount = new BankAccount(contractpartnerAccountData.getAccountNumber(),
				contractpartnerAccountData.getBankCode());
		final ContractpartnerAccountID contractpartnerAccountId = new ContractpartnerAccountID(
				contractpartnerAccountData.getContractpartnerAccountId());
		final Contractpartner contractpartner = new Contractpartner(
				new ContractpartnerID(contractpartnerAccountData.getMcpContractpartnerId()));
		return new ContractpartnerAccount(contractpartnerAccountId, contractpartner, bankAccount);
	}

	@Override
	public ContractpartnerAccountData mapAToB(final ContractpartnerAccount contractpartnerAccount) {
		final ContractpartnerAccountData contractpartnerAccountData = new ContractpartnerAccountData();
		contractpartnerAccountData.setContractpartnerAccountId(contractpartnerAccount.getId().getId());
		contractpartnerAccountData.setAccountNumber(contractpartnerAccount.getBankAccount().getAccountNumber());
		contractpartnerAccountData.setBankCode(contractpartnerAccount.getBankAccount().getBankCode());
		contractpartnerAccountData.setMcpContractpartnerId(contractpartnerAccount.getContractpartner().getId().getId());

		return contractpartnerAccountData;
	}
}
