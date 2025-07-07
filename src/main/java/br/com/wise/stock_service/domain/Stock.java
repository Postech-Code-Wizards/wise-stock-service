package br.com.wise.stock_service.domain;

import lombok.*;

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
