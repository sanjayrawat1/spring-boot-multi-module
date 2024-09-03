package com.github.sanjayrawat1.security;

import org.springframework.security.core.AuthenticationException;

/**
 * This exception is thrown in case of an invalid client trying to authenticate.
 *
 * @author sanjayrawat1
 */
public class ClientNotFoundException extends AuthenticationException {

    private static final long serialVersionUID = -5778992824215126195L;

    public ClientNotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public ClientNotFoundException(String msg) {
        super(msg);
    }
}
