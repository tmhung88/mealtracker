package com.company.webservice.config;

import com.company.webservice.services.pagination.PageableBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {
    @Bean
    public PageableBuilder pageableBuilder() {
        return new PageableBuilder();
    }
}
