package com.mealtracker.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableAutoConfiguration
@EntityScan(basePackages = {"com.mealtracker.domains"}, basePackageClasses = { Jsr310JpaConverters.class })
@EnableJpaRepositories(basePackages = {"com.mealtracker.repositories"})
@EnableTransactionManagement
public class JpaConfig {
}

