package br.com.wise.stock_service.application.usecase.processor;

import br.com.wise.stock_service.application.usecase.BaixarEstoqueUseCase;
import br.com.wise.stock_service.domain.Stock;
import br.com.wise.stock_service.gateway.database.StockGateway;
import br.com.wise.stock_service.gateway.queue.QueueGateway;
import br.com.wise.stock_service.infrastructure.rest.dto.request.StockItens;
import br.com.wise.stock_service.infrastructure.rest.exception.StockNegativeException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BaixarEstoqueEmLoteUseCase extends BaixarEstoqueUseCase {

    private final StockGateway stockGateway;
    private final BuscaEstoquePorIdProdutoUseCase buscaEstoquePorIdProdutoUseCase;
    private final QueueGateway queueGateway;

    @Transactional
    public void execute(List<StockItens> stockItens) {

        Long pedidoId = stockItens.getFirst().getPedidoId();
        List<Stock> estoques = new ArrayList<>();

        try {

            for (StockItens item : stockItens) {
                Optional<Stock> optEstoque = buscaEstoquePorIdProdutoUseCase.execute(item.getProdutoId());
                Stock stockExistente = optEstoque.get();

                stockExistente.baixarQuantidade(item.getQuantidade());
                estoques.add(stockExistente);
            }

            for (Stock stock : estoques) {
                stockGateway.salvar(stock);
            }

            queueGateway.notificarSucesso(pedidoId);

        } catch (StockNegativeException ex) {
            queueGateway.notificarFalha(pedidoId);
        }

    }

    @Override
    public void baixarEstoqueEmLote(List<StockItens> items) {

    }
}
