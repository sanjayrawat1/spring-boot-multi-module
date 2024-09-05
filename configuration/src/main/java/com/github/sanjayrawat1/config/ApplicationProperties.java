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
 * @author sanjayrawat1
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    private String name = ApplicationDefaults.name;

    private String clientId = ApplicationDefaults.clientId;

    private final ApiDocs apiDocs = new ApiDocs();

    private final Security security = new Security();

    private final Kafka kafka = new Kafka();

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

    @Getter
    public static class Kafka {

        private final Topic topic = new Topic();

        private final Consumer consumer = new Consumer();

        @Getter
        public static class Topic {

            private final Test test = new Test();

            private final OrderStatus orderStatus = new OrderStatus();

            @Getter
            @Setter
            public static class BaseTopic {

                private boolean enabled = ApplicationDefaults.Kafka.Topic.BaseTopic.enabled;

                private String topicName = ApplicationDefaults.Kafka.Topic.BaseTopic.topicName;

                private int partitions = ApplicationDefaults.Kafka.Topic.BaseTopic.partitions;

                private short replicas = ApplicationDefaults.Kafka.Topic.BaseTopic.replicas;
            }

            @Getter
            @Setter
            public static class Test extends BaseTopic {}

            @Getter
            @Setter
            public static class OrderStatus extends BaseTopic {

                private String topicName = ApplicationDefaults.Kafka.Topic.OrderStatus.topicName;
            }
        }

        @Getter
        public static class Consumer {

            private final ErrorHandler errorHandler = new ErrorHandler();

            @Getter
            public static class ErrorHandler {

                private final ExponentialBackOff exponentialBackOff = new ExponentialBackOff();

                @Getter
                @Setter
                public static class ExponentialBackOff {

                    private long initialInterval = ApplicationDefaults.Kafka.Consumer.ErrorHandler.ExponentialBackOff.initialInterval;

                    private double multiplier = ApplicationDefaults.Kafka.Consumer.ErrorHandler.ExponentialBackOff.multiplier;

                    private long maxInterval = ApplicationDefaults.Kafka.Consumer.ErrorHandler.ExponentialBackOff.maxInterval;

                    private long maxElapsedTime = ApplicationDefaults.Kafka.Consumer.ErrorHandler.ExponentialBackOff.maxElapsedTime;
                }
            }
        }
    }
}
