package org.laladev.moneyjinn.businesslogic.service.api;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import org.laladev.moneyjinn.businesslogic.model.access.UserID;
import org.laladev.moneyjinn.businesslogic.model.monthlysettlement.MonthlySettlement;
import org.laladev.moneyjinn.businesslogic.model.validation.ValidationResult;

/**
 * <p>
 * MonthlySettlementService is the Core Service handling everything around an
 * {@link MonthlySettlement}.
 * </p>
 *
 * <p>
 * MonthlySettlementService is the Domain Service handling operations around an
 * {@link MonthlySettlement} like getting, creating, updating, deleting. Before a
 * {@link MonthlySettlement} is created or updated, the {@link MonthlySettlement} is validated for
 * correctness.
 * </p>
 * <p>
 * The main datasource is the Table <code>monthlysettlements</code>.
 * </p>
 *
 * @author Oliver Lehmann
 * @since 0.0.1
 *
 */
public interface IMonthlySettlementService {

	/**
	 * Returns a list of years in which {@link MonthlySettlement}s where created.
	 *
	 * @param userId
	 *            The {@link UserID}
	 * @return list of years
	 */
	public List<Short> getAllYears(UserID userId);

	/**
	 * Returns a list of {@link Month}s where {@link MonthlySettlement}s where created for in the
	 * given year.
	 *
	 * @param userId
	 *            The {@link UserID}
	 * @param year
	 * @return list of {@link Month}s
	 */
	public List<Month> getAllMonth(UserID userId, Short year);

	/**
	 * Returns a list of {@link MonthlySettlement}s for the given year and {@link Month}.
	 *
	 * @param userId
	 *            The {@link UserID}
	 * @param year
	 * @param month
	 *            The {@link Month}
	 * @return list of {@link MonthlySettlement}s
	 */
	public List<MonthlySettlement> getAllMonthlySettlementsByYearMonth(UserID userId, Short year, Month month);

	/**
	 * Returns the last date a {@link MonthlySettlement} was created by the given {@link UserId}.
	 *
	 * @param userId
	 * @return
	 */
	public LocalDate getMaxSettlementDate(UserID userId);

	/**
	 * Checks if at the given year and month, the also given {@link UserId} has already created a
	 * {@link MonthlySettlement}.
	 *
	 * @param userId
	 * @param year
	 * @param month
	 * @return
	 */
	public boolean checkMonthlySettlementsExists(UserID userId, Short year, Month month);

	/**
	 * This method inserts or updates the given {@link MonthlySettlement}s.
	 *
	 * @param monthlySettlements
	 * @return
	 */
	public ValidationResult upsertMonthlySettlements(List<MonthlySettlement> monthlySettlements);

	/**
	 * This method deletes the {@link MonthlySettlement}s for the given year and {@link Month}
	 *
	 * @param userId
	 * @param year
	 * @param month
	 */
	public void deleteMonthlySettlement(UserID userId, Short year, Month month);
}
