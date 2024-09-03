package com.github.sanjayrawat1.external.integration.errors;

import java.net.URI;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

/**
 * Upstream exception.
 *
 * @author sanjayrawat1
 */
public class UpstreamException extends AbstractThrowableProblem {

    private static final long serialVersionUID = -3608843645240504340L;
    private static final String TITLE = "Failed Dependency";
    private static final URI TYPE = null;

    private String errorCode;

    public UpstreamException(String detail) {
        super(TYPE, TITLE, Status.FAILED_DEPENDENCY, detail);
    }

    public UpstreamException(Status status, String detail, String errorCode) {
        super(TYPE, TITLE, status, detail);
        this.errorCode = errorCode;
    }
}
