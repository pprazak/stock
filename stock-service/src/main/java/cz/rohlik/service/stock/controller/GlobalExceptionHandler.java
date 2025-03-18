package cz.rohlik.service.stock.controller;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = Logger.getLogger(GlobalExceptionHandler.class.getName());

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException ex) {
        logger.warning(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Entity not found", ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        logger.warning(ex.getMessage());
        return ResponseEntity.badRequest().body(new ErrorResponse("Invalid request", ex.getMessage()));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalStateException(IllegalStateException ex) {
        logger.warning(ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse("Illegal state", ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        logger.warning(ex.getMessage());
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.badRequest().body(new ErrorResponse("Validation error", errors));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        logger.severe(ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Unexpected error", ex.getMessage()));
    }

    public static class ErrorResponse {

        private final String error;
        private final Map<String, String> details;
        private final String description;

        public ErrorResponse(String error) {
            this.error = error;
            this.details = null;
            this.description = null;
        }

        public ErrorResponse(String error, String description) {
            this.error = error;
            this.details = null;
            this.description = description;
        }

        public ErrorResponse(String error, Map<String, String> details) {
            this.error = error;
            this.details = details;
            this.description = null;
        }

        public String getError() {
            return error;
        }

        public Map<String, String> getDetails() {
            return details;
        }

        public String getDescription() {
            return description;
        }
    }
}
