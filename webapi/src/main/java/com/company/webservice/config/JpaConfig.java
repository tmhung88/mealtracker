package com.company.webservice.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableAutoConfiguration
@EntityScan(basePackages = {"com.company.webservice.domains"})
@EnableJpaRepositories(basePackages = {"com.company.webservice.repositories"})
@EnableTransactionManagement
public class JpaConfig {
}

