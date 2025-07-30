package br.com.wise.stock_service.application.usecase;

import br.com.wise.stock_service.domain.Stock;
import br.com.wise.stock_service.gateway.database.StockGateway;
import br.com.wise.stock_service.infrastructure.rest.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BuscaEstoquePorIdProdutoUseCase {

    private final StockGateway stockGateway;

    public Stock execute(Long produtoId) {
        return stockGateway.buscaStockPorIdProduto(produtoId)
                .orElseThrow(() -> new ResourceNotFoundException("Estoque do produto " + produtoId));
    }
}
