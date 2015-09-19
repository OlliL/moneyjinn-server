package org.laladev.moneyjinn.businesslogic.service.impl;

import java.util.Arrays;
import java.util.List;

import javax.inject.Named;

import org.laladev.moneyjinn.businesslogic.model.ContractpartnerAccount;
import org.laladev.moneyjinn.businesslogic.model.ContractpartnerAccountID;
import org.laladev.moneyjinn.businesslogic.model.ContractpartnerID;
import org.laladev.moneyjinn.businesslogic.model.access.UserID;
import org.laladev.moneyjinn.businesslogic.model.validation.ValidationResult;
import org.laladev.moneyjinn.businesslogic.model.validation.ValidationResultItem;
import org.laladev.moneyjinn.businesslogic.service.api.IContractpartnerAccountService;
import org.laladev.moneyjinn.core.error.ErrorCode;

@Named
public class ContractpartnerAccountService implements IContractpartnerAccountService {

	@Override
	public ValidationResult validateContractpartnerAccount(final UserID userId,
			final ContractpartnerAccount contractpartnerAccount) {
		final ValidationResult validationResult = new ValidationResult();

		if (contractpartnerAccount.getBankAccount() == null) {
			validationResult.addValidationResultItem(new ValidationResultItem(contractpartnerAccount.getId(),
					ErrorCode.BANK_CODE_CONTAINS_ILLEGAL_CHARS_OR_IS_EMPTY));
			validationResult.addValidationResultItem(new ValidationResultItem(contractpartnerAccount.getId(),
					ErrorCode.ACCOUNT_NUMBER_CONTAINS_ILLEGAL_CHARS_OR_IS_EMPTY));
		} else {
			for (final ErrorCode errorCode : contractpartnerAccount.getBankAccount().checkValidity()) {
				validationResult
						.addValidationResultItem(new ValidationResultItem(contractpartnerAccount.getId(), errorCode));
			}
		}

		final ContractpartnerAccount contractpartnerAccountChecked = this.getContractpartnerAccountByAccount(userId,
				contractpartnerAccount);
		if (contractpartnerAccountChecked != null && !contractpartnerAccountChecked.getContractpartner().getId()
				.equals(contractpartnerAccount.getContractpartner().getId())) {
			validationResult.addValidationResultItem(new ValidationResultItem(contractpartnerAccount.getId(),
					ErrorCode.ACCOUNT_ALREADY_ASSIGNED_TO_OTHER_PARTNER,
					Arrays.asList(contractpartnerAccountChecked.getContractpartner().getName())));
		}

		return validationResult;
	}

	private ContractpartnerAccount getContractpartnerAccountByAccount(final UserID userId,
			final ContractpartnerAccount contractpartnerAccount) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ContractpartnerAccount getContractpartnerAccountById(final UserID userId,
			final ContractpartnerAccountID contractpartnerAccountId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ContractpartnerAccount> getContractpartnerAccounts(final UserID userId,
			final ContractpartnerID contractpartnerId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void createContractpartnerAccount(final UserID userId, final ContractpartnerAccount contractpartnerAccount) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateContractpartnerAccount(final UserID userId, final ContractpartnerAccount contractpartnerAccount) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteContractpartnerAccountById(final UserID userId,
			final ContractpartnerAccountID contractpartnerAccountId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteContractpartnerAccounts(final UserID userId, final ContractpartnerID contractpartnerId) {
		// TODO Auto-generated method stub

	}

}
