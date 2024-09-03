package com.github.sanjayrawat1.errors;

import java.net.URI;
import lombok.Getter;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

/**
 * Resource not found exception.
 *
 * @author sanjayrawat1
 */
@Getter
public class ResourceNotFoundException extends AbstractThrowableProblem {

    private static final long serialVersionUID = -8252155714282806480L;
    private static final String TITLE = "Resource Not Fount";
    private static final URI TYPE = null;

    public ResourceNotFoundException() {
        super(TYPE, TITLE, Status.NOT_FOUND);
    }

    public ResourceNotFoundException(String detail) {
        super(TYPE, TITLE, Status.NOT_FOUND, detail);
    }

    public ResourceNotFoundException(Long resourceId) {
        super(TYPE, TITLE, Status.NOT_FOUND, String.format("Resource '%s' not found", resourceId));
    }
}
