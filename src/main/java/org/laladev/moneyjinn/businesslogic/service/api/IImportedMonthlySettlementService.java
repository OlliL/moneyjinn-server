package org.laladev.moneyjinn.businesslogic.service.api;

import java.time.Month;
import java.util.List;

import org.laladev.moneyjinn.businesslogic.model.access.UserID;
import org.laladev.moneyjinn.businesslogic.model.monthlysettlement.ImportedMonthlySettlement;

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

	List<ImportedMonthlySettlement> getImportedMonthlySettlementsByMonth(UserID userId, Short year, Month month);

}
