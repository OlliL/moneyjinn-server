package org.laladev.moneyjinn.hbci.batch.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observer;
import java.util.Properties;

import javax.script.ScriptException;

import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.ImprovedNamingStrategy;
import org.laladev.moneyjinn.hbci.batch.entity.AccountData;
import org.laladev.moneyjinn.hbci.core.entity.AccountMovement;
import org.laladev.moneyjinn.hbci.core.entity.BalanceDaily;
import org.laladev.moneyjinn.hbci.core.handler.AbstractHandler;
import org.laladev.moneyjinn.hbci.core.handler.AccountMovementHandler;
import org.laladev.moneyjinn.hbci.core.handler.BalanceDailyHandler;
import org.laladev.moneyjinn.hbci.core.handler.BalanceMonthlyHandler;

public class ScreenScrapingHandler {
	public void handle(final Properties properties, final List<Observer> observerList)
			throws NoSuchMethodException, ScriptException, IOException {
		final SessionFactory sf = getSessionFactory();
		final StatelessSession session = sf.openStatelessSession();

		final Transaction tx = session.beginTransaction();
		try {
			final List<AccountData> accountDataList = new ArrayList<AccountData>();
			accountDataList.add(this.collectMoneyYouData(properties));

			for (final AccountData accountData : accountDataList) {
				if (accountData != null) {
					final List<AccountMovement> accountMovements = accountData.getAccountMovements();
					final BalanceDaily balanceDaily = accountData.getBalanceDaily();

					this.executeHandler(new AccountMovementHandler(session, accountMovements), observerList);
					this.executeHandler(new BalanceDailyHandler(session, balanceDaily), observerList);
					this.executeHandler(new BalanceMonthlyHandler(session, balanceDaily, accountMovements),
							observerList);
					tx.commit();
				}
			}

		} finally {
			session.close();
			sf.close();
		}
	}

	private AccountData collectMoneyYouData(final Properties properties)
			throws ScriptException, IOException, NoSuchMethodException {
		final String account = properties.getProperty("hbci.scraping.moneyyou");
		final String username = properties.getProperty("hbci.scraping." + account + ".user");
		final String pin = properties.getProperty("hbci.scraping." + account + ".pin");

		final MoneyYouHandler handler = new MoneyYouHandler();
		return handler.handle(account, username, pin);
	}

	/**
	 * creates and returns the Hibernate {@link SessionFactory} lalaHBCI works with
	 *
	 * @return {@link SessionFactory}
	 */
	private static SessionFactory getSessionFactory() {
		final Configuration configuration = new Configuration().setNamingStrategy(ImprovedNamingStrategy.INSTANCE)
				.configure();
		final StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
				.applySettings(configuration.getProperties());
		return configuration.buildSessionFactory(builder.build());

	}

	/**
	 * executes a given handler for all specified accounts
	 *
	 * @param handler
	 * @param hbciHandler
	 * @param observerList
	 * @param accounts
	 */
	private void executeHandler(final AbstractHandler handler, final List<Observer> observerList) {

		for (final Observer observer : observerList) {
			handler.addObserver(observer);
		}
		handler.handle();

	}
}
