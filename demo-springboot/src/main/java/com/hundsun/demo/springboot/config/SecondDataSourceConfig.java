package com.hundsun.demo.springboot.config;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import tk.mybatis.spring.annotation.MapperScan;

import javax.sql.DataSource;

@Configuration
// 扫描 Mapper 接口并容器管理
@MapperScan(basePackages = SecondDataSourceConfig.PACKAGE, sqlSessionFactoryRef = "secondSqlSessionFactory")
public class SecondDataSourceConfig {

    // 精确到 cluster 目录，以便跟其他数据源隔离
    static final String PACKAGE = "com.hundsun.demo.springboot.mapper.oracle";
    static final String MAPPER_LOCATION = "classpath:mapper/oracle/*.xml";

    @Value("${second.datasource.url}")
    private String url;

    @Value("${second.datasource.username}")
    private String user;

    @Value("${second.datasource.password}")
    private String password;

    @Value("${second.datasource.driverClassName}")
    private String driverClass;

    @Bean(name = "secondDataSource")
    public DataSource clusterDataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(driverClass);
        dataSource.setJdbcUrl(url);
        dataSource.setUsername(user);
        dataSource.setPassword(password);
        return dataSource;
    }

    @Bean(name = "secondTransactionManager")
    public DataSourceTransactionManager clusterTransactionManager() {
        return new DataSourceTransactionManager(clusterDataSource());
    }

    @Bean(name = "secondSqlSessionFactory")
    public SqlSessionFactory clusterSqlSessionFactory(@Qualifier("secondDataSource") DataSource clusterDataSource)
            throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(clusterDataSource);
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources(SecondDataSourceConfig.MAPPER_LOCATION));
        return sessionFactory.getObject();
    }
}