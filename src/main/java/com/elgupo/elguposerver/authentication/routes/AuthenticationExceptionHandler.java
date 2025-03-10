package com.elgupo.elguposerver.authentication.routes;

import com.elgupo.elguposerver.authentication.exceptions.BadEmailException;
import com.elgupo.elguposerver.authentication.exceptions.BadPasswordException;
import com.elgupo.elguposerver.authentication.exceptions.EmailAlreadyInUseException;
import com.elgupo.elguposerver.authentication.exceptions.NotMatchingPasswordsException;
import com.elgupo.elguposerver.authentication.models.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AuthenticationExceptionHandler {

    @ExceptionHandler(BadEmailException.class)
    public ResponseEntity<ErrorMessage> badEmailException(BadEmailException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorMessage(exception.getMessage()));
    }

    @ExceptionHandler(BadPasswordException.class)
    public ResponseEntity<ErrorMessage> badPasswordException(BadPasswordException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorMessage(exception.getMessage()));
    }

    @ExceptionHandler(EmailAlreadyInUseException.class)
    public ResponseEntity<ErrorMessage> emailAlreadyInUseException(EmailAlreadyInUseException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorMessage(exception.getMessage()));
    }

    @ExceptionHandler(NotMatchingPasswordsException.class)
    public ResponseEntity<ErrorMessage> notMatchingPasswordsException(NotMatchingPasswordsException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorMessage(exception.getMessage()));
    }

}
