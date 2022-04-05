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
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.util.Arrays;

@Configuration
@MapperScan(basePackages = "com.winitech.transaction.mapper" ,annotationClass= Db1ConnMapper.class, sqlSessionFactoryRef = "db1SqlSessionFactory")
@Log4j2
//@MapperScan("com.winitech.transaction.mapper")		//@Mapper 어노테이션을 사용했으므로 비활성화, package단위로 scan 가능
public class Db1Config {
	@Value("${spring.db1.datasource.config-location}")	    String config;
    @Value("${spring.db1.datasource.mapper-locations}")		String mapper;

	@Bean(name="db1DataSource")
	@ConfigurationProperties(prefix="spring.db1.datasource")
	public DataSource db1DbSource() {
		return DataSourceBuilder.create().type(HikariDataSource.class).build();
	}

	@Bean(name="db1SqlSessionFactory")
	public SqlSessionFactory db1SqlSessionFactoryBean(
			@Autowired @Qualifier("db1DataSource") DataSource dataSource,
			ApplicationContext applicationContext
	) throws Exception
	{
		log.info("config-location : {}", applicationContext.getResource(config).getFilename());
//		log.info("mapper-locations : {}", applicationContext.getResources(mapper).getFilename());

		Arrays.stream(applicationContext.getResources(mapper)).forEach(p->{
			log.info("파일명 :: {}", p.getFilename());
		});


	    SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
	    factoryBean.setDataSource(dataSource);
		factoryBean.setMapperLocations(applicationContext.getResources(mapper));

		Resource myBatisConfig = new PathMatchingResourcePatternResolver().getResource(config);
	    factoryBean.setConfigLocation(myBatisConfig);

	    return factoryBean.getObject();
	}

	@Bean(name="db1SqlSession")
	public SqlSession db1sqlSessionTemplate(@Autowired @Qualifier("db1SqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
	    return new SqlSessionTemplate(sqlSessionFactory);
	}

	@Bean(name="db1TransactionManager")
	public DataSourceTransactionManager db1transactionManager(@Autowired @Qualifier("db1DataSource") DataSource dataSource) {
	    return new DataSourceTransactionManager(dataSource);
	}
}
