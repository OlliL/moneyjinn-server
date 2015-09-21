package org.laladev.moneyjinn.businesslogic.dao;

import java.util.List;

import javax.inject.Named;

import org.laladev.moneyjinn.businesslogic.dao.data.ContractpartnerAccountData;

@Named
public class ContractpartnerAccountDao {

	// @Inject
	// IContractpartnerDaoMapper mapper;

	public ContractpartnerAccountData getContractpartnerAccountByBankAccount(final Long userId, final String bankCode,
			final String accountNumber) {
		// TODO Auto-generated method stub
		return null;
	}

	public ContractpartnerAccountData getContractpartnerAccountById(final Long userId,
			final Long contractpartnerAccountId) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<ContractpartnerAccountData> getContractpartnerAccounts(final Long userId,
			final Long contractpartnerId) {
		// TODO Auto-generated method stub
		return null;
	}

	public Long createContractpartnerAccount(final ContractpartnerAccountData contractpartnerAccountData) {
		// TODO Auto-generated method stub
		return null;
	}

	public void updateContractpartnerAccount(final ContractpartnerAccountData contractpartnerAccountData) {
		// TODO Auto-generated method stub

	}

	public void deleteContractpartnerAccount(final Long userId, final Long contractpartnerAccountId) {
		// TODO Auto-generated method stub

	}

	public void deleteContractpartnerAccounts(final Long userId, final Long contractpartnerId) {
		// TODO Auto-generated method stub

	}

}
