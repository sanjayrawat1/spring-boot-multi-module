package com.github.sanjayrawat1.config;

/**
 * Application Constants.
 *
 * @author Sanjay Singh Rawat
 */
public interface ApplicationConstants {
    String SPRING_PROFILE_DEVELOPMENT = "dev";

    String SPRING_PROFILE_TEST = "test";

    String SPRING_PROFILE_PRODUCTION = "prod";
    /**
     * Spring profile used to enable api-docs.
     */
    String SPRING_PROFILE_API_DOCS = "api-docs";
    /**
     * Spring profile used when deploying to Kubernetes.
     */
    String SPRING_PROFILE_K8S = "k8s";
}
