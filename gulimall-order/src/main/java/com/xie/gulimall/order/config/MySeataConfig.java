package com.xie.gulimall.order.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;

/**
 * @title: MySeataConfig
 * @Author Xie
 * @Date: 2023/2/4 22:07
 * @Version 1.0
 */
/**
 * seata分布式事务
 * 配置代理数据源
 */
//@Configuration
public class MySeataConfig {

    /*@Autowired
    DataSourceProperties dataSourceProperties;

    *//**
     * 自动配置类，如果容器中存在数据源就不自动配置数据源了
     *//*
    @Bean
    public DataSource dataSource(DataSourceProperties dataSourceProperties) {
        HikariDataSource dataSource = dataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
        if (StringUtils.hasText(dataSourceProperties.getName())) {
            dataSource.setPoolName(dataSourceProperties.getName());
        }
        return new DataSourceProxy(dataSource);
    }*/
}
