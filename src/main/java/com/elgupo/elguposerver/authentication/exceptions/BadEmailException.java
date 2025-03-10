package com.elgupo.elguposerver.authentication.exceptions;

import org.springframework.security.authentication.BadCredentialsException;

public class BadEmailException extends BadCredentialsException {
    public BadEmailException() {
        super("Invalid email address");
    }
}
