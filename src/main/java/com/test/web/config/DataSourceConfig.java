package com.test.web.config;

import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * Created by 青葱的熊猫 on 2017/5/26.
 * 数据源配置类(此类的存在是为了解决多数据源的存在)
 * @EnableTransactionManagement
 * Spring容器会自动扫描注解 @Transactional的方法和类
 */
@Configuration
@EnableTransactionManagement
public class DataSourceConfig {


    /**
     * WEB端主数据源
     * @Primary 在多数据源时可以用来指定默认数据源
     * @return
     */
    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.web")
    public DataSource webDataSource(){
        return DataSourceBuilder.create().build();
    }

    @Bean
    @Primary
    public JdbcTemplate webJdbcTemplate(DataSource webDataSource){
        return new JdbcTemplate(webDataSource);
    }

    /**
     * 事务管理器
     * 相当于<tx:annotation-driven />
     * @param webDataSource
     * @return
     */
    @Bean
    @Primary
    public PlatformTransactionManager txManager(DataSource webDataSource){
        return new DataSourceTransactionManager(webDataSource);
    }
}
