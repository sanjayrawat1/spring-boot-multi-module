package com.github.sanjayrawat1.config;

/**
 * Application defaults.
 *
 * @author Sanjay Singh Rawat
 */
public interface ApplicationDefaults {
    String name = "spring-boot-multi-module";
    String clientId = "spring-boot-multi-module";

    interface ApiDocs {
        String title = "Spring Boot Multi Module API";
        String description = "Spring Boot Multi Module API documentation";
        String version = "0.0.1";
        String termsOfServiceUrl = null;
        String contactName = null;
        String contactUrl = null;
        String contactEmail = null;
        String license = null;
        String defaultIncludePattern = "/spring-boot-multi-module/api/.*";
        String host = null;
        String[] protocols = {};
        boolean useDefaultResponseMessage = true;
    }
}
