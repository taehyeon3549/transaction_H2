package com.winitech.transaction.conf;

import com.winitech.transaction.mapper.Db1ConnMapper;
import com.winitech.transaction.mapper.Db2ConnMapper;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;

@Configuration
//@MapperScan(basePackages = "com.winitech.transaction.mapper", sqlSessionFactoryRef = "db3SqlSessionFactory")
public class Db3Config {
	@Value("${spring.db3.datasource.mapper-locations}")		String mapper;
	@Value("${spring.db3.datasource.schema-location}")		String schema;

	@Bean(name="db3DataSource")
	@ConfigurationProperties(prefix="spring.db3.datasource")
	public DataSource db3DbSource() {
		return DataSourceBuilder.create().type(HikariDataSource.class).build();
	}

	@Bean(name="dbH2DataSourceInitializer")
	public DataSourceInitializer dataSourceInitializer(@Qualifier("db3DataSource") DataSource datasource) {
		ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator();
		resourceDatabasePopulator.addScript(new ClassPathResource(schema));
//		resourceDatabasePopulator.addScript(new ClassPathResource("config/database/h2-data.sql"));

		DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();
		dataSourceInitializer.setDataSource(datasource);
		dataSourceInitializer.setDatabasePopulator(resourceDatabasePopulator);

		return dataSourceInitializer;
	}

	@Bean(name="db3SqlSessionFactory")
	public SqlSessionFactory db3SqlSessionFactoryBean(
			@Autowired @Qualifier("db3DataSource") DataSource dataSource,
			ApplicationContext applicationContext
	) throws Exception
	{
	    SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
	    factoryBean.setDataSource(dataSource);
		factoryBean.setMapperLocations(applicationContext.getResources(mapper));

//		Resource myBatisConfig = new PathMatchingResourcePatternResolver().getResource(config);
//		factoryBean.setConfigLocation(myBatisConfig);

	    return factoryBean.getObject();
	}

	@Bean(name="db3SqlSession")
	public SqlSession db3sqlSessionTemplate(@Autowired @Qualifier("db3SqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
	    return new SqlSessionTemplate(sqlSessionFactory);
	}

	@Bean(name="db3TransactionManager")
	public DataSourceTransactionManager db3transactionManager(@Autowired @Qualifier("db3DataSource") DataSource dataSource) {
	    return new DataSourceTransactionManager(dataSource);
	}
}
