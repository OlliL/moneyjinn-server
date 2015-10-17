package org.laladev.moneyjinn.businesslogic.service.api;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.laladev.moneyjinn.businesslogic.model.PostingAccount;
import org.laladev.moneyjinn.businesslogic.model.access.UserID;
import org.laladev.moneyjinn.businesslogic.model.capitalsource.CapitalsourceID;
import org.laladev.moneyjinn.businesslogic.model.exception.BusinessException;
import org.laladev.moneyjinn.businesslogic.model.moneyflow.Moneyflow;
import org.laladev.moneyjinn.businesslogic.model.moneyflow.MoneyflowID;
import org.laladev.moneyjinn.businesslogic.model.validation.ValidationResult;

/**
 * <p>
 * MoneyflowService is the Service handling everything around an {@link Moneyflow}.
 * </p>
 *
 * <p>
 * MoneyflowService is the Service handling operations around an {@link Moneyflow} like getting,
 * creating, updating, deleting. Before a {@link Moneyflow} is created or updated, the
 * {@link Moneyflow} is validated for correctness.
 * </p>
 * <p>
 * The main datasource is the Table <code>moneyflows</code>.
 * </p>
 *
 * @author Oliver Lehmann
 * @since 0.0.1
 *
 */
public interface IMoneyflowService {
	/**
	 * Checks the validity of the given {@link Moneyflow}
	 *
	 * @param moneyflow
	 *            the {@link Moneyflow}
	 * @return {@link ValidationResult}
	 */
	public ValidationResult validateMoneyflow(Moneyflow moneyflow);

	/**
	 * This method persists (creates) the given {@link Moneyflow}s.
	 *
	 * @param moneyflows
	 *            {@link Moneyflow}s
	 * @throws BusinessException
	 */

	public void createMoneyflows(List<Moneyflow> moneyflows);

	/**
	 * This method returns the {@link Moneyflow} specified by its Id
	 *
	 * @param userId
	 *            {@link UserID}
	 * @param moneyflowId
	 *            {@link MoneyflowID}
	 * @return {@link Moneyflow}
	 */
	public Moneyflow getMoneyflowById(UserID userId, MoneyflowID moneyflowId);

	/**
	 * This service changes a {@link Moneyflow}. Before the {@link Moneyflow} is changed, the new
	 * values are validated for correctness.
	 *
	 * @param moneyflow
	 *            the new {@link Moneyflow} attributes
	 */
	public void updateMoneyflow(Moneyflow moneyflow);

	/**
	 * This service deletes a {@link Moneyflow} from the system
	 *
	 * @param userId
	 *            {@link UserID}
	 * @param moneyflowId
	 *            The {@link MoneyflowID} of the to-be-deleted {@link PostingAccount}
	 */
	public void deleteMoneyflow(UserID userId, MoneyflowID moneyflowId);

	/**
	 * retrieves the sum of all booked {@link Moneyflow}s for the given {@link CapitalsourceID}
	 * during a defined period of time.
	 *
	 * @param userId
	 * @param beginOfMonth
	 * @param endOfMonth
	 * @param capitalsourceId
	 * @return
	 */
	public BigDecimal getSumAmountByDateRangeForCapitalsourceId(UserID userId, LocalDate beginOfMonth,
			LocalDate endOfMonth, CapitalsourceID capitalsourceId);
}
