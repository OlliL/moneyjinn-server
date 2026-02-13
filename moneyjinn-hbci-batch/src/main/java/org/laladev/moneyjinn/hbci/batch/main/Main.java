//
// Copyright (c) 2014-2025 Oliver Lehmann <lehmann@ans-netz.de>
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

package org.laladev.moneyjinn.hbci.batch.main;

import org.laladev.moneyjinn.hbci.backend.ApiException;
import org.laladev.moneyjinn.hbci.backend.api.UserControllerApi;
import org.laladev.moneyjinn.hbci.backend.model.LoginRequest;
import org.laladev.moneyjinn.hbci.backend.model.LoginResponse;
import org.laladev.moneyjinn.hbci.batch.subscriber.AccountMovementObserver;
import org.laladev.moneyjinn.hbci.batch.subscriber.BalanceDailyObserver;
import org.laladev.moneyjinn.hbci.batch.subscriber.BalanceMonthlyObserver;
import org.laladev.moneyjinn.hbci.core.LalaHBCI;

import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public final class Main {
    public static void main(final String[] args) throws Exception {

        final Properties properties = new Properties();
        try (final FileInputStream propertyFile = new FileInputStream(
                System.getProperty("user.home") + File.separator + "hbci_pass.properties")) {
            properties.load(propertyFile);
        }

        final List<PropertyChangeListener> observers = new ArrayList<>(1);
        observers.add(new AccountMovementObserver());
        observers.add(new BalanceMonthlyObserver());
        observers.add(new BalanceDailyObserver());

        final LalaHBCI lalaHBCI = new LalaHBCI(properties);
        final List<String> passports = new ArrayList<>();
        final String[] passportFiles = properties.getProperty("hbci.passport.files").split(",");
        for (final String passportFile : passportFiles) {
            passports.add(System.getProperty("user.home") + File.separator + passportFile);
        }

        try (final Connection con = connectToDatabase(properties.getProperty("hbci.database.url"),
                properties.getProperty("hbci.database.username"), properties.getProperty("hbci.database.password"))) {
            login(properties.getProperty("hbci.server.username"), properties.getProperty("hbci.server.password"));

            lalaHBCI.process(passports, observers);
        }
    }

    private static Connection connectToDatabase(final String url, final String username, final String password)
            throws ClassNotFoundException, SQLException {
        final Connection con = DriverManager.getConnection(url, username, password);
        con.setAutoCommit(false);

        MoneyjinnConnectionHolder.setConnection(con);

        return con;
    }

    private static void login(final String username, final String password) throws ApiException {
        MoneyjinnApiClient.initialize();
        final UserControllerApi userControllerApi = new UserControllerApi(MoneyjinnApiClient.getApiClient());
        final LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUserName(username);
        loginRequest.setUserPassword(password);

        final LoginResponse loginResponse = userControllerApi.login(loginRequest);
        MoneyjinnApiClient.setJwtToken(loginResponse.getToken());
    }
}
