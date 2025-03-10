package com.elgupo.elguposerver.authentication.exceptions;

import org.springframework.security.authentication.BadCredentialsException;

public class NotMatchingPasswordsException extends BadCredentialsException {
    public NotMatchingPasswordsException() {
        super("Passwords do not match");
    }
}
