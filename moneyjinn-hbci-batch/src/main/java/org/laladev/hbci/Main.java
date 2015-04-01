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

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Observer;
import java.util.Properties;

import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.kapott.hbci.manager.HBCIUtils;
import org.kapott.hbci.passport.AbstractHBCIPassport;
import org.kapott.hbci.passport.HBCIPassport;
import org.laladev.hbci.callback.LalaMoneyflowHBCICallback;
import org.laladev.hbci.executor.LalaMoneyflowHBCIExecutor;

public final class Main {
	public void main(final List<String> passports, final String propertyFilename, final List<Observer> observer)
			throws Exception {
		final FileInputStream passFile = new FileInputStream(propertyFilename);
		final Properties passProperties = new Properties();
		passProperties.load(passFile);
		passFile.close();

		for (final String passport : passports) {
			process(passport, passProperties, observer);
		}
	}

	private void process(final String passport, final Properties passProperties, final List<Observer> observers)
			throws IOException {
		final Properties hbciProperties = new Properties();
		hbciProperties.setProperty("client.passport.PinTan.filename", passport);
		hbciProperties.setProperty("client.product.name", "HBCI4Java");
		hbciProperties.setProperty("client.product.version", "2.5");
		hbciProperties.setProperty("client.passport.PinTan.checkcert", "1");
		hbciProperties.setProperty("client.passport.PinTan.init", "1");
		hbciProperties.setProperty("client.passport.default", "PinTan");
		hbciProperties.setProperty("log.loglevel.default", "0");
		hbciProperties.setProperty("log.filter", "3");

		HBCIUtils.init(hbciProperties, new LalaMoneyflowHBCICallback(passProperties));

		final Configuration configuration = new Configuration().configure();
		final StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
				.applySettings(configuration.getProperties());
		final SessionFactory sf = configuration.buildSessionFactory(builder.build());
		final StatelessSession session = sf.openStatelessSession();

		try {
			final Transaction tx = session.beginTransaction();
			final LalaMoneyflowHBCIExecutor executor = new LalaMoneyflowHBCIExecutor(session);
			final HBCIPassport hbciPassport = AbstractHBCIPassport.getInstance();

			for (final Observer observer : observers) {
				executor.addObserver(observer);
			}

			executor.execute(hbciPassport);
			tx.commit();
		} finally {
			session.close();
			sf.close();
		}

		HBCIUtils.done();

	}
}
