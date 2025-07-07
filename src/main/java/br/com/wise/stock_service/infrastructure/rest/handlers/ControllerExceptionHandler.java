package br.com.wise.stock_service.infrastructure.rest.handlers;

import br.com.wise.stock_service.infrastructure.rest.exception.BusinessException;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Locale;

@RestControllerAdvice
public class ControllerExceptionHandler {

    private static final String NO_MESSAGE_AVAILABLE = "No message available";

    private final MessageSource apiErrorMessageSource;

    public ControllerExceptionHandler(MessageSource apiErrorMessageSource) {
        this.apiErrorMessageSource = apiErrorMessageSource;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, Locale locale) {
        List<ErrorResponse.ApiError> apiErrors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> {
                    var defaultMessage = error.getDefaultMessage();
                    return new ErrorResponse.ApiError("VALIDATION_ERROR", defaultMessage);
                })
                .toList();

        return ResponseEntity
                .badRequest()
                .body(ErrorResponse.of(HttpStatus.BAD_REQUEST, apiErrors));
    }


    @ExceptionHandler({BusinessException.class})
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex, Locale locale) {
        final String errorCode = ex.getCode();
        final HttpStatus status = ex.getStatus();
        final String message = ex.getMessage();

        String defaultMessage = apiErrorMessageSource.getMessage(errorCode, null, locale);

        String finalMessage = (message != null && !message.isEmpty() ? message + " " : "") + defaultMessage;

        final ErrorResponse errorResponse = ErrorResponse.of(status, new ErrorResponse.ApiError(errorCode, finalMessage));
        return ResponseEntity.status(status).body(errorResponse);

    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

    public ErrorResponse.ApiError toApiError(String errorCode, Locale locale, Object... args) {
        String message;
        try {
            message = apiErrorMessageSource.getMessage(errorCode, args, locale);
        } catch (NoSuchMessageException e) {
            message = NO_MESSAGE_AVAILABLE;
        }
        return new ErrorResponse.ApiError(errorCode, message);
    }
}
