package org.laladev.moneyjinn.businesslogic.service.api;

import java.time.Month;
import java.util.List;

import org.laladev.moneyjinn.businesslogic.model.access.UserID;
import org.laladev.moneyjinn.businesslogic.model.monthlysettlement.ImportedMonthlySettlement;
import org.laladev.moneyjinn.businesslogic.model.validation.ValidationResult;

/**
 * <p>
 * ImportedMonthlySettlementService is the Core Service handling everything around an
 * {@link ImportedMonthlySettlement}.
 * </p>
 *
 * <p>
 * ImportedMonthlySettlementService is the Domain Service handling operations around an
 * {@link ImportedMonthlySettlement} like getting, creating, updating, deleting. Before a
 * {@link ImportedMonthlySettlement} is created or updated, the {@link ImportedMonthlySettlement} is
 * validated for correctness.
 * </p>
 * <p>
 * The main datasource is the Table <code>impmonthlysettlements</code>.
 * </p>
 *
 * @author Oliver Lehmann
 * @since 0.0.1
 *
 */
public interface IImportedMonthlySettlementService {

	/**
	 * Retrieves all the imported monthly settlements for the given year and month.
	 *
	 * @param userId
	 * @param year
	 * @param month
	 * @return
	 */
	List<ImportedMonthlySettlement> getImportedMonthlySettlementsByMonth(UserID userId, Short year, Month month);

	/**
	 * Persists the given {@link ImportedMonthlySettlement}.
	 *
	 * @param importedMonthlySettlement
	 */
	void createImportedMonthlySettlement(ImportedMonthlySettlement importedMonthlySettlement);

	/**
	 * Validates the given {@link ImportedMonthlySettlement} for correctness.
	 * 
	 * @param importedMonthlySettlement
	 * @return
	 */
	ValidationResult validateImportedMonthlySettlement(ImportedMonthlySettlement importedMonthlySettlement);

}
