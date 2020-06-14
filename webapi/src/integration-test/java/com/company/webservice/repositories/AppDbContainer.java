package com.company.webservice.repositories;

import lombok.extern.slf4j.Slf4j;
import org.testcontainers.containers.MySQLContainer;

@Slf4j
public class AppDbContainer extends MySQLContainer<AppDbContainer> {
    private static final String IMAGE_VERSION = "mysql:5.7";
    private static final String JDBC_URL_TEMPLATE = "%s?useSSL=false&useTimezone=true&serverTimezone=UTC";
    /**
     * A directory in the test classpath
     */
    private static final String MYSQL_CONF_DIRECTORY = "testcontainers-mysql";
    private static AppDbContainer container;

    static class TestDbConfig {
        static String DATABASE_NAME = "test";
        static String MYSQL_USERNAME = "test";
        static String MYSQL_PASSWORD = "test";
        static String MYSQL_ROOT_USER = "root";
        static String MYSQL_ROOT_PASSWORD = "root";
    }

    private AppDbContainer() {
        super(IMAGE_VERSION);
    }

    public static AppDbContainer getInstance() {
        if (container == null) {
            container = new AppDbContainer();
        }
        return container;
    }

    @Override
    protected void configure() {
        optionallyMapResourceParameterAsVolume("TC_MY_CNF", "/etc/mysql/conf.d", MYSQL_CONF_DIRECTORY);
        addExposedPort(MYSQL_PORT);
        addEnv("MYSQL_DATABASE", TestDbConfig.DATABASE_NAME);
        addEnv("MYSQL_USER", TestDbConfig.MYSQL_USERNAME);
        addEnv("MYSQL_PASSWORD", TestDbConfig.MYSQL_PASSWORD);
        addEnv("MYSQL_ROOT_PASSWORD", TestDbConfig.DATABASE_NAME);
        addEnv("MYSQL_ROOT_PASSWORD", TestDbConfig.MYSQL_ROOT_PASSWORD);
        setStartupAttempts(3);
    }

    @Override
    public void start() {
        super.start();
        var jdbcUrl = String.format(JDBC_URL_TEMPLATE, container.getJdbcUrl());
        System.setProperty("DB_URL", jdbcUrl);
        System.setProperty("DB_USERNAME", TestDbConfig.MYSQL_ROOT_USER);
        System.setProperty("DB_PASSWORD", TestDbConfig.MYSQL_ROOT_PASSWORD);
        log.info("DB_USERNAME: {} | DB_PASSWORD: {}", TestDbConfig.MYSQL_ROOT_USER, TestDbConfig.MYSQL_ROOT_PASSWORD);
        log.info("Jdbc Url: {}", jdbcUrl);
    }

    @Override
    public void stop() {
        //do nothing, JVM handles shut down
    }
}
