package br.com.wise.stock_service.infrastructure.rest.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StockResponseMessage {
    private Long pedidoId;
    private Boolean status;
}
