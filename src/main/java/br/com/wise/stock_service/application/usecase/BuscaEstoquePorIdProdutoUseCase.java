package br.com.wise.stock_service.application.usecase;

import br.com.wise.stock_service.domain.Stock;
import br.com.wise.stock_service.gateway.database.StockGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BuscaEstoquePorIdProdutoUseCase {

    private final StockGateway stockGateway;

    public Optional<Stock> execute(Long produtoId) {
        return stockGateway.buscaStockPorIdProduto(produtoId);
    }
}
