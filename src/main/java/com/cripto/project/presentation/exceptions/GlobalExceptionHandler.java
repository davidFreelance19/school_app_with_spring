package com.cripto.project.presentation.exceptions;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;


import jakarta.persistence.EntityExistsException;
import jakarta.persistence.NoResultException;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String KEY = "error";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    private ResponseEntity<Map<String, String>> handleValidationErrors(
        MethodArgumentNotValidException ex
    ) {

        String error = ex.getBindingResult()
                .getFieldErrors()
                .stream().map(FieldError::getDefaultMessage)
                .findFirst().orElse(ex.getMessage());

        return new ResponseEntity<>(Map.of(KEY, error), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityExistsException.class)
    private final ResponseEntity<Map<String, String>> handleValidationEntityExist(
        EntityExistsException ex
    ) {
        return new ResponseEntity<>(Map.of(KEY, ex.getMessage()), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoResultException.class)
    private final ResponseEntity<Map<String, String>> handleValidationEntityNotFound(
        NoResultException ex
    ) {
        return new ResponseEntity<>(Map.of(KEY, ex.getMessage()), new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    private final ResponseEntity<Map<String, String>> handleValidationJsonSintax(
        HttpMessageNotReadableException ex
    ) {
        return new ResponseEntity<>(Map.of(KEY, GlobalErrorsMessage.JSON_SYNTAX_ERROR), new HttpHeaders(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    private final ResponseEntity<Map<String, String>> handleConstraintViolationException(
        HandlerMethodValidationException ex
    ) {
        String message = ex.getAllErrors().stream().findFirst().get().getDefaultMessage();
        return new ResponseEntity<>(Map.of(KEY, message), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    private final ResponseEntity<Map<String, String>> handleTypeMismatchException(
        MethodArgumentTypeMismatchException ex
    ) {
        String errorMessage = String.format("The param '%s' should be a positive number", ex.getName());
        return new ResponseEntity<>(Map.of(KEY, errorMessage), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    private final ResponseEntity<Map<String, String>> handleMissingParameterException(
        MissingServletRequestParameterException ex
    ) {
        String errorMessage = "The query parameter " + ex.getParameterName() + " is required";
        return new ResponseEntity<>(Map.of(KEY, errorMessage), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    private final ResponseEntity<Map<String, String>> handleMethodNotSupported(
        HttpRequestMethodNotSupportedException ex
    ) {
        String errorMessage = "The requested action doesn't support the " + ex.getMethod() + " method";
        return new ResponseEntity<>(Map.of(KEY, errorMessage), HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    private final ResponseEntity<Map<String, String>> handleMediaTypeNotSupported(
        HttpMediaTypeNotSupportedException ex
    ) {
        String errorMessage = "Media type not supported: " + ex.getContentType();
        return new ResponseEntity<>(Map.of(KEY, errorMessage), HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        return new ResponseEntity<>(Map.of(KEY, ex.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, String>> handleBadCredentialsException(BadCredentialsException ex) {
        return new ResponseEntity<>(Map.of(KEY, ex.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, String>> handleDeniedAccessException(AccessDeniedException ex) {
        return new ResponseEntity<>(Map.of(KEY, ex.getMessage()), HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Map<String, String>> handleResourceNotFound(NoResourceFoundException ex) {
        return new ResponseEntity<>(Map.of(KEY, GlobalErrorsMessage.RESOURCE_NOT_FOUND), HttpStatus.NOT_FOUND);
    }

}
