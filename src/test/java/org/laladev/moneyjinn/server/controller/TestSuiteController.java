package org.laladev.moneyjinn.server.controller;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.laladev.moneyjinn.server.controller.capitalsource.TestSuiteCapitalsource;
import org.laladev.moneyjinn.server.controller.contractpartner.TestSuiteContractpartner;
import org.laladev.moneyjinn.server.controller.contractpartneraccount.TestSuiteContractpartnerAccount;
import org.laladev.moneyjinn.server.controller.group.TestSuiteGroup;
import org.laladev.moneyjinn.server.controller.moneyflow.TestSuiteMoneyflow;
import org.laladev.moneyjinn.server.controller.monthlysettlement.TestSuiteMonthlySettlement;
import org.laladev.moneyjinn.server.controller.postingaccount.TestSuitePostingAccount;
import org.laladev.moneyjinn.server.controller.predefmoneyflow.TestSuitePreDefMoneyflow;
import org.laladev.moneyjinn.server.controller.setting.TestSuiteSetting;
import org.laladev.moneyjinn.server.controller.user.TestSuiteUser;

@RunWith(Suite.class)
@Suite.SuiteClasses({ //
		TestSuiteCapitalsource.class, //
		TestSuiteContractpartner.class, //
		TestSuiteContractpartnerAccount.class, //
		TestSuiteGroup.class, //
		TestSuiteMoneyflow.class, //
		TestSuiteMonthlySettlement.class, //
		TestSuitePostingAccount.class, //
		TestSuitePreDefMoneyflow.class, //
		TestSuiteSetting.class, //
		TestSuiteUser.class, //
})
public class TestSuiteController {

}
