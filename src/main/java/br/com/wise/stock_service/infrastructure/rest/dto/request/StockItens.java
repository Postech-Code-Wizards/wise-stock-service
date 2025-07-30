package br.com.wise.stock_service.infrastructure.rest.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StockDTO {
    private Long pedidoId;
    private Long produtoId;
    private Integer quantidade;
}
