package br.com.wise.stock_service.application.usecase.processor;

import br.com.wise.stock_service.domain.Product;
import br.com.wise.stock_service.domain.Stock;
import br.com.wise.stock_service.gateway.database.StockGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReporEstoqueUseCase {

    private final StockGateway stockGateway;
    private final BuscaEstoquePorIdProdutoUseCase buscaEstoquePorIdProdutoUseCase;

    public Stock execute(Stock stock) {
        var existingStockOp = buscaEstoquePorIdProdutoUseCase.execute(stock.getProduto().getProdutoId());

        if (existingStockOp.isPresent()) {
            var stockExistente = existingStockOp.get();
            stockExistente.reporQuantidade(stock.getQuantidade());
            return stockGateway.salvar(stockExistente);
        }

        return stockGateway.salvar(new Stock(new Product(stock.getProduto().getProdutoId()), stock.getQuantidade()));
    }
}
