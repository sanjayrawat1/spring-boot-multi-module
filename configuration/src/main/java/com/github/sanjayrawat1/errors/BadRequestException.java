package com.github.sanjayrawat1.errors;

import java.net.URI;
import lombok.Getter;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

/**
 * Bad request exception.
 *
 * @author sanjayrawat1
 */
@Getter
public class BadRequestException extends AbstractThrowableProblem {

    private static final long serialVersionUID = -2666905042150582608L;
    private static final String TITLE = "Bad Request";
    private static final URI TYPE = null;

    public BadRequestException() {
        super(TYPE, TITLE, Status.BAD_REQUEST);
    }

    public BadRequestException(String detail) {
        super(TYPE, TITLE, Status.BAD_REQUEST, detail);
    }
}
