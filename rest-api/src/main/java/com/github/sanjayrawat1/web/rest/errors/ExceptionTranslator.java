package com.github.sanjayrawat1.web.rest.errors;

import com.github.sanjayrawat1.config.ApplicationConstants;
import com.github.sanjayrawat1.errors.BadRequestException;
import com.github.sanjayrawat1.errors.ResourceNotFoundException;
import com.github.sanjayrawat1.external.integration.errors.UpstreamException;
import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.NativeWebRequest;
import org.zalando.problem.DefaultProblem;
import org.zalando.problem.Problem;
import org.zalando.problem.ProblemBuilder;
import org.zalando.problem.StatusType;
import org.zalando.problem.ThrowableProblem;
import org.zalando.problem.spring.web.advice.ProblemHandling;
import org.zalando.problem.spring.web.advice.security.SecurityAdviceTrait;
import org.zalando.problem.violations.ConstraintViolationProblem;

/**
 * Controller advice to translate the server side exceptions to client-friendly json structures.
 * The error response follows RFC7807 - Problem Details for HTTP APIs (https://tools.ietf.org/html/rfc7807).
 *
 * @author sanjayrawat1
 */
@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class ExceptionTranslator implements ProblemHandling, SecurityAdviceTrait {

    private static final String FIELD_ERRORS_KEY = "fieldErrors";
    private static final String MESSAGE_KEY = "message";
    private static final String VIOLATIONS_KEY = "violations";
    private static final String TRACE_ID_KEY = "traceId";

    private final Environment environment;

    /**
     * Post-process the Problem payload to add the message key for the api consumer if needed.
     */
    @Override
    public ResponseEntity<Problem> process(@Nullable ResponseEntity<Problem> entity, NativeWebRequest request) {
        if (entity == null) {
            return null;
        }
        Problem problem = entity.getBody();
        if (!(problem instanceof ConstraintViolationProblem || problem instanceof DefaultProblem)) {
            return entity;
        }

        HttpServletRequest nativeRequest = request.getNativeRequest(HttpServletRequest.class);
        String requestUri = nativeRequest != null ? nativeRequest.getRequestURI() : "";
        ProblemBuilder builder = Problem
            .builder()
            .withStatus(problem.getStatus())
            .withTitle(problem.getTitle())
            .withDetail(problem.getDetail())
            .withInstance(URI.create(requestUri))
            .with(TRACE_ID_KEY, MDC.get(ApplicationConstants.SLEUTH_TRACE_ID_KEY));

        if (problem instanceof ConstraintViolationProblem) {
            builder.with(VIOLATIONS_KEY, ((ConstraintViolationProblem) problem).getViolations()).with(MESSAGE_KEY, ErrorConstants.ERR_VALIDATION);
        } else {
            builder.withCause(((DefaultProblem) problem).getCause()).withDetail(problem.getDetail()).withInstance(problem.getInstance());
            problem.getParameters().forEach(builder::with);
            if (!problem.getParameters().containsKey(MESSAGE_KEY) && problem.getStatus() != null) {
                builder.with(MESSAGE_KEY, "error.http." + problem.getStatus().getStatusCode());
            }
        }
        return new ResponseEntity<>(builder.build(), entity.getHeaders(), entity.getStatusCode());
    }

    @Override
    @ParametersAreNonnullByDefault
    public ProblemBuilder prepare(Throwable throwable, StatusType status, URI type) {
        List<String> activeProfiles = List.of(environment.getActiveProfiles());
        if (activeProfiles.contains(ApplicationConstants.SPRING_PROFILE_PRODUCTION)) {
            if (throwable instanceof HttpMessageConversionException) {
                return Problem
                    .builder()
                    .withType(type)
                    .withStatus(status)
                    .withTitle(status.getReasonPhrase())
                    .withDetail("Unable to convert http message")
                    .withCause(Optional.ofNullable(throwable.getCause()).filter(cause -> isCausalChainsEnabled()).map(this::toProblem).orElse(null));
            }
            if (throwable instanceof DataAccessException) {
                return Problem
                    .builder()
                    .withType(type)
                    .withStatus(status)
                    .withTitle(status.getReasonPhrase())
                    .withDetail("Failure during data access")
                    .withCause(Optional.ofNullable(throwable.getCause()).filter(cause -> isCausalChainsEnabled()).map(this::toProblem).orElse(null));
            }
        }
        if (throwable instanceof ThrowableProblem) {
            ThrowableProblem throwableProblem = (ThrowableProblem) throwable;
            return Problem
                .builder()
                .withType(type)
                .withStatus(status)
                .withTitle(StringUtils.hasText(throwableProblem.getTitle()) ? throwableProblem.getTitle() : status.getReasonPhrase())
                .withDetail(StringUtils.hasText(throwableProblem.getDetail()) ? throwableProblem.getDetail() : throwable.getMessage())
                .withCause(Optional.ofNullable(throwable.getCause()).filter(cause -> isCausalChainsEnabled()).map(this::toProblem).orElse(null));
        }
        return Problem
            .builder()
            .withType(type)
            .withStatus(status)
            .withTitle(status.getReasonPhrase())
            .withDetail(throwable.getMessage())
            .withCause(Optional.ofNullable(throwable.getCause()).filter(cause -> isCausalChainsEnabled()).map(this::toProblem).orElse(null));
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleResourceNotFoundException(ResourceNotFoundException exception, NativeWebRequest request) {
        return create(Objects.requireNonNull(exception.getStatus()), exception, request);
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleBadRequestException(BadRequestException exception, NativeWebRequest request) {
        return create(Objects.requireNonNull(exception.getStatus()), exception, request);
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleUpstreamException(UpstreamException exception, NativeWebRequest request) {
        return create(Objects.requireNonNull(exception.getStatus()), exception, request);
    }
}
