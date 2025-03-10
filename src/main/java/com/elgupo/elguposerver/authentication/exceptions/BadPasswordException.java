package com.elgupo.elguposerver.authentication.exceptions;

import org.springframework.security.authentication.BadCredentialsException;

public class BadPasswordException extends BadCredentialsException {
    public BadPasswordException() {
        super("Invalid password");
    }
}
