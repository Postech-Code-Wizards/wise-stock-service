package br.com.wise.stock_service.infrastructure.rest.handlers;

import br.com.wise.stock_service.infrastructure.rest.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Locale;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ControllerExceptionHandlerTest {

    private ControllerExceptionHandler handler;
    private MessageSource messageSource;

    @BeforeEach
    void setup() {
        messageSource = mock(MessageSource.class);
        handler = new ControllerExceptionHandler(messageSource);
    }

    @Test
    void shouldHandleMethodArgumentNotValidException() {
        FieldError fieldError = new FieldError("objectName", "field", "must not be null");
        BindException bindException = new BindException(new Object(), "objectName");
        bindException.addError(fieldError);

        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindException.getBindingResult());

        ResponseEntity<ErrorResponse> response = handler.handleMethodArgumentNotValidException(ex, Locale.ENGLISH);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void shouldHandleBusinessException() {
        BusinessException exception = new BusinessException("TEST_CODE", HttpStatus.CONFLICT, "Algo deu errado");

        when(messageSource.getMessage(eq("TEST_CODE"), any(), any())).thenReturn("Mensagem amigável");

        ResponseEntity<ErrorResponse> response = handler.handleBusinessException(exception, Locale.ENGLISH);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void shouldHandleIllegalArgumentException() {
        IllegalArgumentException ex = new IllegalArgumentException("Parâmetro inválido");

        ResponseEntity<String> response = handler.handleIllegalArgumentException(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo("Parâmetro inválido");
    }

    @Test
    void shouldReturnNoMessageAvailableWhenMessageNotFound() {
        when(messageSource.getMessage(eq("UNKNOWN_CODE"), any(), any(Locale.class)))
                .thenThrow(new NoSuchMessageException("UNKNOWN_CODE"));

        ErrorResponse.ApiError error = handler.toApiError("UNKNOWN_CODE", Locale.ENGLISH);

        assertThat(error).isNotNull();
    }
}
