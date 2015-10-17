package org.laladev.moneyjinn.businesslogic.service.api;

import java.util.List;

import org.laladev.moneyjinn.businesslogic.model.access.UserID;
import org.laladev.moneyjinn.businesslogic.model.capitalsource.CapitalsourceID;
import org.laladev.moneyjinn.businesslogic.model.moneyflow.ImportedMoneyflow;
import org.laladev.moneyjinn.businesslogic.model.moneyflow.ImportedMoneyflowID;
import org.laladev.moneyjinn.businesslogic.model.validation.ValidationResult;

/**
 * <p>
 * ImportedMoneyflowService is the Core Service handling everything around an
 * {@link ImportedMoneyflow}.
 * </p>
 *
 * <p>
 * ImportedMoneyflowService is the Domain Service handling operations around an
 * {@link ImportedMoneyflow} like getting, creating, updating, deleting. Before a
 * {@link ImportedMoneyflow} is created or updated, the {@link ImportedMoneyflow} is validated for
 * correctness.
 * </p>
 * <p>
 * The main datasource is the Table <code>impmoneyflows</code>.
 * </p>
 *
 * @author Oliver Lehmann
 * @since 0.0.1
 *
 */
public interface IImportedMoneyflowService {
	/**
	 * Validates the given {@link ImportedMoneyflow} for correctness.
	 *
	 * @param importedMoneyflow
	 * @return
	 */
	ValidationResult validateImportedMoneyflow(ImportedMoneyflow importedMoneyflow);

	/**
	 * Counts how much {@link ImportedMoneyflow} are there to be processed.
	 *
	 * @param userId
	 * @param capitalsourceIds
	 * @return
	 */
	Integer countImportedMoneyflows(UserID userId, List<CapitalsourceID> capitalsourceIds);

	/**
	 * Retrives all {@link ImportedMoneyflow}s to be processed by the user for the given
	 * {@link CapitalsourceID}s.
	 *
	 * @param userId
	 * @param capitalsourceIds
	 * @return
	 */
	List<ImportedMoneyflow> getAllImportedMoneyflowsByCapitalsourceIds(UserID userId,
			List<CapitalsourceID> capitalsourceIds);

	/**
	 * Persists the given {@link ImportedMoneyflow}.
	 *
	 * @param importedMoneyflow
	 */
	void createImportedMoneyflow(ImportedMoneyflow importedMoneyflow);

	/**
	 * Deletes the {@link ImportedMoneyflow} specified by its {@link ImportedMoneyflowID}.
	 *
	 * @param userId
	 * @param importedMoneyflowId
	 */
	void deleteImportedMoneyflowById(UserID userId, ImportedMoneyflowID importedMoneyflowId);

}
