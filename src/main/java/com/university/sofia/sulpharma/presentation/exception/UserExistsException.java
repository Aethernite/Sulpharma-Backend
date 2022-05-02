package com.university.sofia.sulpharma.presentation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The type Token refresh exception.
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class UserExistsException extends RuntimeException {

    /**
     * Instantiates a new User exists exception.
     *
     * @param message the message
     */
    public UserExistsException(String message) {
        super(message);
    }
}
