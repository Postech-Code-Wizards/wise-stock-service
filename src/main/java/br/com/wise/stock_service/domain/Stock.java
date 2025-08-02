package br.com.wise.stock_service.domain;

import br.com.wise.stock_service.infrastructure.rest.exception.StockNegativeException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.ZonedDateTime;

@Getter
@AllArgsConstructor
public class Stock {
    private Long id;
    private Product produto;
    private Integer quantidade;
    private ZonedDateTime atualizado;
    private ZonedDateTime criado;

    public void reporQuantidade(Integer qtdRequest) {
        this.atualizado = ZonedDateTime.now();
        this.quantidade = this.quantidade + qtdRequest;
    }

    public void baixarQuantidade(Integer qtdRequest) {
        int novaQtd = this.quantidade - qtdRequest;

        if (novaQtd < 0) throw new StockNegativeException("");

        this.quantidade = novaQtd;
    }

    public Stock(Product product, Integer quantidade) {
        this.produto = product;
        this.quantidade = quantidade;
        this.atualizado = ZonedDateTime.now();
        this.criado = ZonedDateTime.now();
    }

}
