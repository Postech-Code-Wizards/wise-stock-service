package br.com.wise.stock_service.application.usecase.processor;

import br.com.wise.stock_service.application.usecase.BaixarEstoqueUseCase;
import br.com.wise.stock_service.domain.Stock;
import br.com.wise.stock_service.gateway.database.StockGateway;
import br.com.wise.stock_service.infrastructure.rest.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BaixarEstoqueIndividualUseCase implements BaixarEstoqueUseCase {

    private final BuscaEstoquePorIdProdutoUseCase buscaEstoquePorIdProdutoUseCase;
    private final StockGateway stockGateway;

    public Stock execute(Stock stockBaixa) {

        var existingStockOp = buscaEstoquePorIdProdutoUseCase.execute(stockBaixa.getProduto().getId());

        if (existingStockOp.isEmpty()) {
            log.error("Não foi encontrado estoque para o produto {}", stockBaixa.getProduto().getId());
            throw new ResourceNotFoundException("Produto com ID " + stockBaixa.getProduto().getId());
        }

        var stockExistente = existingStockOp.get();
        stockExistente.baixarQuantidade(stockBaixa.getQuantidade());
        
        return stockGateway.salvar(stockExistente);
    }

    @Override
    public Stock baixarEstoque(Stock stockBaixa) {
        return null;
    }
}
