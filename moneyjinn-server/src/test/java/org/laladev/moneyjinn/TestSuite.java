package org.laladev.moneyjinn;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.laladev.moneyjinn.businesslogic.TestSuiteBusinesslogic;
import org.laladev.moneyjinn.server.TestSuiteServer;

@RunWith(Suite.class)
@Suite.SuiteClasses({ TestSuiteBusinesslogic.class, TestSuiteServer.class })
public class TestSuite {

}
