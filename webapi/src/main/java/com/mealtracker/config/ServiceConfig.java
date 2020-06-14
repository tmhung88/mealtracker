package com.mealtracker.config;

import com.mealtracker.services.pagination.PageableBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {
    @Bean
    public PageableBuilder pageableBuilder() {
        return new PageableBuilder();
    }
}
