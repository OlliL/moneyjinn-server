package org.laladev.moneyjinn.server.controller;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.laladev.moneyjinn.server.controller.capitalsource.TestSuiteCapitalsource;
import org.laladev.moneyjinn.server.controller.group.TestSuiteGroup;
import org.laladev.moneyjinn.server.controller.postingaccount.TestSuitePostingAccount;
import org.laladev.moneyjinn.server.controller.setting.TestSuiteSetting;
import org.laladev.moneyjinn.server.controller.user.TestSuiteUser;

@RunWith(Suite.class)
@Suite.SuiteClasses({ TestSuiteCapitalsource.class, TestSuiteGroup.class, TestSuitePostingAccount.class,
		TestSuiteSetting.class, TestSuiteUser.class })
public class TestSuiteController {

}
