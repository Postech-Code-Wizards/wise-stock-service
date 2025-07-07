package br.com.wise.stock_service.infrastructure.rest.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;

@RestControllerAdvice
public class GeneralExceptionHandler {

    private final ControllerExceptionHandler exceptionHandler;

    public GeneralExceptionHandler(ControllerExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handlerInternalServerError(Exception exception, Locale locale) {
        final String errorCode = "INTERNAL_SERVER_ERROR";
        final HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        final ErrorResponse errorResponse = ErrorResponse.of(status, exceptionHandler.toApiError(errorCode, locale));
        return ResponseEntity.status(status).body(errorResponse);
    }
}
