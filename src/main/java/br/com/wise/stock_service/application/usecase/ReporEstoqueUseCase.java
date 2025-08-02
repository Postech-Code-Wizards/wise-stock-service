package br.com.wise.stock_service.application.usecase;

import br.com.wise.stock_service.domain.Product;
import br.com.wise.stock_service.domain.Stock;
import br.com.wise.stock_service.gateway.database.StockGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReporEstoqueUseCase {

    private final StockGateway stockGateway;

    public Stock execute(Stock stock) {
        var existingStockOp = stockGateway.buscaStockPorIdProduto(stock.getProduto().getProdutoId());

        if (existingStockOp.isPresent()) {
            var stockExistente = existingStockOp.get();
            stockExistente.reporQuantidade(stock.getQuantidade());
            return stockGateway.salvar(stockExistente);
        }

        return stockGateway.salvar(new Stock(new Product(stock.getProduto().getProdutoId()), stock.getQuantidade()));
    }
}
