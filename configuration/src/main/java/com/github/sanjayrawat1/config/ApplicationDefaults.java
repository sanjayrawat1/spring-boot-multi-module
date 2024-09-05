package com.github.sanjayrawat1.config;

/**
 * Application defaults.
 *
 * @author sanjayrawat1
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

    interface Security {
        interface Authentication {
            interface Jwt {
                String secret = null;
                String base64Secret = null;
                long tokenValidityInSeconds = 1800;
            }
        }
    }

    interface Kafka {
        interface Topic {
            interface BaseTopic {
                boolean enabled = false;
                String topicName = "spring-boot-multi-module-topic";
                int partitions = 1;
                short replicas = 1;
            }

            interface Test extends BaseTopic {}

            interface OrderStatus extends BaseTopic {}
        }

        interface Consumer {
            interface ErrorHandler {
                interface ExponentialBackOff {
                    long initialInterval = 60_000L;
                    double multiplier = 2.5D;
                    long maxInterval = 270_000L;
                    long maxElapsedTime = 937_500L;
                }
            }
        }
    }
}
