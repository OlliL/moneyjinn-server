package org.laladev.moneyjinn.server.config;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.laladev.moneyjinn.api.MoneyjinnProfiles;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@MapperScan("org.laladev.moneyjinn.businesslogic.dao.mapper")
@Profile("!" + MoneyjinnProfiles.TEST)
public class DatabaseConfiguration {
	@Inject
	private DataSource pool;

	@Bean
	public SqlSessionFactory sqlSessionFactoryBean() throws Exception {

		final SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(this.pool);

		final PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		sqlSessionFactoryBean.setMapperLocations(
				resolver.getResources("classpath:org/laladev/moneyjinn/businesslogic/dao/mapper/*.xml"));

		return sqlSessionFactoryBean.getObject();
	}

	@Bean
	public PlatformTransactionManager transactionManager() {
		return new DataSourceTransactionManager(this.pool);
	}

}
