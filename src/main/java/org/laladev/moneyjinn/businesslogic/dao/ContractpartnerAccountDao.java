package org.laladev.moneyjinn.businesslogic.dao;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.laladev.moneyjinn.businesslogic.dao.data.ContractpartnerAccountData;
import org.laladev.moneyjinn.businesslogic.dao.mapper.IContractpartnerAccountDaoMapper;

@Named
public class ContractpartnerAccountDao {

	@Inject
	IContractpartnerAccountDaoMapper mapper;

	public ContractpartnerAccountData getContractpartnerAccountByBankAccount(final Long userId, final String bankCode,
			final String accountNumber) {
		return this.mapper.getContractpartnerAccountByBankAccount(bankCode, accountNumber);
	}

	public ContractpartnerAccountData getContractpartnerAccountById(final Long userId,
			final Long contractpartnerAccountId) {
		return this.mapper.getContractpartnerAccountById(contractpartnerAccountId);
	}

	public List<ContractpartnerAccountData> getContractpartnerAccounts(final Long userId,
			final Long contractpartnerId) {
		return this.mapper.getContractpartnerAccounts(contractpartnerId);
	}

	public Long createContractpartnerAccount(final ContractpartnerAccountData contractpartnerAccountData) {
		this.mapper.createContractpartnerAccount(contractpartnerAccountData);
		return contractpartnerAccountData.getId();
	}

	public void updateContractpartnerAccount(final ContractpartnerAccountData contractpartnerAccountData) {
		this.mapper.updateContractpartnerAccount(contractpartnerAccountData);
	}

	public void deleteContractpartnerAccount(final Long userId, final Long contractpartnerAccountId) {
		this.mapper.deleteContractpartnerAccount(contractpartnerAccountId);
	}

	public void deleteContractpartnerAccounts(final Long userId, final Long contractpartnerId) {
		this.mapper.deleteContractpartnerAccounts(contractpartnerId);
	}

}
