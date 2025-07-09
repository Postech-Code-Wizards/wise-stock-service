package br.com.wise.stock_service.infrastructure.rest.exception;

import org.springframework.http.HttpStatus;

public class StockNegativeException extends BusinessException {
    public StockNegativeException(String message) {
        super("VALIDA_ESTOQUE", HttpStatus.UNPROCESSABLE_ENTITY, message);
    }
}
