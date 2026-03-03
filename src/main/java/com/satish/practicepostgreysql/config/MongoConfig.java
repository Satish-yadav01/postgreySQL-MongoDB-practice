package com.satish.practicepostgreysql.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * MongoDB Configuration class.
 * Enables MongoDB repositories and auditing.
 */
@Configuration
@EnableMongoRepositories(basePackages = "com.satish.practicepostgreysql.repository")
@EnableMongoAuditing
public class MongoConfig {
    // Additional MongoDB configuration can be added here if needed
}
