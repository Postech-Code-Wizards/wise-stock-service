package br.com.wise.stock_service.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Stock {
    private Long id;
    private Long produtoId;
    private Integer quantidade;
    private ZonedDateTime atualizado;
    private ZonedDateTime criado;
}
