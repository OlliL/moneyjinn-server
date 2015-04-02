//
// Copyright (c) 2014-2015 Oliver Lehmann <oliver@laladev.org>
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
// $Id: Main.java,v 1.14 2015/03/31 18:55:59 olivleh1 Exp $
//
package org.laladev.hbci;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Observer;
import java.util.Properties;

import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.ImprovedNamingStrategy;
import org.kapott.hbci.manager.HBCIHandler;
import org.kapott.hbci.manager.HBCIUtils;
import org.kapott.hbci.manager.HBCIUtilsInternal;
import org.kapott.hbci.passport.AbstractHBCIPassport;
import org.kapott.hbci.passport.HBCIPassport;
import org.kapott.hbci.structures.Konto;
import org.laladev.hbci.handler.AbstractHandler;
import org.laladev.hbci.handler.AccountMovementHandler;

public final class LalaHBCI {
	private final Properties properties;

	public LalaHBCI(final Properties properties) {
		this.properties = properties;
	}

	public void main(final List<String> passports, final List<Observer> observerList) throws Exception {

		HBCIUtils.init(this.getHbciProperties(), new LalaHBCICallback(this.getProperty("hbci.passport.password")));

		final SessionFactory sf = this.getSessionFactory();
		final StatelessSession session = sf.openStatelessSession();

		try {
			for (final String passport : passports) {
				process(passport, session, observerList);
			}
		} finally {
			session.close();
			sf.close();
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
	private void process(final String passport, final StatelessSession session, final List<Observer> observerList)
			throws IOException {

		HBCIUtils.setParam("client.passport.PinTan.filename", passport);
		((LalaHBCICallback) HBCIUtilsInternal.getCallback()).setPin(this.getPin(passport));

		final Transaction tx = session.beginTransaction();
		final HBCIPassport hbciPassport = AbstractHBCIPassport.getInstance();

		HBCIHandler hbciHandler = null;
		try {
			hbciHandler = new HBCIHandler(null, hbciPassport);
			final Konto[] accounts = hbciPassport.getAccounts();

			executeHandler(new AccountMovementHandler(session), hbciHandler, observerList, accounts);

		} finally {
			if (hbciHandler != null) {
				hbciHandler.close();
			} else if (hbciPassport != null) {
				hbciPassport.close();
			}
		}

		tx.commit();

	}

	/**
	 * executes a given handler for all specified accounts
	 *
	 * @param handler
	 * @param hbciHandler
	 * @param observerList
	 * @param accounts
	 */
	private void executeHandler(final AbstractHandler handler, final HBCIHandler hbciHandler,
			final List<Observer> observerList, final Konto[] accounts) {
		for (final Observer observer : observerList) {
			handler.addObserver(observer);
		}
		for (final Konto account : accounts) {
			handler.handle(hbciHandler, account);
		}

	}

	/**
	 * returns the configured online banking PIN for the given {@link HBCIPassport} filename
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
		hbciProperties.setProperty("client.product.name", "HBCI4Java");
		hbciProperties.setProperty("client.product.version", "2.5");
		hbciProperties.setProperty("client.passport.PinTan.checkcert", "1");
		hbciProperties.setProperty("client.passport.PinTan.init", "1");
		hbciProperties.setProperty("client.passport.default", "PinTan");
		hbciProperties.setProperty("log.loglevel.default", "0");
		hbciProperties.setProperty("log.filter", "3");
		return hbciProperties;
	}

	/**
	 * creates and returns the Hibernate {@link SessionFactory} lalaHBCI works with
	 *
	 * @return {@link SessionFactory}
	 */
	private SessionFactory getSessionFactory() {
		final Configuration configuration = new Configuration().setNamingStrategy(ImprovedNamingStrategy.INSTANCE)
				.configure();
		final StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
				.applySettings(configuration.getProperties());
		return configuration.buildSessionFactory(builder.build());

	}
}
