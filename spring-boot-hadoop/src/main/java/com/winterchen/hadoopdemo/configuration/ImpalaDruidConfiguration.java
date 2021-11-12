package com.winterchen.hadoopdemo.configuration;

import com.alibaba.druid.pool.DruidDataSource;
import com.winterchen.hadoopdemo.properties.DataSourceCommonProperties;
import com.winterchen.hadoopdemo.properties.ImpalaJdbcProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;

@Slf4j
@Configuration
@EnableConfigurationProperties({ImpalaJdbcProperties.class, DataSourceCommonProperties.class})
public class ImpalaDruidConfiguration {

    @Autowired
    private ImpalaJdbcProperties impalaJdbcProperties;

    @Autowired
    private DataSourceCommonProperties dataSourceCommonProperties;

    @Bean("impalaDruidDataSource")
    @Qualifier("impalaDruidDataSource")
    public DataSource dataSource() {
        DruidDataSource datasource = new DruidDataSource();

        //配置数据源属性
        datasource.setUrl(impalaJdbcProperties.getUrl());
        datasource.setUsername(impalaJdbcProperties.getUsername());
        datasource.setPassword(impalaJdbcProperties.getPassword());
        datasource.setDriverClassName(impalaJdbcProperties.getDriverClassName());

        //配置统一属性
        datasource.setInitialSize(dataSourceCommonProperties.getInitialSize());
        datasource.setMinIdle(dataSourceCommonProperties.getMinIdle());
        datasource.setMaxActive(dataSourceCommonProperties.getMaxActive());
        datasource.setMaxWait(dataSourceCommonProperties.getMaxWait());
        datasource.setTimeBetweenEvictionRunsMillis(dataSourceCommonProperties.getTimeBetweenEvictionRunsMillis());
        datasource.setMinEvictableIdleTimeMillis(dataSourceCommonProperties.getMinEvictableIdleTimeMillis());
        datasource.setValidationQuery(dataSourceCommonProperties.getValidationQuery());
        datasource.setTestWhileIdle(dataSourceCommonProperties.isTestWhileIdle());
        datasource.setTestOnBorrow(dataSourceCommonProperties.isTestOnBorrow());
        datasource.setTestOnReturn(dataSourceCommonProperties.isTestOnReturn());
        datasource.setPoolPreparedStatements(dataSourceCommonProperties.isPoolPreparedStatements());
        try {
            datasource.setFilters(dataSourceCommonProperties.getFilters());
        } catch (SQLException e) {
            log.error("Impala Druid configuration initialization filter error.", e);
        }
        return datasource;
    }
}
