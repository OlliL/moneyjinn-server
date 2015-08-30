package org.laladev.moneyjinn.server.controller.user;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ CreateUserTest.class, DeleteUserByIdTest.class, GetUserSettingsForStartupTest.class,
		ShowCreateUserTest.class, ShowDeleteUserTest.class, ShowEditUserTest.class, UpdateUserTest.class })
public class TestSuiteUser {

}
