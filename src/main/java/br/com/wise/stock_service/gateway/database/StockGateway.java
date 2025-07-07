package br.com.wise.stock_service.gateway.database;


import br.com.wise.stock_service.domain.Stock;

import java.util.Optional;

public interface StockGateway {
    Optional<Stock> buscaStockPorIdProduto(Long produtoId);

    Stock salvar(Stock stock);
}
