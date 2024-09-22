package com.example.BloggerApplication.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(ObjectNotFoundException ex) {

        log.error(ex.getMessage());

        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<String> handleUserExistsException(UserAlreadyExistsException ex) {

        log.error(ex.getMessage());

        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<String> handleExpiredTokenException(TokenExpiredException ex) {

        log.error(ex.getMessage());

        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

}
