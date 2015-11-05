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
// $Id: Main.java,v 1.20 2015/10/01 17:56:26 olivleh1 Exp $
//
package org.laladev.moneyjinn.hbci.batch.main;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Observer;
import java.util.Properties;

import org.laladev.moneyjinn.hbci.batch.handler.ScreenScrapingHandler;
import org.laladev.moneyjinn.hbci.batch.subscriber.AccountMovementObserver;
import org.laladev.moneyjinn.hbci.batch.subscriber.BalanceDailyObserver;
import org.laladev.moneyjinn.hbci.batch.subscriber.BalanceMonthlyObserver;
import org.laladev.moneyjinn.hbci.core.LalaHBCI;

public final class Main {
	public static void main(final String[] args) throws Exception {

		final FileInputStream propertyFile = new FileInputStream(
				System.getProperty("user.home") + File.separator + "hbci_pass.properties");
		final Properties properties = new Properties();
		properties.load(propertyFile);
		propertyFile.close();

		final List<Observer> observers = new ArrayList<Observer>(1);
		observers.add(new AccountMovementObserver());
		observers.add(new BalanceMonthlyObserver());
		observers.add(new BalanceDailyObserver());

		final LalaHBCI lalaHBCI = new LalaHBCI(properties);
		final List<String> passports = new ArrayList<String>();
		final String[] passportFiles = properties.getProperty("hbci.passport.files").split(",");
		for (final String passportFile : passportFiles) {
			passports.add(System.getProperty("user.home") + File.separator + passportFile);
		}

		lalaHBCI.main(passports, observers);

		final ScreenScrapingHandler scrapingHandler = new ScreenScrapingHandler();
		scrapingHandler.handle(properties, observers);

	}
}
