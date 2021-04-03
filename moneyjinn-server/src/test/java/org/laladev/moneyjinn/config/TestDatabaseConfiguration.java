package org.laladev.moneyjinn.config;

import javax.sql.DataSource;

import org.h2.Driver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

@Configuration
public class TestDatabaseConfiguration {

	/**
	 * Configures the DataSource for the Unit-Test-Context.
	 *
	 * @return H2 DataSource
	 */
	@Bean
	public DataSource getDataSource() {
		final DataSource dataSource = this.simpleDriverDataSource();
		DatabasePopulatorUtils.execute(this.databasePopulator(), dataSource);
		return dataSource;
	}

	private SimpleDriverDataSource simpleDriverDataSource() {

		// final Mode mode = Mode.getInstance("MYSQL");

		final SimpleDriverDataSource ds = new SimpleDriverDataSource();
		ds.setDriverClass(Driver.class);
		ds.setUrl("jdbc:h2:mem:test;MODE=mysql;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE");
		// for debugging:
		// ds.setUrl(
		// "jdbc:h2:mem:test;MODE=mysql;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;TRACE_LEVEL_FIle=4;TRACE_LEVEL_SYSTEM_OUT=3");
		ds.setUsername("sa");
		ds.setPassword("");

		return ds;
	}

	@Value("classpath:h2dropAll.sql")
	private Resource dropAllScript;

	@Value("classpath:h2dump.sql")
	private Resource schemaScript;

	@Value("classpath:h2ext.sql")
	private Resource dataScript;

	private DatabasePopulator databasePopulator() {
		final ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
		populator.addScript(this.dropAllScript);
		populator.addScript(this.schemaScript);
		populator.addScript(this.dataScript);
		return populator;
	}
}
