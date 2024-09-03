package com.github.sanjayrawat1.web.rest.errors;

import java.io.Serializable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * View model for sending a field error message.
 *
 * @author sanjayrawat1
 */
@Getter
@RequiredArgsConstructor
public class FieldErrorVM implements Serializable {

    private static final long serialVersionUID = 4891277239988784698L;

    private final String objectName;

    private final String field;

    private final String message;
}
