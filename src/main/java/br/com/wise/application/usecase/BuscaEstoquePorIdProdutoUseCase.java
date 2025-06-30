package br.com.wise.application.usecase;

import br.com.wise.domain.Stock;
import br.com.wise.gateway.database.StockGateway;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@ApplicationScoped
@RequiredArgsConstructor
public class BuscaEstoquePorIdProdutoUseCase {

    private final StockGateway stockGateway;

    public Optional<Stock> execute(Long produtoId) {
        return stockGateway.buscaStockPorIdProduto(produtoId);
    }
}
