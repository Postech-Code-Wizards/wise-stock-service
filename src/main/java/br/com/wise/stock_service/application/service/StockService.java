package br.com.wise.stock_service.application.service;

import br.com.wise.stock_service.application.usecase.BuscaEstoquePorIdProdutoUseCase;
import br.com.wise.stock_service.application.usecase.EnviaRespostaRabbitUseCase;
import br.com.wise.stock_service.application.usecase.SalvarStockUseCase;
import br.com.wise.stock_service.converter.StockConverter;
import br.com.wise.stock_service.domain.Stock;
import br.com.wise.stock_service.infrastructure.configuration.RabbitMQConfig;
import br.com.wise.stock_service.infrastructure.rest.dto.request.QuantidadeRequest;
import br.com.wise.stock_service.infrastructure.rest.dto.request.StockRequestMessage;
import br.com.wise.stock_service.infrastructure.rest.dto.response.StockResponse;
import br.com.wise.stock_service.infrastructure.rest.dto.response.StockResponseMessage;
import br.com.wise.stock_service.infrastructure.rest.exception.ResourceNotFoundException;
import br.com.wise.stock_service.infrastructure.rest.exception.StockNegativeException;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockConverter stockConverter;
    private final BuscaEstoquePorIdProdutoUseCase buscaEstoquePorIdProdutoUseCase;
    private final SalvarStockUseCase salvarStockUseCase;
    private final EnviaRespostaRabbitUseCase enviaRespostaRabbitUseCase;

    public StockResponse verificaQuantidade(Long produtoId) {
        Optional<Stock> stock = Optional.ofNullable(buscaEstoquePorIdProdutoUseCase.execute(produtoId)
                .orElseThrow(() -> new ResourceNotFoundException("Estoque")));
        return stockConverter.toResponse(stock);
    }

    public StockResponse reporQuantidade(Long produtoId, QuantidadeRequest quantidadeRequest) {
        return buscaEstoquePorIdProdutoUseCase.execute(produtoId)
                .map(existing -> {
                    Integer novaQuantidade = existing.getQuantidade() + quantidadeRequest.getQuantidade();
                    var atualizado = stockConverter.toDomain(existing, novaQuantidade);
                    var salvo = salvarStockUseCase.execute(atualizado);
                    return stockConverter.toResponse(salvo);
                })
                .orElseGet(() -> {
                    var novo = stockConverter.toDomain(produtoId, quantidadeRequest);
                    var salvo = salvarStockUseCase.execute(novo);
                    return stockConverter.toResponse(salvo);
                });
    }

    public StockResponse baixaQuantidade(Long produtoId, QuantidadeRequest quantidadeRequest) {
        return buscaEstoquePorIdProdutoUseCase.execute(produtoId)
                .map(existing -> {
                    int novaQuantidade = existing.getQuantidade() - quantidadeRequest.getQuantidade();

                    if (novaQuantidade < 0) throw new StockNegativeException("");

                    var atualizado = stockConverter.toDomain(existing, novaQuantidade);
                    var salvo = salvarStockUseCase.execute(atualizado);
                    return stockConverter.toResponse(salvo);
                }).orElseThrow(() -> new ResourceNotFoundException("Produto com ID " + produtoId));
    }

    @RabbitListener(queues = RabbitMQConfig.STOCK_BAIXA_QUEUE)
    public void baixaQuantidadeRabbit(List<StockRequestMessage> messages) {
        if (messages == null || messages.isEmpty()) return;

        Long pedidoId = messages.getFirst().getPedidoId();

        boolean negativo = messages.stream().anyMatch(msg -> {
            var optEstoque = buscaEstoquePorIdProdutoUseCase.execute(msg.getProdutoId());
            if (optEstoque.isEmpty()) return true;

            int atual = optEstoque.get().getQuantidade();
            return (atual - msg.getQuantidade()) < 0;
        });

        if (negativo) {
            enviaRespostaRabbitUseCase.enviar(new StockResponseMessage(pedidoId, false));
            return;
        }

        for (StockRequestMessage msg : messages) {
            try {
                baixaQuantidade(msg.getProdutoId(), new QuantidadeRequest(msg.getQuantidade()));
            } catch (Exception ex) {
                enviaRespostaRabbitUseCase.enviar(new StockResponseMessage(pedidoId, false));
                return;
            }
        }

        enviaRespostaRabbitUseCase.enviar(new StockResponseMessage(pedidoId, true));
    }

    @RabbitListener(queues = RabbitMQConfig.STOCK_REPOR_QUEUE)
    public void reporQuantidadeRabbit(List<StockRequestMessage> messages) {
        for (StockRequestMessage msg : messages) {
            reporQuantidade(msg.getProdutoId(), new QuantidadeRequest(msg.getQuantidade()));
        }

    }

}
