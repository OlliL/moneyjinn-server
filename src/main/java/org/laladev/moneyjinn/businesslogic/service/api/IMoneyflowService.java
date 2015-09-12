package org.laladev.moneyjinn.businesslogic.service.api;

import java.util.List;

import org.laladev.moneyjinn.businesslogic.model.Moneyflow;
import org.laladev.moneyjinn.businesslogic.model.exception.BusinessException;
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
}
