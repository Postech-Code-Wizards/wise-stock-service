package br.com.wise.stock_service.application.usecase;

import br.com.wise.stock_service.domain.Stock;
import br.com.wise.stock_service.gateway.database.StockGateway;
import br.com.wise.stock_service.infrastructure.rest.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BaixarEstoqueUseCase {

    private final StockGateway stockGateway;

    public Stock execute(Stock stockBaixa) {

        var existingStockOp = stockGateway.buscaStockPorIdProduto(stockBaixa.getProduto().getProdutoId());

        if (existingStockOp.isEmpty()) {
            log.error("Não foi encontrado estoque para o produto {}", stockBaixa.getProduto().getProdutoId());
            throw new ResourceNotFoundException("Produto com ID " + stockBaixa.getProduto().getProdutoId());
        }

        var stockExistente = existingStockOp.get();
        stockExistente.baixarQuantidade(stockBaixa.getQuantidade());
        
        return stockGateway.salvar(stockExistente);
    }

}
