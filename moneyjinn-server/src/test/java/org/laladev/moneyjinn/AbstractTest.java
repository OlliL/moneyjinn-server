package org.laladev.moneyjinn;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.laladev.moneyjinn.server.config.MoneyjinnConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
// @SpringApplicationConfiguration(classes = { MoneyjinnConfiguration.class })
// @EnableAutoConfiguration
@SpringBootTest
@ContextConfiguration(classes = { MoneyjinnConfiguration.class })
@WebAppConfiguration
@SqlGroup({ @Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = { "classpath:h2defaults.sql",
		"classpath:testdata.sql" }) })
public abstract class AbstractTest {

	@Inject
	CacheManager cacheManager;

	@Before
	public void before() {
		this.cacheManager.getCacheNames().stream().map(this.cacheManager::getCache).forEach(Cache::clear);
	}

}
