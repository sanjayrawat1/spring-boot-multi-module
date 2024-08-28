package com.github.sanjayrawat1.config;

import static io.swagger.models.auth.In.HEADER;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.util.StopWatch;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.ParameterType;
import springfox.documentation.service.RequestParameter;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Springfox OpenAPI configuration.
 *
 * @author Sanjay Singh Rawat
 */
@Slf4j
@Configuration
@Profile(ApplicationConstants.SPRING_PROFILE_API_DOCS)
public class OpenApiConfiguration {

    static final String STARTING_MESSAGE = "Starting OpenAPI docs";
    static final String STARTED_MESSAGE = "Started OpenAPI docs in {} ms";

    @Bean
    public Docket openApiDocket(ApplicationProperties applicationProperties) {
        log.debug(STARTING_MESSAGE);
        StopWatch watch = new StopWatch();
        watch.start();

        var apiDocs = applicationProperties.getApiDocs();

        Contact contact = new Contact(apiDocs.getContactName(), apiDocs.getContactUrl(), apiDocs.getContactEmail());

        ApiInfo apiInfo = new ApiInfoBuilder()
            .title(apiDocs.getTitle())
            .description(apiDocs.getDescription())
            .version(apiDocs.getVersion())
            .termsOfServiceUrl(apiDocs.getTermsOfServiceUrl())
            .contact(contact)
            .license(apiDocs.getLicense())
            .licenseUrl(apiDocs.getContactUrl())
            .extensions(new ArrayList<>())
            .build();

        Docket docket = new Docket(DocumentationType.OAS_30)
            .groupName("spring-boot-multi-module-openapi")
            .host(apiDocs.getHost())
            .protocols(Set.of(apiDocs.getProtocols()))
            .apiInfo(apiInfo)
            .securityContexts(Collections.singletonList(securityContext()))
            .securitySchemes(Collections.singletonList(apiKey()))
            .globalRequestParameters(globalParams())
            .useDefaultResponseMessages(apiDocs.isUseDefaultResponseMessage())
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.github.sanjayrawat1.web.rest"))
            .paths(PathSelectors.regex(apiDocs.getDefaultIncludePattern()))
            .build();

        watch.stop();
        log.debug(STARTED_MESSAGE, watch.getTotalTimeMillis());
        return docket;
    }

    private ApiKey apiKey() {
        return new ApiKey(AUTHORIZATION, AUTHORIZATION, HEADER.name());
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder().securityReferences(defaultAuth()).build();
    }

    private List<SecurityReference> defaultAuth() {
        var authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Collections.singletonList(new SecurityReference(AUTHORIZATION, authorizationScopes));
    }

    private List<RequestParameter> globalParams() {
        var clientId = new RequestParameterBuilder().name("Client-Id").in(ParameterType.HEADER).required(true).description("Client Id").build();
        return List.of(clientId);
    }
}
