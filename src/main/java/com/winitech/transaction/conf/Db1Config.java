package com.winitech.transaction.conf;

import com.winitech.transaction.mapper.Db1ConnMapper;
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
import java.util.Arrays;

@Configuration
@MapperScan(basePackages = "com.winitech.transaction.mapper" ,annotationClass= Db1ConnMapper.class, sqlSessionFactoryRef = "db1SqlSessionFactory")
@Log4j2
public class Db1Config {
	@Value("${spring.db1.datasource.config-location}")	    String config;
    @Value("${spring.db1.datasource.mapper-locations}")		String mapper;

	@Value("${spring.db1.datasource.schema-location}")		String schema;

	@Bean(name="db1DataSource")
	@ConfigurationProperties(prefix="spring.db1.datasource")
	public DataSource db1DbSource() {
		return DataSourceBuilder.create().type(HikariDataSource.class).build();
	}

	/**
	 * 프로파일이 test 이면 in memory h2 DB에 실행할 스크립트 실행
	 * */
	@Profile("test")
	@Bean(name="db1H2DataSourceInitializer")
	public DataSourceInitializer db1DataSourceInitializer(@Qualifier("db1DataSource") DataSource datasource) {
		log.info("============== DB1 schema-location : {}", new ClassPathResource(schema).getFilename());

		ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator();
		resourceDatabasePopulator.addScript(new ClassPathResource(schema));

		DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();
		dataSourceInitializer.setDataSource(datasource);
		dataSourceInitializer.setDatabasePopulator(resourceDatabasePopulator);

		return dataSourceInitializer;
	}

	@Bean(name="db1SqlSessionFactory")
	public SqlSessionFactory db1SqlSessionFactoryBean(
			@Autowired @Qualifier("db1DataSource") DataSource dataSource,
			ApplicationContext applicationContext
	) throws Exception
	{
		log.info("============== DB1 config-location : {}", applicationContext.getResource(config).getFilename());
		log.info("============== DB1 mapper-location : {}", applicationContext.getResource(mapper).getFilename());

	    SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
	    factoryBean.setDataSource(dataSource);
		factoryBean.setMapperLocations(applicationContext.getResources(mapper));

		Resource myBatisConfig = new PathMatchingResourcePatternResolver().getResource(config);
	    factoryBean.setConfigLocation(myBatisConfig);

	    return factoryBean.getObject();
	}

	@Bean(name="db1SqlSession")
	public SqlSession db1SqlSessionTemplate(@Autowired @Qualifier("db1SqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
	    return new SqlSessionTemplate(sqlSessionFactory);
	}

	@Bean(name="db1TransactionManager")
	public DataSourceTransactionManager db1TransactionManager(@Autowired @Qualifier("db1DataSource") DataSource dataSource) {
	    return new DataSourceTransactionManager(dataSource);
	}
}
