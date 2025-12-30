package com.challenge.coupon.config.exception;

import com.challenge.coupon.domain.entity.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorResponse> domainException(DomainException ex) {

        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), List.of(ex.getMessage()));

        log.info("domainException: {}", ex.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(InvalidCodeException.class)
    public ResponseEntity<ErrorResponse> invalidCodeException(InvalidCodeException ex) {

        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), List.of(ex.getMessage()));

        log.info("invalidCodeException: {}", ex.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(InvalidDiscountValueException.class)
    public ResponseEntity<ErrorResponse> invalidDiscountValueException(InvalidDiscountValueException ex) {

        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), List.of(ex.getMessage()));

        log.info("invalidDiscountValueException: {}", ex.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(InvalidExpirationDateException.class)
    public ResponseEntity<ErrorResponse> invalidExpirationDateException(InvalidExpirationDateException ex) {

        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), List.of(ex.getMessage()));

        log.info("invalidExpirationDateException: {}", ex.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(DomainNotFoundException.class)
    public ResponseEntity<ErrorResponse> domainNotFoundException(DomainNotFoundException ex) {

        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), List.of(ex.getMessage()));

        log.info("domainNotFoundException: {}", ex.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(DuplicateCodeException.class)
    public ResponseEntity<ErrorResponse> duplicateCodeException(DuplicateCodeException ex) {

        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), List.of(ex.getMessage()));

        log.info("duplicateCodeException: {}", ex.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(InvalidUuidException.class)
    public ResponseEntity<ErrorResponse> invalidUuidException(InvalidUuidException ex) {

        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), List.of(ex.getMessage()));

        log.info("invalidUuidException: {}", ex.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> methodArgumentNotValidException(MethodArgumentNotValidException ex) {

        List<String> errors = new ArrayList<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String errorMessage = error.getDefaultMessage();

            errors.add(errorMessage);
        });

        ErrorResponse errorsResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), errors);

        log.info("methodArgumentNotValidException: {}", errorsResponse.errors());
        return ResponseEntity.badRequest().body(errorsResponse);
    }
}