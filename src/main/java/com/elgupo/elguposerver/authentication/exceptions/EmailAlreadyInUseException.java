package com.elgupo.elguposerver.authentication.exceptions;

import org.springframework.security.authentication.BadCredentialsException;

public class EmailAlreadyInUseException extends BadCredentialsException {
    public EmailAlreadyInUseException() {
        super("This email address is already in use");
    }
}
