package com.university.sofia.sulpharma.presentation.handler;

import com.university.sofia.sulpharma.presentation.exception.InvalidRoleException;
import com.university.sofia.sulpharma.presentation.exception.TokenRefreshException;
import com.university.sofia.sulpharma.presentation.exception.UserExistsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityNotFoundException;
import java.util.Map;

/**
 * The Http exception handlers class.
 * <p>
 * This class contains all of the needed {@link ExceptionHandler}
 */
@Slf4j
@ControllerAdvice
public class HttpExceptionHandler {

    /**
     * Entity not found exception response entity.
     *
     * @param e the exception
     * @return the response entity
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, String>> entityNotFoundException(EntityNotFoundException e) {
        log.info(e.getMessage());
        return new ResponseEntity<>(Map.of("message", e.getMessage()), HttpStatus.NOT_FOUND);
    }

    /**
     * User already exists exception response entity.
     *
     * @param e the exception
     * @return the response entity
     */
    @ExceptionHandler(UserExistsException.class)
    public ResponseEntity<Map<String, String>> userAlreadyExistsException(UserExistsException e) {
        log.info(e.getMessage());
        return new ResponseEntity<>(Map.of("message", e.getMessage()), HttpStatus.CONFLICT);
    }

    /**
     * Invalid role exception response entity.
     *
     * @param e the exception
     * @return the response entity
     */
    @ExceptionHandler(InvalidRoleException.class)
    public ResponseEntity<Map<String, String>> invalidRoleException(InvalidRoleException e) {
        log.info(e.getMessage());
        return new ResponseEntity<>(Map.of("message", e.getMessage()), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    /**
     * Invalid token refresh exception response entity.
     *
     * @param e the exception
     * @return the response entity
     */
    @ExceptionHandler(TokenRefreshException.class)
    public ResponseEntity<Map<String, String>> tokenRefreshException(TokenRefreshException e) {
        log.info(e.getMessage());
        return new ResponseEntity<>(Map.of("message", e.getMessage()), HttpStatus.FORBIDDEN);
    }

}
