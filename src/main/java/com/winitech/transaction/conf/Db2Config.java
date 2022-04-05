package com.winitech.transaction.conf;

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
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "com.winitech.transaction.mapper", annotationClass= Db2ConnMapper.class, sqlSessionFactoryRef = "db2SqlSessionFactory")
public class Db2Config {
	@Value("${spring.db2.datasource.config-location}")	    String config;
	@Value("${spring.db2.datasource.mapper-locations}")		String mapper;

	@Bean(name="db2DataSource")
	@ConfigurationProperties(prefix="spring.db2.datasource")
	public DataSource db2DbSource() {
		return DataSourceBuilder.create().type(HikariDataSource.class).build();
	}

	@Bean(name="db2SqlSessionFactory")
	public SqlSessionFactory db2SqlSessionFactoryBean(
			@Autowired @Qualifier("db2DataSource") DataSource dataSource,
			ApplicationContext applicationContext
	) throws Exception
	{
	    SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
	    factoryBean.setDataSource(dataSource);
		factoryBean.setMapperLocations(applicationContext.getResources(mapper));

		Resource myBatisConfig = new PathMatchingResourcePatternResolver().getResource(config);
		factoryBean.setConfigLocation(myBatisConfig);

	    return factoryBean.getObject();
	}

	@Bean(name="db2SqlSession")
	public SqlSession db2sqlSessionTemplate(@Autowired @Qualifier("db2SqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
	    return new SqlSessionTemplate(sqlSessionFactory);
	}

	@Bean(name="db2TransactionManager")
	public DataSourceTransactionManager db2transactionManager(@Autowired @Qualifier("db2DataSource") DataSource dataSource) {
	    return new DataSourceTransactionManager(dataSource);
	}
}
