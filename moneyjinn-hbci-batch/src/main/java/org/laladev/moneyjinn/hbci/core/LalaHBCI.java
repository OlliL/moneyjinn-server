//
// Copyright (c) 2014-2016 Oliver Lehmann <lehmann@ans-netz.de>
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
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
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

  public void main(final List<String> passports, final List<PropertyChangeListener> observerList)
      throws Exception {

    HBCIUtils.init(this.getHbciProperties(), new LalaHBCICallback());

    final SessionFactory sf = this.getSessionFactory();
    final EntityManager entityManager = sf.createEntityManager();
    try {
      for (final String passport : passports) {
        this.process(passport, entityManager, observerList);
      }
    } finally {
      entityManager.close();
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
  private void process(final String passport, final EntityManager entityManager,
      final List<PropertyChangeListener> observerList) throws IOException {

    ((LalaHBCICallback) HBCIUtilsInternal.getCallback())
        .setPassportPassword(this.getPassword(passport));

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

    final EntityTransaction tx = entityManager.getTransaction();
    tx.begin();
    final HBCIPassport hbciPassport = AbstractHBCIPassport.getInstance(type);

    HBCIHandler hbciHandler = null;
    try {
      hbciHandler = new HBCIHandler(null, hbciPassport);
      final Properties updAlt = hbciPassport.getUPD();
      final Konto[] accounts = hbciPassport.getAccounts();

      for (final Konto account : accounts) {
        final AccountMovementCollector accountMovementCollector = new AccountMovementCollector();
        final BalanceDailyCollector balanceDailyCollector = new BalanceDailyCollector();

        final List<AccountMovement> accountMovements = accountMovementCollector.collect(hbciHandler,
            account);
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

        this.executeHandler(new AccountMovementHandler(entityManager, accountMovements),
            observerList);
        this.executeHandler(new BalanceDailyHandler(entityManager, balanceDaily), observerList);
        this.executeHandler(
            new BalanceMonthlyHandler(entityManager, balanceDaily, accountMovements), observerList);
      }

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
   * It happens, that during dialog init a UPD is returned which does not contain IBAN or BIC
   * information. hbci4java does then just use this new UPD and overwrites the old UPD in the
   * passport file. Because of that, IBAN and BIC is now missing in the UPD and the Account no
   * longer contains those information. This renders the whole stuff useless for us.
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
    System.out.println("====> MyIban: " + accountEntity.getMyIban());
    System.out.println("====> MyAccountnumber: " + accountEntity.getMyAccountnumber());
    System.out.println("====> MyBankcode: " + accountEntity.getMyBankcode());
    if (accountEntity.getMyIban() == null && accountEntity.getMyAccountnumber() != null
        && accountEntity.getMyBankcode() != null) {
      accountEntity.setMyIban(
          this.getProperty("hbci.mapping.iban." + accountEntity.getMyAccountnumber().toString()
              + "." + accountEntity.getMyBankcode().toString()));
      accountEntity.setMyBic(
          this.getProperty("hbci.mapping.bic." + accountEntity.getMyAccountnumber().toString() + "."
              + accountEntity.getMyBankcode().toString()));
    }
  }

  /**
   * executes a given handler for all specified accounts
   *
   * @param handler
   * @param hbciHandler
   * @param observerList
   * @param accounts
   */
  private void executeHandler(final AbstractHandler handler,
      final List<PropertyChangeListener> observerList) {

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
      throw new RuntimeException("password for passport " + passport
          + " not defined as property (hbci." + passportFile.getName() + ".password)");
    }
    return password;

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
    hbciProperties.setProperty("client.passport.PinTan.checkcert", "1");
    hbciProperties.setProperty("client.passport.PinTan.init", "1");
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
    return new Configuration().configure().buildSessionFactory();
  }
}
