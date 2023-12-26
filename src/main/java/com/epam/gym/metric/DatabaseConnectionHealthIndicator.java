package com.epam.gym.metric;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Component
@PropertySource("classpath:application.yaml")
public class DatabaseConnectionHealthIndicator implements HealthIndicator {

    @Value("${spring.datasource.url}")
    private String db_url;

    @Value("${spring.datasource.username}")
    private String db_username;

    @Value("${spring.datasource.password}")
    private String db_password;


    private static Logger LOGGER = LoggerFactory.getLogger(DatabaseConnectionHealthIndicator.class);

    @Override
    public Health health() {

        if (isDatabaseConnectionUp()) {
            return Health.up().withDetail("message", "Database connection is OK").build();
        }
        return Health.down().withDetail("message", "Unable to connect to the database").build();
    }

    private boolean isDatabaseConnectionUp() {
        try {
            Connection connection = DriverManager.getConnection(db_url, db_username, db_password);

            if (isValidConnection(connection)) {
                connection.close();
                return true;
            }
        } catch (SQLException e) {
            LOGGER.error(e.getLocalizedMessage());
        }
        return false;
    }

    private boolean isValidConnection(Connection connection) {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
}

