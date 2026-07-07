package com.acharya.dikshanta.InventoryManagement.common.config;

import com.acharya.dikshanta.InventoryManagement.tenant.datasource.TenantAwareDataSource;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jdbc.autoconfigure.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class TenantDataSourceConfig {

    @Bean("rawDataSource")
    public DataSource rawDataSource(DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();
    }

    @Bean
    @Primary
    public DataSource dataSource(@Qualifier("rawDataSource") DataSource rawDataSource) {
        return new TenantAwareDataSource(rawDataSource);
    }
}
