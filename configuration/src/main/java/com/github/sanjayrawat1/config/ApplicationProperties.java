package com.github.sanjayrawat1.config;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
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

    private final Security security = new Security();

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

    @Getter
    public static class Security {

        private final Authentication authentication = new Authentication();

        @Getter
        @Setter
        public static class Authentication {

            private Map<String, Jwt> clients = new HashMap<>();

            @Getter
            @Setter
            public static class Jwt {

                private String secret = ApplicationDefaults.Security.Authentication.Jwt.secret;

                private String base64Secret = ApplicationDefaults.Security.Authentication.Jwt.base64Secret;

                private long tokenValidityInSeconds = ApplicationDefaults.Security.Authentication.Jwt.tokenValidityInSeconds;

                private Set<String> roles = new HashSet<>();
            }
        }
    }
}
