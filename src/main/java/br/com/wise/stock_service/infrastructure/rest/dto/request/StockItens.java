package br.com.wise.stock_service.infrastructure.rest.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StockItens {
    private Long pedidoId;
    private Long produtoId;
    private Integer quantidade;

}
