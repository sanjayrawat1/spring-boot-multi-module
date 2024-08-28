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

    private final ApiDocs apiDocs = new ApiDocs();

    @Getter
    @Setter
    public static class ApiDocs {

        private String title = ApplicationDefaults.ApiDocs.title;

        private String description = ApplicationDefaults.ApiDocs.description;

        private String version = ApplicationDefaults.ApiDocs.version;

        private String termsOfServiceUrl = ApplicationDefaults.ApiDocs.termsOfServiceUrl;

        private String contactName = ApplicationDefaults.ApiDocs.contactName;

        private String contactUrl = ApplicationDefaults.ApiDocs.contactUrl;

        private String contactEmail = ApplicationDefaults.ApiDocs.contactEmail;

        private String license = ApplicationDefaults.ApiDocs.license;

        private String defaultIncludePattern = ApplicationDefaults.ApiDocs.defaultIncludePattern;

        private String host = ApplicationDefaults.ApiDocs.host;

        private String[] protocols = ApplicationDefaults.ApiDocs.protocols;

        private boolean useDefaultResponseMessage = ApplicationDefaults.ApiDocs.useDefaultResponseMessage;
    }
}
