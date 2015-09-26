package org.laladev.moneyjinn.businesslogic;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.laladev.moneyjinn.businesslogic.dao.data.mapper.AccessFlattenedDataMapperTest;
import org.laladev.moneyjinn.businesslogic.service.impl.TestSuiteService;

@RunWith(Suite.class)
@Suite.SuiteClasses({ TestSuiteService.class, AccessFlattenedDataMapperTest.class })
public class TestSuiteBusinesslogic {

}
