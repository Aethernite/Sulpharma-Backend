package com.university.sofia.sulpharma.presentation.exception;

/**
 * The Invalid role exception.
 * <p>
 * This exception is thrown when invalid role is supplied by the request body
 */
public class InvalidRoleException extends RuntimeException {
    /**
     * Instantiates a new Invalid role exception.
     *
     * @param message the message
     */
    public InvalidRoleException(String message) {
        super(message);
    }
}
