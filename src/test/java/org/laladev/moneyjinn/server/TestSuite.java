package org.laladev.moneyjinn.server;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.laladev.moneyjinn.server.controller.TestSuiteController;
import org.laladev.moneyjinn.server.dao.data.mapper.AccessFlattenedDataMapperTest;
import org.laladev.moneyjinn.server.main.MoneyJinnHandlerInterceptorTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({ TestSuiteController.class, MoneyJinnHandlerInterceptorTest.class,
		AccessFlattenedDataMapperTest.class })
public class TestSuite {

}
