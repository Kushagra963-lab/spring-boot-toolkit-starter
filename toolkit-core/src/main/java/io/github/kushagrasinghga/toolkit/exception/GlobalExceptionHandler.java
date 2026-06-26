package io.github.kushagrasinghga.toolkit.exception;

import io.github.kushagrasinghga.toolkit.dto.ApiError;
import io.github.kushagrasinghga.toolkit.dto.ErrorCode;
import io.github.kushagrasinghga.toolkit.logging.CorrelationId;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiError> handleValidationException(ValidationException exception, HttpServletRequest request) {
        return error(HttpStatus.BAD_REQUEST, exception.getErrorCode(), exception.getMessage(), List.of(), request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
                                                                 HttpServletRequest request) {
        List<String> details = exception.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();
        return error(HttpStatus.BAD_REQUEST, ErrorCode.VALIDATION_ERROR, "Validation failed", details, request);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgument(IllegalArgumentException exception, HttpServletRequest request) {
        return error(HttpStatus.BAD_REQUEST, ErrorCode.BAD_REQUEST, exception.getMessage(), List.of(), request);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiError> handleCustomException(CustomException exception, HttpServletRequest request) {
        HttpStatus status = exception.getErrorCode() == ErrorCode.RESOURCE_NOT_FOUND
                ? HttpStatus.NOT_FOUND
                : HttpStatus.BAD_REQUEST;
        return error(status, exception.getErrorCode(), exception.getMessage(), List.of(), request);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiError> handleRuntime(RuntimeException exception, HttpServletRequest request) {
        return error(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.INTERNAL_ERROR, "Unexpected server error", List.of(), request);
    }

    private ResponseEntity<ApiError> error(HttpStatus status,
                                           ErrorCode code,
                                           String message,
                                           List<String> details,
                                           HttpServletRequest request) {
        return ResponseEntity.status(status)
                .body(ApiError.of(code, message, details, request.getRequestURI(), CorrelationId.current()));
    }
}
