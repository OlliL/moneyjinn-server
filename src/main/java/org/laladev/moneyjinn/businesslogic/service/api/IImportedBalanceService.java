package org.laladev.moneyjinn.businesslogic.service.api;

import java.util.List;

import org.laladev.moneyjinn.businesslogic.model.ImportedBalance;
import org.laladev.moneyjinn.businesslogic.model.access.UserID;
import org.laladev.moneyjinn.businesslogic.model.capitalsource.CapitalsourceID;
import org.laladev.moneyjinn.businesslogic.model.validation.ValidationResult;

/**
 * <p>
 * ImportedBalanceService is the Core Service handling everything around an {@link ImportedBalance}.
 * </p>
 *
 * <p>
 * ImportedBalanceService is the Domain Service handling operations around an
 * {@link ImportedBalance} like getting, creating, updating, deleting. Before a
 * {@link ImportedBalance} is created or updated, the {@link ImportedBalance} is validated for
 * correctness.
 * </p>
 * <p>
 * The main datasource is the Table <code>impbalance</code>.
 * </p>
 *
 * @author Oliver Lehmann
 * @since 0.0.1
 *
 */
public interface IImportedBalanceService {

	/**
	 * Validates the given {@link ImportedBalance} for correctness.
	 *
	 * @param importedBalance
	 * @return
	 */
	ValidationResult validateImportedBalance(ImportedBalance importedBalance);

	/**
	 * Persists the given {@link ImportedBalance}.
	 *
	 * @param importedBalance
	 */
	void upsertImportedBalance(ImportedBalance importedBalance);

	/**
	 * Retrives all {@link ImportedBalance}s for the given {@link CapitalsourceID}s
	 *
	 * @param userId
	 * @param capitalsourceIds
	 * @return
	 */
	List<ImportedBalance> getAllImportedBalancesByCapitalsourceIds(UserID userId,
			List<CapitalsourceID> capitalsourceIds);

}
