package br.com.wise.stock_service.infrastructure.rest.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockResponse {
    private Long id;
    private Long produtoId;
    private Integer quantidade;
    private ZonedDateTime atualizado;
    private ZonedDateTime criado;
}
