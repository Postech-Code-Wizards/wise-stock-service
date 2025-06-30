package br.com.wise.gateway.database;

import br.com.wise.domain.Stock;

import java.util.Optional;

public interface StockGateway {
    Optional<Stock> buscaStockPorIdProduto(Long produtoId);

    Stock salvar(Stock stock);
}
