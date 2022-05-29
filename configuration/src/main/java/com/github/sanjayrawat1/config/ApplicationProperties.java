package com.github.sanjayrawat1.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to spring-boot-multi-module Application.
 * Properties are configured in the {@code application.yml} file.
 *
 * @author Sanjay Singh Rawat
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    private String name = ApplicationDefaults.name;

    private String clientId = ApplicationDefaults.clientId;
}
