package br.com.wise.stock_service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Product {
    private Long id;
    private Long produtoId;

    public Product(Long produtoId) {
        this.produtoId = produtoId;
    }
}
