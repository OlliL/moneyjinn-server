package org.laladev.moneyjinn.server.config;

import org.h2.Driver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

@Configuration
public class TestDatabaseConfiguration {
	// @Bean
	// public EmbeddedDatabase dataSource() {
	// return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).addScript("h2dump.sql")
	// .addScript("h2ext.sql").build();
	// }

	@Bean
	public SimpleDriverDataSource simpleDriverDataSource() {
		final SimpleDriverDataSource ds = new SimpleDriverDataSource();
		ds.setDriverClass(Driver.class);
		ds.setUrl("jdbc:h2:mem:test;MODE=mysql;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE");
		ds.setUsername("sa");
		ds.setPassword("");

		return ds;
	}

	// @Value("classpath:h2dump.sql")
	// private Resource schemaScript;
	//
	// @Value("classpath:h2ext.sql")
	// private Resource dataScript;
	// @Bean
	// public DataSourceInitializer dataSourceInitializer(final DataSource dataSource) {
	// final DataSourceInitializer initializer = new DataSourceInitializer();
	// initializer.setDataSource(dataSource);
	// initializer.setDatabasePopulator(databasePopulator());
	// return initializer;
	// }
	//
	// private DatabasePopulator databasePopulator() {
	// final ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
	// populator.addScript(schemaScript);
	// populator.addScript(dataScript);
	// return populator;
	// }
}
