package br.com.wise.stock_service.gateway.database;

import br.com.wise.stock_service.domain.Product;

import java.util.Optional;

public interface ProductGateway {
    Optional<Product> buscaProdutoPorId(Long produtoId);
}
