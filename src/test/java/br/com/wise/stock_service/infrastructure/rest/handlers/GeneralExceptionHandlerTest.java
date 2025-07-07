package br.com.wise.stock_service.infrastructure.rest.handlers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Locale;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GeneralExceptionHandlerTest {

    private ControllerExceptionHandler controllerExceptionHandler;
    private GeneralExceptionHandler generalHandler;

    @BeforeEach
    void setUp() {
        controllerExceptionHandler = mock(ControllerExceptionHandler.class);
        generalHandler = new GeneralExceptionHandler(controllerExceptionHandler);
    }

    @Test
    void shouldHandleGenericException() {
        Exception ex = new RuntimeException("Falha inesperada");
        ErrorResponse.ApiError apiError = new ErrorResponse.ApiError("INTERNAL_SERVER_ERROR", "Erro interno");
        ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, apiError);

        when(controllerExceptionHandler.toApiError("INTERNAL_SERVER_ERROR", Locale.ENGLISH))
                .thenReturn(apiError);

        ResponseEntity<ErrorResponse> response = generalHandler.handlerInternalServerError(ex, Locale.ENGLISH);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isEqualTo(errorResponse);
    }
}
