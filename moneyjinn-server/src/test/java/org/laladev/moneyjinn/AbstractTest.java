
package org.laladev.moneyjinn;

import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.laladev.moneyjinn.config.MockConfiguration;
import org.laladev.moneyjinn.config.TestDatabaseConfiguration;
import org.laladev.moneyjinn.server.config.MoneyjinnTestConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.web.WebAppConfiguration;

@SpringBootTest
@ContextConfiguration(classes = { MoneyjinnTestConfiguration.class, MockConfiguration.class,
    TestDatabaseConfiguration.class })
@WebAppConfiguration
@SqlGroup({ @Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = {
    "classpath:h2defaults.sql", "classpath:testdata.sql" }) })
@ActiveProfiles("test")
public abstract class AbstractTest {
  @Inject
  CacheManager cacheManager;

  @BeforeEach
  public void before() {
    this.cacheManager.getCacheNames().stream().map(this.cacheManager::getCache)
        .forEach(Cache::clear);
  }
}
