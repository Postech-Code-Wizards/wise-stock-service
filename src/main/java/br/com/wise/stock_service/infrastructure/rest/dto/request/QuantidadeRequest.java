package br.com.wise.stock_service.infrastructure.rest.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QuantidadeRequest {
    private Integer quantidade;
}
