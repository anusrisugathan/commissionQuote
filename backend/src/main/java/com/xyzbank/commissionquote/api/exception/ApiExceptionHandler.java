package com.xyzbank.commissionquote.api.exception;

import com.xyzbank.commissionquote.api.dto.ApiError;
import com.xyzbank.commissionquote.vendor.exception.InvalidVendorApiKeyException;
import com.xyzbank.commissionquote.vendor.exception.VendorUnavailableException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.http.converter.HttpMessageNotReadableException;

import java.util.stream.Collectors;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException exception) {
        String message = exception.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getDefaultMessage())
                .filter(fieldMessage -> fieldMessage != null && !fieldMessage.isBlank())
                .distinct()
                .collect(Collectors.joining(" "));
        if (message.isBlank()) {
            message = "Enter a positive loan amount and term, and a valid risk band.";
        }
        return ResponseEntity.badRequest().body(new ApiError(message));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleUnreadableRequest(HttpMessageNotReadableException exception) {
        return ResponseEntity.badRequest().body(new ApiError("Request body is invalid. Check the supplied values and risk band."));
    }

    @ExceptionHandler(InvalidVendorApiKeyException.class)
    public ResponseEntity<ApiError> handleInvalidApiKey(InvalidVendorApiKeyException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiError("Vendor API authentication failed."));
    }

    @ExceptionHandler(VendorUnavailableException.class)
    public ResponseEntity<ApiError> handleVendorUnavailable(VendorUnavailableException exception) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(new ApiError("Quote service is temporarily unavailable. Please try again."));
    }

    @ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity<ApiError> handleVendorConnection(ResourceAccessException exception) {
        return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT)
                .body(new ApiError("Quote service timed out or could not be reached. Please try again."));
    }

    @ExceptionHandler(RestClientResponseException.class)
    public ResponseEntity<ApiError> handleVendorResponse(RestClientResponseException exception) {
        if (exception.getStatusCode().is5xxServerError()) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(new ApiError("Quote service is temporarily unavailable. Please try again."));
        }
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                .body(new ApiError("Quote service returned an unexpected response."));
    }
}
