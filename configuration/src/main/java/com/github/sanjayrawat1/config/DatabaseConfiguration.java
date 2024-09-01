package com.github.sanjayrawat1.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Database Configuration.
 *
 * @author Sanjay Singh Rawat
 */
@Configuration
@EnableJpaRepositories("com.github.sanjayrawat1.persistence.repository")
@EnableTransactionManagement
public class DatabaseConfiguration {}
