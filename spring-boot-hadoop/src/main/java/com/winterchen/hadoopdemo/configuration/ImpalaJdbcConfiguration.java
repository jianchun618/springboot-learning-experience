package com.winterchen.hadoopdemo.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class ImpalaJdbcConfiguration {

    @Bean("impalaJdbcTemplate")
    @Qualifier("impalaJdbcTemplate")
    public JdbcTemplate jdbcTemplate(@Qualifier("impalaDruidDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
