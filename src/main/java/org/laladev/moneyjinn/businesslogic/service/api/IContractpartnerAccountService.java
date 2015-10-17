package org.laladev.moneyjinn.businesslogic.service.api;

import java.util.List;

import org.laladev.moneyjinn.businesslogic.model.BankAccount;
import org.laladev.moneyjinn.businesslogic.model.ContractpartnerAccount;
import org.laladev.moneyjinn.businesslogic.model.ContractpartnerAccountID;
import org.laladev.moneyjinn.businesslogic.model.ContractpartnerID;
import org.laladev.moneyjinn.businesslogic.model.access.UserID;
import org.laladev.moneyjinn.businesslogic.model.exception.BusinessException;
import org.laladev.moneyjinn.businesslogic.model.validation.ValidationResult;

/**
 * <p>
 * ContractpartnerAccountService is the Core Service handling everything around an
 * {@link ContractpartnerAccount}.
 * </p>
 *
 * <p>
 * ContractpartnerAccountService is the Domain Service handling operations around an
 * {@link ContractpartnerAccount} like getting, creating, updating, deleting. Before a
 * {@link ContractpartnerAccount} is created or updated, the {@link ContractpartnerAccount} is
 * validated for correctness.
 * </p>
 * <p>
 * The main datasource is the Table <code>contractpartneraccounts</code>.
 * </p>
 *
 * @author Oliver Lehmann
 * @since 0.0.1
 *
 */
public interface IContractpartnerAccountService {
	/**
	 * This method validates a given {@link ContractpartnerAccount} for correctness.
	 *
	 * @param userId
	 *            {@link UserID}
	 * @param contractpartnerAccount
	 *            {@link ContractpartnerAccount}
	 * @return {@link ValidationResult}
	 */
	public ValidationResult validateContractpartnerAccount(UserID userId,
			ContractpartnerAccount contractpartnerAccount);

	/**
	 * This method returns the {@link ContractpartnerAccount} specified by its Id
	 *
	 * @param userId
	 *            {@link UserID}
	 * @param contractpartnerAccountId
	 *            {@link ContractpartnerAccountID}
	 * @return {@link ContractpartnerAccount}
	 */
	public ContractpartnerAccount getContractpartnerAccountById(UserID userId,
			ContractpartnerAccountID contractpartnerAccountId);

	/**
	 * This method returns the {@link ContractpartnerAccount}s assigned to a given
	 * {@link ContractpartnerID}
	 *
	 * @param userId
	 *            {@link UserID}
	 * @param contractpartnerId
	 *            {@link ContractpartnerID}
	 * @return list of {@link ContractpartnerAccount}s
	 */
	public List<ContractpartnerAccount> getContractpartnerAccounts(UserID userId, ContractpartnerID contractpartnerId);

	/**
	 * This method persists (creates) the given {@link ContractpartnerAccount}.
	 *
	 * @param userId
	 *            {@link UserID}
	 * @param contractpartnerAccount
	 *            {@link ContractpartnerAccount}
	 * @throws BusinessException
	 */
	public void createContractpartnerAccount(UserID userId, ContractpartnerAccount contractpartnerAccount);

	/**
	 * This method persists (updates) the given {@link ContractpartnerAccount}.
	 *
	 * @param userId
	 *            {@link UserID}
	 * @param contractpartnerAccount
	 *            {@link ContractpartnerAccount}
	 * @throws BusinessException
	 */
	public void updateContractpartnerAccount(UserID userId, ContractpartnerAccount contractpartnerAccount);

	/**
	 * This method deletes the given {@link ContractpartnerAccountID}.
	 *
	 * @param userId
	 *            {@link UserID}
	 * @param contractpartnerAccountId
	 *            {@link ContractpartnerAccountID}
	 * @throws BusinessException
	 */
	public void deleteContractpartnerAccountById(UserID userId, ContractpartnerAccountID contractpartnerAccountId);

	/**
	 * This method deletes the {@link ContractpartnerAccount}s assigned to a given
	 * {@link ContractpartnerID}.
	 *
	 * @param userId
	 *            {@link UserID}
	 * @param contractpartnerId
	 *            {@link ContractpartnerID}
	 */
	public void deleteContractpartnerAccounts(UserID userId, ContractpartnerID contractpartnerId);

	/**
	 * This method selects all {@link ContractpartnerAccount}s for the given {@link BankAccount}s
	 *
	 * @param userId
	 *            {@link UserID}
	 * @param bankAccounts
	 *            List of {@link BankAccount}
	 * @return
	 */
	public List<ContractpartnerAccount> getAllContractpartnerByAccounts(UserID userId, List<BankAccount> bankAccounts);
}
