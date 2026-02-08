package org.laladev.moneyjinn;

import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.laladev.moneyjinn.config.MoneyjinnTestConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

import java.util.Objects;

@SpringBootTest
@ContextConfiguration(classes = {MoneyjinnTestConfiguration.class})
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD,
        scripts = {"classpath:h2reinit.sql", "classpath:h2defaults.sql", "classpath:testdata.sql"})
@ActiveProfiles("test")
public abstract class AbstractTest {
    @Inject
    private CacheManager cacheManager;

    @BeforeEach
    public void before() {
        this.cacheManager.getCacheNames().stream()
                .map(this.cacheManager::getCache)
                .filter(Objects::nonNull)
                .forEach(Cache::clear);
    }
}
