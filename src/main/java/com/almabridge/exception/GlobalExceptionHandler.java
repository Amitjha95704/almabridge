package com.almabridge.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log =
            LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(
            ResourceNotFoundException ex) {

        return buildResponse(
                HttpStatus.NOT_FOUND,
                ex.getMessage()
        );
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicate(
            DuplicateResourceException ex) {

        return buildResponse(
                HttpStatus.CONFLICT,
                ex.getMessage()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(
            MethodArgumentNotValidException ex) {

        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": "
                        + error.getDefaultMessage())
                .findFirst()
                .orElse("Invalid request");

        return buildResponse(
                HttpStatus.BAD_REQUEST,
                message
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDatabaseConflict(
            DataIntegrityViolationException ex) {

        log.warn("Database constraint violation", ex);

        return buildResponse(
                HttpStatus.CONFLICT,
                "A record with the same unique value already exists"
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneral(
            Exception ex) {

        log.error("Unexpected application error", ex);

        return buildResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred"
        );
    }

    private ResponseEntity<Map<String, Object>> buildResponse(
            HttpStatus status,
            String message) {

        Map<String, Object> body = Map.of(
                "timestamp", LocalDateTime.now().toString(),
                "status", status.value(),
                "error", status.getReasonPhrase(),
                "message", message
        );

        return ResponseEntity.status(status).body(body);
    }
    

@ExceptionHandler(
    org.springframework.security.core.AuthenticationException.class
)
public ResponseEntity<Map<String, Object>> handleAuthenticationException(
        org.springframework.security.core.AuthenticationException ex) {

    return buildResponse(
            HttpStatus.UNAUTHORIZED,
            "Invalid email or password"
    );
}

    

@ExceptionHandler(IllegalArgumentException.class)
public ResponseEntity<Map<String, Object>> handleIllegalArgument(
        IllegalArgumentException ex) {

    return buildResponse(
            HttpStatus.BAD_REQUEST,
            ex.getMessage()
    );
}


    @ExceptionHandler(
    	    org.springframework.web.servlet.resource.NoResourceFoundException.class
    	)
    	public ResponseEntity<Map<String, Object>> handleNoResource(
    	        org.springframework.web.servlet.resource.NoResourceFoundException ex) {

    	    return buildResponse(
    	        HttpStatus.NOT_FOUND,
    	        "API endpoint not found"
    	    );
    	}
    
    @ExceptionHandler(
    	    org.springframework.security.authorization.AuthorizationDeniedException.class
    	)
    	public ResponseEntity<Map<String, Object>> handleAuthorizationDenied(
    	        org.springframework.security.authorization.AuthorizationDeniedException ex) {

    	    return buildResponse(
    	            HttpStatus.FORBIDDEN,
    	            "You do not have permission to perform this action"
    	    );
    	}
}