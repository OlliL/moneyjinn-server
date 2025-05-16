//
// Copyright (c) 2014-2024 Oliver Lehmann <lehmann@ans-netz.de>
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions
// are met:
// 1. Redistributions of source code must retain the above copyright
// notice, this list of conditions and the following disclaimer
// 2. Redistributions in binary form must reproduce the above copyright
// notice, this list of conditions and the following disclaimer in the
// documentation and/or other materials provided with the distribution.
//
// THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS'' AND
// ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE LIABLE
// FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
// DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
// OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
// HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
// LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
// OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE.
//

package org.laladev.moneyjinn.hbci.core;

import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.kapott.hbci.manager.BankInfo;
import org.kapott.hbci.manager.HBCIHandler;
import org.kapott.hbci.manager.HBCIUtils;
import org.kapott.hbci.manager.HBCIUtilsInternal;
import org.kapott.hbci.passport.AbstractHBCIPassport;
import org.kapott.hbci.passport.HBCIPassport;
import org.kapott.hbci.structures.Konto;
import org.laladev.moneyjinn.hbci.core.collector.AccountMovementCollector;
import org.laladev.moneyjinn.hbci.core.collector.BalanceDailyCollector;
import org.laladev.moneyjinn.hbci.core.entity.AbstractAccountEntitiy;
import org.laladev.moneyjinn.hbci.core.entity.AccountMovement;
import org.laladev.moneyjinn.hbci.core.entity.BalanceDaily;
import org.laladev.moneyjinn.hbci.core.handler.AbstractHandler;
import org.laladev.moneyjinn.hbci.core.handler.AccountMovementHandler;
import org.laladev.moneyjinn.hbci.core.handler.BalanceDailyHandler;
import org.laladev.moneyjinn.hbci.core.handler.BalanceMonthlyHandler;

public final class LalaHBCI {
	private final Properties properties;

	public LalaHBCI(final Properties properties) {
		this.properties = properties;
	}

	public void main(final List<String> passports, final List<PropertyChangeListener> observerList) {

		HBCIUtils.init(this.getHbciProperties(), new LalaHBCICallback());

		try {
			for (final String passport : passports) {
				this.process(passport, observerList);
			}
		} finally {
			HBCIUtils.done();
		}
	}

	/**
	 * executes all implemented handlers for all given passports
	 *
	 * @param passport
	 * @param session
	 * @param observerList
	 * @throws IOException
	 */
	private void process(final String passport, final List<PropertyChangeListener> observerList) {

		((LalaHBCICallback) HBCIUtilsInternal.getCallback()).setPassportPassword(this.getPassword(passport));

		final String type = this.getType(passport);

		if (type.equals("RDHNew")) {
			HBCIUtils.setParam("client.passport.RDHNew.filename", passport);
		} else if (type.equals("PinTan")) {
			HBCIUtils.setParam("client.passport.PinTan.filename", passport);
			((LalaHBCICallback) HBCIUtilsInternal.getCallback()).setPin(this.getPin(passport));
		} else {
			throw new RuntimeException("type " + type + " for passport " + passport
					+ " not supported (only PinTan and RDHNew supported)!");
		}

		HBCIUtils.setParam("client.errors.ignoreWrongJobDataErrors", "yes");

		final HBCIPassport hbciPassport = AbstractHBCIPassport.getInstance(type);

		HBCIHandler hbciHandler = null;
		try {
			hbciHandler = new HBCIHandler(null, hbciPassport);
			final Properties updAlt = hbciPassport.getUPD();
			final Konto[] accounts = hbciPassport.getAccounts();

			for (final Konto account : accounts) {

				if (account.bic == null && account.iban == null && account.number != null
						&& account.number.length() <= 10 && account.blz != null) {
					account.iban = HBCIUtils.getIBANForKonto(account);
				}

				if (account.bic == null && account.iban != null) {
					final String blz = account.iban.substring(4, 12);
					final BankInfo bankInfo = HBCIUtils.getBankInfo(blz);
					if (bankInfo != null) {
						account.bic = bankInfo.getBic();
					}
				}

				final AccountMovementCollector accountMovementCollector = new AccountMovementCollector();
				final BalanceDailyCollector balanceDailyCollector = new BalanceDailyCollector();

				final List<AccountMovement> accountMovements = accountMovementCollector.collect(hbciHandler, account);
				this.mergeMissingUPD(hbciPassport, hbciHandler, updAlt);
				final BalanceDaily balanceDaily = balanceDailyCollector.collect(hbciHandler, account);
				this.mergeMissingUPD(hbciPassport, hbciHandler, updAlt);

				if (accountMovements != null && !accountMovements.isEmpty()) {
					for (final AccountMovement accountMovement : accountMovements) {
						this.addIbanBic(accountMovement);
					}
				}

				if (balanceDaily != null) {
					this.addIbanBic(balanceDaily);
				}

				this.executeHandler(new AccountMovementHandler(accountMovements), observerList);
				this.executeHandler(new BalanceDailyHandler(balanceDaily), observerList);
				this.executeHandler(new BalanceMonthlyHandler(balanceDaily, accountMovements), observerList);
			}

		} finally {
			if (hbciHandler != null) {
				hbciHandler.close();
			} else if (hbciPassport != null) {
				hbciPassport.close();
			}
		}
	}

	/**
	 * It happens, that during dialog init a UPD is returned which does not contain
	 * IBAN or BIC information. hbci4java does then just use this new UPD and
	 * overwrites the old UPD in the passport file. Because of that, IBAN and BIC is
	 * now missing in the UPD and the Account no longer contains those information.
	 * This renders the whole stuff useless for us.
	 *
	 * @param hbciPassport
	 * @param hbciHandler
	 * @param updAlt
	 */
	private void mergeMissingUPD(final HBCIPassport hbciPassport, final HBCIHandler hbciHandler,
			final Properties updAlt) {
		final Properties updNeu = hbciPassport.getUPD();
		final Set<String> propertyNames = updAlt.stringPropertyNames();
		System.out.println("Merging UPD...");
		for (final String name : propertyNames) {
			if (!updNeu.containsKey(name)) {
				System.out.println("UPD is missing: " + name);
				updNeu.setProperty(name, updAlt.getProperty(name));
			}
		}
		hbciHandler.getPassport().saveChanges();
	}

	private void addIbanBic(final AbstractAccountEntitiy accountEntity) {
		System.out.println("1===> MyIban: " + accountEntity.getMyIban());
		System.out.println("1===> MyBic: " + accountEntity.getMyBic());
		System.out.println("1===> MyAccountnumber: " + accountEntity.getMyAccountnumber());
		System.out.println("1===> MyBankcode: " + accountEntity.getMyBankcode());

		if (accountEntity.getMyAccountnumber() != null && accountEntity.getMyBankcode() != null) {
			if (accountEntity.getMyIban() == null) {
				accountEntity
						.setMyIban(this.getProperty("hbci.mapping.iban." + accountEntity.getMyAccountnumber().toString()
								+ "." + accountEntity.getMyBankcode().toString()));
				System.out.println("2===> MyIban: " + accountEntity.getMyIban());
			}
			if (accountEntity.getMyBic() == null) {
				accountEntity
						.setMyBic(this.getProperty("hbci.mapping.bic." + accountEntity.getMyAccountnumber().toString()
								+ "." + accountEntity.getMyBankcode().toString()));
				System.out.println("2===> MyBic: " + accountEntity.getMyBic());
			}
		}

		if (accountEntity instanceof final AccountMovement accountMovement) {
			System.out.println("3===> OtherIban: " + accountMovement.getOtherIban());
			System.out.println("3===> OtherBic: " + accountMovement.getOtherBic());
			System.out.println("3===> OtherAccountnumber: " + accountMovement.getOtherAccountnumber());
			System.out.println("3===> OtherBankcode: " + accountMovement.getOtherBankcode());
			if (accountMovement.getOtherIban() != null) {
				String bic = accountMovement.getOtherBic();
				if (bic.length() < 6) {
					final String blz = accountMovement.getOtherIban().substring(4, 12);
					final BankInfo bankInfo = HBCIUtils.getBankInfo(blz);
					if (bankInfo != null) {
						bic = bankInfo.getBic();
						System.out.println("4===> OtherBic: " + bic);
					} else {
						final String propertyBic = this.getProperty("hbci.mapping.blz." + blz);
						bic = propertyBic != null ? propertyBic : "XXXXXXXXXXX";
						System.out.println("5===> OtherBic: " + bic);
					}
					accountMovement.setOtherBic(bic);
				}
			}
		}

		System.out.println("==========================");

	}

	/**
	 * executes a given handler for all specified accounts
	 *
	 * @param handler
	 * @param hbciHandler
	 * @param observerList
	 * @param accounts
	 */
	private void executeHandler(final AbstractHandler handler, final List<PropertyChangeListener> observerList) {

		for (final PropertyChangeListener observer : observerList) {
			handler.addObserver(observer);
		}
		handler.handle();

	}

	/**
	 * returns the configured type for the given {@link HBCIPassport} filename
	 *
	 * @param passport
	 * @return online banking PIN
	 */
	private String getType(final String passport) {
		final File passportFile = new File(passport);
		final String type = this.getProperty("hbci." + passportFile.getName() + ".type");
		if (type == null) {
			throw new RuntimeException("type for passport " + passport + " not defined as property (hbci."
					+ passportFile.getName() + ".type)");
		}
		return type;

	}

	/**
	 * returns the configured password for the given {@link HBCIPassport} filename
	 *
	 * @param passport
	 * @return online banking PIN
	 */
	private String getPassword(final String passport) {
		final File passportFile = new File(passport);
		final String password = this.getProperty("hbci." + passportFile.getName() + ".password");
		if (password == null) {
			throw new RuntimeException("password for passport " + passport + " not defined as property (hbci."
					+ passportFile.getName() + ".password)");
		}
		return password;

	}

	/**
	 * returns the configured online banking PIN for the given {@link HBCIPassport}
	 * filename
	 *
	 * @param passport
	 * @return online banking PIN
	 */
	private String getPin(final String passport) {
		final File passportFile = new File(passport);
		final String pin = this.getProperty("hbci." + passportFile.getName() + ".pin");
		if (pin == null) {
			throw new RuntimeException("pin for passport " + passport + " not defined as property (hbci."
					+ passportFile.getName() + ".pin)");
		}
		return pin;

	}

	/**
	 * gets a property
	 *
	 * @param property
	 * @return property value
	 */
	private String getProperty(final String property) {
		return this.properties.getProperty(property);
	}

	/**
	 * creates the default hbci4java Properties valid for all of lalaHBCIs requests
	 *
	 * @return hbci4java {@link Properties}
	 */
	private Properties getHbciProperties() {
		final Properties hbciProperties = new Properties();
		hbciProperties.setProperty("client.passport.PinTan.checkcert", "1");
		hbciProperties.setProperty("client.passport.PinTan.init", "1");
		hbciProperties.setProperty("log.loglevel.default", "0");
		hbciProperties.setProperty("log.filter", "3");
		return hbciProperties;
	}
}
