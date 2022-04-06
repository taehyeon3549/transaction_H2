package com.winitech.transaction.conf;

import com.winitech.transaction.mapper.Db2ConnMapper;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.log4j.Log4j2;
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
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;

@Configuration
@Log4j2
@MapperScan(basePackages = "com.winitech.transaction.mapper", annotationClass= Db2ConnMapper.class, sqlSessionFactoryRef = "db2SqlSessionFactory")
public class Db2Config {
	@Value("${spring.db2.datasource.config-location}")	    String config;
	@Value("${spring.db2.datasource.mapper-locations}")		String mapper;

	@Value("${spring.db2.datasource.schema-location}")		String schema;

	@Bean(name="db2DataSource")
	@ConfigurationProperties(prefix="spring.db2.datasource")
	public DataSource db2DbSource() {
		return DataSourceBuilder.create().type(HikariDataSource.class).build();
	}

	/**
	 * 프로파일이 test 이면 in memory h2 DB에 실행할 스크립트 실행
	 * */
	@Profile("test")
	@Bean(name="db2H2DataSourceInitializer")
	public DataSourceInitializer db2DataSourceInitializer(@Qualifier("db2DataSource") DataSource datasource) {
		log.info("============== DB2 schema-location : {}", new ClassPathResource(schema).getFilename());

		ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator();
		resourceDatabasePopulator.addScript(new ClassPathResource(schema));

		DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();
		dataSourceInitializer.setDataSource(datasource);
		dataSourceInitializer.setDatabasePopulator(resourceDatabasePopulator);

		return dataSourceInitializer;
	}

	@Bean(name="db2SqlSessionFactory")
	public SqlSessionFactory db2SqlSessionFactoryBean(
			@Autowired @Qualifier("db2DataSource") DataSource dataSource,
			ApplicationContext applicationContext
	) throws Exception
	{
		log.info("============== DB2 config-location : {}", applicationContext.getResource(config).getFilename());
		log.info("============== DB2 mapper-location : {}", applicationContext.getResource(mapper).getFilename());

	    SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
	    factoryBean.setDataSource(dataSource);
		factoryBean.setMapperLocations(applicationContext.getResources(mapper));

		Resource myBatisConfig = new PathMatchingResourcePatternResolver().getResource(config);
		factoryBean.setConfigLocation(myBatisConfig);

	    return factoryBean.getObject();
	}

	@Bean(name="db2SqlSession")
	public SqlSession db2SqlSessionTemplate(@Autowired @Qualifier("db2SqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
	    return new SqlSessionTemplate(sqlSessionFactory);
	}

	@Bean(name="db2TransactionManager")
	public DataSourceTransactionManager db2TransactionManager(@Autowired @Qualifier("db2DataSource") DataSource dataSource) {
	    return new DataSourceTransactionManager(dataSource);
	}
}
